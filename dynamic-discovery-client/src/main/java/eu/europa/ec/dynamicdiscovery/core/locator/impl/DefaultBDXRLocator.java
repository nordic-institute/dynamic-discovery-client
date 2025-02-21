/*
 * #%L
 * dynamic-discovery-cli
 * %%
 * Copyright (C) 2016 - 2023 European Commission | eDelivery | Dynamic Discovery Client
 * %%
 * Licensed under the LGPL, Version 2.1 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * [PROJECT_HOME]\license\lgpl2-1\license.txt or https://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package eu.europa.ec.dynamicdiscovery.core.locator.impl;

import eu.europa.ec.dynamicdiscovery.core.locator.IPublisherLocator;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.IDNSLookup;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.impl.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.enums.DNSLookupHashType;
import eu.europa.ec.dynamicdiscovery.enums.DNSLookupType;
import eu.europa.ec.dynamicdiscovery.exception.DDCRuntimeException;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.DDCExceptionCode;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.identifiers.ParticipantIdentifierFormatter;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.types.FormatterType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The BDXL locator implementation. The locator implementations is based on
 * the BDXL specification and eDelivery BDXL 2.0 profile. It uses DNS NAPTR (and legacy CNAME)
 * records to locate the metadata for the given participant identifier.
 * In case of the CNAME record, the URL is created from.
 *
 * @author Flávio W. R. SANTOS
 * @author Joze RIHTARSIC
 * @since 1.0
 */
public class DefaultBDXRLocator implements IPublisherLocator {
    static final Logger LOG = LoggerFactory.getLogger(DefaultBDXRLocator.class);

    ParticipantIdentifierFormatter resourceIdentifierFormatter = new ParticipantIdentifierFormatter();
    private static final String DOMAIN_SEPARATOR = ".";


    private final List<String> topDnsDomains;
    private final List<DNSLookupType> dnsLookupTypeList;
    private String cnameURLScheme = "http";
    private String cnameURLContext = "/";

    private final IDNSLookup dnsLookup;

    private DefaultBDXRLocator(Builder builder) {
        this.topDnsDomains = new ArrayList<>(builder.topDnsDomains);
        this.dnsLookupTypeList = new ArrayList<>(builder.dnsLookupTypeList);
        this.dnsLookup = builder.dnsLookup;

        if (builder.resourceSchemeMandatory != null) {
            this.resourceIdentifierFormatter.setSchemeMandatory(builder.resourceSchemeMandatory);
        }

        if (builder.resourceSchemeValidationPattern != null) {
            this.resourceIdentifierFormatter.setSchemeValidationPattern(builder.resourceSchemeValidationPattern);
        }

        if (!builder.resourceCaseSensitiveSchemas.isEmpty()) {
            this.resourceIdentifierFormatter.setCaseSensitiveSchemas(builder.resourceCaseSensitiveSchemas);
        }

        if (!builder.resourceFormatterTypes.isEmpty()) {
            this.resourceIdentifierFormatter.setFormatterTypes(builder.resourceFormatterTypes);
        }

        if (builder.resourceWildcardEnabled != null) {
            this.resourceIdentifierFormatter.setWildcardEnabled(builder.resourceWildcardEnabled);
        }

        if (StringUtils.isNotEmpty(builder.cnameURLScheme)) {
            this.cnameURLScheme = builder.cnameURLScheme;
        }

        if (StringUtils.isNotEmpty(builder.cnameURLContext)) {
            this.cnameURLContext = builder.cnameURLContext;
        }
    }

    public List<String> getTopDnsDomains() {
        return topDnsDomains;
    }

    public List<DNSLookupType> getDnsLookupTypeList() {
        return dnsLookupTypeList;
    }

