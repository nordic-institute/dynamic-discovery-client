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

import eu.europa.ec.dynamicdiscovery.core.locator.dns.impl.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.enums.DNSLookupType;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;

/**
 * @author Flávio W. R. Santos
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
class DefaultBDXRLocatorTest {

    @Captor
    ArgumentCaptor<String> dnsRecordUrlCaptor;

    @Test
    void testLookupNAPTR() throws Exception {
        //GIVEN
        DefaultBDXRLocator defaultBDXRLocator = lookupNAPTR();
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("urn:brazil:saopaulo", "country-state-qns");

        //WHEN
        URI uri = defaultBDXRLocator.lookup(participantIdentifier);

        //THEN
        assertEquals("http://smp-mock-1.ehealth.eu:8888", uri.toString());
        assertEquals("2CDN5ANIHSX2W6D2ZA5YSSGR2BXVLCGTLS6STIYM2CZYHB3L7GMA.country-state-qns.ehealth.acc.edelivery.tech.ec.europa.eu", dnsRecordUrlCaptor.getValue());
    }

    @Test
    void testLookupNAPTRUpperCaseIdentifier() throws Exception {
        //GIVEN
        DefaultBDXRLocator defaultBDXRLocator = lookupNAPTR();

        //WHEN
        URI uri = defaultBDXRLocator.lookup("urn:brazil:SAOPAULO", "country-state-qns");

        //THEN
        assertEquals("http://smp-mock-1.ehealth.eu:8888", uri.toString());
        assertEquals("2CDN5ANIHSX2W6D2ZA5YSSGR2BXVLCGTLS6STIYM2CZYHB3L7GMA.country-state-qns.ehealth.acc.edelivery.tech.ec.europa.eu", dnsRecordUrlCaptor.getValue());
    }

    @Test
    void testLookupNAPTROasisPartyType() throws Exception {
        //GIVEN
        DefaultBDXRLocator defaultBDXRLocator = lookupNAPTR();

        //WHEN
        URI uri = defaultBDXRLocator.lookup("urn:brazil:saopaulo", "urn:oasis:names:tc:ebcore:partyid-type:unregistered");

        //THEN
        assertEquals("http://smp-mock-1.ehealth.eu:8888", uri.toString());
        assertEquals("XN536BJVZUJJWWJZPQN5KAM6LFPK4ZZD2VL4AXQRELT5HTCJ6LEQ.ehealth.acc.edelivery.tech.ec.europa.eu", dnsRecordUrlCaptor.getValue());
    }

    @Test
    void testLookupNAPTROasisPartyTypeNullScheme() throws Exception {
        //GIVEN
        DefaultBDXRLocator defaultBDXRLocator = lookupNAPTR();

        //WHEN
        URI uri = defaultBDXRLocator.lookup("urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn:brazil:saopaulo", null);

        //THEN
        assertEquals("http://smp-mock-1.ehealth.eu:8888", uri.toString());
        assertEquals("XN536BJVZUJJWWJZPQN5KAM6LFPK4ZZD2VL4AXQRELT5HTCJ6LEQ.ehealth.acc.edelivery.tech.ec.europa.eu", dnsRecordUrlCaptor.getValue());
    }

    @Test
    void testLookupNAPTROasisPartyTypeEmptyScheme() throws Exception {
        //GIVEN
        DefaultBDXRLocator defaultBDXRLocator = lookupNAPTR();

        //WHEN
        URI uri = defaultBDXRLocator.lookup("urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn:brazil:saopaulo", "");

        //THEN
        assertEquals("http://smp-mock-1.ehealth.eu:8888", uri.toString());
        assertEquals("XN536BJVZUJJWWJZPQN5KAM6LFPK4ZZD2VL4AXQRELT5HTCJ6LEQ.ehealth.acc.edelivery.tech.ec.europa.eu", dnsRecordUrlCaptor.getValue());
    }

    @Test
    void testLookupCNAMEPeppolPartyType() throws Exception {
        //GIVEN
        DefaultBDXRLocator defaultBDXRLocator = lookupCNAME();
        SMPParticipantIdentifier participantIdentifier
                = new SMPParticipantIdentifier("urn:brazil:saopaulo", "country-state-qns");

        //WHEN
        URI uri = defaultBDXRLocator.lookup(participantIdentifier);

        //THEN
        assertEquals("http://b-5cc29a6e1d849a3089cb7d8b192e55b7.country-state-qns.ehealth.acc.edelivery.tech.ec.europa.eu", uri.toString());
    }

    @Test
    void testLookupCNAMEOasisPartyType() throws Exception {
        //GIVEN
        DefaultBDXRLocator defaultBDXRLocator = lookupCNAME();
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("urn:brazil:saopaulo", "urn:oasis:names:tc:ebcore:partyid-type:unregistered");

        //WHEN
        URI uri = defaultBDXRLocator.lookup(participantIdentifier);

        //THEN
        assertEquals("http://b-761c04e661616234cd81659d456b0cf6.ehealth.acc.edelivery.tech.ec.europa.eu", uri.toString());
    }

    @Test
    void testLookupCNAMEOasisPartyTypeNullScheme() throws Exception {
        //GIVEN
        DefaultBDXRLocator defaultBDXRLocator = lookupCNAME();
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn:brazil:saopaulo", null);

        //WHEN
        URI uri = defaultBDXRLocator.lookup(participantIdentifier);

        //THEN
        assertEquals("http://b-761c04e661616234cd81659d456b0cf6.ehealth.acc.edelivery.tech.ec.europa.eu", uri.toString());
    }

    @Test
    void testLookupCNAMEOasisPartyTypeEmptyScheme() throws Exception {
        //GIVEN
        DefaultBDXRLocator defaultBDXRLocator = lookupCNAME();
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn:brazil:saopaulo", "");

        //WHEN
        URI uri = defaultBDXRLocator.lookup(participantIdentifier);

        //THEN
        assertEquals("http://b-761c04e661616234cd81659d456b0cf6.ehealth.acc.edelivery.tech.ec.europa.eu", uri.toString());
    }

    private DefaultBDXRLocator lookupNAPTR() throws Exception {
        DefaultBDXRLocator defaultBDXRLocator = new DefaultBDXRLocator.Builder().addTopDnsDomain("ehealth.acc.edelivery.tech.ec.europa.eu").build();
        defaultBDXRLocator = spy(defaultBDXRLocator);
        Mockito.doReturn("http://smp-mock-1.ehealth.eu:8888").when(defaultBDXRLocator).naptrLookupFetcher(any(SMPParticipantIdentifier.class), dnsRecordUrlCaptor.capture());

        return defaultBDXRLocator;
    }

    private DefaultBDXRLocator lookupCNAME() throws Exception {
        DefaultDNSLookup idnsLookup = spy(new DefaultDNSLookup.Builder().build());
        DefaultBDXRLocator defaultBDXRLocator = spy(new DefaultBDXRLocator("ehealth.acc.edelivery.tech.ec.europa.eu", idnsLookup));
        defaultBDXRLocator.getDnsLookupTypeList().clear();
        defaultBDXRLocator.getDnsLookupTypeList().add(DNSLookupType.CNAME);

        Mockito.doReturn(false).when(idnsLookup).dnsRecordNotExists(
                any(SMPParticipantIdentifier.class), dnsRecordUrlCaptor.capture(), any(DNSLookupType.class));

        return defaultBDXRLocator;
    }
}
