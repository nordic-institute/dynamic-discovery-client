package eu.europa.ec.dynamicdiscovery.model;

import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPProcessIdentifier;
import eu.europa.ec.dynamicdiscovery.util.TestCaseConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static eu.europa.ec.dynamicdiscovery.util.TestCaseConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class SMPServiceMetadataTest {

    private static Stream<Arguments> getEndpointTestArguments() {
        return Stream.of(
                Arguments.of("Simple OK test",
                        PROCESS_IDENTIFIER_01,
                        Collections.singletonList(TRANSPORT_PROFILE_01),
                        Collections.singletonList(PROCESS_IDENTIFIER_01),
                        TRANSPORT_PROFILE_01,
                        true
                ),
                Arguments.of("Multiple OK test",
                        PROCESS_IDENTIFIER_01,
                        Arrays.asList(TRANSPORT_PROFILE_01,TRANSPORT_PROFILE_02),
                        Arrays.asList(PROCESS_IDENTIFIER_01,PROCESS_IDENTIFIER_02),
                        TRANSPORT_PROFILE_01,
                        true
                ),
                Arguments.of("Missing by process",
                        PROCESS_IDENTIFIER_01,
                        Arrays.asList(TRANSPORT_PROFILE_01,TRANSPORT_PROFILE_02),
                        Collections.singletonList(PROCESS_IDENTIFIER_02),
                        TRANSPORT_PROFILE_01,
                        false
                ),
                Arguments.of("Missing by transport",
                        PROCESS_IDENTIFIER_01,
                        Collections.singletonList(TRANSPORT_PROFILE_02),
                        Arrays.asList(PROCESS_IDENTIFIER_01,PROCESS_IDENTIFIER_02),
                        TRANSPORT_PROFILE_01,
                        false
                )
        );
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("getEndpointTestArguments")
    void testGetEndpointOK(String name, SMPProcessIdentifier queryProcessIdentifier,
                           List<SMPTransportProfile> queryTransportProfile,
                           List<SMPProcessIdentifier> endpointProcessIdentifiers,
                           SMPTransportProfile endpointTransportProfile,
                           boolean foundEndpoint
    ) {

        SMPEndpoint endpoint = new SMPEndpoint.Builder()
                .addProcessIdentifiers(endpointProcessIdentifiers)
                .transportProfile(endpointTransportProfile)
                .address("http://edelivery.tech.ec.europa.eu").build();

        SMPServiceMetadata testInstance = new SMPServiceMetadata.Builder().addEndpoint(endpoint).build();

        SMPEndpoint result = testInstance.getEndpoint(queryProcessIdentifier, queryTransportProfile.toArray(new SMPTransportProfile[0]));
        assertEquals(foundEndpoint, result != null);
        if (foundEndpoint) {
            assertEquals(endpoint, result);
        }
    }
}
