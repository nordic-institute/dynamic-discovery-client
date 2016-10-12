package org.w3._2000._09.xmldsig_;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "PGPDataType",
        propOrder = {"content"}
)
public class PGPDataType {
    @XmlElementRefs({@XmlElementRef(
            name = "PGPKeyID",
            namespace = "http://www.w3.org/2000/09/xmldsig#",
            type = JAXBElement.class
    ), @XmlElementRef(
            name = "PGPKeyPacket",
            namespace = "http://www.w3.org/2000/09/xmldsig#",
            type = JAXBElement.class
    )})
    @XmlAnyElement(
            lax = true
    )
    protected List<Object> content;

    public PGPDataType() {
    }

    public List<Object> getContent() {
        if(this.content == null) {
            this.content = new ArrayList();
        }

        return this.content;
    }
}
