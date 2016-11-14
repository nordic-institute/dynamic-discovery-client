/*
 * Copyright 2016 Dynamic Discovery Client Project
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/servlets/Docbb6d.pdf?id=31979
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */
package eu.europa.ec.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.core.locator.BDXRLocator;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.fetcher.URLFetcherMock;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.util.Constants;
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
    public void getDocumentIdentifierByNaptrOK2() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR, Constants.SERVICE_GROUP_URL_URN_POLAND_NCPB, Constants.SERVICE_GROUP_BODY_URN_POLAND_NCPB);

        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:ehealth:be:ncpb-idp", "ehealth-actorid-qns");

        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.lookupFetcher(participantIdentifier, "L6WYVSCV5TIX6VAH5YLULNSJJMKR2ZHM65J7RRCFZ6JYYCV7JXGQ.ehealth-actorid-qns.edelivery.tech.ec.europa.eu")).thenReturn(Constants.SMP_DOMAIN_ALIAS);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new BDXRLocator("edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .build();
        List<DocumentIdentifier> documentIdentifiers = smpClient.getDocumentIdentifiers(participantIdentifier);
        Assert.assertEquals(3, documentIdentifiers.size());
        Assert.assertEquals("urn::epsos##services:extended:epsos::105", documentIdentifiers.get(0).getDocumentIdentifier());
        Assert.assertEquals("ehealth-resid-qns", documentIdentifiers.get(1).getScheme());

    }

    @Test
    public void getDocumentIdentifierByCNAMEOK() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.CNAME, Constants.SERVICE_GROUP_URL_9925_0367302178, Constants.SERVICE_GROUP_BODY_9925_0367302178, "b-ed520c91b58f3e9f19714d8170aac5af.iso6523-actorid-upis.edelivery.tech.ec.europa.eu");

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new BDXRLocator("edelivery.tech.ec.europa.eu"))
                .fetcher(urlFetcherURL)
                .build();
        List<DocumentIdentifier> documentIdentifiers = smpClient.getDocumentIdentifiers(new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis"));
        Assert.assertEquals(3, documentIdentifiers.size());
        Assert.assertEquals("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", documentIdentifiers.get(0).getDocumentIdentifier());
        Assert.assertEquals("busdox-docid-qns", documentIdentifiers.get(1).getScheme());
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
    }

    @Test(expected = DNSLookupException.class)
    public void getDocumentIdentifierByCNAMENotOK() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.CNAME, Constants.SERVICE_GROUP_URL_9925_0367302178, Constants.SERVICE_GROUP_BODY_9925_0367302178, "b-12345678910.iso6523-actorid-upis.edelivery.tech.ec.europa.eu");

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new BDXRLocator("edelivery.tech.ec.europa.eu"))
                .fetcher(urlFetcherURL)
                .build();
        List<DocumentIdentifier> documentIdentifiers = smpClient.getDocumentIdentifiers(new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis"));
    }
}
