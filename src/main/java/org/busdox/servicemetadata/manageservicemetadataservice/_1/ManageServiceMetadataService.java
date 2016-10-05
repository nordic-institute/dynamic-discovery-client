package org.busdox.servicemetadata.manageservicemetadataservice._1;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import org.busdox.servicemetadata.manageservicemetadataservice._1.ManageServiceMetadataServiceSoap;

@WebServiceClient(name="ManageServiceMetadataService", targetNamespace="http://busdox.org/serviceMetadata/ManageServiceMetadataService/1.0/", wsdlLocation="META-INF/ManageServiceMetadataService-1.0.wsdl")
public class ManageServiceMetadataService
extends Service {
    private static final URL MANAGESERVICEMETADATASERVICE_WSDL_LOCATION;
    private static final WebServiceException MANAGESERVICEMETADATASERVICE_EXCEPTION;
    private static final QName MANAGESERVICEMETADATASERVICE_QNAME;

    public ManageServiceMetadataService() {
        super(ManageServiceMetadataService.__getWsdlLocation(), MANAGESERVICEMETADATASERVICE_QNAME);
    }

    public /* varargs */ ManageServiceMetadataService(WebServiceFeature ... features) {
        super(ManageServiceMetadataService.__getWsdlLocation(), MANAGESERVICEMETADATASERVICE_QNAME, features);
    }

    public ManageServiceMetadataService(URL wsdlLocation) {
        super(wsdlLocation, MANAGESERVICEMETADATASERVICE_QNAME);
    }

    public /* varargs */ ManageServiceMetadataService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, MANAGESERVICEMETADATASERVICE_QNAME, features);
    }

    public ManageServiceMetadataService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public /* varargs */ ManageServiceMetadataService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    @WebEndpoint(name="ManageServiceMetadataServicePort")
    public ManageServiceMetadataServiceSoap getManageServiceMetadataServicePort() {
        return (ManageServiceMetadataServiceSoap)super.getPort(new QName("http://busdox.org/serviceMetadata/ManageServiceMetadataService/1.0/", "ManageServiceMetadataServicePort"), ManageServiceMetadataServiceSoap.class);
    }

    @WebEndpoint(name="ManageServiceMetadataServicePort")
    public /* varargs */ ManageServiceMetadataServiceSoap getManageServiceMetadataServicePort(WebServiceFeature ... features) {
        return (ManageServiceMetadataServiceSoap)super.getPort(new QName("http://busdox.org/serviceMetadata/ManageServiceMetadataService/1.0/", "ManageServiceMetadataServicePort"), ManageServiceMetadataServiceSoap.class, features);
    }

    private static URL __getWsdlLocation() {
        if (MANAGESERVICEMETADATASERVICE_EXCEPTION != null) {
            throw MANAGESERVICEMETADATASERVICE_EXCEPTION;
        }
        return MANAGESERVICEMETADATASERVICE_WSDL_LOCATION;
    }

    static {
        MANAGESERVICEMETADATASERVICE_QNAME = new QName("http://busdox.org/serviceMetadata/ManageServiceMetadataService/1.0/", "ManageServiceMetadataService");
        MANAGESERVICEMETADATASERVICE_WSDL_LOCATION = ManageServiceMetadataService.class.getClassLoader().getResource("META-INF/ManageServiceMetadataService-1.0.wsdl");
        WebServiceException e = null;
        if (MANAGESERVICEMETADATASERVICE_WSDL_LOCATION == null) {
            e = new WebServiceException("Cannot find 'META-INF/ManageServiceMetadataService-1.0.wsdl' wsdl. Place the resource correctly in the classpath.");
        }
        MANAGESERVICEMETADATASERVICE_EXCEPTION = e;
    }
}

