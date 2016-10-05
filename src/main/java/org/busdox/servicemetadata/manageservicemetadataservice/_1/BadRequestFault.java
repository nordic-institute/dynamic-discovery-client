package org.busdox.servicemetadata.manageservicemetadataservice._1;

import javax.xml.ws.WebFault;
import org.busdox.servicemetadata.locator._1.FaultType;

@WebFault(name="BadRequestFault", targetNamespace="http://busdox.org/serviceMetadata/locator/1.0/")
public class BadRequestFault
extends Exception {
    private FaultType faultInfo;

    public BadRequestFault(String message, FaultType faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    public BadRequestFault(String message, FaultType faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    public FaultType getFaultInfo() {
        return this.faultInfo;
    }
}

