package eu.europa.ec.dynamicdiscovery.exception;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public abstract class TechnicalException extends Exception {

    public TechnicalException(String message) {
        this(message, null);
    }

    public TechnicalException(String message, Throwable cause) {
        super(message, cause);
    }
}
