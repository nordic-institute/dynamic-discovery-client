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
import eu.europa.ec.dynamicdiscovery.enums.DNSLookupFormatType;
import eu.europa.ec.dynamicdiscovery.enums.DNSLookupType;
import eu.europa.ec.dynamicdiscovery.exception.DDCRuntimeException;
import eu.europa.ec.dynamicdiscovery.exception.MalformedIdentifierException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.types.FormatterType;
import eu.europa.ec.dynamicdiscovery.model.identifiers.types.TemplateFormatterType;
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
import java.util.regex.Pattern;
import java.util.stream.Stream;

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
                        null,
                        "http://smp-mock-1.ehealth.eu:8888",
                        "2CDN5ANIHSX2W6D2ZA5YSSGR2BXVLCGTLS6STIYM2CZYHB3L7GMA.country-state-qns.ehealth.acc.edelivery.tech.ec.europa.eu"),
                Arguments.of("testLookupNAPTR wildcard",
                        "*",
                        "country-state-qns",
                        null,
                        "http://smp-mock-1.ehealth.eu:8888",
                        "*.country-state-qns.ehealth.acc.edelivery.tech.ec.europa.eu"),
                Arguments.of("testLookupNAPTRCaseInsensitiveScheme",
                        "urn:brazil:SAOPAULO",
                        "country-state-qns",
                        null,
                        "http://smp-mock-1.ehealth.eu:8888",
                        "2CDN5ANIHSX2W6D2ZA5YSSGR2BXVLCGTLS6STIYM2CZYHB3L7GMA.country-state-qns.ehealth.acc.edelivery.tech.ec.europa.eu"),
                Arguments.of("testLookupNAPTRCaseSensitiveScheme",
                        "urn:brazil:SAOPAULO",
                        "country-state-qns",
                        "country-state-qns",
                        "http://smp-mock-1.ehealth.eu:8888",
                        "CLUO32PLJFDWG7L4MMX63A7GZSOVTBUGACULASCQO7JEGFWE4GTA.country-state-qns.ehealth.acc.edelivery.tech.ec.europa.eu"),
                Arguments.of("testLookupNAPTROasisPartyType",
                        "urn:brazil:saopaulo",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered",
                        null,
                        "http://smp-mock-1.ehealth.eu:8888",
                        "XN536BJVZUJJWWJZPQN5KAM6LFPK4ZZD2VL4AXQRELT5HTCJ6LEQ.ehealth.acc.edelivery.tech.ec.europa.eu"),
                Arguments.of("testLookupNAPTROasisPartyTypeNormalized",
                        "brazil:saopaulo",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn",
                        null,
                        "http://smp-mock-1.ehealth.eu:8888",
                        "XN536BJVZUJJWWJZPQN5KAM6LFPK4ZZD2VL4AXQRELT5HTCJ6LEQ.ehealth.acc.edelivery.tech.ec.europa.eu"),
                Arguments.of("testLookupNAPTROasisPartyTypeCaseInsensitive",
                        "BRAZIL:saopaulo",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn",
                        null,
                        "http://smp-mock-1.ehealth.eu:8888",
                        "XN536BJVZUJJWWJZPQN5KAM6LFPK4ZZD2VL4AXQRELT5HTCJ6LEQ.ehealth.acc.edelivery.tech.ec.europa.eu"),
                Arguments.of("testLookupNAPTROasisPartyTypeCaseSensitive",
                        "BRAZIL:saopaulo",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn",
                        "http://smp-mock-1.ehealth.eu:8888",
                        "CSIEEUZW4CTHXGR2O5NCGGWYV7KBUGCNPPPVIMJOMTNMJ7BUEEYQ.ehealth.acc.edelivery.tech.ec.europa.eu"),
                Arguments.of("testLookupNAPTROasisPartyTypeEmptyScheme",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn:brazil:saopaulo",
                        null,
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
                        null,
                        "http://b-5cc29a6e1d849a3089cb7d8b192e55b7.country-state-qns.ehealth.acc.edelivery.tech.ec.europa.eu"),
                Arguments.of("testLookupCNameCaseInsentitive",
                        "urn:BRAZIL:saoPaulo",
                        "country-state-qns",
                        null,
                        "http://b-5cc29a6e1d849a3089cb7d8b192e55b7.country-state-qns.ehealth.acc.edelivery.tech.ec.europa.eu"),
                Arguments.of("testLookupCNameCaseSentitive",
                        "urn:BRAZIL:saoPaulo",
                        "country-state-qns",
                        "country-state-qns",
                        "http://b-66dc923ee75a737cae33562297567763.country-state-qns.ehealth.acc.edelivery.tech.ec.europa.eu"),
                Arguments.of("testLookupCNAMEOasisPartyType",
                        "urn:brazil:saopaulo",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered",
                        null,
                        "http://b-761c04e661616234cd81659d456b0cf6.ehealth.acc.edelivery.tech.ec.europa.eu"),
                Arguments.of("testLookupCNAMEOasisPartyTypeNormalized",
                        "brazil:saopaulo",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn",
                        null,
                        "http://b-761c04e661616234cd81659d456b0cf6.ehealth.acc.edelivery.tech.ec.europa.eu"),
                Arguments.of("testLookupCNAMEOasisPartyTypeNormalizedCaseInsensitive",
                        "BRAZIL:saopaulo",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn",
                        null,
                        "http://b-761c04e661616234cd81659d456b0cf6.ehealth.acc.edelivery.tech.ec.europa.eu"),
                Arguments.of("testLookupCNAMEOasisPartyTypeNormalizedCaseSensitie",
                        "BRAZIL:saopaulo",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn",
                        "http://b-dbb71b7c50103af1f3bb6dc67e3b5781.ehealth.acc.edelivery.tech.ec.europa.eu"),
                Arguments.of("testLookupCNAMEOasisPartyTypeNullScheme",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn:brazil:saopaulo",
                        null,
                        null,
                        "http://b-761c04e661616234cd81659d456b0cf6.ehealth.acc.edelivery.tech.ec.europa.eu")
        );
    }

    private static Stream<Arguments> testWildCardTests() {
        return Stream.of(
                Arguments.of("Enable for peppol type schema ",
                        true,
                        "valid-Scheme-value",
                        "ehealth.acc.edelivery.tech.ec.europa.eu",
                        "*.valid-scheme-value.ehealth.acc.edelivery.tech.ec.europa.eu"),
                Arguments.of("Null schema",
                        true,
                        null,
                        "ehealth.acc.edelivery.tech.ec.europa.eu",
                        "*.ehealth.acc.edelivery.tech.ec.europa.eu"),
                Arguments.of("ebCoreParty schema",
                        true,
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered",
                        "ehealth.acc.edelivery.tech.ec.europa.eu",
                        "*.ehealth.acc.edelivery.tech.ec.europa.eu"),
                Arguments.of("ebCoreParty schema disabled",
                        false,
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered",
                        "ehealth.acc.edelivery.tech.ec.europa.eu",
                        "NPTVY7VWFTCMVQSPQFM535B5PIAZ4H62CAESQJDEB3JCHF3VTHNQ.ehealth.acc.edelivery.tech.ec.europa.eu"),
                Arguments.of("ebCoreParty schema disabled",
                        false,
                        "valid-scheme-value",
                        "ehealth.acc.edelivery.tech.ec.europa.eu",
                        "NBEIRQHLWF7TOQUYWZPOFADVE3AGMCKMOAN4Y7V34HAQSX2JJ7AQ.valid-scheme-value.ehealth.acc.edelivery.tech.ec.europa.eu")
        );
    }

    @Captor
    ArgumentCaptor<String> dnsRecordUrlCaptor;

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("testNaptrLookupArguments")
    void testLookupNAPTR(String name, String partyId, String partyScheme, String caseSensitiveScheme, String expectedUrl, String expectedDomain) throws Exception {
        //GIVEN
        DefaultBDXRLocator defaultBDXRLocator = lookupNAPTR(caseSensitiveScheme);
        //WHEN
        URI uri = defaultBDXRLocator.lookup(partyId, partyScheme);
        //THEN
        assertEquals(expectedUrl, uri.toString());
        assertEquals(expectedDomain, dnsRecordUrlCaptor.getValue());
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("testNaptrLookupArguments")
    void testLookupNAPTRWithIdentifier(String name, String partyId, String partyScheme, String caseSensitiveScheme, String expectedUrl, String expectedDomain) throws Exception {
        //GIVEN
        SMPParticipantIdentifier identifier = new SMPParticipantIdentifier(partyId, partyScheme);
        DefaultBDXRLocator defaultBDXRLocator = lookupNAPTR(caseSensitiveScheme);
        //WHEN
        URI uri = defaultBDXRLocator.lookup(identifier);
        //THEN
        assertEquals(expectedUrl, uri.toString());
        assertEquals(expectedDomain, dnsRecordUrlCaptor.getValue());
    }


    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("testCNameLookupArguments")
    void testLookupCNAME(String name, String partyId, String partyScheme, String caseSensitiveScheme, String expectedDomain) throws Exception {
        //GIVEN
        DefaultBDXRLocator defaultBDXRLocator = lookupCNAME(caseSensitiveScheme);

        //WHEN
        URI uri = defaultBDXRLocator.lookup(partyId, partyScheme);

        //THEN
        assertEquals(expectedDomain, uri.toString());
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("testCNameLookupArguments")
    void testLookupCNAMEWithIdentifier(String name, String partyId, String partyScheme, String caseSensitive, String expectedDomain) throws Exception {
        //GIVEN
        SMPParticipantIdentifier identifier = new SMPParticipantIdentifier(partyId, partyScheme);
        DefaultBDXRLocator defaultBDXRLocator = lookupCNAME(caseSensitive);

        //WHEN
        URI uri = defaultBDXRLocator.lookup(identifier);

        //THEN
        assertEquals(expectedDomain, uri.toString());
    }

    private DefaultBDXRLocator lookupNAPTR(String caseSensitiveScheme) throws Exception {
        DefaultBDXRLocator defaultBDXRLocator = new DefaultBDXRLocator.Builder()
                .addTopDnsDomain("ehealth.acc.edelivery.tech.ec.europa.eu")
                .addCaseSensitiveSchema(caseSensitiveScheme)
                .build();
        defaultBDXRLocator = spy(defaultBDXRLocator);
        Mockito.doReturn("http://smp-mock-1.ehealth.eu:8888").when(defaultBDXRLocator).naptrLookupFetcher(any(SMPParticipantIdentifier.class), dnsRecordUrlCaptor.capture());

        return defaultBDXRLocator;
    }

    private DefaultBDXRLocator lookupCNAME(String caseSensitiveScheme) throws Exception {
        DefaultDNSLookup idnsLookup = spy(new DefaultDNSLookup.Builder().build());
        DefaultBDXRLocator defaultBDXRLocator = new DefaultBDXRLocator.Builder()
                .addTopDnsDomain("ehealth.acc.edelivery.tech.ec.europa.eu")
                .addDnsLookupType(DNSLookupType.CNAME)
                .dnsLookup(idnsLookup)
                .addCaseSensitiveSchema(caseSensitiveScheme)
                .build();

        Mockito.doReturn(false).when(idnsLookup).dnsRecordNotExists(
                any(SMPParticipantIdentifier.class), dnsRecordUrlCaptor.capture(), any(DNSLookupType.class));

        return defaultBDXRLocator;
    }

    @Test
    void testInvalidScheme() {
        DefaultBDXRLocator defaultBDXRLocator = new DefaultBDXRLocator.Builder()
                .addTopDnsDomain("ehealth.acc.edelivery.tech.ec.europa.eu")
                .schemeValidationPattern(Pattern.compile("just-this-scheme"))
                .build();

        MalformedIdentifierException result = assertThrows(MalformedIdentifierException.class,
                () -> defaultBDXRLocator.lookup("identifier", "wrong-this-scheme"));

        assertEquals("Invalid Identifier: [wrong-this-scheme::identifier]. Scheme does not match pattern: [just-this-scheme]!",
                result.getMessage());

    }

    @Test
    void testValidScheme() throws TechnicalException {
        DefaultBDXRLocator defaultBDXRLocator = new DefaultBDXRLocator.Builder()
                .addTopDnsDomain("ehealth.acc.edelivery.tech.ec.europa.eu")
                .schemeValidationPattern(Pattern.compile("just-this-scheme"))
                .build();
        defaultBDXRLocator = spy(defaultBDXRLocator);
        Mockito.doReturn("http://smp-mock-1.ehealth.eu:8888").when(defaultBDXRLocator).naptrLookupFetcher(any(SMPParticipantIdentifier.class), dnsRecordUrlCaptor.capture());
        // when
        URI uri = defaultBDXRLocator.lookup("identifier", "just-this-scheme");
        //THEN
        assertEquals("http://smp-mock-1.ehealth.eu:8888", uri.toString());
    }

    @Test
    void testValidNoScheme() throws TechnicalException {
        DefaultBDXRLocator defaultBDXRLocator = new DefaultBDXRLocator.Builder()
                .addTopDnsDomain("ehealth.acc.edelivery.tech.ec.europa.eu")
                .build();
        defaultBDXRLocator = spy(defaultBDXRLocator);
        Mockito.doReturn("http://smp-mock-1.ehealth.eu:8888").when(defaultBDXRLocator).naptrLookupFetcher(any(SMPParticipantIdentifier.class), dnsRecordUrlCaptor.capture());
        // when
        URI uri = defaultBDXRLocator.lookup("valid-No-Scheme-Identifier", null);
        //THEN
        assertEquals("R75GQKYPOXUS66JUTXJNIQQCGKNVYSJCZJU27OKQJBWBXOV3S3BA.ehealth.acc.edelivery.tech.ec.europa.eu", dnsRecordUrlCaptor.getValue());
        assertEquals("http://smp-mock-1.ehealth.eu:8888", uri.toString());
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("testWildCardTests")
    void testWildcardEnabled(String name, boolean enable, String schema, String topDomain, String expectedResult) throws TechnicalException {
        DefaultBDXRLocator defaultBDXRLocator = new DefaultBDXRLocator.Builder()
                .addTopDnsDomain(topDomain)
                .wildcardEnabled(enable)
                .build();
        String targetUrl = "http://smp-mock-1.ehealth.eu:8888";
        defaultBDXRLocator = spy(defaultBDXRLocator);
        Mockito.doReturn(targetUrl).when(defaultBDXRLocator).naptrLookupFetcher(any(SMPParticipantIdentifier.class), dnsRecordUrlCaptor.capture());
        // when
        URI uri = defaultBDXRLocator.lookup("*", schema);
        //THEN
        assertEquals(expectedResult, dnsRecordUrlCaptor.getValue());
        assertEquals(targetUrl, uri.toString());
    }

    @Test
    void testMandatoryScheme() throws TechnicalException {
        DefaultBDXRLocator defaultBDXRLocator = new DefaultBDXRLocator.Builder()
                .addTopDnsDomain("ehealth.acc.edelivery.tech.ec.europa.eu")
                .schemeMandatory(true)
                .build();

        MalformedIdentifierException result = assertThrows(MalformedIdentifierException.class, () -> defaultBDXRLocator.lookup("valid-No-Scheme-Identifier", null));
        //THEN
        assertEquals("Invalid Identifier: [::valid-No-Scheme-Identifier]. Can not detect schema!", result.getMessage());
    }


    @Test
    void testCustomFormatter() throws TechnicalException {

        FormatterType customFormatter = new TemplateFormatterType(Pattern.compile("^(?i)mailto.*$"),
                "${scheme}-->${identifier}", "${identifier}",
                Pattern.compile("^(?i)\\s*(-->)?(?<scheme>mailto)-->(?<identifier>.+)?\\s*$"),
                DNSLookupFormatType.SCHEMA_AFTER_HASH);


        DefaultBDXRLocator defaultBDXRLocator = new DefaultBDXRLocator.Builder()
                .addTopDnsDomain("ehealth.acc.edelivery.tech.ec.europa.eu")
                .addFormatterType(customFormatter)
                .build();

        defaultBDXRLocator = spy(defaultBDXRLocator);
        Mockito.doReturn("http://smp-mock-1.ehealth.eu:8888").when(defaultBDXRLocator)
                .naptrLookupFetcher(any(SMPParticipantIdentifier.class), dnsRecordUrlCaptor.capture());
        // when
        URI uri = defaultBDXRLocator.lookup("identifier", "mailto");
        //THEN
        assertEquals("FBKDPW4MLYKUDKSJOCFUVPKRMQVAMW3G4S5MBEA6OW2EZYWQZ27A.mailto.ehealth.acc.edelivery.tech.ec.europa.eu", dnsRecordUrlCaptor.getValue());
        assertEquals("http://smp-mock-1.ehealth.eu:8888", uri.toString());
    }

    @Test
    void testCustomFormatterNormalize() throws TechnicalException {

        FormatterType customFormatter = new TemplateFormatterType(Pattern.compile("^(?i)mailto.*$"),
                "${scheme}-->${identifier}", "${identifier}",
                Pattern.compile("^(?i)\\s*(?<scheme>mailto)-->(?<identifier>.+)?\\s*$"),
                DNSLookupFormatType.SCHEMA_AFTER_HASH);


        DefaultBDXRLocator defaultBDXRLocator = new DefaultBDXRLocator.Builder()
                .addTopDnsDomain("ehealth.acc.edelivery.tech.ec.europa.eu")
                .addFormatterType(customFormatter)
                .build();

        defaultBDXRLocator = spy(defaultBDXRLocator);
        Mockito.doReturn("http://smp-mock-1.ehealth.eu:8888").when(defaultBDXRLocator)
                .naptrLookupFetcher(any(SMPParticipantIdentifier.class), dnsRecordUrlCaptor.capture());
        // when
        URI uri = defaultBDXRLocator.lookup("mailto-->identifier", null);
        //THEN
        assertEquals("FBKDPW4MLYKUDKSJOCFUVPKRMQVAMW3G4S5MBEA6OW2EZYWQZ27A.mailto.ehealth.acc.edelivery.tech.ec.europa.eu", dnsRecordUrlCaptor.getValue());
        assertEquals("http://smp-mock-1.ehealth.eu:8888", uri.toString());
    }

    @Test
    void testConfigurationMissingTopDomain() {
        DefaultBDXRLocator.Builder testInstance = new DefaultBDXRLocator.Builder();

        DDCRuntimeException result = assertThrows(DDCRuntimeException.class,
                () -> testInstance.build());

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
