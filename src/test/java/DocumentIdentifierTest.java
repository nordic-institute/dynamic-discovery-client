import com.github.tomakehurst.wiremock.client.WireMock;
import eu.europa.ec.dynamicdiscovery.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.provider.DefaultProvider;
import eu.europa.ec.dynamicdiscovery.reader.BdxrReader;
import eu.europa.ec.dynamicdiscovery.reader.MultiReader;
import eu.europa.ex.dynamicdiscovery.AbstractTest;
import eu.europa.ex.dynamicdiscovery.Constants;
import eu.europa.ex.dynamicdiscovery.service.WebServerMock;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.post;

/**
 * Created by rodrfla on 30/09/2016.
 */
public class DocumentIdentifierTest extends AbstractTest {
    private static Logger logger = LoggerFactory.getLogger(DocumentIdentifierTest.class);

    @InjectMocks
    WebServerMock serverMock;

    @Test
    public void getDocumentIdentifier() throws Exception {
        WireMock.stubFor(post(WireMock.urlEqualTo(Constants.COMPLETE_SERVICE_GROUP_URL_0088_123456789101112))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/soap+xml")
                        .withBody(Constants.COMPLETE_SERVICE_GROUP_BODY_0088_123456789101112)));

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("http://localhost:8080/" + Constants.COMPLETE_SERVICE_GROUP_URL_0088_123456789101112);
        HttpResponse response = client.execute(request);
        logger.info("getStatusCode " + response.getStatusLine().getStatusCode());
        // logger.info(IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset()));

        DefaultProvider provider = new DefaultProvider();

        URI providerURI = provider.resolveDocumentIdentifiers(new URI("http://localhost:8080/" + Constants.COMPLETE_SERVICE_GROUP_URL_0088_123456789101112), new ParticipantIdentifier("0088:123456789101112"));

        MultiReader reader = new MultiReader();
        FetcherResponse fetcherResponse = new FetcherResponse(new BufferedInputStream(response.getEntity().getContent()), response.containsHeader("X-SMP-Namespace") ? response.getFirstHeader("X-SMP-Namespace").getValue() : null);

       /*
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
       // System.out.println("Response " + response.toString());
        String teste = "<ns0:CompleteServiceGroup xmlns:ns2=\"http://www.w3.org/2005/08/addressing\" xmlns:ns1=\"http://busdox.org/transport/identifiers/1.0/\" xmlns:ns3=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:ns0=\"http://busdox.org/serviceMetadata/publishing/1.0/\">\n" +
                "<ns0:ServiceGroup>\n" +
                "<ns1:ParticipantIdentifier scheme=\"iso6523-actorid-upis\">0088:123456789101112</ns1:ParticipantIdentifier>\n" +
                "<ns0:ServiceMetadataReferenceCollection>\n" +
                "<ns0:ServiceMetadataReference href=\"http://localhost:7001/iso6523-actorid-upis%3A%3A0088%3A123456789101112/services/busdox-docid-qns%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3AInvoice-2%3A%3AInvoice%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiicoretrdm010%3Aver1.0%3A%23urn%3Awww.peppol.eu%3Abis%3Apeppol4a%3Aver1.0%3A%3A2.0\"/>\n" +
                "</ns0:ServiceMetadataReferenceCollection>\n" +
                "</ns0:ServiceGroup>\n" +
                "<ns0:ServiceMetadata>\n" +
                "<ns0:ServiceInformation>\n" +
                "<ns1:ParticipantIdentifier scheme=\"iso6523-actorid-upis\">0088:123456789101112</ns1:ParticipantIdentifier>\n" +
                "<ns1:DocumentIdentifier scheme=\"busdox-docid-qns\">\n" +
                "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:www.cenbii.eu:transaction:biicoretrdm010:ver1.0:#urn:www.peppol.eu:bis:peppol4a:ver1.0::2.0\n" +
                "</ns1:DocumentIdentifier>\n" +
                "<ns0:ProcessList/>\n" +
                "</ns0:ServiceInformation>\n" +
                "</ns0:ServiceMetadata>\n" +
                "</ns0:CompleteServiceGroup>";


        BufferedReader in = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        String inputLine;
        StringBuffer response2 = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response2.append(inputLine);
        }
        in.close();
       System.out.println("Response *" + response2.toString()+"*");

*/



        List<DocumentIdentifier> documentIdentifiers = reader.parseDocumentIdentifiers(fetcherResponse);


    }

}
