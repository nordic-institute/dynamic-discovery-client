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

import eu.europa.ec.dynamicdiscovery.core.extension.impl.oasis10.OasisSMP10Extension;
import eu.europa.ec.dynamicdiscovery.core.extension.impl.peppol.PeppolSMPExtension;
import eu.europa.ec.dynamicdiscovery.core.fetcher.URLFetcherMock;
import eu.europa.ec.dynamicdiscovery.core.locator.PublisherLookupResult;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.impl.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.core.locator.impl.DefaultBDXRLocator;
import eu.europa.ec.dynamicdiscovery.core.locator.impl.StaticMapMetadataLocator;
import eu.europa.ec.dynamicdiscovery.core.provider.impl.DefaultDocumentRequestProvider;
import eu.europa.ec.dynamicdiscovery.core.reader.impl.DefaultBDXRReader;
import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.core.security.impl.DefaultSignatureValidator;
import eu.europa.ec.dynamicdiscovery.enums.DNSLookupType;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceGroup;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.service.impl.DynamicDiscoveryService;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import eu.europa.ec.dynamicdiscovery.util.DNSUtils;
import eu.europa.ec.dynamicdiscovery.util.TestCaseConstants;
import gen.eu.europa.ec.ddc.api.smp10.ServiceGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.FieldSource;
import org.mockito.Mockito;

import java.net.URI;
import java.util.List;

import static eu.europa.ec.dynamicdiscovery.util.DNSUtils.*;
import static eu.europa.ec.dynamicdiscovery.util.TestCaseConstants.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;

/**
 * @author Flávio W. R. Santos
 */
class DDCSearchSubresourceIT {


    static Object[][] documentIdentifierByNaptrTest = {
            {PARTY_9925_0367302178_FILE_RESOURCE, PARTY_9925_0367302178_IDENTIFIER, PARTY_9925_0367302178_SERVICE_GROUP_URL, PARTY_9925_0367302178_NAPTR_QUERY, 2},
            {PARTY_URL_URN_POLAND_NCPB_FILE_SM, PARTY_URL_URN_POLAND_NCPB_IDENTIFIER, PARTY_URL_URN_POLAND_NCPB_SERVICE_GROUP_URL, PARTY_URL_URN_POLAND_NCPB_NAPTR_QUERY, 2}
    };

    @ParameterizedTest
    @FieldSource("documentIdentifierByNaptrTest")
    void getDocumentIdentifierByNaptrOK(String filename, SMPParticipantIdentifier participantIdentifier, String urlContext, String requestHash, int expectedCount) throws Exception {
        // given
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR,
                urlContext,
                filename);

