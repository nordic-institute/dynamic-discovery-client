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
package eu.europa.ec.dynamicdiscovery.core.reader;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.reader.impl.DefaultBDXRReader;
import eu.europa.ec.dynamicdiscovery.core.security.impl.DefaultSignatureValidator;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceGroup;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import gen.eu.europa.ec.ddc.api.smp10.SignedServiceMetadata;
import org.junit.jupiter.api.Test;

import java.security.KeyStore;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Flávio W. R. Santos
 */
class ResponseParserTest {


    @Test
    void parseServiceMetadataTest() throws Exception {
        FetcherResponse fetcherResponse = new FetcherResponse(CommonUtil.getInputStreamFromOasisSMP10XmlResource("signed_service_metadata_urn_poland_ncpb"));
        KeyStore keyStore = CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts");

        DefaultBDXRReader testInstance = new DefaultBDXRReader(new DefaultSignatureValidator(keyStore));

        SMPServiceMetadata serviceMetadata = testInstance.getServiceMetadata(fetcherResponse);
        assertEquals("urn:poland:ncpb", serviceMetadata.getParticipantIdentifier().getIdentifier());
        assertEquals("ehealth-actorid-qns", serviceMetadata.getParticipantIdentifier().getScheme());
        assertEquals(1, serviceMetadata.getEndpoints().size());
        // assertEquals("urn:epsosPatientService::List", serviceMetadata.getProcessIdentifiers().get(0).getIdentifier());
        //assertEquals("ehealth-procid-qns", serviceMetadata.getProcessIdentifiers().get(0).getScheme());
        assertEquals("urn::epsos##services:extended:epsos::107", serviceMetadata.getDocumentIdentifier().getIdentifier());
        assertEquals("ehealth-resid-qns", serviceMetadata.getDocumentIdentifier().getScheme());
        //assertEquals(getStringFromXmlFile("signed_service_metadata_urn_poland_ncpb"), serviceMetadata.getResponseBody());
        assertNotNull(serviceMetadata.getDocumentIdentifier());
    }

    @Test
    void parseDocumentIdentifierTest() throws Exception {
        FetcherResponse fetcherResponse = new FetcherResponse(CommonUtil.getInputStreamFromOasisSMP10XmlResource("service_group_urn_poland_ncpb"));
        DefaultBDXRReader responseParser = new DefaultBDXRReader(null);
        SMPServiceGroup serviceGroup = responseParser.getServiceGroup(fetcherResponse);
        List<SMPDocumentIdentifier> documentIdentifiers = serviceGroup.getDocumentIdentifiers();
        assertEquals(2, documentIdentifiers.size());
        assertEquals("urn::epsos:services##epsos-21", documentIdentifiers.get(0).getIdentifier());
        assertEquals("epsos-docid-qns", documentIdentifiers.get(0).getScheme());
        assertEquals("urn::epsos##services:extended:epsos::107", documentIdentifiers.get(1).getIdentifier());
        assertEquals("ehealth-resid-qns", documentIdentifiers.get(1).getScheme());
    }

    //BUG EDELIVERY-2484
    @Test
    void parseServiceMetadataWithEmptyCertificateTest() throws Exception {
        //given
        FetcherResponse fetcherResponse = new FetcherResponse(CommonUtil.getInputStreamFromOasisSMP10XmlResource("signed_service_metadata_empty_certificate"));
        KeyStore keyStore = CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts");
        DefaultBDXRReader testInstance = new DefaultBDXRReader(new DefaultSignatureValidator(keyStore));

        //when
        SMPServiceMetadata serviceMetadata = testInstance.getServiceMetadata(fetcherResponse);

        //then
        byte[] certificate = serviceMetadata.unwrap(SignedServiceMetadata.class).getServiceMetadata().getServiceInformation().getProcessList().getProcesses().get(0).getServiceEndpointList().getEndpoints().get(0).getCertificate();
        assertEquals(0, certificate.length);
    }

    //BUG EDELIVERY-2484
    @Test
    void parseServiceMetadataWithInvalidCertificateTest() throws Exception {
        //given
        FetcherResponse fetcherResponse = new FetcherResponse(CommonUtil.getInputStreamFromOasisSMP10XmlResource("signed_service_metadata_invalid_certificate"));
        KeyStore keyStore = CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts");
        DefaultBDXRReader testInstance = new DefaultBDXRReader(new DefaultSignatureValidator(keyStore));

        //when
        SMPServiceMetadata serviceMetadata = testInstance.getServiceMetadata(fetcherResponse);

        //then
        byte[] certificate = serviceMetadata.unwrap(SignedServiceMetadata.class).getServiceMetadata().getServiceInformation().getProcessList().getProcesses().get(0).getServiceEndpointList().getEndpoints().get(0).getCertificate();
        assertEquals("base64 encoded invalid certificate content", new String(certificate));
        assertNull(serviceMetadata.getEndpoints().get(0).getCertificate());
    }
}

