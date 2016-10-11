import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import eu.europa.ec.dynamicdiscovery.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.provider.DefaultProvider;
import eu.europa.ec.dynamicdiscovery.reader.MultiReader;
import eu.europa.ex.dynamicdiscovery.AbstractTest;
import eu.europa.ex.dynamicdiscovery.Constants;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.net.URI;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.post;

/**
 * Created by rodrfla on 30/09/2016.
 */
public class DocumentIdentifierIT extends AbstractTest {
    private static Logger logger = LoggerFactory.getLogger(DocumentIdentifierIT.class);

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Test
    public void getDocumentIdentifier() throws Exception {
        WireMock.stubFor(post(WireMock.urlEqualTo(Constants.SERVICE_GROUP_URL_0088_123456789101112))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/soap+xml")
                        .withBody(Constants.SERVICE_GROUP_BODY_0088_123456789101112)));

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("http://localhost:8080/" + Constants.SERVICE_GROUP_URL_0088_123456789101112);
        HttpResponse response = client.execute(request);
        logger.info("getStatusCode " + response.getStatusLine().getStatusCode());
        // logger.info(IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset()));

        // List<Record> records = dnsService.getAllRecords();
        //logger.info("records " + records.size());
        DefaultProvider provider = new DefaultProvider();

        URI providerURI = provider.resolveDocumentIdentifiers(new URI("http://localhost:8080/" + Constants.SERVICE_GROUP_URL_0088_123456789101112), new ParticipantIdentifier("0088:123456789101112", "iso6523-actorid-upis"));

        MultiReader reader = new MultiReader();
        FetcherResponse fetcherResponse = new FetcherResponse(new BufferedInputStream(response.getEntity().getContent()), response.containsHeader("X-SMP-Namespace") ? response.getFirstHeader("X-SMP-Namespace").getValue() : null);

        List<DocumentIdentifier> documentIdentifiers = reader.parseDocumentIdentifiers(fetcherResponse);

    }

}
