package org.busdox.servicemetadata.locator._1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.busdox.transport.identifiers._1.ParticipantIdentifierType;


/**
 * <p>Java class for ParticipantIdentifierPageType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ParticipantIdentifierPageType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://busdox.org/transport/identifiers/1.0/}ParticipantIdentifier" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{http://busdox.org/serviceMetadata/locator/1.0/}ServiceMetadataPublisherID" minOccurs="0"/&gt;
 *         &lt;element name="NextPageIdentifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParticipantIdentifierPageType", propOrder = {
    "participantIdentifier",
    "serviceMetadataPublisherID",
    "nextPageIdentifier"
})
public class ParticipantIdentifierPageType {

    @XmlElement(name = "ParticipantIdentifier", namespace = "http://busdox.org/transport/identifiers/1.0/")
    protected List<ParticipantIdentifierType> participantIdentifier;
    @XmlElement(name = "ServiceMetadataPublisherID")
    protected String serviceMetadataPublisherID;
    @XmlElement(name = "NextPageIdentifier")
    protected String nextPageIdentifier;

    /**
     * Gets the value of the participantIdentifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the participantIdentifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParticipantIdentifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ParticipantIdentifierType }
     * 
     * 
     */
    public List<ParticipantIdentifierType> getParticipantIdentifier() {
        if (participantIdentifier == null) {
            participantIdentifier = new ArrayList<ParticipantIdentifierType>();
        }
        return this.participantIdentifier;
    }

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
     * Gets the value of the nextPageIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNextPageIdentifier() {
        return nextPageIdentifier;
    }

    /**
     * Sets the value of the nextPageIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNextPageIdentifier(String value) {
        this.nextPageIdentifier = value;
    }

}
