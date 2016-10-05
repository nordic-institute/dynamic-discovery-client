package org.w3._2005._08.addressing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "ReferenceParametersType",
        propOrder = {"any"}
)
public class ReferenceParametersType {
    @XmlAnyElement(
            lax = true
    )
    protected List<Object> any;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap();

    public ReferenceParametersType() {
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

