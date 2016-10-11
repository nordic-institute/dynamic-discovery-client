package eu.europa.ec.dynamicdiscovery.locator;

import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;

import java.net.URI;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 * @author Erlend Klakegg Bergheim - erlend.klakegg.bergheim@difi.no
 */
public abstract class AbstractLocator implements IMetadataLocator {

    public static final String PRODUCTION = "edelivery.tech.ec.europa.eu";
    public static final String TEST = "acc.edelivery.tech.ec.europa.eu";
    protected String hostname;
    protected IDNSLookup dnsLookup;

    public AbstractLocator(IDNSLookup dnsLookup) {
        this(PRODUCTION, dnsLookup);
    }

    public AbstractLocator(String hostname, IDNSLookup dnsLookup) {
        this.hostname = hostname;
        this.dnsLookup = dnsLookup;
    }

    public abstract URI lookup(ParticipantIdentifier identifier) throws TechnicalException;

    public URI lookup(String identifier, String scheme) throws TechnicalException {
        return this.lookup(new ParticipantIdentifier(identifier, scheme));
    }

    public IDNSLookup getDnsLookup() {
        return dnsLookup;
    }
}
