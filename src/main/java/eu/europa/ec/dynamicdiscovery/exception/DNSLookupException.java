package eu.europa.ec.dynamicdiscovery.exception;

/**
 *
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public class DNSLookupException extends TechnicalException {

    public DNSLookupException() {
        this("DNSLookupException exception has occurred.");
    }

    public DNSLookupException(String message) {
        this(message, null);
    }

    public DNSLookupException(String message, Throwable cause) {
        super(message, cause);
    }
}
