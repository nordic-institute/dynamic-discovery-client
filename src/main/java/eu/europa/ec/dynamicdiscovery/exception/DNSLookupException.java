package eu.europa.ec.dynamicdiscovery.exception;

/**
 *
 * Created by rodrfla on 05/10/2016.
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
