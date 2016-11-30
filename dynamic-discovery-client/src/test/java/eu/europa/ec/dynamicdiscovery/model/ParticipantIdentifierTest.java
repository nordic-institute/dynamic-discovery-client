/*
 * (C) Copyright 2016 - European Commission | Dynamic Discovery Client
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
 *
 * @author Flávio W. R. Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 *
 */
package eu.europa.ec.dynamicdiscovery.model;

import org.junit.Assert;
import org.junit.Test;

public class ParticipantIdentifierTest {

    @Test
    public void checkFullIdentifierTest() throws Exception {
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");
        Assert.assertEquals("urn:poland:ncpb", participantIdentifier.getIdentifier());
        Assert.assertEquals("ehealth-actorid-qns", participantIdentifier.getScheme());
        Assert.assertEquals("ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb", participantIdentifier.urlencoded());
    }

    @Test
    public void checkEncodedURLTest() throws Exception {
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");
        Assert.assertEquals("ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb", participantIdentifier.urlencoded());
    }

    @Test
    public void checkIdentifierLowerCaseTest() throws Exception {
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("URN:POLAND:NCPB", "ehealth-actorid-qns");
        Assert.assertNotEquals("URN:POLAND:NCPB", participantIdentifier.getIdentifier());
        Assert.assertEquals("urn:poland:ncpb", participantIdentifier.getIdentifier());

        participantIdentifier = new ParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");
        Assert.assertEquals("urn:poland:ncpb", participantIdentifier.getIdentifier());
    }

    @Test
    public void checkSchemeNotCaseManagedTest() throws Exception {
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");
        Assert.assertEquals("ehealth-actorid-qns", participantIdentifier.getScheme());
        Assert.assertNotEquals("EHEALTH-ACTORID-QNS", participantIdentifier.getScheme());

        participantIdentifier = new ParticipantIdentifier("urn:poland:ncpb", "EHEALTH-ACTORID-QNS");
        Assert.assertNotEquals("ehealth-actorid-qns", participantIdentifier.getScheme());
        Assert.assertEquals("EHEALTH-ACTORID-QNS", participantIdentifier.getScheme());
    }

}