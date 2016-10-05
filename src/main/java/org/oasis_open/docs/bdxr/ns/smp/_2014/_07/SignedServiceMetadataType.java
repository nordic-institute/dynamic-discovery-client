package org.oasis_open.docs.bdxr.ns.smp._2014._07;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.oasis_open.docs.bdxr.ns.smp._2014._07.ServiceMetadataType;
import org.w3._2000._09.xmldsig_.SignatureType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "SignedServiceMetadataType",
        propOrder = {"serviceMetadata", "signature"}
)
public class SignedServiceMetadataType {
    @XmlElement(
            name = "ServiceMetadata",
            required = true
    )
    protected ServiceMetadataType serviceMetadata;
    @XmlElement(
            name = "Signature",
            namespace = "http://www.w3.org/2000/09/xmldsig#",
            required = true
    )
    protected SignatureType signature;

    public SignedServiceMetadataType() {
    }

    public ServiceMetadataType getServiceMetadata() {
        return this.serviceMetadata;
    }

    public void setServiceMetadata(ServiceMetadataType value) {
        this.serviceMetadata = value;
    }

    public SignatureType getSignature() {
        return this.signature;
    }

    public void setSignature(SignatureType value) {
        this.signature = value;
    }
}
