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
 * @author Erlend Klakegg Bergheim - erlend.klakegg.bergheim@difi.no
 *
 */
package eu.europa.ec.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.core.fetcher.IMetadataFetcher;
import eu.europa.ec.dynamicdiscovery.core.fetcher.URLFetcher;
import eu.europa.ec.dynamicdiscovery.core.locator.BDXRLocator;
import eu.europa.ec.dynamicdiscovery.core.locator.IMetadataLocator;
import eu.europa.ec.dynamicdiscovery.core.provider.DefaultProvider;
import eu.europa.ec.dynamicdiscovery.core.provider.IMetadataProvider;
import eu.europa.ec.dynamicdiscovery.core.reader.BdxrReader;
import eu.europa.ec.dynamicdiscovery.core.reader.IMetadataReader;
import eu.europa.ec.dynamicdiscovery.exception.ConnectionException;
import eu.europa.ec.dynamicdiscovery.service.DynamicDiscoveryService;
import eu.europa.ec.dynamicdiscovery.service.IDynamicDiscoveryService;

public class DynamicDiscoveryBuilder {

    private IMetadataFetcher metadataFetcher;
    private IMetadataLocator metadataLocator;
    private IMetadataProvider metadataProvider;
    private IMetadataReader metadataReader;
    private IDynamicDiscoveryService service;

    public DynamicDiscoveryBuilder() {
    }

    public static DynamicDiscoveryBuilder newInstance() {
        return new DynamicDiscoveryBuilder();
    }

    public DynamicDiscoveryBuilder fetcher(IMetadataFetcher metadataFetcher) {
        this.metadataFetcher = metadataFetcher;
        return this;
    }

    public DynamicDiscoveryBuilder locator(IMetadataLocator metadataLocator) {
        this.metadataLocator = metadataLocator;
        return this;
    }

    public DynamicDiscoveryBuilder service(IDynamicDiscoveryService service) {
        this.service = service;
        return this;
    }

    public DynamicDiscoveryBuilder provider(IMetadataProvider metadataProvider) {
        this.metadataProvider = metadataProvider;
        return this;
    }

    public DynamicDiscoveryBuilder reader(IMetadataReader metadataReader) {
        this.metadataReader = metadataReader;
        return this;
    }

    public DynamicDiscovery build() throws ConnectionException {
        if (this.metadataLocator == null) {
            throw new IllegalStateException("Locator not defined.");
        } else {
            if (this.metadataFetcher == null) {
                this.fetcher(new URLFetcher());
            }

            if (this.metadataProvider == null) {
                this.provider(new DefaultProvider());
            }

            if (this.metadataReader == null) {
                this.reader(new BdxrReader());
            }

            if (this.service == null) {
                this.service(new DynamicDiscoveryService(this.metadataLocator, this.metadataProvider, this.metadataFetcher, this.metadataReader));
            } else {
                this.service.build(this.metadataLocator, this.metadataProvider, this.metadataFetcher, this.metadataReader);
            }

            return new DynamicDiscovery(this.service);
        }
    }

}
