package eu.europa.ec.dynamicdiscovery.util;

import eu.europa.ec.dynamicdiscovery.enums.DNSLookupHashType;
import eu.europa.ec.dynamicdiscovery.exception.DDCRuntimeException;
import eu.europa.ec.dynamicdiscovery.model.identifiers.ParticipantIdentifierFormatter;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.DClass;
import org.xbill.DNS.NAPTRRecord;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DNSUtils {

    protected static final ParticipantIdentifierFormatter identifierFormatter = new ParticipantIdentifierFormatter();
    protected static final Logger LOG = LoggerFactory.getLogger(DNSUtils.class);
    public static final String NAPTR_SERVICE = "Meta:SMP";
    public static final String TEST_TOP_DOMAIN = "ehealth.acc.edelivery.tech.ec.europa.eu";
    public static final String TEST_TARGET_URL = "http://smp-mock-1.ehealth.eu:8888";
    public static final String NAPTR_FORMAT = "!.*!%s!";
    public static final String NAPTR_FORMAT_LEGACY = "!^.*$!%s!";
    public static final String DNS_NAME_SEPARATOR = ".";


    private DNSUtils() {
    }

    public static List<Record> createSmpDnsNaptrResponse(SMPParticipantIdentifier participantIdentifier) {
        return createSmpDnsNaptrResponse(participantIdentifier, NAPTR_SERVICE);
    }

    public static List<Record> createSmpDnsNaptrResponse(SMPParticipantIdentifier participantIdentifier, String service) {
        return createSmpDnsNaptrResponse(participantIdentifier,
                TEST_TOP_DOMAIN,
                TEST_TARGET_URL,
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

}
