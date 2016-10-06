package eu.europa.ec.dynamicdiscovery.exception;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public class ConnectionException extends TechnicalException {

    public ConnectionException() {
        this("Connection exception has occurred.");
    }

    public ConnectionException(String message) {
        this(message, null);
    }

    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
