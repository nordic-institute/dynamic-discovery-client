/*
 * Copyright 2016 Dynamic Discovery Client Project
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/servlets/Docbb6d.pdf?id=31979
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
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

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 * @author Erlend Klakegg Bergheim - erlend.klakegg.bergheim@difi.no
 */
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
