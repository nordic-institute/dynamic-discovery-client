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
import eu.europa.ec.dynamicdiscovery.core.locator.PublisherLookupResult;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.IDNSLookup;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.impl.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.enums.DNSLookupHashType;
import eu.europa.ec.dynamicdiscovery.enums.DNSLookupType;
import eu.europa.ec.dynamicdiscovery.exception.*;
import eu.europa.ec.dynamicdiscovery.model.identifiers.ParticipantIdentifierFormatter;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The BDXR locator implementation. The locator implementations is based on
 * the BDXR specification and eDelivery BDXR 2.0 profile. It uses DNS NAPTR (and legacy CNAME)
 * records to locate the metadata for the given participant identifier.
 * In case of the CNAME record, the URL is created from.
 *
 * @author Flávio W. R. SANTOS
 * @author Joze RIHTARSIC
 * @since 1.0
 */
public class DefaultBDXRLocator implements IPublisherLocator {

    ParticipantIdentifierFormatter resourceIdentifierFormatter;
    private static final String DOMAIN_SEPARATOR = ".";

    static final Logger LOG = LoggerFactory.getLogger(DefaultBDXRLocator.class);
    private final List<String> topDnsDomains;
    private List<DNSLookupType> dnsLookupTypeList = new ArrayList<>(Arrays.asList(DNSLookupType.NAPTR, DNSLookupType.CNAME));
    private String cnameURLScheme = "http";
    private String cnameURLContext = "/";
    private final IDNSLookup dnsLookup;

