package org.w3._2000._09.xmldsig_;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "KeyInfoType",
        propOrder = {"content"}
)
public class KeyInfoType {
    @XmlElementRefs({@XmlElementRef(
            name = "SPKIData",
            namespace = "http://www.w3.org/2000/09/xmldsig#",
            type = JAXBElement.class
    ), @XmlElementRef(
            name = "RetrievalMethod",
            namespace = "http://www.w3.org/2000/09/xmldsig#",
            type = JAXBElement.class
    ), @XmlElementRef(
            name = "X509Data",
            namespace = "http://www.w3.org/2000/09/xmldsig#",
            type = JAXBElement.class
    ), @XmlElementRef(
            name = "PGPData",
            namespace = "http://www.w3.org/2000/09/xmldsig#",
            type = JAXBElement.class
    ), @XmlElementRef(
            name = "MgmtData",
            namespace = "http://www.w3.org/2000/09/xmldsig#",
            type = JAXBElement.class
    ), @XmlElementRef(
            name = "KeyName",
            namespace = "http://www.w3.org/2000/09/xmldsig#",
            type = JAXBElement.class
    ), @XmlElementRef(
            name = "KeyValue",
            namespace = "http://www.w3.org/2000/09/xmldsig#",
            type = JAXBElement.class
    )})
    @XmlMixed
    @XmlAnyElement(
            lax = true
    )
    protected List<Object> content;
    @XmlAttribute(
            name = "Id"
    )
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(
            name = "ID"
    )
    protected String id;

    public KeyInfoType() {
    }

    public List<Object> getContent() {
        if (this.content == null) {
            this.content = new ArrayList();
        }

        return this.content;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String value) {
        this.id = value;
    }
}
