package eu.europa.ec.dynamicdiscovery.model.identifiers.types;

import eu.europa.ec.dynamicdiscovery.exception.MalformedIdentifierException;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Joze Rihtarsic
 * @since 2.0
 */
class EBCorePartyIdFormatterTypeTest {


    private static Stream<Arguments> participantIdentifierPositiveCases() {
        return Stream.of(
                Arguments.of(
                        "unregistered with <scheme-in-catalog",
                        true,
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered:domain:ec.europa.eu",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered:domain",
                        "ec.europa.eu",
                        null, null
                ),
                Arguments.of(
                        "Case insensitive schema",
                        true,
                        "urn:OASIS:names:tC:eBcore:partyId-type:unregistered:domain:ec.europa.eu",
                        "urn:OASIS:names:tC:eBcore:partyId-type:unregistered:domain",
                        "ec.europa.eu",
                        null, null
                ),
                Arguments.of(
                        "unregistered without <scheme-in-catalog",
                        true,
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered:ec.europa.eu",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered",
                        "ec.europa.eu",
                        null, null),
                Arguments.of(
                        "iso6523",
                        true,
                        "urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088:123456789",
                        "urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088",
                        "123456789",
                        null, null),
                Arguments.of("with spaces",
                        true,
                        "  urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088:123456789 ",
                        "urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088", "123456789",
                        null, null),
                Arguments.of("with spaces in the identifier",
                        true,
                        "urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088: 123456789 ",
                        "urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088", "123456789",
                        null, null),
                Arguments.of("Parse eDelivery URN format",
                        true,
                        "::urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088:123456789",
                        "urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088", "123456789",
                        null, null),
                Arguments.of("Parse peppol URN format 1",
                        true,
                        "urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088::123456789",
                        "urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088", "123456789",
                        null, null),
                Arguments.of("Parse peppol URN format 2",
                        true,
                        "urn:oasis:names:tc:ebcore:partyid-type:iso6523::0088:123456789",
                        "urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088", "123456789",
                        null, null),
                Arguments.of(
                        "invalid catalog identifier",
                        true,
                        "urn:oasis:names:tc:ebcore:partyid-type:invalid-catalog:ec.europa.eu",
                        "urn:oasis:names:tc:ebcore:partyid-type:invalid-catalog",
                        "ec.europa.eu",
                        IllegalArgumentException.class, "Invalid ebCore id "),
                Arguments.of(
                        "Not ebcore party id",
                        false,
                        "urn:ehealth:invalid-catalog:ec.europa.eu",
                        "urn:ehealth:invalid-catalog:ec.europa.eu",
                        null,
                        MalformedIdentifierException.class, "Malformed identifier"),
                Arguments.of(
                        "Not ebcore party id iso6523",
                        true,
                        "urn:oasis:names:tc:ebcore:partyid-type:iso6523::0088",
                        "urn:oasis:names:tc:ebcore:partyid-type:iso6523::0088",
                        null,
                        IllegalArgumentException.class, "Invalid ebCore id")
        );
    }

    EBCorePartyIdFormatterType testInstance = new EBCorePartyIdFormatterType();

    // input parameters


    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("participantIdentifierPositiveCases")
    void isTypeByScheme(String testName, boolean isEBCorePartyId, String toParseIdentifier, String schemaPart, String idPart, Class errorClass, String containsErrorMessage) {

        boolean result = testInstance.isTypeByScheme(schemaPart);
        assertEquals(isEBCorePartyId, result);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("participantIdentifierPositiveCases")
    void isType(String testName, boolean isEBCorePartyId, String toParseIdentifier, String schemaPart, String idPart, Class errorClass, String containsErrorMessage) {

        boolean result = testInstance.isType(toParseIdentifier);
        assertEquals(isEBCorePartyId, result);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("participantIdentifierPositiveCases")
    void format(String testName, boolean isEBCorePartyId, String toParseIdentifier, String schemaPart, String idPart, Class errorClass, String containsErrorMessage) {
        // skip format for not ebcore party ids
        if (!isEBCorePartyId) {
            return;
        }

        String result = testInstance.format(schemaPart, idPart);

        String schema = trimToEmpty(schemaPart);
        assertEquals(schema + ":" + trimToEmpty(idPart), result);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("participantIdentifierPositiveCases")
    void parse(String testName, boolean isEBCorePartyId, String toParseIdentifier, String schemaPart, String idPart, Class errorClass, String containsErrorMessage) {
        // skip parse not ebcore party ids
        if (!isEBCorePartyId) {
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
