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
package eu.europa.ec.dynamicdiscovery.core.provider.impl;

import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Flávio W. R. Santos
 * @author Erlend Klakegg Bergheim
 */
class DefaultProviderTest {
    private static final String TEST_URI = "http://b-adb4c6d3821d142c684b13ed269fad65.ehealth-actorid-qns.ehealth.acc.edelivery.tech.ec.europa.eu";
    DefaultProvider testInstance = new DefaultProvider.Builder().build();


    @Test
    void resolveParticipantIdentifierTest() throws Exception {
        // given
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");
        URI uri = new URI(TEST_URI);
        //when
        String resolvedURI = testInstance.resolveForParticipantIdentifier(uri, participantIdentifier).toString();
        // then
        assertEquals(TEST_URI + "/ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb", resolvedURI);
    }

    @Test
    void resolveServiceMetadataTest() throws Exception {
        // given
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");
        SMPDocumentIdentifier documentIdentifier = new SMPDocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns");
        URI uri = new URI(TEST_URI);
        // when
        String resolvedURI = testInstance.resolveServiceMetadata(uri, participantIdentifier, documentIdentifier).toString();

        assertEquals(TEST_URI + "/ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A107", resolvedURI);
    }

    @Test
    void resolveDocumentIdentifiersForSmpUnderNonRootContextTest() throws Exception {
        //given
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");


        //when
        String url = testInstance.resolveForParticipantIdentifier(new URI("http://example.local:1234/smp_context"), participantIdentifier).toString();

        //then
        assertEquals("http://example.local:1234/smp_context/ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb", url);
    }

    @Test
    void resolveDocumentIdentifiersForSmpUnderRootContextWithExtraSlashTest() throws Exception {
        //given
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");

        //when
        String url = testInstance.resolveForParticipantIdentifier(new URI("http://example.local:1234/"), participantIdentifier).toString();

        //then
        assertEquals("http://example.local:1234/ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb", url);
    }

    @Test
    void resolveServiceMetadataForSmpUnderRootContextWithoutLastSlashTest() throws Exception {
        //given
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");
        SMPDocumentIdentifier documentIdentifier = new SMPDocumentIdentifier("doc_id", "ehealth-resid-qns");


        //when
        String url = testInstance.resolveServiceMetadata(new URI("http://example.local:1234"), participantIdentifier, documentIdentifier).toString();

        //then
        assertEquals("http://example.local:1234/ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb/services/ehealth-resid-qns%3A%3Adoc_id", url);
    }

    @Test
    void resolveServiceMetadataForSmpUnderNonRootContextTest() throws Exception {
        //given
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");
        SMPDocumentIdentifier documentIdentifier = new SMPDocumentIdentifier("doc_id", "ehealth-resid-qns");

        //when
        String url = testInstance.resolveServiceMetadata(new URI("http://example.local:1234/smp_context"), participantIdentifier, documentIdentifier).toString();

        //then
        assertEquals("http://example.local:1234/smp_context/ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb/services/ehealth-resid-qns%3A%3Adoc_id", url);
    }

}
