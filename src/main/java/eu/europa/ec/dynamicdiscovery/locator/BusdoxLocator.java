package eu.europa.ec.dynamicdiscovery.locator;

import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.util.HashUtil;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 * @author Erlend Klakegg Bergheim - erlend.klakegg.bergheim@difi.no
 */
public class BusdoxLocator extends AbstractLocator {

    public BusdoxLocator() {
        super();
    }

    public BusdoxLocator(String hostname) {
        super(hostname);
    }

    public URI lookup(ParticipantIdentifier participantIdentifier) {
        try {
            String e = HashUtil.getMD5Hash(participantIdentifier.getIdentifier());
            return new URI(String.format("http://b-%s.%s.%s", new Object[]{e, participantIdentifier.getScheme(), super.hostname}));
        } catch (URISyntaxException | UnsupportedEncodingException | NoSuchAlgorithmException exc) {
            throw new RuntimeException(exc.getMessage(), exc);
        }
    }
}
