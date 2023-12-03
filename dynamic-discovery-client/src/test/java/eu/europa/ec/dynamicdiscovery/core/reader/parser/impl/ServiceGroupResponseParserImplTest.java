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
import eu.europa.ec.dynamicdiscovery.exception.BindException;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;


/**
 * @author Flávio W. R. Santos
 */
class ServiceGroupResponseParserImplTest {

    @Test
    void testDocumentBuilderWithDocTypeDisabled1() throws Exception {
        testForDocType("service_group_with_doctype_filesystem");
    }

    @Test
    void testDocumentBuilderWithDocTypeDisabled2() throws Exception {
        testForDocType("service_group_with_doctype_multiplying_entities_out_of_memory");
    }

    private void testForDocType(String filename, String... errorMessage) throws Exception {
        //given
        DefaultBDXRReader responseParser = new DefaultBDXRReader(null);

        InputStream serviceGroupStream = CommonUtil.getInputStreamFromOasisSMP10XmlResource(filename);
        FetcherResponse fetcherResponse = new FetcherResponse(serviceGroupStream);

        //when
        BindException result = Assertions.assertThrows(BindException.class, () -> responseParser.getServiceGroup(fetcherResponse));
        //then
        MatcherAssert.assertThat(result.getMessage(), CoreMatchers.containsString("DOCTYPE is disallowed"));
    }
}
