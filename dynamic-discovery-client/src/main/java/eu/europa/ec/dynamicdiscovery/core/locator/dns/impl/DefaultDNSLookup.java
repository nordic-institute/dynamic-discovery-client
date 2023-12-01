/*
 * (C) Copyright 2016-2023 - European Commission | Dynamic Discovery Client
 *
 * https://ec.europa.eu/digital-building-blocks/code/projects/EDELIVERY/repos/dynamic-discovery-client/browse
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
package eu.europa.ec.dynamicdiscovery.core.locator.dns.impl;

import eu.europa.ec.dynamicdiscovery.core.locator.dns.IDNSLookup;
import eu.europa.ec.dynamicdiscovery.enums.DNSLookupType;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.NAPTRRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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



    protected DefaultDNSLookup(Builder builder) {
        this.requiredURLSchemas = new ArrayList<>(builder.requiredURLSchemas);
        this.requiredNaptrServices = new ArrayList<>(builder.requiredNaptrServices);
        this.requiredNaptrFlagsList = new ArrayList<>(builder.requiredNaptrFlagsList);
    }


    public String getURLFromNaptrRecord(List<Record> records,
                                        List<String> services,
                                        List<String> schemas,
                                        List<String> flagsList){

        for (Record dnsRecord : records) {
            NAPTRRecord naptrRecord = (NAPTRRecord) dnsRecord;
            String recordDescription = naptrRecord.rdataToString();

            if (!validNaptrService(naptrRecord.getService(), services)) {
                LOG.debug("NAPTR Record: [{}] does not have any of required services [{}].", recordDescription, services);
                continue;
            }

            if (!validNaptrFlags(naptrRecord.getFlags(), flagsList)) {
                LOG.debug("NAPTR Record: [{}] does not have any of required flag [{}].", recordDescription, flagsList);
                continue;
            }

            String smpAddress = resolveNaptrValue(naptrRecord.getRegexp(), StringUtils.removeEnd(naptrRecord.getName().toString(),"."));
            if (validURLSchema(smpAddress, schemas)) {
                return smpAddress;
            }
        }
        return null;
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
        if (StringUtils.equalsAny(regExp, ".*","^.*$"))
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
            return Collections.emptyList();
        }
        if (lookupClient.getResult() != Lookup.SUCCESSFUL) {
            throw new DNSLookupException("Lookup [" + recordType + "] for participant [" + participantIdentifier
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
