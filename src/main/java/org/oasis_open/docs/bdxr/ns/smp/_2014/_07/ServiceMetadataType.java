package org.oasis_open.docs.bdxr.ns.smp._2014._07;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.oasis_open.docs.bdxr.ns.smp._2014._07.RedirectType;
import org.oasis_open.docs.bdxr.ns.smp._2014._07.ServiceInformationType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "ServiceMetadataType",
        propOrder = {"serviceInformation", "redirect"}
)
public class ServiceMetadataType {
    @XmlElement(
            name = "ServiceInformation"
    )
    protected ServiceInformationType serviceInformation;
    @XmlElement(
            name = "Redirect"
    )
    protected RedirectType redirect;

    public ServiceMetadataType() {
    }

    public ServiceInformationType getServiceInformation() {
        return this.serviceInformation;
    }

    public void setServiceInformation(ServiceInformationType value) {
        this.serviceInformation = value;
    }

    public RedirectType getRedirect() {
        return this.redirect;
    }

    public void setRedirect(RedirectType value) {
        this.redirect = value;
    }
}
