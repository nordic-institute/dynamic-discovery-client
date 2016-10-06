package eu.europa.ec.dynamicdiscovery.reader;

import eu.europa.ec.dynamicdiscovery.ServiceMetadata;
import eu.europa.ec.dynamicdiscovery.exception.BindException;
import eu.europa.ec.dynamicdiscovery.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;

import java.util.List;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 * @author Erlend Klakegg Bergheim - erlend.klakegg.bergheim@difi.no
 */

public interface IMetadataReader {
    List<DocumentIdentifier> parseDocumentIdentifiers(FetcherResponse var1) throws BindException;

    ServiceMetadata parseServiceMetadata(FetcherResponse var1) throws BindException, SecurityException;
}
