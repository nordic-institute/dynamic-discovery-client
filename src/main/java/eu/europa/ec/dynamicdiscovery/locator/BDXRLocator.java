package eu.europa.ec.dynamicdiscovery.locator;

import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.util.HashUtil;
import org.apache.commons.lang3.StringUtils;
import org.xbill.DNS.*;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rodrfla on 30/09/2016.
 */
public class BDXRLocator extends AbstractLocator {

    public BDXRLocator() {
        super();
    }

    public BDXRLocator(String hostname) {
        super(hostname);
    }

    public URI lookup(ParticipantIdentifier participantIdentifier) throws TechnicalException {
        URI uri = naptrLookup(participantIdentifier);
        if (uri != null) {
            uri = cnameLookup(participantIdentifier);
        }
        if (uri == null) {
            throw new DNSLookupException(String.format("DNS Lookup was not able to retrieve information using NAPTR and/or CNAME for the participant [ %s ]" , new Object[]{participantIdentifier.getIdentifier()}));
        }

        return uri;
    }

    private URI cnameLookup(ParticipantIdentifier participantIdentifier) {
        return new BusdoxLocator().lookup(participantIdentifier);
    }

    private URI naptrLookup(ParticipantIdentifier participantIdentifier) {
        URI uri = null;
        try {
            String participantIdHashed = HashUtil.getSHA256HashBase32(participantIdentifier.getIdentifier());
            String smpURI = naptrLookupFetcher(participantIdentifier, String.format("%s.%s.%s", new Object[]{participantIdHashed, participantIdentifier.getScheme(), super.hostname}));
            uri = new URI(smpURI);
        } catch (URISyntaxException | UnsupportedEncodingException | NoSuchAlgorithmException | TextParseException exc) {
            throw new RuntimeException(exc.getMessage(), exc);
        } catch (TechnicalException exc) {
            //logs
        }
        return uri;
    }

    private String naptrLookupFetcher(ParticipantIdentifier participantIdentifier, String uri) throws TechnicalException, TextParseException {

        Lookup lookup = new Lookup(uri, Type.NAPTR);
        Record[] records = lookup.run();

        if (lookup.getResult() != Lookup.SUCCESSFUL) {
            throw new DNSLookupException(String.format("DNS Lookup for participant [ %s ] and NATPR record [ %s ] failed. Lookup CODE [ %s ]", new Object[]{participantIdentifier.getIdentifier(), uri, lookup.getResult()}));
        }

        String smpAddress = null;
        String naptrRegex = null;
        for (Record record : records) {
            NAPTRRecord naptrRecord = (NAPTRRecord) record;
            String regex = ".*?(http:\\/\\/.*[^!])";
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
}
