package eu.europa.ec.dynamicdiscovery.fetcher;

import eu.europa.ec.dynamicdiscovery.fetcher.FetcherResponse;

import java.net.URI;

/**
 * Created by rodrfla on 30/09/2016.
 */
public interface IMetadataFetcher {

    FetcherResponse fetch(URI uri) throws Exception;
}
