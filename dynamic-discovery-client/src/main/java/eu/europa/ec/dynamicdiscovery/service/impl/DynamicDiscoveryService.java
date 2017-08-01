/*
 * (C) Copyright 2016 - European Commission | Dynamic Discovery Client
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
 *
 * @author Flávio W. R. Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 *
 */
package eu.europa.ec.dynamicdiscovery.service.impl;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.fetcher.IMetadataFetcher;
import eu.europa.ec.dynamicdiscovery.core.fetcher.impl.DefaultURLFetcher;
import eu.europa.ec.dynamicdiscovery.core.locator.IMetadataLocator;
import eu.europa.ec.dynamicdiscovery.core.provider.IMetadataProvider;
import eu.europa.ec.dynamicdiscovery.core.provider.impl.DefaultProvider;
import eu.europa.ec.dynamicdiscovery.core.reader.IMetadataReader;
import eu.europa.ec.dynamicdiscovery.core.reader.impl.DefaultBDXRReader;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ServiceMetadata;
import eu.europa.ec.dynamicdiscovery.service.IDynamicDiscoveryService;
import eu.europa.ec.dynamicdiscovery.wrapper.DocumentIdVO;
import eu.europa.ec.dynamicdiscovery.wrapper.ParticipantIdVO;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceGroupType;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.SignedServiceMetadataType;

import java.net.URI;
import java.util.List;

public class DynamicDiscoveryService implements IDynamicDiscoveryService {

    private IMetadataLocator metadataLocator;
    private IMetadataProvider metadataProvider;
    private IMetadataFetcher metadataFetcher;
    private IMetadataReader metadataReader;

    public DynamicDiscoveryService() {
        this.metadataProvider = new DefaultProvider();
        this.metadataFetcher = new DefaultURLFetcher();
    }

    @Override
    public List<DocumentIdentifier> getDocumentIdentifiers(ParticipantIdentifier participantIdentifier) throws TechnicalException {
        return metadataReader.getDocumentIdentifiers(getFetcherResponseForDocs(participantIdentifier));
    }

    @Override
    public ServiceGroupType getServiceGroup(ParticipantIdentifier participantIdentifier) throws TechnicalException {
        return metadataReader.getServiceGroup(getFetcherResponseForDocs(participantIdentifier));
    }

    /**
     * @deprecated Replaced by {@link #getSignedServiceMetadata(ParticipantIdentifier, DocumentIdentifier)}
     */
    @Deprecated
    @Override
    public ServiceMetadata getServiceMetadata(ParticipantIdentifier participantIdentifier, DocumentIdentifier documentIdentifier) throws TechnicalException {
        return metadataReader.getServiceMetadata(getFetcherResponseForServiceMetadata(participantIdentifier, documentIdentifier));
    }

    @Override
    public SignedServiceMetadataType getSignedServiceMetadata(ParticipantIdentifier participantIdentifier, DocumentIdentifier documentIdentifier) throws TechnicalException {
        return metadataReader.getSignedServiceMetadata(getFetcherResponseForServiceMetadata(participantIdentifier, documentIdentifier));
    }

    private FetcherResponse getFetcherResponseForServiceMetadata(ParticipantIdentifier participantIdentifier, DocumentIdentifier documentIdentifier) throws TechnicalException {
        URI smpURI = metadataLocator.lookup(participantIdentifier);
        URI participantUnderSmpURI = metadataProvider.resolveServiceMetadata(smpURI, participantIdentifier, documentIdentifier);
        return metadataFetcher.fetch(participantUnderSmpURI);
    }

    private FetcherResponse getFetcherResponseForDocs(ParticipantIdentifier participantIdentifier) throws TechnicalException {
        URI smpURI = metadataLocator.lookup(participantIdentifier);
        URI participantUnderSmpURI = metadataProvider.resolveDocumentIdentifiers(smpURI, participantIdentifier);
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
