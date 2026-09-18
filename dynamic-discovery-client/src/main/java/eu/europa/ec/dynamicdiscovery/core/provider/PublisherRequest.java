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
package eu.europa.ec.dynamicdiscovery.core.provider;

import eu.europa.ec.dynamicdiscovery.core.locator.PublisherLookupResult;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;

import java.net.URI;
import java.util.StringJoiner;

/**
 * The class contains result of the metadata locator.
 *
 * @author Joze RIHTARSIC
 * @since 3.0
 */
public class PublisherRequest {

    PublisherLookupResult publisherLookupData;
    SMPParticipantIdentifier resourceIdentifier;
    SMPDocumentIdentifier subresourceIdentifier;
    URI resourceUri;
    URI subresourceUri;

    /**
     * Constructor which sets the publisher Resource/Subresource URL with the PublisherLookupResult . The DNS lookup type is set to NAPTR.
     *
     * @param resourceUri          The URL of the resource/subresource of the publisher
     * @param publisherLookupData The result of the metadata locator
     */
    public PublisherRequest(URI resourceUri, PublisherLookupResult publisherLookupData) {
        this.resourceUri = resourceUri;
        this.publisherLookupData = publisherLookupData;
        this.resourceIdentifier = publisherLookupData == null ? null : publisherLookupData.getResourceIdentifier();
    }

    public PublisherLookupResult getPublisherLookupData() {
        return publisherLookupData;
    }

    public URI getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(URI subresourceUri) {
        this.subresourceUri = subresourceUri;
    }

    public SMPParticipantIdentifier getResourceIdentifier() {
        return resourceIdentifier;
    }

    public URI getSubresourceUri() {
        return subresourceUri;
    }

    public void setSubresourceUri(URI subresourceUri) {
        this.subresourceUri = subresourceUri;
    }

    public SMPDocumentIdentifier getSubresourceIdentifier() {
        return subresourceIdentifier;
    }

    public void setSubresourceIdentifier(SMPDocumentIdentifier subresourceIdentifier) {
        this.subresourceIdentifier = subresourceIdentifier;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PublisherRequest.class.getSimpleName() + "[", "]")
                .add("publisherLookupData=" + publisherLookupData)
                .add("resourceIdentifier=" + resourceIdentifier)
                .add("subresourceIdentifier=" + subresourceIdentifier)
                .add("resourceUri=" + resourceUri)
                .add("subresourceUri=" + subresourceUri)
                .toString();
    }
}
