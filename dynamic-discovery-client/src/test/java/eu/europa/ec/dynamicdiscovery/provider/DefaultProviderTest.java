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
 * @author Erlend Klakegg Bergheim - erlend.klakegg.bergheim@difi.no
 *
 */
package eu.europa.ec.dynamicdiscovery.provider;

import eu.europa.ec.dynamicdiscovery.core.locator.impl.DefaultBDXRLocator;
import eu.europa.ec.dynamicdiscovery.core.provider.impl.DefaultProvider;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import org.junit.Assert;
import org.junit.Test;

import java.net.URI;

public class DefaultProviderTest {

    @Test
    public void resolveDocumentIdentifiersTest() throws Exception {
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");
        DefaultBDXRLocator location = new DefaultBDXRLocator("ehealth.acc.edelivery.tech.ec.europa.eu");
        DefaultProvider defaultProvider = new DefaultProvider();
        Assert.assertEquals("http://b-adb4c6d3821d142c684b13ed269fad65.ehealth-actorid-qns.ehealth.acc.edelivery.tech.ec.europa.eu/ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb", defaultProvider.resolveDocumentIdentifiers(new URI("http://b-adb4c6d3821d142c684b13ed269fad65.ehealth-actorid-qns.ehealth.acc.edelivery.tech.ec.europa.eu"), participantIdentifier).toString());
        Assert.assertEquals("ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb", participantIdentifier.urlencoded());
    }

    @Test
    public void resolveServiceMetadataTest() throws Exception {
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");
        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns");
        DefaultProvider defaultProvider = new DefaultProvider();
        Assert.assertEquals("http://b-adb4c6d3821d142c684b13ed269fad65.ehealth-actorid-qns.ehealth.acc.edelivery.tech.ec.europa.eu/ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A107", defaultProvider.resolveServiceMetadata(new URI("http://b-adb4c6d3821d142c684b13ed269fad65.ehealth-actorid-qns.ehealth.acc.edelivery.tech.ec.europa.eu"), participantIdentifier, documentIdentifier).toString());
        Assert.assertEquals("ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb", participantIdentifier.urlencoded());
        Assert.assertEquals("ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A107", documentIdentifier.urlencoded());
    }
}
