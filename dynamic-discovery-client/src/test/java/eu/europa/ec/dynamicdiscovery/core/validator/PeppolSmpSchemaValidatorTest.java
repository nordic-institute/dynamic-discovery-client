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
/* Copyright 2017 European Commission | CEF eDelivery
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence attached in file: LICENCE-EUPL-v1.2.pdf
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 */
package eu.europa.ec.dynamicdiscovery.core.validator;

import eu.europa.ec.dynamicdiscovery.exception.XmlInvalidAgainstSchemaException;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Joze Rihtarsic
 * @since 3.1
 */
class PeppolSmpSchemaValidatorTest {

    @ParameterizedTest(name = "{index}: {0}")
    @CsvSource({"peppol_service_group_valid_iso6523_namespace, false, null",
            "signed_service_metadata_valid_iso6523_wildcard, false, null",
            "service_group_invalid_iso6523_DTD-01, true, 'External DTD'",
            "service_metadata_invalid_iso6523_DTD-01, true, 'External DTD'",
            "peppol_service_group_invalid_scheme, true, 'Cannot find the declaration of element'",
            "signed_service_metadata_invalid_scheme, true, 'Cannot find the declaration of element'"
    })
    void testOasisSMP10Validate(String xmlFilename, boolean throwsError, String errorMessage) throws IOException, XmlInvalidAgainstSchemaException, URISyntaxException {
        // given
        byte[] xmlBody = loadPeppolSMPXMLFileAsByteArray(xmlFilename);

        XmlInvalidAgainstSchemaException result = null;
        // when
        if (throwsError) {
            result = assertThrows(XmlInvalidAgainstSchemaException.class, () -> OasisSmpSchemaValidator.validateOasisSMP10Schema(xmlBody));
            MatcherAssert.assertThat(result.getMessage(), CoreMatchers.containsString(errorMessage));
        } else {
            PeppolSmpSchemaValidator.validatePeppolSMPSchema(xmlBody);
        }
        assertEquals(throwsError, result != null);
    }

    public byte[] loadPeppolSMPXMLFileAsByteArray(String name) throws IOException, URISyntaxException {
        return Files.readAllBytes(Paths.get(OasisSmpSchemaValidator.class.getResource("/response/peppol/" + name + ".xml").toURI()));
    }
}