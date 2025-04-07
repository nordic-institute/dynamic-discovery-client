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
package eu.europa.ec.dynamicdiscovery.core.locator.impl;

import eu.europa.ec.dynamicdiscovery.core.locator.PublisherLookupResult;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.IDNSLookup;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.impl.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.enums.DNSLookupFormatType;
import eu.europa.ec.dynamicdiscovery.enums.DNSLookupType;
import eu.europa.ec.dynamicdiscovery.exception.DDCRuntimeException;
import eu.europa.ec.dynamicdiscovery.exception.MalformedIdentifierException;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.types.FormatterType;
import eu.europa.ec.dynamicdiscovery.model.identifiers.types.TemplateFormatterType;
import eu.europa.ec.dynamicdiscovery.util.DNSUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.xbill.DNS.NAPTRRecord;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static eu.europa.ec.dynamicdiscovery.util.DNSUtils.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Flávio W. R. Santos
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
class DefaultBDXRLocatorTest {

    private static final String TEST_IDENTIFIER_SCHEME_PEPPOL = "iso6523-actorid-upis";
    private static final String TEST_IDENTIFIER_VALUE_PEPPOL = "0088:123456789";

    private static Stream<Arguments> testNaptrLookupArguments() {
        return Stream.of(
                Arguments.of("testLookupNAPTR",
                        "urn:brazil:saopaulo",
                        "country-state-qns",
                        null,
                        "2CDN5ANIHSX2W6D2ZA5YSSGR2BXVLCGTLS6STIYM2CZYHB3L7GMA.country-state-qns." + TEST_TOP_DOMAIN_02),
                Arguments.of("testLookupNAPTR wildcard",
                        "*",
                        "country-state-qns",
                        null,
                        "*.country-state-qns." + TEST_TOP_DOMAIN_02),
                Arguments.of("testLookupNAPTRCaseInsensitiveScheme",
                        "urn:brazil:SAOPAULO",
                        "country-state-qns",
                        null,
                        "2CDN5ANIHSX2W6D2ZA5YSSGR2BXVLCGTLS6STIYM2CZYHB3L7GMA.country-state-qns." + TEST_TOP_DOMAIN_02),
                Arguments.of("testLookupNAPTRCaseSensitiveScheme",
                        "urn:brazil:SAOPAULO",
                        "country-state-qns",
                        "country-state-qns",
                        "CLUO32PLJFDWG7L4MMX63A7GZSOVTBUGACULASCQO7JEGFWE4GTA.country-state-qns." + TEST_TOP_DOMAIN_02),
                Arguments.of("testLookupNAPTROasisPartyType",
                        "urn:brazil:saopaulo",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered",
                        null,
                        "XN536BJVZUJJWWJZPQN5KAM6LFPK4ZZD2VL4AXQRELT5HTCJ6LEQ." + TEST_TOP_DOMAIN_02),
                Arguments.of("testLookupNAPTROasisPartyTypeNormalized",
                        "brazil:saopaulo",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn",
                        null,
                        "XN536BJVZUJJWWJZPQN5KAM6LFPK4ZZD2VL4AXQRELT5HTCJ6LEQ." + TEST_TOP_DOMAIN_02),
                Arguments.of("testLookupNAPTROasisPartyTypeCaseInsensitive",
                        "BRAZIL:saopaulo",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn",
                        null,
                        "XN536BJVZUJJWWJZPQN5KAM6LFPK4ZZD2VL4AXQRELT5HTCJ6LEQ." + TEST_TOP_DOMAIN_02),
                Arguments.of("testLookupNAPTROasisPartyTypeCaseSensitive",
                        "BRAZIL:saopaulo",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn",
                        "CSIEEUZW4CTHXGR2O5NCGGWYV7KBUGCNPPPVIMJOMTNMJ7BUEEYQ." + TEST_TOP_DOMAIN_02),
                Arguments.of("testLookupNAPTROasisPartyTypeEmptyScheme",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn:brazil:saopaulo",
                        null,
                        null,
                        "XN536BJVZUJJWWJZPQN5KAM6LFPK4ZZD2VL4AXQRELT5HTCJ6LEQ." + TEST_TOP_DOMAIN_02)
        );
    }

