package org.busdox.servicemetadata.publishing._1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.busdox.servicemetadata.publishing._1.EndpointType;

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


