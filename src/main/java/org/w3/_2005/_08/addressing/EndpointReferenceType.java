package org.w3._2005._08.addressing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import org.w3._2005._08.addressing.AttributedURIType;
import org.w3._2005._08.addressing.MetadataType;
import org.w3._2005._08.addressing.ReferenceParametersType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "EndpointReferenceType",
        propOrder = {"address", "referenceParameters", "metadata", "any"}
)
public class EndpointReferenceType {
    @XmlElement(
            name = "Address",
            required = true
    )
    protected AttributedURIType address;
    @XmlElement(
            name = "ReferenceParameters"
    )
    protected ReferenceParametersType referenceParameters;
    @XmlElement(
            name = "Metadata"
    )
    protected MetadataType metadata;
    @XmlAnyElement(
            lax = true
    )
    protected List<Object> any;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap();

    public EndpointReferenceType() {
    }

    public AttributedURIType getAddress() {
        return this.address;
    }

    public void setAddress(AttributedURIType value) {
        this.address = value;
    }

    public ReferenceParametersType getReferenceParameters() {
        return this.referenceParameters;
    }

    public void setReferenceParameters(ReferenceParametersType value) {
        this.referenceParameters = value;
    }

    public MetadataType getMetadata() {
        return this.metadata;
    }

    public void setMetadata(MetadataType value) {
        this.metadata = value;
    }

    public List<Object> getAny() {
        if(this.any == null) {
            this.any = new ArrayList();
        }

        return this.any;
    }

    public Map<QName, String> getOtherAttributes() {
        return this.otherAttributes;
    }
}

