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

import eu.europa.ec.dynamicdiscovery.core.locator.PublisherLookupResult;
import eu.europa.ec.dynamicdiscovery.core.provider.IDocumentRequestProvider;
import eu.europa.ec.dynamicdiscovery.core.provider.PublisherRequest;
import eu.europa.ec.dynamicdiscovery.exception.DDCInvalidDataException;
import eu.europa.ec.dynamicdiscovery.model.identifiers.DocumentIdentifierFormatter;
import eu.europa.ec.dynamicdiscovery.model.identifiers.ParticipantIdentifierFormatter;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

import static org.apache.commons.lang3.StringUtils.prependIfMissing;

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
    private static final String URL_PATH_SEPARATOR = "/";

    private static final Logger LOG = LoggerFactory.getLogger(DefaultDocumentRequestProvider.class);

    ParticipantIdentifierFormatter participantIdentifierFormatter;
    DocumentIdentifierFormatter documentIdentifierFormatter;

    protected DefaultDocumentRequestProvider(Builder builder) {
        this.participantIdentifierFormatter = builder.resourceIdentifierFormatter;
        this.documentIdentifierFormatter = builder.subresourceIdentifierFormatter;
    }

    /**
     * Method builds publisher URL from provided parameters. If the publisherURI
     * ends with the contextPath, then the contextPath is not appended.
     *
     * @param publisher       - the publisher lookup result.
     * @param resourceContext - the context path of the resource.
     * @param identifier      the resource/participant  identifying the document REST resource.
     * @return URI of the document REST resource.
     */
    @Override
    public PublisherRequest createRequestForResource(PublisherLookupResult publisher, String resourceContext, SMPParticipantIdentifier identifier) {
        LOG.debug("Creating request for resource with identifier [{}] and context [{}]", identifier, resourceContext);
        String encodedIdentifier = identifier == null ? "" : participantIdentifierFormatter.urlEncodedFormat(identifier);
        return createRequest(publisher, resourceContext, encodedIdentifier);
    }

    /**
     * Method builds HTTP GET request, where context and identifier are prepend to baseURI.
     *
     * @param publisherData     - the publisher lookup result.
     * @param resourceContext   - the context path of the resource.
     * @param encodedIdentifier the URL encoded identifier. .
     * @return URI of the document REST resource.
     * @throws DDCInvalidDataException if the baseURI is null or the identifier is null or empty.
     */
    protected PublisherRequest createRequest(PublisherLookupResult publisherData, String resourceContext, String encodedIdentifier) {
        if (publisherData == null || publisherData.getUrl() == null) {
            throw new DDCInvalidDataException("Base URI cannot be null");
        }
        URI baseURI = publisherData.getUrl();
        if (StringUtils.isBlank(encodedIdentifier)) {
            throw new DDCInvalidDataException("Identifier cannot be null or empty");
        }
        URI result = createRequestURI(baseURI, resourceContext, encodedIdentifier);
        return new PublisherRequest(result, publisherData);
    }

    /**
     * Method builds URI with adding  context and identifier to the baseURI.
     *
     * @param baseURI           - the base URI
     * @param resourceContext   - the context path of the resource.
     * @param encodedIdentifier - the URL encoded identifier.
     * @return
     */
    protected URI createRequestURI(URI baseURI, String resourceContext, String encodedIdentifier) {
        String contextPath = StringUtils.isBlank(resourceContext) ?
                URL_PATH_SEPARATOR : prependIfMissing(StringUtils.removeEnd(resourceContext, URL_PATH_SEPARATOR), URL_PATH_SEPARATOR);
        String baseURIPath = StringUtils.removeEnd(baseURI.getRawPath(), URL_PATH_SEPARATOR);
        URI result = StringUtils.endsWith(baseURIPath, contextPath) ? baseURI :
                baseURI.resolve(baseURIPath + contextPath);
        return result.resolve(result + prependIfMissing(encodedIdentifier, URL_PATH_SEPARATOR)).normalize();
    }

    @Override
    public PublisherRequest createRequestForSubresource(PublisherLookupResult publisherData,
                                                        String resourceContext, SMPParticipantIdentifier participantIdentifier,
                                                        String subresourceContext, SMPDocumentIdentifier documentIdentifier) {

        if (documentIdentifier == null) {
            throw new DDCInvalidDataException("Can not create publisher request for subresource for null identifier");
        }

        PublisherRequest resourceRequest = createRequestForResource(publisherData, resourceContext, participantIdentifier);
        return createRequestForSubresource(resourceRequest, subresourceContext, documentIdentifier);
    }


    @Override
    public PublisherRequest createRequestForSubresource(PublisherRequest resourceRequest,
                                                        String subresourceContext,
                                                        SMPDocumentIdentifier documentIdentifier) {

        LOG.debug("Creating request for subresource with identifier [{}] and subresource context [{}]", documentIdentifier, subresourceContext);
        if (documentIdentifier == null) {
            throw new DDCInvalidDataException("Can not create publisher request for subresource for null identifier");
        }
        //get with exact match
        String encodedIdentifier = documentIdentifierFormatter.urlEncodedFormat(documentIdentifier);
        URI result = createRequestURI(resourceRequest.getResourceUri(), subresourceContext, encodedIdentifier);
        resourceRequest.setSubresourceUri(result);
        resourceRequest.setSubresourceIdentifier(documentIdentifier);
        return resourceRequest;
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


    /**
     * The builder is responsible for creating  the instance of the
     * {@link DefaultDocumentRequestProvider} with the provided configuration.
     * If the identifiers formatters are not provided, the default ones will be used.
     */
    public static class Builder {


        protected ParticipantIdentifierFormatter resourceIdentifierFormatter;
        protected DocumentIdentifierFormatter subresourceIdentifierFormatter;

        public DefaultDocumentRequestProvider.Builder resourceIdentifierFormatter(ParticipantIdentifierFormatter resourceIdentifierFormatter) {
            this.resourceIdentifierFormatter = resourceIdentifierFormatter;
            return this;
        }

        public DefaultDocumentRequestProvider.Builder subresourceIdentifierFormatter(DocumentIdentifierFormatter subresourceIdentifierFormatter) {
            this.subresourceIdentifierFormatter = subresourceIdentifierFormatter;
            return this;
        }

        public DefaultDocumentRequestProvider build() {
            // validate the configuration
            validate();

            return new DefaultDocumentRequestProvider(this);
        }

        private void validate() {
            //if no formatters are provided, use the default ones
            if (this.resourceIdentifierFormatter == null) {
                this.resourceIdentifierFormatter = new ParticipantIdentifierFormatter();
            }
            if (this.subresourceIdentifierFormatter == null) {
                this.subresourceIdentifierFormatter = new DocumentIdentifierFormatter();
            }
        }
    }
}
