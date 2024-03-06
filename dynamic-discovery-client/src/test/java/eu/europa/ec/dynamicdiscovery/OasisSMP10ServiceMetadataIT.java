/*
 * #%L
 * dynamic-discovery-cli
 * %%
 * Copyright (C) 2016 - 2023 European Commission | eDelivery | Dynamic Discovery Client
 * %%
 * Licensed under the LGPL, Version 2.1 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * [PROJECT_HOME]\license\lgpl2-1\license.txt or https://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package eu.europa.ec.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.core.fetcher.URLFetcherMock;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.impl.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.core.locator.impl.DefaultBDXRLocator;
import eu.europa.ec.dynamicdiscovery.core.provider.impl.DefaultProvider;
import eu.europa.ec.dynamicdiscovery.core.reader.impl.DefaultBDXRReader;
import eu.europa.ec.dynamicdiscovery.core.security.impl.DefaultSignatureValidator;
import eu.europa.ec.dynamicdiscovery.enums.DNSLookupType;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.SignatureException;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import eu.europa.ec.dynamicdiscovery.util.TestCaseConstants;
import gen.eu.europa.ec.ddc.api.smp10.SignedServiceMetadata;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;

/**
 * @author Flávio W. R. Santos
 * @since 1.0
 */
class OasisSMP10ServiceMetadataIT {



