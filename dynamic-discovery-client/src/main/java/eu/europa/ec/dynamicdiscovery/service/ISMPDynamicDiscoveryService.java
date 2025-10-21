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

import eu.europa.ec.dynamicdiscovery.exception.DDCCertificateNotFoundException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.SMPEndpoint;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceGroup;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import eu.europa.ec.dynamicdiscovery.model.SMPTransportProfile;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPProcessIdentifier;

import java.security.cert.X509Certificate;
import java.util.List;

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

    /**
     * Method returns endpoint for given participant, document, process identifiers and transport profile.
     * If redirectionEnabled is set to true and returned Endpoint contains redirect it tris to resolve the redirect as well.
     * The first transport option that matches the criteria will be returned and the remaining options in the list will be ignored.
     *
     * @param participantIdentifier participant identifier to discover endpoint
     * @param documentIdentifier the target document identifier (or action identifier for AS4)
     * @param processId process identifier (or service identifier for AS4)
     * @param processIdScheme process identifier scheme
     * @param transportProfiles a list of transport profile to match against; the order of the transport options is important, the first match being returned
     * @return endpoint for given parameters or null if no endpoint is found.
     * @throws TechnicalException if any error occurs during the lookup
     */
    SMPEndpoint discoverEndpoint(SMPParticipantIdentifier participantIdentifier,
                                 SMPDocumentIdentifier documentIdentifier,
                                 String processId, String processIdScheme, List<String> transportProfiles) throws TechnicalException;

    /**
     * Method returns endpoint for given serviceMetadata with process identifiers and transport profile.
     * If redirectionEnabled is set to true and returned Endpoint contains redirect it tris to resolve the redirect as well.
     * The first transport option that matches the criteria will be returned and the remaining options in the list will be ignored.
     *
     * @param serviceMetadata serviceMetadata
     * @param processId process identifier (or service identifier for AS4)
     * @param processIdScheme process identifier scheme
     * @param transportProfiles a list of transport profile to match against; the order of the transport options is important, the first match being returned
     * @return endpoint for given parameters or null if no endpoint is found.
     * @throws TechnicalException if any error occurs during the lookup
     */
    SMPEndpoint discoverEndpoint(SMPServiceMetadata serviceMetadata,
                                 String processId, String processIdScheme, List<String> transportProfiles) throws TechnicalException;


    /**
     * Check if the certificate exists in the dynamic discovery infrastructure for
     * the given participant, document, process and transport profile.
     * Note: The method does not check the certificate validity and additional
     * trustability such as "trust-anchor exists in clients truststore.
     * The method throws TechnicalException in case of technical error or
     * DDCCertificateNotFoundException if the certificate is not found.
     *
     * @param certificate certificate to validate
     * @param certificateCode certificate code. If the certificate code is null/empty/blank, then any certificate is accepted.
     * @param participantIdentifier participant identifier
     * @param documentIdentifier document identifier
     * @param processIdentifier process identifier
     * @param transportProfile transport profile
     * @throws TechnicalException if the certificate is not valid
     * @throws DDCCertificateNotFoundException if the certificate is not found
     */
    void certificateExists(X509Certificate certificate, String certificateCode,
                           SMPParticipantIdentifier participantIdentifier,
                           SMPDocumentIdentifier documentIdentifier,
                           SMPProcessIdentifier processIdentifier,
                           SMPTransportProfile transportProfile) throws TechnicalException;

}
