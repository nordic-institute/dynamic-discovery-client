/*
 * (C) Copyright 2016-2021 - European Commission | Dynamic Discovery Client
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
 */
package eu.europa.ec.dynamicdiscovery.service.impl;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.fetcher.IMetadataFetcher;
import eu.europa.ec.dynamicdiscovery.core.fetcher.impl.DefaultURLFetcher;
import eu.europa.ec.dynamicdiscovery.core.locator.IMetadataLocator;
import eu.europa.ec.dynamicdiscovery.core.provider.IMetadataProvider;
import eu.europa.ec.dynamicdiscovery.core.provider.impl.DefaultProvider;
import eu.europa.ec.dynamicdiscovery.core.reader.IMetadataReader;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ServiceGroup;
import eu.europa.ec.dynamicdiscovery.model.ServiceMetadata;
import eu.europa.ec.dynamicdiscovery.service.IDynamicDiscoveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * @author Flávio W. R. Santos
 */
public class DynamicDiscoveryService implements IDynamicDiscoveryService {
    final static Logger LOG = LoggerFactory.getLogger(DynamicDiscoveryService.class);
    private IMetadataLocator metadataLocator;
    private IMetadataProvider metadataProvider;
    private IMetadataFetcher metadataFetcher;
    private IMetadataReader metadataReader;

    public DynamicDiscoveryService() {
        this.metadataProvider = new DefaultProvider();
        this.metadataFetcher = new DefaultURLFetcher();
    }

    @Override
    public ServiceGroup getServiceGroup(ParticipantIdentifier participantIdentifier) throws TechnicalException {
        return metadataReader.getServiceGroup(getFetcherResponseForDocs(participantIdentifier));
    }

    @Override
    public ServiceMetadata getServiceMetadata(ParticipantIdentifier participantIdentifier, DocumentIdentifier documentIdentifier) throws TechnicalException {
        return metadataReader.getServiceMetadata(getFetcherResponseForServiceMetadata(participantIdentifier, documentIdentifier));
    }

    private FetcherResponse getFetcherResponseForServiceMetadata(ParticipantIdentifier participantIdentifier, DocumentIdentifier documentIdentifier) throws TechnicalException {
        URI smpURI = metadataLocator.lookup(participantIdentifier);
        LOG.debug("Got SMP URI: " + smpURI + " for participant: " + participantIdentifier);
        URI participantUnderSmpURI = metadataProvider.resolveServiceMetadata(smpURI, participantIdentifier, documentIdentifier);
        LOG.info("Get service metadata for URI: " + participantUnderSmpURI);
        return metadataFetcher.fetch(participantUnderSmpURI);
    }

    private FetcherResponse getFetcherResponseForDocs(ParticipantIdentifier participantIdentifier) throws TechnicalException {
        URI smpURI = metadataLocator.lookup(participantIdentifier);
        URI participantUnderSmpURI = metadataProvider.resolveDocumentIdentifiers(smpURI, participantIdentifier);
        LOG.info("Get participant data / documents for URI: " + participantUnderSmpURI);
        return metadataFetcher.fetch(participantUnderSmpURI);
    }

    @Override
    public void setMetadataLocator(IMetadataLocator metadataLocator) {
        this.metadataLocator = metadataLocator;
    }

    @Override
    public void setMetadataProvider(IMetadataProvider metadataProvider) {
        this.metadataProvider = metadataProvider;
    }

    @Override
    public void setMetadataFetcher(IMetadataFetcher metadataFetcher) {
        this.metadataFetcher = metadataFetcher;
    }

    @Override
    public void setMetadataReader(IMetadataReader metadataReader) {
        this.metadataReader = metadataReader;
    }

    @Override
    public IMetadataLocator getMetadataLocator() {
        return metadataLocator;
    }

    @Override
    public IMetadataProvider getMetadataProvider() {
        return metadataProvider;
    }

    @Override
    public IMetadataFetcher getMetadataFetcher() {
        return metadataFetcher;
    }

    @Override
    public IMetadataReader getMetadataReader() {
        return metadataReader;
    }
}
