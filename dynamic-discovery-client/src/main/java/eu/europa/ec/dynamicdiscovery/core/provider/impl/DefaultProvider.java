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

package eu.europa.ec.dynamicdiscovery.core.provider.impl;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.fetcher.IMetadataFetcher;
import eu.europa.ec.dynamicdiscovery.core.provider.IMetadataProvider;
import eu.europa.ec.dynamicdiscovery.core.provider.WildcardUtil;
import eu.europa.ec.dynamicdiscovery.core.reader.IMetadataReader;
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

        final SMPDocumentIdentifier wildcardDocumentIdentifierWithLongestMatch = wildcardUtil.getWildcardDocumentIdentifierWithLongestMatch(discoveredDocumentIdentifiers, documentIdentifier);
        if (wildcardDocumentIdentifierWithLongestMatch != null) {
            LOG.debug("Found SMPDocumentIdentifier wildcard match [{}] for participant [{}] and document identifier [{}]. Fetching from SMP", participantIdentifier, wildcardDocumentIdentifierWithLongestMatch);
            return getDocumentIdentifierWithExactMatch(smpURI, participantIdentifier, wildcardDocumentIdentifierWithLongestMatch);
        }
        throw new SMPServiceMetadataException(SMPExceptionCode.SERVICE_METADATA, "Could not find SMPServiceMetadata for participant [" + participantIdentifier + "] and document identifier [" + documentIdentifier + "]");
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

    public void setMetadataFetcher(IMetadataFetcher iMetadataFetcher) {
        this.metadataFetcher = iMetadataFetcher;
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

    public void setMetadataReader(IMetadataReader metadataReader) {
        this.metadataReader = metadataReader;
    }

    public static class Builder {

        protected IMetadataFetcher metadataFetcher;
        protected IMetadataReader metadataReader;
        protected List<String> wildcardSchemes;

        private IMetadataProvider metadataProvider;

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
            if (this.metadataReader == null) {
                throw new IllegalStateException("MetadataReader not defined.");
            }
            if (this.metadataFetcher == null) {
                throw new IllegalStateException("MetadataFetcher not defined.");
            }

            final DefaultProvider defaultProvider = new DefaultProvider();
            defaultProvider.setMetadataReader(metadataReader);
            defaultProvider.setMetadataFetcher(metadataFetcher);
            defaultProvider.setWildcardSchemes(wildcardSchemes);
            return defaultProvider;
        }

    }

}
