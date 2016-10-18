package eu.europa.ec.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ServiceMetadata;
import eu.europa.ec.dynamicdiscovery.service.IDynamicDiscoveryService;

import java.util.List;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public class DynamicDiscovery {

    private IDynamicDiscoveryService service;

    DynamicDiscovery(IDynamicDiscoveryService service) {
        this.service = service;
    }

    public List<DocumentIdentifier> getDocumentIdentifiers(ParticipantIdentifier participantIdentifier) throws Exception {
        return service.getDocumentIdentifiers(participantIdentifier);
    }

    public ServiceMetadata getServiceMetadata(ParticipantIdentifier participantIdentifier, DocumentIdentifier documentIdentifier) throws Exception {
        return service.getServiceMetadata(participantIdentifier, documentIdentifier);
    }
}
