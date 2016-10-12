package org.oasis_open.docs.bdxr.ns.smp._2014._07;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "EndpointType",
        propOrder = {"endpointURI", "requireBusinessLevelSignature", "minimumAuthenticationLevel", "serviceActivationDate", "serviceExpirationDate", "certificate", "serviceDescription", "technicalContactUrl", "technicalInformationUrl", "extension"}
)
public class EndpointType {
    @XmlElement(
            name = "EndpointURI",
            required = true
    )
    @XmlSchemaType(
            name = "anyURI"
    )
    protected String endpointURI;
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
    protected byte[] certificate;
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
            name = "transportProfile",
            required = true
    )
    protected String transportProfile;

    public EndpointType() {
    }

    public String getEndpointURI() {
        return this.endpointURI;
    }

    public void setEndpointURI(String value) {
        this.endpointURI = value;
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

    public byte[] getCertificate() {
        return this.certificate;
    }

    public void setCertificate(byte[] value) {
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