        List<PublisherLookupResult> publisherLookupResult = createMockPublisherLookupNaptrResult(participantIdentifier, TestCaseConstants.PUBLISHER_URL_02);
        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.naptrUrlValueLookup(participantIdentifier,
                        requestHash + "." + DNSUtils.TEST_TOP_DOMAIN_02))
                .thenReturn(publisherLookupResult);

        // build the client
        DynamicDiscoveryService smpClient = new DynamicDiscoveryService.Builder()
                .publisherLocator(new DefaultBDXRLocator.Builder()
                        .addTopDnsDomain(DNSUtils.TEST_TOP_DOMAIN_02)
                        .dnsLookup(defaultDNSLookup)
                        .build())
                .documentReader(new DefaultBDXRReader.Builder()
                        .signatureValidator(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts")))
                        .build())
                .documentFetcher(urlFetcherURL)
                .build();
        // when

        SMPServiceGroup result = smpClient.getResource(participantIdentifier);
        // then
        assertNotNull(result);
        ServiceGroup serviceGroup = result.unwrap(ServiceGroup.class);
        assertNotNull(result.unwrap(ServiceGroup.class));
        assertEquals(expectedCount, result.getDocumentIdentifiers().size());
        assertEquals(participantIdentifier.getIdentifier(), result.getParticipantIdentifier().getIdentifier());
        assertEquals(participantIdentifier.getScheme(), result.getParticipantIdentifier().getScheme());
        assertEquals(participantIdentifier.getIdentifier(), serviceGroup.getParticipantIdentifier().getValue());
        assertEquals(participantIdentifier.getScheme(), serviceGroup.getParticipantIdentifier().getScheme());
    }


    @Test
    void getDocumentIdentifierByCNAMEOK() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(CommonUtil.OASIS_SMP_10, URLFetcherMock.LookupType.CNAME, PARTY_9925_0367302178_SERVICE_GROUP_URL,
                PARTY_9925_0367302178_FILE_RESOURCE, PARTY_9925_0367302178_CNAME_QUERY + "." + TEST_TOP_DOMAIN_01);

        SMPParticipantIdentifier participantIdentifier = PARTY_9925_0367302178_IDENTIFIER;

        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.dnsRecordExists(participantIdentifier,
                PARTY_9925_0367302178_CNAME_QUERY + "." + TEST_TOP_DOMAIN_01,
                DNSLookupType.CNAME)).thenReturn(true);

        DynamicDiscoveryService smpClient = new DynamicDiscoveryService.Builder()
                .publisherLocator(new DefaultBDXRLocator.Builder()
                        .addTopDnsDomain(TEST_TOP_DOMAIN_01)
                        .dnsLookup(defaultDNSLookup)
                        .build())
                .documentReader(new DefaultBDXRReader.Builder()
                        .signatureValidator(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts")))
                        .build())
                .documentFetcher(urlFetcherURL)
                .build();

        List<SMPDocumentIdentifier> documentIdentifiers = smpClient.getResource(participantIdentifier).getDocumentIdentifiers();
        assertEquals(2, documentIdentifiers.size());
        assertEquals("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", documentIdentifiers.get(1).getIdentifier());
        assertEquals("bdx-docid-qns", documentIdentifiers.get(1).getScheme());
    }


    @Test
    void getDocumentIdentifierByStaticLookup() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.STATIC,
                "/cipa-smp-full-webapp" + TestCaseConstants.PARTY_URL_URN_POLAND_NCPB_SERVICE_GROUP_URL, "service_group_urn_poland_ncpb");

        DynamicDiscoveryService smpClient = new DynamicDiscoveryService.Builder()
                .publisherLocator(new StaticMapMetadataLocator(new URI(TestCaseConstants.SMP_STATIC_DOMAIN)))
                .documentReader(new DefaultBDXRReader.Builder()
                        .signatureValidator(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts")))
                        .build())
                .documentFetcher(urlFetcherURL)
                .build();

        List<SMPDocumentIdentifier> documentIdentifiers = smpClient.getResource(PARTY_URL_URN_POLAND_NCPB_IDENTIFIER).getDocumentIdentifiers();
        assertEquals(2, documentIdentifiers.size());
        assertEquals("urn::epsos:services##epsos-21", documentIdentifiers.get(0).getIdentifier());
        assertEquals("ehealth-resid-qns", documentIdentifiers.get(1).getScheme());
    }


    @Test
    void getDocumentIdentifierNAPTRNotOK() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.dnsRecordExists(any(SMPParticipantIdentifier.class),
                anyString(),
                any(DNSLookupType.class))).thenReturn(false);

        DynamicDiscoveryService smpClient = new DynamicDiscoveryService.Builder()
                .publisherLocator(new DefaultBDXRLocator.Builder()
                        .addTopDnsDomain(TEST_NAPTR_SERVICE_SMP1)
                        .dnsLookup(defaultDNSLookup)
                        .build())
                .documentReader(new DefaultBDXRReader.Builder()
                        .signatureValidator(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts")))
                        .build())
                .documentFetcher(urlFetcherURL)
                .build();


        assertThrows(DNSLookupException.class, () -> smpClient.getResource(PARTY_9925_0367302178_IDENTIFIER).getDocumentIdentifiers());
    }

    @Test
    void getDocumentIdentifierByCNAMENotOK() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(CommonUtil.OASIS_SMP_10, URLFetcherMock.LookupType.CNAME,
                PARTY_9925_0367302178_SERVICE_GROUP_URL,
                PARTY_9925_0367302178_FILE_RESOURCE, PARTY_9925_0367302178_CNAME_QUERY + "." + TEST_TOP_DOMAIN_01);

        DynamicDiscoveryService smpClient = new DynamicDiscoveryService.Builder()
                .publisherLocator(new DefaultBDXRLocator.Builder().addTopDnsDomain(TEST_TOP_DOMAIN_01).build())
                .documentReader(new DefaultBDXRReader.Builder()
                        .signatureValidator(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts")))
                        .build())
                .documentFetcher(urlFetcherURL)
                .build();
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis");

        assertThrows(DNSLookupException.class, () -> smpClient.getResource(participantIdentifier).getDocumentIdentifiers());

    }


    @Test
    void testGetServiceGroupTypeOk() throws Exception {
        SMPServiceGroup smoServiceGroup = getServiceGroupType("9925:0367302178");
        ServiceGroup serviceGroup = smoServiceGroup.unwrap(ServiceGroup.class);
        assertNotNull(serviceGroup);
        assertEquals("9925:0367302178", serviceGroup.getParticipantIdentifier().getValue());
        assertEquals("iso6523-actorid-upis", serviceGroup.getParticipantIdentifier().getScheme());
        assertEquals("http://cipa-smp-full-webapp/iso6523-actorid-upis%3A%3A9915%3A0367302178/services/bdx-docid-qns%3A%3Aurn%3A%3Aepsos%3Aservices%23%23epsos-21",
                serviceGroup.getServiceMetadataReferenceCollection().getServiceMetadataReferences().get(0).getHref());
    }

    @Test
    void testGetServiceGroupTypeParticipantNotOk() {
        DNSLookupException result = assertThrows(DNSLookupException.class, () -> getServiceGroupType("9925:00NotExistsInDNS"));
        assertThat(result.getMessage(), containsString("can not be resolved!"));
    }


    @Test
    void getPeppolDocumentIdentifierWithWildcardSchemeBasedOnDocumentIdentifierValuedWhichMatchesSMPWildcardDocument() throws Exception {

        String filenameSubresourceWildcard = "signed_service_metadata_valid_iso6523_wildcard";
        String filenameResourceWildcard = "peppol_service_group_valid_iso6523_wildcard";
        String subresourceURLPath = "/iso6523-actorid-upis%3A%3A9925%3A0367302178/services/peppol-doctype-wildcard%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3AInvoice-2%3A%3AInvoice%23%23urn%3Apeppol%3Apint%3Abilling-3.0%40jp%3Apeppol-1%2A%3A%3A2.1";


        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(CommonUtil.PEPPOL,
                URLFetcherMock.LookupType.CNAME,
                subresourceURLPath,
                filenameSubresourceWildcard,
                "b-ed520c91b58f3e9f19714d8170aac5af.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu");
        urlFetcherURL.addParameters(CommonUtil.PEPPOL, filenameResourceWildcard, PARTY_9925_0367302178_SERVICE_GROUP_URL);

        final SMPDocumentIdentifier smpDocumentIdentifierToCheck = new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:peppol:pint:billing-3.0@jp:peppol-1:invoice::2.1",
                PEPPOL_DOCTYPE_WILDCARD);

        final String expectedDiscoveredDocumentIdentifier = "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:peppol:pint:billing-3.0@jp:peppol-1*::2.1";

        getDocumentWithWildcardSchemeAndAssert(urlFetcherURL, smpDocumentIdentifierToCheck, expectedDiscoveredDocumentIdentifier, PEPPOL_DOCTYPE_WILDCARD);
    }

    /**
     * @param urlFetcherMock
     * @param smpDocumentIdentifierToCheck
     * @param expectedDiscoveredDocumentIdentifier
     * @param expectedDiscoveredDocumentScheme
     * @throws Exception
     */
    private void getDocumentWithWildcardSchemeAndAssert(URLFetcherMock urlFetcherMock,
                                                        SMPDocumentIdentifier smpDocumentIdentifierToCheck,
                                                        String expectedDiscoveredDocumentIdentifier,
                                                        String expectedDiscoveredDocumentScheme) throws Exception {
        SMPParticipantIdentifier participantIdentifier = PARTY_9925_0367302178_IDENTIFIER;

        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);

        List<PublisherLookupResult> publisherLookupResult = createMockPublisherLookupNaptrResult(participantIdentifier, TestCaseConstants.PUBLISHER_URL_02);
        Mockito.when(defaultDNSLookup.naptrUrlValueLookup(participantIdentifier,
                        PARTY_9925_0367302178_NAPTR_QUERY + "." + DNSUtils.TEST_TOP_DOMAIN_01))
                .thenReturn(publisherLookupResult);

        final ISignatureValidator emptyValidator = (document, trustedList) -> null;

        final DefaultBDXRLocator defaultBDXRLocator = new DefaultBDXRLocator.Builder()
                .addTopDnsDomain(DNSUtils.TEST_TOP_DOMAIN_01)
                .dnsLookup(defaultDNSLookup)
                .build();
        final DefaultBDXRReader bdxReader = new DefaultBDXRReader.Builder()
                .signatureValidator(emptyValidator)
                .build();
        final DefaultDocumentRequestProvider defaultProvider = new DefaultDocumentRequestProvider.Builder()
                .build();

        DynamicDiscoveryService smpClient = new DynamicDiscoveryService.Builder()
                .publisherLocator(defaultBDXRLocator)
                .documentReader(bdxReader)
                .addExtension(new OasisSMP10Extension())
                .addExtension(new PeppolSMPExtension())
                .wildcardSubresourceSchemes(expectedDiscoveredDocumentScheme)
                .documentFetcher(urlFetcherMock)
                .documentRequestProvider(defaultProvider)
                .build();

        final SMPServiceMetadata serviceMetadata = smpClient.getSubresource(participantIdentifier, smpDocumentIdentifierToCheck);
        assertNotNull(serviceMetadata);
        final SMPDocumentIdentifier documentIdentifier = serviceMetadata.getDocumentIdentifier();
        assertNotNull(documentIdentifier);

        assertEquals(expectedDiscoveredDocumentIdentifier, documentIdentifier.getIdentifier());
        assertEquals(expectedDiscoveredDocumentScheme, documentIdentifier.getScheme());
    }


    /**
     * Method configures the DDC client to fetch the service group of the given participant id "9925:0367302178" and sheme "iso6523-actorid-upis",
     * but it allows to change the participant id to test different scenarios.
     *
     * @param participantId the participant id to be used to fetch the service group
     * @return the service group
     * @throws Exception if an error occurs
     */
    private SMPServiceGroup getServiceGroupType(String participantId) throws Exception {
        // given
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR,
                PARTY_9925_0367302178_SERVICE_GROUP_URL,
                PARTY_9925_0367302178_FILE_RESOURCE);
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier(participantId, "iso6523-actorid-upis");

        List<PublisherLookupResult> publisherLookupResult = createMockPublisherLookupNaptrResult(participantIdentifier, TestCaseConstants.PUBLISHER_URL_02);
        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.naptrUrlValueLookup(participantIdentifier,
                        PARTY_9925_0367302178_NAPTR_QUERY + "." + DNSUtils.TEST_TOP_DOMAIN_02))
                .thenReturn(publisherLookupResult);

        // build the client
        DynamicDiscoveryService smpClient = new DynamicDiscoveryService.Builder()
                .publisherLocator(new DefaultBDXRLocator.Builder()
                        .addTopDnsDomain(DNSUtils.TEST_TOP_DOMAIN_02)
                        .dnsLookup(defaultDNSLookup)
                        .build())
                .documentReader(new DefaultBDXRReader.Builder()
                        .signatureValidator(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts")))
                        .build())
                .documentFetcher(urlFetcherURL)
                .build();
        // when
        return smpClient.getResource(participantIdentifier);
    }

}

