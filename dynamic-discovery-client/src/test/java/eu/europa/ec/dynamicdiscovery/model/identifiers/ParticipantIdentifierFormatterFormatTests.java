package eu.europa.ec.dynamicdiscovery.model.identifiers;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Joze Rihtarsic
 * @since 2.0
 */
class ParticipantIdentifierFormatterFormatTests {


    private static Stream<Arguments> partyIdentifierTestArguments() {
        return Stream.of(
                Arguments.of("ebCore unregistered",
                        new SMPParticipantIdentifier("ec.europa.eu", "urn:oasis:names:tc:ebcore:partyid-type:unregistered:domain"),
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered:domain:ec.europa.eu",
                        "urn%3Aoasis%3Anames%3Atc%3Aebcore%3Apartyid-type%3Aunregistered%3Adomain%3Aec.europa.eu"),
                Arguments.of("ebCore iso6523",
                        new SMPParticipantIdentifier("123456789", "urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088"),
                        "urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088:123456789",
                        "urn%3Aoasis%3Anames%3Atc%3Aebcore%3Apartyid-type%3Aiso6523%3A0088%3A123456789"),
                Arguments.of("Double colon basic",
                        new SMPParticipantIdentifier("b", "a"),
                        "a::b",
                        "a%3A%3Ab"),
                Arguments.of("Double colon twice", new SMPParticipantIdentifier("b::c", "a"),
                        "a::b::c",
                        "a%3A%3Ab%3A%3Ac"),
                Arguments.of("Double colon iso6523",
                        new SMPParticipantIdentifier("0002:12345", "iso6523-actorid-upis"),
                        "iso6523-actorid-upis::0002:12345",
                        "iso6523-actorid-upis%3A%3A0002%3A12345"),
                Arguments.of("Double colon eHealth",
                        new SMPParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns"),
                        "ehealth-actorid-qns::urn:poland:ncpb",
                        "ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb"),
                Arguments.of("Identifier with spaces -  formatted to uri with '%20",
                        new SMPParticipantIdentifier("urn ncpb test", "ehealth-actorid-qns"),
                        "ehealth-actorid-qns::urn ncpb test",
                        "ehealth-actorid-qns%3A%3Aurn%20ncpb%20test")
        );
    }

    ParticipantIdentifierFormatter testInstance = new ParticipantIdentifierFormatter();

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("partyIdentifierTestArguments")
    void testFormat(String name, SMPParticipantIdentifier participantIdentifierType, String formattedIdentifier, String uriFormattedIdentifier) {

        String result = testInstance.format(participantIdentifierType);
        String uriResult = testInstance.urlEncodedFormat(participantIdentifierType);

        assertEquals(formattedIdentifier, result);
        assertEquals(uriFormattedIdentifier, uriResult);
    }
}
