package eu.europa.ex.dynamicdiscovery;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import eu.europa.ec.dynamicdiscovery.DynamicDiscovery;
import eu.europa.ec.dynamicdiscovery.DynamicDiscoveryBuilder;
import eu.europa.ec.dynamicdiscovery.locator.BDXRLocator;
import eu.europa.ec.dynamicdiscovery.locator.dns.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ex.dynamicdiscovery.fetcher.URLFetcherMock;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * Created by rodrfla on 30/09/2016.
 */
public class DocumentIdentifierIT extends AbstractIT {
    private static Logger logger = LoggerFactory.getLogger(DocumentIdentifierIT.class);

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

/*
    @Test
    public void getDocumentIdentifier() throws Exception {
        WireMock.stubFor(post(WireMock.urlEqualTo(Constants.SERVICE_GROUP_URL_9925_0367302178))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/soap+xml")
                        .withBody(Constants.SERVICE_GROUP_BODY_9925_0367302178)));

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("http://localhost:8080/" + Constants.SERVICE_GROUP_URL_9925_0367302178);
        HttpResponse response = client.execute(request);
        logger.info("getStatusCode " + response.getStatusLine().getStatusCode());
        // logger.info(IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset()));

        // List<Record> records = dnsService.getAllRecords();
        //logger.info("records " + records.size());
        DefaultProvider provider = new DefaultProvider();

        URI providerURI = provider.resolveDocumentIdentifiers(new URI("http://localhost:8080/" + Constants.SERVICE_GROUP_URL_9925_0367302178), new ParticipantIdentifier("0088:123456789101112", "iso6523-actorid-upis"));

        MultiReader reader = new MultiReader();
        FetcherResponse fetcherResponse = new FetcherResponse(new BufferedInputStream(response.getEntity().getContent()), response.containsHeader("X-SMP-Namespace") ? response.getFirstHeader("X-SMP-Namespace").getValue() : null);

        List<DocumentIdentifier> documentIdentifiers = reader.parseDocumentIdentifiers(fetcherResponse);

    }
*/

    @Test
    public void getDocumentIdentifierOK() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();

        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.lookupFetcher(new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis"), "ZR2ZGDOAGAVSHSQ2MRHXEZV2H6ATTQBF4JJ4J7VJNPYMRDZ3UG4Q.iso6523-actorid-upis.edelivery.tech.ec.europa.eu")).thenReturn(Constants.SMP_DOMAIN_ALIAS);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new BDXRLocator("edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .build();
        List<DocumentIdentifier> documentIdentifiers = smpClient.getDocumentIdentifiers(new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis"));
        Assert.assertEquals(documentIdentifiers.size(), 3);

    }

}
