package eu.europa.ec.dynamicdiscovery.exception;

public enum SMPExceptionCode {

    SERVICE_GROUP("Service group error"),
    SERVICE_METADATA("Service metadata error");

    SMPExceptionCode(String message) {
        this.message = message;
    }

    protected String message;

    public String getMessage() {
        return message;
    }
}
