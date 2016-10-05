package org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_utility_1_0;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "AttributedURI",
        propOrder = {"value"}
)
public class AttributedURI {
    @XmlValue
    @XmlSchemaType(
            name = "anyURI"
    )
    protected String value;
    @XmlAttribute(
            name = "Id",
            namespace = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"
    )
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(
            name = "ID"
    )
    protected String id;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap();

    public AttributedURI() {
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String value) {
        this.id = value;
    }

    public Map<QName, String> getOtherAttributes() {
        return this.otherAttributes;
    }
}
