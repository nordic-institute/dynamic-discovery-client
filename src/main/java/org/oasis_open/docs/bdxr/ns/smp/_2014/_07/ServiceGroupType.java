package org.oasis_open.docs.bdxr.ns.smp._2014._07;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.oasis_open.docs.bdxr.ns.smp._2014._07.ExtensionType;
import org.oasis_open.docs.bdxr.ns.smp._2014._07.ParticipantIdentifierType;
import org.oasis_open.docs.bdxr.ns.smp._2014._07.ServiceMetadataReferenceCollectionType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "ServiceGroupType",
        propOrder = {"participantIdentifier", "serviceMetadataReferenceCollection", "extension"}
)
public class ServiceGroupType {
    @XmlElement(
            name = "ParticipantIdentifier",
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

