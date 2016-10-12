package eu.europa.ec.dynamicdiscovery.locator.dns;

import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.locator.IDNSLookup;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import org.apache.commons.lang3.StringUtils;
import org.xbill.DNS.*;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public class DefaultDNSLookup implements IDNSLookup {

    private ILookupClient lookupClient;

    @Override
    public String lookupFetcher(ParticipantIdentifier participantIdentifier, String uri) throws TechnicalException, TextParseException {
        List<Record> records = getAllRecords(uri, participantIdentifier);

        String smpAddress = null;
        String naptrRegex = null;
        for (Record record : records) {
            NAPTRRecord naptrRecord = (NAPTRRecord) record;
            String regex = ".*?(http.*[^!])";
            naptrRegex = naptrRecord.getRegexp();
            Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher m = p.matcher(naptrRecord.getRegexp());
            if (m.find()) {
                smpAddress = m.group(1);
            }
        }

        if (StringUtils.isEmpty(smpAddress)) {
            throw new DNSLookupException(String.format("DNS Lookup for NATPR record failed, CODE: ", new Object[]{naptrRegex}));
        }

        return smpAddress;
    }

    public String lookupFetcher(LookupClient lookupClient, ParticipantIdentifier participantIdentifier, String uri) throws TechnicalException, TextParseException {
        this.lookupClient = lookupClient;
        return lookupFetcher(participantIdentifier, uri);
    }

    @Override
    public List<Record> getAllRecords(Object... parameters) throws DNSLookupException, TextParseException {

        if (parameters == null || parameters.length != 2) {
            throw new DNSLookupException(String.format("Parameters for NAPTR Loopup are NULL or Incorrect [%s].", new Object[]{parameters}));
        }
        String uri = (String) parameters[0];
        String participantId = ((ParticipantIdentifier) parameters[1]).getIdentifier();
        Record[] records = runLookup(uri, Type.NAPTR);
        if (lookupClient.getResultCode() != Lookup.SUCCESSFUL) {
            throw new DNSLookupException(String.format("NAPTR Lookup for participant [ %s ] has failed. Lookup result CODE [ %s ]", new Object[]{participantId, lookupClient.getResultCode()}));
        }

        return Arrays.asList(records);
    }

    public Record[] runLookup(String uri, Integer recordType) throws TextParseException {
        if (lookupClient == null) {
            lookupClient = new LookupClient(uri, recordType);
        }
        return lookupClient.run();
    }
}
