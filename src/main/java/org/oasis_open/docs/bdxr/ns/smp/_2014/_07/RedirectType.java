package org.oasis_open.docs.bdxr.ns.smp._2014._07;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import org.oasis_open.docs.bdxr.ns.smp._2014._07.ExtensionType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "RedirectType",
        propOrder = {"certificateUID", "extension"}
)
public class RedirectType {
    @XmlElement(
            name = "CertificateUID",
            required = true
    )
    protected String certificateUID;
    @XmlElement(
            name = "Extension"
    )
    protected ExtensionType extension;
    @XmlAttribute(
            name = "href",
            required = true
    )
    @XmlSchemaType(
            name = "anyURI"
    )
    protected String href;

    public RedirectType() {
    }

    public String getCertificateUID() {
        return this.certificateUID;
    }

    public void setCertificateUID(String value) {
        this.certificateUID = value;
    }

    public ExtensionType getExtension() {
        return this.extension;
    }

    public void setExtension(ExtensionType value) {
        this.extension = value;
    }

    public String getHref() {
        return this.href;
    }

    public void setHref(String value) {
        this.href = value;
    }
}
