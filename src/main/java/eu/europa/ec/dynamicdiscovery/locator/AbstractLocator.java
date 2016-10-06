package eu.europa.ec.dynamicdiscovery.locator;

import eu.europa.ec.dynamicdiscovery.IMetadataLocator;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;

import java.net.URI;

/**
 * Created by rodrfla on 30/09/2016.
 */
public abstract class AbstractLocator implements IMetadataLocator {

    public static final String OPENPEPPOL_PRODUCTION = "edelivery.tech.ec.europa.eu";
    public static final String OPENPEPPOL_TEST = "acc.edelivery.tech.ec.europa.eu";
    protected String hostname;

    public AbstractLocator() {
        this(OPENPEPPOL_PRODUCTION);
    }

    public AbstractLocator(String hostname) {
        this.hostname = hostname;
    }

    public abstract URI lookup(ParticipantIdentifier identifier) throws TechnicalException;

    public URI lookup(String identifier) throws TechnicalException {
        return this.lookup(new ParticipantIdentifier(identifier));
    }
}
