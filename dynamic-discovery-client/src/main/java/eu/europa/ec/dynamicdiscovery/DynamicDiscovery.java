/*
 * Copyright 2017-2023 European Commission | eDelivery Dynamic Discovery Client
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence attached in file: LICENSE-EUPL-v1.2-EN.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 */
package eu.europa.ec.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceGroup;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import eu.europa.ec.dynamicdiscovery.service.IDynamicDiscoveryService;

import java.util.List;

/**
 * @author Flávio W. R. Santos
 * @since 1.0
 */
public class DynamicDiscovery {

    private IDynamicDiscoveryService service;

    public DynamicDiscovery(IDynamicDiscoveryService service) {
        this.service = service;
    }

    public SMPServiceGroup getServiceGroup(SMPParticipantIdentifier participantIdentifier) throws TechnicalException {
        return service.getServiceGroup(participantIdentifier);
    }
    
    public List<SMPDocumentIdentifier> getDocumentIdentifiers(SMPParticipantIdentifier participantIdentifier) throws TechnicalException {
        return getServiceGroup(participantIdentifier).getDocumentIdentifiers();
    }

    public SMPServiceMetadata getServiceMetadata(SMPParticipantIdentifier participantIdentifier, SMPDocumentIdentifier documentIdentifier) throws TechnicalException {
        return service.getServiceMetadata(participantIdentifier, documentIdentifier);
    }

    public IDynamicDiscoveryService getService() {
        return service;
    }
}