    /**
     * Method validates record by record until
     *
     * @param participantIdentifier
     * @return
     * @throws TechnicalException
     */
    public URI lookupPrivate(SMPParticipantIdentifier participantIdentifier) throws TechnicalException {

        for (String domain : topDnsDomains) {
            for (DNSLookupType type : dnsLookupTypeList) {
                URI participantIdentifierURI = getUrlForTopDomain(participantIdentifier, domain, type);
                if (participantIdentifierURI != null) {
                    return participantIdentifierURI;
                }
            }
        }
        return null;
    }

    /**
     * This method is used to lookup the DNS record for the given participant identifier and top domain. It generates the DNS query
     * based on the DNS record type.
     * @param identifier The target resource/participant identifier to discover the SMP's URL address
     * @param topDomain The SML DNS top domain to be used for the DNS query
     * @param lookupType The DNS record type to be used for the DNS query (NAPTR or CNAME)
     * @return The URL of the publisher
     * @throws TechnicalException if an error occurs during the DNS lookup
     */
    private URI getUrlForTopDomain(SMPParticipantIdentifier identifier, String topDomain, DNSLookupType lookupType) throws TechnicalException {
        switch (lookupType) {
            case NAPTR:
                return naptrLookup(identifier, topDomain);
            case CNAME:
                return cnameLookup(identifier, topDomain);
            default:
                throw new DNSLookupException("DNS record type [" + lookupType + "] is not supported!");
        }

    }

    @Override
    public URI lookup(SMPParticipantIdentifier participantIdentifier) throws TechnicalException {
        try {
            return this.lookupPrivate(resourceIdentifierFormatter.normalize(participantIdentifier));
        } catch (TechnicalException e) {
            e.setSmpExceptionCode(DDCExceptionCode.SERVICE_GROUP);
            throw e;
        }
    }

    @Override
    public URI lookup(String participantIdentifier, String participantScheme) throws TechnicalException {
        try {
            return this.lookupPrivate(resourceIdentifierFormatter.normalize(participantScheme, participantIdentifier));
        } catch (TechnicalException e) {
            e.setSmpExceptionCode(DDCExceptionCode.SERVICE_GROUP);
            throw e;
        }
    }

    protected URI cnameLookup(SMPParticipantIdentifier participantIdentifier, String topDomain) throws TechnicalException {
        String dnsQuery = buildCNameDNSQuery(participantIdentifier, topDomain);
        if (getDnsLookup().dnsRecordNotExists(participantIdentifier, dnsQuery, DNSLookupType.CNAME)) {
            return null;
        }

        try {
            return new URI(cnameURLScheme + "://" + dnsQuery + StringUtils.prependIfMissing(cnameURLContext, "/"));
        } catch (URISyntaxException exc) {
            throw new DNSLookupException(exc.getMessage(), exc);
        }
    }

    private URI naptrLookup(SMPParticipantIdentifier participantIdentifier, String topDomain) throws TechnicalException {
        try {
            LOG.debug("Start naptr search for participant [{}].", participantIdentifier);
            String naptrURI = buildNaptrDNSQuery(participantIdentifier, topDomain);
            String smpURI = naptrLookupForDomain(participantIdentifier, naptrURI);
            LOG.debug("Got URL: [{}] for participant [{}] with naptr query url: [{}].", smpURI, participantIdentifier, naptrURI);
            return new URI(smpURI);
        } catch (URISyntaxException exc) {
            throw new DNSLookupException(exc.getMessage(), exc);
        } catch (TechnicalException | NullPointerException exc) {
            LOG.debug("Naptr lookup was not possible, CNAME lookup will be used instead for participant [{}]", participantIdentifier);
            //It was not possible to lookup using NAPTR, CNAME lookup will be used instead
            return null;
        }
    }

    public String buildCNameDNSQuery(SMPParticipantIdentifier participantIdentifier, String topDomain) {

        StringBuilder sb = new StringBuilder();
        sb.append(resourceIdentifierFormatter.dnsLookupFormat(participantIdentifier, DNSLookupHashType.MD5_HEX));
        sb.append(DOMAIN_SEPARATOR)
                .append(topDomain);
        return sb.toString();
    }

