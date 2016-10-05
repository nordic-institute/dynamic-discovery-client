package eu.europa.ec.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;

import java.util.List;

/**
 * Created by rodrfla on 30/09/2016.
 */

public interface IMetadataReader {
    List<DocumentIdentifier> parseDocumentIdentifiers(FetcherResponse var1) throws Exception;

    ServiceMetadata parseServiceMetadata(FetcherResponse var1) throws Exception, SecurityException;
}
