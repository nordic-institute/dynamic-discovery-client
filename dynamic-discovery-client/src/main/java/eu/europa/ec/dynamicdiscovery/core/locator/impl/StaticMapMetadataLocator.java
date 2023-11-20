/*
 * Copyright 2017-2023 European Commission | CEF eDelivery
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence attached in file: LICENCE-EUPL-v1.2.pdf
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 */
package eu.europa.ec.dynamicdiscovery.core.locator.impl;

import eu.europa.ec.dynamicdiscovery.core.locator.IMetadataLocator;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.IDNSLookup;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
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
public class StaticMapMetadataLocator implements IMetadataLocator {
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
    public URI lookup(String participantId, String participantScheme) throws TechnicalException {
        return lookup(new SMPParticipantIdentifier(participantId, participantScheme));
    }

    @Override
    public URI lookup(SMPParticipantIdentifier participantIdentifier) throws TechnicalException {
        if (mapExceptionsUri != null && mapExceptionsUri.containsKey(participantIdentifier)) {
            URI uri = mapExceptionsUri.get(participantIdentifier);
            LOG.debug("Return uri [{}] for participant [{}]!", uri, participantIdentifier);
            return uri;
        }
        LOG.debug("Return default uri [{}] for participant identifier [{}]!", defaultURI, participantIdentifier);
        return defaultURI;
    }

    @Override
    public IDNSLookup getDnsLookup() {
        return null;
    }
}
