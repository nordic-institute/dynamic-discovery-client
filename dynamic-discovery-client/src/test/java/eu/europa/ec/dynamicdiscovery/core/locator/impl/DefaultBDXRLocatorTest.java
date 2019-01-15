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
package eu.europa.ec.dynamicdiscovery.core.locator.impl;

import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.URI;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;


public class DefaultBDXRLocatorTest {

    @Test
    public void testLookupNAPTR() throws Exception {
        //GIVEN
        DefaultBDXRLocator defaultBDXRLocator = lookupNAPTR();
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:brazil:saopaulo", "country-state-qns");

        //WHEN
        URI uri = defaultBDXRLocator.lookup(participantIdentifier);

        //THEN
        Assert.assertEquals("http://smp-mock-1.ehealth.eu:8888", uri.toString());
    }

    @Test
    public void testLookupNAPTR2() throws Exception {
        //GIVEN
        DefaultBDXRLocator defaultBDXRLocator = lookupNAPTR();

        //WHEN
        URI uri = defaultBDXRLocator.lookup("urn:brazil:saopaulo", "country-state-qns");

        //THEN
        Assert.assertEquals("http://smp-mock-1.ehealth.eu:8888", uri.toString());
    }

    @Test
    public void testLookupCNAME() throws Exception {
        //GIVEN
        DefaultBDXRLocator defaultBDXRLocator = new DefaultBDXRLocator("ehealth.acc.edelivery.tech.ec.europa.eu");
        defaultBDXRLocator = spy(defaultBDXRLocator);
        Mockito.doReturn(null).when(defaultBDXRLocator).naptrLookupFetcher(any(ParticipantIdentifier.class), any(String.class));
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:brazil:saopaulo", "country-state-qns");

        //WHEN
        URI uri = defaultBDXRLocator.lookup(participantIdentifier);

        //THEN
        Assert.assertEquals("http://b-5cc29a6e1d849a3089cb7d8b192e55b7.country-state-qns.ehealth.acc.edelivery.tech.ec.europa.eu", uri.toString());
    }

    private DefaultBDXRLocator lookupNAPTR() throws Exception {
        DefaultBDXRLocator defaultBDXRLocator = new DefaultBDXRLocator("ehealth.acc.edelivery.tech.ec.europa.eu");
        defaultBDXRLocator = spy(defaultBDXRLocator);
        Mockito.doReturn("http://smp-mock-1.ehealth.eu:8888").when(defaultBDXRLocator).naptrLookupFetcher(any(ParticipantIdentifier.class), any(String.class));

        return defaultBDXRLocator;
    }
}
