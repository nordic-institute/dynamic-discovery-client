package org.oasis_open.docs.bdxr.ns.smp._2014._07;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.oasis_open.docs.bdxr.ns.smp._2014._07.ExtensionType;
import org.oasis_open.docs.bdxr.ns.smp._2014._07.ProcessIdentifierType;
import org.oasis_open.docs.bdxr.ns.smp._2014._07.ServiceEndpointList;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "ProcessType",
        propOrder = {"processIdentifier", "serviceEndpointList", "extension"}
)
public class ProcessType {
    @XmlElement(
            name = "ProcessIdentifier",
            required = true
    )
    protected ProcessIdentifierType processIdentifier;
    @XmlElement(
            name = "ServiceEndpointList",
            required = true
    )
    protected ServiceEndpointList serviceEndpointList;
    @XmlElement(
            name = "Extension"
    )
    protected ExtensionType extension;

    public ProcessType() {
    }

    public ProcessIdentifierType getProcessIdentifier() {
        return this.processIdentifier;
    }

    public void setProcessIdentifier(ProcessIdentifierType value) {
        this.processIdentifier = value;
    }

    public ServiceEndpointList getServiceEndpointList() {
        return this.serviceEndpointList;
    }

    public void setServiceEndpointList(ServiceEndpointList value) {
        this.serviceEndpointList = value;
    }

    public ExtensionType getExtension() {
        return this.extension;
    }

    public void setExtension(ExtensionType value) {
        this.extension = value;
    }
}
