package eu.europa.ex.dynamicdiscovery;

/**
 * Created by rodrfla on 10/10/2016.
 */
public class Constants {
    public static final String SMP_DOMAIN = "http://localhost:8080/cipa-smp-full-webapp/";
    public static final String SMP_DOMAIN_ALIAS = "http://smp.ec.europa.eu/";

    public static final String SERVICE_METADATA_URL_urn_ehealth_pt_ncpb_idp = "/cipa-smp-full-webapp/ehealth-actorid-qns%3A%3Aurn%3Aehealth%3Apt%3Ancpb-idp/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A105";
    public static final String SERVICE_METADATA_BODY_urn_ehealth_pt_ncpb_idp = "<ns3:SignedServiceMetadata xmlns:ns3=\"http://busdox.org/serviceMetadata/publishing/1.0/\" xmlns=\"http://busdox.org/transport/identifiers/1.0/\" xmlns:ns2=\"http://www.w3.org/2005/08/addressing\" xmlns:ns4=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
            "<ns3:ServiceMetadata>\n" +
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
            "</ns3:ServiceMetadata>\n" +
            "<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
            "<SignedInfo>\n" +
            "<CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/>\n" +
            "<SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/>\n" +
            "<Reference URI=\"\">\n" +
            "<Transforms>\n" +
            "<Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/>\n" +
            "</Transforms>\n" +
            "<DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/>\n" +
            "<DigestValue>t9b10ekCN6O9uhrm/UaglanltLY=</DigestValue>\n" +
            "</Reference>\n" +
            "</SignedInfo>\n" +
            "<SignatureValue>\n" +
            "f9wQQ7lou7TSz97Q7ZOLUYWrsWW+zfJdRYTKWYcNrLnoO9v5tAY9Lu/WPFQupLK3FffRkf6AfJWx hn8RlQVTC/+brGBcfEwx+FnOn3l6Gke+2CPUgFY6XFn91BbGv4vxuSXFW3vGdGYTxJDyokpBndg7 caqbgl8Tu2/1KT+4ICQ3E15txPcreXYo2tkoJAtZdBeih9MATYuiIaS+XKzCKTv6LzomLC/n6aNc XeREJmMF9lFqQUnv90Re2O0rQDu6YZJDkO+1m5UiG9JQGvXwWXTsmmiXFXCiwvUgii48g8h3CJbm ZkO/VQ+lQlKQiNR1DMHLibjN0rVqUSj/6SiNTQ==\n" +
            "</SignatureValue>\n" +
            "<KeyInfo>\n" +
            "<X509Data>\n" +
            "<X509SubjectName>CN=SMP_2000000177,O=UPRC,C=GR</X509SubjectName>\n" +
            "<X509Certificate>\n" +
            "MIIEgDCCA2igAwIBAgIQMv82uQdbVTITU7BPpa6sIDANBgkqhkiG9w0BAQsFADCBizELMAkGA1UE BhMCREsxJzAlBgNVBAoTHk5BVElPTkFMIElUIEFORCBURUxFQ09NIEFHRU5DWTEfMB0GA1UECxMW Rk9SIFRFU1QgUFVSUE9TRVMgT05MWTEyMDAGA1UEAxMpUEVQUE9MIFNFUlZJQ0UgTUVUQURBVEEg UFVCTElTSEVSIFRFU1QgQ0EwHhcNMTUxMTMwMDAwMDAwWhcNMTcxMTI5MjM1OTU5WjA1MQswCQYD VQQGEwJHUjENMAsGA1UECgwEVVBSQzEXMBUGA1UEAwwOU01QXzIwMDAwMDAxNzcwggEiMA0GCSqG SIb3DQEBAQUAA4IBDwAwggEKAoIBAQDL5zB6HI3P2LFqsPrUE1tLFnsXev4TsQYVUgItzWRXokq6 3MKXLl/pMFRYUz1kmckTdRYcZJJInbJkPZgsXtorVw/WUqsj6C7xMHJvIdBRemQG8TsknDvmXjBJ JnJToV/+7zcAGadsyVbwB8EM8GNvmqIc0AwMyWkpmcMTvF8WO4An5BhA04rV6m1TEwkeXpFJrYyO ewMqvJJvi8GRpVhW2VGyfM44jipaxgwMmAaNuVUxKFBf0RA0g0e0tdN6cP8EQQ6LaMQwxacYDDBG b3khdXZnuAi4E04faAuizCfkAZlqT9ujB6lQTXFZThs1zQ8X/VgqmbPM1froIXCNgB1PAgMBAAGj ggEzMIIBLzAJBgNVHRMEAjAAMAsGA1UdDwQEAwIDuDCBgwYDVR0fBHwwejB4oHagdIZyaHR0cDov L3BpbG90b25zaXRlY3JsLnZlcmlzaWduLmNvbS9EaWdpdGFsaXNlcmluZ3NzdHlyZWxzZW5QaWxv dE9wZW5QRVBQT0xTRVJWSUNFTUVUQURBVEFQVUJMSVNIRVJDQS9MYXRlc3RDUkwuY3JsMB8GA1Ud IwQYMBaAFODHhUA1Ov0c8/Zewh8w0PJkoQJ3MB0GA1UdDgQWBBSelAllYQAN2WWp9v/S8kdLwBOi LDA6BggrBgEFBQcBAQQuMCwwKgYIKwYBBQUHMAGGHmh0dHA6Ly9waWxvdC1vY3NwLnZlcmlzaWdu LmNvbTATBgNVHSUEDDAKBggrBgEFBQcDAjANBgkqhkiG9w0BAQsFAAOCAQEAJDwUmOC0dXpJvFuv 4UDtHSRHWBGTN7QjcVyMco8JpJ07H+gU14NX/nHNu88eLDZEXDyHuDXnk1Oo8dIy1n1vwR+Taeoi ANZZvqfPlUpQdgg+7HHhVhbmuut4lMWHot4AgXj7ZLQpmjRCFFYEEP+4daPidCykkXUWGIKeSZqq 0xJ8WhWa6qx8A/LDJ4oabEE32QuRLJusXilifss1sVa/CVK+DyqbIYFwdRiSxfAx15AzQaGetbmo 1+SA9islZNgv01BkdCmACxLA5zciTRCVZwAJNCk8S9T/Lw5Sh4wl7Cc0kzwgMTAreqerLSNTUDi4 XwaGFa/0jUuu0yZv5tpg7A==\n" +
            "</X509Certificate>\n" +
            "</X509Data>\n" +
            "</KeyInfo>\n" +
            "</Signature>\n" +
            "</ns3:SignedServiceMetadata>";

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
