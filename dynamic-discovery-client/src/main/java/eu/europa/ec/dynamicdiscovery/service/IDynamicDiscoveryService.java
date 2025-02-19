package eu.europa.ec.dynamicdiscovery.service;

import eu.europa.ec.dynamicdiscovery.core.fetcher.IDocumentFetcher;
import eu.europa.ec.dynamicdiscovery.core.locator.IPublisherLocator;
import eu.europa.ec.dynamicdiscovery.core.provider.IDocumentRequestProvider;
import eu.europa.ec.dynamicdiscovery.core.reader.ISMPDocumentReader;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;


/**
 * Main interface for the Dynamic Discovery Service. The implementation of the
 * interface  is responsible for discovering the resource and subresource based on identifiers.
 *
 *
 * @author Flávio W. R. SANTOS
 * @author Joze RIHTARSIC
 * @since 1.0
 */
public interface IDynamicDiscoveryService<R, S> {
    R getResource(SMPParticipantIdentifier resourceIdentifier) throws TechnicalException;

    S getSubresource(SMPParticipantIdentifier resourceIdentifier, SMPDocumentIdentifier subresourceIdentifier) throws TechnicalException;

    IPublisherLocator getPublisherLocator();

    IDocumentRequestProvider getDocumentRequestProvider();

    IDocumentFetcher getDocumentFetcher();

    ISMPDocumentReader getDocumentReader();
}
