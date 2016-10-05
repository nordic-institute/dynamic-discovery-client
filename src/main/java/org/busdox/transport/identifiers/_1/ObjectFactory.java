package org.busdox.transport.identifiers._1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import org.busdox.transport.identifiers._1.DocumentIdentifierType;
import org.busdox.transport.identifiers._1.ParticipantIdentifierType;
import org.busdox.transport.identifiers._1.ProcessIdentifierType;

@XmlRegistry
public class ObjectFactory {
    private static final QName _DocumentIdentifier_QNAME = new QName("http://busdox.org/transport/identifiers/1.0/", "DocumentIdentifier");
    private static final QName _ChannelIdentifier_QNAME = new QName("http://busdox.org/transport/identifiers/1.0/", "ChannelIdentifier");
    private static final QName _SenderIdentifier_QNAME = new QName("http://busdox.org/transport/identifiers/1.0/", "SenderIdentifier");
    private static final QName _MessageIdentifier_QNAME = new QName("http://busdox.org/transport/identifiers/1.0/", "MessageIdentifier");
    private static final QName _ProcessIdentifier_QNAME = new QName("http://busdox.org/transport/identifiers/1.0/", "ProcessIdentifier");
    private static final QName _RecipientIdentifier_QNAME = new QName("http://busdox.org/transport/identifiers/1.0/", "RecipientIdentifier");
    private static final QName _ParticipantIdentifier_QNAME = new QName("http://busdox.org/transport/identifiers/1.0/", "ParticipantIdentifier");

    public ObjectFactory() {
    }

    public ParticipantIdentifierType createParticipantIdentifierType() {
        return new ParticipantIdentifierType();
    }

    public DocumentIdentifierType createDocumentIdentifierType() {
        return new DocumentIdentifierType();
    }

    public ProcessIdentifierType createProcessIdentifierType() {
        return new ProcessIdentifierType();
    }

    @XmlElementDecl(
            namespace = "http://busdox.org/transport/identifiers/1.0/",
            name = "DocumentIdentifier"
    )
    public JAXBElement<DocumentIdentifierType> createDocumentIdentifier(DocumentIdentifierType value) {
        return new JAXBElement(_DocumentIdentifier_QNAME, DocumentIdentifierType.class, (Class) null, value);
    }

    @XmlElementDecl(
            namespace = "http://busdox.org/transport/identifiers/1.0/",
            name = "ChannelIdentifier"
    )
    public JAXBElement<String> createChannelIdentifier(String value) {
        return new JAXBElement(_ChannelIdentifier_QNAME, String.class, (Class) null, value);
    }

    @XmlElementDecl(
            namespace = "http://busdox.org/transport/identifiers/1.0/",
            name = "SenderIdentifier"
    )
    public JAXBElement<ParticipantIdentifierType> createSenderIdentifier(ParticipantIdentifierType value) {
        return new JAXBElement(_SenderIdentifier_QNAME, ParticipantIdentifierType.class, (Class) null, value);
    }

    @XmlElementDecl(
            namespace = "http://busdox.org/transport/identifiers/1.0/",
            name = "MessageIdentifier"
    )
    public JAXBElement<String> createMessageIdentifier(String value) {
        return new JAXBElement(_MessageIdentifier_QNAME, String.class, (Class) null, value);
    }

    @XmlElementDecl(
            namespace = "http://busdox.org/transport/identifiers/1.0/",
            name = "ProcessIdentifier"
    )
    public JAXBElement<ProcessIdentifierType> createProcessIdentifier(ProcessIdentifierType value) {
        return new JAXBElement(_ProcessIdentifier_QNAME, ProcessIdentifierType.class, (Class) null, value);
    }

    @XmlElementDecl(
            namespace = "http://busdox.org/transport/identifiers/1.0/",
            name = "RecipientIdentifier"
    )
    public JAXBElement<ParticipantIdentifierType> createRecipientIdentifier(ParticipantIdentifierType value) {
        return new JAXBElement(_RecipientIdentifier_QNAME, ParticipantIdentifierType.class, (Class) null, value);
    }

    @XmlElementDecl(
            namespace = "http://busdox.org/transport/identifiers/1.0/",
            name = "ParticipantIdentifier"
    )
    public JAXBElement<ParticipantIdentifierType> createParticipantIdentifier(ParticipantIdentifierType value) {
        return new JAXBElement(_ParticipantIdentifier_QNAME, ParticipantIdentifierType.class, (Class) null, value);
    }
}

