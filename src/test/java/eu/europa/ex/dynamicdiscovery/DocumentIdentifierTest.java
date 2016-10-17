package eu.europa.ex.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.DynamicDiscovery;
import eu.europa.ec.dynamicdiscovery.DynamicDiscoveryBuilder;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.locator.BDXRLocator;
import eu.europa.ec.dynamicdiscovery.locator.BusdoxLocator;
import eu.europa.ec.dynamicdiscovery.locator.dns.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ex.dynamicdiscovery.fetcher.URLFetcherMock;
import eu.europa.ex.dynamicdiscovery.util.Constants;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public class DocumentIdentifierTest extends AbstractTest {

    @Test
    public void getDocumentIdentifierByNaptrOK() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR, Constants.SERVICE_GROUP_URL_9925_0367302178, Constants.SERVICE_GROUP_BODY_9925_0367302178);

        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis");

        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.lookupFetcher(participantIdentifier, "ZR2ZGDOAGAVSHSQ2MRHXEZV2H6ATTQBF4JJ4J7VJNPYMRDZ3UG4Q.iso6523-actorid-upis.edelivery.tech.ec.europa.eu")).thenReturn(Constants.SMP_DOMAIN_ALIAS);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new BDXRLocator("edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .build();
        List<DocumentIdentifier> documentIdentifiers = smpClient.getDocumentIdentifiers(participantIdentifier);
        Assert.assertEquals(3, documentIdentifiers.size());

    }

    @Test
    public void getDocumentIdentifierByCNAMEOK() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.CNAME, Constants.SERVICE_GROUP_URL_9925_0367302178, Constants.SERVICE_GROUP_BODY_9925_0367302178, "b-ed520c91b58f3e9f19714d8170aac5af.iso6523-actorid-upis.edelivery.tech.ec.europa.eu");

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new BusdoxLocator("edelivery.tech.ec.europa.eu"))
                .fetcher(urlFetcherURL)
                .build();
        List<DocumentIdentifier> documentIdentifiers = smpClient.getDocumentIdentifiers(new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis"));
        Assert.assertEquals(3, documentIdentifiers.size());
    }

    @Test(expected = DNSLookupException.class)
    public void getDocumentIdentifierNAPTRNotOK() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR, Constants.SERVICE_GROUP_URL_9925_0367302178, Constants.SERVICE_GROUP_BODY_9925_0367302178);
        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.lookupFetcher(new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis"), "ZR2ZGDOAGAVSHSQ2MRHXEZV2H6ATTQBF4JJ4J7VJNPYMRDZ3UG4Q.iso6523-actorid-upis.edelivery.tech.ec.europa.eu")).thenReturn(null);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new BDXRLocator("edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .build();
        List<DocumentIdentifier> documentIdentifiers = smpClient.getDocumentIdentifiers(new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis"));
        Assert.assertEquals(3, documentIdentifiers.size());
    }

    @Test(expected = DNSLookupException.class)
    public void getDocumentIdentifierByCNAMENotOK() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.CNAME, Constants.SERVICE_GROUP_URL_9925_0367302178, Constants.SERVICE_GROUP_BODY_9925_0367302178, "b-12345678910.iso6523-actorid-upis.edelivery.tech.ec.europa.eu");

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new BusdoxLocator("edelivery.tech.ec.europa.eu"))
                .fetcher(urlFetcherURL)
                .build();
        List<DocumentIdentifier> documentIdentifiers = smpClient.getDocumentIdentifiers(new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis"));
        Assert.assertEquals(3, documentIdentifiers.size());
    }
}
