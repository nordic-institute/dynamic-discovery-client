package org.busdox.servicemetadata.publishing._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "ServiceMetadataReferenceCollectionType",
        propOrder = {"serviceMetadataReference"}
)
public class ServiceMetadataReferenceCollectionType {
    @XmlElement(
            name = "ServiceMetadataReference"
    )
    protected List<ServiceMetadataReferenceType> serviceMetadataReference;

    public ServiceMetadataReferenceCollectionType() {
    }

    public List<ServiceMetadataReferenceType> getServiceMetadataReference() {
        if (this.serviceMetadataReference == null) {
            this.serviceMetadataReference = new ArrayList();
        }

        return this.serviceMetadataReference;
    }
}

