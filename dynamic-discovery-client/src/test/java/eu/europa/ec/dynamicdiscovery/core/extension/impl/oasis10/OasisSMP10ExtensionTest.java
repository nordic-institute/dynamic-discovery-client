package eu.europa.ec.dynamicdiscovery.core.extension.impl.oasis10;

import eu.europa.ec.dynamicdiscovery.core.extension.IObjectReader;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceGroup;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.xml.namespace.QName;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Joze Rihtarsic
 * @since 2.0
 */
class OasisSMP10ExtensionTest {

    private static Stream<Arguments> testParameters() {
        return Stream.of(
                Arguments.of("Test ServiceGroup",
                        new QName("http://docs.oasis-open.org/bdxr/ns/SMP/2016/05", "ServiceGroup"),
                        SMPServiceGroup.class,
                        OasisSMP10ServiceGroupReader.class,
                        true
                ),
                Arguments.of("Test SignedServiceMetadata",
                        new QName("http://docs.oasis-open.org/bdxr/ns/SMP/2016/05", "SignedServiceMetadata"),
                        SMPServiceMetadata.class,
                        OasisSMP10ServiceMetadataReader.class,
                        true
                ),
                Arguments.of("ServiceGroup Wrong class",
                        new QName("http://docs.oasis-open.org/bdxr/ns/SMP/2016/05", "ServiceGroup"),
                        SMPServiceMetadata.class,
                        null,
                        false
                ),
                Arguments.of("ServiceMetadata Wrong class",
                        new QName("http://docs.oasis-open.org/bdxr/ns/SMP/2016/05", "SignedServiceMetadata"),
                        SMPServiceGroup.class,
                        null,
                        false
                ),
                Arguments.of("ServiceMetadata Wrong namespace",
                        new QName("http://docs.oasis-open.org/bdxr/ns/SMP/2016/wrong", "SignedServiceMetadata"),
                        SMPServiceGroup.class,
                        null,
                        false
                )
        );
    }

    OasisSMP10Extension testInstance = new OasisSMP10Extension();

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
}
