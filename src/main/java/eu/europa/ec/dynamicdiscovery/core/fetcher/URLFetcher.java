package eu.europa.ec.dynamicdiscovery.core.fetcher;

import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.core.security.ProxyConfiguration;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedInputStream;
import java.net.URI;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 * @author Erlend Klakegg Bergheim - erlend.klakegg.bergheim@difi.no
 */
public class URLFetcher implements IMetadataFetcher {
    private HttpClient httpClient;
    private ProxyConfiguration proxyConfiguration;

    public URLFetcher(ProxyConfiguration proxyConfiguration) {
        this.proxyConfiguration = proxyConfiguration;
    }

    public URLFetcher() {
        this(null);
    }

    public FetcherResponse fetch(URI uri) throws DNSLookupException {
        if (this.proxyConfiguration != null) {
            proxyConfiguration.build(uri);
            return connect(this.proxyConfiguration.getHttpclient(), this.proxyConfiguration.getHttpget());
        } else {
            return connect(HttpClients.createDefault(), new HttpGet(uri));
        }
    }

    public FetcherResponse connect(HttpClient httpClient, HttpGet httpGet) throws DNSLookupException {
        try {
            HttpResponse response = httpClient.execute(httpGet);
            switch (response.getStatusLine().getStatusCode()) {
                case 200:
                    return new FetcherResponse(new BufferedInputStream(response.getEntity().getContent()), response.containsHeader("X-SMP-Namespace") ? response.getFirstHeader("X-SMP-Namespace").getValue() : null);
                case 404:
                    throw new DNSLookupException("Not supported.");
                default:
                    throw new DNSLookupException(String.format("Received code %s for lookup.", new Object[]{Integer.valueOf(response.getStatusLine().getStatusCode())}));
            }
        } catch (Exception exc) {
            throw new DNSLookupException(exc.getMessage(), exc);
        }
    }
}