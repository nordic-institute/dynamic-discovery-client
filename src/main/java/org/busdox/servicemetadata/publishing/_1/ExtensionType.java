package org.busdox.servicemetadata.publishing._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "ExtensionType",
        propOrder = {"any"}
)
public class ExtensionType {
    @XmlAnyElement(
            lax = true
    )
    protected Object any;

    public ExtensionType() {
    }

    public Object getAny() {
        return this.any;
    }

    public void setAny(Object value) {
        this.any = value;
    }
}
