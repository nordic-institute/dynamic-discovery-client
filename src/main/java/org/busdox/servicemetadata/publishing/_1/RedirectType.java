package org.busdox.servicemetadata.publishing._1;

import javax.xml.bind.annotation.*;

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
            name = "href"
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