    private static Stream<Arguments> testCNameLookupArguments() {
        return Stream.of(
                Arguments.of("testLookupCName",
                        "urn:brazil:saopaulo",
                        "country-state-qns",
                        null,
                        "B-5cc29a6e1d849a3089cb7d8b192e55b7.country-state-qns." + TEST_TOP_DOMAIN_02),
                Arguments.of("testLookupCNameCaseInsentitive",
                        "urn:BRAZIL:saoPaulo",
                        "country-state-qns",
                        null,
                        "B-5cc29a6e1d849a3089cb7d8b192e55b7.country-state-qns." + TEST_TOP_DOMAIN_02),
                Arguments.of("testLookupCNameCaseSentitive",
                        "urn:BRAZIL:saoPaulo",
                        "country-state-qns",
                        "country-state-qns",
                        "B-66dc923ee75a737cae33562297567763.country-state-qns." + TEST_TOP_DOMAIN_02),
                Arguments.of("testLookupCNAMEOasisPartyType",
                        "urn:brazil:saopaulo",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered",
                        null,
                        "B-761c04e661616234cd81659d456b0cf6." + TEST_TOP_DOMAIN_02),
                Arguments.of("testLookupCNAMEOasisPartyTypeNormalized",
                        "brazil:saopaulo",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn",
                        null,
                        "B-761c04e661616234cd81659d456b0cf6." + TEST_TOP_DOMAIN_02),
                Arguments.of("testLookupCNAMEOasisPartyTypeNormalizedCaseInsensitive",
                        "BRAZIL:saopaulo",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn",
                        null,
                        "B-761c04e661616234cd81659d456b0cf6." + TEST_TOP_DOMAIN_02),
                Arguments.of("testLookupCNAMEOasisPartyTypeNormalizedCaseSensitie",
                        "BRAZIL:saopaulo",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn",
                        "B-dbb71b7c50103af1f3bb6dc67e3b5781." + TEST_TOP_DOMAIN_02),
                Arguments.of("testLookupCNAMEOasisPartyTypeNullScheme",
                        "urn:oasis:names:tc:ebcore:partyid-type:unregistered:urn:brazil:saopaulo",
                        null,
                        null,
                        "B-761c04e661616234cd81659d456b0cf6." + TEST_TOP_DOMAIN_02)
        );
    }

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("testNaptrLookupArguments")
    void testLookupNAPTRWithIdentifierValueAndScheme(String name, String partyId, String partyScheme, String caseSensitiveScheme, String expectedDomain) throws Exception {
        //GIVEN
        String publisherID = "http://" + UUID.randomUUID() + ".eu:8888/";
        DefaultBDXRLocator testInstance = buildPublisherLocator(caseSensitiveScheme, DNSLookupType.NAPTR, TEST_NAPTR_SERVICE_SMP1, publisherID);
        //WHEN
        List<PublisherLookupResult> results = testInstance.lookup(partyId, partyScheme);
        //THEN
        assertEquals(1, results.size());
        assertEquals(publisherID, results.get(0).getUrl().toString());
        verify(testInstance).naptrLookupForDomain(any(SMPParticipantIdentifier.class), stringArgumentCaptor.capture());
        assertEquals(expectedDomain, stringArgumentCaptor.getValue());
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("testNaptrLookupArguments")
    void testLookupNAPTRWithIdentifier(String name, String partyId, String partyScheme, String caseSensitiveScheme, String expectedDomain) throws Exception {
        //GIVEN
        String publisherURL = "http://" + UUID.randomUUID() + ".eu:8888/";
        DefaultBDXRLocator testInstance = buildPublisherLocator(caseSensitiveScheme, DNSLookupType.NAPTR, "Meta:SMP", publisherURL);
        SMPParticipantIdentifier identifier = new SMPParticipantIdentifier(partyId, partyScheme);
        //WHEN
        List<PublisherLookupResult> results = testInstance.lookup(identifier);
        //THEN
        assertEquals(1, results.size());
        assertEquals(publisherURL, results.get(0).getUrl().toString());
        verify(testInstance).naptrLookupForDomain(any(SMPParticipantIdentifier.class), stringArgumentCaptor.capture());
        assertEquals(expectedDomain, stringArgumentCaptor.getValue());
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("testCNameLookupArguments")
    void testLookupCNAMEWithIdentifierValueAndScheme(String name, String partyId, String partyScheme, String caseSensitiveScheme, String expectedDomain) throws Exception {
        //GIVEN
        DefaultBDXRLocator testInstance = buildPublisherLocator(caseSensitiveScheme, DNSLookupType.CNAME, null, null);
        //WHEN
        List<PublisherLookupResult> results = testInstance.lookup(partyId, partyScheme);
        //THEN
        assertEquals(1, results.size());
        verify(testInstance.getDnsLookup())
                .dnsRecordExists(any(SMPParticipantIdentifier.class), stringArgumentCaptor.capture(), eq(DNSLookupType.CNAME));
        assertEquals(expectedDomain, stringArgumentCaptor.getValue());
        assertEquals("http://" + expectedDomain + "/", results.get(0).getUrl().toString());
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("testCNameLookupArguments")
    void testLookupCNAMEWithIdentifier(String name, String partyId, String partyScheme, String caseSensitiveScheme, String expectedDomain) throws Exception {
        //GIVEN
        DefaultBDXRLocator testInstance = buildPublisherLocator(caseSensitiveScheme, DNSLookupType.CNAME, null, null);
        SMPParticipantIdentifier identifier = new SMPParticipantIdentifier(partyId, partyScheme);
        //WHEN
        List<PublisherLookupResult> results = testInstance.lookup(identifier);
        //THEN
        assertEquals(1, results.size());
        verify(testInstance.getDnsLookup())
                .dnsRecordExists(any(SMPParticipantIdentifier.class), stringArgumentCaptor.capture(), eq(DNSLookupType.CNAME));
        assertEquals(expectedDomain, stringArgumentCaptor.getValue());
        assertEquals("http://" + expectedDomain + "/", results.get(0).getUrl().toString());
    }

    @ParameterizedTest
    @CsvSource({"ResourceSchemeMandatory enabled, true, true",
            "ResourceSchemeMandatory disabled, false, false"})
    void testResourceSchemeMandatory(String name, boolean enable, boolean throwError) throws Exception {

        // no need to add mockito stubs when error is expected
        boolean addDNSClientMock = !throwError;
        final DefaultBDXRLocator testInstance = buildNaptrPublisherLocator(addDNSClientMock);
        testInstance.getResourceIdentifierFormatter().setSchemeMandatory(enable);

        if (throwError) {
            //WHEN
            MalformedIdentifierException result = assertThrows(MalformedIdentifierException.class,
                    () -> testInstance.lookup(TEST_IDENTIFIER_VALUE_PEPPOL, null));
            // then
            assertThat(result.getMessage(), containsString("Invalid Identifier"));
        } else {
            //WHEN
            List<PublisherLookupResult> results = testInstance.lookup(TEST_IDENTIFIER_VALUE_PEPPOL, null);
            // then
            assertEquals(1, results.size());
            verify(testInstance).naptrLookupForDomain(any(SMPParticipantIdentifier.class), stringArgumentCaptor.capture());
            assertEquals("OWX2CZAOQ6EB4UMD3GCVN36IF5KXBTEMIBNB3HAY3ZJFWYOU4J7A.ehealth.acc.edelivery.tech.ec.europa.eu", stringArgumentCaptor.getValue());
            assertEquals(TEST_PUBLISHER_URL, results.get(0).getUrl().toString());
        }
    }

    @ParameterizedTest
    @CsvSource({"Wildcard enabled, true,iso6523-actorid-upis, *.iso6523-actorid-upis.ehealth.acc.edelivery.tech.ec.europa.eu",
            "Wildcard enabled, true,, *.ehealth.acc.edelivery.tech.ec.europa.eu",
            "Wildcard enabled, true, urn:oasis:names:tc:ebcore:partyid-type:unregistered, *.ehealth.acc.edelivery.tech.ec.europa.eu",
            "Wildcard enabled, false, urn:oasis:names:tc:ebcore:partyid-type:unregistered, B-5ca89d645e0c61390281b8d7f857fd3c.ehealth.acc.edelivery.tech.ec.europa.eu",
            "Wildcard disabled, false,iso6523-actorid-upis, B-3389dae361af79b04c9c8e7057f60cc6.iso6523-actorid-upis.ehealth.acc.edelivery.tech.ec.europa.eu"})
    void testWildcardCNAMEEnabled(String name, boolean enable, String idScheme, String expectedDomain) throws Exception {
        //GIVEN
        String targetUrl = TEST_PUBLISHER_URL;
        final DefaultBDXRLocator testInstance = buildPublisherLocator(null, DNSLookupType.CNAME, null, null);
        testInstance.getResourceIdentifierFormatter().setWildcardEnabled(enable);

        //WHEN
        List<PublisherLookupResult> results = testInstance.lookup("*", idScheme);
        //THEN
        assertEquals(1, results.size());
        verify(testInstance.getDnsLookup()).dnsRecordExists(any(SMPParticipantIdentifier.class),
                stringArgumentCaptor.capture(), eq(DNSLookupType.CNAME));
        assertEquals(expectedDomain, stringArgumentCaptor.getValue());
    }

    @ParameterizedTest
    @CsvSource({
            "just-this-scheme, just-this-scheme, false, ",
            ".*-scheme, just-this-scheme, false, ",
            "(just-this-scheme|just-that-scheme), just-this-scheme, false, ",
            "just-this-scheme, wrong-this-scheme, true,  has invalid scheme [wrong-this-scheme]",
            "just-this-scheme, wrong-this-scheme, true,   has invalid scheme [wrong-this-scheme]"})
    void testInvalidScheme(String pattern, String scheme, boolean throwException, String expectedMessage) throws Exception {

        DefaultBDXRLocator testInstance = buildNaptrPublisherLocator(!throwException);
        testInstance.getResourceIdentifierFormatter().setSchemeValidationPattern(Pattern.compile(pattern));

        if (throwException) {
            MalformedIdentifierException result = assertThrows(MalformedIdentifierException.class,
                    () -> testInstance.lookup(TEST_IDENTIFIER_VALUE_PEPPOL, scheme));

            assertThat(result.getMessage(), containsString(expectedMessage));
        } else {
            //WHEN
            List<PublisherLookupResult> results = testInstance.lookup(TEST_IDENTIFIER_VALUE_PEPPOL, scheme);
            //THEN
            assertEquals(1, results.size());
        }
    }


    @ParameterizedTest
    @CsvSource({"'^(?i)mailto.*$','${scheme}-->${identifier}','${identifier}','^(?i)\\s*(-->)?(?<scheme>mailto)-->(?<identifier>.+)?\\s*$',SCHEMA_AFTER_HASH, " +
            "test@mailtest.eu, mailto,  ISE6IXAPSALNUFQ3XCFNHH3NC6EOK3RDK5OJ5OCCWNMD2E2OZJUQ.mailto",
            "'^(?i)mailto.*$','${scheme}-->${identifier}','${identifier}','^(?i)\\s*(-->)?(?<scheme>mailto)-->(?<identifier>.+)?\\s*$',SCHEMA_AFTER_HASH, " +
                    "mailto-->test@mailtest.eu,,ISE6IXAPSALNUFQ3XCFNHH3NC6EOK3RDK5OJ5OCCWNMD2E2OZJUQ.mailto",
            "'^(?i)mailto.*$','${scheme}-->${identifier}','${identifier}','^(?i)\\s*(-->)?(?<scheme>mailto)-->(?<identifier>.+)?\\s*$',ALL_IN_HASH, " +
                    "test@mailtest.eu, mailto,  LBOHF5726ZKZEPAMMJLKW52B3VYBP4XOLVXXAEID7IEUKFJWXFCQ",
            "'^(?i)mailto.*$','${scheme}:${identifier}','${identifier}','^(?i)\\s*:?(?<scheme>mailto):(?<identifier>.+)?\\s*$',SCHEMA_AFTER_HASH, " +
                    "test@mailtest.eu, mailto,  ISE6IXAPSALNUFQ3XCFNHH3NC6EOK3RDK5OJ5OCCWNMD2E2OZJUQ.mailto",
            "'^(?i)mailto.*$','${scheme}:${identifier}','${identifier}','^(?i)\\s*:?(?<scheme>mailto):(?<identifier>.+)?\\s*$',SCHEMA_AFTER_HASH, " +
                    "mailto:test@mailtest.eu, , ISE6IXAPSALNUFQ3XCFNHH3NC6EOK3RDK5OJ5OCCWNMD2E2OZJUQ.mailto"})
    void testTemplateFormatterType(Pattern matchSchema,
                                   String formatTemplate,
                                   String formatTemplateNullScheme,
                                   Pattern splitRegularExpression,
                                   DNSLookupFormatType dnsLookupFormatType,
                                   String identifier, String scheme, String expectedDomain
    ) throws Exception {

        FormatterType customFormatter = new TemplateFormatterType(matchSchema,
                formatTemplate, formatTemplateNullScheme,
                splitRegularExpression,
                dnsLookupFormatType);


        DefaultBDXRLocator testInstance = buildNaptrPublisherLocator(true);
        testInstance.getResourceIdentifierFormatter().addFormatterTypes(customFormatter);

        List<PublisherLookupResult> results = testInstance.lookup(identifier, scheme);
        //THEN
        assertEquals(1, results.size());
        verify(testInstance).naptrLookupForDomain(any(SMPParticipantIdentifier.class), stringArgumentCaptor.capture());
        assertEquals(expectedDomain + "." + TEST_TOP_DOMAIN_02, stringArgumentCaptor.getValue());
    }

    @ParameterizedTest
    @CsvSource({"'Test two services', 'Meta:SMP,oasis-bdxr-smp-2','Meta:SMP,oasis-bdxr-smp-2',2,'Meta:SMP,oasis-bdxr-smp-2'",
            "'Respect required order', 'Meta:SMP,oasis-bdxr-smp-2','oasis-bdxr-smp-2,Meta:SMP,',2,'oasis-bdxr-smp-2,Meta:SMP'",
            "'Intersect 01', 'oasis-bdxr-smp-2','oasis-bdxr-smp-2,Meta:SMP,',1,'oasis-bdxr-smp-2'",
            "'Intersect 02', 'Meta:SMP,oasis-bdxr-smp-2','oasis-bdxr-smp-2,',1,'oasis-bdxr-smp-2'"
    })
    void testDiscoverMultipleNaptrServices(String name, String naptrServices, String requiredNaptrServices, int expectedCount, String expected) throws Exception {
        List<String> existingNaptrServices = Arrays.asList(naptrServices.split(","));
        List<String> requiredServices = Arrays.asList(requiredNaptrServices.split(","));
        List<String> expectedResult = Arrays.asList(expected.split(","));

        //GIVEN
        DefaultBDXRLocator testInstance = buildPublisherLocator(null,
                Collections.singletonList(DNSLookupType.NAPTR),
                existingNaptrServices, TEST_PUBLISHER_URL, true);
        ((DefaultDNSLookup) testInstance.getDnsLookup()).setRequiredNaptrServices(requiredServices);

        //WHEN
        List<PublisherLookupResult> results = testInstance.lookup(TEST_IDENTIFIER_VALUE_PEPPOL, TEST_IDENTIFIER_SCHEME_PEPPOL);
        //THEN
        assertEquals(expectedCount, results.size());
        // all results should have the same URL
        results.forEach(publisherLookupResult -> {
            assertEquals(TEST_PUBLISHER_URL, publisherLookupResult.getUrl().toString());
            assertEquals(DNSLookupType.NAPTR, publisherLookupResult.getDnsLookupType());
        });
        // respect the order of the expected services
        IntStream.range(0, expectedCount)
                .forEachOrdered(
                        i -> assertEquals(expectedResult.get(i), results.get(i).getServiceType())
                );
    }

    @Test
    void testConfigurationMissingTopDomain() {
        DefaultBDXRLocator.Builder testInstance = new DefaultBDXRLocator.Builder();

        DDCRuntimeException result = assertThrows(DDCRuntimeException.class,
                testInstance::build);

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

    private DefaultBDXRLocator buildNaptrPublisherLocator(boolean addMockStub)
            throws Exception {
        return buildPublisherLocator(null, DNSLookupType.NAPTR, TEST_NAPTR_SERVICE_SMP1, TEST_PUBLISHER_URL, addMockStub);

    }


    private DefaultBDXRLocator buildPublisherLocator(String caseSensitiveScheme,
                                                     DNSLookupType type,
                                                     String service,
                                                     String publisherURL
    ) throws Exception {
        return buildPublisherLocator(caseSensitiveScheme, type, service, publisherURL, true);

    }

    /**
     * Build a publisher locator and mock the  idnsLookup for the lookup. The
     * locator iw wrapped in a spy object to allow the verification of the method calls.
     *
     * @param caseSensitiveScheme case sensitive scheme
     * @param type                DNSLookupType type
     * @param service
     * @param publisherURL
     * @param addMockitoStubbing  add mockito stubbing for DNS client
     * @return DefaultBDXRLocator
     * @throws Exception exception
     */
    private DefaultBDXRLocator buildPublisherLocator(String caseSensitiveScheme, DNSLookupType type, String service, String publisherURL,
                                                     boolean addMockitoStubbing) throws Exception {
        return buildPublisherLocator(caseSensitiveScheme, Collections.singletonList(type), Collections.singletonList(service), publisherURL, addMockitoStubbing);
    }

    /**
     * Build a publisher locator and mock the  idnsLookup for the lookup. The
     * locator iw wrapped in a spy object to allow the verification of the method calls.
     *
     * @param caseSensitiveScheme case sensitive scheme
     * @param types               DNSLookupType type
     * @param services
     * @param publisherURL
     * @param addMockitoStubbing  add mockito stubbing for DNS client
     * @return DefaultBDXRLocator
     * @throws Exception exception
     */
    private DefaultBDXRLocator buildPublisherLocator(String caseSensitiveScheme, List<DNSLookupType> types, List<String> services, String publisherURL,
                                                     boolean addMockitoStubbing) throws Exception {
        DefaultDNSLookup idnsLookup = spy(new DefaultDNSLookup.Builder().build());
        DefaultBDXRLocator testInstance = new DefaultBDXRLocator.Builder()
                .addTopDnsDomain(TEST_TOP_DOMAIN_02)
                .addDnsLookupTypes(types)
                .dnsLookup(idnsLookup)
                .addResourceCaseSensitiveSchema(caseSensitiveScheme)
                .build();
        testInstance = spy(testInstance);

        if (!addMockitoStubbing) {
            return testInstance;
        }
        // mock idnsLookup
        if (types.contains(DNSLookupType.NAPTR)) {

            Name domainName = Mockito.mock(Name.class);
            doReturn(true).when(domainName).isAbsolute();
            List<Record> records = new ArrayList<>();
            for (String naptrService : services) {
                String naptrValue = String.format(DNSUtils.NAPTR_FORMAT, publisherURL);
                Record record = new NAPTRRecord(domainName, 0, 60, 10, 100, "U", naptrService, naptrValue, domainName);
                records.add(record);
            }
            Mockito.doReturn(records).when(idnsLookup).getAllRecordsForType(any(SMPParticipantIdentifier.class), anyString(), any(DNSLookupType.class));
        }
        if (types.contains(DNSLookupType.CNAME)) {
            // return  domain exists
            Mockito.doReturn(true).when(idnsLookup).dnsRecordExists(
                    any(SMPParticipantIdentifier.class), anyString(), any(DNSLookupType.class));
        }
        return testInstance;
    }
}
