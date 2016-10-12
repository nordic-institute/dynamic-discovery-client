package eu.europa.ex.dynamicdiscovery.fetcher;

import com.github.tomakehurst.wiremock.client.WireMock;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.fetcher.IMetadataFetcher;
import eu.europa.ex.dynamicdiscovery.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URI;

import static com.github.tomakehurst.wiremock.client.WireMock.post;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public class URLFetcherMock implements IMetadataFetcher {

    public enum LookupType {
        NAPTR, CNAME;
    }

    private LookupType lookupType;
    private String serviceUrl;
    private String serviceBodyResponse;
    private String smpAlias;

    public URLFetcherMock() {
    }

    public void setParameters(LookupType lookupType, String serviceUrl, String serviceBodyResponse, String smpAlias) throws DNSLookupException {
        this.lookupType = lookupType;
        this.serviceUrl = serviceUrl;
        this.serviceBodyResponse = serviceBodyResponse;

        if (lookupType == LookupType.CNAME && StringUtils.isEmpty(smpAlias)) {
            throw new DNSLookupException("SMP alias represented by MD5 must be not null");
        }
        if (!StringUtils.isEmpty(smpAlias)) {
            this.smpAlias = "http://" + smpAlias + (!smpAlias.endsWith("/") ? "/" : "");
        }
    }

    public void setParameters(LookupType lookupType, String serviceUrl, String serviceBodyResponse) throws DNSLookupException {
        setParameters(lookupType, serviceUrl, serviceBodyResponse, null);
    }

    @Override
    public FetcherResponse fetch(URI uri) throws Exception {
        return switchResponse(uri);
    }

    private FetcherResponse switchResponse(URI uri) throws IOException, DNSLookupException {

        WireMock.stubFor(post(WireMock.urlEqualTo(serviceUrl))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/soap+xml")
                        .withBody(serviceBodyResponse)));

        HttpClient client = HttpClientBuilder.create().build();

        String uriStr;
        if (isCNAME()) {
            uriStr = uri.toString().replace(smpAlias, Constants.SMP_DOMAIN);
        } else {
            uriStr = uri.toString().replace(Constants.SMP_DOMAIN_ALIAS, Constants.SMP_DOMAIN);

        }
        HttpPost request = new HttpPost(uriStr);
        HttpResponse response = client.execute(request);

        switch (response.getStatusLine().getStatusCode()) {
            case 200:
                return new FetcherResponse(new BufferedInputStream(response.getEntity().getContent()), response.containsHeader("X-SMP-Namespace") ? response.getFirstHeader("X-SMP-Namespace").getValue() : null);
            case 404:
                throw new DNSLookupException("Not supported.");
            default:
                throw new DNSLookupException(String.format("Received code %s for lookup.", new Object[]{Integer.valueOf(response.getStatusLine().getStatusCode())}));
        }
    }

    private boolean isCNAME() {
        if (lookupType != null && lookupType == LookupType.CNAME) {
            return true;
        }
        return false;
    }
}