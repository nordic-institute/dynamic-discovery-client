/*
 * (C) Copyright 2016-2021 - European Commission | Dynamic Discovery Client
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
 */
package eu.europa.ec.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.core.fetcher.URLFetcherMock;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.impl.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.core.locator.impl.DefaultBDXRLocator;
import eu.europa.ec.dynamicdiscovery.core.locator.impl.StaticMapMetadataLocator;
import eu.europa.ec.dynamicdiscovery.core.reader.impl.DefaultBDXRReader;
import eu.europa.ec.dynamicdiscovery.core.security.impl.DefaultSignatureValidator;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
                .reader(new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts"))))
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
                .reader(new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts"))))
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
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.CNAME, TestCaseConstants.SERVICE_GROUP_URL_9925_0367302178, "service_group_valid_iso6523", "b-ed520c91b58f3e9f19714d8170aac5af.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu");

        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis");

        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        Mockito.when(defaultDNSLookup.naptrUrlValueLookup(participantIdentifier, "ZR2ZGDOAGAVSHSQ2MRHXEZV2H6ATTQBF4JJ4J7VJNPYMRDZ3UG4Q.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu")).thenReturn(null);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .reader(new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts"))))
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
                .reader(new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts"))))
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
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.NAPTR, TestCaseConstants.SERVICE_GROUP_URL_9925_0367302178, "service_group_valid_iso6523");
        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis");
        Mockito.when(defaultDNSLookup.naptrUrlValueLookup(participantIdentifier, "ZR2ZGDOAGAVSHSQ2MRHXEZV2H6ATTQBF4JJ4J7VJNPYMRDZ3UG4Q.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu")).thenReturn(null);

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .reader(new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts"))))
                .fetcher(urlFetcherURL)
                .build();

        assertThrows(DNSLookupException.class, () -> smpClient.getDocumentIdentifiers(participantIdentifier));
    }

    @Test
    void getDocumentIdentifierByCNAMENotOK() throws Exception {
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(URLFetcherMock.LookupType.CNAME, TestCaseConstants.SERVICE_GROUP_URL_9925_0367302178, "service_group_valid_iso6523", "b-12345678910.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu");

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator.Builder().addTopDnsDomain("acc.edelivery.tech.ec.europa.eu").build())
                .reader(new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts"))))
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
    void testGetServiceGroupTypeParticipantNotOk() throws Exception {
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
                .reader(new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts"))))
                .fetcher(urlFetcherURL)
                .build();
        return smpClient.getServiceGroup(participantIdentifier).unwrap(ServiceGroup.class);
    }
}
