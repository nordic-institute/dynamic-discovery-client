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
package eu.europa.ec.dynamicdiscovery.core.reader.parser.impl;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.reader.impl.DefaultBDXRReader;
import eu.europa.ec.dynamicdiscovery.core.security.impl.DefaultSignatureValidator;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.security.KeyStore;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Flávio W. R. Santos
 */
class SignedServiceMetadataResponseParserImplTest {

    @Test
    void testDocumentBuilderWithDocTypeDisabled() throws Exception {
        //given
        KeyStore keyStore = CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts");
        DefaultBDXRReader testInstance = new DefaultBDXRReader(new DefaultSignatureValidator(keyStore));
        InputStream serviceMetadataStream = CommonUtil.getInputStreamFromOasisSMP10XmlResource("service_metadata_with_doctype_multiplying_entities_out_of_memory");
        FetcherResponse fetcherResponse = new FetcherResponse(serviceMetadataStream);
        //when then
        try {
            testInstance.getServiceMetadata(fetcherResponse);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("DOCTYPE is disallowed when the feature \"http://apache.org/xml/features/disallow-doctype-decl\" set to true."));
            return;
        }
        fail("DOCTYPE declaration must be blocked to prevent from XXE attacks");
    }

    @Test
    void testDocumentBuilderWithDocTypeDisabled1() throws Exception {
        //given
        KeyStore keyStore = CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts");
        DefaultBDXRReader testInstance = new DefaultBDXRReader(new DefaultSignatureValidator(keyStore));
        InputStream serviceMetadataStream = CommonUtil.getInputStreamFromOasisSMP20XmlResource("service_metadata_unsigned_valid_iso6523");
        FetcherResponse fetcherResponse = new FetcherResponse(serviceMetadataStream);
        //when then

        SMPServiceMetadata serviceMetadata = testInstance.getServiceMetadata(fetcherResponse);

        assertNotNull(serviceMetadata);

    }


}
