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
import eu.europa.ec.dynamicdiscovery.core.fetcher.SMPParticipantIdentifierLookupResult;
import eu.europa.ec.dynamicdiscovery.core.fetcher.impl.DefaultURLFetcher;
import eu.europa.ec.dynamicdiscovery.core.locator.IMetadataLocator;
import eu.europa.ec.dynamicdiscovery.core.provider.IMetadataProvider;
import eu.europa.ec.dynamicdiscovery.core.provider.impl.DefaultProvider;
import eu.europa.ec.dynamicdiscovery.core.reader.IMetadataReader;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.SMPExceptionCode;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceGroup;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.service.IDynamicDiscoveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * @author Flávio W. R. Santos
 */
public class DynamicDiscoveryService implements IDynamicDiscoveryService {
    static final Logger LOG = LoggerFactory.getLogger(DynamicDiscoveryService.class);
    private IMetadataLocator metadataLocator;
    private IMetadataProvider metadataProvider;
    private IMetadataFetcher metadataFetcher;
    private IMetadataReader metadataReader;

    public DynamicDiscoveryService() {
        this.metadataProvider = new DefaultProvider();
        this.metadataFetcher = new DefaultURLFetcher.Builder().build();
    }

    @Override
    public SMPServiceGroup getServiceGroup(SMPParticipantIdentifier participantIdentifier) throws TechnicalException {
        final SMPParticipantIdentifierLookupResult lookupParticipantInSMP = lookupParticipantInSMP(participantIdentifier);
        final SMPServiceGroup serviceGroup = metadataReader.getServiceGroup(lookupParticipantInSMP.getFetcherResponse());
        serviceGroup.setServiceGroupSmpURI(lookupParticipantInSMP.getParticipantUnderSmpURI());
        return serviceGroup;
    }

    @Override
    public SMPServiceMetadata getServiceMetadata(SMPParticipantIdentifier participantIdentifier, SMPDocumentIdentifier documentIdentifier) throws TechnicalException {
        final FetcherResponse fetcherResponseForServiceMetadata = getFetcherResponseForServiceMetadata(participantIdentifier, documentIdentifier);
        return metadataReader.getServiceMetadata(fetcherResponseForServiceMetadata);
    }

    private FetcherResponse getFetcherResponseForServiceMetadata(SMPParticipantIdentifier participantIdentifier, SMPDocumentIdentifier documentIdentifier) throws TechnicalException {
        final URI documentURI = getDocumentURI(participantIdentifier, documentIdentifier);
        LOG.info("Fetching service metadata using URI: [{}].", documentURI);
        return metadataFetcher.fetch(documentURI);
    }

    protected URI getDocumentURI(SMPParticipantIdentifier participantIdentifier, SMPDocumentIdentifier documentIdentifier) throws TechnicalException {
        final URI documentIdentifierSmpURI = documentIdentifier.getDocumentIdentifierSmpURI();
        //in case the document identifier was previously discovered from the ServiceGroup, we skip the DNS lookup and reuse the discovered URL
        if (documentIdentifierSmpURI != null) {
            LOG.info("Using service metadata from SMPDocumentIdentifier already discovered");
            return documentIdentifierSmpURI;
        }

        URI smpURI = lookupParticipantSMPUri(participantIdentifier);
        LOG.debug("Got SMP URI: [{}] for participant: [{}].", smpURI, participantIdentifier);
        URI resolvedDocumentIdentifierSmpURI = metadataProvider.resolveServiceMetadata(smpURI, participantIdentifier, documentIdentifier);
        return resolvedDocumentIdentifierSmpURI;
    }

    public SMPParticipantIdentifierLookupResult lookupParticipantInSMP(SMPParticipantIdentifier participantIdentifier) throws TechnicalException {
        URI smpURI = lookupParticipantSMPUri(participantIdentifier);
        URI participantUnderSmpURI = metadataProvider.resolveForParticipantIdentifier(smpURI, participantIdentifier);
        LOG.info("Get participant data / documents for URI: [{}].", participantUnderSmpURI);
        final FetcherResponse fetcherResponse = metadataFetcher.fetch(participantUnderSmpURI);
        return new SMPParticipantIdentifierLookupResult(smpURI, participantUnderSmpURI, fetcherResponse);
    }

    private URI lookupParticipantSMPUri(SMPParticipantIdentifier participantIdentifier) throws TechnicalException {
        URI smpURI = metadataLocator.lookup(participantIdentifier);
        if (smpURI == null) {
            throw new DNSLookupException(SMPExceptionCode.SERVICE_GROUP, "DNS record for participant [" + participantIdentifier + "] can not be resolved!");
        }
        LOG.debug("Got SMP URI: [{}] for participant: [{}].", smpURI, participantIdentifier);
        return smpURI;
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
