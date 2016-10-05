package org.busdox.servicemetadata.manageservicemetadataservice._1;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import org.busdox.servicemetadata.locator._1.ObjectFactory;
import org.busdox.servicemetadata.locator._1.ServiceMetadataPublisherServiceType;
import org.busdox.servicemetadata.manageservicemetadataservice._1.BadRequestFault;
import org.busdox.servicemetadata.manageservicemetadataservice._1.InternalErrorFault;
import org.busdox.servicemetadata.manageservicemetadataservice._1.NotFoundFault;
import org.busdox.servicemetadata.manageservicemetadataservice._1.UnauthorizedFault;

@WebService(name="ManageServiceMetadataServiceSoap", targetNamespace="http://busdox.org/serviceMetadata/ManageServiceMetadataService/1.0/")
@SOAPBinding(parameterStyle=SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso(value={ObjectFactory.class, org.busdox.transport.identifiers._1.ObjectFactory.class})
public interface ManageServiceMetadataServiceSoap {
    @WebMethod(operationName="Create", action="http://busdox.org/serviceMetadata/ManageServiceMetadataService/1.0/:createIn")
    public void create(@WebParam(name = "CreateServiceMetadataPublisherService", targetNamespace = "http://busdox.org/serviceMetadata/locator/1.0/", partName = "messagePart") ServiceMetadataPublisherServiceType var1) throws BadRequestFault, InternalErrorFault, UnauthorizedFault;

    @WebMethod(operationName="Read", action="http://busdox.org/serviceMetadata/ManageServiceMetadataService/1.0/:readIn")
    @WebResult(name="ServiceMetadataPublisherService", targetNamespace="http://busdox.org/serviceMetadata/locator/1.0/", partName="messagePart")
    public ServiceMetadataPublisherServiceType read(@WebParam(name = "ReadServiceMetadataPublisherService", targetNamespace = "http://busdox.org/serviceMetadata/locator/1.0/", partName = "messagePart") ServiceMetadataPublisherServiceType var1) throws BadRequestFault, InternalErrorFault, NotFoundFault, UnauthorizedFault;

    @WebMethod(operationName="Update", action="http://busdox.org/serviceMetadata/ManageServiceMetadataService/1.0/:updateIn")
    public void update(@WebParam(name = "UpdateServiceMetadataPublisherService", targetNamespace = "http://busdox.org/serviceMetadata/locator/1.0/", partName = "messagePart") ServiceMetadataPublisherServiceType var1) throws BadRequestFault, InternalErrorFault, NotFoundFault, UnauthorizedFault;

    @WebMethod(operationName="Delete", action="http://busdox.org/serviceMetadata/ManageServiceMetadataService/1.0/:deleteIn")
    public void delete(@WebParam(name = "ServiceMetadataPublisherID", targetNamespace = "http://busdox.org/serviceMetadata/locator/1.0/", partName = "messagePart") String var1) throws BadRequestFault, InternalErrorFault, NotFoundFault, UnauthorizedFault;
}

