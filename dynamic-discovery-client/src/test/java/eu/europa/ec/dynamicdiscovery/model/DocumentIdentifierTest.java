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

import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author Flávio W. R. Santos
 */
class DocumentIdentifierTest {

    @Test
    void checkEbMS30TestService() throws Exception {
        SMPDocumentIdentifier documentIdentifier = new SMPDocumentIdentifier("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/test", null);
        assertEquals("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/test", documentIdentifier.getIdentifier());
        assertEquals(null, documentIdentifier.getScheme());
    }

    @Test
    void checkEbMS30TestServiceEmpty() throws Exception {
        SMPDocumentIdentifier documentIdentifier = new SMPDocumentIdentifier("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/test", "");
        assertEquals("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/test", documentIdentifier.getIdentifier());
        assertEquals("", documentIdentifier.getScheme());
    }

    @Test
    void checkFullIdentifierTest() throws Exception {
        SMPDocumentIdentifier documentIdentifier = new SMPDocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns");
        assertEquals("urn::epsos##services:extended:epsos::107", documentIdentifier.getIdentifier());
        assertEquals("ehealth-resid-qns", documentIdentifier.getScheme());
    }

    @Test
    void checkIdentifierNotCaseManagedTest() throws Exception {
        SMPDocumentIdentifier documentIdentifier = new SMPDocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns");
        assertEquals("urn::epsos##services:extended:epsos::107", documentIdentifier.getIdentifier());
        assertNotEquals("URN::EPSOS##SERVICES:EXTENDED:EPSOS::107", documentIdentifier.getIdentifier());

        documentIdentifier = new SMPDocumentIdentifier("URN::EPSOS##SERVICES:EXTENDED:EPSOS::107", "ehealth-resid-qns");
        assertNotEquals("urn::epsos##services:extended:epsos::107", documentIdentifier.getIdentifier());
        assertEquals("URN::EPSOS##SERVICES:EXTENDED:EPSOS::107", documentIdentifier.getIdentifier());
    }

    @Test
    void checkSchemeNotCaseManagedTest() throws Exception {
        SMPDocumentIdentifier documentIdentifier = new SMPDocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns");
        assertEquals("ehealth-resid-qns", documentIdentifier.getScheme());
        assertNotEquals("EHEALTH-RESID-QNS", documentIdentifier.getScheme());

        documentIdentifier = new SMPDocumentIdentifier("urn::epsos##services:extended:epsos::107", "EHEALTH-RESID-QNS");
        assertNotEquals("ehealth-resid-qns", documentIdentifier.getScheme());
        assertEquals("EHEALTH-RESID-QNS", documentIdentifier.getScheme());
    }
}
