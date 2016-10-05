package org.busdox.servicemetadata.publishing._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "ServiceMetadataReferenceType"
)
public class ServiceMetadataReferenceType {
    @XmlAttribute(
            name = "href"
    )
    @XmlSchemaType(
            name = "anyURI"
    )
    protected String href;

    public ServiceMetadataReferenceType() {
    }

    public String getHref() {
        return this.href;
    }

    public void setHref(String value) {
        this.href = value;
    }
}
