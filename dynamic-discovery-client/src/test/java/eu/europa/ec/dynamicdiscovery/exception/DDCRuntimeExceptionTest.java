
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