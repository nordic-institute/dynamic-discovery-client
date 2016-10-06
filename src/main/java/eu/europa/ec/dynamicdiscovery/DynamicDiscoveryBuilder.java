package eu.europa.ec.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.exception.ConnectionException;
import eu.europa.ec.dynamicdiscovery.fetcher.IMetadataFetcher;
import eu.europa.ec.dynamicdiscovery.fetcher.URLFetcher;
import eu.europa.ec.dynamicdiscovery.locator.BusdoxLocator;
import eu.europa.ec.dynamicdiscovery.locator.IMetadataLocator;
import eu.europa.ec.dynamicdiscovery.provider.DefaultProvider;
import eu.europa.ec.dynamicdiscovery.provider.IMetadataProvider;
import eu.europa.ec.dynamicdiscovery.reader.IMetadataReader;
import eu.europa.ec.dynamicdiscovery.reader.MultiReader;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 * @author Erlend Klakegg Bergheim - erlend.klakegg.bergheim@difi.no
 */
public class DynamicDiscoveryBuilder {


    public DynamicDiscoveryBuilder() {
    }

    private IMetadataFetcher metadataFetcher;
    private IMetadataLocator metadataLocator;
    private IMetadataProvider metadataProvider;
    private IMetadataReader metadataReader;

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
                this.reader(new MultiReader());
            }

            return new DynamicDiscovery(this.metadataLocator, this.metadataProvider, this.metadataFetcher, this.metadataReader);
        }
    }

}
