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

import eu.europa.ec.dynamicdiscovery.core.locator.PublisherLookupResult;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.IDNSLookup;
import eu.europa.ec.dynamicdiscovery.enums.DNSLookupType;
import eu.europa.ec.dynamicdiscovery.exception.DDCExceptionCode;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.NAPTRRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import java.net.URI;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.startsWithIgnoreCase;

/**
 * Default implementation of the {@link IDNSLookup} interface.
 *
 * @author Flávio W. R. Santos
 * @author Joze RIHTARSIC
 * @since 1.0
 */
public class DefaultDNSLookup implements IDNSLookup {
    static final Logger LOG = LoggerFactory.getLogger(DefaultDNSLookup.class);
    public static final String NAPTR_SPLIT_REGEXP_CHAR = "!";
    public static final String NAPTR_REPLACE_REGEXP = ".*";
    public static final String NAPTR_REPLACE_REGEXP_LEGACY = "^.*$";
    final List<String> requiredURLSchemas;
    final List<String> requiredNaptrServices;
    final List<String> requiredNaptrFlags;

    protected DefaultDNSLookup(Builder builder) {
        this.requiredURLSchemas = new ArrayList<>(builder.requiredURLSchemas);
        this.requiredNaptrServices = new ArrayList<>(builder.requiredNaptrServices);
        this.requiredNaptrFlags = new ArrayList<>(builder.requiredNaptrFlags);
    }


    public List<PublisherLookupResult> getURLFromNaptrRecord(SMPParticipantIdentifier identifier,
                                                             List<Record> records,
                                                             List<String> requiredServices,
                                                             List<String> schemas,
                                                             List<String> flagsList) {
        List<PublisherLookupResult> result = requiredServices.isEmpty()?
                convertAllRecordsToResult(identifier, records, schemas, flagsList):
                convertRecordsWithRequiredServices(identifier, records, requiredServices, schemas, flagsList);
        if (result.isEmpty()){
            LOG.warn("No NAPTR Record found for services: [{}], schemas: [{}], flags: [{}].",
                    requiredServices, schemas, flagsList);
        }
        return result;
    }

    private List<PublisherLookupResult> convertRecordsWithRequiredServices(SMPParticipantIdentifier identifier, List<Record> records, List<String> requiredServices, List<String> schemas, List<String> flagsList) {
        List<PublisherLookupResult> result = new ArrayList<>();
        for (String service : requiredServices) {
            NAPTRRecord naptrRecord = (NAPTRRecord) records.stream()
                    .filter(r -> Strings.CI.equals(((NAPTRRecord) r).getService(), service))
                    .findFirst()
                    .orElse(null);
            if (naptrRecord == null) {
                continue;
            }
            URI uri = getURIFromNaptrRecord(schemas, flagsList, naptrRecord);
            if (uri == null) continue;
            result.add(new PublisherLookupResult(identifier, uri, service, DNSLookupType.NAPTR));
        }
        return result;
    }

    private URI getURIFromNaptrRecord(List<String> schemas, List<String> flagsList, NAPTRRecord naptrRecord) {
        String recordDescription = naptrRecord.rdataToString();
        if (!validNaptrFlags(naptrRecord.getFlags(), flagsList)) {
            LOG.debug("NAPTR Record: [{}] does not have any of required flag [{}].", recordDescription, flagsList);
            return null;
        }
        String smpAddress = resolveNaptrValue(naptrRecord.getRegexp(), Strings.CI.removeEnd(naptrRecord.getName().toString(), "."));
        URI uri = URI.create(smpAddress);

        if (!Strings.CI.containsAny(uri.getScheme(), schemas.toArray(new String[0]))) {
            LOG.debug("NAPTR Record: [{}] does not have any URL of required schema [{}].", recordDescription, schemas);
            return null;
        }
        return uri;
    }
    private List<PublisherLookupResult> convertAllRecordsToResult(SMPParticipantIdentifier identifier, List<Record> records, List<String> schemas, List<String> flagsList) {
        // get all records
        List<PublisherLookupResult> result = new ArrayList<>();
        for (Record record : records) {
            NAPTRRecord naptrRecord = (NAPTRRecord) record;
            URI uri = getURIFromNaptrRecord(schemas, flagsList, naptrRecord);
            if (uri == null) continue;
            result.add(new PublisherLookupResult(identifier, uri, naptrRecord.getService(), DNSLookupType.NAPTR));
        }
        return result;
    }

