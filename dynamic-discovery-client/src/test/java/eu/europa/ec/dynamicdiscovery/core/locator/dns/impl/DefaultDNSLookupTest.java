/*
 * (C) Copyright 2016-2023 - European Commission | Dynamic Discovery Client
 *
 * https://ec.europa.eu/digital-building-blocks/code/projects/EDELIVERY/repos/dynamic-discovery-client/browse
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
package eu.europa.ec.dynamicdiscovery.core.locator.dns.impl;

import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.util.DNSUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.xbill.DNS.DClass;
import org.xbill.DNS.NAPTRRecord;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * @author Flávio W. R. Santos
 */
class DefaultDNSLookupTest {


    static final SMPParticipantIdentifier testParticipantIdentifier = new SMPParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");

    private static Stream<Arguments> naptrUrlValueLookupTestArguments() {
        return Stream.of(
                Arguments.of("Oasis SMP lookup",
                        testParticipantIdentifier,
                        Collections.singletonList("Meta:SMP"),
                        Arrays.asList("http:", "https:"),
                        Collections.singletonList("U"),
                        "Meta:SMP",
                        "http://test:8080/smp",
                        "http://test:8080/smp"),
                Arguments.of("Oasis SMP lookup multiple flags",
                        testParticipantIdentifier,
                        Collections.singletonList("Meta:SMP"),
                        Arrays.asList("http:", "https:"),
                        Arrays.asList("U","A","P"),
                        "Meta:SMP",
                        "http://test:8080/smp",
                        "http://test:8080/smp"),
                Arguments.of("Oasis SMP lookup caseInsensitive service",
                        testParticipantIdentifier,
                        Collections.singletonList("Meta:SMP"),
                        Arrays.asList("http:", "https:"),
                        Collections.singletonList("U"),
                        "MEtA:SmP",
                        "http://test:8080/smp",
                        "http://test:8080/smp"),
                Arguments.of("Oasis SMP lookup caseInsensitive schema",
                        testParticipantIdentifier,
                        Collections.singletonList("Meta:SMP"),
                        Arrays.asList("HTTP:", "HTTPS:"),
                        Collections.singletonList("U"),
                        "Meta:SMP",
                        "http://test:8080/smp",
                        "http://test:8080/smp"),
                Arguments.of("Oasis SMP lookup wrong service",
                        testParticipantIdentifier,
                        Collections.singletonList("Meta:SMP"),
                        Arrays.asList("http:", "https:"),
                        Collections.singletonList("U"),
                        "Wrong:SMP",
                        "http://test:8080/smp",
                        null),
                Arguments.of("Oasis SMP lookup wrong schema",
                        testParticipantIdentifier,
                        Collections.singletonList("Meta:SMP"),
                        Collections.singletonList("https:"),
                        Collections.singletonList("U"),
                        "Wrong:SMP",
                        "http://test:8080/smp",
                        null),
                Arguments.of("Oasis CPP lookup",
                        testParticipantIdentifier,
                        Collections.singletonList("meta:cppa3"),
                        Arrays.asList("http:", "https:"),
                        Collections.singletonList("U"),
                        "meta:cppa3",
                        "http://test:8080/cpp",
                        "http://test:8080/cpp"),
                Arguments.of("Oasis SMP lookup wrong flags",
                        testParticipantIdentifier,
                        Collections.singletonList("Meta:SMP"),
                        Arrays.asList("http:", "https:"),
                        Arrays.asList("S"),
                        "Meta:SMP",
                        "http://test:8080/smp",
                        "http://test:8080/smp")
        );
    }

