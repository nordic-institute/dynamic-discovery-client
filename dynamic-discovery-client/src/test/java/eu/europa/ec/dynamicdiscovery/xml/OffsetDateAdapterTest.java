/*-
 * #%L
 * dynamic-discovery-client
 * %%
 * Copyright (C) 2016 - 2025 European Commission | eDelivery | Dynamic Discovery Client
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
package eu.europa.ec.dynamicdiscovery.xml;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.OffsetDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OffsetDateAdapterTest {

    private static Stream<Arguments> testDateArguments() {
        return Stream.of(
                Arguments.of("Null value",
                        null,
                        null
                ),
                Arguments.of("Empty value",
                        "",
                        null
                ),
                Arguments.of("Blank value",
                        "  ",
                        null
                ),
                Arguments.of("DateTime with positive offset",
                        "2024-09-03" + OffsetDateTime.now().getOffset(),
                        OffsetDateTime.parse("2024-09-03T00:00:00" + OffsetDateTime.now().getOffset())
                ),
                Arguments.of("DateTime with default offset",
                        "2024-09-03",
                        OffsetDateTime.parse("2024-09-03T00:00:00" + OffsetDateTime.now().getOffset())
                )
        );
    }

    OffsetDateAdapter testInstance = new OffsetDateAdapter();

    @ParameterizedTest
    @MethodSource("testDateArguments")
    void unmarshal(String desc, String value, OffsetDateTime expected) {
        System.out.println("unmarshal: " + desc);
        assertEquals(expected, testInstance.unmarshal(value));
    }

    @Test
    void marshal() {
        // create test
        OffsetDateTime value = OffsetDateTime.
                parse("2011-12-03T00:00:00+04:00");
        String expected = "2011-12-03+04:00";
        String result = testInstance.marshal(value);
        assertEquals(expected, result);
    }
}
