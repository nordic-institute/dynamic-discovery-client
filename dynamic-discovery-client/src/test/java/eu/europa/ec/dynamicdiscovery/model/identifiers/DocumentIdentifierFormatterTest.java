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
class DocumentIdentifierFormatterTest {


    private static Stream<Arguments> documentIdentifierCases() {
        return Stream.of(
                Arguments.of("Basic example",
                        new SMPDocumentIdentifier("b", "a"),
                        "a::b",
                        "a%3A%3Ab",
                        "a::b",
                        false),
                Arguments.of("Double separator example",
                        new SMPDocumentIdentifier("b::c", "a"),
                        "a::b::c",
                        "a%3A%3Ab%3A%3Ac",
                        "a::b::c",
                        false),
                Arguments.of("Char : in scheme colon basic",
                        new SMPDocumentIdentifier("c", "a:b"),
                        "a:b::c",
                        "a%3Ab%3A%3Ac",
                        "a:b::c",
                        false),
                Arguments.of("No Scheme Identifier ",
                        new SMPDocumentIdentifier("NoSchemeIdentifier", null),
                        "::NoSchemeIdentifier",
                        "%3A%3ANoSchemeIdentifier",
                        "NoSchemeIdentifier",
                        false),
                Arguments.of("No Scheme Identifier with ::",
                        new SMPDocumentIdentifier("NoSchemeIdentifier01", null),
                        "::NoSchemeIdentifier01",
                        "%3A%3ANoSchemeIdentifier01",
                        "::NoSchemeIdentifier01",
                        false),
                Arguments.of("No Scheme Identifier with no scheme and ::",
                        new SMPDocumentIdentifier("NoSchemeIdentifier01::test", null),
                        "::NoSchemeIdentifier01::test",
                        "%3A%3ANoSchemeIdentifier01%3A%3Atest",
                        "::NoSchemeIdentifier01::test",
                        false),
                Arguments.of("Example with ## and double colon",
                        new SMPDocumentIdentifier("urn::epsos##services:extended:epsos::51", "ehealth-resid-qns"),
                        "ehealth-resid-qns::urn::epsos##services:extended:epsos::51",
                        "ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A51",
                        "ehealth-resid-qns::urn::epsos##services:extended:epsos::51",
                        false),
                Arguments.of("Identifier with spaces -  formatted to uri with '%20",
                        new SMPDocumentIdentifier("urn ncpb test", "ehealth-actorid-qns"),
                        "ehealth-actorid-qns::urn ncpb test",
                        "ehealth-actorid-qns%3A%3Aurn%20ncpb%20test",
                        "ehealth-actorid-qns::urn ncpb test",
                        false),
                Arguments.of("Example 01 (parse spaces)", new SMPDocumentIdentifier("urn:ehealth:pt:ncpb-idp", "scheme"),
                        "scheme::urn:ehealth:pt:ncpb-idp",
                        "scheme%3A%3Aurn%3Aehealth%3Apt%3Ancpb-idp",
                        " scheme::urn:ehealth:pt:ncpb-idp",
                        false),
                Arguments.of("Example 02 (parse spaces)", new SMPDocumentIdentifier("urn:ehealth:be:ncpb-idp", "otherscheme"),
                        "otherscheme::urn:ehealth:be:ncpb-idp",
                        "otherscheme%3A%3Aurn%3Aehealth%3Abe%3Ancpb-idp",
                        "otherscheme::urn:ehealth:be:ncpb-idp ",
                        false),
                Arguments.of("Example 03 (parse spaces in argument)", new SMPDocumentIdentifier("urn:ehealth:IdentityService::XCPD::CrossGatewayPatientDiscovery##ITI-55", "ehealth-resid-qns"),
                        "ehealth-resid-qns::urn:ehealth:IdentityService::XCPD::CrossGatewayPatientDiscovery##ITI-55",
                        "ehealth-resid-qns%3A%3Aurn%3Aehealth%3AIdentityService%3A%3AXCPD%3A%3ACrossGatewayPatientDiscovery%23%23ITI-55",
                        "ehealth-resid-qns:: urn:ehealth:IdentityService::XCPD::CrossGatewayPatientDiscovery##ITI-55 ",
                        false),
                Arguments.of("Example 04", new SMPDocumentIdentifier("urn:XCPD::CrossGatewayPatientDiscovery", "ehealth-resid-qns"),
                        "ehealth-resid-qns::urn:XCPD::CrossGatewayPatientDiscovery",
                        "ehealth-resid-qns%3A%3Aurn%3AXCPD%3A%3ACrossGatewayPatientDiscovery",
                        "ehealth-resid-qns::urn:XCPD::CrossGatewayPatientDiscovery",
                        false),
                Arguments.of("Example 05", new SMPDocumentIdentifier("urn:ehealth:PatientService::XCA::CrossGatewayQuery##ITI-38", "ehealth-resid-qns"),
                        "ehealth-resid-qns::urn:ehealth:PatientService::XCA::CrossGatewayQuery##ITI-38",
                        "ehealth-resid-qns%3A%3Aurn%3Aehealth%3APatientService%3A%3AXCA%3A%3ACrossGatewayQuery%23%23ITI-38",
                        "ehealth-resid-qns::urn:ehealth:PatientService::XCA::CrossGatewayQuery##ITI-38",
                        false),
                Arguments.of("Example 06", new SMPDocumentIdentifier("urn:XCA::CrossGatewayQuery", "ehealth-resid-qns"),
                        "ehealth-resid-qns::urn:XCA::CrossGatewayQuery",
                        "ehealth-resid-qns%3A%3Aurn%3AXCA%3A%3ACrossGatewayQuery",
                        "ehealth-resid-qns::urn:XCA::CrossGatewayQuery",
                        false),
                Arguments.of("Example 07", new SMPDocumentIdentifier("urn:ehealth:OrderService::XCA::CrossGatewayQuery##ITI-38", "ehealth-resid-qns"),
                        "ehealth-resid-qns::urn:ehealth:OrderService::XCA::CrossGatewayQuery##ITI-38",
                        "ehealth-resid-qns%3A%3Aurn%3Aehealth%3AOrderService%3A%3AXCA%3A%3ACrossGatewayQuery%23%23ITI-38",
                        "ehealth-resid-qns::urn:ehealth:OrderService::XCA::CrossGatewayQuery##ITI-38",
                        false),
                Arguments.of("Example 08", new SMPDocumentIdentifier("urn:ehealth:DispensationService:Initialize::XDR::ProvideandRegisterDocumentSet-b##ITI-41", "ehealth-resid-qns"),
                        "ehealth-resid-qns::urn:ehealth:DispensationService:Initialize::XDR::ProvideandRegisterDocumentSet-b##ITI-41",
                        "ehealth-resid-qns%3A%3Aurn%3Aehealth%3ADispensationService%3AInitialize%3A%3AXDR%3A%3AProvideandRegisterDocumentSet-b%23%23ITI-41",
                        "ehealth-resid-qns::urn:ehealth:DispensationService:Initialize::XDR::ProvideandRegisterDocumentSet-b##ITI-41",
                        false),
                Arguments.of("Example 09", new SMPDocumentIdentifier("urn:XDR::ProvideandRegisterDocumentSet-b", "ehealth-resid-qns"),
                        "ehealth-resid-qns::urn:XDR::ProvideandRegisterDocumentSet-b",
                        "ehealth-resid-qns%3A%3Aurn%3AXDR%3A%3AProvideandRegisterDocumentSet-b",
                        "ehealth-resid-qns::urn:XDR::ProvideandRegisterDocumentSet-b",
                        false),
                Arguments.of("Example 10", new SMPDocumentIdentifier("urn:ehealth:DispensationService:Discard::XDR::ProvideandRegisterDocumentSet-b##ITI-41", "ehealth-resid-qns"),
                        "ehealth-resid-qns::urn:ehealth:DispensationService:Discard::XDR::ProvideandRegisterDocumentSet-b##ITI-41",
                        "ehealth-resid-qns%3A%3Aurn%3Aehealth%3ADispensationService%3ADiscard%3A%3AXDR%3A%3AProvideandRegisterDocumentSet-b%23%23ITI-41",
                        "ehealth-resid-qns::urn:ehealth:DispensationService:Discard::XDR::ProvideandRegisterDocumentSet-b##ITI-41",
                        false)
        );
    }


    // input parameters

    DocumentIdentifierFormatter testInstance = new DocumentIdentifierFormatter();

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("documentIdentifierCases")
    void testFormat(String name, SMPDocumentIdentifier identifierType, String formattedIdentifier, String uriFormattedIdentifier, String identifierToParse, boolean throwParseError) {

        String result = testInstance.format(identifierType);
        String uriResult = testInstance.urlEncodedFormat(identifierType);

        assertEquals(formattedIdentifier, result);
        assertEquals(uriFormattedIdentifier, uriResult);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("documentIdentifierCases")
    void testParse(String name, SMPDocumentIdentifier identifierType, String formattedIdentifier, String uriFormattedIdentifier, String identifierToParse, boolean throwParseError) {
        IllegalArgumentException exception = null;
        SMPDocumentIdentifier result = null;
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
