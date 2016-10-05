package eu.europa.ec.dynamicdiscovery.exception;

/**
 * Created by rodrfla on 05/10/2016.
 */
public abstract class TechnicalException extends Exception {

    public TechnicalException(String message) {
        this(message, null);
    }

    public TechnicalException(String message, Throwable cause) {
        super(message, cause);
    }
}
