package org.w3._2005._08.addressing;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import org.w3._2005._08.addressing.AttributedURIType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "ProblemActionType",
        propOrder = {"action", "soapAction"}
)
public class ProblemActionType {
    @XmlElement(
            name = "Action"
    )
    protected AttributedURIType action;
    @XmlElement(
            name = "SoapAction"
    )
    @XmlSchemaType(
            name = "anyURI"
    )
    protected String soapAction;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap();

    public ProblemActionType() {
    }

    public AttributedURIType getAction() {
        return this.action;
    }

    public void setAction(AttributedURIType value) {
        this.action = value;
    }

    public String getSoapAction() {
        return this.soapAction;
    }

    public void setSoapAction(String value) {
        this.soapAction = value;
    }

    public Map<QName, String> getOtherAttributes() {
        return this.otherAttributes;
    }
}
