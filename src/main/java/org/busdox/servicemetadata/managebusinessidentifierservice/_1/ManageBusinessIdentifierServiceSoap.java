package org.busdox.servicemetadata.managebusinessidentifierservice._1;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import org.busdox.servicemetadata.locator._1.MigrationRecordType;
import org.busdox.servicemetadata.locator._1.ObjectFactory;
import org.busdox.servicemetadata.locator._1.PageRequestType;
import org.busdox.servicemetadata.locator._1.ParticipantIdentifierPageType;
import org.busdox.servicemetadata.locator._1.ServiceMetadataPublisherServiceForParticipantType;
import org.busdox.servicemetadata.managebusinessidentifierservice._1.BadRequestFault;
import org.busdox.servicemetadata.managebusinessidentifierservice._1.InternalErrorFault;
import org.busdox.servicemetadata.managebusinessidentifierservice._1.NotFoundFault;
import org.busdox.servicemetadata.managebusinessidentifierservice._1.UnauthorizedFault;

@WebService(name="ManageBusinessIdentifierServiceSoap", targetNamespace="http://busdox.org/serviceMetadata/ManageBusinessIdentifierService/1.0/")
@SOAPBinding(parameterStyle=SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso(value={ObjectFactory.class, org.busdox.transport.identifiers._1.ObjectFactory.class})
public interface ManageBusinessIdentifierServiceSoap {
    @WebMethod(operationName="Create", action="http://busdox.org/serviceMetadata/ManageBusinessIdentifierService/1.0/         :createIn")
    public void create(@WebParam(name = "CreateParticipantIdentifier", targetNamespace = "http://busdox.org/serviceMetadata/locator/1.0/", partName = "messagePart") ServiceMetadataPublisherServiceForParticipantType var1) throws BadRequestFault, InternalErrorFault, NotFoundFault, UnauthorizedFault;

    @WebMethod(operationName="CreateList", action="http://busdox.org/serviceMetadata/ManageBusinessIdentifierService/1.0/         :createListIn")
    public void createList(@WebParam(name = "CreateList", targetNamespace = "http://busdox.org/serviceMetadata/locator/1.0/", partName = "createListIn") ParticipantIdentifierPageType var1) throws BadRequestFault, InternalErrorFault, NotFoundFault, UnauthorizedFault;

    @WebMethod(operationName="Delete", action="http://busdox.org/serviceMetadata/ManageBusinessIdentifierService/1.0/         :deleteIn")
    public void delete(@WebParam(name = "DeleteParticipantIdentifier", targetNamespace = "http://busdox.org/serviceMetadata/locator/1.0/", partName = "messagePart") ServiceMetadataPublisherServiceForParticipantType var1) throws BadRequestFault, InternalErrorFault, NotFoundFault, UnauthorizedFault;

    @WebMethod(operationName="DeleteList", action="http://busdox.org/serviceMetadata/ManageBusinessIdentifierService/1.0/         :deleteListIn")
    public void deleteList(@WebParam(name = "DeleteList", targetNamespace = "http://busdox.org/serviceMetadata/locator/1.0/", partName = "deleteListIn") ParticipantIdentifierPageType var1) throws BadRequestFault, InternalErrorFault, NotFoundFault, UnauthorizedFault;

    @WebMethod(operationName="List", action="http://busdox.org/serviceMetadata/ManageBusinessIdentifierService/1.0/         :listIn")
    @WebResult(name="ParticipantIdentifierPage", targetNamespace="http://busdox.org/serviceMetadata/locator/1.0/", partName="messagePart")
    public ParticipantIdentifierPageType list(@WebParam(name = "PageRequest", targetNamespace = "http://busdox.org/serviceMetadata/locator/1.0/", partName = "messagePart") PageRequestType var1) throws BadRequestFault, InternalErrorFault, NotFoundFault, UnauthorizedFault;

    @WebMethod(operationName="PrepareToMigrate", action="http://busdox.org/serviceMetadata/ManageBusinessIdentifierService/1.0/         :prepareMigrateIn")
    public void prepareToMigrate(@WebParam(name = "PrepareMigrationRecord", targetNamespace = "http://busdox.org/serviceMetadata/locator/1.0/", partName = "prepareMigrateIn") MigrationRecordType var1) throws BadRequestFault, InternalErrorFault, NotFoundFault, UnauthorizedFault;

    @WebMethod(operationName="Migrate", action="http://busdox.org/serviceMetadata/ManageBusinessIdentifierService/1.0/         :migrateIn")
    public void migrate(@WebParam(name = "CompleteMigrationRecord", targetNamespace = "http://busdox.org/serviceMetadata/locator/1.0/", partName = "migrateIn") MigrationRecordType var1) throws BadRequestFault, InternalErrorFault, NotFoundFault, UnauthorizedFault;
}

