package org.busdox.servicemetadata.locator._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PublisherEndpointType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PublisherEndpointType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="LogicalAddress" type="{http://www.w3.org/2001/XMLSchema}anyURI"/&gt;
 *         &lt;element name="PhysicalAddress" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PublisherEndpointType", propOrder = {
    "logicalAddress",
    "physicalAddress"
})
public class PublisherEndpointType {

    @XmlElement(name = "LogicalAddress", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String logicalAddress;
    @XmlElement(name = "PhysicalAddress", required = true)
    protected String physicalAddress;

    /**
     * Gets the value of the logicalAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogicalAddress() {
        return logicalAddress;
    }

    /**
     * Sets the value of the logicalAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogicalAddress(String value) {
        this.logicalAddress = value;
    }

    /**
     * Gets the value of the physicalAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhysicalAddress() {
        return physicalAddress;
    }

    /**
     * Sets the value of the physicalAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhysicalAddress(String value) {
        this.physicalAddress = value;
    }

}
