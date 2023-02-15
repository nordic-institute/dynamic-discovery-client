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

import eu.europa.ec.dynamicdiscovery.core.locator.dns.IDNSLookup;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.impl.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.enums.DNSLookupType;
import eu.europa.ec.dynamicdiscovery.exception.DDCRuntimeException;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.Collections;
import java.util.stream.Stream;

import static java.time.OffsetDateTime.now;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;

/**
 * @author Flávio W. R. Santos
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
class DefaultBDXRLocatorTest {


    private static Stream<Arguments> testNaptrLookupArguments() {
        return Stream.of(
                Arguments.of("testLookupNAPTR",
                        "urn:brazil:saopaulo",
                        "country-state-qns",
                        "http://smp-mock-1.ehealth.eu:8888",
                        "2CDN5ANIHSX2W6D2ZA5YSSGR2BXVLCGTLS6STIYM2CZYHB3L7GMA.country-state-qns.ehealth.acc.edelivery.tech.ec.europa.eu"),
                Arguments.of("testLookupNAPTRUpperCaseIdentifier",
                        "urn:brazil:SAOPAULO",
                        "country-state-qns",
                        "http://smp-mock-1.ehealth.eu:8888",
                        "2CDN5ANIHSX2W6D2ZA5YSSGR2BXVLCGTLS6STIYM2CZYHB3L7GMA.country-state-qns.ehealth.acc.edelivery.tech.ec.europa.eu"),
                Arguments.of("testLookupNAPTROasisPartyType",
                        "urn:brazil:saopaulo",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered",
                        "http://smp-mock-1.ehealth.eu:8888",
                        "XN536BJVZUJJWWJZPQN5KAM6LFPK4ZZD2VL4AXQRELT5HTCJ6LEQ.ehealth.acc.edelivery.tech.ec.europa.eu"),
                Arguments.of("testLookupNAPTROasisPartyTypeEmptyScheme",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn:brazil:saopaulo",
                        null,
                        "http://smp-mock-1.ehealth.eu:8888",
                        "XN536BJVZUJJWWJZPQN5KAM6LFPK4ZZD2VL4AXQRELT5HTCJ6LEQ.ehealth.acc.edelivery.tech.ec.europa.eu")
        );
    }

    private static Stream<Arguments> testCNameLookupArguments() {
        return Stream.of(
                Arguments.of("testLookupCName",
                        "urn:brazil:saopaulo",
                        "country-state-qns",
                        "http://b-5cc29a6e1d849a3089cb7d8b192e55b7.country-state-qns.ehealth.acc.edelivery.tech.ec.europa.eu"),
                Arguments.of("testLookupCNameCaseInsentitive",
                        "urn:BRAZIL:saoPaulo",
                        "country-state-qns",
                        "http://b-5cc29a6e1d849a3089cb7d8b192e55b7.country-state-qns.ehealth.acc.edelivery.tech.ec.europa.eu"),
                Arguments.of("testLookupCNAMEOasisPartyType",
                        "urn:brazil:saopaulo",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered",
                        "http://b-761c04e661616234cd81659d456b0cf6.ehealth.acc.edelivery.tech.ec.europa.eu"),
                Arguments.of("testLookupCNAMEOasisPartyTypeNullScheme",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn:brazil:saopaulo",
                        null,
                        "http://b-761c04e661616234cd81659d456b0cf6.ehealth.acc.edelivery.tech.ec.europa.eu")

        );
    }

    @Captor
    ArgumentCaptor<String> dnsRecordUrlCaptor;

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("testNaptrLookupArguments")
    void testLookupNAPTR(String name, String partyId, String partyScheme, String expectedUrl, String expectedDomain) throws Exception {
        //GIVEN
        DefaultBDXRLocator defaultBDXRLocator = lookupNAPTR();
        //WHEN
        URI uri = defaultBDXRLocator.lookup(partyId,partyScheme );
        //THEN
        assertEquals(expectedUrl, uri.toString());
        assertEquals(expectedDomain, dnsRecordUrlCaptor.getValue());
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("testNaptrLookupArguments")
    void testLookupNAPTRWithIdentifier(String name, String partyId, String partyScheme, String expectedUrl, String expectedDomain) throws Exception {
        //GIVEN
        SMPParticipantIdentifier identifier = new SMPParticipantIdentifier(partyId, partyScheme);
        DefaultBDXRLocator defaultBDXRLocator = lookupNAPTR();
        //WHEN
        URI uri = defaultBDXRLocator.lookup(identifier);
        //THEN
        assertEquals(expectedUrl, uri.toString());
        assertEquals(expectedDomain, dnsRecordUrlCaptor.getValue());
    }


    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("testCNameLookupArguments")
    void testLookupCNAME(String name, String partyId, String partyScheme, String expectedDomain) throws Exception {
        //GIVEN
        DefaultBDXRLocator defaultBDXRLocator = lookupCNAME();

        //WHEN
        URI uri = defaultBDXRLocator.lookup(partyId, partyScheme);

        //THEN
        assertEquals(expectedDomain, uri.toString());
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("testCNameLookupArguments")
    void testLookupCNAMEWithIdentifier(String name, String partyId, String partyScheme, String expectedDomain) throws Exception {
        //GIVEN
        SMPParticipantIdentifier identifier = new SMPParticipantIdentifier(partyId, partyScheme);
        DefaultBDXRLocator defaultBDXRLocator = lookupCNAME();

        //WHEN
        URI uri = defaultBDXRLocator.lookup(identifier);

        //THEN
        assertEquals(expectedDomain, uri.toString());
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

    @Test
    void testConfigurationMissingTopDomain() {
        DDCRuntimeException result = assertThrows(DDCRuntimeException.class,
                () -> new DefaultBDXRLocator.Builder().build());

        assertEquals("List of top domains must not be empty!", result.getMessage());
    }

    @Test
    void testDefaultConfiguration() {
        DefaultBDXRLocator testInstance = new DefaultBDXRLocator.Builder()
                .addTopDnsDomain("test.top.local")
                .build();
        assertEquals(1, testInstance.getTopDnsDomains().size());
        assertEquals(2, testInstance.getDnsLookupTypeList().size());
        assertNotNull(testInstance.getDnsLookup());
    }

    @Test
    void testConfiguration() {
        IDNSLookup mockLookup = Mockito.mock(IDNSLookup.class);
        DefaultBDXRLocator testInstance = new DefaultBDXRLocator.Builder()
                .addTopDnsDomain("test.top.local")
                .addDnsLookupType(DNSLookupType.NAPTR)
                .dnsLookup(mockLookup)
                .build();

        assertEquals(1, testInstance.getTopDnsDomains().size());
        assertEquals(1, testInstance.getDnsLookupTypeList().size());
        assertEquals("test.top.local", testInstance.getTopDnsDomains().get(0));
        assertEquals(DNSLookupType.NAPTR, testInstance.getDnsLookupTypeList().get(0));
        assertEquals(mockLookup, testInstance.getDnsLookup());
    }

    @Test
    void testConfigurationAddList() {
        IDNSLookup mockLookup = Mockito.mock(IDNSLookup.class);
        DefaultBDXRLocator testInstance = new DefaultBDXRLocator.Builder()
                .addTopDnsDomains(Collections.singletonList("test.top.local"))
                .addDnsLookupTypes(Collections.singletonList(DNSLookupType.NAPTR))
                .build();

        assertEquals(1, testInstance.getTopDnsDomains().size());
        assertEquals(1, testInstance.getDnsLookupTypeList().size());
        assertEquals("test.top.local", testInstance.getTopDnsDomains().get(0));
        assertEquals(DNSLookupType.NAPTR, testInstance.getDnsLookupTypeList().get(0));
    }
}
