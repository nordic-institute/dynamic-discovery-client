package org.w3._2005._08.addressing;

import javax.xml.bind.annotation.*;
import javax.xml.namespace.QName;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "AttributedUnsignedLongType",
        propOrder = {"value"}
)
public class AttributedUnsignedLongType {
    @XmlValue
    @XmlSchemaType(
            name = "unsignedLong"
    )
    protected BigInteger value;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap();

    public AttributedUnsignedLongType() {
    }

    public BigInteger getValue() {
        return this.value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public Map<QName, String> getOtherAttributes() {
        return this.otherAttributes;
    }
}