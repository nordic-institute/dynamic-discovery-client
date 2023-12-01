/*
 * (C) Copyright 2016-2023 - European Commission | Dynamic Discovery Client
 *
 * https://ec.europa.eu/digital-building-blocks/code/projects/EDELIVERY/repos/dynamic-discovery-client/browse
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
package eu.europa.ec.dynamicdiscovery.service;

import eu.europa.ec.dynamicdiscovery.core.fetcher.IMetadataFetcher;
import eu.europa.ec.dynamicdiscovery.core.locator.IMetadataLocator;
import eu.europa.ec.dynamicdiscovery.core.provider.IMetadataProvider;
import eu.europa.ec.dynamicdiscovery.core.reader.IMetadataReader;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceGroup;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;

/**
 * @author Flávio W. R. Santos
 */
public interface IDynamicDiscoveryService {

    SMPServiceGroup getServiceGroup(SMPParticipantIdentifier participantIdentifier) throws TechnicalException;

    SMPServiceMetadata getServiceMetadata(SMPParticipantIdentifier participantIdentifier, SMPDocumentIdentifier documentIdentifier) throws TechnicalException;

    void setMetadataLocator(IMetadataLocator metadataLocator);

    void setMetadataProvider(IMetadataProvider metadataProvider);

    void setMetadataFetcher(IMetadataFetcher metadataFetcher);

    void setMetadataReader(IMetadataReader metadataReader);

    IMetadataLocator getMetadataLocator();

    IMetadataProvider getMetadataProvider();

    IMetadataFetcher getMetadataFetcher();

    IMetadataReader getMetadataReader();
}
