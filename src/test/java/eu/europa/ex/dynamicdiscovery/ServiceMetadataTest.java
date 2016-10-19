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
package eu.europa.ex.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.DynamicDiscovery;
import eu.europa.ec.dynamicdiscovery.DynamicDiscoveryBuilder;
import eu.europa.ec.dynamicdiscovery.core.locator.BDXRLocator;
import eu.europa.ec.dynamicdiscovery.core.locator.BusdoxLocator;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ServiceMetadata;
import eu.europa.ex.dynamicdiscovery.fetcher.URLFetcherMock;
import eu.europa.ex.dynamicdiscovery.util.Constants;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public class ServiceMetadataTest extends AbstractTest {

    @Test
    public void getServiceMetadataNaptrOk() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR, Constants.SERVICE_METADATA_URL_URN_GERMANY_NCPB, Constants.SERVICE_METADATA_BODY_URN_GERMANY_NCPB);
        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:germany:ncpb", "ehealth-participantid-qns");
        Mockito.when(defaultDNSLookup.lookupFetcher(participantIdentifier, "RWR4TB6ADUSN25GA64C5E53ZF5E3J2AYSFXZNTOKMJAXXCVLCMGQ.ehealth-participantid-qns.edelivery.tech.ec.europa.eu")).thenReturn(Constants.SMP_DOMAIN_ALIAS);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new BDXRLocator("edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .build();

        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn::epsos:services##epsos-21", "epsos-docid-qns");
        ServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);

        Assert.assertEquals("urn::epsos:services##epsos-21", serviceMetadata.getDocumentIdentifier().getDocumentIdentifier());
        Assert.assertEquals("epsos-docid-qns::urn::epsos:services##epsos-21", serviceMetadata.getDocumentIdentifier().getIdentifier());
        Assert.assertEquals("epsos-docid-qns%3A%3Aurn%3A%3Aepsos%3Aservices%23%23epsos-21", serviceMetadata.getDocumentIdentifier().urlencoded());
        Assert.assertEquals("epsos-docid-qns", serviceMetadata.getDocumentIdentifier().getScheme());
        Assert.assertEquals("urn:germany:ncpb", serviceMetadata.getParticipantIdentifier().getIdentifier());
        Assert.assertEquals("ehealth-participantid-qns", serviceMetadata.getParticipantIdentifier().getScheme());
        Assert.assertEquals(1, serviceMetadata.getEndpoints().size());
    }

    @Test
    public void getServiceMetadataCnameOk() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.CNAME, Constants.SERVICE_METADATA_URL_URN_GERMANY_NCPB, Constants.SERVICE_METADATA_BODY_URN_GERMANY_NCPB, "b-ce918d50184b5b0327efe7660bd44fde.ehealth-participantid-qns.edelivery.tech.ec.europa.eu");

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new BusdoxLocator("edelivery.tech.ec.europa.eu"))
                .fetcher(urlFetcherURL)
                .build();
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:germany:ncpb", "ehealth-participantid-qns");
        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn::epsos:services##epsos-21", "epsos-docid-qns");

        ServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);
        Assert.assertEquals("urn::epsos:services##epsos-21", serviceMetadata.getDocumentIdentifier().getDocumentIdentifier());
        Assert.assertEquals("epsos-docid-qns::urn::epsos:services##epsos-21", serviceMetadata.getDocumentIdentifier().getIdentifier());
        Assert.assertEquals("epsos-docid-qns%3A%3Aurn%3A%3Aepsos%3Aservices%23%23epsos-21", serviceMetadata.getDocumentIdentifier().urlencoded());
        Assert.assertEquals("epsos-docid-qns", serviceMetadata.getDocumentIdentifier().getScheme());
        Assert.assertEquals("urn:germany:ncpb", serviceMetadata.getParticipantIdentifier().getIdentifier());
        Assert.assertEquals("ehealth-participantid-qns", serviceMetadata.getParticipantIdentifier().getScheme());
        Assert.assertEquals(1, serviceMetadata.getEndpoints().size());
    }

    @Test
    public void getSignedServiceMetadataNaptrOk() throws Exception {
       /* URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR, Constants.SIGNED_SERVICE_METADATA_URL_urn_germany_ncpb, Constants.SIGNED_SERVICE_METADATA_BODY_urn_germany_ncpb);
        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:germany:ncpb", "ehealth-participantid-qns");
        Mockito.when(defaultDNSLookup.lookupFetcher(participantIdentifier, "RWR4TB6ADUSN25GA64C5E53ZF5E3J2AYSFXZNTOKMJAXXCVLCMGQ.ehealth-participantid-qns.edelivery.tech.ec.europa.eu")).thenReturn(Constants.SMP_DOMAIN_ALIAS);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new BDXRLocator("edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .build();

        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn:epsos::services##epsos-21", "ehealth-resid-qns");
        ServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);

        Assert.assertEquals("epsos-docid-qns::urn:epsos:services##epsos-21", serviceMetadata.getDocumentIdentifier().getDocumentIdentifier());
        Assert.assertEquals("urn:germany:ncpb", serviceMetadata.getParticipantIdentifier().getIdentifier());
        Assert.assertEquals("ehealth-participantid-qns", serviceMetadata.getParticipantIdentifier().getScheme());
        Assert.assertEquals(1, serviceMetadata.getEndpoints().size());*/
    }

    @Test(expected = DNSLookupException.class)
    public void getServiceMetadataCnameNotOk() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.CNAME, Constants.SERVICE_METADATA_URL_URN_EHEALTH_PT_NCPB_IDP, Constants.SERVICE_METADATA_BODY_URN_EHEALTH_PT_NCPB_IDP, "b-123456.ehealth-actorid-qns.edelivery.tech.ec.europa.eu");

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new BusdoxLocator("edelivery.tech.ec.europa.eu"))
                .fetcher(urlFetcherURL)
                .build();

        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:ehealth:pt:ncpb-idp", "ehealth-actorid-qns");
        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn::epsos##services:extended:epsos::105", "ehealth-resid-qns");
        ServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);
    }

    @Test(expected = DNSLookupException.class)
    public void getServiceMetadataNaptrNotOk() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR, Constants.SERVICE_METADATA_URL_URN_EHEALTH_PT_NCPB_IDP, Constants.SERVICE_METADATA_BODY_URN_EHEALTH_PT_NCPB_IDP);
        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.lookupFetcher(new ParticipantIdentifier("urn:ehealth:pt:ncpb-idp", "ehealth-actorid-qns"), "adfsdf54.ehealth-actorid-qns.edelivery.tech.ec.europa.eu")).thenReturn(Constants.SMP_DOMAIN_ALIAS);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new BDXRLocator("edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .build();

        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:ehealth:pt:ncpb-idp", "ehealth-actorid-qns");
        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn::epsos##services:extended:epsos::105", "ehealth-resid-qns");
        ServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);
    }

    @Test(expected = DNSLookupException.class)
    public void getServiceMetadataNaptrParticipantIdentifierNotOk() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR, Constants.SERVICE_METADATA_URL_URN_EHEALTH_PT_NCPB_IDP, Constants.SERVICE_METADATA_BODY_URN_EHEALTH_PT_NCPB_IDP);
        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.lookupFetcher(new ParticipantIdentifier("urn:ehealth:pt:ncpb-idp", "ehealth-actorid-qns"), "TTBA75HVAPVICNGX4N3FZJDS7Z6Q7H7MF2GQSLDJTN2UJV4TV6WQ.ehealth-actorid-qns.edelivery.tech.ec.europa.eu")).thenReturn(Constants.SMP_DOMAIN_ALIAS);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new BDXRLocator("edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .build();

        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:ehealth:pt:ncpb-idp123", "ehealth-actorid-qns");
        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn::epsos##services:extended:epsos::105", "ehealth-resid-qns");

        ServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);
        Assert.assertEquals("urn::epsos##services:extended:epsos::105", serviceMetadata.getDocumentIdentifier().getDocumentIdentifier());
        Assert.assertEquals("urn:ehealth:pt:ncpb-idp", serviceMetadata.getParticipantIdentifier().getIdentifier());
        Assert.assertEquals("ehealth-actorid-qns", serviceMetadata.getParticipantIdentifier().getScheme());
        Assert.assertEquals(1, serviceMetadata.getEndpoints().size());
    }

    @Test(expected = DNSLookupException.class)
    public void getServiceMetadataCnameParticipantIdentifierNotOk() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.CNAME, Constants.SERVICE_METADATA_URL_URN_EHEALTH_PT_NCPB_IDP, Constants.SERVICE_METADATA_BODY_URN_EHEALTH_PT_NCPB_IDP);
        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.lookupFetcher(new ParticipantIdentifier("urn:ehealth:pt:ncpb-idp", "ehealth-actorid-qns"), "b-0e2981c9c2044fd64711099b6f9dbe19.ehealth-actorid-qns.edelivery.tech.ec.europa.eu")).thenReturn(Constants.SMP_DOMAIN_ALIAS);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new BDXRLocator("edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .build();

        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:ehealth:pt:ncpb-idp123", "ehealth-actorid-qns");
        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn::epsos##services:extended:epsos::105", "ehealth-resid-qns");

        ServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);
        Assert.assertEquals("urn::epsos##services:extended:epsos::105", serviceMetadata.getDocumentIdentifier().getDocumentIdentifier());
        Assert.assertEquals("urn:ehealth:pt:ncpb-idp", serviceMetadata.getParticipantIdentifier().getIdentifier());
        Assert.assertEquals("ehealth-actorid-qns", serviceMetadata.getParticipantIdentifier().getScheme());
        Assert.assertEquals(1, serviceMetadata.getEndpoints().size());
    }
}
