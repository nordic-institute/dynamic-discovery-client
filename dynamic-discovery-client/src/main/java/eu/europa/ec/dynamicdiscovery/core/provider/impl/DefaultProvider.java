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
import eu.europa.ec.dynamicdiscovery.core.fetcher.IMetadataFetcher;
import eu.europa.ec.dynamicdiscovery.core.provider.IMetadataProvider;
import eu.europa.ec.dynamicdiscovery.core.provider.WildcardUtil;
import eu.europa.ec.dynamicdiscovery.core.reader.IMetadataReader;
import eu.europa.ec.dynamicdiscovery.exception.DDCInvalidConfigurationException;
import eu.europa.ec.dynamicdiscovery.exception.SMPExceptionCode;
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
 * Default implementation of the {@link IMetadataProvider} interface. This implementation is responsible for resolving
 * the SMP query URIs for a given identifiers.
 *
 * @author Flávio W. R. Santos
 * @author Erlend Klakegg Bergheim
 * @since 1.0
 */
public class DefaultProvider implements IMetadataProvider {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultProvider.class);

    ParticipantIdentifierFormatter participantIdentifierFormatter = new ParticipantIdentifierFormatter();
    DocumentIdentifierFormatter documentIdentifierFormatter = new DocumentIdentifierFormatter();

    WildcardUtil wildcardUtil = new WildcardUtil();

    protected IMetadataFetcher metadataFetcher;
    protected IMetadataReader metadataReader;
    protected List<String> wildcardSchemes;

    protected DefaultProvider(Builder builder) {
        this.metadataFetcher = builder.metadataFetcher;
        this.metadataReader = builder.metadataReader;
        this.wildcardSchemes = builder.wildcardSchemes;
    }

    @Override
    public URI resolveForParticipantIdentifier(URI smpURI, SMPParticipantIdentifier participantIdentifier) {
        String participantPathParameter = participantIdentifierFormatter.urlEncodedFormat(participantIdentifier);
        return URI.create(smpURI.toString() + String.format("/%s", participantPathParameter)).normalize();
    }

    @Override
    public URI resolveServiceMetadata(URI smpURI, SMPParticipantIdentifier participantIdentifier, SMPDocumentIdentifier documentIdentifier) throws TechnicalException {
        if (wildcardUtil.isWildcardScheme(wildcardSchemes, documentIdentifier.getScheme())) {
            //get with wildcard match
            return getDocumentIdentifierWithWildcardMatch(smpURI, participantIdentifier, documentIdentifier);
        }

        //get with exact match
        return getDocumentIdentifierWithExactMatch(smpURI, participantIdentifier, documentIdentifier);
    }


    protected URI getDocumentIdentifierWithExactMatch(URI smpURI, SMPParticipantIdentifier participantIdentifier, SMPDocumentIdentifier documentIdentifier) {
        LOG.debug("Getting document identifier URI with exact match");

        String participantPathParameter = participantIdentifierFormatter.urlEncodedFormat(participantIdentifier);
        String documentPathParameter = documentIdentifierFormatter.urlEncodedFormat(documentIdentifier);
        final URI uriNormalized = URI.create(smpURI.toString() + String.format("/%s/services/%s", participantPathParameter, documentPathParameter)).normalize();

        LOG.debug("Retrieved document identifier URI with exact match [{}]", uriNormalized);
        return uriNormalized;
    }

    protected URI getDocumentIdentifierWithWildcardMatch(URI smpURI, SMPParticipantIdentifier participantIdentifier, SMPDocumentIdentifier documentIdentifier) throws TechnicalException {
        LOG.debug("Getting document identifier URI with wildcard match");

        URI participantUnderSmpURI = resolveForParticipantIdentifier(smpURI, participantIdentifier);
        LOG.info("Get participant data / documents for URI: [{}].", participantUnderSmpURI);
        final FetcherResponse fetcherResponse = metadataFetcher.fetch(participantUnderSmpURI);
        final SMPServiceGroup serviceGroup = metadataReader.getServiceGroup(fetcherResponse);

        //the document identifiers supported by the participant
        final List<SMPDocumentIdentifier> discoveredDocumentIdentifiers = serviceGroup.getDocumentIdentifiers();

        final SMPDocumentIdentifier discoveredWildcardDocumentIdentifier = getSmpDocumentIdentifierWithWildcardSchemeUsingExactOrLongestMatch(discoveredDocumentIdentifiers, participantIdentifier, documentIdentifier);
        if (discoveredWildcardDocumentIdentifier != null) {
            LOG.debug("Found SMPDocumentIdentifier wildcard match [{}] for participant [{}] and document identifier [{}]. Fetching from SMP", discoveredWildcardDocumentIdentifier, participantIdentifier, documentIdentifier);
            return getDocumentIdentifierWithExactMatch(smpURI, participantIdentifier, discoveredWildcardDocumentIdentifier);
        }
        throw new SMPServiceMetadataException(SMPExceptionCode.SERVICE_METADATA, "Could not find SMPServiceMetadata for participant [" + participantIdentifier + "] and document identifier [" + documentIdentifier + "]");
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
        return participantIdentifierFormatter.format(identifier);
    }

    public String urlEncodedFormat(SMPParticipantIdentifier identifier) {
        return participantIdentifierFormatter.urlEncodedFormat(identifier);
    }

    public String format(SMPDocumentIdentifier identifier) {
        return documentIdentifierFormatter.format(identifier);
    }

    public String urlEncodedFormat(SMPDocumentIdentifier identifier) {
        return documentIdentifierFormatter.urlEncodedFormat(identifier);
    }

    public IMetadataFetcher getMetadataFetcher() {
        return metadataFetcher;
    }

    public List<String> getWildcardSchemes() {
        return wildcardSchemes;
    }

    public void setWildcardSchemes(List<String> wildcardSchemes) {
        this.wildcardSchemes = wildcardSchemes;
    }

    public IMetadataReader getMetadataReader() {
        return metadataReader;
    }


    public static class Builder {

        protected IMetadataFetcher metadataFetcher;
        protected IMetadataReader metadataReader;
        protected List<String> wildcardSchemes;

        public DefaultProvider.Builder metadataFetcher(IMetadataFetcher metadataFetcher) {
            this.metadataFetcher = metadataFetcher;
            return this;
        }

        public DefaultProvider.Builder metadataReader(IMetadataReader metadataReader) {
            this.metadataReader = metadataReader;
            return this;
        }

        public DefaultProvider.Builder wildcardSchemes(List<String> wildcardSchemes) {
            this.wildcardSchemes = wildcardSchemes;
            return this;
        }

        public DefaultProvider build() {
            if (this.wildcardSchemes != null && !this.wildcardSchemes.isEmpty()) {
                if (this.metadataReader == null) {
                    throw new DDCInvalidConfigurationException("IMetadataReader is mandatory with use of the wildcardSchemes!");
                }
                if (this.metadataFetcher == null) {
                    throw new DDCInvalidConfigurationException("IMetadataFetcher is mandatory with use of the wildcardSchemes");
                }
            }
            return new DefaultProvider(this);
        }

    }

}
