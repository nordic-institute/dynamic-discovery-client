package org.w3._2005._08.addressing;

import javax.xml.bind.annotation.*;
import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "AttributedQNameType",
        propOrder = {"value"}
)
public class AttributedQNameType {
    @XmlValue
    protected QName value;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap();

    public AttributedQNameType() {
    }

    public QName getValue() {
        return this.value;
    }

    public void setValue(QName value) {
        this.value = value;
    }

    public Map<QName, String> getOtherAttributes() {
        return this.otherAttributes;
    }
}

