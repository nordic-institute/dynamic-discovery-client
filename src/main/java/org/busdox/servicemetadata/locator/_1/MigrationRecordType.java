package org.busdox.servicemetadata.locator._1;

import org.busdox.transport.identifiers._1.ParticipantIdentifierType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MigrationRecordType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MigrationRecordType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://busdox.org/serviceMetadata/locator/1.0/}ServiceMetadataPublisherID"/&gt;
 *         &lt;element ref="{http://busdox.org/transport/identifiers/1.0/}ParticipantIdentifier"/&gt;
 *         &lt;element name="MigrationKey" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MigrationRecordType", propOrder = {
    "serviceMetadataPublisherID",
    "participantIdentifier",
    "migrationKey"
})
public class MigrationRecordType {

    @XmlElement(name = "ServiceMetadataPublisherID", required = true)
    protected String serviceMetadataPublisherID;
    @XmlElement(name = "ParticipantIdentifier", namespace = "http://busdox.org/transport/identifiers/1.0/", required = true)
    protected ParticipantIdentifierType participantIdentifier;
    @XmlElement(name = "MigrationKey", required = true)
    protected String migrationKey;

    /**
     * Gets the value of the serviceMetadataPublisherID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceMetadataPublisherID() {
        return serviceMetadataPublisherID;
    }

    /**
     * Sets the value of the serviceMetadataPublisherID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceMetadataPublisherID(String value) {
        this.serviceMetadataPublisherID = value;
    }

    /**
     * Gets the value of the participantIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link ParticipantIdentifierType }
     *     
     */
    public ParticipantIdentifierType getParticipantIdentifier() {
        return participantIdentifier;
    }

    /**
     * Sets the value of the participantIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParticipantIdentifierType }
     *     
     */
    public void setParticipantIdentifier(ParticipantIdentifierType value) {
        this.participantIdentifier = value;
    }

    /**
     * Gets the value of the migrationKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMigrationKey() {
        return migrationKey;
    }

    /**
     * Sets the value of the migrationKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMigrationKey(String value) {
        this.migrationKey = value;
    }

}
