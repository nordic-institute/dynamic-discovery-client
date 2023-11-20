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
        if (this.service.getMetadataProvider() == null) {
            throw new IllegalStateException("MetadataProvider not defined.");
        }

        return new DynamicDiscovery(this.service);
    }

    public IDynamicDiscoveryService getService() {
        return service;
    }
}
