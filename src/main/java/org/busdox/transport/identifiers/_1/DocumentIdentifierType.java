package org.busdox.transport.identifiers._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

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
