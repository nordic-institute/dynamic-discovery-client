package eu.europa.ec.dynamicdiscovery.core.extension.impl.oasis10;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OasisSMP10ExtensionBuilderTest {

    @Test
    void testDefaultBuilderValues() {
        OasisSMP10Extension extension = new OasisSMP10Extension.Builder().build();
        assertEquals(OasisSMP10Extension.DEFAULT_PUBLISHER_URL_CONTEXT, extension.contextPath());
        assertEquals(OasisSMP10Extension.DEFAULT_SUBRESOURCE_URL_CONTEXT, extension.subContextPath());
        assertEquals(1, extension.lookupServices().size());
        assertTrue(extension.lookupServices().contains(OasisSMP10Extension.DEFAULT_LOOKUP_SERVICE));
    }

    @Test
    void testBuilderWithCustomValues() {
        String customContext = "/custom";
        String customSubContext = "/sub";
        String service1 = "Meta:Custom1";
        String service2 = "Meta:Custom2";

        OasisSMP10Extension extension = new OasisSMP10Extension.Builder()
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