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
