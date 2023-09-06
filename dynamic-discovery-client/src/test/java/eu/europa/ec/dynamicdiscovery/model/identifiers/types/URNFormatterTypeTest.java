package eu.europa.ec.dynamicdiscovery.model.identifiers.types;

import eu.europa.ec.dynamicdiscovery.exception.MalformedIdentifierException;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.trim;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Joze Rihtarsic
 * @since 2.0
 */
class URNFormatterTypeTest {


    private static Stream<Arguments> participantIdentifierPositiveCases() {
        return Stream.of(
                Arguments.of(
                        "Email example ",
                        true,
                        "mailto:test@ec.europa.eu",
                        "mailto",
                        "test@ec.europa.eu",
                        null, null
                ),
                Arguments.of(
                        "test URN example ",
                        true,
                        "urn:ehealth:si:1123445",
                        "urn:ehealth:si",
                        "1123445",
                        null, null
                ),
                Arguments.of(
                        "test URN example ",
                        false,
                        "urn:justice:si:a1123445",
                        "urn:justice:si",
                        "a1123445",
                        MalformedIdentifierException.class, "does not match regular expression")
        );
    }


    URNFormatterType testInstance
            = new URNFormatterType(Pattern.compile("^(?i)\\s*(::)?(?<scheme>(urn:ehealth:[a-zA-Z]{2})|mailto):?(?<identifier>.+)?\\s*$")
    );


    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("participantIdentifierPositiveCases")
    void isSchemeValid(String testName, boolean isValidIdentifier, String toParseIdentifier, String schemaPart, String idPart, Class<Exception> errorClass, String containsErrorMessage) {
        boolean result = testInstance.isSchemeValid(schemaPart);
        // all schemes are valid
        assertTrue(result);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("participantIdentifierPositiveCases")
    void isType(String testName, boolean isValidIdentifier, String toParseIdentifier, String schemaPart, String idPart, Class<Exception> errorClass, String containsErrorMessage) {

        boolean result = testInstance.isType(toParseIdentifier);
        assertEquals(isValidIdentifier, result);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("participantIdentifierPositiveCases")
    void format(String testName, boolean isValidIdentifier, String toParseIdentifier, String schemaPart, String idPart, Class<Exception> errorClass, String containsErrorMessage) {
        // skip format for not ebcore party ids
        if (!isValidIdentifier) {
            return;
        }
        String result = testInstance.format(idPart, schemaPart);
        assertEquals(trim(idPart) + ":" + trim(schemaPart), result);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("participantIdentifierPositiveCases")
    void parse(String testName, boolean isValidPartyId, String toParseIdentifier, String schemaPart, String idPart, Class<Exception> errorClass, String containsErrorMessage) {
        // skip parse not ebcore party ids
        if (!isValidPartyId) {
            Exception result = assertThrows(errorClass, () -> testInstance.parse(toParseIdentifier));
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
