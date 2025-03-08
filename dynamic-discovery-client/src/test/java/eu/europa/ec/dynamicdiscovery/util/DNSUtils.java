/*-
 * #%L
 * dynamic-discovery-client
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
package eu.europa.ec.dynamicdiscovery.util;

import eu.europa.ec.dynamicdiscovery.core.locator.PublisherLookupResult;
import eu.europa.ec.dynamicdiscovery.enums.DNSLookupHashType;
import eu.europa.ec.dynamicdiscovery.enums.DNSLookupType;
import eu.europa.ec.dynamicdiscovery.exception.DDCRuntimeException;
import eu.europa.ec.dynamicdiscovery.model.identifiers.ParticipantIdentifierFormatter;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.DClass;
import org.xbill.DNS.NAPTRRecord;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DNSUtils {

    protected static final ParticipantIdentifierFormatter identifierFormatter = new ParticipantIdentifierFormatter();
    protected static final Logger LOG = LoggerFactory.getLogger(DNSUtils.class);
    public static final String TEST_NAPTR_SERVICE_SMP1 = "Meta:SMP";
    private static final String TEST_NAPTR_SERVICE_SMP2 = "oasis-bdxr-smp-2";
    private static final String TEST_NAPTR_SERVICE_CPP3 = "Meta:CPP3";

    public static final String TEST_TOP_DOMAIN_01 = "acc.edelivery.tech.ec.europa.eu";
    public static final String TEST_TOP_DOMAIN_02 = "ehealth.acc.edelivery.tech.ec.europa.eu";
    public static final String TEST_PUBLISHER_URL = "http://smp-mock-1.ehealth.eu:8888";
    public static final String NAPTR_FORMAT = "!.*!%s!";
    public static final String NAPTR_FORMAT_LEGACY = "!^.*$!%s!";
    public static final String DNS_NAME_SEPARATOR = ".";


    private DNSUtils() {
    }

    public static List<Record> createSmpDnsNaptrResponse(SMPParticipantIdentifier participantIdentifier) {
        return createSmpDnsNaptrResponse(participantIdentifier, TEST_NAPTR_SERVICE_SMP1);
    }

    public static List<Record> createSmpDnsNaptrResponse(SMPParticipantIdentifier participantIdentifier, String service) {
        return createSmpDnsNaptrResponse(participantIdentifier,
                TEST_TOP_DOMAIN_02,
                TEST_PUBLISHER_URL,
                service);
    }

    public static List<Record> createSmpDnsNaptrResponseForValue(SMPParticipantIdentifier participantIdentifier, String service, String urlValue) {
        return createSmpDnsNaptrResponse(participantIdentifier,
                TEST_TOP_DOMAIN_02,
                urlValue,
                service);
    }



    public static List<Record> createSmpDnsNaptrResponse(SMPParticipantIdentifier participantIdentifier, String dnsZone, String targetUrl, String service) {
        return createSmpDnsNaptrResponse(participantIdentifier, dnsZone, targetUrl, service, NAPTR_FORMAT);
    }

    public static List<Record> createSmpDnsNaptrResponse(SMPParticipantIdentifier participantIdentifier, String dnsZone, String targetUrl, String service, String valueFormat) {

        if (participantIdentifier == null){
            throw new DDCRuntimeException("Can not lookup DNS record for null participantIdentifier");
        }

        try {
            List<Record> records = new ArrayList<>();
            records.add(new NAPTRRecord(createSmpDnsNaptrDomainName(participantIdentifier.getIdentifier(),
                    participantIdentifier.getScheme(), dnsZone), DClass.IN, 60, 100, 10, "U", service, String.format(valueFormat, targetUrl), Name.fromString(".")));
            return records;
        } catch (Exception e) {
            LOG.error("Error occurred while generating the test naptr record!", e);
        }
        return Collections.emptyList();
    }

    public static String createSmpDnsNaptrDomain(SMPParticipantIdentifier identifier, String dnsZoneName) throws Exception {
        return createSmpDnsNaptrDomain(identifier.getIdentifier(), identifier.getScheme(), dnsZoneName);
    }

    public static String createSmpDnsNaptrDomain(String participantId, String scheme, String dnsZoneName) throws Exception {
        return  identifierFormatter.dnsLookupFormat(scheme, participantId, DNSLookupHashType.SHA256_BASE32) + DNS_NAME_SEPARATOR + dnsZoneName;
    }

    public static Name createSmpDnsNaptrDomainName(String participantId, String scheme, String dnsZoneName) throws Exception {
        String smpDnsName = createSmpDnsNaptrDomain(participantId, scheme, dnsZoneName);
        return Name.fromString(smpDnsName + DNS_NAME_SEPARATOR);
    }

    public static  List<PublisherLookupResult> createMockPublisherLookupNaptrResult(SMPParticipantIdentifier identifier, String urlAddress) {
        return createMockPublisherLookupNaptrResult(identifier, urlAddress, DNSUtils.TEST_NAPTR_SERVICE_SMP1);
    }

    public static  List<PublisherLookupResult> createMockPublisherLookupNaptrResult(SMPParticipantIdentifier identifier, String urlAddress, String ... naptrServices) {
        List<PublisherLookupResult> results = new ArrayList<>();
        for (String naptrService : naptrServices) {
            results.add(new PublisherLookupResult(identifier, URI.create(urlAddress),naptrService, DNSLookupType.NAPTR));
        }
        return results;
    }
}
