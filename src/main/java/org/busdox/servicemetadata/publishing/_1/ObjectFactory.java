package org.busdox.servicemetadata.publishing._1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {
    private static final QName _SignedServiceMetadata_QNAME = new QName("http://busdox.org/serviceMetadata/publishing/1.0/", "SignedServiceMetadata");
    private static final QName _ServiceMetadata_QNAME = new QName("http://busdox.org/serviceMetadata/publishing/1.0/", "ServiceMetadata");
    private static final QName _ServiceGroup_QNAME = new QName("http://busdox.org/serviceMetadata/publishing/1.0/", "ServiceGroup");

    public ObjectFactory() {
    }

    public ServiceGroupType createServiceGroupType() {
        return new ServiceGroupType();
    }

    public SignedServiceMetadataType createSignedServiceMetadataType() {
        return new SignedServiceMetadataType();
    }

    public ServiceMetadataType createServiceMetadataType() {
        return new ServiceMetadataType();
    }

    public ProcessType createProcessType() {
        return new ProcessType();
    }

    public ServiceMetadataReferenceCollectionType createServiceMetadataReferenceCollectionType() {
        return new ServiceMetadataReferenceCollectionType();
    }

    public ServiceMetadataReferenceType createServiceMetadataReferenceType() {
        return new ServiceMetadataReferenceType();
    }

    public ExtensionType createExtensionType() {
        return new ExtensionType();
    }

    public EndpointType createEndpointType() {
        return new EndpointType();
    }

    public ProcessListType createProcessListType() {
        return new ProcessListType();
    }

    public ServiceInformationType createServiceInformationType() {
        return new ServiceInformationType();
    }

    public ServiceEndpointList createServiceEndpointList() {
        return new ServiceEndpointList();
    }

    public RedirectType createRedirectType() {
        return new RedirectType();
    }

    @XmlElementDecl(
            namespace = "http://busdox.org/serviceMetadata/publishing/1.0/",
            name = "SignedServiceMetadata"
    )
    public JAXBElement<SignedServiceMetadataType> createSignedServiceMetadata(SignedServiceMetadataType value) {
        return new JAXBElement(_SignedServiceMetadata_QNAME, SignedServiceMetadataType.class, (Class)null, value);
    }

    @XmlElementDecl(
            namespace = "http://busdox.org/serviceMetadata/publishing/1.0/",
            name = "ServiceMetadata"
    )
    public JAXBElement<ServiceMetadataType> createServiceMetadata(ServiceMetadataType value) {
        return new JAXBElement(_ServiceMetadata_QNAME, ServiceMetadataType.class, (Class)null, value);
    }

    @XmlElementDecl(
            namespace = "http://busdox.org/serviceMetadata/publishing/1.0/",
            name = "ServiceGroup"
    )
    public JAXBElement<ServiceGroupType> createServiceGroup(ServiceGroupType value) {
        return new JAXBElement(_ServiceGroup_QNAME, ServiceGroupType.class, (Class)null, value);
    }
}
