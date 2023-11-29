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
