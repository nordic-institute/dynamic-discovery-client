package org.busdox.transport.identifiers._1;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "ParticipantIdentifierType",
        propOrder = {"value"}
)
public class ParticipantIdentifierType {
    @XmlValue
    protected String value;
    @XmlAttribute(
            name = "scheme"
    )
    protected String scheme;

    public ParticipantIdentifierType() {
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
