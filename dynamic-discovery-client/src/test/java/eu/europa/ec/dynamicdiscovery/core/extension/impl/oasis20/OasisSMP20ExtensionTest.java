package eu.europa.ec.dynamicdiscovery.core.extension.impl.oasis20;

import eu.europa.ec.dynamicdiscovery.core.extension.IObjectReader;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceGroup;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.xml.namespace.QName;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author  Joze Rihtarsic
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
                Arguments.of( "Test ServiceMetadata",
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
    void handlesTest(String name, QName qName, Class clazzResult,  Class clazzReader, boolean handles) {

        boolean result = testInstance.handles(qName, clazzResult);

        assertEquals(handles, result, name);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("testParameters")
    void getParser(String name, QName qName, Class clazzResult, Class clazzReader, boolean handles) {

        IObjectReader result = testInstance.getParser(qName, clazzResult);

        assertEquals(handles, result!=null, name);
        if (handles) {
            assertEquals(clazzReader, result.getClass());
        }

    }
}
