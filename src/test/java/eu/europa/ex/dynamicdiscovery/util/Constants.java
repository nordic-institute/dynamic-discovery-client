package eu.europa.ex.dynamicdiscovery.util;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public class Constants {
    public static final String SMP_DOMAIN = "http://localhost:8080/cipa-smp-full-webapp/";
    public static final String SMP_DOMAIN_ALIAS = "http://smp.ec.europa.eu/";

    public static final String SERVICE_METADATA_URL_urn_ehealth_pt_ncpb_idp = "/cipa-smp-full-webapp/ehealth-actorid-qns%3A%3Aurn%3Aehealth%3Apt%3Ancpb-idp/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A105";
    public static final String SERVICE_METADATA_BODY_urn_ehealth_pt_ncpb_idp = "<ns3:ServiceMetadata xmlns:ns3=\"http://busdox.org/serviceMetadata/publishing/1.0/\" xmlns=\"http://busdox.org/transport/identifiers/1.0/\" xmlns:ns2=\"http://www.w3.org/2005/08/addressing\" xmlns:ns4=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
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
    public static final String SERVICE_GROUP_BODY_9925_0367302178 = "<ServiceGroup xmlns=\"http://busdox.org/serviceMetadata/publishing/1.0/\" xmlns:ids=\"http://busdox.org/transport/identifiers/1.0/\">\n" +
            "<ids:ParticipantIdentifier scheme=\"busdox-actorid-upis\">9925:0367302178</ids:ParticipantIdentifier>\n" +
            "<ServiceMetadataReferenceCollection>\n" +
            "<ServiceMetadataReference href=\"http://smp.fe.babelway.net/iso6523-actorid-upis::9925%3A0367302178/services/busdox-docid-qns%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3ACreditNote-2%3A%3ACreditNote%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns014%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol5a%3Aver2.0%3A%3A2.1\"/>\n" +
            "<ServiceMetadataReference href=\"http://smp.fe.babelway.net/iso6523-actorid-upis::9925%3A0367302178/services/busdox-docid-qns%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3AInvoice-2%3A%3AInvoice%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns010%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol5a%3Aver2.0%3A%3A2.1\"/>\n" +
            "<ServiceMetadataReference href=\"http://smp.fe.babelway.net/iso6523-actorid-upis::9925%3A0367302178/services/busdox-docid-qns%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3AInvoice-2%3A%3AInvoice%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns010%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol4a%3Aver2.0%3A%3A2.1\"/>\n" +
            "</ServiceMetadataReferenceCollection>\n" +
            "<Extension/>\n" +
            "</ServiceGroup>";

    public static final String SERVICE_GROUP_URL_urn_ehealth_pt_ncpb_idp = "/cipa-smp-full-webapp/ehealth-actorid-qns%3A%3Aurn%3Aehealth%3Apt%3Ancpb-idp";
    public static final String SERVICE_GROUP_BODY_urn_ehealth_pt_ncpb_idp = "<ns2:ServiceGroup xmlns=\"http://busdox.org/transport/identifiers/1.0/\" xmlns:ns2=\"http://busdox.org/serviceMetadata/publishing/1.0/\" xmlns:ns3=\"http://www.w3.org/2005/08/addressing\" xmlns:ns4=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
            "<ParticipantIdentifier scheme=\"ehealth-actorid-qns\">urn:ehealth:pt:ncpb-idp</ParticipantIdentifier>\n" +
            "<ns2:ServiceMetadataReferenceCollection>\n" +
            "<ns2:ServiceMetadataReference href=\"http://ehealth.smp.e-sens.gr/ehealth-actorid-qns%3A%3Aurn%3Aehealth%3Apt%3Ancpb-idp/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A105\"/>\n" +
            "<ns2:ServiceMetadataReference href=\"http://ehealth.smp.e-sens.gr/ehealth-actorid-qns%3A%3Aurn%3Aehealth%3Apt%3Ancpb-idp/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A106\"/>\n" +
            "<ns2:ServiceMetadataReference href=\"http://ehealth.smp.e-sens.gr/ehealth-actorid-qns%3A%3Aurn%3Aehealth%3Apt%3Ancpb-idp/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A107\"/>\n" +
            "<ns2:ServiceMetadataReference href=\"http://ehealth.smp.e-sens.gr/ehealth-actorid-qns%3A%3Aurn%3Aehealth%3Apt%3Ancpb-idp/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A11\"/>\n" +
            "<ns2:ServiceMetadataReference href=\"http://ehealth.smp.e-sens.gr/ehealth-actorid-qns%3A%3Aurn%3Aehealth%3Apt%3Ancpb-idp/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A21\"/>\n" +
            "<ns2:ServiceMetadataReference href=\"http://ehealth.smp.e-sens.gr/ehealth-actorid-qns%3A%3Aurn%3Aehealth%3Apt%3Ancpb-idp/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A31\"/>\n" +
            "<ns2:ServiceMetadataReference href=\"http://ehealth.smp.e-sens.gr/ehealth-actorid-qns%3A%3Aurn%3Aehealth%3Apt%3Ancpb-idp/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A41\"/>\n" +
            "<ns2:ServiceMetadataReference href=\"http://ehealth.smp.e-sens.gr/ehealth-actorid-qns%3A%3Aurn%3Aehealth%3Apt%3Ancpb-idp/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A42\"/>\n" +
            "<ns2:ServiceMetadataReference href=\"http://ehealth.smp.e-sens.gr/ehealth-actorid-qns%3A%3Aurn%3Aehealth%3Apt%3Ancpb-idp/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A51\"/>\n" +
            "<ns2:ServiceMetadataReference href=\"http://ehealth.smp.e-sens.gr/ehealth-actorid-qns%3A%3Aurn%3Aehealth%3Apt%3Ancpb-idp/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A52\"/>\n" +
            "<ns2:ServiceMetadataReference href=\"http://ehealth.smp.e-sens.gr/ehealth-actorid-qns%3A%3Aurn%3Aehealth%3Apt%3Ancpb-idp/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A97\"/>\n" +
            "</ns2:ServiceMetadataReferenceCollection>\n" +
            "</ns2:ServiceGroup>";
}
