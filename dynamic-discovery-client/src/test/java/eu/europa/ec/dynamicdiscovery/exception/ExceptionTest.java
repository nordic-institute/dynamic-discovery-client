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
package eu.europa.ec.dynamicdiscovery.exception;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExceptionTest {

    @ParameterizedTest
    @CsvSource({
            "eu.europa.ec.dynamicdiscovery.exception.ConnectionException",
            "eu.europa.ec.dynamicdiscovery.exception.DDCRuntimeException",
            "eu.europa.ec.dynamicdiscovery.exception.DDCCertificateNotFoundException",
            "eu.europa.ec.dynamicdiscovery.exception.DDCInvalidConfigurationException",
            "eu.europa.ec.dynamicdiscovery.exception.DDCInvalidDataException",
            "eu.europa.ec.dynamicdiscovery.exception.DDCFetchException",
            "eu.europa.ec.dynamicdiscovery.exception.DNSLookupException",
            "eu.europa.ec.dynamicdiscovery.exception.DocumentParseException",
            "eu.europa.ec.dynamicdiscovery.exception.MalformedIdentifierException",
            "eu.europa.ec.dynamicdiscovery.exception.SignatureException",
            "eu.europa.ec.dynamicdiscovery.exception.DDCAuthorizationException",
    })
    void testExceptionWithMessage(String className) throws Exception {
        String message = "Test message";

        Class<?> clazz = Class.forName(className);
        Exception exception = (Exception) clazz.getConstructor(String.class)
                .newInstance(message);
        MatcherAssert.assertThat(exception.getMessage(), exception.getMessage().contains(message));
        assertEquals(message, exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "eu.europa.ec.dynamicdiscovery.exception.ConnectionException",
            "eu.europa.ec.dynamicdiscovery.exception.DDCRuntimeException",
            "eu.europa.ec.dynamicdiscovery.exception.DDCInvalidConfigurationException",
            "eu.europa.ec.dynamicdiscovery.exception.DDCInvalidDataException",
            "eu.europa.ec.dynamicdiscovery.exception.DDCCertificateNotFoundException",
            "eu.europa.ec.dynamicdiscovery.exception.DDCFetchException",
            "eu.europa.ec.dynamicdiscovery.exception.DNSLookupException",
            "eu.europa.ec.dynamicdiscovery.exception.DocumentParseException",
            "eu.europa.ec.dynamicdiscovery.exception.MalformedIdentifierException",
            "eu.europa.ec.dynamicdiscovery.exception.SignatureException",
            "eu.europa.ec.dynamicdiscovery.exception.XmlInvalidAgainstSchemaException",
            "eu.europa.ec.dynamicdiscovery.exception.DDCAuthorizationException",
    })
    void testExceptionWithMessageAndCause(String className) throws Exception {
        String message = "Test message";
        Throwable cause = new RuntimeException("Cause");

        Class<?> clazz = Class.forName(className);
        Exception exception = (Exception) clazz.getConstructor(String.class, Throwable.class)
                .newInstance(message, cause);

        MatcherAssert.assertThat(exception.getMessage(), exception.getMessage().contains(message));
        assertEquals(cause, exception.getCause());
    }
}
