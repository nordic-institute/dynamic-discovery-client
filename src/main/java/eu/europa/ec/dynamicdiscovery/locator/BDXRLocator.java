package eu.europa.ec.dynamicdiscovery.locator;

import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.util.HashUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.*;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public class BDXRLocator extends AbstractLocator {
    private static Logger logger = LoggerFactory.getLogger(BDXRLocator.class);

    public BDXRLocator(IDNSLookup dnsLookup) {
        super(dnsLookup);
    }

    public BDXRLocator(String hostname, IDNSLookup dnsLookup) {
        super(hostname, dnsLookup);
    }

    public URI lookup(ParticipantIdentifier participantIdentifier) throws TechnicalException {
        URI uri = naptrLookup(participantIdentifier);
        if (uri == null) {
            uri = cnameLookup(participantIdentifier);
        }
        if (uri == null) {
            throw new DNSLookupException(String.format("DNS Lookup was not able to retrieve information using NAPTR and/or CNAME for the participant [ %s ]", new Object[]{participantIdentifier.getIdentifier()}));
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
            //It was not possible to lookup using NAPTR, CNAME lookup will be used
            logger.debug(exc.getMessage(), exc);
        }
        return uri;
    }

    private String naptrLookupFetcher(ParticipantIdentifier participantIdentifier, String uri) throws TechnicalException, TextParseException {
        return getDnsLookup().lookupFetcher(participantIdentifier, uri);
    }
}
