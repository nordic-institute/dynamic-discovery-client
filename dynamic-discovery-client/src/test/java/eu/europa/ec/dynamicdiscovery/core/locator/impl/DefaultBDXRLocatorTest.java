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
package eu.europa.ec.dynamicdiscovery.core.locator.impl;

import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;

/**
 * @author Flávio W. R. Santos
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultBDXRLocatorTest {

    @Captor
    ArgumentCaptor<String> naptrUrlCaptor;

    @Test
    public void testLookupNAPTR() throws Exception {
        //GIVEN
        DefaultBDXRLocator defaultBDXRLocator = lookupNAPTR();
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:brazil:saopaulo", "country-state-qns");

        //WHEN
        URI uri = defaultBDXRLocator.lookup(participantIdentifier);

        //THEN
        assertEquals("http://smp-mock-1.ehealth.eu:8888", uri.toString());
        assertEquals("2CDN5ANIHSX2W6D2ZA5YSSGR2BXVLCGTLS6STIYM2CZYHB3L7GMA.country-state-qns.ehealth.acc.edelivery.tech.ec.europa.eu", naptrUrlCaptor.getValue());
    }

    @Test
    public void testLookupNAPTRUpperCaseIdentifier() throws Exception {
        //GIVEN
        DefaultBDXRLocator defaultBDXRLocator = lookupNAPTR();

        //WHEN
        URI uri = defaultBDXRLocator.lookup("urn:brazil:SAOPAULO", "country-state-qns");

        //THEN
        assertEquals("http://smp-mock-1.ehealth.eu:8888", uri.toString());
        assertEquals("2CDN5ANIHSX2W6D2ZA5YSSGR2BXVLCGTLS6STIYM2CZYHB3L7GMA.country-state-qns.ehealth.acc.edelivery.tech.ec.europa.eu", naptrUrlCaptor.getValue());
    }

    @Test
    public void testLookupNAPTROasisPartyType() throws Exception {
        //GIVEN
        DefaultBDXRLocator defaultBDXRLocator = lookupNAPTR();

        //WHEN
        URI uri = defaultBDXRLocator.lookup("urn:brazil:saopaulo", "urn:oasis:names:tc:ebcore:partyid-type:unregistered");

        //THEN
        assertEquals("http://smp-mock-1.ehealth.eu:8888", uri.toString());
        assertEquals("XN536BJVZUJJWWJZPQN5KAM6LFPK4ZZD2VL4AXQRELT5HTCJ6LEQ.ehealth.acc.edelivery.tech.ec.europa.eu", naptrUrlCaptor.getValue());
    }

    @Test
    public void testLookupNAPTROasisPartyTypeNullScheme() throws Exception {
        //GIVEN
        DefaultBDXRLocator defaultBDXRLocator = lookupNAPTR();

        //WHEN
        URI uri = defaultBDXRLocator.lookup("urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn:brazil:saopaulo", null);

        //THEN
        assertEquals("http://smp-mock-1.ehealth.eu:8888", uri.toString());
        assertEquals("XN536BJVZUJJWWJZPQN5KAM6LFPK4ZZD2VL4AXQRELT5HTCJ6LEQ.ehealth.acc.edelivery.tech.ec.europa.eu", naptrUrlCaptor.getValue());
    }

    @Test
    public void testLookupNAPTROasisPartyTypeEmptyScheme() throws Exception {
        //GIVEN
        DefaultBDXRLocator defaultBDXRLocator = lookupNAPTR();

        //WHEN
        URI uri = defaultBDXRLocator.lookup("urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn:brazil:saopaulo", "");

        //THEN
        assertEquals("http://smp-mock-1.ehealth.eu:8888", uri.toString());
        assertEquals("XN536BJVZUJJWWJZPQN5KAM6LFPK4ZZD2VL4AXQRELT5HTCJ6LEQ.ehealth.acc.edelivery.tech.ec.europa.eu", naptrUrlCaptor.getValue());
    }

    @Test
    public void testLookupCNAMEPeppolPartyType() throws Exception {
        //GIVEN
        DefaultBDXRLocator defaultBDXRLocator = new DefaultBDXRLocator("ehealth.acc.edelivery.tech.ec.europa.eu");
        defaultBDXRLocator = spy(defaultBDXRLocator);
        Mockito.doReturn(null).when(defaultBDXRLocator).naptrLookupFetcher(any(ParticipantIdentifier.class), any(String.class));
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:brazil:saopaulo", "country-state-qns");

        //WHEN
        URI uri = defaultBDXRLocator.lookup(participantIdentifier);

        //THEN
        assertEquals("http://b-5cc29a6e1d849a3089cb7d8b192e55b7.country-state-qns.ehealth.acc.edelivery.tech.ec.europa.eu", uri.toString());
    }

    @Test
    public void testLookupCNAMEOasisPartyType() throws Exception {
        //GIVEN
        DefaultBDXRLocator defaultBDXRLocator = new DefaultBDXRLocator("ehealth.acc.edelivery.tech.ec.europa.eu");
        defaultBDXRLocator = spy(defaultBDXRLocator);
        Mockito.doReturn(null).when(defaultBDXRLocator).naptrLookupFetcher(any(ParticipantIdentifier.class), any(String.class));
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:brazil:saopaulo", "urn:oasis:names:tc:ebcore:partyid-type:unregistered");

        //WHEN
        URI uri = defaultBDXRLocator.lookup(participantIdentifier);

        //THEN
        assertEquals("http://b-761c04e661616234cd81659d456b0cf6.ehealth.acc.edelivery.tech.ec.europa.eu", uri.toString());
    }

    @Test
    public void testLookupCNAMEOasisPartyTypeNullScheme() throws Exception {
        //GIVEN
        DefaultBDXRLocator defaultBDXRLocator = new DefaultBDXRLocator("ehealth.acc.edelivery.tech.ec.europa.eu");
        defaultBDXRLocator = spy(defaultBDXRLocator);
        Mockito.doReturn(null).when(defaultBDXRLocator).naptrLookupFetcher(any(ParticipantIdentifier.class), any(String.class));
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn:brazil:saopaulo", null);

        //WHEN
        URI uri = defaultBDXRLocator.lookup(participantIdentifier);

        //THEN
        assertEquals("http://b-761c04e661616234cd81659d456b0cf6.ehealth.acc.edelivery.tech.ec.europa.eu", uri.toString());
    }

    @Test
    public void testLookupCNAMEOasisPartyTypeEmptyScheme() throws Exception {
        //GIVEN
        DefaultBDXRLocator defaultBDXRLocator = new DefaultBDXRLocator("ehealth.acc.edelivery.tech.ec.europa.eu");
        defaultBDXRLocator = spy(defaultBDXRLocator);
        Mockito.doReturn(null).when(defaultBDXRLocator).naptrLookupFetcher(any(ParticipantIdentifier.class), any(String.class));
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn:brazil:saopaulo", "");

        //WHEN
        URI uri = defaultBDXRLocator.lookup(participantIdentifier);

        //THEN
        assertEquals("http://b-761c04e661616234cd81659d456b0cf6.ehealth.acc.edelivery.tech.ec.europa.eu", uri.toString());
    }

    private DefaultBDXRLocator lookupNAPTR() throws Exception {
        DefaultBDXRLocator defaultBDXRLocator = new DefaultBDXRLocator("ehealth.acc.edelivery.tech.ec.europa.eu");
        defaultBDXRLocator = spy(defaultBDXRLocator);
        Mockito.doReturn("http://smp-mock-1.ehealth.eu:8888").when(defaultBDXRLocator).naptrLookupFetcher(any(ParticipantIdentifier.class), naptrUrlCaptor.capture());

        return defaultBDXRLocator;
    }
}
