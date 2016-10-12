package org.oasis_open.docs.bdxr.ns.smp._2014._07;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "DocumentIdentifierType",
        propOrder = {"value"}
)
public class DocumentIdentifierType {
    @XmlValue
    protected String value;
    @XmlAttribute(
            name = "scheme"
    )
    protected String scheme;

    public DocumentIdentifierType() {
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getScheme() {
        return this.scheme;
    }

    public void setScheme(String value) {
        this.scheme = value;
    }
}
