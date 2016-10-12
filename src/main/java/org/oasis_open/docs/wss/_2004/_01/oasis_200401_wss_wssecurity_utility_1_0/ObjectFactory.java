package org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_utility_1_0;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {
    private static final QName _Expires_QNAME = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "Expires");
    private static final QName _Created_QNAME = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "Created");
    private static final QName _Timestamp_QNAME = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "Timestamp");

    public ObjectFactory() {
    }

    public AttributedDateTime createAttributedDateTime() {
        return new AttributedDateTime();
    }

    public TimestampType createTimestampType() {
        return new TimestampType();
    }

    public AttributedURI createAttributedURI() {
        return new AttributedURI();
    }

    @XmlElementDecl(
            namespace = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd",
            name = "Expires"
    )
    public JAXBElement<AttributedDateTime> createExpires(AttributedDateTime value) {
        return new JAXBElement(_Expires_QNAME, AttributedDateTime.class, (Class) null, value);
    }

    @XmlElementDecl(
            namespace = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd",
            name = "Created"
    )
    public JAXBElement<AttributedDateTime> createCreated(AttributedDateTime value) {
        return new JAXBElement(_Created_QNAME, AttributedDateTime.class, (Class) null, value);
    }

    @XmlElementDecl(
            namespace = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd",
            name = "Timestamp"
    )
    public JAXBElement<TimestampType> createTimestamp(TimestampType value) {
        return new JAXBElement(_Timestamp_QNAME, TimestampType.class, (Class) null, value);
    }
}
