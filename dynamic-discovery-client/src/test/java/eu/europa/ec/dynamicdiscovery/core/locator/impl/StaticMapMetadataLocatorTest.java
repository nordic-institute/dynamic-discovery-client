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

import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


/**
 * @author Joze RIHTARSIC
 * @since 1.14
 */
class StaticMapMetadataLocatorTest {

    @Test
    void testDefaultLookupNull() throws TechnicalException {
        URI defaultURI = Mockito.mock(URI.class);
        StaticMapMetadataLocator testInstance = new StaticMapMetadataLocator(defaultURI);
        URI result = testInstance.lookup(null);

        assertEquals(defaultURI, result);
    }

    @Test
    void testDefaultLookupNotNull() throws TechnicalException {
        URI defaultURI = Mockito.mock(URI.class);
        StaticMapMetadataLocator testInstance = new StaticMapMetadataLocator(defaultURI);
        URI result = testInstance.lookup(new SMPParticipantIdentifier("test", "test"));
        assertEquals(defaultURI, result);
    }

    @Test
    void testMapLookupExceptionTrue() throws TechnicalException, URISyntaxException {

        URI defaultURI = new URI("http://default/smp");
        URI targetURI = new URI("http://target/smp");
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("testId", "testScheme");
        Map<SMPParticipantIdentifier, URI> map = new HashMap<>();
        map.put(participantIdentifier, targetURI);

        StaticMapMetadataLocator testInstance = new StaticMapMetadataLocator(defaultURI, map);

        URI result = testInstance.lookup("testId", "testScheme");
        assertEquals(targetURI, result);
    }

    @Test
    void testMapLookupExceptionFalse() throws TechnicalException, URISyntaxException {

        URI defaultURI = new URI("http://default/smp");
        URI targetURI = new URI("http://target/smp");
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("testId", "testScheme");
        Map<SMPParticipantIdentifier, URI> map = new HashMap<>();
        map.put(participantIdentifier, targetURI);

        StaticMapMetadataLocator testInstance = new StaticMapMetadataLocator(defaultURI, map);

        URI result = testInstance.lookup("testDefaultId", "testScheme");
        assertEquals(defaultURI, result);
    }

    @Test
    void testGetDnsLookup() {
        StaticMapMetadataLocator testInstance = new StaticMapMetadataLocator((URI) null);
        // return null
        assertNull(testInstance.getDnsLookup());
    }
}
