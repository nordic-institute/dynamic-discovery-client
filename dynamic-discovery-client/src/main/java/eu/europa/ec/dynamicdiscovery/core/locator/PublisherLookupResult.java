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
package eu.europa.ec.dynamicdiscovery.core.locator;

import eu.europa.ec.dynamicdiscovery.enums.DNSLookupType;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;

import java.net.URI;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * The class contains result of the metadata locator.
 *
 *  @author Joze RIHTARSIC
 *  @since 3.0
 */
public class PublisherLookupResult {
    URI url;
    String serviceType;
    DNSLookupType dnsLookupType;
    SMPParticipantIdentifier resourceIdentifier;


    /**
     * Constructor which sets the URL and the service type. The DNS lookup type is set to NONE.
     * The constructor is used when data is located by static discovery or any other means.
     * @param url The URL of the metadata
     * @param serviceType The service type of the metadata
     */
    public PublisherLookupResult(SMPParticipantIdentifier identifier, URI url, String serviceType) {
        this(identifier, url, serviceType, DNSLookupType.NONE);
    }

    /**
     * Constructor which sets the URL and the service type.
     *
     * @param identifier The target identifier of the resource/participant
     * @param url The result: URL of the resource/participant SMP server
     * @param serviceType The service type of the metadata
     * @param dnsLookupType The DNS lookup type (e.g. NAPTR or CNAME)
     */
    public PublisherLookupResult(SMPParticipantIdentifier identifier, URI url, String serviceType, DNSLookupType dnsLookupType) {
        this.resourceIdentifier = identifier;
        this.url = url;
        this.serviceType = serviceType;
        this.dnsLookupType = dnsLookupType;
    }

    public URI getUrl() {
        return url;
    }

    public String getServiceType() {
        return serviceType;
    }

    public DNSLookupType getDnsLookupType() {
        return dnsLookupType;
    }

    public SMPParticipantIdentifier getResourceIdentifier() {
        return resourceIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PublisherLookupResult that = (PublisherLookupResult) o;
        return Objects.equals(url, that.url) && Objects.equals(serviceType, that.serviceType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, serviceType);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PublisherLookupResult.class.getSimpleName() + "[", "]")
                .add("url=" + url)
                .add("serviceType='" + serviceType + "'")
                .add("dnsLookupType=" + dnsLookupType)
                .toString();
    }
}
