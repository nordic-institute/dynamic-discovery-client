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
package eu.europa.ec.dynamicdiscovery.core.locator.impl;

import eu.europa.ec.dynamicdiscovery.core.locator.IPublisherLocator;
import eu.europa.ec.dynamicdiscovery.core.locator.PublisherLookupResult;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The class is simple implementation of the interface IMetadataLocator. The intention of the implementation is
 * for serving configured SMP URI(s) for the network. The StaticMapMetadataLocator lookup URI based on party identifier or
 * it returns default URI.
 *
 * The StaticBDXRLocator can be used in a network configuration where all participants register its service metadata on
 * one SMP with know URL. Additionally, the Map<ParticipantIdentifier, URI>  allows to configure list of exceptions to
 * default URL.
 *
 * @author Joze RIHTARSIC
 * @since 1.14
 */
public class StaticMapMetadataLocator implements IPublisherLocator {
    private static final Logger LOG = LoggerFactory.getLogger(StaticMapMetadataLocator.class);
    final URI defaultURI;
    Map<SMPParticipantIdentifier, URI> mapExceptionsUri;

    public StaticMapMetadataLocator(String defaultURI) throws URISyntaxException {
        this(new URI(defaultURI), null);
    }

    public StaticMapMetadataLocator(URI defaultURI) {
        this(defaultURI, null);
    }

    public StaticMapMetadataLocator(URI defaultURI, Map<SMPParticipantIdentifier, URI> mapExceptionsUri) {
        this.defaultURI = defaultURI;
        this.mapExceptionsUri = mapExceptionsUri;
    }

    @Override
    public List<PublisherLookupResult> lookup(SMPParticipantIdentifier participantIdentifier) throws TechnicalException {
        if (mapExceptionsUri != null && mapExceptionsUri.containsKey(participantIdentifier)) {
            URI uri = mapExceptionsUri.get(participantIdentifier);
            LOG.debug("Return uri [{}] for participant [{}]!", uri, participantIdentifier);
            return Collections.singletonList(new PublisherLookupResult(participantIdentifier, uri, null));
        }
        LOG.debug("Return default uri [{}] for participant identifier [{}]!", defaultURI, participantIdentifier);
        return Collections.singletonList(new PublisherLookupResult(participantIdentifier, defaultURI, null));
    }

}
