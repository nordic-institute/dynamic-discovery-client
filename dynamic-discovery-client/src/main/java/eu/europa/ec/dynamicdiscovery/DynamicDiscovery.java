/*
 * (C) Copyright 2016 - European Commission | Dynamic Discovery Client
 *
 * https://ec.europa.eu/cefdigital/code/projects/EDELIVERY/repos/dynamic-discovery-client/browse
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
 *
 * @author Flávio W. R. Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 *
 */
package eu.europa.ec.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ServiceMetadata;
import eu.europa.ec.dynamicdiscovery.service.IDynamicDiscoveryService;
import eu.europa.ec.dynamicdiscovery.wrapper.ParticipantIdVO;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.SignedServiceMetadataType;

import java.util.List;

public class DynamicDiscovery {

    private IDynamicDiscoveryService service;

    DynamicDiscovery(IDynamicDiscoveryService service) {
        this.service = service;
    }

    public List<DocumentIdentifier> getDocumentIdentifiers(ParticipantIdentifier participantIdentifier) throws TechnicalException {
        return service.getDocumentIdentifiers(participantIdentifier);
    }

    public ServiceMetadata getServiceMetadata(ParticipantIdentifier participantIdentifier, DocumentIdentifier documentIdentifier) throws TechnicalException {
        return service.getServiceMetadata(participantIdentifier, documentIdentifier);
    }

    public SignedServiceMetadataType getSignedServiceMetadata(ParticipantIdentifier participantIdentifier, DocumentIdentifier documentIdentifier) throws TechnicalException {
        return service.getSignedServiceMetadata(participantIdentifier, documentIdentifier);
    }

    public IDynamicDiscoveryService getService() {
        return service;
    }
}
