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
            "eu.europa.ec.dynamicdiscovery.exception.DNSFetchException",
            "eu.europa.ec.dynamicdiscovery.exception.DNSLookupException",
            "eu.europa.ec.dynamicdiscovery.exception.DocumentParseException",
            "eu.europa.ec.dynamicdiscovery.exception.MalformedIdentifierException",
            "eu.europa.ec.dynamicdiscovery.exception.SignatureException"
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
            "eu.europa.ec.dynamicdiscovery.exception.DNSFetchException",
            "eu.europa.ec.dynamicdiscovery.exception.DNSLookupException",
            "eu.europa.ec.dynamicdiscovery.exception.DocumentParseException",
            "eu.europa.ec.dynamicdiscovery.exception.MalformedIdentifierException",
            "eu.europa.ec.dynamicdiscovery.exception.SignatureException",
            "eu.europa.ec.dynamicdiscovery.exception.XmlInvalidAgainstSchemaException",
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