    public String buildNaptrDNSQuery(SMPParticipantIdentifier participantIdentifier, String topDomain) {

        StringBuilder sb = new StringBuilder();
        sb.append(resourceIdentifierFormatter.dnsLookupFormat(participantIdentifier, DNSLookupHashType.SHA256_BASE32));
        sb.append(DOMAIN_SEPARATOR)
                .append(topDomain);
        return sb.toString();
    }

    public String naptrLookupForDomain(SMPParticipantIdentifier participantIdentifier, String participantURI) throws TechnicalException {
        return getDnsLookup().naptrUrlValueLookup(participantIdentifier, participantURI);
    }

    public IDNSLookup getDnsLookup() {
        return dnsLookup;
    }
    /**
     * Builder class for the BDXR locator.
     */
    public static class Builder {

        static final List<DNSLookupType> DEFAULT_LOOKUPS = new ArrayList<>(Arrays.asList(DNSLookupType.NAPTR, DNSLookupType.CNAME));
        private List<String> topDnsDomains = new ArrayList<>();
        private List<DNSLookupType> dnsLookupTypeList = new ArrayList<>();
        private IDNSLookup dnsLookup;
        private Boolean resourceSchemeMandatory;
        private Boolean resourceWildcardEnabled;
        private Pattern resourceSchemeValidationPattern;
        private List<String> resourceCaseSensitiveSchemas = new ArrayList<>();
        private List<FormatterType> resourceFormatterTypes = new ArrayList<>();
        private String cnameURLScheme = "http";
        private String cnameURLContext = "/";

        /**
         * Add a DNS record type which can be used with the BDXR locator.
         * @param recordType The DNS record type (Currently supported: NAPTR, CNAME)
         * @return The builder instance
         */
        public Builder addDnsLookupType(DNSLookupType recordType) {
            this.dnsLookupTypeList.add(recordType);
            return this;
        }

        /**
         * Add a list of DNS record types which can be used with the BDXR locator.
         * @param recordTypes The ordered list of DNS record types (Currently supported: NAPTR, CNAME)
         * @return The builder instance
         */
        public Builder addDnsLookupTypes(List<DNSLookupType> recordTypes) {
            this.dnsLookupTypeList.addAll(recordTypes);
            return this;
        }

        /**
         * Adds a top-level domain for use with the BDXR locator, The top-level domain is used to construct the DNS query.
         * For metadata retrieval, the locator will use the first top-level domain where the DNS record is found.
         * Therefore, the order in which records are added to the list is crucial.
         *
         * @param domain The top level domain.
         * @return The builder instance
         */
        public Builder addTopDnsDomain(String domain) {
            this.topDnsDomains.add(domain);
            return this;
        }

        /**
         * Adds a list of top-level domains for use with the BDXR locator. The top-level domain is used to construct the DNS query.
         * For metadata retrieval, the locator will use the first top-level domain where the DNS record is found.
         * Therefore, the order of topdomain in the list is important.
         *
         * @param domains The list of top level domains.
         * @return The builder instance
         */
        public Builder addTopDnsDomains(List<String> domains) {
            this.topDnsDomains.addAll(domains);
            return this;
        }

        /**
         * Sets the DNS lookup implementation of the IDNSLookup interface to be used with the BDXR locator.
         * @param dnsLookup
         * @return
         */
        public Builder dnsLookup(IDNSLookup dnsLookup) {
            this.dnsLookup = dnsLookup;
            return this;
        }

        /**
         * Sets the resource/participant scheme mandatory flag. If set to true, the locator will only accept resource/participant identifiers
         * with a scheme.
         * @param resourceSchemeMandatory The participant scheme mandatory flag
         * @return The builder instance
         */
        public Builder resourceSchemeMandatory(Boolean resourceSchemeMandatory) {
            this.resourceSchemeMandatory = resourceSchemeMandatory;
            return this;
        }

