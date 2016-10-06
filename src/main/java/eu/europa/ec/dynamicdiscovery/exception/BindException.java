package eu.europa.ec.dynamicdiscovery.exception;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public class BindException extends TechnicalException {

    public BindException() {
        this("A bind exception has occurred.");
    }

    public BindException(String message) {
        this(message, null);
    }

    public BindException(String message, Throwable cause) {
        super(message, cause);
    }
}
