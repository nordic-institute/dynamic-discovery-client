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
package eu.europa.ec.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.model.*;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPProcessIdentifier;
import org.junit.jupiter.api.Test;
import java.security.cert.X509Certificate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author Flávio W. R. Santos
 */
class CommonTest {

    @Test
    void equalsTest() {
        SMPProcessIdentifier processIdentifier1 = new SMPProcessIdentifier("urn:epsosPatientService::List", "scheme='ehealth-procid-qns");
        SMPProcessIdentifier processIdentifier2 = new SMPProcessIdentifier("urn:epsosPatientService::List", "scheme='ehealth-procid-qns");
        SMPTransportProfile transportProfile1 = new SMPTransportProfile("urn:ihe:iti:2013:xcpd");
        SMPTransportProfile transportProfile2 = new SMPTransportProfile("urn:ihe:iti:2013:xcpd");
        SMPEndpoint endpoint1 = new SMPEndpoint.Builder().addProcessIdentifier(processIdentifier1).transportProfile(transportProfile1).address("http://edelivery.tech.ec.europa.eu").build();
        SMPEndpoint endpoint2 = new SMPEndpoint.Builder().addProcessIdentifier(processIdentifier1).transportProfile(transportProfile1).address("http://edelivery.tech.ec.europa.eu").build();

        assertEquals(new SMPParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns"), new SMPParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns"));
        assertEquals(new SMPDocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns"), new SMPDocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns"));
        assertEquals(processIdentifier1, processIdentifier2);
        assertEquals(transportProfile1, transportProfile2);
        assertEquals(endpoint1, endpoint2);
    }

    @Test
    void notEqualsTest() {
        SMPProcessIdentifier processIdentifier1 = new SMPProcessIdentifier("urn:epsosPatientService::List", "scheme='ehealth-procid-qns");
        SMPProcessIdentifier processIdentifier2 = new SMPProcessIdentifier("urn:epsosPatientService::List1", "scheme='ehealth-procid-qns");
        SMPTransportProfile transportProfile1 = new SMPTransportProfile("urn:ihe:iti:2013d:xcpd");
        SMPTransportProfile transportProfile2 = new SMPTransportProfile("urn:ihe:iti:2013:xcpd1");
        SMPEndpoint endpoint1 = new SMPEndpoint.Builder().addProcessIdentifier(processIdentifier1).transportProfile(transportProfile1).address("http://edelivery.tech.ec.europa.eu").build();
        SMPEndpoint endpoint2 =  new SMPEndpoint.Builder().addProcessIdentifier(processIdentifier1).transportProfile(transportProfile1).address("http://edelivery.tech.ec.europa.eu1").build();;

        assertNotEquals(new SMPParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns"), new SMPParticipantIdentifier("urn:poland:ncpb1", "ehealth-actorid-qns"));
        assertNotEquals(new SMPDocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns"), new SMPDocumentIdentifier("urn::epsos##services:extended:epsos::1071", "ehealth-resid-qns"));
        assertNotEquals(processIdentifier1, processIdentifier2);
        assertNotEquals(transportProfile1, transportProfile2);
        assertNotEquals(endpoint1, endpoint2);
    }
}

