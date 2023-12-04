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
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.InputStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ServiceMetadataParserTest {

    DefaultBDXRReader testInstance = new DefaultBDXRReader(null);

    private static Stream<Arguments> testOasisSMP10ServiceMetadataArguments() {
        return Stream.of(
                Arguments.of("signed_service_metadata_date-time-local-invalid", 0),
                Arguments.of("signed_service_metadata_date-time-UTC-invalid", 0),
                Arguments.of("signed_service_metadata_date-time-offset-invalid", 0),
                Arguments.of("signed_service_metadata_date-local-invalid", 0)
        );
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("testOasisSMP10ServiceMetadataArguments")
    void testOnInvalidServiceIsIgnored(String xmlFilename, int validEndpoints) throws Exception {
        // given
        InputStream serviceGroupStream = CommonUtil.getInputStreamFromOasisSMP10XmlResource(xmlFilename);
        FetcherResponse fetcherResponse = new FetcherResponse(serviceGroupStream);
        SMPServiceMetadata serviceMetadata = testInstance.getServiceMetadata(fetcherResponse);
        assertNotNull(serviceMetadata);


        assertEquals(validEndpoints, serviceMetadata.getEndpoints().size());

    }
}
