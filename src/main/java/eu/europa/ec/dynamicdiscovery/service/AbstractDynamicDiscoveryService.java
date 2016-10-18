package eu.europa.ec.dynamicdiscovery.service;

import eu.europa.ec.dynamicdiscovery.core.fetcher.IMetadataFetcher;
import eu.europa.ec.dynamicdiscovery.core.locator.IMetadataLocator;
import eu.europa.ec.dynamicdiscovery.core.provider.IMetadataProvider;
import eu.europa.ec.dynamicdiscovery.core.reader.IMetadataReader;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public abstract class AbstractDynamicDiscoveryService implements IDynamicDiscoveryService {

    protected IMetadataLocator metadataLocator;
    protected IMetadataProvider metadataProvider;
    protected IMetadataFetcher metadataFetcher;
    protected IMetadataReader metadataReader;

    public void build(IMetadataLocator metadataLocator, IMetadataProvider metadataProvider, IMetadataFetcher metadataFetcher, IMetadataReader metadataReader) {
        this.metadataLocator = metadataLocator;
        this.metadataProvider = metadataProvider;
        this.metadataFetcher = metadataFetcher;
        this.metadataReader = metadataReader;
    }
}
