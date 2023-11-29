/* Copyright 2017-2023 European Commission | eDelivery Dynamic Discovery Client
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence attached in file: LICENSE-EUPL-v1.2-EN.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 */
package eu.europa.ec.dynamicdiscovery.core.validator;

import eu.europa.ec.dynamicdiscovery.exception.XmlInvalidAgainstSchemaException;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Joze Rihtarsic
 * @since 5.0
 */
@ExtendWith(MockitoExtension.class)
class OasisSmpSchemaValidatorTest {

    private static Stream<Arguments> testOasisSMP10Arguments() {
        return Stream.of(
                Arguments.of("service_group_urn_poland_ncpb", false, null),
                Arguments.of("service_metadata_unsigned_valid_iso6523", false, null),
                Arguments.of("service_metadata_invalid_element_added", true, "Invalid content was found starting with element"),
                Arguments.of("service_metadata_element-missing", true, "is not complete"),
                Arguments.of("service_group_unexpected_attribute", true, "cvc-complex-type.3.2.2: Attribute 'unexpectedAttribute' is not allowed to appear in element 'ServiceMetadataReferenceCollection'."),
                Arguments.of("service_group_externalDTD", true, "External DTD: Failed to read external DTD 'any_external_file_address.dtd', because 'file' access is not allowed due to restriction set by the accessExternalDTD property.")
        );
    }

    private static Stream<Arguments> testOasisSMP20ServiceGroupArguments() {
        return Stream.of(
                Arguments.of("service_group_unsigned_valid_iso6523", false, null),
                Arguments.of("service_group_unsigned_invalid_iso6523", true, "Invalid content was found starting with element"),
                Arguments.of("service_group_unsigned_invalid_iso6523_DTD", true, "External DTD: Failed to read external DTD 'any_external_file_address.dtd', because 'file' access is not allowed due to restriction set by the accessExternalDTD property.")
        );
    }

    private static Stream<Arguments> testOasisSMP20ServiceMetadataArguments() {
        return Stream.of(
                Arguments.of("service_metadata_unsigned_valid_iso6523", false, null),
                Arguments.of("service_metadata_unsigned_redirection_iso6523", false, null),
                Arguments.of("service_metadata_unsigned_invalid_iso6523", true, "Invalid content was found starting with element"),
                Arguments.of("service_metadata_unsigned_invalid_iso6523_DTD", true, "External DTD: Failed to read external DTD 'any_external_file_address.dtd', because 'file' access is not allowed due to restriction set by the accessExternalDTD property.")
        );
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("testOasisSMP10Arguments")
    void testOasisSMP10Validate(String xmlFilename, boolean throwsError, String errorMessage) throws IOException, XmlInvalidAgainstSchemaException, URISyntaxException {
        // given
        byte[] xmlBody = loadSMP10XMLFileAsByteArray(xmlFilename);

        XmlInvalidAgainstSchemaException result = null;
        // when
        if (throwsError) {
            result = assertThrows(XmlInvalidAgainstSchemaException.class, () -> OasisSmpSchemaValidator.validateOasisSMP10Schema(xmlBody));
            MatcherAssert.assertThat(result.getMessage(), CoreMatchers.containsString(errorMessage));
        } else {
            OasisSmpSchemaValidator.validateOasisSMP10Schema(xmlBody);
        }
        assertEquals(throwsError, result != null);
    }


    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("testOasisSMP20ServiceGroupArguments")
    void testOasisSMP20Validate(String xmlFilename, boolean throwsError, String errorMessage) throws IOException, XmlInvalidAgainstSchemaException, URISyntaxException {
        // given
        byte[] xmlBody = loadSMP20XMLFileAsByteArray(xmlFilename);

        XmlInvalidAgainstSchemaException result = null;
        // when
        if (throwsError) {
            result = assertThrows(XmlInvalidAgainstSchemaException.class, () -> OasisSmpSchemaValidator.validateOasisSMP20ServiceGroupSchema(xmlBody));
            MatcherAssert.assertThat(result.getMessage(), CoreMatchers.containsString(errorMessage));
        } else {
            OasisSmpSchemaValidator.validateOasisSMP20ServiceGroupSchema(xmlBody);
        }
        assertEquals(throwsError, result != null);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("testOasisSMP20ServiceMetadataArguments")
    void testOasisSMP20ServiceMetadataValidate(String xmlFilename, boolean throwsError, String errorMessage) throws IOException, XmlInvalidAgainstSchemaException, URISyntaxException {
        // given
        byte[] xmlBody = loadSMP20XMLFileAsByteArray(xmlFilename);

        XmlInvalidAgainstSchemaException result = null;
        // when
        if (throwsError) {
            result = assertThrows(XmlInvalidAgainstSchemaException.class, () -> OasisSmpSchemaValidator.validateOasisSMP20ServiceMetadataSchema(xmlBody));
            MatcherAssert.assertThat(result.getMessage(), CoreMatchers.containsString(errorMessage));
        } else {
            OasisSmpSchemaValidator.validateOasisSMP20ServiceMetadataSchema(xmlBody);
        }
        assertEquals(throwsError, result != null);
    }

    public byte[] loadSMP10XMLFileAsByteArray(String name) throws IOException, URISyntaxException {
        return Files.readAllBytes(Paths.get(OasisSmpSchemaValidator.class.getResource("/response/oasis-smp-1.0/"+name+".xml").toURI()));
    }
    public byte[] loadSMP20XMLFileAsByteArray(String name) throws IOException, URISyntaxException {
        return Files.readAllBytes(Paths.get(OasisSmpSchemaValidator.class.getResource("/response/oasis-smp-2.0/"+name+".xml").toURI()));
    }
}
