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
package eu.europa.ec.dynamicdiscovery.reader.parser.impl;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.reader.parser.impl.SignedServiceMetadataResponseParserImpl;
import eu.europa.ec.dynamicdiscovery.core.security.impl.DefaultSignatureValidator;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.security.KeyStore;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertTrue;

public class SignedServiceMetadataResponseParserImplTest {

    private static DocumentBuilderFactory documentBuilderFactory;

    @BeforeClass
    public static void before() throws Exception {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
    }

    @Test
    public void testDocumentBuilderWithDocTypeEnabled() throws Exception {
        testForDocType("service_metadata_with_doctype_filesystem", false, "\\etc\\ddcxxeAttackTest (The system cannot find the path specified)", "DOCTYPE declaration is not blocked and should be able to access filesystem and not found the path.");
    }

    @Test
    public void testDocumentBuilderWithDocTypeDisabled() throws Exception {
        testForDocType("service_metadata_with_doctype_multiplying_entities_out_of_memory", true, "DOCTYPE is disallowed when the feature \"http://apache.org/xml/features/disallow-doctype-decl\" set to true.", "DOCTYPE declaration must be blocked to prevent from XXE attacks");
    }

    private void testForDocType(String filename, boolean disableDocType, String... errorMessage) throws Exception {
        //given
        documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", disableDocType);
        KeyStore keyStore = CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts");
        SignedServiceMetadataResponseParserImpl signedServiceMetadataResponseParser = new SignedServiceMetadataResponseParserImpl(documentBuilderFactory, new DefaultSignatureValidator(keyStore));
        InputStream serviceMetadataStream = CommonUtil.getStreamFromXmlFile(filename);
        FetcherResponse fetcherResponse = new FetcherResponse(serviceMetadataStream);
        //when then
        try {
            signedServiceMetadataResponseParser.getServiceMetadata(fetcherResponse);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains(errorMessage[0]));
            return;
        }
        fail(errorMessage[1]);
    }
}
