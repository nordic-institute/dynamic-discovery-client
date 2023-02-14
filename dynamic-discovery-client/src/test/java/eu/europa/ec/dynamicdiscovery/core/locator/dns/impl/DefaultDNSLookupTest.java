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
                        "Meta:SMP",
                        "http://test:8080/smp",
                        "http://test:8080/smp"),
                Arguments.of("Oasis SMP lookup caseInsensitive service",
                        testParticipantIdentifier,
                        Collections.singletonList("Meta:SMP"),
                        Arrays.asList("http:", "https:"),
                        "MEtA:SmP",
                        "http://test:8080/smp",
                        "http://test:8080/smp"),
                Arguments.of("Oasis SMP lookup caseInsensitive schema",
                        testParticipantIdentifier,
                        Collections.singletonList("Meta:SMP"),
                        Arrays.asList("HTTP:", "HTTPS:"),
                        "Meta:SMP",
                        "http://test:8080/smp",
                        "http://test:8080/smp"),
                Arguments.of("Oasis SMP lookup wrong service",
                        testParticipantIdentifier,
                        Collections.singletonList("Meta:SMP"),
                        Arrays.asList("http:", "https:"),
                        "Wrong:SMP",
                        "http://test:8080/smp",
                        null),
                Arguments.of("Oasis SMP lookup wrong schema",
                        testParticipantIdentifier,
                        Collections.singletonList("Meta:SMP"),
                        Collections.singletonList("https:"),
                        "Wrong:SMP",
                        "http://test:8080/smp",
                        null),
                Arguments.of("Oasis CPP lookup",
                        testParticipantIdentifier,
                        Collections.singletonList("meta:cppa3"),
                        Arrays.asList("http:", "https:"),
                        "meta:cppa3",
                        "http://test:8080/cpp",
                        "http://test:8080/cpp")
        );
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("naptrUrlValueLookupTestArguments")
    void testNaptrUrlValueLookup(String name, SMPParticipantIdentifier identifier, List<String> services, List<String> schemas, String recordService, String naptrValue, String expectedResult) throws Exception {
        String testUri = "localhost";
        DefaultDNSLookup testInstance = Mockito.spy(new DefaultDNSLookup.
                Builder()
                .addRequiredNaptrURLSchemas(schemas)
                .addRequiredNaptrServices(services)
                .build());

        Mockito.doReturn(DNSUtils.createSmpDnsNaptrResponse(identifier, recordService, naptrValue, recordService))
                .when(testInstance)
                .getAllNaptrRecords(any(SMPParticipantIdentifier.class), anyString());

        String result = testInstance.naptrUrlValueLookup(identifier, testUri);

        assertEquals(expectedResult, result);
    }

    @Test
    void lookupFetcherTest1() throws Exception {
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");
        DefaultDNSLookup defaultDNSLookup = new DefaultDNSLookup.Builder().build();
        List<Record> records = new ArrayList<>();
        records.add(new NAPTRRecord(DNSUtils.createSmpDnsNaptrDomainName(participantIdentifier.getIdentifier(), participantIdentifier.getScheme(),
                "ehealth.acc.edelivery.tech.ec.europa.eu"), DClass.IN, 60, 100, 10, "U",
                "Meta:SMP",
                "!^.*$!http://smp-mock-1.ehealth.eu!", Name.fromString(".")));
        assertEquals("http://smp-mock-1.ehealth.eu", defaultDNSLookup.getURLFromNaptrRecord(records,
                Collections.singletonList("Meta:SMP"), Arrays.asList("http:", "https:")));
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
                defaultDNSLookup.getURLFromNaptrRecord(records, Collections.singletonList("Meta:SMP"), Arrays.asList("http:", "https:")));
    }
}
