/*
 * (C) Copyright 2016 - European Commission | Dynamic Discovery Client
 *
 * https://ec.europa.eu/cefdigital/code/projects/EDELIVERY/repos/dynamic-discovery-client/browse
 *
 * Licensed under the LGPL, Version 2.1 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     dynamic-discovery\License_LGPL-2.1.txt or https://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Flávio W. R. Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 *
 */
package eu.europa.ec.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.core.locator.impl.DefaultBDXRLocator;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.impl.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.SignatureException;
import eu.europa.ec.dynamicdiscovery.fetcher.URLFetcherMock;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ServiceMetadata;
import eu.europa.ec.dynamicdiscovery.util.Constants;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;

public class ServiceMetadataIT extends AbstractIT {

    @Test
    public void getServiceMetadataNaptrOk1() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR, Constants.SERVICE_METADATA_URL_URN_POLAND_NCPB, "service_metadata_urn_poland_ncpb");
        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");
        Mockito.when(defaultDNSLookup.lookupFetcher(participantIdentifier, "DALXFO3CDYE5ZSLF5WAVCYQ3XGERI6ONUBJU5WAH3T77THFWCGEQ.ehealth-actorid-qns.ehealth.acc.edelivery.tech.ec.europa.eu")).thenReturn(Constants.SMP_DOMAIN_ALIAS);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("ehealth.acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .build();

        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns");
        ServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);

        Assert.assertEquals("urn::epsos##services:extended:epsos::107", serviceMetadata.getDocumentIdentifier().getIdentifier());
        Assert.assertEquals("ehealth-resid-qns::urn::epsos##services:extended:epsos::107", serviceMetadata.getDocumentIdentifier().getFullIdentifier());
        Assert.assertEquals("ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A107", serviceMetadata.getDocumentIdentifier().urlencoded());
        Assert.assertEquals("ehealth-resid-qns", serviceMetadata.getDocumentIdentifier().getScheme());
        Assert.assertEquals("urn:poland:ncpb", serviceMetadata.getParticipantIdentifier().getIdentifier());
        Assert.assertEquals("ehealth-actorid-qns", serviceMetadata.getParticipantIdentifier().getScheme());
        Assert.assertEquals(1, serviceMetadata.getEndpoints().size());
    }

    @Test
    public void getSignedServiceMetadataNaptrOk1() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR, Constants.SIGNED_SERVICE_METADATA_URL_URN_POLAND_NCPB, "signed_service_metadata_urn_poland_ncpb");
        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");
        Mockito.when(defaultDNSLookup.lookupFetcher(participantIdentifier, "DALXFO3CDYE5ZSLF5WAVCYQ3XGERI6ONUBJU5WAH3T77THFWCGEQ.ehealth-actorid-qns.ehealth.acc.edelivery.tech.ec.europa.eu")).thenReturn(Constants.SMP_DOMAIN_ALIAS);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("ehealth.acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .build();

        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns");
        ServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);

        Assert.assertEquals("urn::epsos##services:extended:epsos::107", serviceMetadata.getDocumentIdentifier().getIdentifier());
        Assert.assertEquals("ehealth-resid-qns::urn::epsos##services:extended:epsos::107", serviceMetadata.getDocumentIdentifier().getFullIdentifier());
        Assert.assertEquals("ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A107", serviceMetadata.getDocumentIdentifier().urlencoded());
        Assert.assertEquals("ehealth-resid-qns", serviceMetadata.getDocumentIdentifier().getScheme());
        Assert.assertEquals("urn:poland:ncpb", serviceMetadata.getParticipantIdentifier().getIdentifier());
        Assert.assertEquals("ehealth-actorid-qns", serviceMetadata.getParticipantIdentifier().getScheme());
        Assert.assertEquals(1, serviceMetadata.getEndpoints().size());
    }

    @Test
    public void getServiceMetadataCnameOk1() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.CNAME, Constants.SERVICE_METADATA_URL_URN_POLAND_NCPB, "service_metadata_urn_poland_ncpb", "b-adb4c6d3821d142c684b13ed269fad65.ehealth-actorid-qns.ehealth.acc.edelivery.tech.ec.europa.eu");

        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");
        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns");

        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.lookupFetcher(participantIdentifier, "DALXFO3CDYE5ZSLF5WAVCYQ3XGERI6ONUBJU5WAH3T77THFWCGEQ.ehealth-actorid-qns.ehealth.acc.edelivery.tech.ec.europa.eu")).thenReturn(null);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("ehealth.acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .build();

        ServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);
        Assert.assertEquals("urn::epsos##services:extended:epsos::107", serviceMetadata.getDocumentIdentifier().getIdentifier());
        Assert.assertEquals("ehealth-resid-qns::urn::epsos##services:extended:epsos::107", serviceMetadata.getDocumentIdentifier().getFullIdentifier());
        Assert.assertEquals("ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A107", serviceMetadata.getDocumentIdentifier().urlencoded());
        Assert.assertEquals("ehealth-resid-qns", serviceMetadata.getDocumentIdentifier().getScheme());
        Assert.assertEquals("urn:poland:ncpb", serviceMetadata.getParticipantIdentifier().getIdentifier());
        Assert.assertEquals("ehealth-actorid-qns", serviceMetadata.getParticipantIdentifier().getScheme());
        Assert.assertEquals(1, serviceMetadata.getEndpoints().size());
    }

    @Test
    public void getSignedServiceMetadataCnameOk1() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.CNAME, Constants.SIGNED_SERVICE_METADATA_URL_URN_POLAND_NCPB, "signed_service_metadata_urn_poland_ncpb", "b-adb4c6d3821d142c684b13ed269fad65.ehealth-actorid-qns.ehealth.acc.edelivery.tech.ec.europa.eu");

        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");
        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns");

        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.lookupFetcher(participantIdentifier, "DALXFO3CDYE5ZSLF5WAVCYQ3XGERI6ONUBJU5WAH3T77THFWCGEQ.ehealth-actorid-qns.ehealth.acc.edelivery.tech.ec.europa.eu")).thenReturn(null);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("ehealth.acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .build();

        ServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);
        Assert.assertEquals("urn::epsos##services:extended:epsos::107", serviceMetadata.getDocumentIdentifier().getIdentifier());
        Assert.assertEquals("ehealth-resid-qns::urn::epsos##services:extended:epsos::107", serviceMetadata.getDocumentIdentifier().getFullIdentifier());
        Assert.assertEquals("ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A107", serviceMetadata.getDocumentIdentifier().urlencoded());
        Assert.assertEquals("ehealth-resid-qns", serviceMetadata.getDocumentIdentifier().getScheme());
        Assert.assertEquals("urn:poland:ncpb", serviceMetadata.getParticipantIdentifier().getIdentifier());
        Assert.assertEquals("ehealth-actorid-qns", serviceMetadata.getParticipantIdentifier().getScheme());
        Assert.assertEquals(1, serviceMetadata.getEndpoints().size());
    }


    @Test
    public void getServiceMetadataNaptrOk2() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR, Constants.SERVICE_METADATA_URL_9915_123456789, "service_metadata_9915_123456789");
        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("9915:123456789", "iso6523-actorid-upis");
        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", "bdxr-docid-qns");

        Mockito.when(defaultDNSLookup.lookupFetcher(participantIdentifier, "L7KCFF3BPTJLMZWOPCTSIAG4CTMUFMH2EEELHVL5QQ52JYPALDTA.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu")).thenReturn(Constants.SMP_DOMAIN_ALIAS);
        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .build();

        ServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);

        Assert.assertEquals("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", serviceMetadata.getDocumentIdentifier().getIdentifier());
        Assert.assertEquals("bdxr-docid-qns::urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", serviceMetadata.getDocumentIdentifier().getFullIdentifier());
        Assert.assertEquals("bdxr-docid-qns%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3ACreditNote-2%3A%3ACreditNote%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns014%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol5a%3Aver2.0%3A%3A2.1", serviceMetadata.getDocumentIdentifier().urlencoded());
        Assert.assertEquals("bdxr-docid-qns", serviceMetadata.getDocumentIdentifier().getScheme());
        Assert.assertEquals("9915:123456789", serviceMetadata.getParticipantIdentifier().getIdentifier());
        Assert.assertEquals("iso6523-actorid-upis", serviceMetadata.getParticipantIdentifier().getScheme());
        Assert.assertEquals(1, serviceMetadata.getEndpoints().size());
    }

    @Test
    public void getSignedServiceMetadataNaptrOk2() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR, Constants.SIGNED_SERVICE_METADATA_URL_9915_123456789, "signed_service_metadata_9915_123456789");
        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("9915:123456789", "iso6523-actorid-upis");
        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", "bdxr-docid-qns");

        Mockito.when(defaultDNSLookup.lookupFetcher(participantIdentifier, "L7KCFF3BPTJLMZWOPCTSIAG4CTMUFMH2EEELHVL5QQ52JYPALDTA.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu")).thenReturn(Constants.SMP_DOMAIN_ALIAS);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .build();

        ServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);

        Assert.assertEquals("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", serviceMetadata.getDocumentIdentifier().getIdentifier());
        Assert.assertEquals("bdxr-docid-qns::urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", serviceMetadata.getDocumentIdentifier().getFullIdentifier());
        Assert.assertEquals("bdxr-docid-qns%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3ACreditNote-2%3A%3ACreditNote%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns014%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol5a%3Aver2.0%3A%3A2.1", serviceMetadata.getDocumentIdentifier().urlencoded());
        Assert.assertEquals("bdxr-docid-qns", serviceMetadata.getDocumentIdentifier().getScheme());
        Assert.assertEquals("9915:123456789", serviceMetadata.getParticipantIdentifier().getIdentifier());
        Assert.assertEquals("iso6523-actorid-upis", serviceMetadata.getParticipantIdentifier().getScheme());
        Assert.assertEquals(1, serviceMetadata.getEndpoints().size());
    }

    @Test
    public void getServiceMetadataCnameOk2() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.CNAME, Constants.SERVICE_METADATA_URL_9915_123456789, "service_metadata_9915_123456789", "b-ce8f928e3ad220c389fb2d3790e76119.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu");

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("acc.edelivery.tech.ec.europa.eu"))
                .fetcher(urlFetcherURL)
                .build();
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("9915:123456789", "iso6523-actorid-upis");
        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", "bdxr-docid-qns");

        ServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);
        Assert.assertEquals("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", serviceMetadata.getDocumentIdentifier().getIdentifier());
        Assert.assertEquals("bdxr-docid-qns::urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", serviceMetadata.getDocumentIdentifier().getFullIdentifier());
        Assert.assertEquals("bdxr-docid-qns%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3ACreditNote-2%3A%3ACreditNote%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns014%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol5a%3Aver2.0%3A%3A2.1", serviceMetadata.getDocumentIdentifier().urlencoded());
        Assert.assertEquals("bdxr-docid-qns", serviceMetadata.getDocumentIdentifier().getScheme());
        Assert.assertEquals("9915:123456789", serviceMetadata.getParticipantIdentifier().getIdentifier());
        Assert.assertEquals("iso6523-actorid-upis", serviceMetadata.getParticipantIdentifier().getScheme());
        Assert.assertEquals(1, serviceMetadata.getEndpoints().size());
    }

    @Test
    public void getSignedServiceMetadataCnameOk2() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.CNAME, Constants.SIGNED_SERVICE_METADATA_URL_9915_123456789, "signed_service_metadata_9915_123456789", "b-ce8f928e3ad220c389fb2d3790e76119.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu");

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("acc.edelivery.tech.ec.europa.eu"))
                .fetcher(urlFetcherURL)
                .build();
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("9915:123456789", "iso6523-actorid-upis");
        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", "bdxr-docid-qns");

        ServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);
        Assert.assertEquals("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", serviceMetadata.getDocumentIdentifier().getIdentifier());
        Assert.assertEquals("bdxr-docid-qns::urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", serviceMetadata.getDocumentIdentifier().getFullIdentifier());
        Assert.assertEquals("bdxr-docid-qns%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3ACreditNote-2%3A%3ACreditNote%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns014%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol5a%3Aver2.0%3A%3A2.1", serviceMetadata.getDocumentIdentifier().urlencoded());
        Assert.assertEquals("bdxr-docid-qns", serviceMetadata.getDocumentIdentifier().getScheme());
        Assert.assertEquals("9915:123456789", serviceMetadata.getParticipantIdentifier().getIdentifier());
        Assert.assertEquals("iso6523-actorid-upis", serviceMetadata.getParticipantIdentifier().getScheme());
        Assert.assertEquals(1, serviceMetadata.getEndpoints().size());
    }

    @Test(expected = SignatureException.class)
    public void getSignedServiceMetadataNaptrInvalidSignature1() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR, Constants.SIGNED_SERVICE_METADATA_URL_URN_POLAND_NCPB, "signed_service_metadata_urn_poland_ncpb_invalid_signature");
        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");
        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns");

        Mockito.when(defaultDNSLookup.lookupFetcher(participantIdentifier, "DALXFO3CDYE5ZSLF5WAVCYQ3XGERI6ONUBJU5WAH3T77THFWCGEQ.ehealth-actorid-qns.ehealth.acc.edelivery.tech.ec.europa.eu")).thenReturn(Constants.SMP_DOMAIN_ALIAS);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("ehealth.acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .build();

        ServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);
    }

    @Test(expected = SignatureException.class)
    public void getSignedServiceMetadataNaptrInvalidSignature2() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR, Constants.SIGNED_SERVICE_METADATA_URL_9915_123456789, "signed_service_metadata_9915_123456789_invalid_signature");
        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("9915:123456789", "iso6523-actorid-upis");
        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", "bdxr-docid-qns");

        Mockito.when(defaultDNSLookup.lookupFetcher(participantIdentifier, "L7KCFF3BPTJLMZWOPCTSIAG4CTMUFMH2EEELHVL5QQ52JYPALDTA.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu")).thenReturn(Constants.SMP_DOMAIN_ALIAS);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .build();
        ServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);
    }


    @Test(expected = DNSLookupException.class)
    public void getServiceMetadataCnameNotOk() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.CNAME, Constants.SERVICE_METADATA_URL_URN_POLAND_NCPB, "service_metadata_urn_poland_ncpb", "b-123456.ehealth-actorid-qns.acc.edelivery.tech.ec.europa.eu");

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("acc.edelivery.tech.ec.europa.eu"))
                .fetcher(urlFetcherURL)
                .build();

        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:ehealth:pt:ncpb-idp", "ehealth-actorid-qns");
        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn::epsos##services:extended:epsos::105", "ehealth-resid-qns");
        ServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);
    }


    @Test(expected = DNSLookupException.class)
    public void getServiceMetadataNaptrParticipantIdentifierNotOk() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR, Constants.SERVICE_METADATA_URL_URN_POLAND_NCPB, "service_metadata_urn_poland_ncpb");
        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.lookupFetcher(new ParticipantIdentifier("urn:ehealth:pt:ncpb-idp", "ehealth-actorid-qns"), "TTBA75HVAPVICNGX4N3FZJDS7Z6Q7H7MF2GQSLDJTN2UJV4TV6WQ.ehealth-actorid-qns.acc.edelivery.tech.ec.europa.eu")).thenReturn(Constants.SMP_DOMAIN_ALIAS);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .build();

        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:ehealth:pt:ncpb-idp123", "ehealth-actorid-qns");
        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn::epsos##services:extended:epsos::105", "ehealth-resid-qns");
        ServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);
    }
}
