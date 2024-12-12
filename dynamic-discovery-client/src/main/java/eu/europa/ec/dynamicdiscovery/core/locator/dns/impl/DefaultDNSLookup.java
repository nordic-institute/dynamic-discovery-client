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
package eu.europa.ec.dynamicdiscovery.core.locator.dns.impl;

import eu.europa.ec.dynamicdiscovery.core.locator.dns.IDNSLookup;
import eu.europa.ec.dynamicdiscovery.enums.DNSLookupType;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.SMPExceptionCode;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.NAPTRRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import java.util.*;

import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.startsWithIgnoreCase;

/**
 * @author Flávio W. R. Santos
 */
public class DefaultDNSLookup implements IDNSLookup {
    static final Logger LOG = LoggerFactory.getLogger(DefaultDNSLookup.class);
    final List<String> requiredURLSchemas;
    final List<String> requiredNaptrServices;
    final List<String> requiredNaptrFlagsList;
    final Map<String, String> httpBindingForNaptrServices = new HashMap<>();


    protected DefaultDNSLookup(Builder builder) {
        this.requiredURLSchemas = new ArrayList<>(builder.requiredURLSchemas);
        this.requiredNaptrServices = new ArrayList<>(builder.requiredNaptrServices);
        this.requiredNaptrFlagsList = new ArrayList<>(builder.requiredNaptrFlagsList);
        this.httpBindingForNaptrServices.putAll(builder.httpBindingForNaptrServices);
    }


    public String getURLFromNaptrRecord(List<Record> records,
                                        List<String> services,
                                        List<String> schemas,
                                        List<String> flagsList) {

        // respect the order of the services
        for (String service : services) {
            NAPTRRecord naptrRecord = (NAPTRRecord) records.stream()
                    .filter(r -> StringUtils.equalsIgnoreCase(((NAPTRRecord) r).getService(), service))
                    .findFirst()
                    .orElse(null);
            if (naptrRecord == null) {
                continue;
            }
            String recordDescription = naptrRecord.rdataToString();
            if (!validNaptrFlags(naptrRecord.getFlags(), flagsList)) {
                LOG.debug("NAPTR Record: [{}] does not have any of required flag [{}].", recordDescription, flagsList);
                continue;
            }
            String smpAddress = resolveNaptrValue(naptrRecord.getRegexp(), StringUtils.removeEnd(naptrRecord.getName().toString(), "."));
            if (validURLSchema(smpAddress, schemas)) {
                return updateURLWithHttpBinding(smpAddress, service);
            }

        }
        LOG.warn("No NAPTR Record found for services: [{}], schemas: [{}], flags: [{}].",
                services, schemas, flagsList);
        return null;
    }

    /**
     * Updates the URL with the HTTP Binding if defined in httpBindingForNaptrServices
     *
     * @param url     the URL string address
     * @param service the service name to be used as key to find the HTTP Binding
     * @return the updated URL
     */
    protected String updateURLWithHttpBinding(String url, String service) {
        if (!httpBindingForNaptrServices.containsKey(service)) {
            LOG.debug("No HTTP Binding found for service: [{}].", service);
            return url;
        }

        String httpBinding = httpBindingForNaptrServices.get(service);
        if (StringUtils.isNotBlank(httpBinding) && !url.endsWith(httpBinding)) {
            return concatenatePathSegment(url, httpBinding);
        }
        return url;
    }

    /**
     * Concatenates the URL with the HTTP Binding if necessary.
     *
     * @param url         the URL
     * @param httpBinding the HTTP Binding
     * @return the concatenated URL
     */
    protected String concatenatePathSegment(String url, String httpBinding) {

        if (StringUtils.endsWithIgnoreCase(url, "/")) {
            return url + StringUtils.removeStart(httpBinding, "/");
        }
        if (!StringUtils.startsWithIgnoreCase(httpBinding, "/")) {
            return url + "/" + httpBinding;
        }
        return url + httpBinding;
    }

    public String resolveNaptrValue(String recordValue, String hostname) {
        String[] split = StringUtils.split(recordValue, "!");
        if (split.length != 2) {
            LOG.warn("Parse NAPTR Record value: [{}] does not have 2 parts separated by character '!'.", recordValue);
            return null;
        }
        String regExp = split[0];
        String value = split[1];
        // Fast parse (used for U-NAPTR and the legacy '^.*$'
        if (StringUtils.equalsAny(regExp, ".*", "^.*$"))
            return value;
        // Using regex
        return hostname.replaceAll(regExp, value);
    }

    protected boolean validURLSchema(String url, List<String> requiredSchemas) {
        return requiredSchemas.stream()
                .anyMatch(schema -> startsWithIgnoreCase(url, schema));
    }

    protected boolean validNaptrService(String service, List<String> requiredServices) {
        return requiredServices.stream()
                .anyMatch(targetService -> equalsIgnoreCase(service, targetService));
    }

    protected boolean validNaptrFlags(String flags, List<String> requiredFlagsList) {
        return requiredFlagsList.stream()
                .anyMatch(targetFlags -> equalsIgnoreCase(flags, targetFlags));
    }

    @Override
    public String naptrUrlValueLookup(SMPParticipantIdentifier participantIdentifier, String uri) throws TechnicalException {
        List<Record> records = getAllNaptrRecords(participantIdentifier, uri);
        return getURLFromNaptrRecord(records, requiredNaptrServices, requiredURLSchemas, requiredNaptrFlagsList);
    }

