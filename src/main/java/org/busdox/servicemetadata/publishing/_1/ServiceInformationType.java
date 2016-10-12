package org.busdox.servicemetadata.publishing._1;

import org.busdox.transport.identifiers._1.DocumentIdentifierType;
import org.busdox.transport.identifiers._1.ParticipantIdentifierType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "ServiceInformationType",
        propOrder = {"participantIdentifier", "documentIdentifier", "processList", "extension"}
)
public class ServiceInformationType {
    @XmlElement(
            name = "ParticipantIdentifier",
            namespace = "http://busdox.org/transport/identifiers/1.0/",
            required = true
    )
    protected ParticipantIdentifierType participantIdentifier;
    @XmlElement(
            name = "DocumentIdentifier",
            namespace = "http://busdox.org/transport/identifiers/1.0/",
            required = true
    )
    protected DocumentIdentifierType documentIdentifier;
    @XmlElement(
            name = "ProcessList",
            required = true
    )
    protected ProcessListType processList;
    @XmlElement(
            name = "Extension"
    )
    protected ExtensionType extension;

    public ServiceInformationType() {
    }

    public ParticipantIdentifierType getParticipantIdentifier() {
        return this.participantIdentifier;
    }

    public void setParticipantIdentifier(ParticipantIdentifierType value) {
        this.participantIdentifier = value;
    }

    public DocumentIdentifierType getDocumentIdentifier() {
        return this.documentIdentifier;
    }

    public void setDocumentIdentifier(DocumentIdentifierType value) {
        this.documentIdentifier = value;
    }

    public ProcessListType getProcessList() {
        return this.processList;
    }

    public void setProcessList(ProcessListType value) {
        this.processList = value;
    }

    public ExtensionType getExtension() {
        return this.extension;
    }

    public void setExtension(ExtensionType value) {
        this.extension = value;
    }
}