        /**
         * Sets the wildcard enabled flag. If set to true, the locator will accept participant identifiers with a wildcard.
         * @param resourceWildcardEnabled The wildcard enabled flag
         * @return The builder instance
         */
        public Builder resourceWildcardEnabled(Boolean resourceWildcardEnabled) {
            this.resourceWildcardEnabled = resourceWildcardEnabled;
            return this;
        }


        /**
         * Sets the resource/participant scheme validation pattern. The locator will use this pattern to validate the scheme of the participant identifier.
         * @param resourceSchemeValidationPattern The participant scheme validation pattern
         * @return The builder instance
         */
        public Builder resourceSchemeValidationPattern(Pattern resourceSchemeValidationPattern) {
            this.resourceSchemeValidationPattern = resourceSchemeValidationPattern;
            return this;
        }

        /**
         * Adds a case-sensitive schema to the list of case-sensitive schemas.
         * The locator will use this list to validate the scheme of the resource/participant identifier.
         * If the scheme is not in the list, the locator will set the identifier to lowercase before calling the hash value
         * for the DNS query.
         * @param scheme The case-sensitive schema
         * @return The builder instance
         */
        public Builder addResourceCaseSensitiveSchema(String scheme) {
            if (StringUtils.isNotEmpty(scheme)) {
                this.resourceCaseSensitiveSchemas.add(scheme);
            }
            return this;
        }

        /**
         * Adds a  list of case-sensitive schemas to the list of case-sensitive schemas.
         * The locator will use this list to validate the scheme of the resource/participant identifier.
         * If the scheme is not in the list, the locator will set the identifier to lowercase before calling the hash value
         * for the DNS query.
         * @param schemes The case-sensitive schema
         * @return The builder instance
         */
        public Builder addResourceCaseSensitiveSchemas(List<String> schemes) {
            this.resourceCaseSensitiveSchemas.addAll(schemes);
            return this;
        }

        /**
         * Adds a list of formatter types to the list of formatter types.
         * The client will format/normalize the resource/participant identifier before
         * lookup according to the formatter type. By default, ebCoreParty Identifier
         * type and Peppol Party identifier types are registered.
         *
         * @param resourceFormatterType The list of formatter types
         * @return The builder instance
         */
        public Builder addResourceFormatterType(FormatterType resourceFormatterType) {
            this.resourceFormatterTypes.add(resourceFormatterType);
            return this;
        }

        /**
         * Adds a list of formatter types to the list of formatter types.
         * The client will format/normalize the resource/participant identifier before
         * lookup according to the formatter type. By default, ebCoreParty Identifier
         * type and Peppol Party identifier types are registered.
         *
         * @param formatters The list of formatter types
         * @return The builder instance
         */
        public Builder addResourceFormatterTypes(List<FormatterType> formatters) {
            this.resourceFormatterTypes.addAll(formatters);
            return this;
        }

        /**
         * Sets the CNAME URL scheme. The locator will use this scheme to create the SMPs URL for the participant identifier.
         * @param scheme The CNAME URL scheme   (default: http)
         * @return The builder instance
         */
        public Builder cnameURLScheme(String scheme) {
            this.cnameURLScheme = scheme;
            return this;
        }

        /**
         * Sets the CNAME URL context. The locator will use this context to create the SMPs URL for the participant identifier.
         * @param context The CNAME URL context (default: /)
         * @return The builder instance
         */
        public Builder cnameURLContext(String context) {
            this.cnameURLContext = context;
            return this;
        }

        public DefaultBDXRLocator build() {
            validate();
            return new DefaultBDXRLocator(this);
        }

        private void validate() {
            if (topDnsDomains.isEmpty()) {
                throw new DDCRuntimeException("List of top domains must not be empty!");
            }
            if (dnsLookupTypeList.isEmpty()) {
                dnsLookupTypeList.addAll(DEFAULT_LOOKUPS);
            }
            if (dnsLookup == null) {
                dnsLookup = new DefaultDNSLookup.Builder().build();
            }
        }
    }


}