    private static Stream<Arguments> urlFromNaptrRecordArguments() {
        return Stream.of(
                Arguments.of("The u-naptr",
                        "!.*!http://smp-mock-1.ehealth.eu!",
                        "test.ehealth.acc.edelivery.tech.ec.europa.eu",
                        "http://smp-mock-1.ehealth.eu"
                ),
                Arguments.of("The u-naptr - legacy ",
                        "!^.*$!http://smp-mock-1.ehealth.eu!",
                        "test.ehealth.acc.edelivery.tech.ec.europa.eu",
                        "http://smp-mock-1.ehealth.eu"
                ),
                Arguments.of("Not U-Naptr! Regular u-naptr with simple domain match",
                        "!^([a-zA-Z0-9]+(-[a-zA-Z0-9]+)*\\.)+[a-zA-Z]{2,}\\.?$!http://smp-test-regexp-1.eu!",
                        "test.ehealth.acc.edelivery.tech.ec.europa.eu",
                        "http://smp-test-regexp-1.eu"
                ),
                Arguments.of("Not U-Naptr!!, Test custom regular expression  change first part of naptr [A-Z0-9]+.ehealth-actorid-qns.ehealth.europa. with https://smp-mock-1.",
                        "![A-Z0-9]+.ehealth-actorid-qns.ehealth.europa.!https://smp-mock-1.example.!",
                        "ehealth.europa.eu",
                        "https://smp-mock-1.example.eu"
                )
        );
    }

