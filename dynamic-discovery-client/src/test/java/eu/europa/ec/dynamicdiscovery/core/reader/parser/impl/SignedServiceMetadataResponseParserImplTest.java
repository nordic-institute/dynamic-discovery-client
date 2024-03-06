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
package eu.europa.ec.dynamicdiscovery.core.reader.parser.impl;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.reader.impl.DefaultBDXRReader;
import eu.europa.ec.dynamicdiscovery.core.security.impl.DefaultSignatureValidator;
import eu.europa.ec.dynamicdiscovery.exception.BindException;
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

        BindException result = assertThrows(BindException.class, () -> testInstance.getServiceMetadata(fetcherResponse));
        assertTrue(result.getMessage().contains("DOCTYPE is disallowed when the feature \"http://apache.org/xml/features/disallow-doctype-decl\" set to true."));

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
