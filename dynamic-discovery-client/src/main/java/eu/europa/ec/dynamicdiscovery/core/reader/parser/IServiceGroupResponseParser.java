package eu.europa.ec.dynamicdiscovery.core.reader.parser;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ServiceGroup;

import java.util.List;

/**
 * Created by rodrfla on 01/08/2017.
 */
public interface IServiceGroupResponseParser {

    List<DocumentIdentifier> getDocumentIdentifiers(FetcherResponse fetcherResponse) throws TechnicalException;

    ServiceGroup getServiceGroup(FetcherResponse fetcherResponse) throws TechnicalException;
}
