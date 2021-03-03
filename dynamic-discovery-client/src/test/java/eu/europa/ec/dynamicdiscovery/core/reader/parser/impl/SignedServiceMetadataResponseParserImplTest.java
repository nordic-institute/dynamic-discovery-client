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
package eu.europa.ec.dynamicdiscovery.core.reader.parser.impl;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.security.impl.DefaultSignatureValidator;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import org.junit.Test;

import java.io.InputStream;
import java.security.KeyStore;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertTrue;

/**
 * @author Flávio W. R. Santos
 */
public class SignedServiceMetadataResponseParserImplTest {

    @Test
    public void testDocumentBuilderWithDocTypeDisabled() throws Exception {
        //given
        KeyStore keyStore = CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts");
        SignedServiceMetadataResponseParserImpl signedServiceMetadataResponseParser = new SignedServiceMetadataResponseParserImpl(new DefaultSignatureValidator(keyStore));
        InputStream serviceMetadataStream = CommonUtil.getStreamFromXmlFile("service_metadata_with_doctype_multiplying_entities_out_of_memory");
        FetcherResponse fetcherResponse = new FetcherResponse(serviceMetadataStream);
        //when then
        try {
            signedServiceMetadataResponseParser.getServiceMetadata(fetcherResponse);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("DOCTYPE is disallowed when the feature \"http://apache.org/xml/features/disallow-doctype-decl\" set to true."));
            return;
        }
        fail("DOCTYPE declaration must be blocked to prevent from XXE attacks");
    }
}
