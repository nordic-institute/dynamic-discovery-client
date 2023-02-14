package eu.europa.ec.dynamicdiscovery.model.identifiers.types;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Joze Rihtarsic
 * @since 2.0
 */
class OasisSMPFormatterTypeTest {


    private static Stream<Arguments> participantIdentifierPositiveCases() {
        return Stream.of(
                Arguments.of(
                        "Valid peppol party identifier",
                        true,
                        "iso6523-actorid-upis::0002:12345",
                        "iso6523-actorid-upis",
                        "0002:12345",
                        null, null
                ),
                Arguments.of(
                        "no schema",
                        true,
                        "::0002:12345",
                        null,
                        "0002:12345",
                        null, null
                ),
                Arguments.of(
                        "test URN example ",
                        true, // allways true - default parser
                        "urn:justice:si:1123445",
                        null,
                        "urn:justice:si:1123445",
                        null, null)
        );
    }

    OasisSMPFormatterType testInstance = new OasisSMPFormatterType();

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("participantIdentifierPositiveCases")
    void isTypeByScheme(String testName, boolean isValidPartyId, String toParseIdentifier, String schemaPart, String idPart, Class errorClass, String containsErrorMessage) {

        boolean result = testInstance.isTypeByScheme(schemaPart);
        assertEquals(isValidPartyId, result);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("participantIdentifierPositiveCases")
    void isType(String testName, boolean isValidPartyId, String toParseIdentifier, String schemaPart, String idPart, Class errorClass, String containsErrorMessage) {

        boolean result = testInstance.isType(toParseIdentifier);
        assertEquals(isValidPartyId, result);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("participantIdentifierPositiveCases")
    void format(String testName, boolean isValidPartyId, String toParseIdentifier, String schemaPart, String idPart, Class errorClass, String containsErrorMessage) {
        // skip format for not ebcore party ids
        if (!isValidPartyId) {
            return;
        }

        String result = testInstance.format(schemaPart, idPart);
        String resultNoDelimiterForNullSchema = testInstance.format(schemaPart, idPart, true);

        String schema = trimToEmpty(schemaPart);
        assertEquals(schema + "::" + trim(idPart), result);

        assertEquals((isEmpty(schema) ? "" : schema + "::") + trim(idPart), resultNoDelimiterForNullSchema);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("participantIdentifierPositiveCases")
    void parse(String testName, boolean isValidPartyId, String toParseIdentifier, String schemaPart, String idPart, Class errorClass, String containsErrorMessage) {
        // skip parse not ebcore party ids
        if (!isValidPartyId) {
            IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> testInstance.parse(toParseIdentifier));
            MatcherAssert.assertThat(result.getMessage(), CoreMatchers.containsString(containsErrorMessage));
        }
        if (errorClass != null) {
            Throwable result = assertThrows(errorClass, () -> testInstance.parse(toParseIdentifier));
            MatcherAssert.assertThat(result.getMessage(), CoreMatchers.containsString(containsErrorMessage));
        } else {

            String[] result = testInstance.parse(toParseIdentifier);
            assertNotNull(result);
            assertEquals(2, result.length);
            assertEquals(schemaPart, result[0]);
            assertEquals(idPart, result[1]);
        }
    }
}
