package eu.europa.ex.dynamicdiscovery.util;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public class Constants {
    public static final String SMP_DOMAIN = "http://localhost:8080/cipa-smp-full-webapp/";
    public static final String SMP_DOMAIN_ALIAS = "http://smp.ec.europa.eu/";

    public static final String SERVICE_METADATA_URL_urn_ehealth_pt_ncpb_idp = "/cipa-smp-full-webapp/ehealth-actorid-qns%3A%3Aurn%3Aehealth%3Apt%3Ancpb-idp/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A105";
    public static final String SERVICE_METADATA_BODY_urn_ehealth_pt_ncpb_idp = "<ns3:ServiceMetadata xmlns:ns3=\"http://docs.oasis-open.org/bdxr/ns/SMP/2014/07\" xmlns=\"http://busdox.org/transport/identifiers/1.0/\" xmlns:ns2=\"http://www.w3.org/2005/08/addressing\" xmlns:ns4=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
            "<ns3:ServiceInformation>\n" +
            "<ParticipantIdentifier scheme=\"ehealth-actorid-qns\">urn:ehealth:pt:ncpb-idp</ParticipantIdentifier>\n" +
            "<DocumentIdentifier scheme=\"ehealth-resid-qns\">urn::epsos##services:extended:epsos::105</DocumentIdentifier>\n" +
            "<ns3:ProcessList>\n" +
            "<ns3:Process>\n" +
            "<ProcessIdentifier scheme=\"ehealth-procid-qns\">urn:ehealth:ncp:vpngateway</ProcessIdentifier>\n" +
            "<ns3:ServiceEndpointList>\n" +
            "<ns3:Endpoint transportProfile=\"\">\n" +
            "<ns2:EndpointReference>\n" +
            "<ns2:Address>ipsec://qavpn.epsos-min.saude.pt</ns2:Address>\n" +
            "</ns2:EndpointReference>\n" +
            "<ns3:RequireBusinessLevelSignature>false</ns3:RequireBusinessLevelSignature>\n" +
            "<ns3:MinimumAuthenticationLevel>urn:epSOS:loa:1</ns3:MinimumAuthenticationLevel>\n" +
            "<ns3:ServiceActivationDate>2016-06-06T10:57:21Z</ns3:ServiceActivationDate>\n" +
            "<ns3:ServiceExpirationDate>2026-06-06T10:57:21Z</ns3:ServiceExpirationDate>\n" +
            "<ns3:Certificate>\n" +
            "MIID5zCCA1CgAwIBAgICA+QwDQYJKoZIhvcNAQENBQAwOjELMAkGA1UEBhMCRlIx\n" +
            " EzARBgNVBAoMCklIRSBFdXJvcGUxFjAUBgNVBAMMDUlIRSBFdXJvcGUgQ0EwHhcN\n" +
            " MTYwNjAxMTQzNTMxWhcNMjYwNjAxMTQzNTMxWjB9MQswCQYDVQQGEwJQVDEMMAoG\n" +
            " A1UECgwDTW9IMQ0wCwYDVQQLDARTUE1TMQ0wCwYDVQQqDARKb2FvMQ4wDAYDVQQF\n" +
            " EwVDdW5oYTEdMBsGA1UEAwwUcWFlcHNvcy5taW4tc2F1ZGUucHQxEzARBgNVBAwM\n" +
            " ClZQTiBTZXJ2ZXIwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDcopPo\n" +
            " wWuBSEokl4UE9BTpA6gzdzQvZrHawcsesieUNuJUBY04dD9dsnWjppz8JOE64Jmj\n" +
            " rxZp+QJxESL2LDrE99r74S+rHtUQG4AMrD6qcfVRmf/9kw9tiVTHc1SdP8ycXTDs\n" +
            " QjeTyymdBvL5c6TiDJafC6C/jaaqlmxekzIIYhVyhlr1JQH3L9TOcEpaKgNPm+HI\n" +
            " dzdrpJiayVJz6Th256ZV1rPvBHRhFxfaG5xh640YstTMvqnfVX8u+8KMcoQ/9TOm\n" +
            " CkMAeUiR9PduCZYaK6cE5519l5g/yWZD84YIz1P8fio1DnS8W1keV5DwXvHXiDNu\n" +
            " Jz84gzyDDox767rBAgMBAAGjggEzMIIBLzA+BgNVHR8ENzA1MDOgMaAvhi1odHRw\n" +
            " czovL2dhemVsbGUuaWhlLm5ldC9wa2kvY3JsLzY0My9jYWNybC5jcmwwPAYJYIZI\n" +
            " AYb4QgEEBC8WLWh0dHBzOi8vZ2F6ZWxsZS5paGUubmV0L3BraS9jcmwvNjQzL2Nh\n" +
            " Y3JsLmNybDA8BglghkgBhvhCAQMELxYtaHR0cHM6Ly9nYXplbGxlLmloZS5uZXQv\n" +
            " cGtpL2NybC82NDMvY2FjcmwuY3JsMB8GA1UdIwQYMBaAFOwzDhPIIl6i4WuvQ3t6\n" +
            " XdJ3cx1+MB0GA1UdDgQWBBQFn8LrGlDMCZ87E25x3y2+UzZWczAMBgNVHRMBAf8E\n" +
            " AjAAMA4GA1UdDwEB/wQEAwIFIDATBgNVHSUEDDAKBggrBgEFBQcDATANBgkqhkiG\n" +
            " 9w0BAQ0FAAOBgQBU7TdPBJmlgh+dS1YNJpUFezcaIDaf2c7POe2x2QsWfIayARhh\n" +
            " gxEhSwemUFjhNYDx5sF6Us6lwcHsC1wiuWSyXxG5OonqBauDe/+sAkS2yetWUlM4\n" +
            " a8YvefZ2X38Y1pIOa5Cfq1r8/G7n5liFweMFXhvc22YI6vk/VLEcZJAp2A==\n" +
            "</ns3:Certificate>\n" +
            "<ns3:ServiceDescription>This is the VPN Server configuration of the PT NCP</ns3:ServiceDescription>\n" +
            "<ns3:TechnicalContactUrl>licinio.mano@spms.min-saude.pt</ns3:TechnicalContactUrl>\n" +
            "<ns3:TechnicalInformationUrl>licinio.mano@spms.min-saude.pt</ns3:TechnicalInformationUrl>\n" +
            "<ns3:Extension>\n" +
            "<root xmlns=\"\" xmlns:ns5=\"http://busdox.org/transport/identifiers/1.0/\"/>\n" +
            "</ns3:Extension>\n" +
            "</ns3:Endpoint>\n" +
            "</ns3:ServiceEndpointList>\n" +
            "</ns3:Process>\n" +
            "</ns3:ProcessList>\n" +
            "</ns3:ServiceInformation>\n" +
            "</ns3:ServiceMetadata>";

    public static final String SERVICE_GROUP_URL_9925_0367302178 = "/cipa-smp-full-webapp/iso6523-actorid-upis%3A%3A9925%3A0367302178";
    public static final String SERVICE_GROUP_BODY_9925_0367302178 = "<ServiceGroup xmlns=\"http://docs.oasis-open.org/bdxr/ns/SMP/2014/07\" xmlns:ids=\"http://busdox.org/transport/identifiers/1.0/\">\n" +
            "<ids:ParticipantIdentifier scheme=\"busdox-actorid-upis\">9925:0367302178</ids:ParticipantIdentifier>\n" +
            "<ServiceMetadataReferenceCollection>\n" +
            "<ServiceMetadataReference href=\"http://smp.fe.babelway.net/iso6523-actorid-upis::9925%3A0367302178/services/busdox-docid-qns%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3ACreditNote-2%3A%3ACreditNote%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns014%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol5a%3Aver2.0%3A%3A2.1\"/>\n" +
            "<ServiceMetadataReference href=\"http://smp.fe.babelway.net/iso6523-actorid-upis::9925%3A0367302178/services/busdox-docid-qns%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3AInvoice-2%3A%3AInvoice%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns010%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol5a%3Aver2.0%3A%3A2.1\"/>\n" +
            "<ServiceMetadataReference href=\"http://smp.fe.babelway.net/iso6523-actorid-upis::9925%3A0367302178/services/busdox-docid-qns%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3AInvoice-2%3A%3AInvoice%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns010%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol4a%3Aver2.0%3A%3A2.1\"/>\n" +
            "</ServiceMetadataReferenceCollection>\n" +
            "<Extension/>\n" +
            "</ServiceGroup>";

    public static final String SERVICE_GROUP_URL_urn_ehealth_pt_ncpb_idp = "/cipa-smp-full-webapp/ehealth-actorid-qns%3A%3Aurn%3Aehealth%3Apt%3Ancpb-idp";
    public static final String SERVICE_GROUP_BODY_urn_ehealth_pt_ncpb_idp = "<ServiceGroup xmlns=\"http://docs.oasis-open.org/bdxr/ns/SMP/2014/07\" xmlns:ids=\"http://busdox.org/transport/identifiers/1.0/\">\n" +
            "<ids:ParticipantIdentifier scheme=\"ehealth-actorid-qns\">urn:ehealth:pt:ncpb-idp</ids:ParticipantIdentifier>\n" +
            "<ServiceMetadataReferenceCollection>\n" +
            "<ServiceMetadataReference href=\"http://ehealth.smp.e-sens.gr/ehealth-actorid-qns%3A%3Aurn%3Aehealth%3Apt%3Ancpb-idp/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A105\"/>\n" +
            "<ServiceMetadataReference href=\"http://ehealth.smp.e-sens.gr/ehealth-actorid-qns%3A%3Aurn%3Aehealth%3Apt%3Ancpb-idp/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A106\"/>\n" +
            "<ServiceMetadataReference href=\"http://ehealth.smp.e-sens.gr/ehealth-actorid-qns%3A%3Aurn%3Aehealth%3Apt%3Ancpb-idp/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A21\"/>\n" +
            "</ServiceMetadataReferenceCollection>\n" +
            "<Extension/>\n" +
            "</ServiceGroup>";


    public static final String SIGNED_SERVICE_METADATA_URL_0088_999888777 = "/cipa-smp-full-webapp/iso6523-actorid-upis%3A%3A0088%3A999888777/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A105";
    public static final String SIGNED_SERVICE_METADATA_BODY_0088_999888777 = "<ns0:SignedServiceMetadata xmlns:ns0=\"http://busdox.org/serviceMetadata/publishing/1.0/\" xmlns:ns1=\"http://busdox.org/transport/identifiers/1.0/\" xmlns:ns2=\"http://www.w3.org/2005/08/addressing\" xmlns:ns3=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
            "   <ns0:ServiceMetadata>\n" +
            "      <ns0:ServiceInformation>\n" +
            "         <ns1:ParticipantIdentifier scheme=\"iso6523-actorid-upis\">0088:999888777</ns1:ParticipantIdentifier>\n" +
            "         <ns1:DocumentIdentifier scheme=\"busdox-docid-qns\">urn:oasis:names:specification:ubl:schema:xsd:Invoice-12::Invoice##urn:www.cenbii.eu:transaction:biicoretrdm010:ver1.0:#urn:www.peppol.eu:bis:peppol4a:ver1.0::2.0</ns1:DocumentIdentifier>\n" +
            "         <ns0:ProcessList>\n" +
            "            <ns0:Process>\n" +
            "               <ns1:ProcessIdentifier scheme=\"cenbii-procid-ubl\">urn:www.cenbii.eu:profile:bii04:ver1.0</ns1:ProcessIdentifier>\n" +
            "               <ns0:ServiceEndpointList>\n" +
            "                  <ns0:Endpoint transportProfile=\"busdox-transport-as2-ver1p0\">\n" +
            "                     <ns2:EndpointReference>\n" +
            "                        <ns2:Address>http://busdox.org/otherService/as2</ns2:Address>\n" +
            "                     </ns2:EndpointReference>\n" +
            "                     <ns0:RequireBusinessLevelSignature>false</ns0:RequireBusinessLevelSignature>\n" +
            "                     <ns0:ServiceActivationDate>2009-05-01T09:00:00Z</ns0:ServiceActivationDate>\n" +
            "                     <ns0:ServiceExpirationDate>2016-05-01T09:00:00Z</ns0:ServiceExpirationDate>\n" +
            "                     <ns0:Certificate>CERTIFICATEA</ns0:Certificate>\n" +
            "                     <ns0:ServiceDescription>invoice service</ns0:ServiceDescription>\n" +
            "                     <ns0:TechnicalContactUrl>https://example.com</ns0:TechnicalContactUrl>\n" +
            "                  </ns0:Endpoint>\n" +
            "                  <ns0:Endpoint transportProfile=\"busdox-transport-as2-ver1p0\">\n" +
            "                     <ns2:EndpointReference>\n" +
            "                        <ns2:Address>http://d02di1010873.net1.cec.eu.int:7080/cipa-dispatcher/AS2Receiver/</ns2:Address>\n" +
            "                     </ns2:EndpointReference>\n" +
            "                     <ns0:RequireBusinessLevelSignature>false</ns0:RequireBusinessLevelSignature>\n" +
            "                     <ns0:ServiceActivationDate>2003-01-01T00:00:00Z</ns0:ServiceActivationDate>\n" +
            "                     <ns0:ServiceExpirationDate>2020-05-01T00:00:00Z</ns0:ServiceExpirationDate>\n" +
            "                     <ns0:Certificate>CERTIFICATEA</ns0:Certificate>\n" +
            "                     <ns0:ServiceDescription>invoice service AS2</ns0:ServiceDescription>\n" +
            "                     <ns0:TechnicalContactUrl>https://example.com</ns0:TechnicalContactUrl>\n" +
            "                  </ns0:Endpoint>\n" +
            "               </ns0:ServiceEndpointList>\n" +
            "            </ns0:Process>\n" +
            "         </ns0:ProcessList>\n" +
            "      </ns0:ServiceInformation>\n" +
            "   </ns0:ServiceMetadata>\n" +
            "   <Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
            "      <SignedInfo>\n" +
            "         <CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/>\n" +
            "         <SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/>\n" +
            "         <Reference URI=\"\">\n" +
            "            <Transforms>\n" +
            "               <Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/>\n" +
            "            </Transforms>\n" +
            "            <DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/>\n" +
            "            <DigestValue>R1L74tylY1NvRZu3E5cyoNPVva4=</DigestValue>\n" +
            "         </Reference>\n" +
            "      </SignedInfo>\n" +
            "      <SignatureValue>duRQqDjw22wo9NpafAB1vJlL2pAu0OCXMHcGxfgPl25+EUwG+yX4Qge4TVfohV+ocN0x47ugxyOy\n" +
            "JSEFJRGm06/x0xHoA0RNGqn7EjSAqgCU5gUuXadyxBO692BSRg4gtQcB03i2q5tTiT83z2GCe8to\n" +
            "xtajdh062ZqRSSLSkS4yp1KS4Ul1Q5kVQnSDcDxh8Ix+kiftXITxwcDF/ZH5IHOo0K3SvWX+Lgky\n" +
            "fbqm/5FV6JbockdhCYswK+OI+FSgvJk84WGUI9oUYHCM9Cp0xaPp2VcYtDHfr2uguhPybQtfjHl+\n" +
            "4RxULeGfpuDW4cS4Q4ReBgImjupJhsUKtYdCLg==</SignatureValue>\n" +
            "      <KeyInfo>\n" +
            "         <X509Data>\n" +
            "            <X509SubjectName>O=DG-DIGIT,CN=SMP_2000000002,C=BE</X509SubjectName>\n" +
            "            <X509Certificate>MIIETzCCAzegAwIBAgIQARFxaLrzuXQpqYLY0ySE1DANBgkqhkiG9w0BAQsFADBlMQswCQYDVQQG\n" +
            "EwJESzEnMCUGA1UEChMeTkFUSU9OQUwgSVQgQU5EIFRFTEVDT00gQUdFTkNZMS0wKwYDVQQDEyRQ\n" +
            "RVBQT0wgU0VSVklDRSBNRVRBREFUQSBQVUJMSVNIRVIgQ0EwHhcNMTMwNDA0MDAwMDAwWhcNMTUw\n" +
            "NDA0MjM1OTU5WjA5MQswCQYDVQQGEwJCRTEXMBUGA1UEAwwOU01QXzIwMDAwMDAwMDIxETAPBgNV\n" +
            "BAoMCERHLURJR0lUMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwjxHvWWnijln+yip\n" +
            "xRbTkYdhEWJnKS3Hc4cCzrwtQpgR5jXQUtOpn6CK0Xj7WO6UU8Wly/mu7oX0FUtqrNYMCs/I2hrD\n" +
            "zYRj0Zaa68cjgqcPIljUVHymzlIayUR2WP60v7pPAN6SSZUaOn4zRMvDzdpO+519avWrkgiLhzXS\n" +
            "FPgFwZsU2KxoWKT/qisbooJDTCDK7c2J39i8ZOEnfT1u9Jb9KRF4NDH1+OBy+IYXnAkbitRLzIoK\n" +
            "r5Re1BcyNPc3bF8qsbdztBA8fmIEnR+WzQ7obDIiXqbhXKER5+0C6zTB/iMG5+HxjLlQQaJyN034\n" +
            "bKaxLaFbvnA6Az182Yp+ZwIDAQABo4IBJTCCASEwCQYDVR0TBAIwADALBgNVHQ8EBAMCA7gweQYD\n" +
            "VR0fBHIwcDBuoGygaoZoaHR0cDovL29uc2l0ZWNybC52ZXJpc2lnbi5jb20vRGlnaXRhbGlzZXJp\n" +
            "bmdzc3R5cmVsc2VuT3BlblBFUFBPTFNFUlZJQ0VNRVRBREFUQVBVQkxJU0hFUkNBL0xhdGVzdENS\n" +
            "TC5jcmwwHwYDVR0jBBgwFoAUecu1tBDrlLX1esCsLlWh24o33xQwHQYDVR0OBBYEFI49FXA9zFx9\n" +
            "Y56Rb7VsLQaa8ALyMDcGCCsGAQUFBwEBBCswKTAnBggrBgEFBQcwAYYbaHR0cDovL3BraS1vY3Nw\n" +
            "LnN5bWF1dGguY29tMBMGA1UdJQQMMAoGCCsGAQUFBwMCMA0GCSqGSIb3DQEBCwUAA4IBAQCrYw5q\n" +
            "RtmIFAr8mmZQUaF4TtWTkOgTctZy7IIAdS1ABnOqtU86fQFxxReUb9ASujG4YtZEXXuvlnBvunmQ\n" +
            "FDqL0S3vEIq0fLVeMwkuez/E7LQG0aUANDs2CX7zlqaB3zWZbAz4bNlG1TwJctEagUChhHKk4Fsw\n" +
            "Avv9E453uDSp0nfa4a8PH7YHDp7yA2iUgf5hAWodei9loYfIupSgh597wecQMO39XARYT2g1Mrzy\n" +
            "cKhuaX7A1tvvtWe6XDZoP/VCaNOstRH9l8VhomV7evhscULg4FuYcRCJ/k87xh+25h2ZA851huz1\n" +
            "H4yz2blzTKFzEku5AI6WT7BrIuuW8CdR</X509Certificate>\n" +
            "         </X509Data>\n" +
            "      </KeyInfo>\n" +
            "   </Signature>\n" +
            "</ns0:SignedServiceMetadata>";


    public static final String SIGNED_SERVICE_METADATA_URL_urn_germany_ncpb = "/cipa-smp-full-webapp/ehealth-participantid-qns%3A%3Aurn%3Agermany%3Ancpb/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A105";
    public static final String SIGNED_SERVICE_METADATA_BODY_urn_germany_ncpb = "<SignedServiceMetadata xmlns=\"http://docs.oasis-open.org/bdxr/ns/SMP/2014/07\"\n" +
            "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "    xsi:schemaLocation=\"http://docs.oasis-open.org/bdxr/ns/SMP/2014/07 file:/Users/max/Downloads/bdx-smp-201407.xsd\"\n" +
            "    xmlns:ns=\"urn:esens:smp\">\n" +
            "    <ServiceMetadata>\n" +
            "        <ServiceInformation>\n" +
            "            <!-- Same as the service group -->\n" +
            "            <ParticipantIdentifier scheme=\"ehealth-participantid-qns\">urn:germany:ncpb</ParticipantIdentifier>\n" +
            "            <DocumentIdentifier scheme=\"epsos-docid-qns\">epsos-docid-qns::urn:epsos:services##epsos-21</DocumentIdentifier>\n" +
            "            <ProcessList>\n" +
            "                <Process>\n" +
            "                    <ProcessIdentifier scheme=\"ehealth-procid-qns\">urn:germany:ncpb:epsosPatientService::List</ProcessIdentifier>\n" +
            "                    <ServiceEndpointList>\n" +
            "                        <Endpoint transportProfile=\"urn:ihe:iti:2013:xcpd\">\n" +
            "                            \n" +
            "                            <EndpointURI>http://germany/ncp/patient/list</EndpointURI>\n" +
            "                            <RequireBusinessLevelSignature>false</RequireBusinessLevelSignature>\n" +
            "                            <MinimumAuthenticationLevel>urn:epSOS:loa:1</MinimumAuthenticationLevel>\n" +
            "                            <ServiceActivationDate>2015-04-29T12:55:39Z</ServiceActivationDate>\n" +
            "                            <ServiceExpirationDate>2015-04-29T12:55:39Z</ServiceExpirationDate>\n" +
            "                            <Certificate>SGksIEkgYW0gYSBuaWNlIFg1MDkgQ2VydGlmaWNhdGU=</Certificate>\n" +
            "                            <ServiceDescription>This is the epSOS Patient Service List for the German NCP</ServiceDescription>\n" +
            "                            <TechnicalContactUrl>http://germany/contact</TechnicalContactUrl>\n" +
            "                            <TechnicalInformationUrl>http://germany/contact</TechnicalInformationUrl>\n" +
            "                            \n" +
            "                        </Endpoint>\n" +
            "                    </ServiceEndpointList>\n" +
            "                    \n" +
            "                </Process>\n" +
            "            </ProcessList>\n" +
            "            \n" +
            "        </ServiceInformation>\n" +
            "    </ServiceMetadata>\n" +
            "   <Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
            "  <SignedInfo>\n" +
            "   <CanonicalizationMethod\n" +
            "    Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/>\n" +
            "   <SignatureMethod\n" +
            "    Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/>\n" +
            "   <Reference URI=\"\">\n" +
            "    <Transforms>\n" +
            "     <Transform\n" +
            "      Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/>\n" +
            "    </Transforms>\n" +
            "    <DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/>\n" +
            "    <DigestValue>tVicGh6V+8cHbVYFIU91o5+L3OQ=</DigestValue>\n" +
            "   </Reference>\n" +
            "  </SignedInfo>\n" +
            "  <SignatureValue>\n" +
            "   dJDHiGQMaKN8iPuWApAL57eVnxz2BQtyujwfPSgE7HyKoxYtoRB97ocxZ\n" +
            "   8ZU440wHtE39ZwRGIjvwor3WfURxnIgnI1CChMXXwoGpHH//Zc0z4ejaz\n" +
            "   DuCNEq4Mm4OUVTiEVuwcWAOMkfDHaM82awYQiOGcwMbZe38UX0oPJ2DOE=\n" +
            "  </SignatureValue>\n" +
            "  <KeyInfo>\n" +
            "   <X509Data>\n" +
            "    <X509SubjectName>O=DG-DIGIT,CN=SMP_2000000002,C=BE</X509SubjectName>\n" +
            "    <X509Certificate>MIIETzCCAzegAwIBAgIQARFxaLrzuXQpqYLY0ySE1DANBgkqhkiG9w0BAQsFADBlMQswCQYDVQQG\n" +
            "EwJESzEnMCUGA1UEChMeTkFUSU9OQUwgSVQgQU5EIFRFTEVDT00gQUdFTkNZMS0wKwYDVQQDEyRQ\n" +
            "RVBQT0wgU0VSVklDRSBNRVRBREFUQSBQVUJMSVNIRVIgQ0EwHhcNMTMwNDA0MDAwMDAwWhcNMTUw\n" +
            "NDA0MjM1OTU5WjA5MQswCQYDVQQGEwJCRTEXMBUGA1UEAwwOU01QXzIwMDAwMDAwMDIxETAPBgNV\n" +
            "BAoMCERHLURJR0lUMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwjxHvWWnijln+yip\n" +
            "xRbTkYdhEWJnKS3Hc4cCzrwtQpgR5jXQUtOpn6CK0Xj7WO6UU8Wly/mu7oX0FUtqrNYMCs/I2hrD\n" +
            "zYRj0Zaa68cjgqcPIljUVHymzlIayUR2WP60v7pPAN6SSZUaOn4zRMvDzdpO+519avWrkgiLhzXS\n" +
            "FPgFwZsU2KxoWKT/qisbooJDTCDK7c2J39i8ZOEnfT1u9Jb9KRF4NDH1+OBy+IYXnAkbitRLzIoK\n" +
            "r5Re1BcyNPc3bF8qsbdztBA8fmIEnR+WzQ7obDIiXqbhXKER5+0C6zTB/iMG5+HxjLlQQaJyN034\n" +
            "bKaxLaFbvnA6Az182Yp+ZwIDAQABo4IBJTCCASEwCQYDVR0TBAIwADALBgNVHQ8EBAMCA7gweQYD\n" +
            "VR0fBHIwcDBuoGygaoZoaHR0cDovL29uc2l0ZWNybC52ZXJpc2lnbi5jb20vRGlnaXRhbGlzZXJp\n" +
            "bmdzc3R5cmVsc2VuT3BlblBFUFBPTFNFUlZJQ0VNRVRBREFUQVBVQkxJU0hFUkNBL0xhdGVzdENS\n" +
            "TC5jcmwwHwYDVR0jBBgwFoAUecu1tBDrlLX1esCsLlWh24o33xQwHQYDVR0OBBYEFI49FXA9zFx9\n" +
            "Y56Rb7VsLQaa8ALyMDcGCCsGAQUFBwEBBCswKTAnBggrBgEFBQcwAYYbaHR0cDovL3BraS1vY3Nw\n" +
            "LnN5bWF1dGguY29tMBMGA1UdJQQMMAoGCCsGAQUFBwMCMA0GCSqGSIb3DQEBCwUAA4IBAQCrYw5q\n" +
            "RtmIFAr8mmZQUaF4TtWTkOgTctZy7IIAdS1ABnOqtU86fQFxxReUb9ASujG4YtZEXXuvlnBvunmQ\n" +
            "FDqL0S3vEIq0fLVeMwkuez/E7LQG0aUANDs2CX7zlqaB3zWZbAz4bNlG1TwJctEagUChhHKk4Fsw\n" +
            "Avv9E453uDSp0nfa4a8PH7YHDp7yA2iUgf5hAWodei9loYfIupSgh597wecQMO39XARYT2g1Mrzy\n" +
            "cKhuaX7A1tvvtWe6XDZoP/VCaNOstRH9l8VhomV7evhscULg4FuYcRCJ/k87xh+25h2ZA851huz1\n" +
            "H4yz2blzTKFzEku5AI6WT7BrIuuW8CdR</X509Certificate>\n" +
            "   </X509Data>\n" +
            "  </KeyInfo>\n" +
            " </Signature>\n" +
            "</SignedServiceMetadata>";
}
