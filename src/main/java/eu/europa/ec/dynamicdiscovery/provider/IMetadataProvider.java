package eu.europa.ec.dynamicdiscovery.provider;

import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;

import java.net.URI;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public interface IMetadataProvider {
    URI resolveDocumentIdentifiers(URI var1, ParticipantIdentifier var2);

    URI resolveServiceMetadata(URI var1, ParticipantIdentifier var2, DocumentIdentifier var3);
}
