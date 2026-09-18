/*-
 * #%L
 * dynamic-discovery-client
 * %%
 * Copyright (C) 2016 - 2025 European Commission | eDelivery | Dynamic Discovery Client
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

import eu.europa.ec.dynamicdiscovery.core.fetcher.IDocumentFetcher;
import eu.europa.ec.dynamicdiscovery.core.locator.IPublisherLocator;
import eu.europa.ec.dynamicdiscovery.core.provider.IDocumentRequestProvider;
import eu.europa.ec.dynamicdiscovery.core.reader.IDocumentReader;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;


/**
 * Main interface for the Dynamic Discovery Service. The implementation of the
 * interface  is responsible for discovering the endpoints of a given participant, document and process.
 *
 * @author Flávio W. R. SANTOS
 * @author Joze RIHTARSIC
 * @since 1.0
 */
public interface IDynamicDiscoveryService<R, S> {


    /**
     * Get the resource (e.g. service group) for the given resource/participant identifier.
     *
     * @param resourceIdentifier resource/participant identifier
     * @return get target resource object
     * @throws TechnicalException if any error occurs during the lookup
     */
    R getResource(SMPParticipantIdentifier resourceIdentifier) throws TechnicalException;

    /**
     * Get the sub-resource (e.g. service metadata) for the given resource/participant and souresource/document identifier.
     *
     * @param resourceIdentifier    resource/participant identifier
     * @param subresourceIdentifier subresource/document identifier
     * @return get target sub-resource object
     * @throws TechnicalException if any error occurs during the lookup
     */
    S getSubresource(SMPParticipantIdentifier resourceIdentifier, SMPDocumentIdentifier subresourceIdentifier) throws TechnicalException;


    IPublisherLocator getPublisherLocator();

    IDocumentRequestProvider getDocumentRequestProvider();

    IDocumentFetcher getDocumentFetcher();

    IDocumentReader<R, S> getDocumentReader();
}
