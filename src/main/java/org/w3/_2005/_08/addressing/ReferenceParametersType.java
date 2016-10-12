package org.w3._2005._08.addressing;

import javax.xml.bind.annotation.*;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

