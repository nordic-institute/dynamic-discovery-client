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
package eu.europa.ec.dynamicdiscovery.model;

import eu.europa.ec.dynamicdiscovery.model.identifiers.ParticipantIdentifierFormatter;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Flávio W. R. Santos
 */

class ParticipantIdentifierTest {

    ParticipantIdentifierFormatter formatter = (ParticipantIdentifierFormatter) new ParticipantIdentifierFormatter()
            .caseSensitiveSchemas(Collections.singletonList("case-sensitive-qns"));

    private static Stream<Arguments> participantIdentifierTestArguments() {
        return Stream.of(
                Arguments.of("peppol: identifier", "urn:poland:ncpb", "ehealth-actorid-qns", "urn:poland:ncpb", "ehealth-actorid-qns", "ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb", Boolean.FALSE),
                Arguments.of("peppol: upper case identifier (set to lower case)", "URN:POLAND:NCPB", "ehealth-actorid-qns", "urn:poland:ncpb", "ehealth-actorid-qns", "ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb", Boolean.FALSE),
                Arguments.of("peppol: upper case identifier (Case sensitive Schema)", "URN:POLAND:NCPB", "case-sensitive-qns", "URN:POLAND:NCPB", "case-sensitive-qns", "case-sensitive-qns%3A%3AURN%3APOLAND%3ANCPB", Boolean.FALSE),
                Arguments.of("peppol: upper case Type (Case sensitive Schema)", "URN:POLAND:NCPB", "Case-Sensitive-Qns", "URN:POLAND:NCPB", "Case-Sensitive-Qns", "Case-Sensitive-Qns%3A%3AURN%3APOLAND%3ANCPB", Boolean.FALSE),
                Arguments.of("peppol: upper case type (set to lower case)", "urn:poland:ncpb", "EHEALTH-ACTORID-QNS", "urn:poland:ncpb", "ehealth-actorid-qns", "ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb", Boolean.FALSE),
                Arguments.of("peppol: null type", "urn:poland:ncpb", null, "urn:poland:ncpb", null, "%3A%3Aurn%3Apoland%3Ancpb", Boolean.FALSE),
                Arguments.of("oasis unregistered", "ec.europa.eu", "urn:oasis:names:tc:ebcore:partyid-type:unregistered:domain", "ec.europa.eu", "urn:oasis:names:tc:ebcore:partyid-type:unregistered:domain", "urn%3Aoasis%3Anames%3Atc%3Aebcore%3Apartyid-type%3Aunregistered%3Adomain%3Aec.europa.eu", Boolean.TRUE),
                Arguments.of("oasis iso6523", "123456789", "urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088", "123456789", "urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088", "urn%3Aoasis%3Anames%3Atc%3Aebcore%3Apartyid-type%3Aiso6523%3A0088%3A123456789", Boolean.TRUE),
                Arguments.of("oasis iso6523 null schema", "urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088:123456789", null, "123456789", "urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088", "urn%3Aoasis%3Anames%3Atc%3Aebcore%3Apartyid-type%3Aiso6523%3A0088%3A123456789", Boolean.TRUE),
                Arguments.of("oasis multipart", "urn:ehealth:pl:ncp-idp", "urn:oasis:names:tc:ebcore:partyid-type:unregistered:ehealth", "urn:ehealth:pl:ncp-idp", "urn:oasis:names:tc:ebcore:partyid-type:unregistered:ehealth", "urn%3Aoasis%3Anames%3Atc%3Aebcore%3Apartyid-type%3Aunregistered%3Aehealth%3Aurn%3Aehealth%3Apl%3Ancp-idp", Boolean.TRUE),
                Arguments.of("oasis multipart 2", "pl:ncp-idp", "urn:oasis:names:tc:ebcore:partyid-type:unregistered:ehealth", "pl:ncp-idp", "urn:oasis:names:tc:ebcore:partyid-type:unregistered:ehealth", "urn%3Aoasis%3Anames%3Atc%3Aebcore%3Apartyid-type%3Aunregistered%3Aehealth%3Apl%3Ancp-idp", Boolean.TRUE),
                Arguments.of("oasis without catalog", "blue-gw", "urn:oasis:names:tc:ebcore:partyid-type:unregistered", "blue-gw", "urn:oasis:names:tc:ebcore:partyid-type:unregistered", "urn%3Aoasis%3Anames%3Atc%3Aebcore%3Apartyid-type%3Aunregistered%3Ablue-gw", Boolean.TRUE),
                Arguments.of("oasis without catalog 1", "blue-gw", "urn:oasis:names:tc:ebcore:partyid-type:unregistered:", "blue-gw", "urn:oasis:names:tc:ebcore:partyid-type:unregistered", "urn%3Aoasis%3Anames%3Atc%3Aebcore%3Apartyid-type%3Aunregistered%3Ablue-gw", Boolean.TRUE),
                Arguments.of("oasis without catalog 2", "urn:oasis:names:tc:ebcore:partyid-type:unregistered:blue-gw", null, "blue-gw", "urn:oasis:names:tc:ebcore:partyid-type:unregistered", "urn%3Aoasis%3Anames%3Atc%3Aebcore%3Apartyid-type%3Aunregistered%3Ablue-gw", Boolean.TRUE),
                Arguments.of("oasis schema upper case", "ec.europa.eu", "URN:OASIS:NAMES:TC:EBCORE:PARTYID-TYPE:UNREGISTERED", "ec.europa.eu", "urn:oasis:names:tc:ebcore:partyid-type:unregistered", "urn%3Aoasis%3Anames%3Atc%3Aebcore%3Apartyid-type%3Aunregistered%3Aec.europa.eu", Boolean.TRUE),
                Arguments.of("oasis schema upper case", "URN:OASIS:NAMES:TC:EBCORE:PARTYID-TYPE:UNREGISTERED:ec.europa.eu", null, "ec.europa.eu", "urn:oasis:names:tc:ebcore:partyid-type:unregistered", "urn%3Aoasis%3Anames%3Atc%3Aebcore%3Apartyid-type%3Aunregistered%3Aec.europa.eu", Boolean.TRUE)
        );
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("participantIdentifierTestArguments")
    void testIdentifier(String testDescription, String identifier, String type, String normalizedIdentifier, String normalizedType, String urlEncoded, Boolean isOasisIdentifierType) {
        SMPParticipantIdentifier participantIdentifier = formatter.normalize(type, identifier);

        assertEquals(normalizedIdentifier, participantIdentifier.getIdentifier());
        assertEquals(normalizedType, participantIdentifier.getScheme());
        assertEquals(urlEncoded, formatter.urlEncodedFormat(participantIdentifier));
    }


}
