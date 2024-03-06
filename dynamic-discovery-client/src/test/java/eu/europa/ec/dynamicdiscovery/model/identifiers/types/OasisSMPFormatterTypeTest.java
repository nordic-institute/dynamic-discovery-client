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
