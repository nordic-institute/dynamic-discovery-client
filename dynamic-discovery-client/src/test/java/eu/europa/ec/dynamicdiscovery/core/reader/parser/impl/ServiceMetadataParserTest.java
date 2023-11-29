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
