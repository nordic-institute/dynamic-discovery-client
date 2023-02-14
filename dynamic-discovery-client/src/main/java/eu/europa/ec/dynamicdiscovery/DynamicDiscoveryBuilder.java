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
 *
 */
package eu.europa.ec.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.core.fetcher.IMetadataFetcher;
import eu.europa.ec.dynamicdiscovery.core.locator.IMetadataLocator;
import eu.europa.ec.dynamicdiscovery.core.provider.IMetadataProvider;
import eu.europa.ec.dynamicdiscovery.core.reader.IMetadataReader;
import eu.europa.ec.dynamicdiscovery.service.IDynamicDiscoveryService;
import eu.europa.ec.dynamicdiscovery.service.impl.DynamicDiscoveryService;

/**
 * @author Flávio W. R. Santos
 * @author Erlend Klakegg Bergheim
 */
public class DynamicDiscoveryBuilder {

    private IDynamicDiscoveryService service;

    public DynamicDiscoveryBuilder() {
        this.service = new DynamicDiscoveryService();
    }

    public static DynamicDiscoveryBuilder newInstance() {
        return new DynamicDiscoveryBuilder();
    }

    public DynamicDiscoveryBuilder fetcher(IMetadataFetcher metadataFetcher) {
        this.service.setMetadataFetcher(metadataFetcher);
        return this;
    }

    public DynamicDiscoveryBuilder locator(IMetadataLocator metadataLocator) {
        this.service.setMetadataLocator(metadataLocator);
        return this;
    }

    public DynamicDiscoveryBuilder provider(IMetadataProvider metadataProvider) {
        this.service.setMetadataProvider(metadataProvider);
        return this;
    }

    public DynamicDiscoveryBuilder reader(IMetadataReader metadataReader) {
        this.service.setMetadataReader(metadataReader);
        return this;
    }

    public DynamicDiscovery build() {
        if (this.service.getMetadataLocator() == null) {
            throw new IllegalStateException("MetadataLocator not defined.");
        }

        if (this.service.getMetadataReader() == null) {
            throw new IllegalStateException("MetadataReader not defined.");
        }

        return new DynamicDiscovery(this.service);
    }

    public IDynamicDiscoveryService getService() {
        return service;
    }
}
