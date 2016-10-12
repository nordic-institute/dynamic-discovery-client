package org.w3._2005._08.addressing;

import javax.xml.bind.annotation.*;
import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "RelatesToType",
        propOrder = {"value"}
)
public class RelatesToType {
    @XmlValue
    @XmlSchemaType(
            name = "anyURI"
    )
    protected String value;
    @XmlAttribute(
            name = "RelationshipType"
    )
    protected String relationshipType;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap();

    public RelatesToType() {
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRelationshipType() {
        return this.relationshipType == null?"http://www.w3.org/2005/08/addressing/reply":this.relationshipType;
    }

    public void setRelationshipType(String value) {
        this.relationshipType = value;
    }

    public Map<QName, String> getOtherAttributes() {
        return this.otherAttributes;
    }
}
