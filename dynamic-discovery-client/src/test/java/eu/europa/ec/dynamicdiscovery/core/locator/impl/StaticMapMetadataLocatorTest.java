/*
 * Copyright 2017-2023 European Commission | eDelivery Dynamic Discovery Client
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence attached in file: LICENSE-EUPL-v1.2-EN.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
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
