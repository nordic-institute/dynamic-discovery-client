package eu.europa.ec.dynamicdiscovery.fetcher;

import eu.europa.ec.dynamicdiscovery.security.ProxyConfiguration;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedInputStream;
import java.net.URI;

/**
 * Created by rodrfla on 30/09/2016.
 */
public class URLFetcher implements IMetadataFetcher {
    //private static Logger logger = LoggerFactory.getLogger(ApacheFetcher.class);
    private HttpClient httpClient;
    private ProxyConfiguration proxyConfiguration;

    public URLFetcher(ProxyConfiguration proxyConfiguration) {
        this.proxyConfiguration = proxyConfiguration;
    }

    public URLFetcher() {
        this(null);
    }

    public FetcherResponse fetch(URI uri) throws Exception {
        if (this.proxyConfiguration != null) {
            proxyConfiguration.build(uri);
            return connect(this.proxyConfiguration.getHttpclient(), this.proxyConfiguration.getHttpget());
        } else {
            return connect(HttpClients.createDefault(), new HttpGet(uri));
        }
    }

    private FetcherResponse connect(HttpClient httpClient, HttpGet httpGet) throws Exception {
        try {
            HttpResponse response = httpClient.execute(httpGet);
            switch (response.getStatusLine().getStatusCode()) {
                case 200:
                    return new FetcherResponse(new BufferedInputStream(response.getEntity().getContent()), response.containsHeader("X-SMP-Namespace") ? response.getFirstHeader("X-SMP-Namespace").getValue() : null);
                case 404:
                    throw new Exception("Not supported.");
                default:
                    throw new Exception(String.format("Received code %s for lookup.", new Object[]{Integer.valueOf(response.getStatusLine().getStatusCode())}));
            }
        } catch (Exception var3) {
            throw new Exception(var3);
        }
    }
}