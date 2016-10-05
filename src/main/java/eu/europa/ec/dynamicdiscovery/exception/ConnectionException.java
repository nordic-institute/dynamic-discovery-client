package eu.europa.ec.dynamicdiscovery.exception;

/**
 * Created by rodrfla on 05/10/2016.
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
