package eu.europa.ex.dynamicdiscovery;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import eu.europa.ec.dynamicdiscovery.DynamicDiscovery;
import eu.europa.ec.dynamicdiscovery.DynamicDiscoveryBuilder;
import eu.europa.ec.dynamicdiscovery.ServiceMetadata;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.locator.BDXRLocator;
import eu.europa.ec.dynamicdiscovery.locator.BusdoxLocator;
import eu.europa.ec.dynamicdiscovery.locator.dns.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ex.dynamicdiscovery.AbstractTest;
import eu.europa.ex.dynamicdiscovery.Constants;
import eu.europa.ex.dynamicdiscovery.fetcher.URLFetcherMock;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * Created by rodrfla on 30/09/2016.
 */
public class ServiceMetadataTest extends AbstractTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Test
    public void getDocumentIdentifierByNaptrForEhealthOK() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR, Constants.SERVICE_GROUP_URL_urn_ehealth_pt_ncpb_idp, Constants.SERVICE_GROUP_BODY_urn_ehealth_pt_ncpb_idp);
        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.lookupFetcher(new ParticipantIdentifier("urn:ehealth:pt:ncpb-idp", "ehealth-actorid-qns"), "TTBA75HVAPVICNGX4N3FZJDS7Z6Q7H7MF2GQSLDJTN2UJV4TV6WQ.ehealth-actorid-qns.edelivery.tech.ec.europa.eu")).thenReturn(Constants.SMP_DOMAIN_ALIAS);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new BDXRLocator("edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .build();
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:ehealth:pt:ncpb-idp", "ehealth-actorid-qns");
        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn::epsos##services:extended:epsos", "ehealth-resid-qns");
        List<DocumentIdentifier> documentIdentifiers = smpClient.getDocumentIdentifiers(new ParticipantIdentifier("urn:ehealth:pt:ncpb-idp", "ehealth-actorid-qns"));
        for (DocumentIdentifier doc : documentIdentifiers) {
            System.out.println("getIdentifier" + doc.getIdentifier());
            System.out.println("getScheme" + doc.getScheme());
        }
        Assert.assertEquals(documentIdentifiers.size(), 11);

        ServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifiers.get(0));
    }

    @Test
    public void getDocumentIdentifierByNaptrForEhealthOK2() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR, Constants.SERVICE_GROUP_URL_9925_0367302178, Constants.SERVICE_GROUP_BODY_9925_0367302178);
        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.lookupFetcher(new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis"), "ZR2ZGDOAGAVSHSQ2MRHXEZV2H6ATTQBF4JJ4J7VJNPYMRDZ3UG4Q.iso6523-actorid-upis.edelivery.tech.ec.europa.eu")).thenReturn(Constants.SMP_DOMAIN_ALIAS);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new BDXRLocator("edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .build();
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis");
        List<DocumentIdentifier> documentIdentifiers = smpClient.getDocumentIdentifiers(participantIdentifier);

        Assert.assertEquals(documentIdentifiers.size(), 3);
        for (DocumentIdentifier doc : documentIdentifiers) {
            System.out.println("getIdentifier " + doc.getIdentifier());
            System.out.println("getScheme " + doc.getScheme());
        }

        ServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifiers.get(0));
    }
}
