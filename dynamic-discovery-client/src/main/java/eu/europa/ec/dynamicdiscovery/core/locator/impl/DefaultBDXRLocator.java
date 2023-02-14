/*
 * (C) Copyright 2016-2021 - European Commission | Dynamic Discovery Client
 *
 * https://ec.europa.eu/cefdigital/code/projects/EDELIVERY/repos/dynamic-discovery-client/browse
 *
 * Licensed under the LGPL, Version 2.1 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     dynamic-discovery\License_LGPL-2.1.txt or https://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.europa.ec.dynamicdiscovery.core.locator.impl;

import eu.europa.ec.dynamicdiscovery.core.locator.IMetadataLocator;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.IDNSLookup;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.impl.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.enums.DNSLookupHashType;
import eu.europa.ec.dynamicdiscovery.enums.DNSLookupType;
import eu.europa.ec.dynamicdiscovery.exception.DDCRuntimeException;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.identifiers.ParticipantIdentifierFormatter;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Flávio W. R. Santos
 * @since 1.0
 */
public class DefaultBDXRLocator implements IMetadataLocator {

    ParticipantIdentifierFormatter participantIdentifierFormatter = new ParticipantIdentifierFormatter();
    private static final String PEPPOL_URL_SCHEME = "http://";
    private static final String DOMAIN_SEPARATOR = ".";

    static final Logger LOG = LoggerFactory.getLogger(DefaultBDXRLocator.class);
    private List<String> topDnsDomains;
    private List<DNSLookupType> dnsLookupTypeList = new ArrayList<>(Arrays.asList(DNSLookupType.NAPTR, DNSLookupType.CNAME));

    private IDNSLookup dnsLookup;

    private DefaultBDXRLocator(Builder builder) {
        this.topDnsDomains = new ArrayList<>(builder.topDnsDomains);
        this.dnsLookupTypeList = new ArrayList<>(builder.dnsLookupTypeList);
        this.dnsLookup = builder.dnsLookup;

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
     * Method validates record by record until
     *
     * @param participantIdentifier
     * @return
     * @throws TechnicalException
     */
    @Override
    public URI lookup(SMPParticipantIdentifier participantIdentifier) throws TechnicalException {

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
    public URI lookup(String participantIdentifier, String participantScheme) throws TechnicalException {
        return this.lookup(participantIdentifierFormatter.normalize(participantScheme, participantIdentifier));
    }

    protected URI cnameLookup(SMPParticipantIdentifier participantIdentifier, String topDomain) throws TechnicalException {
        String dnsDomain = buildCNameDNSDomain(participantIdentifier, topDomain);
        if (getDnsLookup().dnsRecordNotExists(participantIdentifier, dnsDomain, DNSLookupType.CNAME)) {
            return null;
        }

        try {
            return new URI(PEPPOL_URL_SCHEME + buildCNameDNSDomain(participantIdentifier, topDomain));
        } catch (URISyntaxException exc) {
            throw new DNSLookupException(exc.getMessage(), exc);
        }
    }

    private URI naptrLookup(SMPParticipantIdentifier participantIdentifier, String topDomain) throws TechnicalException {
        try {
            LOG.debug("Start naptr search for participant [{}].", participantIdentifier);
            String naptrURI = buildNaptrDNSDomain(participantIdentifier, topDomain);
            String smpURI = naptrLookupFetcher(participantIdentifier, naptrURI);
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

    protected String buildCNameDNSDomain(SMPParticipantIdentifier participantIdentifier, String topDomain) {

        StringBuilder sb = new StringBuilder();
        sb.append(participantIdentifierFormatter.dnsLookupFormat(participantIdentifier, DNSLookupHashType.MD5_HEX));
        sb.append(DOMAIN_SEPARATOR)
                .append(topDomain);
        return sb.toString();
    }

    protected String buildNaptrDNSDomain(SMPParticipantIdentifier participantIdentifier, String topDomain) {

        StringBuilder sb = new StringBuilder();
        sb.append(participantIdentifierFormatter.dnsLookupFormat(participantIdentifier, DNSLookupHashType.SHA256_BASE32));
        sb.append(DOMAIN_SEPARATOR)
                .append(topDomain);
        return sb.toString();
    }

    public String naptrLookupFetcher(SMPParticipantIdentifier participantIdentifier, String participantURI) throws TechnicalException {
        return getDnsLookup().naptrUrlValueLookup(participantIdentifier, participantURI);
    }

    @Override
    public IDNSLookup getDnsLookup() {
        return dnsLookup;
    }

    public static class Builder {

        static final List<DNSLookupType> DEFAULT_LOOKUPS = new ArrayList<>(Arrays.asList(DNSLookupType.NAPTR, DNSLookupType.CNAME));
        private List<String> topDnsDomains = new ArrayList<>();
        private List<DNSLookupType> dnsLookupTypeList = new ArrayList<>();
        private IDNSLookup dnsLookup;

        public Builder addDnsLookupType(DNSLookupType recordType) {
            this.dnsLookupTypeList.add(recordType);
            return this;
        }

        public Builder addDnsLookupTypes(List<DNSLookupType> recordTypes) {
            this.dnsLookupTypeList.addAll(recordTypes);
            return this;
        }

        public Builder addTopDnsDomain(String domain) {
            this.topDnsDomains.add(domain);
            return this;
        }

        public Builder addTopDnsDomains(List<String> domains) {
            this.topDnsDomains.addAll(domains);
            return this;
        }

        public Builder dnsLookup(IDNSLookup dnsLookup) {
            this.dnsLookup = dnsLookup;
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
