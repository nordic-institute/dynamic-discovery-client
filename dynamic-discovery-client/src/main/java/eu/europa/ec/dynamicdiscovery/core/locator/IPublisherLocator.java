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
package eu.europa.ec.dynamicdiscovery.core.locator;

import eu.europa.ec.dynamicdiscovery.core.locator.dns.IDNSLookup;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;

import java.net.URI;

/**
 * Interface for publisher locator. This interface is used to lookup publisher
 * server address where participant published its connectivity data.
 *
 * @author Flávio W. R. SANTOS
 * @author Erlend KLAKEGG BERGHEIM
 * @author Joze RIHTARSIC
 * @since 1.0
 */
public interface IPublisherLocator {

    /**
     * Method to look up the publisher addresses  for the resource/participant identifier. It returns resource URI
     * @param resourceId The resource identifier value for which the publisher address is looked up.
     * @param resourceScheme The resource scheme for which the publisher address is looked up.
     * @return It returns resource URI
     */
    URI lookup(String resourceId, String resourceScheme) throws TechnicalException;

    /**
     * Method to look up the publisher addresses  for the resource/participant identifier. It returns resource URI
     * @param resourceIdentifier The resource identifier for which the publisher address is looked up.
     * @return It returns resource URI
     */
    URI lookup(SMPParticipantIdentifier resourceIdentifier) throws TechnicalException;

    IDNSLookup getDnsLookup();
}
