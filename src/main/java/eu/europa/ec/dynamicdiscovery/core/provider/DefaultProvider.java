package eu.europa.ec.dynamicdiscovery.core.provider;

import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;

import java.net.URI;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 * @author Erlend Klakegg Bergheim - erlend.klakegg.bergheim@difi.no
 */
public class DefaultProvider implements IMetadataProvider {

    public DefaultProvider() {
    }

    public URI resolveDocumentIdentifiers(URI location, ParticipantIdentifier participantIdentifier) {
        return location.resolve(String.format("/%s", new Object[]{participantIdentifier.urlencoded()}));
    }

    public URI resolveServiceMetadata(URI location, ParticipantIdentifier participantIdentifier, DocumentIdentifier documentIdentifier) {
        return location.resolve(String.format("/%s/services/%s", new Object[]{participantIdentifier.urlencoded(), documentIdentifier.urlencoded()}));
    }
}
