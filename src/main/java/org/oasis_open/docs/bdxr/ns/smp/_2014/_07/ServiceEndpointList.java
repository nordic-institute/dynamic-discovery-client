package org.oasis_open.docs.bdxr.ns.smp._2014._07;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "ServiceEndpointList",
        propOrder = {"endpoint"}
)
public class ServiceEndpointList {
    @XmlElement(
            name = "Endpoint",
            required = true
    )
    protected List<EndpointType> endpoint;

    public ServiceEndpointList() {
    }

    public List<EndpointType> getEndpoint() {
        if (this.endpoint == null) {
            this.endpoint = new ArrayList();
        }

        return this.endpoint;
    }
}

