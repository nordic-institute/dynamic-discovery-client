/*
 * (C) Copyright 2016-2023 - European Commission | Dynamic Discovery Client
 *
 * https://ec.europa.eu/digital-building-blocks/code/projects/EDELIVERY/repos/dynamic-discovery-client/browse
 *
 * Licensed under the LGPL, Version 2.1 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     dynamic-discovery\License_LGPL-2.1.txt or https://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.europa.ec.dynamicdiscovery.model;

import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPProcessIdentifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author Flávio W. R. Santos
 */
class ProcessIdentifierTest {

    @Test
    void checkEbMS30TestService() throws Exception {
        SMPProcessIdentifier processIdentifier = new SMPProcessIdentifier("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/service", null);
        assertEquals("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/service", processIdentifier.getIdentifier());
        assertEquals(null, processIdentifier.getScheme());
    }

    @Test
    void checkEbMS30TestServiceEmpty() throws Exception {
        SMPProcessIdentifier processIdentifier = new SMPProcessIdentifier("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/service", "");
        assertEquals("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/service", processIdentifier.getIdentifier());
        assertEquals("", processIdentifier.getScheme());
    }

    @Test
    void checkFullIdentifierTest() throws Exception {
        SMPProcessIdentifier processIdentifier = new SMPProcessIdentifier("urn:www.cenbii.eu:profile:bii05:ver2.0", "cenbii-procid-ubl");
        assertEquals("urn:www.cenbii.eu:profile:bii05:ver2.0", processIdentifier.getIdentifier());
        assertEquals("cenbii-procid-ubl", processIdentifier.getScheme());
    }

    @Test
    void checkIdentifierNotCaseManagedTest() throws Exception {
        SMPProcessIdentifier processIdentifier = new SMPProcessIdentifier("urn:www.cenbii.eu:profile:bii05:ver2.0", "cenbii-procid-ubl");
        assertEquals("urn:www.cenbii.eu:profile:bii05:ver2.0", processIdentifier.getIdentifier());
        assertNotEquals("URN:WWW.CENBII.EU:PROFILE:BII05:VER2.0", processIdentifier.getIdentifier());

        processIdentifier = new SMPProcessIdentifier("URN:WWW.CENBII.EU:PROFILE:BII05:VER2.0", "cenbii-procid-ubl");
        assertNotEquals("urn:www.cenbii.eu:profile:bii05:ver2.0", processIdentifier.getIdentifier());
        assertEquals("URN:WWW.CENBII.EU:PROFILE:BII05:VER2.0", processIdentifier.getIdentifier());
    }

    @Test
    void checkSchemeNotCaseManagedTest() throws Exception {
        SMPProcessIdentifier processIdentifier = new SMPProcessIdentifier("urn:www.cenbii.eu:profile:bii05:ver2.0", "cenbii-procid-ubl");
        assertEquals("cenbii-procid-ubl", processIdentifier.getScheme());
        assertNotEquals("CENBII-PROCID-UBL", processIdentifier.getScheme());

        processIdentifier = new SMPProcessIdentifier("urn:www.cenbii.eu:profile:bii05:ver2.0", "CENBII-PROCID-UBL");
        assertNotEquals("cenbii-procid-ubl", processIdentifier.getScheme());
        assertEquals("CENBII-PROCID-UBL", processIdentifier.getScheme());
    }
}
