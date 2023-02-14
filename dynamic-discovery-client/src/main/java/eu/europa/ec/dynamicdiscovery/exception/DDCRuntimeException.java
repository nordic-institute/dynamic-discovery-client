package eu.europa.ec.dynamicdiscovery.exception;

/**
 * DDCRuntimeException is the "unchecked exception" thrown when an exceptional condition has occurred. The error
 * does not need to be declared in a method or constructor's throws clause.
 *
 * NOTE: The error must not be used in regular negative flows of the dynamic discovery process.
 *
 * @since 2.0
 * @author Joze Rihtarsic
 */
public class DDCRuntimeException extends RuntimeException {


    public DDCRuntimeException(String message) {
        super(message);
    }

    public DDCRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
