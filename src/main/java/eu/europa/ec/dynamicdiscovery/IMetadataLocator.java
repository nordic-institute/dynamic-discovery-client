package eu.europa.ec.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;

import java.net.URI;

/**
 * Created by rodrfla on 30/09/2016.
 */
public interface IMetadataLocator {
    URI lookup(String var1) throws Exception;

    URI lookup(ParticipantIdentifier var1) throws Exception;
}
