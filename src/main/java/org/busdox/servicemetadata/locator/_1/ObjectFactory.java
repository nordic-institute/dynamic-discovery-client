package org.busdox.servicemetadata.locator._1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.busdox.servicemetadata.locator._1 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ServiceMetadataPublisherID_QNAME = new QName("http://busdox.org/serviceMetadata/locator/1.0/", "ServiceMetadataPublisherID");
    private final static QName _CreateServiceMetadataPublisherService_QNAME = new QName("http://busdox.org/serviceMetadata/locator/1.0/", "CreateServiceMetadataPublisherService");
    private final static QName _ReadServiceMetadataPublisherService_QNAME = new QName("http://busdox.org/serviceMetadata/locator/1.0/", "ReadServiceMetadataPublisherService");
    private final static QName _UpdateServiceMetadataPublisherService_QNAME = new QName("http://busdox.org/serviceMetadata/locator/1.0/", "UpdateServiceMetadataPublisherService");
    private final static QName _ServiceMetadataPublisherService_QNAME = new QName("http://busdox.org/serviceMetadata/locator/1.0/", "ServiceMetadataPublisherService");
    private final static QName _CreateParticipantIdentifier_QNAME = new QName("http://busdox.org/serviceMetadata/locator/1.0/", "CreateParticipantIdentifier");
    private final static QName _DeleteParticipantIdentifier_QNAME = new QName("http://busdox.org/serviceMetadata/locator/1.0/", "DeleteParticipantIdentifier");
    private final static QName _ParticipantIdentifierPage_QNAME = new QName("http://busdox.org/serviceMetadata/locator/1.0/", "ParticipantIdentifierPage");
    private final static QName _CreateList_QNAME = new QName("http://busdox.org/serviceMetadata/locator/1.0/", "CreateList");
    private final static QName _DeleteList_QNAME = new QName("http://busdox.org/serviceMetadata/locator/1.0/", "DeleteList");
    private final static QName _PageRequest_QNAME = new QName("http://busdox.org/serviceMetadata/locator/1.0/", "PageRequest");
    private final static QName _PrepareMigrationRecord_QNAME = new QName("http://busdox.org/serviceMetadata/locator/1.0/", "PrepareMigrationRecord");
    private final static QName _CompleteMigrationRecord_QNAME = new QName("http://busdox.org/serviceMetadata/locator/1.0/", "CompleteMigrationRecord");
    private final static QName _BadRequestFault_QNAME = new QName("http://busdox.org/serviceMetadata/locator/1.0/", "BadRequestFault");
    private final static QName _InternalErrorFault_QNAME = new QName("http://busdox.org/serviceMetadata/locator/1.0/", "InternalErrorFault");
    private final static QName _NotFoundFault_QNAME = new QName("http://busdox.org/serviceMetadata/locator/1.0/", "NotFoundFault");
    private final static QName _UnauthorizedFault_QNAME = new QName("http://busdox.org/serviceMetadata/locator/1.0/", "UnauthorizedFault");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.busdox.servicemetadata.locator._1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ServiceMetadataPublisherServiceType }
     * 
     */
    public ServiceMetadataPublisherServiceType createServiceMetadataPublisherServiceType() {
        return new ServiceMetadataPublisherServiceType();
    }

    /**
     * Create an instance of {@link ServiceMetadataPublisherServiceForParticipantType }
     * 
     */
    public ServiceMetadataPublisherServiceForParticipantType createServiceMetadataPublisherServiceForParticipantType() {
        return new ServiceMetadataPublisherServiceForParticipantType();
    }

    /**
     * Create an instance of {@link ParticipantIdentifierPageType }
     * 
     */
    public ParticipantIdentifierPageType createParticipantIdentifierPageType() {
        return new ParticipantIdentifierPageType();
    }

    /**
     * Create an instance of {@link PageRequestType }
     * 
     */
    public PageRequestType createPageRequestType() {
        return new PageRequestType();
    }

    /**
     * Create an instance of {@link MigrationRecordType }
     * 
     */
    public MigrationRecordType createMigrationRecordType() {
        return new MigrationRecordType();
    }

    /**
     * Create an instance of {@link FaultType }
     * 
     */
    public FaultType createFaultType() {
        return new FaultType();
    }

    /**
     * Create an instance of {@link PublisherEndpointType }
     * 
     */
    public PublisherEndpointType createPublisherEndpointType() {
        return new PublisherEndpointType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://busdox.org/serviceMetadata/locator/1.0/", name = "ServiceMetadataPublisherID")
    public JAXBElement<String> createServiceMetadataPublisherID(String value) {
        return new JAXBElement<String>(_ServiceMetadataPublisherID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceMetadataPublisherServiceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://busdox.org/serviceMetadata/locator/1.0/", name = "CreateServiceMetadataPublisherService")
    public JAXBElement<ServiceMetadataPublisherServiceType> createCreateServiceMetadataPublisherService(ServiceMetadataPublisherServiceType value) {
        return new JAXBElement<ServiceMetadataPublisherServiceType>(_CreateServiceMetadataPublisherService_QNAME, ServiceMetadataPublisherServiceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceMetadataPublisherServiceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://busdox.org/serviceMetadata/locator/1.0/", name = "ReadServiceMetadataPublisherService")
    public JAXBElement<ServiceMetadataPublisherServiceType> createReadServiceMetadataPublisherService(ServiceMetadataPublisherServiceType value) {
        return new JAXBElement<ServiceMetadataPublisherServiceType>(_ReadServiceMetadataPublisherService_QNAME, ServiceMetadataPublisherServiceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceMetadataPublisherServiceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://busdox.org/serviceMetadata/locator/1.0/", name = "UpdateServiceMetadataPublisherService")
    public JAXBElement<ServiceMetadataPublisherServiceType> createUpdateServiceMetadataPublisherService(ServiceMetadataPublisherServiceType value) {
        return new JAXBElement<ServiceMetadataPublisherServiceType>(_UpdateServiceMetadataPublisherService_QNAME, ServiceMetadataPublisherServiceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceMetadataPublisherServiceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://busdox.org/serviceMetadata/locator/1.0/", name = "ServiceMetadataPublisherService")
    public JAXBElement<ServiceMetadataPublisherServiceType> createServiceMetadataPublisherService(ServiceMetadataPublisherServiceType value) {
        return new JAXBElement<ServiceMetadataPublisherServiceType>(_ServiceMetadataPublisherService_QNAME, ServiceMetadataPublisherServiceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceMetadataPublisherServiceForParticipantType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://busdox.org/serviceMetadata/locator/1.0/", name = "CreateParticipantIdentifier")
    public JAXBElement<ServiceMetadataPublisherServiceForParticipantType> createCreateParticipantIdentifier(ServiceMetadataPublisherServiceForParticipantType value) {
        return new JAXBElement<ServiceMetadataPublisherServiceForParticipantType>(_CreateParticipantIdentifier_QNAME, ServiceMetadataPublisherServiceForParticipantType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceMetadataPublisherServiceForParticipantType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://busdox.org/serviceMetadata/locator/1.0/", name = "DeleteParticipantIdentifier")
    public JAXBElement<ServiceMetadataPublisherServiceForParticipantType> createDeleteParticipantIdentifier(ServiceMetadataPublisherServiceForParticipantType value) {
        return new JAXBElement<ServiceMetadataPublisherServiceForParticipantType>(_DeleteParticipantIdentifier_QNAME, ServiceMetadataPublisherServiceForParticipantType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParticipantIdentifierPageType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://busdox.org/serviceMetadata/locator/1.0/", name = "ParticipantIdentifierPage")
    public JAXBElement<ParticipantIdentifierPageType> createParticipantIdentifierPage(ParticipantIdentifierPageType value) {
        return new JAXBElement<ParticipantIdentifierPageType>(_ParticipantIdentifierPage_QNAME, ParticipantIdentifierPageType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParticipantIdentifierPageType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://busdox.org/serviceMetadata/locator/1.0/", name = "CreateList")
    public JAXBElement<ParticipantIdentifierPageType> createCreateList(ParticipantIdentifierPageType value) {
        return new JAXBElement<ParticipantIdentifierPageType>(_CreateList_QNAME, ParticipantIdentifierPageType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParticipantIdentifierPageType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://busdox.org/serviceMetadata/locator/1.0/", name = "DeleteList")
    public JAXBElement<ParticipantIdentifierPageType> createDeleteList(ParticipantIdentifierPageType value) {
        return new JAXBElement<ParticipantIdentifierPageType>(_DeleteList_QNAME, ParticipantIdentifierPageType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PageRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://busdox.org/serviceMetadata/locator/1.0/", name = "PageRequest")
    public JAXBElement<PageRequestType> createPageRequest(PageRequestType value) {
        return new JAXBElement<PageRequestType>(_PageRequest_QNAME, PageRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MigrationRecordType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://busdox.org/serviceMetadata/locator/1.0/", name = "PrepareMigrationRecord")
    public JAXBElement<MigrationRecordType> createPrepareMigrationRecord(MigrationRecordType value) {
        return new JAXBElement<MigrationRecordType>(_PrepareMigrationRecord_QNAME, MigrationRecordType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MigrationRecordType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://busdox.org/serviceMetadata/locator/1.0/", name = "CompleteMigrationRecord")
    public JAXBElement<MigrationRecordType> createCompleteMigrationRecord(MigrationRecordType value) {
        return new JAXBElement<MigrationRecordType>(_CompleteMigrationRecord_QNAME, MigrationRecordType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FaultType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://busdox.org/serviceMetadata/locator/1.0/", name = "BadRequestFault")
    public JAXBElement<FaultType> createBadRequestFault(FaultType value) {
        return new JAXBElement<FaultType>(_BadRequestFault_QNAME, FaultType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FaultType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://busdox.org/serviceMetadata/locator/1.0/", name = "InternalErrorFault")
    public JAXBElement<FaultType> createInternalErrorFault(FaultType value) {
        return new JAXBElement<FaultType>(_InternalErrorFault_QNAME, FaultType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FaultType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://busdox.org/serviceMetadata/locator/1.0/", name = "NotFoundFault")
    public JAXBElement<FaultType> createNotFoundFault(FaultType value) {
        return new JAXBElement<FaultType>(_NotFoundFault_QNAME, FaultType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FaultType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://busdox.org/serviceMetadata/locator/1.0/", name = "UnauthorizedFault")
    public JAXBElement<FaultType> createUnauthorizedFault(FaultType value) {
        return new JAXBElement<FaultType>(_UnauthorizedFault_QNAME, FaultType.class, null, value);
    }

}
