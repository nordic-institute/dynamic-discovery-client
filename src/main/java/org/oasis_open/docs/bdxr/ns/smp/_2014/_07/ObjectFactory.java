package org.oasis_open.docs.bdxr.ns.smp._2014._07;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {
    private static final QName _ParticipantIdentifier_QNAME = new QName("http://docs.oasis-open.org/bdxr/ns/SMP/2014/07", "ParticipantIdentifier");
    private static final QName _SignedServiceMetadata_QNAME = new QName("http://docs.oasis-open.org/bdxr/ns/SMP/2014/07", "SignedServiceMetadata");
    private static final QName _RecipientIdentifier_QNAME = new QName("http://docs.oasis-open.org/bdxr/ns/SMP/2014/07", "RecipientIdentifier");
    private static final QName _ServiceMetadata_QNAME = new QName("http://docs.oasis-open.org/bdxr/ns/SMP/2014/07", "ServiceMetadata");
    private static final QName _ServiceGroup_QNAME = new QName("http://docs.oasis-open.org/bdxr/ns/SMP/2014/07", "ServiceGroup");
    private static final QName _ProcessIdentifier_QNAME = new QName("http://docs.oasis-open.org/bdxr/ns/SMP/2014/07", "ProcessIdentifier");
    private static final QName _DocumentIdentifier_QNAME = new QName("http://docs.oasis-open.org/bdxr/ns/SMP/2014/07", "DocumentIdentifier");
    private static final QName _SenderIdentifier_QNAME = new QName("http://docs.oasis-open.org/bdxr/ns/SMP/2014/07", "SenderIdentifier");

    public ObjectFactory() {
    }

    public SignedServiceMetadataType createSignedServiceMetadataType() {
        return new SignedServiceMetadataType();
    }

    public ServiceMetadataType createServiceMetadataType() {
        return new ServiceMetadataType();
    }

    public ParticipantIdentifierType createParticipantIdentifierType() {
        return new ParticipantIdentifierType();
    }

    public DocumentIdentifierType createDocumentIdentifierType() {
        return new DocumentIdentifierType();
    }

    public ServiceGroupType createServiceGroupType() {
        return new ServiceGroupType();
    }

    public ProcessIdentifierType createProcessIdentifierType() {
        return new ProcessIdentifierType();
    }

    public ProcessType createProcessType() {
        return new ProcessType();
    }

    public ServiceMetadataReferenceCollectionType createServiceMetadataReferenceCollectionType() {
        return new ServiceMetadataReferenceCollectionType();
    }

    public RedirectType createRedirectType() {
        return new RedirectType();
    }

    public EndpointType createEndpointType() {
        return new EndpointType();
    }

    public ExtensionType createExtensionType() {
        return new ExtensionType();
    }

    public ServiceInformationType createServiceInformationType() {
        return new ServiceInformationType();
    }

    public ServiceMetadataReferenceType createServiceMetadataReferenceType() {
        return new ServiceMetadataReferenceType();
    }

    public ProcessListType createProcessListType() {
        return new ProcessListType();
    }

    public ServiceEndpointList createServiceEndpointList() {
        return new ServiceEndpointList();
    }

    @XmlElementDecl(
            namespace = "http://docs.oasis-open.org/bdxr/ns/SMP/2014/07",
            name = "ParticipantIdentifier"
    )
    public JAXBElement<ParticipantIdentifierType> createParticipantIdentifier(ParticipantIdentifierType value) {
        return new JAXBElement(_ParticipantIdentifier_QNAME, ParticipantIdentifierType.class, (Class) null, value);
    }

    @XmlElementDecl(
            namespace = "http://docs.oasis-open.org/bdxr/ns/SMP/2014/07",
            name = "SignedServiceMetadata"
    )
    public JAXBElement<SignedServiceMetadataType> createSignedServiceMetadata(SignedServiceMetadataType value) {
        return new JAXBElement(_SignedServiceMetadata_QNAME, SignedServiceMetadataType.class, (Class) null, value);
    }

    @XmlElementDecl(
            namespace = "http://docs.oasis-open.org/bdxr/ns/SMP/2014/07",
            name = "RecipientIdentifier"
    )
    public JAXBElement<ParticipantIdentifierType> createRecipientIdentifier(ParticipantIdentifierType value) {
        return new JAXBElement(_RecipientIdentifier_QNAME, ParticipantIdentifierType.class, (Class) null, value);
    }

    @XmlElementDecl(
            namespace = "http://docs.oasis-open.org/bdxr/ns/SMP/2014/07",
            name = "ServiceMetadata"
    )
    public JAXBElement<ServiceMetadataType> createServiceMetadata(ServiceMetadataType value) {
        return new JAXBElement(_ServiceMetadata_QNAME, ServiceMetadataType.class, (Class) null, value);
    }

    @XmlElementDecl(
            namespace = "http://docs.oasis-open.org/bdxr/ns/SMP/2014/07",
            name = "ServiceGroup"
    )
    public JAXBElement<ServiceGroupType> createServiceGroup(ServiceGroupType value) {
        return new JAXBElement(_ServiceGroup_QNAME, ServiceGroupType.class, (Class) null, value);
    }

    @XmlElementDecl(
            namespace = "http://docs.oasis-open.org/bdxr/ns/SMP/2014/07",
            name = "ProcessIdentifier"
    )
    public JAXBElement<ProcessIdentifierType> createProcessIdentifier(ProcessIdentifierType value) {
        return new JAXBElement(_ProcessIdentifier_QNAME, ProcessIdentifierType.class, (Class) null, value);
    }

    @XmlElementDecl(
            namespace = "http://docs.oasis-open.org/bdxr/ns/SMP/2014/07",
            name = "DocumentIdentifier"
    )
    public JAXBElement<DocumentIdentifierType> createDocumentIdentifier(DocumentIdentifierType value) {
        return new JAXBElement(_DocumentIdentifier_QNAME, DocumentIdentifierType.class, (Class) null, value);
    }

    @XmlElementDecl(
            namespace = "http://docs.oasis-open.org/bdxr/ns/SMP/2014/07",
            name = "SenderIdentifier"
    )
    public JAXBElement<ParticipantIdentifierType> createSenderIdentifier(ParticipantIdentifierType value) {
        return new JAXBElement(_SenderIdentifier_QNAME, ParticipantIdentifierType.class, (Class) null, value);
    }
}


