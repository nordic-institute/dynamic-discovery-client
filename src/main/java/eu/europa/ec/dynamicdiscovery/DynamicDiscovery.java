package eu.europa.ec.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.fetcher.IMetadataFetcher;
import eu.europa.ec.dynamicdiscovery.locator.IMetadataLocator;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.provider.IMetadataProvider;
import eu.europa.ec.dynamicdiscovery.reader.IMetadataReader;

import java.net.URI;
import java.util.List;

/**
 * Created by rodrfla on 30/09/2016.
 */
public class DynamicDiscovery {
    //private static Logger logger = LoggerFactory.getLogger(LookupClient.class);
    private IMetadataLocator metadataLocator;
    private IMetadataProvider metadataProvider;
    private IMetadataFetcher metadataFetcher;
    private IMetadataReader metadataReader;

    DynamicDiscovery(IMetadataLocator metadataLocator, IMetadataProvider metadataProvider, IMetadataFetcher metadataFetcher, IMetadataReader metadataReader) {
        this.metadataLocator = metadataLocator;
        this.metadataProvider = metadataProvider;
        this.metadataFetcher = metadataFetcher;
        this.metadataReader = metadataReader;
    }

    public List<DocumentIdentifier> getDocumentIdentifiers(ParticipantIdentifier participantIdentifier) throws Exception {
        URI location = this.metadataLocator.lookup(participantIdentifier);
        URI provider = this.metadataProvider.resolveDocumentIdentifiers(location, participantIdentifier);
      //  logger.debug("{}", provider);
        return this.metadataReader.parseDocumentIdentifiers(this.metadataFetcher.fetch(provider));
    }

    public ServiceMetadata getServiceMetadata(ParticipantIdentifier participantIdentifier, DocumentIdentifier documentIdentifier) throws Exception, SecurityException {
        URI location = this.metadataLocator.lookup(participantIdentifier);
        URI provider = this.metadataProvider.resolveServiceMetadata(location, participantIdentifier, documentIdentifier);
        //logger.debug("{}", provider);
        ServiceMetadata serviceMetadata = this.metadataReader.parseServiceMetadata(this.metadataFetcher.fetch(provider));
        return serviceMetadata;
    }
}
