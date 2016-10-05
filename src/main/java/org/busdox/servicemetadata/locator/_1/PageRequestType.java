package org.busdox.servicemetadata.locator._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PageRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PageRequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://busdox.org/serviceMetadata/locator/1.0/}ServiceMetadataPublisherID"/&gt;
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
@XmlType(name = "PageRequestType", propOrder = {
    "serviceMetadataPublisherID",
    "nextPageIdentifier"
})
public class PageRequestType {

    @XmlElement(name = "ServiceMetadataPublisherID", required = true)
    protected String serviceMetadataPublisherID;
    @XmlElement(name = "NextPageIdentifier")
    protected String nextPageIdentifier;

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
