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
package eu.europa.ec.dynamicdiscovery.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Flávio W. R. Santos
 */

@RunWith(Parameterized.class)
public class ParticipantIdentifierTest {

    @Parameterized.Parameter(0)
    public String testDescription;

    @Parameterized.Parameter(1)
    public String identifier;

    @Parameterized.Parameter(2)
    public String type;

    @Parameterized.Parameter(3)
    public String normalizedIdentifier;

    @Parameterized.Parameter(4)
    public String normalizedType;

    @Parameterized.Parameter(5)
    public String urlEncoded;

    @Parameterized.Parameter(6)
    public Boolean isOasisIdentifierType;



    @Parameterized.Parameters(name = "{index}.{0}")
    public static Collection<Object[]> values() {
        return Arrays.asList(new Object[][]{
                {"peppol: identifier",   "urn:poland:ncpb","ehealth-actorid-qns","urn:poland:ncpb","ehealth-actorid-qns","ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb", Boolean.FALSE },
                {"peppol: upper case identifier (set to lower case)", "URN:POLAND:NCPB","ehealth-actorid-qns","urn:poland:ncpb","ehealth-actorid-qns","ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb",  Boolean.FALSE },
                {"peppol: upper case type (NOT set to lower case)", "urn:poland:ncpb","EHEALTH-ACTORID-QNS","urn:poland:ncpb","EHEALTH-ACTORID-QNS","EHEALTH-ACTORID-QNS%3A%3Aurn%3Apoland%3Ancpb",  Boolean.FALSE },
                {"peppol: null type", "urn:poland:ncpb",null,"urn:poland:ncpb",null,"urn%3Apoland%3Ancpb",  Boolean.FALSE },
                {"oasis unregistered", "ec.europa.eu", "urn:oasis:names:tc:ebcore:partyid-type:unregistered:domain", "urn:oasis:names:tc:ebcore:partyid-type:unregistered:domain:ec.europa.eu", null, "urn%3Aoasis%3Anames%3Atc%3Aebcore%3Apartyid-type%3Aunregistered%3Adomain%3Aec.europa.eu",  Boolean.TRUE },
                {"oasis iso6523", "123456789", "urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088", "urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088:123456789", null, "urn%3Aoasis%3Anames%3Atc%3Aebcore%3Apartyid-type%3Aiso6523%3A0088%3A123456789",  Boolean.TRUE},
                {"oasis iso6523 null schema", "urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088:123456789", null, "urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088:123456789", null, "urn%3Aoasis%3Anames%3Atc%3Aebcore%3Apartyid-type%3Aiso6523%3A0088%3A123456789",  Boolean.TRUE},
                {"oasis multipart", "urn:ehealth:pl:ncp-idp", "urn:oasis:names:tc:ebcore:partyid-type:unregistered:ehealth",  "urn:oasis:names:tc:ebcore:partyid-type:unregistered:ehealth:urn:ehealth:pl:ncp-idp", null, "urn%3Aoasis%3Anames%3Atc%3Aebcore%3Apartyid-type%3Aunregistered%3Aehealth%3Aurn%3Aehealth%3Apl%3Ancp-idp",  Boolean.TRUE},
                {"oasis multipart 2", "pl:ncp-idp", "urn:oasis:names:tc:ebcore:partyid-type:unregistered:ehealth", "urn:oasis:names:tc:ebcore:partyid-type:unregistered:ehealth:pl:ncp-idp", null, "urn%3Aoasis%3Anames%3Atc%3Aebcore%3Apartyid-type%3Aunregistered%3Aehealth%3Apl%3Ancp-idp",  Boolean.TRUE},
                {"oasis without catalog", "blue-gw", "urn:oasis:names:tc:ebcore:partyid-type:unregistered", "urn:oasis:names:tc:ebcore:partyid-type:unregistered:blue-gw", null , "urn%3Aoasis%3Anames%3Atc%3Aebcore%3Apartyid-type%3Aunregistered%3Ablue-gw",  Boolean.TRUE},
                {"oasis without catalog 1", "blue-gw", "urn:oasis:names:tc:ebcore:partyid-type:unregistered:", "urn:oasis:names:tc:ebcore:partyid-type:unregistered:blue-gw", null , "urn%3Aoasis%3Anames%3Atc%3Aebcore%3Apartyid-type%3Aunregistered%3Ablue-gw",  Boolean.TRUE},
                {"oasis without catalog 2", "urn:oasis:names:tc:ebcore:partyid-type:unregistered:blue-gw", null, "urn:oasis:names:tc:ebcore:partyid-type:unregistered:blue-gw", null , "urn%3Aoasis%3Anames%3Atc%3Aebcore%3Apartyid-type%3Aunregistered%3Ablue-gw",  Boolean.TRUE},
                {"oasis schema upper case", "ec.europa.eu", "URN:OASIS:NAMES:TC:EBCORE:PARTYID-TYPE:UNREGISTERED", "urn:oasis:names:tc:ebcore:partyid-type:unregistered:ec.europa.eu", null , "urn%3Aoasis%3Anames%3Atc%3Aebcore%3Apartyid-type%3Aunregistered%3Aec.europa.eu",  Boolean.TRUE},
                {"oasis schema upper case", "URN:OASIS:NAMES:TC:EBCORE:PARTYID-TYPE:UNREGISTERED:ec.europa.eu", null, "urn:oasis:names:tc:ebcore:partyid-type:unregistered:ec.europa.eu", null , "urn%3Aoasis%3Anames%3Atc%3Aebcore%3Apartyid-type%3Aunregistered%3Aec.europa.eu",  Boolean.TRUE},
        });
    }

    @Test
    public void testIdentifier() throws Exception {
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier(identifier, type);
        Assert.assertEquals(normalizedIdentifier, participantIdentifier.getIdentifier());
        Assert.assertEquals(normalizedType, participantIdentifier.getScheme());
        Assert.assertEquals(urlEncoded, participantIdentifier.urlencoded());
        Assert.assertEquals(isOasisIdentifierType, participantIdentifier.isOasisPartyIdentifierType());
    }


}