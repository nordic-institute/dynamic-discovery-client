/*-
 * #%L
 * dynamic-discovery-client
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
package eu.europa.ec.dynamicdiscovery.model.identifiers;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Joze Rihtarsic
 * @since 2.0
 */
class ProcessIdentifierFormatterTest {


    private static Stream<Arguments> documentIdentifierCases() {
        return Stream.of(
                Arguments.of("Basic example",
                        new SMPProcessIdentifier("b", "a"),
                        "a::b",
                        "a%3A%3Ab",
                        "a::b",
                        false),
                Arguments.of("Double separator example",
                        new SMPProcessIdentifier("b::c", "a"),
                        "a::b::c",
                        "a%3A%3Ab%3A%3Ac",
                        "a::b::c",
                        false),
                Arguments.of("Char : in scheme colon basic",
                        new SMPProcessIdentifier("c", "a:b"),
                        "a:b::c",
                        "a%3Ab%3A%3Ac",
                        "a:b::c",
                        false),
                Arguments.of("No Scheme Identifier ",
                        new SMPProcessIdentifier("NoSchemeIdentifier", null),
                        "::NoSchemeIdentifier",
                        "%3A%3ANoSchemeIdentifier",
                        "NoSchemeIdentifier",
                        false),
                Arguments.of("No Scheme Identifier with ::",
                        new SMPProcessIdentifier("NoSchemeIdentifier01", null),
                        "::NoSchemeIdentifier01",
                        "%3A%3ANoSchemeIdentifier01",
                        "::NoSchemeIdentifier01",
                        false),
                Arguments.of("No Scheme Identifier with no scheme and ::",
                        new SMPProcessIdentifier("NoSchemeIdentifier01::test", null),
                        "::NoSchemeIdentifier01::test",
                        "%3A%3ANoSchemeIdentifier01%3A%3Atest",
                        "::NoSchemeIdentifier01::test",
                        false),
                Arguments.of("Example with ## and double colon",
                        new SMPProcessIdentifier("urn::epsos##services:extended:epsos::51", "ehealth-resid-qns"),
                        "ehealth-resid-qns::urn::epsos##services:extended:epsos::51",
                        "ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A51",
                        "ehealth-resid-qns::urn::epsos##services:extended:epsos::51",
                        false),
                Arguments.of("Identifier with spaces -  formatted to uri with '%20",
                        new SMPProcessIdentifier("urn ncpb test", "ehealth-actorid-qns"),
                        "ehealth-actorid-qns::urn ncpb test",
                        "ehealth-actorid-qns%3A%3Aurn%20ncpb%20test",
                        "ehealth-actorid-qns::urn ncpb test",
                        false),
                Arguments.of("Example 01 (parse spaces)", new SMPProcessIdentifier("urn:ehealth:pt:ncpb-idp", "scheme"),
                        "scheme::urn:ehealth:pt:ncpb-idp",
                        "scheme%3A%3Aurn%3Aehealth%3Apt%3Ancpb-idp",
                        " scheme::urn:ehealth:pt:ncpb-idp",
                        false),
                Arguments.of("Example 02 (parse spaces)", new SMPProcessIdentifier("urn:ehealth:be:ncpb-idp", "otherscheme"),
                        "otherscheme::urn:ehealth:be:ncpb-idp",
                        "otherscheme%3A%3Aurn%3Aehealth%3Abe%3Ancpb-idp",
                        "otherscheme::urn:ehealth:be:ncpb-idp ",
                        false),
                Arguments.of("Example 03 (parse spaces in argument)", new SMPProcessIdentifier("urn:ehealth:IdentityService::XCPD::CrossGatewayPatientDiscovery##ITI-55", "ehealth-resid-qns"),
                        "ehealth-resid-qns::urn:ehealth:IdentityService::XCPD::CrossGatewayPatientDiscovery##ITI-55",
                        "ehealth-resid-qns%3A%3Aurn%3Aehealth%3AIdentityService%3A%3AXCPD%3A%3ACrossGatewayPatientDiscovery%23%23ITI-55",
                        "ehealth-resid-qns:: urn:ehealth:IdentityService::XCPD::CrossGatewayPatientDiscovery##ITI-55 ",
                        false),
                Arguments.of("Example 04", new SMPProcessIdentifier("urn:XCPD::CrossGatewayPatientDiscovery", "ehealth-resid-qns"),
                        "ehealth-resid-qns::urn:XCPD::CrossGatewayPatientDiscovery",
                        "ehealth-resid-qns%3A%3Aurn%3AXCPD%3A%3ACrossGatewayPatientDiscovery",
                        "ehealth-resid-qns::urn:XCPD::CrossGatewayPatientDiscovery",
                        false),
                Arguments.of("Example 05", new SMPProcessIdentifier("urn:ehealth:PatientService::XCA::CrossGatewayQuery##ITI-38", "ehealth-resid-qns"),
                        "ehealth-resid-qns::urn:ehealth:PatientService::XCA::CrossGatewayQuery##ITI-38",
                        "ehealth-resid-qns%3A%3Aurn%3Aehealth%3APatientService%3A%3AXCA%3A%3ACrossGatewayQuery%23%23ITI-38",
                        "ehealth-resid-qns::urn:ehealth:PatientService::XCA::CrossGatewayQuery##ITI-38",
                        false),
                Arguments.of("Example 06", new SMPProcessIdentifier("urn:XCA::CrossGatewayQuery", "ehealth-resid-qns"),
                        "ehealth-resid-qns::urn:XCA::CrossGatewayQuery",
                        "ehealth-resid-qns%3A%3Aurn%3AXCA%3A%3ACrossGatewayQuery",
                        "ehealth-resid-qns::urn:XCA::CrossGatewayQuery",
                        false),
                Arguments.of("Example 07", new SMPProcessIdentifier("urn:ehealth:OrderService::XCA::CrossGatewayQuery##ITI-38", "ehealth-resid-qns"),
                        "ehealth-resid-qns::urn:ehealth:OrderService::XCA::CrossGatewayQuery##ITI-38",
                        "ehealth-resid-qns%3A%3Aurn%3Aehealth%3AOrderService%3A%3AXCA%3A%3ACrossGatewayQuery%23%23ITI-38",
                        "ehealth-resid-qns::urn:ehealth:OrderService::XCA::CrossGatewayQuery##ITI-38",
                        false),
                Arguments.of("Example 08", new SMPProcessIdentifier("urn:ehealth:DispensationService:Initialize::XDR::ProvideandRegisterDocumentSet-b##ITI-41", "ehealth-resid-qns"),
                        "ehealth-resid-qns::urn:ehealth:DispensationService:Initialize::XDR::ProvideandRegisterDocumentSet-b##ITI-41",
                        "ehealth-resid-qns%3A%3Aurn%3Aehealth%3ADispensationService%3AInitialize%3A%3AXDR%3A%3AProvideandRegisterDocumentSet-b%23%23ITI-41",
                        "ehealth-resid-qns::urn:ehealth:DispensationService:Initialize::XDR::ProvideandRegisterDocumentSet-b##ITI-41",
                        false),
                Arguments.of("Example 09", new SMPProcessIdentifier("urn:XDR::ProvideandRegisterDocumentSet-b", "ehealth-resid-qns"),
                        "ehealth-resid-qns::urn:XDR::ProvideandRegisterDocumentSet-b",
                        "ehealth-resid-qns%3A%3Aurn%3AXDR%3A%3AProvideandRegisterDocumentSet-b",
                        "ehealth-resid-qns::urn:XDR::ProvideandRegisterDocumentSet-b",
                        false),
                Arguments.of("Example 10", new SMPProcessIdentifier("urn:ehealth:DispensationService:Discard::XDR::ProvideandRegisterDocumentSet-b##ITI-41", "ehealth-resid-qns"),
                        "ehealth-resid-qns::urn:ehealth:DispensationService:Discard::XDR::ProvideandRegisterDocumentSet-b##ITI-41",
                        "ehealth-resid-qns%3A%3Aurn%3Aehealth%3ADispensationService%3ADiscard%3A%3AXDR%3A%3AProvideandRegisterDocumentSet-b%23%23ITI-41",
                        "ehealth-resid-qns::urn:ehealth:DispensationService:Discard::XDR::ProvideandRegisterDocumentSet-b##ITI-41",
                        false)
        );
    }


    // input parameters

    ProcessIdentifierFormatter testInstance = new ProcessIdentifierFormatter.Builder().build();

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("documentIdentifierCases")
    void testFormat(String name, SMPProcessIdentifier identifierType, String formattedIdentifier, String uriFormattedIdentifier, String identifierToParse, boolean throwParseError) {

        String result = testInstance.format(identifierType);
        String uriResult = testInstance.urlEncodedFormat(identifierType);

        assertEquals(formattedIdentifier, result);
        assertEquals(uriFormattedIdentifier, uriResult);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("documentIdentifierCases")
    void testParse(String name, SMPProcessIdentifier identifierType, String formattedIdentifier, String uriFormattedIdentifier, String identifierToParse, boolean throwParseError) {
        IllegalArgumentException exception = null;
        SMPProcessIdentifier result = null;
        if (throwParseError) {
            exception = assertThrows(IllegalArgumentException.class, () -> testInstance.parse(identifierToParse));
        } else {
            result = testInstance.parse(identifierToParse);
        }

        assertNotNull(throwParseError ? exception : result);
        if (!throwParseError) {
            assertEquals(identifierType.getScheme(), result.getScheme());
            assertEquals(identifierType.getIdentifier(), result.getIdentifier());
        }
    }
}
