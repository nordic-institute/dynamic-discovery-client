package eu.europa.ec.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.core.fetcher.IMetadataFetcher;
import eu.europa.ec.dynamicdiscovery.core.fetcher.URLFetcher;
import eu.europa.ec.dynamicdiscovery.core.locator.BusdoxLocator;
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

    public static DynamicDiscoveryBuilder forProduction() {
        return newInstance().locator(new BusdoxLocator("edelivery.tech.ec.europa.eu"));
    }

    public static DynamicDiscoveryBuilder forTest() {
        return newInstance().locator(new BusdoxLocator("acc.edelivery.tech.ec.europa.eu"));
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
