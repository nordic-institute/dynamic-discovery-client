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
