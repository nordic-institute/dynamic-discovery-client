/*
 * #%L
 * dynamic-discovery-cli
 * %%
 * Copyright (C) 2016 - 2023 European Commission | eDelivery | Dynamic Discovery Client
 * %%
 * Licensed under the LGPL, Version 2.1 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * [PROJECT_HOME]\license\lgpl2-1\license.txt or https://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package eu.europa.ec.dynamicdiscovery.core.provider.impl;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.fetcher.IDocumentFetcher;
import eu.europa.ec.dynamicdiscovery.core.provider.IDocumentRequestProvider;
import eu.europa.ec.dynamicdiscovery.core.provider.WildcardUtil;
import eu.europa.ec.dynamicdiscovery.core.reader.ISMPDocumentReader;
import eu.europa.ec.dynamicdiscovery.exception.DDCInvalidConfigurationException;
import eu.europa.ec.dynamicdiscovery.exception.DDCExceptionCode;
import eu.europa.ec.dynamicdiscovery.exception.SMPServiceMetadataException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceGroup;
import eu.europa.ec.dynamicdiscovery.model.identifiers.DocumentIdentifierFormatter;
import eu.europa.ec.dynamicdiscovery.model.identifiers.ParticipantIdentifierFormatter;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;

/**
 * Default implementation of the {@link IDocumentRequestProvider} interface. This implementation is responsible
 * for building document request e.g:  SMP HTTP get query (URI) for a given publisher lookup result.
 * The default request builder is compliant with the eDelivery SMP 1.x/2.x profile.
 * It additionally supports wildcard subresource (e.g. ServiceMetadata) schemes
 * as defined in PEPPOL technical specification.
 *
 * @author Flávio W. R. SANTOS
 * @author Erlend KLAKEGG BERGHEIM
 * @author Cosmin BACIU
 * @author Joze RIHTARSIC
 * @since 1.0
 */
