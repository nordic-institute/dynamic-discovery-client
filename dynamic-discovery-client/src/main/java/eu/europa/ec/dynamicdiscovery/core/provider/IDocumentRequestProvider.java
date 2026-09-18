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
package eu.europa.ec.dynamicdiscovery.core.provider;

import eu.europa.ec.dynamicdiscovery.core.locator.PublisherLookupResult;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;

/**
 * Interface for the Document request provider.The implementation of the interface
 * is responsible for building document request. Example of request definition are the
 * eDelivery SMP profile which is based on OASIS SMP 1.0/2.0 standards, where the request
 * is the REST HTTP-GET query type with dedicated url context path for a given
 * resource/participant identifier and/or subresource/document identifier.
 *
 * @author Flávio W. R. SANTOS
 * @author Joze RIHTARSIC
 * @since 1.0
 */
public interface IDocumentRequestProvider {
    String DEFAULT_CONTEXT = "";
    String DEFAULT_SUB_CONTEXT = "services";

    default PublisherRequest createRequestForResource(PublisherLookupResult publisher, SMPParticipantIdentifier resourceIdentifier) {
        return createRequestForResource(publisher, DEFAULT_CONTEXT, resourceIdentifier);
    }


    default PublisherRequest createRequestForSubresource(PublisherLookupResult publisher, SMPParticipantIdentifier resourceIdentifier, SMPDocumentIdentifier subresourceIdentifier) {
        return createRequestForSubresource(publisher, DEFAULT_CONTEXT, resourceIdentifier, DEFAULT_SUB_CONTEXT, subresourceIdentifier);
    }

    /**
     * Create request for the given resource identifier.
     *
     * @param publisher          The result of the metadata locator
     * @param resourceContext    The context path of the resource
     * @param resourceIdentifier The resource identifier
     * @return The request for the given resource identifier
     * @throws eu.europa.ec.dynamicdiscovery.exception.DDCRuntimeException if the request cannot be created
     */
    PublisherRequest createRequestForResource(PublisherLookupResult publisher,
                                              String resourceContext, SMPParticipantIdentifier resourceIdentifier);

    /**
     * Create request for the given resource identifier.
     *
     * @param publisher             The result of the metadata locator
     * @param resourceContext       The context path of the resource
     * @param resourceIdentifier    The resource identifier
     * @param subresourceContext    The context path of the subresource
     * @param subresourceIdentifier The subresource identifier
     * @return The request for the given resource identifier
     * @throws eu.europa.ec.dynamicdiscovery.exception.DDCRuntimeException if the request cannot be created
     */
    PublisherRequest createRequestForSubresource(PublisherLookupResult publisher,
                                                 String resourceContext, SMPParticipantIdentifier resourceIdentifier,
                                                 String subresourceContext, SMPDocumentIdentifier subresourceIdentifier);

    PublisherRequest createRequestForSubresource(PublisherRequest resourceRequest,
                                                 String subresourceContext,
                                                 SMPDocumentIdentifier documentIdentifier);

}