    private static Stream<Arguments> naptrValueExamplesArguments() {
        return Stream.of(
                Arguments.of("The correct u-naptr",
                        "!.*!http://smp-mock-1.ehealth.eu!",
                        "L7KCFF3BPTJLMZWOPCTSIAG4CTMUFMH2EEELHVL5QQ52JYPALDTA.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu",
                        "http://smp-mock-1.ehealth.eu"
                ),
                Arguments.of("The legacy/invalid u-naptr ",
                        "!^.*$!http://smp-mock-1.ehealth.eu!",
                        "L7KCFF3BPTJLMZWOPCTSIAG4CTMUFMH2EEELHVL5QQ52JYPALDTA.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu",
                        "http://smp-mock-1.ehealth.eu"
                ),
                Arguments.of("Not U-Naptr! Regular u-naptr with simple domain match",
                        "!^([a-zA-Z0-9]+(-[a-zA-Z0-9]+)*\\.)+[a-zA-Z]{2,}\\.?$!http://smp-test-regexp-1.eu!",
                        "L7KCFF3BPTJLMZWOPCTSIAG4CTMUFMH2EEELHVL5QQ52JYPALDTA.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu",
                        "http://smp-test-regexp-1.eu"
                ),
                Arguments.of("Not U-Naptr!!, Test custom regular expression  change first part of naptr [A-Z0-9]+.ehealth-actorid-qns.ehealth.europa. with https://smp-mock-1.",
                        "![A-Z0-9]+.ehealth-actorid-qns.ehealth.europa.!https://smp-mock-1.example.!",
                        "L7KCFF3BPTJLMZWOPCTSIAG4CTMUFMH2EEELHVL5QQ52JYPALDTA.ehealth-actorid-qns.ehealth.europa.eu",
                        "https://smp-mock-1.example.eu"
                )
        );
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("naptrValueExamplesArguments")
    void testResolveNaptrValue(String name, String regularExpression, String hostname, String expectedResult) throws Exception {
        DefaultDNSLookup testInstance = Mockito.spy(new DefaultDNSLookup.
                Builder().build());

        String result = testInstance.resolveNaptrValue( regularExpression, hostname);
        assertEquals(expectedResult, result);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("naptrUrlValueLookupTestArguments")
    void testNaptrUrlValueLookup(String name, SMPParticipantIdentifier identifier, List<String> services, List<String> schemas, List<String> flagsList, String recordService, String naptrValue, String expectedResult) throws Exception {
        String testUri = "localhost";
        DefaultDNSLookup testInstance = Mockito.spy(new DefaultDNSLookup.
                Builder()
                .addRequiredNaptrURLSchemas(schemas)
                .addRequiredNaptrServices(services)
                .addRequiredNaptrServices(flagsList)
                .build());

        Mockito.doReturn(DNSUtils.createSmpDnsNaptrResponse(identifier, recordService, naptrValue, recordService))
                .when(testInstance)
                .getAllNaptrRecords(any(SMPParticipantIdentifier.class), anyString());

        String result = testInstance.naptrUrlValueLookup(identifier, testUri);

        assertEquals(expectedResult, result);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("urlFromNaptrRecordArguments")
    void testGetURLFromNaptrRecord(String name, String value, String hostname, String result) throws Exception {
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");
        DefaultDNSLookup defaultDNSLookup = new DefaultDNSLookup.Builder().build();
        List<Record> records = new ArrayList<>();
        records.add(new NAPTRRecord(DNSUtils.createSmpDnsNaptrDomainName(participantIdentifier.getIdentifier(), participantIdentifier.getScheme(),
                hostname), DClass.IN, 60, 100, 10, "U",
                "Meta:SMP",
                value, Name.fromString(".")));

        assertEquals(result, defaultDNSLookup.getURLFromNaptrRecord(records,
                Collections.singletonList("Meta:SMP"), Arrays.asList("http:", "https:"), Collections.singletonList("U")));
    }

    @Test
    void lookupFetcherTest2() throws Exception {
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");
        DefaultDNSLookup defaultDNSLookup = new DefaultDNSLookup.Builder().build();
        List<Record> records = new ArrayList<>();
        records.add(new NAPTRRecord(DNSUtils.createSmpDnsNaptrDomainName(participantIdentifier.getIdentifier(), participantIdentifier.getScheme(),
                "ehealth.acc.edelivery.tech.ec.europa.eu"), DClass.IN, 60, 100, 10, "U", "Meta:SMP",
                "!^.*$!http://smp-mock-1.ehealth.eu:8888!", Name.fromString(".")));
        assertEquals("http://smp-mock-1.ehealth.eu:8888",
                defaultDNSLookup.getURLFromNaptrRecord(records, Collections.singletonList("Meta:SMP"), Arrays.asList("http:", "https:"), Collections.singletonList("U")));
    }

    @Test
    void testDefaultConfiguration() {
        DefaultDNSLookup defaultDNSLookup = new DefaultDNSLookup.Builder().build();
        assertEquals(1, defaultDNSLookup.getRequiredNaptrServices().size());
        assertEquals(2, defaultDNSLookup.getRequiredURLSchemas().size());
        assertTrue(defaultDNSLookup.getRequiredNaptrServices().contains("Meta:SMP"));
        assertTrue(defaultDNSLookup.getRequiredURLSchemas().containsAll(Arrays.asList("http:", "https:")));
    }

    @Test
    void testConfiguration() {
        DefaultDNSLookup defaultDNSLookup = new DefaultDNSLookup.Builder()
                .addRequiredNaptrService("meta:cppa3")
                .addRequiredNaptrURLSchema("http:").build();
        assertEquals(1, defaultDNSLookup.getRequiredNaptrServices().size());
        assertEquals(1, defaultDNSLookup.getRequiredURLSchemas().size());
        assertTrue(defaultDNSLookup.getRequiredNaptrServices().contains("meta:cppa3"));
        assertTrue(defaultDNSLookup.getRequiredURLSchemas().contains("http:"));
    }

    @Test
    void testConfigurationAddList() {
        DefaultDNSLookup defaultDNSLookup = new DefaultDNSLookup.Builder()
                .addRequiredNaptrServices(Collections.singletonList("meta:cppa3"))
                .addRequiredNaptrURLSchemas(Collections.singletonList("http:")).build();
        assertEquals(1, defaultDNSLookup.getRequiredNaptrServices().size());
        assertEquals(1, defaultDNSLookup.getRequiredURLSchemas().size());
        assertTrue(defaultDNSLookup.getRequiredNaptrServices().contains("meta:cppa3"));
        assertTrue(defaultDNSLookup.getRequiredURLSchemas().contains("http:"));
    }

}
