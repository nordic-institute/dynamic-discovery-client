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
package eu.europa.ec.dynamicdiscovery.service;

import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.SMPEndpoint;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceGroup;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import eu.europa.ec.dynamicdiscovery.model.SMPTransportProfile;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPProcessIdentifier;

import java.security.cert.X509Certificate;

/**
 * Main SMP  DDC interface for the Dynamic Discovery Service. The implementation of the
 * interface  is responsible for discovering the endpoints of a given participant, document and process.
 *
 * @author Flávio W. R. Santos
 * @since 1.0
 */
public interface ISMPDynamicDiscoveryService extends IDynamicDiscoveryService<SMPServiceGroup, SMPServiceMetadata> {

    void setRedirectionEnabled(boolean redirectionEnabled);

    void setDefaultEndpointForEmptyProcess(boolean defaultEndpointForEmptyProcess);

    SMPEndpoint discoverEndpoint(SMPParticipantIdentifier participantIdentifier,
                                 SMPDocumentIdentifier documentIdentifier,
                                 String processId, String processIdScheme, String transportProfile) throws TechnicalException;

    SMPEndpoint discoverEndpoint(SMPServiceMetadata serviceMetadata,
                                 String processId, String processIdScheme, String transportProfile) throws TechnicalException;


    void certificateExists(X509Certificate certificate, String certificateCode,
                           SMPParticipantIdentifier participantIdentifier,
                           SMPDocumentIdentifier documentIdentifier,
                           SMPProcessIdentifier processIdentifier, SMPTransportProfile transportProfile) throws TechnicalException;

}
