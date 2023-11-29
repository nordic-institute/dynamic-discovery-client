/*
 * Copyright 2017-2023 European Commission | eDelivery Dynamic Discovery Client
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence attached in file: LICENSE-EUPL-v1.2-EN.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 */
package eu.europa.ec.dynamicdiscovery.core.reader.impl;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.security.impl.DefaultSignatureValidator;
import eu.europa.ec.dynamicdiscovery.exception.BindException;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceGroup;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import gen.eu.europa.ec.ddc.api.smp10.ServiceGroup;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Flávio W. R. Santos
 * @since 1.0
 */
class DefaultBDXRReaderTest {


    @Test
    void testGetServiceGroup() throws Exception {
        DefaultBDXRReader defaultBDXRReader = new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts")));
        FetcherResponse fetcherResponse = new FetcherResponse(CommonUtil.getInputStreamFromOasisSMP10XmlResource("service_group_urn_poland_ncpb"));
        SMPServiceGroup serviceGroup = defaultBDXRReader.getServiceGroup(fetcherResponse);

        assertNotNull(serviceGroup);
        assertNotNull(serviceGroup.getParticipantIdentifier());
        assertEquals("urn:poland:ncpb", serviceGroup.getParticipantIdentifier().getIdentifier());
        assertEquals("ehealth-actorid-qns", serviceGroup.getParticipantIdentifier().getScheme());
        assertEquals(2, serviceGroup.getDocumentIdentifiers().size());
        assertTrue(serviceGroup.isWrapperFor(ServiceGroup.class));
        assertNotNull(serviceGroup.unwrap(ServiceGroup.class));
    }

    @Test
    void testGetServiceGroupNullContent() throws Exception {
        DefaultBDXRReader defaultBDXRReader = new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts")));
        FetcherResponse fetcherResponse = new FetcherResponse(null);

        BindException result = assertThrows(BindException.class, () -> defaultBDXRReader.getServiceGroup(fetcherResponse));
        assertEquals("Error occurred while retrieving the data!", result.getMessage());
    }

    @Test
    void testGetServiceMetadata() throws Exception {
        DefaultBDXRReader defaultBDXRReader = new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts")));
        FetcherResponse fetcherResponse = new FetcherResponse(CommonUtil.getInputStreamFromOasisSMP10XmlResource("signed_service_metadata_invalid_certificate"));
        SMPServiceMetadata serviceMetadata = defaultBDXRReader.getServiceMetadata(fetcherResponse);

        assertNotNull(serviceMetadata);
    }

    @Test
    void testGetServiceMetadataNotOk() throws Exception {
        DefaultBDXRReader defaultBDXRReader = new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts")));
        FetcherResponse fetcherResponse = new FetcherResponse(null);

        BindException result = assertThrows(BindException.class, () -> defaultBDXRReader.getServiceMetadata(fetcherResponse));
        assertEquals("Error occurred while retrieving the data!", result.getMessage());
    }

}
