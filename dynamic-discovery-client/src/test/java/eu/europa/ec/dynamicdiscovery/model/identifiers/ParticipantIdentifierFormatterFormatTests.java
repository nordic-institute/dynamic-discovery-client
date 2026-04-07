/*-
 * #%L
 * dynamic-discovery-client
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
package eu.europa.ec.dynamicdiscovery.model.identifiers;

import eu.europa.ec.dynamicdiscovery.exception.MalformedIdentifierException;
import eu.europa.ec.dynamicdiscovery.model.identifiers.types.OasisSMPFormatterType;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.regex.Pattern;
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


    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("partyIdentifierTestArguments")
    void testFormat(String name, SMPParticipantIdentifier participantIdentifierType,
                    String formattedIdentifier, String uriFormattedIdentifier) {
        System.out.println("Testing Format: [" + name + "] with identifier: [" + participantIdentifierType + "]");
        ParticipantIdentifierFormatter testInstance = new ParticipantIdentifierFormatter.Builder().initDefault().build();
        String result = testInstance.format(participantIdentifierType);
        String uriResult = testInstance.urlEncodedFormat(participantIdentifierType);

        assertEquals(formattedIdentifier, result);
        assertEquals(uriFormattedIdentifier, uriResult);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @CsvSource(value = {
            "Valid identifier, t-t-t::1234:abcd, true, ''",
            "Bad scheme length, test-test-test::1234:abcd, false, 'A scheme identifier MUST NOT exceed 10 characters!'",
            "Bad Value length, t-t-t::1234:abcdefghijklmnoprst, false, 'A identifier value MUST NOT exceed 15 characters!'",
            "Bad scheme pattern, test-t::1234:abcd, false, 'Identifier: [test-t::1234:abcd] has invalid scheme [test-t] (Check the length or scheme pattern)!'",
            "Bad Value pattern, t-t-t::1234-abcd, false, 'Identifier value [1234-abcd] is illegal.'",
            "Missing scheme, 1234:abcd, false, 'Invalid Identifier: [1234:abcd]. Can not detect schema!'",
            "ebCore unregistered,urn:oasis:names:tc:ebcore:partyid-type:unregistered:domain:ec.europa.eu,false, 'Can not detect schema!'",
    })
    void testParseFailOnlyOasisFormatter(String name, String formattedIdentifier, boolean isValid, String containsErrorMessage) {
        System.out.println("Testing ParseFailOnlyOasisFormatter: [" + name + "] with identifier: [" + formattedIdentifier + "]");
        ParticipantIdentifierFormatter identifierFormatter = new ParticipantIdentifierFormatter.Builder()
                .defaultFormatter(new OasisSMPFormatterType.Builder()
                        .wildcardEnabled(false)
                        .valueMaxLength(15)
                        .schemeMandatory(true)
                        .schemeMaxLength(10)
                        .schemeValidationPattern(Pattern.compile("^[a-zA-Z0-9]+-[a-zA-Z0-9]+-[a-zA-Z0-9]+$"))
                        .valueValidationPattern(Pattern.compile("^[0-9]{4}:[a-zA-Z0-9]+$"))
                        .build())
                .build();

        if (isValid) {
            SMPParticipantIdentifier result = identifierFormatter.parse(formattedIdentifier);
            Assertions.assertNotNull(result, "Expected valid identifier for: " + formattedIdentifier);
        } else {
            MalformedIdentifierException result = Assertions.assertThrows(MalformedIdentifierException.class, () -> identifierFormatter.parse(formattedIdentifier));
            MatcherAssert.assertThat(result.getMessage(), CoreMatchers.containsString(containsErrorMessage));
        }
    }

    /**
     * Test parsing of the default formatter.
     * <p>
     * This test uses the default formatter settings for parsing ebCorePartyIdentifier and peppol party identifiers.
     */
    @ParameterizedTest(name = "{index}: {0}")
    @CsvSource(value = {
            "Valid identifier, t-t-t::1234:abcd, true, ''",
            "ebCore unregistered,urn:oasis:names:tc:ebcore:partyid-type:unregistered:domain:ec.europa.eu,true, ''",
            "ebCore invalid unregistered,urn:oasis:names:tc:ebcore:partyid:unregistered:domain:ec.europa.eu,false, 'Invalid Identifier: '",
            "Bad scheme length, test-test1234567890-test1234567890::1234:abcd, false, 'A scheme identifier MUST NOT exceed 25 characters!'",
            "The Peppol  largest OK length, iso6523-actorid-upis::1234:1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890, true, ''",
            "Bad Peppol Value length, iso6523-actorid-upis::1234:12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901, false, 'A identifier value MUST NOT exceed 135 characters!'",
            "Bad scheme pattern, test-t::1234:abcd, false, 'Identifier: [test-t::1234:abcd] has invalid scheme [test-t] (Check the length or scheme pattern)!'",
    })
    void testParseDefaultFormatter(String name, String formattedIdentifier, boolean isValid, String containsErrorMessage) {
        System.out.println("Testing ParseDefaultFormatter: [" + name + "] with identifier: [" + formattedIdentifier + "]");
        ParticipantIdentifierFormatter identifierFormatter = new ParticipantIdentifierFormatter.Builder().initDefault()
                .build();
        identifierFormatter.setSchemeMandatory(true);

        if (isValid) {
            SMPParticipantIdentifier result = identifierFormatter.parse(formattedIdentifier);
            Assertions.assertNotNull(result, "Expected valid identifier for: " + formattedIdentifier);
        } else {
            MalformedIdentifierException result = Assertions.assertThrows(MalformedIdentifierException.class, () -> identifierFormatter.parse(formattedIdentifier));
            MatcherAssert.assertThat(result.getMessage(), CoreMatchers.containsString(containsErrorMessage));
        }
    }
}
