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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DDCRuntimeExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String message = "Test message";
        DDCRuntimeException exception = new DDCRuntimeException(message);

        assertEquals(DDCExceptionCode.GENERIC_ERROR, exception.getSmpExceptionCode());
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testConstructorWithCodeAndMessage() {
        String message = "Test message";
        DDCExceptionCode code = DDCExceptionCode.FETCH_EXCEPTION;
        DDCRuntimeException exception = new DDCRuntimeException(code, message);

        assertEquals(code, exception.getSmpExceptionCode());
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String message = "Test message";
        Throwable cause = new RuntimeException("Cause");
        DDCRuntimeException exception = new DDCRuntimeException(message, cause);

        assertEquals(DDCExceptionCode.GENERIC_ERROR, exception.getSmpExceptionCode());
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testConstructorWithCodeMessageAndCause() {
        String message = "Test message";
        Throwable cause = new RuntimeException("Cause");
        DDCExceptionCode code = DDCExceptionCode.FETCH_EXCEPTION;
        DDCRuntimeException exception = new DDCRuntimeException(code, message, cause);

        assertEquals(code, exception.getSmpExceptionCode());
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
