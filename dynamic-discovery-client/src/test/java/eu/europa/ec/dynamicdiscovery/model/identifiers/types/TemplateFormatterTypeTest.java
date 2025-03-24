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
package eu.europa.ec.dynamicdiscovery.model.identifiers.types;

import eu.europa.ec.dynamicdiscovery.enums.DNSLookupFormatType;
import eu.europa.ec.dynamicdiscovery.enums.DNSLookupHashType;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.trim;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Joze Rihtarsic
 * @since 2.0
 */
class TemplateFormatterTypeTest {


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
                        "urn:justice:si:1123445",
                        "urn:justice:si",
                        "1123445",
                        IllegalArgumentException.class, "does not match regular expression")
        );
    }

    TemplateFormatterType testInstance
            = new TemplateFormatterType(Pattern.compile("^(?i)\\s*(::)?((urn:ehealth:[a-zA-Z]{2})|mailto).*$"),
            "${scheme}::${identifier}",
            Pattern.compile("^(?i)\\s*(::)?(?<scheme>(urn:ehealth:[a-zA-Z]{2})|mailto):?(?<identifier>.+)?\\s*$")
    );


    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("participantIdentifierPositiveCases")
    void isTypeByScheme(String testName, boolean isValidPartyId, String toParseIdentifier, String schemaPart, String idPart, Class errorClass, String containsErrorMessage) {

        boolean result = testInstance.isSchemeValid(schemaPart);
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

        String result = testInstance.format(idPart, schemaPart);
        assertEquals(trim(idPart) + "::" + trim(schemaPart), result);
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

    @ParameterizedTest(name = "{index} {0}")
    @CsvSource({
            "Test NAPTR 9915, iso6523-actorid-upis, 9915:1234567890, SHA256_BASE32, 7XKO2KDROICHCCHPB7U6KMM5U7E2UHV6NR4OP4AD7R74HYNDNOXQ.9915.iso6523-actorid-upis",
            "Test NAPTR 0088, iso6523-actorid-upis, 0088:1234567890, SHA256_BASE32, RJUAFVEKBQJSVT3HDHLN34S4BVVM5GBFTPD5TDI5BTBTKXKBNTNA.0088.iso6523-actorid-upis",
            "Test CNAME 9915, iso6523-actorid-upis, 9915:1234567890, MD5_HEX, B-1b3826fdfc84df07744b11acae7fa614.9915.iso6523-actorid-upis",
            "Test CNAME 0088, iso6523-actorid-upis, 0088:1234567890, MD5_HEX, B-8d445c8aa1f398f6f5f4a147fe63f120.0088.iso6523-actorid-upis",
    })
    void testDNSLookup(String testName, String scheme, String identifier, DNSLookupHashType hashType, String expected) {
        System.out.println(testName);
        Pattern matchSchema = Pattern.compile("^(?i)(iso6523-actorid-upis)");
        String formatTemplate = "${scheme}::${identifier}";
        Pattern splitRegularExpression = Pattern.compile("^(?i)\\s*(::)?(?<scheme>(iso6523-actorid-upis))::?(?<identifier>.+)?\\s*$");
        Pattern splitDNSLookupRegularExpression = Pattern.compile("^(?i)\\s*(::)?(?<scheme>(iso6523-actorid-upis))::?(?<icd>\\d{4})?:(?<identifier>.+)?\\s*$");

        TemplateFormatterType testInstanceFormat  = new TemplateFormatterType(
                        matchSchema,
                        formatTemplate,
                        formatTemplate,
                        splitRegularExpression,
                        DNSLookupFormatType.IDENTIFIER_IN_HASH,
                        "${icd}.${scheme}",
                        splitDNSLookupRegularExpression
                );
        String result = testInstanceFormat.dnsLookupFormat(scheme, identifier, hashType);
        assertEquals(expected, result);
    }

    @CsvSource({
            "Test NAPTR 9915, iso6523-actorid-upis, 9915:1234567890, SHA256_BASE32",
            "Test NAPTR 0088, iso6523-actorid-upis, 0088:1234567890, SHA256_BASE32",
            "Test CNAME 9915, iso6523-actorid-upis, 9915:1234567890, MD5_HEX",
            "Test CNAME 0088, iso6523-actorid-upis, 0088:1234567890, MD5_HEX",
    })
    void testDNSLookupAsPeppolFormat(String testName, String scheme, String identifier, DNSLookupHashType hashType, String expected) {
        System.out.println(testName);
        Pattern matchSchema = Pattern.compile("^(?i)(iso6523-actorid-upis)");
        String formatTemplate = "${scheme}::${identifier}";
        Pattern splitRegularExpression = Pattern.compile("^(?i)\\s*(::)?(?<scheme>(iso6523-actorid-upis))::?(?<identifier>.+)?\\s*$");
        Pattern splitDNSLookupRegularExpression = splitRegularExpression;

        TemplateFormatterType testInstanceFormat  = new TemplateFormatterType(
                matchSchema,
                formatTemplate,
                formatTemplate,
                splitRegularExpression,
                DNSLookupFormatType.IDENTIFIER_IN_HASH,
                "${scheme}",
                splitDNSLookupRegularExpression
        );
        PeppolPartyIdFormatterType testInstanceFormatPeppol  = new PeppolPartyIdFormatterType();
        String expectedPeppol = testInstanceFormatPeppol.dnsLookupFormat(scheme, identifier, hashType);
        String result = testInstanceFormat.dnsLookupFormat(scheme, identifier, hashType);
        assertEquals(expectedPeppol, result);
    }

    @Test
    void testRegularExpression(){
        String testValue = "iso6523-actorid-upis::9915:1234567890123";
        Pattern splitDNSLookupRegularExpression = Pattern.compile("^(?i)\\s*(::)?(?<scheme>(iso6523-actorid-upis))::?(?<icd>\\d{4})?:(?<identifier>.+)?\\s*$");

        Matcher matcher = splitDNSLookupRegularExpression.matcher(testValue);

        assertTrue(matcher.matches());
        assertEquals("iso6523-actorid-upis", matcher.group("scheme"));
        assertEquals("9915", matcher.group("icd"));
        assertEquals("1234567890123", matcher.group("identifier"));

    }
}
