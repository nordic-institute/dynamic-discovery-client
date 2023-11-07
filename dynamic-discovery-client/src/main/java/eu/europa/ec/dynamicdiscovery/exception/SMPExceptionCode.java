package eu.europa.ec.dynamicdiscovery.exception;

public enum SMPExceptionCode {

    SERVICE_GROUP("Service group error"),
    SERVICE_METADATA("Service metadata error");

    protected final String message;

    SMPExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
