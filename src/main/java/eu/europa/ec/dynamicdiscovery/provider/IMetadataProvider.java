package eu.europa.ec.dynamicdiscovery.provider;

import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;

import java.net.URI;

/**
 * Created by rodrfla on 30/09/2016.
 */
public interface IMetadataProvider {
    URI resolveDocumentIdentifiers(URI var1, ParticipantIdentifier var2);

    URI resolveServiceMetadata(URI var1, ParticipantIdentifier var2, DocumentIdentifier var3);
}