    private DefaultBDXRLocator(Builder builder) {
        this.topDnsDomains = new ArrayList<>(builder.topDnsDomains);
        this.dnsLookupTypeList = new ArrayList<>(builder.dnsLookupTypeList);
        this.dnsLookup = builder.dnsLookup;

        if (builder.resourceIdentifierFormatter == null) {
            this.resourceIdentifierFormatter = new ParticipantIdentifierFormatter.Builder()
                    .initDefault()
                    .build();
        } else {
            this.resourceIdentifierFormatter = builder.resourceIdentifierFormatter;
        }

        if (builder.resourceSchemeMandatory != null) {
            this.resourceIdentifierFormatter.setSchemeMandatory(builder.resourceSchemeMandatory);
        }

        if (builder.resourceSchemeValidationPattern != null) {
            this.resourceIdentifierFormatter.setSchemeValidationPattern(builder.resourceSchemeValidationPattern);
        }

        if (!builder.resourceCaseSensitiveSchemas.isEmpty()) {
            this.resourceIdentifierFormatter.setCaseSensitiveSchemas(builder.resourceCaseSensitiveSchemas);
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

    public DefaultBDXRLocator(List<String> domains) {
        this(domains, new DefaultDNSLookup.Builder().build());
    }

    public DefaultBDXRLocator(String domain, IDNSLookup dnsLookup) {
        this(Collections.singletonList(domain), dnsLookup);
    }

    public DefaultBDXRLocator(List<String> domains, IDNSLookup dnsLookup) {
        this.topDnsDomains = domains;
        this.dnsLookup = dnsLookup;
    }

    public List<String> getTopDnsDomains() {
        return topDnsDomains;
    }

    public List<DNSLookupType> getDnsLookupTypeList() {
        return dnsLookupTypeList;
    }

    /**
     * Returns the resource identifier formatter which is responsible for
     * normalizing the resource/participant identifier when generating the DNS query.
     *
     * @return The resource identifier formatter
     */
    public ParticipantIdentifierFormatter getResourceIdentifierFormatter() {
        return resourceIdentifierFormatter;
    }

    /**
     * Sets the resource identifier formatter. The formatter is used to normalize the resource/participant identifier before
     * generating the resource hash value and the DNS query. The formatter contains a list of formatter types implementations such as
     * {@link eu.europa.ec.dynamicdiscovery.model.identifiers.types.EBCorePartyIdFormatterType}
     * and @link {@link eu.europa.ec.dynamicdiscovery.model.identifiers.types.OasisSMPFormatterType}.
     *
     * @param resourceIdentifierFormatter The resource identifier formatter to be used
     */
    public void setResourceIdentifierFormatter(ParticipantIdentifierFormatter resourceIdentifierFormatter) {
        this.resourceIdentifierFormatter = resourceIdentifierFormatter;
    }

    /**
     * This method performs a DNS lookup to retrieve the SMP addresses for the specified participant identifier.
     * It returns the first result(s) found for each top domain and DNS record type.
     * Note: for the same top domain and NAPTR record type, multiple results can be
     * returned with different NAPTR services.
     * The method returns just the first successful result, thus it does not iterate
     * over all possible options to optimize performance by reducing the number of lookup queries.
     * <br />
     * The lookup is ordered by the order of list of top domains followed by the  ordered list
     * of DNS record type. The first successful result is returned.
     *
     * @param participantIdentifier The target resource/participant identifier to discover the SMP's URL address
     * @return The list of MetadataLocatorResult for the participant identifier or empty list if no DNS record exist.
     * @throws TechnicalException if an error occurs during the DNS lookup
     */
    public List<PublisherLookupResult> lookupPrivate(SMPParticipantIdentifier participantIdentifier) throws TechnicalException {

        List<PublisherLookupResult> results = new ArrayList<>();

        for (String domain : topDnsDomains) {
            for (DNSLookupType type : dnsLookupTypeList) {
                List<PublisherLookupResult> resultForTopDomain = dnsLookupForTopDomain(participantIdentifier, domain, type);
                if (!resultForTopDomain.isEmpty()) {
                    results.addAll(resultForTopDomain);
                    return results;
                }
            }
        }
        return results;
    }


    /**
     * This method is used to lookup the DNS record for the given participant identifier and top domain. It generates the DNS query
     * based on the DNS record type.
     *
     * @param identifier The target resource/participant identifier to discover the SMP's URL address
     * @param topDomain  The SML DNS top domain to be used for the DNS query
     * @param lookupType The DNS record type to be used for the DNS query (NAPTR or CNAME)
     * @return The list of MetadataLocatorResult for the participant identifier or empty list if no DNS record exist.
     * @throws TechnicalException if an error occurs during the DNS lookup
     */
    private List<PublisherLookupResult> dnsLookupForTopDomain(SMPParticipantIdentifier identifier, String topDomain, DNSLookupType lookupType) throws TechnicalException {
        if (identifier == null) {
            throw new DDCInvalidDataException("Participant identifier must not be null");
        }

        if (StringUtils.isBlank(topDomain)) {
            throw new DDCInvalidDataException("DNS top domain must not be blank");
        }

        switch (lookupType) {
            case NAPTR:
                return naptrLookup(identifier, topDomain);
            case CNAME:
                return cnameLookup(identifier, topDomain);
            default:
                throw new DNSLookupException("DNS record type [" + lookupType + "] is not supported!");
        }

    }

    /**
     * This method is used to lookup the SMPs "addresses" for the given participant identifier.
     *
     * @param participantIdentifier The target resource/participant identifier to discover the SMP's URL address
     * @return The list of MetadataLocatorResult for the participant identifier or empty list if no DNS record exist.
     * @throws TechnicalException if an error occurs during the DNS lookup
     */
    @Override
    public List<PublisherLookupResult> lookup(SMPParticipantIdentifier participantIdentifier) throws TechnicalException {
        try {
            return this.lookupPrivate(resourceIdentifierFormatter.normalize(participantIdentifier));
        } catch (TechnicalException e) {
            e.setSmpExceptionCode(DDCExceptionCode.SERVICE_GROUP);
            throw e;
        }
    }

    /**
     * This is a legacy method that generates a DNS domain based on the "CNAME record" rules for a participant identifier.
     * The method is deprecated and NAPTR record should be used instead.
     *
     * @param participantIdentifier the participant identifier
     * @param topDomain             the top domain
     * @return the singleton list of the MetadataLocatorResult for the participant identifier or empty list if the DNS record does not exist
     * @throws TechnicalException if an error occurs during the URI creation
     */
    @Deprecated
    protected List<PublisherLookupResult> cnameLookup(SMPParticipantIdentifier participantIdentifier, String topDomain) throws TechnicalException {
        String dnsQuery = buildCNameDNSQuery(participantIdentifier, topDomain);
        if (!getDnsLookup().dnsRecordExists(participantIdentifier, dnsQuery, DNSLookupType.CNAME)) {
            return Collections.emptyList();
        }

        try {
            URI uri = new URI(cnameURLScheme + "://" + dnsQuery + StringUtils.prependIfMissing(cnameURLContext, "/"));
            return Collections.singletonList(new PublisherLookupResult(participantIdentifier, uri, null, DNSLookupType.CNAME));
        } catch (URISyntaxException exc) {
            throw new DNSLookupException(exc.getMessage(), exc);
        }
    }

    private List<PublisherLookupResult> naptrLookup(SMPParticipantIdentifier participantIdentifier, String topDomain) throws TechnicalException {
        LOG.debug("Start naptr search for participant [{}].", participantIdentifier);
        try {
            String naptrURI = buildNaptrDNSQuery(participantIdentifier, topDomain);
            List<PublisherLookupResult> results = naptrLookupForDomain(participantIdentifier, naptrURI);
            LOG.debug("Got DNS results: [{}] for participant [{}] with naptr query url: [{}].", results, participantIdentifier, naptrURI);
            return results;
        } catch (TechnicalException | NullPointerException exc) {
            LOG.debug("Naptr lookup was not possible, CNAME lookup will be used instead for participant [{}]", participantIdentifier);
            //It was not possible to lookup using NAPTR, CNAME lookup will be used instead
            return Collections.emptyList();
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

    public List<PublisherLookupResult> naptrLookupForDomain(SMPParticipantIdentifier participantIdentifier, String participantURI) throws TechnicalException {
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
        private String cnameURLScheme = "http";
        private String cnameURLContext = "/";
        private ParticipantIdentifierFormatter resourceIdentifierFormatter;


        /**
         * Add a DNS record type which can be used with the BDXR locator.
         *
         * @param recordType The DNS record type (Currently supported: NAPTR, CNAME)
         * @return The builder instance
         */
        public Builder addDnsLookupType(DNSLookupType recordType) {
            this.dnsLookupTypeList.add(recordType);
            return this;
        }

        /**
         * Add a list of DNS record types which can be used with the BDXR locator.
         *
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
         *
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
         *
         * @param resourceSchemeMandatory The participant scheme mandatory flag
         * @return The builder instance
         */
        public Builder resourceSchemeMandatory(Boolean resourceSchemeMandatory) {
            this.resourceSchemeMandatory = resourceSchemeMandatory;
            return this;
        }

        /**
         * Sets the wildcard enabled flag. If set to true, the locator will accept participant identifiers with a wildcard.
         *
         * @param resourceWildcardEnabled The wildcard enabled flag
         * @return The builder instance
         * @deprecated The wildcard support is not specified in eDelivery profiles and support will be removed in the future.
         */
        @Deprecated
        public Builder resourceWildcardEnabled(Boolean resourceWildcardEnabled) {
            this.resourceWildcardEnabled = resourceWildcardEnabled;
            return this;
        }

        public Builder resourceIdentifierFormatter(ParticipantIdentifierFormatter resourceIdentifierFormatter) {
            this.resourceIdentifierFormatter = resourceIdentifierFormatter;
            return this;
        }

        /**
         * Sets the resource/participant scheme validation pattern. The locator will use this pattern to validate the scheme of the participant identifier.
         *
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
         *
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
         *
         * @param schemes The case-sensitive schema
         * @return The builder instance
         */
        public Builder addResourceCaseSensitiveSchemas(List<String> schemes) {
            this.resourceCaseSensitiveSchemas.addAll(schemes);
            return this;
        }

        /**
         * Sets the CNAME URL scheme. The locator will use this scheme to create the SMPs URL for the participant identifier.
         *
         * @param scheme The CNAME URL scheme   (default: http)
         * @return The builder instance
         * @deprecated The CNAME support is not specified in eDelivery profiles and support will be removed in the future.
         */
        @Deprecated
        public Builder cnameURLScheme(String scheme) {
            this.cnameURLScheme = scheme;
            return this;
        }

        /**
         * Sets the CNAME URL context. The locator will use this context to create the SMPs URL for the participant identifier.
         *
         * @param context The CNAME URL context (default: /)
         * @return The builder instance
         * @deprecated The CNAME support is not specified in eDelivery profiles and support will be removed in the future.
         */
        @Deprecated
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

            if (resourceIdentifierFormatter == null) {
                resourceIdentifierFormatter = new ParticipantIdentifierFormatter.Builder().initDefault().build();
            }
        }
    }
}
