package eu.europa.ec.dynamicdiscovery.model.identifiers;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;



import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Joze Rihtarsic
 * @since 2.0
 */
class ParticipantIdentifierFormatterParseTests {
    private static Stream<Arguments> partyIdentifierTestArguments() {
        return Stream.of(
                Arguments.of("ebCore unregistered", false, "urn:oasis:names:tc:ebcore:partyid-type:unregistered:domain:ec.europa.eu", "urn:oasis:names:tc:ebcore:partyid-type:unregistered:domain", "ec.europa.eu" ),
                Arguments.of("ebCore iso6523", false, "urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088:123456789", "urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088", "123456789" ),
                Arguments.of("ebCore with space 1", false, " urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088:123456789", "urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088", "123456789" ),
                Arguments.of("ebCore with space 2", false, "urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088:123456789 ", "urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088", "123456789" ),
                Arguments.of("ebCore with space 3", false, "urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088::123456789 ", "urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088", "123456789" ),
                Arguments.of("ebCore unregistered with urn and colons", false, "urn:oasis:names:tc:ebcore:partyid-type:unregistered:ehealth:urn:ehealth:pl:ncp-idp", "urn:oasis:names:tc:ebcore:partyid-type:unregistered:ehealth", "urn:ehealth:pl:ncp-idp" ),
                Arguments.of("ebCore unregistered with dash", false, "urn:oasis:names:tc:ebcore:partyid-type:unregistered:ehealth:pl:ncp-idp", "urn:oasis:names:tc:ebcore:partyid-type:unregistered:ehealth", "pl:ncp-idp" ),
                Arguments.of("ebCore unregistered example double colon", false, "urn:oasis:names:tc:ebcore:partyid-type:unregistered::blue-gw", "urn:oasis:names:tc:ebcore:partyid-type:unregistered", "blue-gw" ),
                Arguments.of("ebCore unregistered example", false, "urn:oasis:names:tc:ebcore:partyid-type:unregistered:blue-gw", "urn:oasis:names:tc:ebcore:partyid-type:unregistered", "blue-gw" ),
                Arguments.of("ebCore unregistered domain example", false, "urn:oasis:names:tc:ebcore:partyid-type:unregistered:ec.europa.eu", "urn:oasis:names:tc:ebcore:partyid-type:unregistered", "ec.europa.eu" ),
                Arguments.of("ebCore unregistered email scheme example", false, "urn:oasis:names:tc:ebcore:partyid-type:unregistered:email:test@my.mail.com", "urn:oasis:names:tc:ebcore:partyid-type:unregistered:email", "test@my.mail.com" ),
                Arguments.of("ebCore unregistered email example", false, "urn:oasis:names:tc:ebcore:partyid-type:unregistered:test@my.mail.com", "urn:oasis:names:tc:ebcore:partyid-type:unregistered", "test@my.mail.com" ),
                Arguments.of("ebCore with double colon", false, " urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088::123456789", "urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088", "123456789" ),
                Arguments.of("ebCore with double colon start", false, " ::urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088:123456789", "urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088", "123456789" ),
                Arguments.of("Double colon basic", false, "a::b", "a", "b" ),
                Arguments.of("Double colon twice", false, "a::b::c", "a", "b::c" ),
                Arguments.of("Double colon iso6523", false, "iso6523-actorid-upis::0002:gutek", "iso6523-actorid-upis", "0002:gutek" ),
                Arguments.of("Double colon ehealth", false, "ehealth-actorid-qns::urn:poland:ncpb", "ehealth-actorid-qns", "urn:poland:ncpb" ),
                Arguments.of("Double colon ehealth 2", false, "ehealth-actorid-qns::urn:ehealth:hr:ncpb-idp", "ehealth-actorid-qns", "urn:ehealth:hr:ncpb-idp" ),
                Arguments.of("Double colon ehealth 3", false, "scheme::urn:ehealth:pt:ncpb-idp", "scheme", "urn:ehealth:pt:ncpb-idp" ),
                Arguments.of("Double colon custom scheme", false, "otherscheme::urn:ehealth:be:ncpb-idp", "otherscheme", "urn:ehealth:be:ncpb-idp" ),
                Arguments.of("ebCore iso6523", true, "urn:oasis:names:tc:ebcore:partyid-type:iso6523:Illegal-value-without-scheme", null, null ),
                Arguments.of("ebCore with no catalog", true, " urn:oasis:names:tc:ebcore:partyid-type:0088123456789", null, null)
        );
    }


    ParticipantIdentifierFormatter testInstance = new ParticipantIdentifierFormatter();

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("partyIdentifierTestArguments")
    void testPartyIdentifierParse( String name,boolean throwError,String identifier,String schemaPart, String idPart) {
        IllegalArgumentException exception = null;
        SMPParticipantIdentifier result = null;
        if (throwError) {
            exception = assertThrows(IllegalArgumentException.class, () -> testInstance.parse(identifier));
        } else {
            result = testInstance.parse(identifier);
        }

        assertNotNull(throwError ? exception : result);
        if (!throwError) {
            assertEquals(schemaPart, result.getScheme());
            assertEquals(idPart, result.getIdentifier());
        }
    }


}
