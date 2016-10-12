package eu.europa.ex.dynamicdiscovery.fetcher;

import com.github.tomakehurst.wiremock.client.WireMock;
import eu.europa.ec.dynamicdiscovery.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.fetcher.IMetadataFetcher;
import eu.europa.ex.dynamicdiscovery.Constants;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.net.URI;

import static com.github.tomakehurst.wiremock.client.WireMock.post;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public class URLFetcherMock implements IMetadataFetcher {
    private static Logger logger = LoggerFactory.getLogger(URLFetcherMock.class);

    enum SMPResponse{
        NA
    }

    public URLFetcherMock() {
    }

    @Override
    public FetcherResponse fetch(URI uri) throws Exception {

        WireMock.stubFor(post(WireMock.urlEqualTo(Constants.SERVICE_GROUP_URL_9925_0367302178))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/soap+xml")
                        .withBody(Constants.SERVICE_GROUP_BODY_9925_0367302178)));

        HttpClient client = HttpClientBuilder.create().build();
        String uriStr = uri.toString().replace(Constants.SMP_DOMAIN_ALIAS, Constants.SMP_DOMAIN);
        HttpPost request = new HttpPost(uriStr);
        HttpResponse response = client.execute(request);
        logger.info("getStatusCode " + response.getStatusLine().getStatusCode());

        try {
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