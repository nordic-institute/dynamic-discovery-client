package org.busdox.servicemetadata.locator._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ServiceMetadataPublisherServiceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ServiceMetadataPublisherServiceType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="PublisherEndpoint" type="{http://busdox.org/serviceMetadata/locator/1.0/}PublisherEndpointType"/&gt;
 *         &lt;element ref="{http://busdox.org/serviceMetadata/locator/1.0/}ServiceMetadataPublisherID"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceMetadataPublisherServiceType", propOrder = {
    "publisherEndpoint",
    "serviceMetadataPublisherID"
})
public class ServiceMetadataPublisherServiceType {

    @XmlElement(name = "PublisherEndpoint", required = true)
    protected PublisherEndpointType publisherEndpoint;
    @XmlElement(name = "ServiceMetadataPublisherID", required = true)
    protected String serviceMetadataPublisherID;

    /**
     * Gets the value of the publisherEndpoint property.
     * 
     * @return
     *     possible object is
     *     {@link PublisherEndpointType }
     *     
     */
    public PublisherEndpointType getPublisherEndpoint() {
        return publisherEndpoint;
    }

    /**
     * Sets the value of the publisherEndpoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link PublisherEndpointType }
     *     
     */
    public void setPublisherEndpoint(PublisherEndpointType value) {
        this.publisherEndpoint = value;
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

}
