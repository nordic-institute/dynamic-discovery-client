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
package eu.europa.ec.dynamicdiscovery.core.locator.impl;

import eu.europa.ec.dynamicdiscovery.core.locator.PublisherLookupResult;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * @author Joze RIHTARSIC
 * @since 1.14
 */
class StaticMapMetadataLocatorTest {
    private static final URI TEST_DEFAULT_URI = URI.create("https://default:1234/smp");


    @Test
    void testDefaultLookupNull() throws TechnicalException {
        // given
        StaticMapMetadataLocator testInstance = new StaticMapMetadataLocator(TEST_DEFAULT_URI);
        // when
        List<PublisherLookupResult> result = testInstance.lookup(null);
        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TEST_DEFAULT_URI, result.get(0).getUrl());
    }

    @Test
    void testDefaultLookupNotNull() throws TechnicalException {

        // given
        StaticMapMetadataLocator testInstance = new StaticMapMetadataLocator(TEST_DEFAULT_URI);
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("testId", "testScheme");
        // when
        List<PublisherLookupResult> result = testInstance.lookup(participantIdentifier);
        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TEST_DEFAULT_URI, result.get(0).getUrl());
    }

    @Test
    void testMapLookupTargetParticipantOK() throws TechnicalException, URISyntaxException {
        // given
        URI targetURI = new URI("http://target/smp");
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("testId", "testScheme");
        Map<SMPParticipantIdentifier, URI> map = new HashMap<>();
        map.put(participantIdentifier, targetURI);
        StaticMapMetadataLocator testInstance = new StaticMapMetadataLocator(TEST_DEFAULT_URI, map);

        // when
        List<PublisherLookupResult> result = testInstance.lookup("testId", "testScheme");
        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(targetURI, result.get(0).getUrl());
    }

    @Test
    void testMapLookupTargetParticipantFalse() throws TechnicalException, URISyntaxException {

        URI targetURI = new URI("http://target/smp");
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("testId", "testScheme");
        Map<SMPParticipantIdentifier, URI> map = new HashMap<>();
        map.put(participantIdentifier, targetURI);
        StaticMapMetadataLocator testInstance = new StaticMapMetadataLocator(TEST_DEFAULT_URI, map);

        // when
        List<PublisherLookupResult> result = testInstance.lookup("testDefaultId", "testScheme");
        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TEST_DEFAULT_URI, result.get(0).getUrl());
    }
}
