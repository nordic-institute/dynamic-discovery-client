package org.w3._2000._09.xmldsig_;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "DigestMethodType",
        propOrder = {"content"}
)
public class DigestMethodType {
    @XmlMixed
    @XmlAnyElement(
            lax = true
    )
    protected List<Object> content;
    @XmlAttribute(
            name = "Algorithm",
            required = true
    )
    @XmlSchemaType(
            name = "anyURI"
    )
    protected String algorithm;

    public DigestMethodType() {
    }

    public List<Object> getContent() {
        if (this.content == null) {
            this.content = new ArrayList();
        }

        return this.content;
    }

    public String getAlgorithm() {
        return this.algorithm;
    }

    public void setAlgorithm(String value) {
        this.algorithm = value;
    }
}
