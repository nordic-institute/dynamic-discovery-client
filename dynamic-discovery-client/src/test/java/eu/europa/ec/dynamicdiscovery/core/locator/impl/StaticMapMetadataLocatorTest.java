package eu.europa.ec.dynamicdiscovery.core.locator.impl;

import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Joze RIHTARSIC
 * @since 1.14
 */
public class StaticMapMetadataLocatorTest {

    @Test
    public void testDefaultLookupNull() throws TechnicalException {
        URI defaultURI = Mockito.mock(URI.class);
        StaticMapMetadataLocator testInstance = new StaticMapMetadataLocator(defaultURI);
        URI result = testInstance.lookup(null);

        assertEquals(defaultURI, result);
    }

    @Test
    public void testDefaultLookupNotNull() throws TechnicalException {
        URI defaultURI = Mockito.mock(URI.class);
        StaticMapMetadataLocator testInstance = new StaticMapMetadataLocator(defaultURI);
        URI result = testInstance.lookup(new ParticipantIdentifier("test", "test"));
        assertEquals(defaultURI, result);
    }

    @Test
    public void testMapLookupExceptionTrue() throws TechnicalException, URISyntaxException {

        URI defaultURI = new URI("http://default/smp");
        URI targetURI = new URI("http://target/smp");
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("testId", "testScheme");
        Map<ParticipantIdentifier, URI> map = new HashMap<>();
        map.put(participantIdentifier, targetURI);

        StaticMapMetadataLocator testInstance = new StaticMapMetadataLocator(defaultURI, map);

        URI result = testInstance.lookup("testId", "testScheme");
        assertEquals(targetURI, result);
    }

    @Test
    public void testMapLookupExceptionFalse() throws TechnicalException, URISyntaxException {

        URI defaultURI = new URI("http://default/smp");
        URI targetURI = new URI("http://target/smp");
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("testId", "testScheme");
        Map<ParticipantIdentifier, URI> map = new HashMap<>();
        map.put(participantIdentifier, targetURI);

        StaticMapMetadataLocator testInstance = new StaticMapMetadataLocator(defaultURI, map);

        URI result = testInstance.lookup("testDefaultId", "testScheme");
        assertEquals(defaultURI, result);
    }

    @Test
    public void testGetDnsLookup() {
        StaticMapMetadataLocator testInstance = new StaticMapMetadataLocator((URI) null);
        // return null
        Assert.assertNull(testInstance.getDnsLookup());
    }
}
