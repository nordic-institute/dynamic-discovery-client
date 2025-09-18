package eu.europa.ec.dynamicdiscovery.core.extension.impl.oasis20;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OasisSMP20ExtensionBuilderTest {

    @Test
    void testDefaultBuilderValues() {
        OasisSMP20Extension extension = new OasisSMP20Extension.Builder().build();
        assertEquals(OasisSMP20Extension.DEFAULT_PUBLISHER_URL_CONTEXT, extension.contextPath());
        assertEquals(OasisSMP20Extension.DEFAULT_SUBRESOURCE_URL_CONTEXT, extension.subContextPath());
        assertEquals(1, extension.lookupServices().size());
        assertTrue(extension.lookupServices().contains(OasisSMP20Extension.DEFAULT_LOOKUP_SERVICE));
    }

    @Test
    void testBuilderWithCustomValues() {
        String customContext = "/custom";
        String customSubContext = "/sub";
        String service1 = "Meta:Custom1";
        String service2 = "Meta:Custom2";

        OasisSMP20Extension extension = new OasisSMP20Extension.Builder()
                .contextPath(customContext)
                .subContextPath(customSubContext)
                .addNaptrService(service1)
                .addNaptrServices(service2)
                .build();

        assertEquals(customContext, extension.contextPath());
        assertEquals(customSubContext, extension.subContextPath());
        assertTrue(extension.lookupServices().contains(service1));
        assertTrue(extension.lookupServices().contains(service2));
        assertEquals(2, extension.lookupServices().size());
    }
}