    @Test
   void getServiceMetadataNaptrOk1() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR, TestCaseConstants.SERVICE_METADATA_URL_URN_POLAND_NCPB, "signed_service_metadata_urn_poland_ncpb");
        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");
        Mockito.when(defaultDNSLookup.naptrUrlValueLookup(participantIdentifier, "DALXFO3CDYE5ZSLF5WAVCYQ3XGERI6ONUBJU5WAH3T77THFWCGEQ.ehealth-actorid-qns.ehealth.acc.edelivery.tech.ec.europa.eu")).thenReturn(TestCaseConstants.SMP_DOMAIN_ALIAS);


        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("ehealth.acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .reader(new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts"))))
                .fetcher(urlFetcherURL)
                .build();


        DefaultProvider defaultProvider = (DefaultProvider) smpClient.getService().getMetadataProvider();
        SMPDocumentIdentifier documentIdentifier = new SMPDocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns");
        SMPServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);

        assertEquals("urn::epsos##services:extended:epsos::107", serviceMetadata.getDocumentIdentifier().getIdentifier());
        assertEquals("ehealth-resid-qns::urn::epsos##services:extended:epsos::107", defaultProvider.format(serviceMetadata.getDocumentIdentifier()));
        assertEquals("ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A107", defaultProvider.urlEncodedFormat(serviceMetadata.getDocumentIdentifier()));
        assertEquals("ehealth-resid-qns", serviceMetadata.getDocumentIdentifier().getScheme());
        assertEquals("urn:poland:ncpb", serviceMetadata.getParticipantIdentifier().getIdentifier());
        assertEquals("ehealth-actorid-qns", serviceMetadata.getParticipantIdentifier().getScheme());
        assertEquals(1, serviceMetadata.getEndpoints().size());
    }

    @Test
   void getSignedServiceMetadataNaptrOk1() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR, TestCaseConstants.SIGNED_SERVICE_METADATA_URL_URN_POLAND_NCPB, "signed_service_metadata_urn_poland_ncpb");
        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");
        Mockito.when(defaultDNSLookup.naptrUrlValueLookup(participantIdentifier, "DALXFO3CDYE5ZSLF5WAVCYQ3XGERI6ONUBJU5WAH3T77THFWCGEQ.ehealth-actorid-qns.ehealth.acc.edelivery.tech.ec.europa.eu")).thenReturn(TestCaseConstants.SMP_DOMAIN_ALIAS);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("ehealth.acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .reader(new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts"))))
                .build();

        DefaultProvider defaultProvider = (DefaultProvider) smpClient.getService().getMetadataProvider();
        SMPDocumentIdentifier documentIdentifier = new SMPDocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns");
        SMPServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);

        assertEquals("urn::epsos##services:extended:epsos::107", serviceMetadata.getDocumentIdentifier().getIdentifier());
        assertEquals("ehealth-resid-qns::urn::epsos##services:extended:epsos::107", defaultProvider.format(serviceMetadata.getDocumentIdentifier()));
        assertEquals("ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A107", defaultProvider.urlEncodedFormat(serviceMetadata.getDocumentIdentifier()));
        assertEquals("ehealth-resid-qns", serviceMetadata.getDocumentIdentifier().getScheme());
        assertEquals("urn:poland:ncpb", serviceMetadata.getParticipantIdentifier().getIdentifier());
        assertEquals("ehealth-actorid-qns", serviceMetadata.getParticipantIdentifier().getScheme());
        assertEquals(1, serviceMetadata.getEndpoints().size());
    }

    @Test
   void getServiceMetadataCnameOk1() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(CommonUtil.OASIS_SMP_10, URLFetcherMock.LookupType.CNAME, TestCaseConstants.SERVICE_METADATA_URL_URN_POLAND_NCPB, "signed_service_metadata_urn_poland_ncpb", "b-adb4c6d3821d142c684b13ed269fad65.ehealth-actorid-qns.ehealth.acc.edelivery.tech.ec.europa.eu");

        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");
        SMPDocumentIdentifier documentIdentifier = new SMPDocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns");

        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.naptrUrlValueLookup(participantIdentifier, "DALXFO3CDYE5ZSLF5WAVCYQ3XGERI6ONUBJU5WAH3T77THFWCGEQ.ehealth-actorid-qns.ehealth.acc.edelivery.tech.ec.europa.eu")).thenReturn(null);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("ehealth.acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .reader(new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts"))))
                .build();

        DefaultProvider defaultProvider = (DefaultProvider) smpClient.getService().getMetadataProvider();
        SMPServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);
        assertEquals("urn::epsos##services:extended:epsos::107", serviceMetadata.getDocumentIdentifier().getIdentifier());
        assertEquals("ehealth-resid-qns::urn::epsos##services:extended:epsos::107", defaultProvider.format(serviceMetadata.getDocumentIdentifier()));
        assertEquals("ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A107", defaultProvider.urlEncodedFormat(serviceMetadata.getDocumentIdentifier()));
        assertEquals("ehealth-resid-qns", serviceMetadata.getDocumentIdentifier().getScheme());
        assertEquals("urn:poland:ncpb", serviceMetadata.getParticipantIdentifier().getIdentifier());
        assertEquals("ehealth-actorid-qns", serviceMetadata.getParticipantIdentifier().getScheme());
        assertEquals(1, serviceMetadata.getEndpoints().size());
    }

    @Test
   void getSignedServiceMetadataCnameOk1() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(CommonUtil.OASIS_SMP_10, URLFetcherMock.LookupType.CNAME, TestCaseConstants.SIGNED_SERVICE_METADATA_URL_URN_POLAND_NCPB, "signed_service_metadata_urn_poland_ncpb", "b-adb4c6d3821d142c684b13ed269fad65.ehealth-actorid-qns.ehealth.acc.edelivery.tech.ec.europa.eu");

        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");
        SMPDocumentIdentifier documentIdentifier = new SMPDocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns");

        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.naptrUrlValueLookup(participantIdentifier,
                "DALXFO3CDYE5ZSLF5WAVCYQ3XGERI6ONUBJU5WAH3T77THFWCGEQ.ehealth-actorid-qns.ehealth.acc.edelivery.tech.ec.europa.eu"))
                .thenReturn(null);

        Mockito.when(defaultDNSLookup.dnsRecordNotExists(eq(participantIdentifier),
                        anyString(),
                        eq(DNSLookupType.CNAME)))
                .thenReturn(false);


        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("ehealth.acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .reader(new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts"))))
                .build();
        DefaultProvider defaultProvider = (DefaultProvider) smpClient.getService().getMetadataProvider();

        SMPServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);

        assertEquals("urn::epsos##services:extended:epsos::107", serviceMetadata.getDocumentIdentifier().getIdentifier());
        assertEquals("ehealth-resid-qns::urn::epsos##services:extended:epsos::107", defaultProvider.format(serviceMetadata.getDocumentIdentifier()));
        assertEquals("ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A107", defaultProvider.urlEncodedFormat(serviceMetadata.getDocumentIdentifier()));
        assertEquals("ehealth-resid-qns", serviceMetadata.getDocumentIdentifier().getScheme());
        assertEquals("urn:poland:ncpb", serviceMetadata.getParticipantIdentifier().getIdentifier());
        assertEquals("ehealth-actorid-qns", serviceMetadata.getParticipantIdentifier().getScheme());
        assertEquals(1, serviceMetadata.getEndpoints().size());
    }


    @Test
   void getServiceMetadataNaptrOk2() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR, TestCaseConstants.SERVICE_METADATA_URL_9915_123456789, "signed_service_metadata_signed_valid_iso6523");
        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("9915:123456789", "iso6523-actorid-upis");
        SMPDocumentIdentifier documentIdentifier = new SMPDocumentIdentifier("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", "bdxr-docid-qns");

        Mockito.when(defaultDNSLookup.naptrUrlValueLookup(participantIdentifier, "L7KCFF3BPTJLMZWOPCTSIAG4CTMUFMH2EEELHVL5QQ52JYPALDTA.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu")).thenReturn(TestCaseConstants.SMP_DOMAIN_ALIAS);
        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .reader(new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts"))))
                .build();

        DefaultProvider defaultProvider = (DefaultProvider) smpClient.getService().getMetadataProvider();
        SMPServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);

        assertEquals("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", serviceMetadata.getDocumentIdentifier().getIdentifier());
        assertEquals("bdxr-docid-qns::urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", defaultProvider.format(serviceMetadata.getDocumentIdentifier()));
        assertEquals("bdxr-docid-qns%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3ACreditNote-2%3A%3ACreditNote%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns014%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol5a%3Aver2.0%3A%3A2.1", defaultProvider.urlEncodedFormat(serviceMetadata.getDocumentIdentifier()));
        assertEquals("bdxr-docid-qns", serviceMetadata.getDocumentIdentifier().getScheme());
        assertEquals("9915:123456789", serviceMetadata.getParticipantIdentifier().getIdentifier());
        assertEquals("iso6523-actorid-upis", serviceMetadata.getParticipantIdentifier().getScheme());
        assertEquals(1, serviceMetadata.getEndpoints().size());
    }

    @Test
   void getSignedServiceMetadataNaptrOk2() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR, TestCaseConstants.SERVICE_METADATA_URL_9915_123456789, "signed_service_metadata_signed_valid_iso6523");
        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("9915:123456789", "iso6523-actorid-upis");
        SMPDocumentIdentifier documentIdentifier = new SMPDocumentIdentifier("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", "bdxr-docid-qns");

        Mockito.when(defaultDNSLookup.naptrUrlValueLookup(participantIdentifier, "L7KCFF3BPTJLMZWOPCTSIAG4CTMUFMH2EEELHVL5QQ52JYPALDTA.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu")).thenReturn(TestCaseConstants.SMP_DOMAIN_ALIAS);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .reader(new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts"))))
                .build();

        SMPServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);

        assertEquals("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", serviceMetadata.getDocumentIdentifier().getIdentifier());
        assertEquals("bdxr-docid-qns", serviceMetadata.getDocumentIdentifier().getScheme());
        assertEquals("9915:123456789", serviceMetadata.getParticipantIdentifier().getIdentifier());
        assertEquals("iso6523-actorid-upis", serviceMetadata.getParticipantIdentifier().getScheme());
        assertEquals(1, serviceMetadata.getEndpoints().size());
    }

    @Test
   void getServiceMetadataCnameOk2() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(CommonUtil.OASIS_SMP_10, URLFetcherMock.LookupType.CNAME, TestCaseConstants.SERVICE_METADATA_URL_9915_123456789, "signed_service_metadata_signed_valid_iso6523", "b-ce8f928e3ad220c389fb2d3790e76119.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu");

        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("9915:123456789", "iso6523-actorid-upis");
        SMPDocumentIdentifier documentIdentifier = new SMPDocumentIdentifier("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", "bdxr-docid-qns");

        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.dnsRecordNotExists(eq(participantIdentifier),
                        anyString(),
                        eq(DNSLookupType.CNAME)))
                .thenReturn(false);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .reader(new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts"))))
                .build();

        SMPServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);
        assertEquals("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", serviceMetadata.getDocumentIdentifier().getIdentifier());
        assertEquals("bdxr-docid-qns", serviceMetadata.getDocumentIdentifier().getScheme());
        assertEquals("9915:123456789", serviceMetadata.getParticipantIdentifier().getIdentifier());
        assertEquals("iso6523-actorid-upis", serviceMetadata.getParticipantIdentifier().getScheme());
        assertEquals(1, serviceMetadata.getEndpoints().size());
    }

    @Test
   void getSignedServiceMetadataCnameOk2() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(CommonUtil.OASIS_SMP_10, URLFetcherMock.LookupType.CNAME,
                TestCaseConstants.SERVICE_METADATA_URL_9915_123456789,
                "signed_service_metadata_signed_valid_iso6523",
                "b-ce8f928e3ad220c389fb2d3790e76119.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu");

        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("9915:123456789", "iso6523-actorid-upis");
        SMPDocumentIdentifier documentIdentifier = new SMPDocumentIdentifier("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", "bdxr-docid-qns");

        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.dnsRecordNotExists(eq(participantIdentifier),
                        anyString(),
                        eq(DNSLookupType.CNAME)))
                .thenReturn(false);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .reader(new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts"))))
                .build();

        SMPServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);
        assertEquals("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", serviceMetadata.getDocumentIdentifier().getIdentifier());
        assertEquals("bdxr-docid-qns", serviceMetadata.getDocumentIdentifier().getScheme());
        assertEquals("9915:123456789", serviceMetadata.getParticipantIdentifier().getIdentifier());
        assertEquals("iso6523-actorid-upis", serviceMetadata.getParticipantIdentifier().getScheme());
        assertEquals(1, serviceMetadata.getEndpoints().size());
    }

    @Test
   void getSignedServiceMetadataNaptrInvalidSignature1() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR, TestCaseConstants.SIGNED_SERVICE_METADATA_URL_URN_POLAND_NCPB, "signed_service_metadata_urn_poland_ncpb_invalid_signature");
        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");
        SMPDocumentIdentifier documentIdentifier = new SMPDocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns");

        Mockito.when(defaultDNSLookup.naptrUrlValueLookup(participantIdentifier, "DALXFO3CDYE5ZSLF5WAVCYQ3XGERI6ONUBJU5WAH3T77THFWCGEQ.ehealth-actorid-qns.ehealth.acc.edelivery.tech.ec.europa.eu")).thenReturn(TestCaseConstants.SMP_DOMAIN_ALIAS);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("ehealth.acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .reader(new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts"))))
                .build();

        assertThrows(SignatureException.class, () -> smpClient.getServiceMetadata(participantIdentifier, documentIdentifier));
    }

    @Test
   void getSignedServiceMetadataNaptrInvalidSignature2() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR, TestCaseConstants.SERVICE_METADATA_URL_9915_123456789, "signed_service_metadata_urn_poland_ncpb_invalid_signature");
        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("9915:123456789", "iso6523-actorid-upis");
        SMPDocumentIdentifier documentIdentifier = new SMPDocumentIdentifier("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", "bdxr-docid-qns");

        Mockito.when(defaultDNSLookup.naptrUrlValueLookup(participantIdentifier, "L7KCFF3BPTJLMZWOPCTSIAG4CTMUFMH2EEELHVL5QQ52JYPALDTA.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu")).thenReturn(TestCaseConstants.SMP_DOMAIN_ALIAS);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .reader(new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts"))))
                .build();
        assertThrows(SignatureException.class, ()-> smpClient.getServiceMetadata(participantIdentifier, documentIdentifier));
    }


    @Test
   void getServiceMetadataCnameNotOk() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(CommonUtil.OASIS_SMP_10, URLFetcherMock.LookupType.CNAME, TestCaseConstants.SERVICE_METADATA_URL_URN_POLAND_NCPB, "service_metadata_urn_poland_ncpb", "b-123456.ehealth-actorid-qns.acc.edelivery.tech.ec.europa.eu");

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator.Builder().addTopDnsDomain("acc.edelivery.tech.ec.europa.eu").build())
                .fetcher(urlFetcherURL)
                .reader(new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts"))))
                .build();

        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("urn:ehealth:pt:ncpb-idp", "ehealth-actorid-qns");
        SMPDocumentIdentifier documentIdentifier = new SMPDocumentIdentifier("urn::epsos##services:extended:epsos::105", "ehealth-resid-qns");

        assertThrows(DNSLookupException.class, ()-> smpClient.getServiceMetadata(participantIdentifier, documentIdentifier));
    }


    @Test
   void getServiceMetadataNaptrParticipantIdentifierNotOk() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR, TestCaseConstants.SERVICE_METADATA_URL_URN_POLAND_NCPB, "service_metadata_urn_poland_ncpb");
        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.naptrUrlValueLookup(new SMPParticipantIdentifier("urn:ehealth:pt:ncpb-idp", "ehealth-actorid-qns"), "TTBA75HVAPVICNGX4N3FZJDS7Z6Q7H7MF2GQSLDJTN2UJV4TV6WQ.ehealth-actorid-qns.acc.edelivery.tech.ec.europa.eu")).thenReturn(TestCaseConstants.SMP_DOMAIN_ALIAS);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .reader(new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts"))))
                .build();

        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("urn:ehealth:pt:ncpb-idp123", "ehealth-actorid-qns");
        SMPDocumentIdentifier documentIdentifier = new SMPDocumentIdentifier("urn::epsos##services:extended:epsos::105", "ehealth-resid-qns");

        assertThrows(DNSLookupException.class, ()-> smpClient.getServiceMetadata(participantIdentifier, documentIdentifier));
    }

    @Test
   void testSignedServiceMetadata() throws Exception {
        SignedServiceMetadata SignedServiceMetadata = (SignedServiceMetadata) getSignedServiceMetada("extension", "urn:poland:ncpb", false);

        assertEquals("urn::epsos##services:extended:epsos::107", SignedServiceMetadata.getServiceMetadata().getServiceInformation().getDocumentIdentifier().getValue());
        assertEquals("ehealth-resid-qns", SignedServiceMetadata.getServiceMetadata().getServiceInformation().getDocumentIdentifier().getScheme());
        assertEquals(" DIGIT-B003", SignedServiceMetadata.getServiceMetadata().getServiceInformation().getExtensions().get(0).getExtensionAgencyID());
        assertEquals("urn:poland:ncpb", SignedServiceMetadata.getServiceMetadata().getServiceInformation().getParticipantIdentifier().getValue());
        assertEquals("ehealth-actorid-qns", SignedServiceMetadata.getServiceMetadata().getServiceInformation().getParticipantIdentifier().getScheme());
        assertEquals(1, SignedServiceMetadata.getServiceMetadata().getServiceInformation().getProcessList().getProcesses().size());
        assertEquals("urn:epsosPatientService::List", SignedServiceMetadata.getServiceMetadata().getServiceInformation().getProcessList().getProcesses().get(0).getProcessIdentifier().getValue());
        assertEquals("ehealth-procid-qns", SignedServiceMetadata.getServiceMetadata().getServiceInformation().getProcessList().getProcesses().get(0).getProcessIdentifier().getScheme());
    }

    @Test
   void testSignedServiceMetadataParticipantNotOk() throws Exception {

        assertThrows(DNSLookupException.class, ()-> getSignedServiceMetada("extension", "urn:poland:ncpb1", false));
    }

    private Object getSignedServiceMetada(String filename, String participantId, boolean deprecateServiceMetadata) throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(CommonUtil.OASIS_SMP_10, URLFetcherMock.LookupType.CNAME, TestCaseConstants.SIGNED_SERVICE_METADATA_URL_URN_POLAND_NCPB, filename, "b-adb4c6d3821d142c684b13ed269fad65.ehealth-actorid-qns.ehealth.acc.edelivery.tech.ec.europa.eu");

        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier(participantId, "ehealth-actorid-qns");
        SMPDocumentIdentifier documentIdentifier = new SMPDocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns");

        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.naptrUrlValueLookup(participantIdentifier, "DALXFO3CDYE5ZSLF5WAVCYQ3XGERI6ONUBJU5WAH3T77THFWCGEQ.ehealth-actorid-qns.ehealth.acc.edelivery.tech.ec.europa.eu")).thenReturn(null);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("ehealth.acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .fetcher(urlFetcherURL)
                .reader(new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts"))))
                .build();

        if (deprecateServiceMetadata) {
            return smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);
        }

        return smpClient.getServiceMetadata(participantIdentifier, documentIdentifier).unwrap(SignedServiceMetadata.class);
    }
}
