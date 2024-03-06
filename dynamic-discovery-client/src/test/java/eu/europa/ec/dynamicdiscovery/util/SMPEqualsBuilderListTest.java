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
package eu.europa.ec.dynamicdiscovery.util;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Joze Rihtarsic
 * @since 2.0
 */
class SMPEqualsBuilderListTest {

    private static Stream<Arguments> equalsListsTestArguments() {
        return Stream.of(
                Arguments.of("Equal array",
                        Arrays.asList("a", "b", "c"),
                        Arrays.asList("a", "b", "c"),
                        true
                ),
                Arguments.of("Shuffled array",
                        Arrays.asList("a", "b", "c"),
                        Arrays.asList("b", "c", "a"),
                        true
                ),
                Arguments.of("Same size not match",
                        Arrays.asList("a", "b", "c"),
                        Arrays.asList("a", "b", "d"),
                        false
                ),
                Arguments.of("Same size repeated 1",
                        Arrays.asList("a", "b", "c"),
                        Arrays.asList("a", "a", "a"),
                        false
                ),
                Arguments.of("Same size repeated 1",
                        Arrays.asList("a", "a", "a"),
                        Arrays.asList("a", "b", "c"),
                        false
                ),
                Arguments.of("Both null",
                        null,
                        null,
                        true
                ),
                Arguments.of("Both empty",
                        new ArrayList<>(),
                        new ArrayList<>(),
                        true
                ),
                Arguments.of("One is null",
                        null,
                        Arrays.asList("a", "b", "c"),
                        false
                ));
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("equalsListsTestArguments")
    void equalsList(String name, List<String> firstList, List<String> secondList, boolean expectedResult) {

        boolean result = new SMPEqualsBuilder()
                .append(firstList, secondList).build();

        Assertions.assertEquals(expectedResult, result);
    }
}
