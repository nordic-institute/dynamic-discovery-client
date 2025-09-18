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
package eu.europa.ec.dynamicdiscovery.core.locator.dns.impl;

import eu.europa.ec.dynamicdiscovery.core.locator.PublisherLookupResult;
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

import static eu.europa.ec.dynamicdiscovery.util.DNSUtils.*;
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
                        Collections.singletonList(TEST_NAPTR_SERVICE_SMP1),
                        Arrays.asList("http", "https"),
                        Collections.singletonList("U"),
                        TEST_NAPTR_SERVICE_SMP1,
                        "http://test:8080/smp",
                        1,
                        "http://test:8080/smp"),
                Arguments.of("Oasis SMP lookup multiple flags",
                        testParticipantIdentifier,
                        Collections.singletonList(TEST_NAPTR_SERVICE_SMP1),
                        Arrays.asList("http", "https"),
                        Arrays.asList("U", "A", "P"),
                        TEST_NAPTR_SERVICE_SMP1,
                        "http://test:8080/smp",
                        1,
                        "http://test:8080/smp"),
                Arguments.of("Oasis SMP lookup caseInsensitive service",
                        testParticipantIdentifier,
                        Collections.singletonList(TEST_NAPTR_SERVICE_SMP1),
                        Arrays.asList("http", "https"),
                        Collections.singletonList("U"),
                        "MEtA:SmP",
                        "http://test:8080/smp",
                        1,
                        "http://test:8080/smp"),
                Arguments.of("Oasis SMP lookup caseInsensitive schema",
                        testParticipantIdentifier,
                        Collections.singletonList(TEST_NAPTR_SERVICE_SMP1),
                        Arrays.asList("HTTP:", "HTTPS:"),
                        Collections.singletonList("U"),
                        TEST_NAPTR_SERVICE_SMP1,
                        "http://test:8080/smp",
                        1,
                        "http://test:8080/smp"),
                Arguments.of("Oasis SMP lookup wrong service",
                        testParticipantIdentifier,
                        Collections.singletonList(TEST_NAPTR_SERVICE_SMP1),
                        Arrays.asList("http", "https"),
                        Collections.singletonList("U"),
                        "Wrong:SMP",
                        "http://test:8080/smp",
                        0,
                        null),
                Arguments.of("Oasis SMP lookup wrong schema",
                        testParticipantIdentifier,
                        Collections.singletonList(TEST_NAPTR_SERVICE_SMP1),
                        Collections.singletonList("https:"),
                        Collections.singletonList("U"),
                        "Wrong:SMP",
                        "http://test:8080/smp",
                        0,
                        null),
                Arguments.of("Oasis CPP lookup",
                        testParticipantIdentifier,
                        Collections.singletonList("meta:cppa3"),
                        Arrays.asList("http", "https"),
                        Collections.singletonList("U"),
                        "meta:cppa3",
                        "http://test:8080/cpp",
                        1,
                        "http://test:8080/cpp"),
                Arguments.of("Oasis SMP lookup wrong flags",
                        testParticipantIdentifier,
                        Collections.singletonList(TEST_NAPTR_SERVICE_SMP1),
                        Arrays.asList("http", "https"),
                        List.of("S"),
                        TEST_NAPTR_SERVICE_SMP1,
                        "http://test:8080/smp",
                        1,
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
    void testResolveNaptrValue(String name, String regularExpression, String hostname, String expectedResult){
        DefaultDNSLookup testInstance = Mockito.spy(new DefaultDNSLookup.
                Builder().build());

        String result = testInstance.resolveNaptrValue(regularExpression, hostname);
        assertEquals(expectedResult, result);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("naptrUrlValueLookupTestArguments")
    void testNaptrUrlValueLookup(String name, SMPParticipantIdentifier identifier,
                                 List<String> services, List<String> schemas,
                                 List<String> flagsList, String recordService, String naptrValue,
                                 int expectedResultCount, String expectedResult) throws Exception {
        String testUri = "localhost";
        DefaultDNSLookup testInstance = Mockito.spy(new DefaultDNSLookup.
                Builder()
                .addRequiredNaptrURLSchemas(schemas)
                .addRequiredNaptrServices(services)
                .addRequiredNaptrServices(flagsList)
                .build());

        List<Record> mockRecords = DNSUtils.createSmpDnsNaptrResponse(identifier, recordService, naptrValue, recordService);
        Mockito.doReturn(mockRecords)
                .when(testInstance)
                .getAllNaptrRecords(any(SMPParticipantIdentifier.class), anyString());

        List<PublisherLookupResult> result =  testInstance.naptrUrlValueLookup(identifier, testUri);

        assertEquals(expectedResultCount, result.size());
        if (expectedResultCount > 0){
            assertEquals(expectedResult, result.get(0).getUrl().toString());
        }
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("urlFromNaptrRecordArguments")
    void testGetURLFromNaptrRecord(String name, String value, String hostname, String expectedResult) throws Exception {
        //given
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");
        DefaultDNSLookup defaultDNSLookup = new DefaultDNSLookup.Builder().build();
        List<Record> records = new ArrayList<>();
        records.add(new NAPTRRecord(DNSUtils.createSmpDnsNaptrDomainName(participantIdentifier.getIdentifier(), participantIdentifier.getScheme(),
                hostname), DClass.IN, 60, 100, 10, "U",
                TEST_NAPTR_SERVICE_SMP1,
                value, Name.fromString(".")));
        // when
        List<PublisherLookupResult> result = defaultDNSLookup.getURLFromNaptrRecord(participantIdentifier,
                records,
                Collections.singletonList(TEST_NAPTR_SERVICE_SMP1),
                Arrays.asList("http", "https"),
                Collections.singletonList("U"));
        // then
        assertEquals(1, result.size());
        assertEquals(expectedResult, result.get(0).getUrl().toString());
    }



    @Test
    void testDefaultConfiguration() {
        DefaultDNSLookup defaultDNSLookup = new DefaultDNSLookup.Builder().build();
        assertEquals(0, defaultDNSLookup.getRequiredNaptrServices().size());
        assertEquals(2, defaultDNSLookup.getRequiredURLSchemas().size());
        assertTrue(defaultDNSLookup.getRequiredURLSchemas().containsAll(Arrays.asList("http", "https")));
    }

    @Test
    void testConfiguration() {
        DefaultDNSLookup defaultDNSLookup = new DefaultDNSLookup.Builder()
                .addRequiredNaptrService("meta:cppa3")
                .addRequiredNaptrFlag("A")
                .addRequiredNaptrURLSchema("http").build();
        assertEquals(1, defaultDNSLookup.getRequiredNaptrServices().size());
        assertEquals(1, defaultDNSLookup.getRequiredURLSchemas().size());
        assertEquals(1, defaultDNSLookup.getRequiredNaptrFlags().size());
        assertTrue(defaultDNSLookup.getRequiredNaptrServices().contains("meta:cppa3"));
        assertTrue(defaultDNSLookup.getRequiredURLSchemas().contains("http"));
        assertTrue(defaultDNSLookup.getRequiredNaptrFlags().contains("A"));
    }

    @Test
    void testConfigurationAddList() {
        DefaultDNSLookup defaultDNSLookup = new DefaultDNSLookup.Builder()
                .addRequiredNaptrServices(Collections.singletonList("meta:cppa3"))
                .addRequiredNaptrFlags(Collections.singletonList("A"))
                .addRequiredNaptrURLSchemas(Collections.singletonList("http")).build();

        assertEquals(1, defaultDNSLookup.getRequiredNaptrServices().size());
        assertEquals(1, defaultDNSLookup.getRequiredURLSchemas().size());
        assertEquals(1, defaultDNSLookup.getRequiredNaptrFlags().size());
        assertTrue(defaultDNSLookup.getRequiredNaptrServices().contains("meta:cppa3"));
        assertTrue(defaultDNSLookup.getRequiredURLSchemas().contains("http"));
        assertTrue(defaultDNSLookup.getRequiredNaptrFlags().contains("A"));
    }
}
