package org.w3._2005._08.addressing;

import javax.xml.bind.annotation.*;
import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "AttributedAnyType",
        propOrder = {"any"}
)
public class AttributedAnyType {
    @XmlAnyElement(
            lax = true
    )
    protected Object any;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap();

    public AttributedAnyType() {
    }

    public Object getAny() {
        return this.any;
    }

    public void setAny(Object value) {
        this.any = value;
    }

    public Map<QName, String> getOtherAttributes() {
        return this.otherAttributes;
    }
}