    @Override
    public boolean dnsRecordNotExists(SMPParticipantIdentifier participantIdentifier, String participantURI, DNSLookupType type) throws TechnicalException {
        return getAllRecordsForType(participantIdentifier, participantURI, type).isEmpty();
    }

    @Override
    public List<Record> getAllNaptrRecords(SMPParticipantIdentifier participantIdentifier, String uri) throws TechnicalException {
        return getAllRecordsForType(participantIdentifier, uri, DNSLookupType.NAPTR);
    }

    @Override
    public List<Record> getAllCNameRecords(SMPParticipantIdentifier participantIdentifier, String uri) throws TechnicalException {
        return getAllRecordsForType(participantIdentifier, uri, DNSLookupType.CNAME);
    }

    public List<String> getRequiredURLSchemas() {
        return requiredURLSchemas;
    }

    public void setRequiredURLSchemas(List<String> requiredURLSchemas) {
        this.requiredURLSchemas.clear();
        this.requiredURLSchemas.addAll(requiredURLSchemas);
    }

    public List<String> getRequiredNaptrServices() {
        return requiredNaptrServices;
    }

    public void setRequiredNaptrServices(List<String> requiredNaptrServices) {
        this.requiredNaptrServices.clear();
        this.requiredNaptrServices.addAll(requiredNaptrServices);
    }

    @Override
    public List<Record> getAllRecordsForType(SMPParticipantIdentifier participantIdentifier, String uri, DNSLookupType recordType) throws TechnicalException {

        int dnsType;
        switch (recordType) {
            case CNAME:
                dnsType = Type.CNAME;
                break;
            case NAPTR:
                dnsType = Type.NAPTR;
                break;
            default:
                throw new DNSLookupException("The type: [" + recordType + "] is not supported!");
        }

        Record[] records;
        Lookup lookupClient;
        try {
            lookupClient = new Lookup(uri, dnsType);
            lookupClient.setCache(null);
            records = lookupClient.run();

        } catch (Exception exc) {
            throw new DNSLookupException(exc.getMessage(), exc);
        }

        if (lookupClient.getResult() == Lookup.HOST_NOT_FOUND) {
            LOG.debug("The DNS domain [{}] for participant [{}] was not found.", uri, participantIdentifier);
            return Collections.emptyList();
        } else if (lookupClient.getResult() == Lookup.TYPE_NOT_FOUND) {
            LOG.debug("The DNS domain [{}] for participant [{}] exists, but not for required type [{}].",
                    uri, participantIdentifier, recordType);
            return Collections.emptyList();
        }
        if (lookupClient.getResult() != Lookup.SUCCESSFUL) {
            throw new DNSLookupException(SMPExceptionCode.INVALID_DNS_TYPE, "Lookup [" + recordType + "] for participant [" + participantIdentifier
                    + " ] has failed. Lookup result CODE [ " + lookupClient.getResult() + " ]");
        }
        return Arrays.asList(records);

    }


    public static class Builder {

        static final List<String> DEFAULT_SCHEMAS = Arrays.asList("http:", "https:");
        static final List<String> DEFAULT_NAPTR_SERVICES = Collections.singletonList("Meta:SMP");

        static final List<String> DEFAULT_NAPTR_FLAGS = Collections.singletonList("U");
        List<String> requiredURLSchemas = new ArrayList<>();
        List<String> requiredNaptrServices = new ArrayList<>();
        List<String> requiredNaptrFlagsList = new ArrayList<>();
        final Map<String, String> httpBindingForNaptrServices = new HashMap<>();

        public DefaultDNSLookup.Builder addRequiredNaptrURLSchema(String schema) {
            this.requiredURLSchemas.add(schema);
            return this;
        }

        public DefaultDNSLookup.Builder addRequiredNaptrURLSchemas(List<String> schemas) {
            this.requiredURLSchemas.addAll(schemas);
            return this;
        }

        public DefaultDNSLookup.Builder addRequiredNaptrService(String service) {
            this.requiredNaptrServices.add(service);
            return this;
        }

        public DefaultDNSLookup.Builder addRequiredNaptrServiceHttpBinding(String service, String httpBinding) {
            httpBindingForNaptrServices.put(service, httpBinding);
            return this;
        }

        public DefaultDNSLookup.Builder addRequiredNaptrServices(List<String> service) {
            this.requiredNaptrServices.addAll(service);
            return this;
        }

        public DefaultDNSLookup.Builder addRequiredNaptrFlags(String flag) {
            this.requiredNaptrFlagsList.add(flag);
            return this;
        }

        public DefaultDNSLookup.Builder addRequiredNaptrFlagsList(List<String> flags) {
            this.requiredNaptrFlagsList.addAll(flags);
            return this;
        }

        public DefaultDNSLookup build() {
            validate();
            return new DefaultDNSLookup(this);
        }

        private void validate() {
            if (requiredNaptrServices.isEmpty()) {
                requiredNaptrServices.addAll(DEFAULT_NAPTR_SERVICES);
            }
            if (requiredURLSchemas.isEmpty()) {
                requiredURLSchemas.addAll(DEFAULT_SCHEMAS);
            }

            if (requiredNaptrFlagsList.isEmpty()) {
                requiredNaptrFlagsList.addAll(DEFAULT_NAPTR_FLAGS);
            }
        }
    }
}