    /**
     * Method processes the NAPTR record value and returns the resolved value URL
     * address. In case the record value is U-NAPTR value (e.g. '.*' or '^.*$')
     * the replaces part is returned as is, else the replacement regular expression is executed.
     *
     * @param recordValue the NAPTR record replacement regular expression value
     * @param hostname the hostname to be replaced
     * @return the resolved URL address
     */
    public String resolveNaptrValue(String recordValue, String hostname) {
        String[] split = StringUtils.split(recordValue, NAPTR_SPLIT_REGEXP_CHAR);
        if (split.length != 2) {
            LOG.warn("Parse NAPTR Record value: [{}] does not have 2 parts separated by character '!'.", recordValue);
            return null;
        }
        String regExp = split[0];
        String value = split[1];
        // Fast parse (used for U-NAPTR '.*'  and the legacy '^.*$'
        if (StringUtils.equalsAny(regExp, NAPTR_REPLACE_REGEXP, NAPTR_REPLACE_REGEXP_LEGACY))
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
    public List<PublisherLookupResult> naptrUrlValueLookup(SMPParticipantIdentifier participantIdentifier, String uri) throws TechnicalException {
        List<Record> records = getAllNaptrRecords(participantIdentifier, uri);
        return getURLFromNaptrRecord(participantIdentifier, records, requiredNaptrServices, requiredURLSchemas, requiredNaptrFlags);
    }

    @Override
    public boolean dnsRecordExists(SMPParticipantIdentifier participantIdentifier, String participantURI, DNSLookupType type) throws TechnicalException {
        return !getAllRecordsForType(participantIdentifier, participantURI, type).isEmpty();
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

    public List<String> getRequiredNaptrFlags() {
        return requiredNaptrFlags;
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
            throw new DNSLookupException(DDCExceptionCode.INVALID_DNS_TYPE, "Lookup [" + recordType + "] for participant [" + participantIdentifier
                    + " ] has failed. Lookup result CODE [ " + lookupClient.getResult() + " ]");
        }
        return Arrays.asList(records);
    }


    public static class Builder {

        static final List<String> DEFAULT_SCHEMAS = Arrays.asList("http", "https");
        static final List<String> DEFAULT_NAPTR_SERVICES = Collections.singletonList("Meta:SMP");

        static final List<String> DEFAULT_NAPTR_FLAGS = Collections.singletonList("U");
        List<String> requiredURLSchemas = new ArrayList<>();
        List<String> requiredNaptrServices = new ArrayList<>();
        List<String> requiredNaptrFlags = new ArrayList<>();

        public DefaultDNSLookup.Builder addRequiredNaptrURLSchema(String schema) {
            this.requiredURLSchemas.add(StringUtils.removeEnd(schema, ":"));
            return this;
        }

        public DefaultDNSLookup.Builder addRequiredNaptrURLSchemas(List<String> schemas) {
            if (schemas == null || schemas.isEmpty()) {
                return this;
            }
            schemas.forEach(this::addRequiredNaptrURLSchema);
            return this;
        }

        public DefaultDNSLookup.Builder addRequiredNaptrService(String service) {
            this.requiredNaptrServices.add(service);
            return this;
        }

        public DefaultDNSLookup.Builder addRequiredNaptrServices(List<String> service) {

            service.forEach(value -> this.requiredNaptrServices.add(StringUtils.removeEnd(value, ":")));
            return this;
        }

        public DefaultDNSLookup.Builder addRequiredNaptrFlag(String flag) {
            this.requiredNaptrFlags.add(flag);
            return this;
        }

        public DefaultDNSLookup.Builder addRequiredNaptrFlags(List<String> flags) {
            this.requiredNaptrFlags.addAll(flags);
            return this;
        }

        public DefaultDNSLookup build() {
            validate();
            return new DefaultDNSLookup(this);
        }

        private void validate() {

            if (requiredURLSchemas.isEmpty()) {
                requiredURLSchemas.addAll(DEFAULT_SCHEMAS);
            }

            if (requiredNaptrFlags.isEmpty()) {
                requiredNaptrFlags.addAll(DEFAULT_NAPTR_FLAGS);
            }
        }
    }
}
