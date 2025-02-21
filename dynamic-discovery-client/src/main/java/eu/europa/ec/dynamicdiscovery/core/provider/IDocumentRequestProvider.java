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

import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;

import java.net.URI;

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

    /**
     * Create request for the given resource identifier.
     *
     * @param publisher          The result of the publisher locator
     * @param resourceIdentifier The resource identifier
     * @return The request for the given resource identifier
     */
    URI createRequestForResource(URI publisher, SMPParticipantIdentifier resourceIdentifier);

    /**
     * Create request for the given subresource identifier.
     *
     * @param publisher             The result of the publisher locator
     * @param resourceIdentifier    The resource identifier
     * @param subresourceIdentifier The resource identifier
     * @return The request for the given resource identifier
     */
    URI createRequestForSubresource(URI publisher, SMPParticipantIdentifier resourceIdentifier, SMPDocumentIdentifier subresourceIdentifier) throws TechnicalException;

}
