package org.busdox.servicemetadata.publishing._1;

import org.busdox.transport.identifiers._1.ProcessIdentifierType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "ProcessType",
        propOrder = {"processIdentifier", "serviceEndpointList", "extension"}
)
public class ProcessType {
    @XmlElement(
            name = "ProcessIdentifier",
            namespace = "http://busdox.org/transport/identifiers/1.0/",
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


