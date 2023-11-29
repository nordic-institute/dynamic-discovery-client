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
package eu.europa.ec.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.model.*;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPProcessIdentifier;
import org.junit.jupiter.api.Test;

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

