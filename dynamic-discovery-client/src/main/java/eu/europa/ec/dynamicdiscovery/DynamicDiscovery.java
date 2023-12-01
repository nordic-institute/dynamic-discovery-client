/*
 * Copyright 2016-2023 - European Commission | Dynamic Discovery Client
 *
 * https://ec.europa.eu/digital-building-blocks/code/projects/EDELIVERY/repos/dynamic-discovery-client/browse
 *
 * Licensed under the LGPL, Version 2.1 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     dynamic-discovery\License_LGPL-2.1.txt or https://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
