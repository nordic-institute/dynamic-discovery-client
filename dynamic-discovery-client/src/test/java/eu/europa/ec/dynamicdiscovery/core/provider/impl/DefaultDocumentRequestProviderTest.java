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
import eu.europa.ec.dynamicdiscovery.core.provider.PublisherRequest;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Flávio W. R. Santos
 * @author Erlend Klakegg Bergheim
 */
class DefaultDocumentRequestProviderTest {
    private static final String TEST_URI = "http://b-adb4c6d3821d142c684b13ed269fad65.ehealth-actorid-qns.ehealth.acc.edelivery.tech.ec.europa.eu";
    DefaultDocumentRequestProvider testInstance = new DefaultDocumentRequestProvider.Builder().build();

    @Test
    void testCreateRequestForResource() throws Exception {
        // given
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");
        PublisherLookupResult publisherResult = Mockito.mock(PublisherLookupResult.class);
        Mockito.when(publisherResult.getUrl()).thenReturn(new URI(TEST_URI));
        //when
        PublisherRequest  request = testInstance.createRequestForResource(publisherResult, participantIdentifier);
        // then
        assertNotNull(request);
        assertNotNull(request.getResourceUri());
        assertNull(request.getSubresourceUri());
        assertEquals(TEST_URI + "/ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb", request.getResourceUri().toString());
    }


    @Test
    void testCreateRequestForSubresource() throws Exception {
        // given
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");
        SMPDocumentIdentifier documentIdentifier = new SMPDocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns");
        PublisherLookupResult publisherResult = Mockito.mock(PublisherLookupResult.class);
        Mockito.when(publisherResult.getUrl()).thenReturn(new URI(TEST_URI));

        // when
        PublisherRequest  request = testInstance.createRequestForSubresource(publisherResult, participantIdentifier, documentIdentifier);

        assertNotNull(request);
        assertNotNull(request.getResourceUri());
        assertNotNull(request.getSubresourceUri());
        assertEquals(TEST_URI + "/ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb", request.getResourceUri().toString());
        assertEquals(TEST_URI + "/ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A107", request.getSubresourceUri().toString());
    }

    @ParameterizedTest
    @CsvSource({
            "http://example.com",
            "http://example.com:8080",
            "http://example.com/",
            "http://example.com:8080/",
            "http://example.com/context",
            "http://example.com/context/",
            "https://example.com:8080/",
            "https://example.com:8080/context",
    })
    void testCreateRequestForResourceWithPublisherURLs(String publisherUrl) throws Exception {
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");
        PublisherLookupResult publisherResult = Mockito.mock(PublisherLookupResult.class);
        Mockito.when(publisherResult.getUrl()).thenReturn(new URI(publisherUrl));
        //when
        PublisherRequest  request = testInstance.createRequestForResource(publisherResult, participantIdentifier);
        // then
        assertNotNull(request);
        assertNotNull(request.getResourceUri());
        assertNull(request.getSubresourceUri());
        assertEquals(StringUtils.removeEnd(publisherUrl,"/") + "/ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb", request.getResourceUri().toString());
    }
}
