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

import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;

import java.util.List;

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
     * Method to look up the SMP addresses  for the resource/participant identifier. It returns the list of the
     * {@link PublisherLookupResult} which contains the URL of the metadata and the service type in case of DNS NAPTR resolution.
     * If no metadata is found, the empty list is returned.
     * @param participantId The participant identifier for which the SMP address is looked up.
     * @param participantScheme The participant scheme for which the SMP address is looked up.
     * @return The list of the {@link PublisherLookupResult} or empty list if no results is found.
     * @throws TechnicalException
     */
    default  List<PublisherLookupResult> lookup(String participantId, String participantScheme) throws TechnicalException{
        return lookup(new SMPParticipantIdentifier(participantId, participantScheme));
    }

    /**
     * Method to look up the SMP addresses  for the resource/participant identifier. It returns the list of the
     * {@link PublisherLookupResult} which contains the URL of the metadata and the service type in case of DNS NAPTR resolution.
     * If no metadata is found, the empty list is returned.
     * @param participantIdentifier The participant identifier for which the SMP address is looked up.
     * @return The list of the {@link PublisherLookupResult} or empty list if no results is found.
     * @throws TechnicalException If the lookup fails for any technical cause.
     */
    List<PublisherLookupResult> lookup(SMPParticipantIdentifier participantIdentifier) throws TechnicalException;
}
