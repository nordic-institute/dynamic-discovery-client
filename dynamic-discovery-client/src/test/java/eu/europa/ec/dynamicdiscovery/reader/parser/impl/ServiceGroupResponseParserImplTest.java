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
import eu.europa.ec.dynamicdiscovery.core.reader.parser.impl.ServiceGroupResponseParserImpl;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import org.junit.Test;

import java.io.InputStream;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertTrue;

public class ServiceGroupResponseParserImplTest {

    @Test
    public void testDocumentBuilderWithDocTypeDisabled1() throws Exception {
        testForDocType("service_group_with_doctype_filesystem");
    }

    @Test
    public void testDocumentBuilderWithDocTypeDisabled2() throws Exception {
        testForDocType("service_group_with_doctype_multiplying_entities_out_of_memory");
    }

    private void testForDocType(String filename, String... errorMessage) throws Exception {
        //given
        ServiceGroupResponseParserImpl serviceGroupResponseParser = new ServiceGroupResponseParserImpl();
        InputStream serviceGroupStream = CommonUtil.getStreamFromXmlFile(filename);
        FetcherResponse fetcherResponse = new FetcherResponse(serviceGroupStream);

        //when then
        try {
            serviceGroupResponseParser.getServiceGroup(fetcherResponse);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("DOCTYPE is disallowed when the feature \"http://apache.org/xml/features/disallow-doctype-decl\" set to true."));
            return;
        }
        fail("DOCTYPE declaration must be blocked to prevent from XXE attacks");
    }
}
