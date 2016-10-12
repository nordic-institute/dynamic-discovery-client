package org.busdox.servicemetadata.managebusinessidentifierservice._1;

import javax.xml.namespace.QName;
import javax.xml.ws.*;
import java.net.URL;

@WebServiceClient(name="ManageBusinessIdentifierService", targetNamespace="http://busdox.org/serviceMetadata/ManageBusinessIdentifierService/1.0/", wsdlLocation="META-INF/ManageBusinessIdentifierService-1.0.wsdl")
public class ManageBusinessIdentifierService
extends Service {
    private static final URL MANAGEBUSINESSIDENTIFIERSERVICE_WSDL_LOCATION;
    private static final WebServiceException MANAGEBUSINESSIDENTIFIERSERVICE_EXCEPTION;
    private static final QName MANAGEBUSINESSIDENTIFIERSERVICE_QNAME;

    public ManageBusinessIdentifierService() {
        super(ManageBusinessIdentifierService.__getWsdlLocation(), MANAGEBUSINESSIDENTIFIERSERVICE_QNAME);
    }

    public /* varargs */ ManageBusinessIdentifierService(WebServiceFeature ... features) {
        super(ManageBusinessIdentifierService.__getWsdlLocation(), MANAGEBUSINESSIDENTIFIERSERVICE_QNAME, features);
    }

    public ManageBusinessIdentifierService(URL wsdlLocation) {
        super(wsdlLocation, MANAGEBUSINESSIDENTIFIERSERVICE_QNAME);
    }

    public /* varargs */ ManageBusinessIdentifierService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, MANAGEBUSINESSIDENTIFIERSERVICE_QNAME, features);
    }

    public ManageBusinessIdentifierService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public /* varargs */ ManageBusinessIdentifierService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    @WebEndpoint(name="ManageBusinessIdentifierServicePort")
    public ManageBusinessIdentifierServiceSoap getManageBusinessIdentifierServicePort() {
        return (ManageBusinessIdentifierServiceSoap)super.getPort(new QName("http://busdox.org/serviceMetadata/ManageBusinessIdentifierService/1.0/", "ManageBusinessIdentifierServicePort"), ManageBusinessIdentifierServiceSoap.class);
    }

    @WebEndpoint(name="ManageBusinessIdentifierServicePort")
    public /* varargs */ ManageBusinessIdentifierServiceSoap getManageBusinessIdentifierServicePort(WebServiceFeature ... features) {
        return (ManageBusinessIdentifierServiceSoap)super.getPort(new QName("http://busdox.org/serviceMetadata/ManageBusinessIdentifierService/1.0/", "ManageBusinessIdentifierServicePort"), ManageBusinessIdentifierServiceSoap.class, features);
    }

    private static URL __getWsdlLocation() {
        if (MANAGEBUSINESSIDENTIFIERSERVICE_EXCEPTION != null) {
            throw MANAGEBUSINESSIDENTIFIERSERVICE_EXCEPTION;
        }
        return MANAGEBUSINESSIDENTIFIERSERVICE_WSDL_LOCATION;
    }

    static {
        MANAGEBUSINESSIDENTIFIERSERVICE_QNAME = new QName("http://busdox.org/serviceMetadata/ManageBusinessIdentifierService/1.0/", "ManageBusinessIdentifierService");
        MANAGEBUSINESSIDENTIFIERSERVICE_WSDL_LOCATION = ManageBusinessIdentifierService.class.getClassLoader().getResource("META-INF/ManageBusinessIdentifierService-1.0.wsdl");
        WebServiceException e = null;
        if (MANAGEBUSINESSIDENTIFIERSERVICE_WSDL_LOCATION == null) {
            e = new WebServiceException("Cannot find 'META-INF/ManageBusinessIdentifierService-1.0.wsdl' wsdl. Place the resource correctly in the classpath.");
        }
        MANAGEBUSINESSIDENTIFIERSERVICE_EXCEPTION = e;
    }
}

