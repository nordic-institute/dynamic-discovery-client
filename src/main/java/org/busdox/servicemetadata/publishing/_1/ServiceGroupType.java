package org.busdox.servicemetadata.publishing._1;

import org.busdox.transport.identifiers._1.ParticipantIdentifierType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "ServiceGroupType",
        propOrder = {"participantIdentifier", "serviceMetadataReferenceCollection", "extension"}
)
public class ServiceGroupType {
    @XmlElement(
            name = "ParticipantIdentifier",
            namespace = "http://busdox.org/transport/identifiers/1.0/",
            required = true
    )
    protected ParticipantIdentifierType participantIdentifier;
    @XmlElement(
            name = "ServiceMetadataReferenceCollection",
            required = true
    )
    protected ServiceMetadataReferenceCollectionType serviceMetadataReferenceCollection;
    @XmlElement(
            name = "Extension"
    )
    protected ExtensionType extension;

    public ServiceGroupType() {
    }

    public ParticipantIdentifierType getParticipantIdentifier() {
        return this.participantIdentifier;
    }

    public void setParticipantIdentifier(ParticipantIdentifierType value) {
        this.participantIdentifier = value;
    }

    public ServiceMetadataReferenceCollectionType getServiceMetadataReferenceCollection() {
        return this.serviceMetadataReferenceCollection;
    }

    public void setServiceMetadataReferenceCollection(ServiceMetadataReferenceCollectionType value) {
        this.serviceMetadataReferenceCollection = value;
    }

    public ExtensionType getExtension() {
        return this.extension;
    }

    public void setExtension(ExtensionType value) {
        this.extension = value;
    }
}
