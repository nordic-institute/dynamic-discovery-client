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
import eu.europa.ec.dynamicdiscovery.core.locator.dns.impl.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.core.locator.impl.DefaultBDXRLocator;
import eu.europa.ec.dynamicdiscovery.core.locator.impl.StaticMapMetadataLocator;
import eu.europa.ec.dynamicdiscovery.core.provider.impl.DefaultProvider;
import eu.europa.ec.dynamicdiscovery.core.reader.impl.DefaultBDXRReader;
import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.core.security.impl.DefaultSignatureValidator;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.SMPServiceMetadataException;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import eu.europa.ec.dynamicdiscovery.util.TestCaseConstants;
import gen.eu.europa.ec.ddc.api.smp10.ServiceGroup;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import static eu.europa.ec.dynamicdiscovery.util.TestCaseConstants.PEPPOL_DOCTYPE_WILDCARD;
import static eu.europa.ec.dynamicdiscovery.util.TestCaseConstants.WILDCARD_SCHEME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * @author Flávio W. R. Santos
 */
class DocumentIdentifierIT {


    @Test
    void getDocumentIdentifierByNaptrOK1() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR, TestCaseConstants.SERVICE_GROUP_URL_9925_0367302178, "service_group_valid_iso6523");

        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis");

        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.naptrUrlValueLookup(participantIdentifier, "ZR2ZGDOAGAVSHSQ2MRHXEZV2H6ATTQBF4JJ4J7VJNPYMRDZ3UG4Q.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu")).thenReturn(TestCaseConstants.SMP_DOMAIN_ALIAS);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .reader(new DefaultBDXRReader.Builder()
                        .signatureValidator(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts")))
                        .build())
                .fetcher(urlFetcherURL)
                .build();
        List<SMPDocumentIdentifier> documentIdentifiers = smpClient.getDocumentIdentifiers(participantIdentifier);
        assertEquals(2, documentIdentifiers.size());

    }

    @Test
    void getDocumentIdentifierByNaptrOK2() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR, TestCaseConstants.SERVICE_GROUP_URL_URN_POLAND_NCPB, "service_group_urn_poland_ncpb");

        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");

        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.naptrUrlValueLookup(participantIdentifier, "DALXFO3CDYE5ZSLF5WAVCYQ3XGERI6ONUBJU5WAH3T77THFWCGEQ.ehealth-actorid-qns.ehealth.acc.edelivery.tech.ec.europa.eu")).thenReturn(TestCaseConstants.SMP_DOMAIN_ALIAS);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("ehealth.acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .reader(new DefaultBDXRReader.Builder()
                        .signatureValidator(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts")))
                        .build())
                .fetcher(urlFetcherURL)
                .build();
        List<SMPDocumentIdentifier> documentIdentifiers = smpClient.getDocumentIdentifiers(participantIdentifier);
        assertEquals(2, documentIdentifiers.size());
        assertEquals("urn::epsos:services##epsos-21", documentIdentifiers.get(0).getIdentifier());
        assertEquals("ehealth-resid-qns", documentIdentifiers.get(1).getScheme());
    }

    @Test
    void getDocumentIdentifierByCNAMEOK() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(CommonUtil.OASIS_SMP_10, URLFetcherMock.LookupType.CNAME, TestCaseConstants.SERVICE_GROUP_URL_9925_0367302178, "service_group_valid_iso6523", "b-ed520c91b58f3e9f19714d8170aac5af.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu");

        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis");

        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.naptrUrlValueLookup(participantIdentifier, "ZR2ZGDOAGAVSHSQ2MRHXEZV2H6ATTQBF4JJ4J7VJNPYMRDZ3UG4Q.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu")).thenReturn(null);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .reader(new DefaultBDXRReader.Builder()
                        .signatureValidator(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts")))
                        .build())
                .fetcher(urlFetcherURL)
                .build();

        List<SMPDocumentIdentifier> documentIdentifiers = smpClient.getDocumentIdentifiers(participantIdentifier);
        assertEquals(2, documentIdentifiers.size());
        assertEquals("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", documentIdentifiers.get(1).getIdentifier());
        assertEquals("bdx-docid-qns", documentIdentifiers.get(1).getScheme());
    }


    @Test
    void getDocumentIdentifierByStaticLookup() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.STATIC, "/cipa-smp-full-webapp" + TestCaseConstants.SERVICE_GROUP_URL_URN_POLAND_NCPB, "service_group_urn_poland_ncpb");
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new StaticMapMetadataLocator(new URI(TestCaseConstants.SMP_STATIC_DOMAIN)))
                .reader(new DefaultBDXRReader.Builder()
                        .signatureValidator(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts")))
                        .build())
                .fetcher(urlFetcherURL)
                .build();

        List<SMPDocumentIdentifier> documentIdentifiers = smpClient.getDocumentIdentifiers(participantIdentifier);
        assertEquals(2, documentIdentifiers.size());
        assertEquals("urn::epsos:services##epsos-21", documentIdentifiers.get(0).getIdentifier());
        assertEquals("ehealth-resid-qns", documentIdentifiers.get(1).getScheme());
    }

    @Test
    void getDocumentIdentifierNAPTRNotOK() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis");

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("acc.edelivery.tech.ec.europa.eu.local", defaultDNSLookup))
                .reader(new DefaultBDXRReader.Builder()
                        .signatureValidator(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts")))
                        .build())
                .fetcher(urlFetcherURL)
                .build();

        assertThrows(DNSLookupException.class, () -> smpClient.getDocumentIdentifiers(participantIdentifier));
    }

    @Test
    void getDocumentIdentifierByCNAMENotOK() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(CommonUtil.OASIS_SMP_10, URLFetcherMock.LookupType.CNAME, TestCaseConstants.SERVICE_GROUP_URL_9925_0367302178, "service_group_valid_iso6523", "b-12345678910.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu");

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator.Builder().addTopDnsDomain("acc.edelivery.tech.ec.europa.eu").build())
                .reader(new DefaultBDXRReader.Builder()
                        .signatureValidator(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts")))
                        .build())
                .fetcher(urlFetcherURL)
                .build();
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis");

        assertThrows(DNSLookupException.class, () -> smpClient.getDocumentIdentifiers(participantIdentifier));

    }

    @Test
    void testGetServiceGroupTypeOk() throws Exception {
        ServiceGroup serviceGroup = testGetServiceGroupType("9925:0367302178");
        assertEquals("9925:0367302178", serviceGroup.getParticipantIdentifier().getValue());
        assertEquals("iso6523-actorid-upis", serviceGroup.getParticipantIdentifier().getScheme());
        assertEquals("http://cipa-smp-full-webapp/iso6523-actorid-upis%3A%3A9915%3A0367302178/services/bdx-docid-qns%3A%3Aurn%3A%3Aepsos%3Aservices%23%23epsos-21", serviceGroup.getServiceMetadataReferenceCollection().getServiceMetadataReferences().get(0).getHref());
    }

    @Test
    void testGetServiceGroupTypeParticipantNotOk() {
        DNSLookupException result = assertThrows(DNSLookupException.class, () -> testGetServiceGroupType("9925:036730217815"));
        MatcherAssert.assertThat(result.getMessage(), CoreMatchers.startsWith("Not supported"));
    }

    private ServiceGroup testGetServiceGroupType(String participantId) throws Exception {

        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR, TestCaseConstants.SERVICE_GROUP_URL_9925_0367302178, "service_group_valid_iso6523");

        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier(participantId, "iso6523-actorid-upis");

        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.naptrUrlValueLookup(participantIdentifier,
                        "ZR2ZGDOAGAVSHSQ2MRHXEZV2H6ATTQBF4JJ4J7VJNPYMRDZ3UG4Q.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu"))
                .thenReturn(TestCaseConstants.SMP_DOMAIN_ALIAS);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .reader(new DefaultBDXRReader.Builder()
                        .signatureValidator(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts")))
                        .build())
                .fetcher(urlFetcherURL)
                .build();
        return smpClient.getServiceGroup(participantIdentifier).unwrap(ServiceGroup.class);
    }

    @Test
    void getOasis10DocumentIdentifierWithWildcardSchemeWithNoMatch() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();

        urlFetcherURL.setParameters(CommonUtil.OASIS_SMP_10,
                URLFetcherMock.LookupType.CNAME,
                "iso6523-actorid-upis%3A%3A9925%3A0367302178/services/bdx-docid-wildcard%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3ACreditNote-2%3A%3ACreditNote%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns014%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol5a%2A%3A%3A2.1",
                "service_metadata_valid_iso6523_wildcard",
                "b-ed520c91b58f3e9f19714d8170aac5af.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu");
        urlFetcherURL.addParameters(CommonUtil.OASIS_SMP_10, "service_group_valid_iso6523_wildcard", TestCaseConstants.SERVICE_GROUP_URL_9925_0367302178);

        //provide a document which is not matching
        final SMPDocumentIdentifier smpDocumentIdentifierToCheck = new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:cen.eu:en16931:2017#compliant#urn:fdc:peppol.eu:2017:poacc:billing:3.0::2.1",
                WILDCARD_SCHEME);

        final String expectedDiscoveredDocumentIdentifier = "urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a*::2.1";

        assertThrows(SMPServiceMetadataException.class, () -> getDocumentWithWildcardSchemeAndAssert(urlFetcherURL, smpDocumentIdentifierToCheck, expectedDiscoveredDocumentIdentifier, WILDCARD_SCHEME));

    }

    @Test
    void getOasis10DocumentIdentifierWithWildcardSchemeBasedOnDocumentIdentifierValuedWhichMatchesSMPWildcardDocument() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();

        urlFetcherURL.setParameters(CommonUtil.OASIS_SMP_10,
                URLFetcherMock.LookupType.CNAME,
                "iso6523-actorid-upis%3A%3A9925%3A0367302178/services/bdx-docid-wildcard%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3ACreditNote-2%3A%3ACreditNote%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns014%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol5a%2A%3A%3A2.1",
                "service_metadata_valid_iso6523_wildcard",
                "b-ed520c91b58f3e9f19714d8170aac5af.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu");
        urlFetcherURL.addParameters(CommonUtil.OASIS_SMP_10, "service_group_valid_iso6523_wildcard", TestCaseConstants.SERVICE_GROUP_URL_9925_0367302178);

        final SMPDocumentIdentifier smpDocumentIdentifierToCheck = new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:invoice::2.1",
                WILDCARD_SCHEME);

        final String expectedDiscoveredDocumentIdentifier = "urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a*::2.1";

        getDocumentWithWildcardSchemeAndAssert(urlFetcherURL, smpDocumentIdentifierToCheck, expectedDiscoveredDocumentIdentifier, WILDCARD_SCHEME);
    }

    @Test
    void getOasis10DocumentIdentifierWithWildcardSchemeAndExactMatch() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();

        urlFetcherURL.setParameters(CommonUtil.OASIS_SMP_10,
                URLFetcherMock.LookupType.CNAME,
                "/iso6523-actorid-upis%3A%3A9925%3A0367302178/services/bdx-docid-wildcard%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3ACreditNote-2%3A%3ACreditNote%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns014%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol5a%3A%3A2.1",
                "service_metadata_valid_iso6523_wildcard_exact_match",
                "b-ed520c91b58f3e9f19714d8170aac5af.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu");
        urlFetcherURL.addParameters(CommonUtil.OASIS_SMP_10, "service_group_valid_iso6523_wildcard_with_exact_match", TestCaseConstants.SERVICE_GROUP_URL_9925_0367302178);

        final SMPDocumentIdentifier smpDocumentIdentifierToCheck = new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a::2.1",
                WILDCARD_SCHEME);

        final String expectedDiscoveredDocumentIdentifier = "urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a::2.1";

        getDocumentWithWildcardSchemeAndAssert(urlFetcherURL, smpDocumentIdentifierToCheck, expectedDiscoveredDocumentIdentifier, WILDCARD_SCHEME);
    }

    @Test
    void getOasis10DocumentIdentifierWithWildcardSchemeBasedOnDocumentIdentifierValuedWhichMatchesExactSMPWildcardDocument() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();

        urlFetcherURL.setParameters(CommonUtil.OASIS_SMP_10,
                URLFetcherMock.LookupType.CNAME,
                "iso6523-actorid-upis%3A%3A9925%3A0367302178/services/bdx-docid-wildcard%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3ACreditNote-2%3A%3ACreditNote%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns014%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol5a%2A%3A%3A2.1",
                "service_metadata_valid_iso6523_wildcard",
                "b-ed520c91b58f3e9f19714d8170aac5af.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu");
        urlFetcherURL.addParameters(CommonUtil.OASIS_SMP_10, "service_group_valid_iso6523_wildcard", TestCaseConstants.SERVICE_GROUP_URL_9925_0367302178);

        final SMPDocumentIdentifier smpDocumentIdentifierToCheck = new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a*::2.1",
                WILDCARD_SCHEME);

        final String expectedDiscoveredDocumentIdentifier = "urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a*::2.1";

        getDocumentWithWildcardSchemeAndAssert(urlFetcherURL, smpDocumentIdentifierToCheck, expectedDiscoveredDocumentIdentifier, WILDCARD_SCHEME);
    }

    @Test
    void getOasis10DocumentIdentifierWithWildcardSchemeBasedOnDocumentIdentifierValuedWhichMatchesExactSMPWildcardDocumentCaseInsensitive() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();

        urlFetcherURL.setParameters(CommonUtil.OASIS_SMP_10,
                URLFetcherMock.LookupType.CNAME,
                "iso6523-actorid-upis%3A%3A9925%3A0367302178/services/bdx-docid-wildcard%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3ACreditNote-2%3A%3ACreditNote%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns014%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol5a%2A%3A%3A2.1",
                "service_metadata_valid_iso6523_wildcard",
                "b-ed520c91b58f3e9f19714d8170aac5af.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu");
        urlFetcherURL.addParameters(CommonUtil.OASIS_SMP_10, "service_group_valid_iso6523_wildcard", TestCaseConstants.SERVICE_GROUP_URL_9925_0367302178);

        //document identifier is all in lower case and different from the one from the SMP which has a different case sensitivity
        final SMPDocumentIdentifier smpDocumentIdentifierToCheck = new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:creditNote-2::creditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a*::2.1",
                WILDCARD_SCHEME);

        final String expectedDiscoveredDocumentIdentifier = "urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a*::2.1";

        getDocumentWithWildcardSchemeAndAssert(urlFetcherURL, smpDocumentIdentifierToCheck, expectedDiscoveredDocumentIdentifier, WILDCARD_SCHEME);
    }

    @Test
    void getPeppolDocumentIdentifierWithWildcardSchemeBasedOnDocumentIdentifierValuedWhichMatchesSMPWildcardDocument() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();

        urlFetcherURL.setParameters(CommonUtil.PEPPOL,
                URLFetcherMock.LookupType.CNAME,
                "/iso6523-actorid-upis%3A%3A9925%3A0367302178/services/peppol-doctype-wildcard%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3AInvoice-2%3A%3AInvoice%23%23urn%3Apeppol%3Apint%3Abilling-3.0%40jp%3Apeppol-1%2A%3A%3A2.1",
                "signed_service_metadata_valid_iso6523_wildcard",
                "b-ed520c91b58f3e9f19714d8170aac5af.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu");
        urlFetcherURL.addParameters(CommonUtil.PEPPOL, "peppol_service_group_valid_iso6523_wildcard", TestCaseConstants.SERVICE_GROUP_URL_9925_0367302178);

        final SMPDocumentIdentifier smpDocumentIdentifierToCheck = new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:peppol:pint:billing-3.0@jp:peppol-1:invoice::2.1",
                PEPPOL_DOCTYPE_WILDCARD);

        final String expectedDiscoveredDocumentIdentifier = "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:peppol:pint:billing-3.0@jp:peppol-1*::2.1";

        getDocumentWithWildcardSchemeAndAssert(urlFetcherURL, smpDocumentIdentifierToCheck, expectedDiscoveredDocumentIdentifier, PEPPOL_DOCTYPE_WILDCARD);
    }

    private void getDocumentWithWildcardSchemeAndAssert(URLFetcherMock urlFetcherMock,
                                                        SMPDocumentIdentifier smpDocumentIdentifierToCheck,
                                                        String expectedDiscoveredDocumentIdentifier,
                                                        String expectedDiscoveredDocumentScheme) throws Exception {
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis");

        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.naptrUrlValueLookup(participantIdentifier,
                        "ZR2ZGDOAGAVSHSQ2MRHXEZV2H6ATTQBF4JJ4J7VJNPYMRDZ3UG4Q.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu"))
                .thenReturn(TestCaseConstants.SMP_DOMAIN_ALIAS);

        final ISignatureValidator emptyValidator = (document, trustedList) -> null;

        final DefaultBDXRLocator defaultBDXRLocator = new DefaultBDXRLocator.Builder()
                .addTopDnsDomain("acc.edelivery.tech.ec.europa.eu")
                .dnsLookup(defaultDNSLookup)
                .build();
        final DefaultBDXRReader bdxReader = new DefaultBDXRReader.Builder()
                .addExtension(new OasisSMP10Extension())
                .addExtension(new PeppolSMPExtension())
                .signatureValidator(emptyValidator)
                .build();
        final DefaultProvider defaultProvider = new DefaultProvider.Builder()
                .metadataFetcher(urlFetcherMock)
                .metadataReader(bdxReader)
                .wildcardSchemes(Collections.singletonList(expectedDiscoveredDocumentScheme))
                .build();


        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(defaultBDXRLocator)
                .reader(bdxReader)
                .fetcher(urlFetcherMock)
                .provider(defaultProvider)
                .build();

        final SMPServiceMetadata serviceMetadata = smpClient.getServiceMetadata(participantIdentifier, smpDocumentIdentifierToCheck);
        assertNotNull(serviceMetadata);
        final SMPDocumentIdentifier documentIdentifier = serviceMetadata.getDocumentIdentifier();
        assertNotNull(documentIdentifier);

        assertEquals(expectedDiscoveredDocumentIdentifier, documentIdentifier.getIdentifier());
        assertEquals(expectedDiscoveredDocumentScheme, documentIdentifier.getScheme());
    }
}
