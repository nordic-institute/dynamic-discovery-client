package org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_utility_1_0;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "TimestampType",
        propOrder = {"created", "expires", "any"}
)
public class TimestampType {
    @XmlElement(
            name = "Created"
    )
    protected AttributedDateTime created;
    @XmlElement(
            name = "Expires"
    )
    protected AttributedDateTime expires;
    @XmlAnyElement(
            lax = true
    )
    protected List<Object> any;
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

    public TimestampType() {
    }

    public AttributedDateTime getCreated() {
        return this.created;
    }

    public void setCreated(AttributedDateTime value) {
        this.created = value;
    }

    public AttributedDateTime getExpires() {
        return this.expires;
    }

    public void setExpires(AttributedDateTime value) {
        this.expires = value;
    }

    public List<Object> getAny() {
        if (this.any == null) {
            this.any = new ArrayList();
        }

        return this.any;
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