public class DefaultDocumentRequestProvider implements IDocumentRequestProvider {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultDocumentRequestProvider.class);

    ParticipantIdentifierFormatter resourceIdentifierFormatter = new ParticipantIdentifierFormatter();
    DocumentIdentifierFormatter subresourceIdentifierFormatter = new DocumentIdentifierFormatter();

    WildcardUtil wildcardUtil = new WildcardUtil();

    protected IDocumentFetcher documentFetcher;
    protected ISMPDocumentReader documentReader;
    protected List<String> wildcardSchemes;

    protected DefaultDocumentRequestProvider(Builder builder) {
        this.documentFetcher = builder.documentFetcher;
        this.documentReader = builder.documentReader;
        this.wildcardSchemes = builder.wildcardSchemes;
    }

    @Override
    public URI createRequestForResource(URI smpURI, SMPParticipantIdentifier participantIdentifier) {
        String participantPathParameter = resourceIdentifierFormatter.urlEncodedFormat(participantIdentifier);
        return URI.create(smpURI.toString() + String.format("/%s", participantPathParameter)).normalize();
    }

    @Override
    public URI createRequestForSubresource(URI smpURI, SMPParticipantIdentifier participantIdentifier, SMPDocumentIdentifier documentIdentifier) throws TechnicalException {
        if (wildcardUtil.isWildcardScheme(wildcardSchemes, documentIdentifier.getScheme())) {
            //get with wildcard match
            return getDocumentIdentifierWithWildcardMatch(smpURI, participantIdentifier, documentIdentifier);
        }

        //get with exact match
        return getDocumentIdentifierWithExactMatch(smpURI, participantIdentifier, documentIdentifier);
    }


    protected URI getDocumentIdentifierWithExactMatch(URI smpURI, SMPParticipantIdentifier participantIdentifier, SMPDocumentIdentifier documentIdentifier) {
        LOG.debug("Getting document identifier URI with exact match");

        String participantPathParameter = resourceIdentifierFormatter.urlEncodedFormat(participantIdentifier);
        String documentPathParameter = subresourceIdentifierFormatter.urlEncodedFormat(documentIdentifier);
        final URI uriNormalized = URI.create(smpURI.toString() + String.format("/%s/services/%s", participantPathParameter, documentPathParameter)).normalize();

        LOG.debug("Retrieved document identifier URI with exact match [{}]", uriNormalized);
        return uriNormalized;
    }

    protected URI getDocumentIdentifierWithWildcardMatch(URI smpURI, SMPParticipantIdentifier participantIdentifier, SMPDocumentIdentifier documentIdentifier) throws TechnicalException {
        LOG.debug("Getting document identifier URI with wildcard match");

        URI participantUnderSmpURI = createRequestForResource(smpURI, participantIdentifier);
        LOG.info("Get participant data / documents for URI: [{}].", participantUnderSmpURI);
        final FetcherResponse fetcherResponse = documentFetcher.fetch(participantUnderSmpURI);
        final SMPServiceGroup serviceGroup = documentReader.getResource(fetcherResponse);

        //the document identifiers supported by the participant
        final List<SMPDocumentIdentifier> discoveredDocumentIdentifiers = serviceGroup.getDocumentIdentifiers();

        final SMPDocumentIdentifier discoveredWildcardDocumentIdentifier = getSmpDocumentIdentifierWithWildcardSchemeUsingExactOrLongestMatch(discoveredDocumentIdentifiers, participantIdentifier, documentIdentifier);
        if (discoveredWildcardDocumentIdentifier != null) {
            LOG.debug("Found SMPDocumentIdentifier wildcard match [{}] for participant [{}] and document identifier [{}]. Fetching from SMP", discoveredWildcardDocumentIdentifier, participantIdentifier, documentIdentifier);
            return getDocumentIdentifierWithExactMatch(smpURI, participantIdentifier, discoveredWildcardDocumentIdentifier);
        }
        throw new SMPServiceMetadataException(DDCExceptionCode.SERVICE_METADATA, "Could not find SMPServiceMetadata for participant [" + participantIdentifier + "] and document identifier [" + documentIdentifier + "]");
    }

    protected SMPDocumentIdentifier getSmpDocumentIdentifierWithWildcardSchemeUsingExactOrLongestMatch(List<SMPDocumentIdentifier> discoveredDocumentIdentifiers,
                                                                                                       SMPParticipantIdentifier participantIdentifier,
                                                                                                       SMPDocumentIdentifier documentIdentifierToCheck) {
        final SMPDocumentIdentifier wildcardDocumentIdentifierWithExactMatch = wildcardUtil.getWildcardDocumentIdentifierWithExactMatch(discoveredDocumentIdentifiers, documentIdentifierToCheck);
        if (wildcardDocumentIdentifierWithExactMatch != null) {
            LOG.debug("Found SMPDocumentIdentifier wildcard scheme with exact match [{}] for participant [{}] and document identifier [{}].", wildcardDocumentIdentifierWithExactMatch, participantIdentifier, documentIdentifierToCheck);
            return wildcardDocumentIdentifierWithExactMatch;
        }

        final SMPDocumentIdentifier wildcardDocumentIdentifierWithLongestMatch = wildcardUtil.getWildcardDocumentIdentifierWithLongestMatch(discoveredDocumentIdentifiers, documentIdentifierToCheck);
        if (wildcardDocumentIdentifierWithLongestMatch != null) {
            LOG.debug("Found SMPDocumentIdentifier wildcard scheme with wildcard match [{}] for participant [{}] and document identifier [{}].", wildcardDocumentIdentifierWithLongestMatch, participantIdentifier, documentIdentifierToCheck);
            return wildcardDocumentIdentifierWithLongestMatch;
        }
        return null;
    }

    public String format(SMPParticipantIdentifier identifier) {
        return resourceIdentifierFormatter.format(identifier);
    }

    public String urlEncodedFormat(SMPParticipantIdentifier identifier) {
        return resourceIdentifierFormatter.urlEncodedFormat(identifier);
    }

    public String format(SMPDocumentIdentifier identifier) {
        return subresourceIdentifierFormatter.format(identifier);
    }

    public String urlEncodedFormat(SMPDocumentIdentifier identifier) {
        return subresourceIdentifierFormatter.urlEncodedFormat(identifier);
    }

    public IDocumentFetcher getDocumentFetcher() {
        return documentFetcher;
    }

    public List<String> getWildcardSchemes() {
        return wildcardSchemes;
    }

    public void setWildcardSchemes(List<String> wildcardSchemes) {
        this.wildcardSchemes = wildcardSchemes;
    }

    public ISMPDocumentReader getDocumentReader() {
        return documentReader;
    }


    public static class Builder {

        protected IDocumentFetcher documentFetcher;
        protected ISMPDocumentReader documentReader;
        protected List<String> wildcardSchemes;

        public DefaultDocumentRequestProvider.Builder documentFetcher(IDocumentFetcher metadataFetcher) {
            this.documentFetcher = metadataFetcher;
            return this;
        }

        public DefaultDocumentRequestProvider.Builder documentReader(ISMPDocumentReader metadataReader) {
            this.documentReader = metadataReader;
            return this;
        }

        public DefaultDocumentRequestProvider.Builder wildcardSchemes(List<String> wildcardSchemes) {
            this.wildcardSchemes = wildcardSchemes;
            return this;
        }

        public DefaultDocumentRequestProvider build() {
            if (this.wildcardSchemes != null && !this.wildcardSchemes.isEmpty()) {
                if (this.documentReader == null) {
                    throw new DDCInvalidConfigurationException("IMetadataReader is mandatory with use of the wildcardSchemes!");
                }
                if (this.documentFetcher == null) {
                    throw new DDCInvalidConfigurationException("IMetadataFetcher is mandatory with use of the wildcardSchemes");
                }
            }
            return new DefaultDocumentRequestProvider(this);
        }

    }

}
