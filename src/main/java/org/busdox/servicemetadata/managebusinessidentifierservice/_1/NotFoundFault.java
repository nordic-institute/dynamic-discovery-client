package org.busdox.servicemetadata.managebusinessidentifierservice._1;

import org.busdox.servicemetadata.locator._1.FaultType;

import javax.xml.ws.WebFault;

@WebFault(name="NotFoundFault", targetNamespace="http://busdox.org/serviceMetadata/locator/1.0/")
public class NotFoundFault
extends Exception {
    private FaultType faultInfo;

    public NotFoundFault(String message, FaultType faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    public NotFoundFault(String message, FaultType faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    public FaultType getFaultInfo() {
        return this.faultInfo;
    }
}

