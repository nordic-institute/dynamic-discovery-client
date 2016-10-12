package org.w3._2000._09.xmldsig_;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "SignaturePropertiesType",
        propOrder = {"signatureProperty"}
)
public class SignaturePropertiesType {
    @XmlElement(
            name = "SignatureProperty",
            required = true
    )
    protected List<SignaturePropertyType> signatureProperty;
    @XmlAttribute(
            name = "Id"
    )
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(
            name = "ID"
    )
    protected String id;

    public SignaturePropertiesType() {
    }

    public List<SignaturePropertyType> getSignatureProperty() {
        if(this.signatureProperty == null) {
            this.signatureProperty = new ArrayList();
        }

        return this.signatureProperty;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String value) {
        this.id = value;
    }
}
