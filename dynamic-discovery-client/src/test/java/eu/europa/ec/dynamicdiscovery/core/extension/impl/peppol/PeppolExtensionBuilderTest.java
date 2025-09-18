package eu.europa.ec.dynamicdiscovery.core.extension.impl.peppol;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PeppolExtensionBuilderTest {

    @Test
    void testDefaultBuilderValues() {
        PeppolSMPExtension extension = new PeppolSMPExtension.Builder().build();
        assertEquals(PeppolSMPExtension.DEFAULT_PUBLISHER_URL_CONTEXT, extension.contextPath());
        assertEquals(PeppolSMPExtension.DEFAULT_SUBRESOURCE_URL_CONTEXT, extension.subContextPath());
        assertEquals(1, extension.lookupServices().size());
        assertTrue(extension.lookupServices().contains(PeppolSMPExtension.DEFAULT_LOOKUP_SERVICE));
    }

    @Test
    void testBuilderWithCustomValues() {
        String customContext = "/custom";
        String customSubContext = "/sub";
        String service1 = "Meta:Custom1";
        String service2 = "Meta:Custom2";

        PeppolSMPExtension extension = new PeppolSMPExtension.Builder()
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