/*-
 * #%L
 * dynamic-discovery-client
 * %%
 * Copyright (C) 2016 - 2025 European Commission | eDelivery | Dynamic Discovery Client
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
