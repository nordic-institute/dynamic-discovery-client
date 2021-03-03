/*
 * (C) Copyright 2016-2021 - European Commission | Dynamic Discovery Client
 *
 * https://ec.europa.eu/cefdigital/code/projects/EDELIVERY/repos/dynamic-discovery-client/browse
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
package eu.europa.ec.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.model.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Flávio W. R. Santos
 */
public class CommonTest {

    @Test
    public void equalsTest() throws Exception {
        ProcessIdentifier processIdentifier1 = new ProcessIdentifier("urn:epsosPatientService::List", "scheme='ehealth-procid-qns");
        ProcessIdentifier processIdentifier2 = new ProcessIdentifier("urn:epsosPatientService::List", "scheme='ehealth-procid-qns");
        TransportProfile transportProfile1 = new TransportProfile("urn:ihe:iti:2013:xcpd");
        TransportProfile transportProfile2 = new TransportProfile("urn:ihe:iti:2013:xcpd");
        Endpoint endpoint1 = new Endpoint(processIdentifier1, transportProfile1, "http://edelivery.tech.ec.europa.eu", null);
        Endpoint endpoint2 = new Endpoint(processIdentifier1, transportProfile1, "http://edelivery.tech.ec.europa.eu", null);

        Assert.assertEquals(new ParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns"), new ParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns"));
        Assert.assertEquals(new DocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns"), new DocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns"));
        Assert.assertEquals(processIdentifier1, processIdentifier2);
        Assert.assertEquals(transportProfile1, transportProfile2);
        Assert.assertEquals(endpoint1, endpoint2);
    }

    @Test
    public void notEqualsTest() throws Exception {
        ProcessIdentifier processIdentifier1 = new ProcessIdentifier("urn:epsosPatientService::List", "scheme='ehealth-procid-qns");
        ProcessIdentifier processIdentifier2 = new ProcessIdentifier("urn:epsosPatientService::List1", "scheme='ehealth-procid-qns");
        TransportProfile transportProfile1 = new TransportProfile("urn:ihe:iti:2013d:xcpd");
        TransportProfile transportProfile2 = new TransportProfile("urn:ihe:iti:2013:xcpd1");
        Endpoint endpoint1 = new Endpoint(processIdentifier1, transportProfile1, "http://edelivery.tech.ec.europa.eu", null);
        Endpoint endpoint2 = new Endpoint(processIdentifier1, transportProfile1, "http://edelivery.tech.ec.europa.eu1", null);

        Assert.assertNotEquals(new ParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns"), new ParticipantIdentifier("urn:poland:ncpb1", "ehealth-actorid-qns"));
        Assert.assertNotEquals(new DocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns"), new DocumentIdentifier("urn::epsos##services:extended:epsos::1071", "ehealth-resid-qns"));
        Assert.assertNotEquals(processIdentifier1, processIdentifier2);
        Assert.assertNotEquals(transportProfile1, transportProfile2);
        Assert.assertNotEquals(endpoint1, endpoint2);
    }
}

