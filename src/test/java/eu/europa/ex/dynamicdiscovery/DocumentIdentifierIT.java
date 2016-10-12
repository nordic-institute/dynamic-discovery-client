package eu.europa.ex.dynamicdiscovery;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import eu.europa.ec.dynamicdiscovery.DynamicDiscovery;
import eu.europa.ec.dynamicdiscovery.DynamicDiscoveryBuilder;
import eu.europa.ec.dynamicdiscovery.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.fetcher.URLFetcher;
import eu.europa.ec.dynamicdiscovery.locator.BDXRLocator;
import eu.europa.ec.dynamicdiscovery.locator.dns.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.locator.dns.LookupClient;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.provider.DefaultProvider;
import eu.europa.ec.dynamicdiscovery.reader.MultiReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.DClass;
import org.xbill.DNS.NAPTRRecord;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

import java.io.BufferedInputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static org.mockito.Mockito.mock;

/**
 * Created by rodrfla on 30/09/2016.
 */
public class DocumentIdentifierIT extends AbstractIT {
    private static Logger logger = LoggerFactory.getLogger(DocumentIdentifierIT.class);

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

   // @Mock
    //DefaultDNSLookup defaultDNSLookup;

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


    @Test
    public void getDocumentIdentifier2() throws Exception {
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

        NAPTRRecord newNaptrRecord = new NAPTRRecord(Name.fromString("ZR2ZGDOAGAVSHSQ2MRHXEZV2H6ATTQBF4JJ4J7VJNPYMRDZ3UG4Q.iso6523-actorid-upis.edelivery.tech.ec.europa.eu."), DClass.IN, 60, 100, 10, "U", "Meta:SMP", "!^.*$!http://123456.co.tes!", Name.fromString("."));
        Record[] records = new Record[]{newNaptrRecord};

       // LookupClient lookupClient = mock(LookupClient.class);
        //Mockito.when(lookupClient.build(new ParticipantIdentifier("0088:123456789101112", "iso6523-actorid-upis"), "ZR2ZGDOAGAVSHSQ2MRHXEZV2H6ATTQBF4JJ4J7VJNPYMRDZ3UG4Q.iso6523-actorid-upis.edelivery.tech.ec.europa.eu");).thenReturn("http://123456.co.tes!");

        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.lookupFetcher(new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis"), "ZR2ZGDOAGAVSHSQ2MRHXEZV2H6ATTQBF4JJ4J7VJNPYMRDZ3UG4Q.iso6523-actorid-upis.edelivery.tech.ec.europa.eu")).thenReturn("http://123456.co.tes!");

        final DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new BDXRLocator("edelivery.tech.ec.europa.eu", defaultDNSLookup))
                //.locator(new BusdoxLocator("edelivery.tech.ec.europa.eu"))
//                .fetcher(new URLFetcher(new ProxyConfiguration("158.169.9.13", 8012, "j50b107", "34i6fv7")))new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis"))
                .fetcher(new URLFetcher())
                .build();
        List<DocumentIdentifier> documentIdentifiers = smpClient.getDocumentIdentifiers(new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis")); //"0037:01841111111111")
        System.out.println(documentIdentifiers.size());

        DefaultProvider provider = new DefaultProvider();
        URI providerURI = provider.resolveDocumentIdentifiers(new URI("http://localhost:8080/" + Constants.SERVICE_GROUP_URL_9925_0367302178), new ParticipantIdentifier("0088:123456789101112", "iso6523-actorid-upis"));

        MultiReader reader = new MultiReader();
        FetcherResponse fetcherResponse = new FetcherResponse(new BufferedInputStream(response.getEntity().getContent()), response.containsHeader("X-SMP-Namespace") ? response.getFirstHeader("X-SMP-Namespace").getValue() : null);

        //List<DocumentIdentifier> documentIdentifiers = reader.parseDocumentIdentifiers(fetcherResponse);

    }

}
