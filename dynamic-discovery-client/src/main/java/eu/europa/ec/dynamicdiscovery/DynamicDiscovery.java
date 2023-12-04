/*
 * #%L
 * dynamic-discovery-cli
 * %%
 * Copyright (C) 2016 - 2023 European Commission | eDelivery | Dynamic Discovery Client
 * %%
 * Licensed under the LGPL, Version 2.1 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * [PROJECT_HOME]\license\lgpl2-1\license.txt or https://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
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
