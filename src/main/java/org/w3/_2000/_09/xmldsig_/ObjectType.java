package org.w3._2000._09.xmldsig_;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "ObjectType",
        propOrder = {"content"}
)
public class ObjectType {
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
    @XmlAttribute(
            name = "MimeType"
    )
    protected String mimeType;
    @XmlAttribute(
            name = "Encoding"
    )
    @XmlSchemaType(
            name = "anyURI"
    )
    protected String encoding;

    public ObjectType() {
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

    public String getMimeType() {
        return this.mimeType;
    }

    public void setMimeType(String value) {
        this.mimeType = value;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setEncoding(String value) {
        this.encoding = value;
    }
}
