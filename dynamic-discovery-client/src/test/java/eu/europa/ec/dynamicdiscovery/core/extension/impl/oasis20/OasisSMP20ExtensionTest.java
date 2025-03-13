/*
 * #%L
 * dynamic-discovery-cli
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
package eu.europa.ec.dynamicdiscovery.core.extension.impl.oasis20;

import eu.europa.ec.dynamicdiscovery.core.extension.IObjectReader;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceGroup;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import javax.xml.namespace.QName;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Joze Rihtarsic
 * @since 2.0
 */
class OasisSMP20ExtensionTest {

    private static Stream<Arguments> testParameters() {
        return Stream.of(
                Arguments.of("Test ServiceGroup",
                        new QName("http://docs.oasis-open.org/bdxr/ns/SMP/2/ServiceGroup", "ServiceGroup"),
                        SMPServiceGroup.class,
                        OasisSMP20ServiceGroupReader.class,
                        true
                ),
                Arguments.of("Test ServiceMetadata",
                        new QName("http://docs.oasis-open.org/bdxr/ns/SMP/2/ServiceMetadata", "ServiceMetadata"),
                        SMPServiceMetadata.class,
                        OasisSMP20ServiceMetadataReader.class,
                        true
                ),
                Arguments.of("ServiceGroup Wrong class",
                        new QName("http://docs.oasis-open.org/bdxr/ns/SMP/2/ServiceGroup", "ServiceGroup"),
                        SMPServiceMetadata.class,
                        null,
                        false
                ),
                Arguments.of("ServiceMetadata Wrong class",
                        new QName("http://docs.oasis-open.org/bdxr/ns/SMP/2/ServiceMetadata", "ServiceMetadata"),
                        SMPServiceGroup.class,
                        null,
                        false
                ),
                Arguments.of("ServiceMetadata Wrong namespace",
                        new QName("http://docs.oasis-open.org/bdxr/ns/SMP/2016/wrong", "ServiceMetadata"),
                        SMPServiceMetadata.class,
                        null,
                        false
                )
        );
    }

    OasisSMP20Extension testInstance = new OasisSMP20Extension();

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("testParameters")
    void handlesTest(String name, QName qName, Class clazzResult, Class clazzReader, boolean handles) {

        boolean result = testInstance.handles(qName, clazzResult);

        assertEquals(handles, result, name);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("testParameters")
    void getParser(String name, QName qName, Class clazzResult, Class clazzReader, boolean handles) {

        IObjectReader result = testInstance.getParser(qName, clazzResult);

        assertEquals(handles, result != null, name);
        if (handles) {
            assertEquals(clazzReader, result.getClass());
        }

    }

    @ParameterizedTest
    @CsvSource({"'oasis-bdxr-smp-2', true, true, 'oasis-bdxr-smp-2, Meta:SMP'",
            "'oasis-bdxr-smp-2,Meta:SMP', true, false, 'oasis-bdxr-smp-2, Meta:SMP'",
            "'oasis-bdxr-smp-2,Meta:SMP', false, true, 'oasis-bdxr-smp-2'",
            "'oasis-bdxr-smp-2', false, false, 'oasis-bdxr-smp-2'",
    })
    void testSetSMP10LookupNaptrServiceEnabled(String initServices, boolean enable, boolean expectedResult, String finalServices) {
        // given
        OasisSMP20Extension testInstance = new OasisSMP20Extension(false, initServices.split(","));
        // when
        boolean result = testInstance.setSMP10LookupNaptrServiceEnabled(enable);
        // then
        assertEquals(expectedResult, result);
        assertEquals(finalServices, String.join(", ", testInstance.lookupServices()));
    }

    @ParameterizedTest
    @CsvSource({"'oasis-bdxr-smp-2', false ",
            "'oasis-bdxr-smp-2,Meta:SMP', true",
            "'Meta:SMP', true",
    })
    void testIsSMP10LookupNaptrServiceEnabled(String initServices, boolean expectedResult) {
        // given
        OasisSMP20Extension testInstance = new OasisSMP20Extension(false, initServices.split(","));
        // when
        boolean result = testInstance.isSMP10LookupNaptrServiceEnabled();
        // then
        assertEquals(expectedResult, result);
    }

}
