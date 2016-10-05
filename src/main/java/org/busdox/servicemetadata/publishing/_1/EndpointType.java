package org.busdox.servicemetadata.publishing._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import org.busdox.servicemetadata.publishing._1.ExtensionType;
import org.w3._2005._08.addressing.EndpointReferenceType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "EndpointType",
        propOrder = {"endpointReference", "requireBusinessLevelSignature", "minimumAuthenticationLevel", "serviceActivationDate", "serviceExpirationDate", "certificate", "serviceDescription", "technicalContactUrl", "technicalInformationUrl", "extension"}
)
public class EndpointType {
    @XmlElement(
            name = "EndpointReference",
            namespace = "http://www.w3.org/2005/08/addressing",
            required = true
    )
    protected EndpointReferenceType endpointReference;
    @XmlElement(
            name = "RequireBusinessLevelSignature"
    )
    protected boolean requireBusinessLevelSignature;
    @XmlElement(
            name = "MinimumAuthenticationLevel"
    )
    protected String minimumAuthenticationLevel;
    @XmlElement(
            name = "ServiceActivationDate"
    )
    @XmlSchemaType(
            name = "dateTime"
    )
    protected XMLGregorianCalendar serviceActivationDate;
    @XmlElement(
            name = "ServiceExpirationDate"
    )
    @XmlSchemaType(
            name = "dateTime"
    )
    protected XMLGregorianCalendar serviceExpirationDate;
    @XmlElement(
            name = "Certificate",
            required = true
    )
    protected String certificate;
    @XmlElement(
            name = "ServiceDescription",
            required = true
    )
    protected String serviceDescription;
    @XmlElement(
            name = "TechnicalContactUrl",
            required = true
    )
    @XmlSchemaType(
            name = "anyURI"
    )
    protected String technicalContactUrl;
    @XmlElement(
            name = "TechnicalInformationUrl"
    )
    @XmlSchemaType(
            name = "anyURI"
    )
    protected String technicalInformationUrl;
    @XmlElement(
            name = "Extension"
    )
    protected ExtensionType extension;
    @XmlAttribute(
            name = "transportProfile"
    )
    protected String transportProfile;

    public EndpointType() {
    }

    public EndpointReferenceType getEndpointReference() {
        return this.endpointReference;
    }

    public void setEndpointReference(EndpointReferenceType value) {
        this.endpointReference = value;
    }

    public boolean isRequireBusinessLevelSignature() {
        return this.requireBusinessLevelSignature;
    }

    public void setRequireBusinessLevelSignature(boolean value) {
        this.requireBusinessLevelSignature = value;
    }

    public String getMinimumAuthenticationLevel() {
        return this.minimumAuthenticationLevel;
    }

    public void setMinimumAuthenticationLevel(String value) {
        this.minimumAuthenticationLevel = value;
    }

    public XMLGregorianCalendar getServiceActivationDate() {
        return this.serviceActivationDate;
    }

    public void setServiceActivationDate(XMLGregorianCalendar value) {
        this.serviceActivationDate = value;
    }

    public XMLGregorianCalendar getServiceExpirationDate() {
        return this.serviceExpirationDate;
    }

    public void setServiceExpirationDate(XMLGregorianCalendar value) {
        this.serviceExpirationDate = value;
    }

    public String getCertificate() {
        return this.certificate;
    }

    public void setCertificate(String value) {
        this.certificate = value;
    }

    public String getServiceDescription() {
        return this.serviceDescription;
    }

    public void setServiceDescription(String value) {
        this.serviceDescription = value;
    }

    public String getTechnicalContactUrl() {
        return this.technicalContactUrl;
    }

    public void setTechnicalContactUrl(String value) {
        this.technicalContactUrl = value;
    }

    public String getTechnicalInformationUrl() {
        return this.technicalInformationUrl;
    }

    public void setTechnicalInformationUrl(String value) {
        this.technicalInformationUrl = value;
    }

    public ExtensionType getExtension() {
        return this.extension;
    }

    public void setExtension(ExtensionType value) {
        this.extension = value;
    }

    public String getTransportProfile() {
        return this.transportProfile;
    }

    public void setTransportProfile(String value) {
        this.transportProfile = value;
    }
}
