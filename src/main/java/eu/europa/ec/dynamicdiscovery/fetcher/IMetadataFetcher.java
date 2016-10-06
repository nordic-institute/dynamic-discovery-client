package eu.europa.ec.dynamicdiscovery.fetcher;

import java.net.URI;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 * @author Erlend Klakegg Bergheim - erlend.klakegg.bergheim@difi.no
 *
 */
public interface IMetadataFetcher {

    FetcherResponse fetch(URI uri) throws Exception;
}
