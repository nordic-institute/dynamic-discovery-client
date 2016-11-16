/*
 * (C) Copyright 2016 Dynamic Discovery Client
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
package eu.europa.ec.dynamicdiscovery.service;

import eu.europa.ec.dynamicdiscovery.core.fetcher.IMetadataFetcher;
import eu.europa.ec.dynamicdiscovery.core.locator.IMetadataLocator;
import eu.europa.ec.dynamicdiscovery.core.provider.IMetadataProvider;
import eu.europa.ec.dynamicdiscovery.core.reader.IMetadataReader;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ServiceMetadata;

import java.net.URI;
import java.util.List;

public class DynamicDiscoveryService extends AbstractDynamicDiscoveryService {

    public DynamicDiscoveryService(IMetadataLocator metadataLocator, IMetadataProvider metadataProvider, IMetadataFetcher metadataFetcher, IMetadataReader metadataReader) {
        build(metadataLocator, metadataProvider, metadataFetcher, metadataReader);
    }

    @Override
    public List<DocumentIdentifier> getDocumentIdentifiers(ParticipantIdentifier participantIdentifier) throws TechnicalException {
        URI location = metadataLocator.lookup(participantIdentifier);
        URI provider = metadataProvider.resolveDocumentIdentifiers(location, participantIdentifier);
        return metadataReader.parseDocumentIdentifiers(metadataFetcher.fetch(provider));
    }

    @Override
    public ServiceMetadata getServiceMetadata(ParticipantIdentifier participantIdentifier, DocumentIdentifier documentIdentifier) throws TechnicalException {
        URI location = metadataLocator.lookup(participantIdentifier);
        URI provider = metadataProvider.resolveServiceMetadata(location, participantIdentifier, documentIdentifier);
        ServiceMetadata serviceMetadata = metadataReader.parseServiceMetadata(metadataFetcher.fetch(provider));
        return serviceMetadata;
    }
}
