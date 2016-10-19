/*
 * Copyright 2016 Dynamic Discovery Client Project
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/servlets/Docbb6d.pdf?id=31979
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */
package eu.europa.ex.dynamicdiscovery.util;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public class Constants {
    public static final String SMP_DOMAIN = "http://localhost:8080/cipa-smp-full-webapp/";
    public static final String SMP_DOMAIN_ALIAS = "http://smp.ec.europa.eu/";

    public static final String SERVICE_METADATA_URL_URN_EHEALTH_PT_NCPB_IDP = "/cipa-smp-full-webapp/ehealth-actorid-qns%3A%3Aurn%3Aehealth%3Apt%3Ancpb-idp/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A105";
    public static final String SERVICE_METADATA_BODY_URN_EHEALTH_PT_NCPB_IDP = "<ns3:ServiceMetadata xmlns:ns3=\"http://docs.oasis-open.org/bdxr/ns/SMP/2014/07\" xmlns=\"http://busdox.org/transport/identifiers/1.0/\" xmlns:ns2=\"http://www.w3.org/2005/08/addressing\" xmlns:ns4=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
            "<ns3:ServiceInformation>\n" +
            "<ParticipantIdentifier scheme=\"ehealth-actorid-qns\">urn:ehealth:pt:ncpb-idp</ParticipantIdentifier>\n" +
            "<DocumentIdentifier scheme=\"ehealth-resid-qns\">urn::epsos##services:extended:epsos::105</DocumentIdentifier>\n" +
            "<ns3:ProcessList>\n" +
            "<ns3:Process>\n" +
            "<ProcessIdentifier scheme=\"ehealth-procid-qns\">urn:ehealth:ncp:vpngateway</ProcessIdentifier>\n" +
            "<ns3:ServiceEndpointList>\n" +
            "<ns3:Endpoint transportProfile=\"\">\n" +
            "<ns2:EndpointReference>\n" +
            "<ns2:Address>ipsec://cipa-smp-full-webapp.com</ns2:Address>\n" +
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
            "<ns3:ServiceDescription></ns3:ServiceDescription>\n" +
            "<ns3:TechnicalContactUrl></ns3:TechnicalContactUrl>\n" +
            "<ns3:TechnicalInformationUrl></ns3:TechnicalInformationUrl>\n" +
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
            "<ServiceMetadataReference href=\"http://cipa-smp-full-webapp.com/iso6523-actorid-upis::9925%3A0367302178/services/busdox-docid-qns%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3ACreditNote-2%3A%3ACreditNote%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns014%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol5a%3Aver2.0%3A%3A2.1\"/>\n" +
            "<ServiceMetadataReference href=\"http://cipa-smp-full-webapp.com/iso6523-actorid-upis::9925%3A0367302178/services/busdox-docid-qns%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3AInvoice-2%3A%3AInvoice%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns010%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol5a%3Aver2.0%3A%3A2.1\"/>\n" +
            "<ServiceMetadataReference href=\"http://cipa-smp-full-webapp.com/iso6523-actorid-upis::9925%3A0367302178/services/busdox-docid-qns%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3AInvoice-2%3A%3AInvoice%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns010%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol4a%3Aver2.0%3A%3A2.1\"/>\n" +
            "</ServiceMetadataReferenceCollection>\n" +
            "<Extension/>\n" +
            "</ServiceGroup>";

    public static final String SERVICE_GROUP_URL_URN_EHEALTH_BE_NCPB_IDP = "/cipa-smp-full-webapp/ehealth-actorid-qns%3A%3Aurn%3Aehealth%3Abe%3Ancpb-idp";
    public static final String SERVICE_GROUP_BODY_URN_EHEALTH_BE_NCPB_IDP = "<ServiceGroup xmlns=\"http://docs.oasis-open.org/bdxr/ns/SMP/2014/07\" xmlns:ids=\"http://busdox.org/transport/identifiers/1.0/\">\n" +
            "<ids:ParticipantIdentifier scheme=\"ehealth-actorid-qns\">urn:ehealth:be:ncpb-idp</ids:ParticipantIdentifier>\n" +
            "<ServiceMetadataReferenceCollection>\n" +
            "<ServiceMetadataReference href=\"http://cipa-smp-full-webapp.com/ehealth-actorid-qns%3A%3Aurn%3Aehealth%3Abe%3Ancpb-idp/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A105\"/>\n" +
            "<ServiceMetadataReference href=\"http://cipa-smp-full-webapp.com/ehealth-actorid-qns%3A%3Aurn%3Aehealth%3Abe%3Ancpb-idp/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A106\"/>\n" +
            "<ServiceMetadataReference href=\"http://cipa-smp-full-webapp.com/ehealth-actorid-qns%3A%3Aurn%3Aehealth%3Abe%3Ancpb-idp/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A21\"/>\n" +
            "</ServiceMetadataReferenceCollection>\n" +
            "<Extension/>\n" +
            "</ServiceGroup>";

    public static final String SIGNED_SERVICE_METADATA_URL_urn_germany_ncpb = "/cipa-smp-full-webapp/ehealth-participantid-qns%3A%3Aurn%3Agermany%3Ancpb/services/ehealth-resid-qns%3A%3Aurn%3Aepsos%3A%3Aservices%23%23epsos-21";
    public static final String SIGNED_SERVICE_METADATA_BODY_urn_germany_ncpb = "<SignedServiceMetadata xmlns=\"http://docs.oasis-open.org/bdxr/ns/SMP/2014/07\" \n" +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns=\"urn:esens:smp\">\n" +
            "<ServiceMetadata xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wsswssecurity-utility-1.0.xsd\">\n" +
            "        <ServiceInformation>\n" +
            "            <ParticipantIdentifier scheme=\"ehealth-participantid-qns\">urn:germany:ncpb</ParticipantIdentifier>\n" +
            "            <DocumentIdentifier scheme=\"epsos-docid-qns\">urn:epsos::services##epsos-21</DocumentIdentifier>\n" +
            "            <ProcessList>\n" +
            "                <Process>\n" +
            "                    <ProcessIdentifier scheme=\"ehealth-procid-qns\">urn:germany:ncpb:epsosPatientService::List</ProcessIdentifier>\n" +
            "                    <ServiceEndpointList>\n" +
            "                        <Endpoint transportProfile=\"urn:ihe:iti:2013:xcpd\">\n" +
            "                            <EndpointURI>http://germany/ncp/patient/list</EndpointURI>\n" +
            "                            <RequireBusinessLevelSignature>false</RequireBusinessLevelSignature>\n" +
            "                            <MinimumAuthenticationLevel>urn:epSOS:loa:1</MinimumAuthenticationLevel>\n" +
            "                            <ServiceActivationDate>2015-04-29T12:55:39Z</ServiceActivationDate>\n" +
            "                            <ServiceExpirationDate>2015-04-29T12:55:39Z</ServiceExpirationDate>\n" +
            "<Certificate>\n" +
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
            "</Certificate>\n" +
            "                            <ServiceDescription>This is the epSOS Patient Service List for the German NCP</ServiceDescription>\n" +
            "                            <TechnicalContactUrl>http://germany/contact</TechnicalContactUrl>\n" +
            "                            <TechnicalInformationUrl>http://germany/contact</TechnicalInformationUrl>\n" +
            "                        </Endpoint>\n" +
            "                    </ServiceEndpointList>\n" +
            "                </Process>\n" +
            "            </ProcessList>\n" +
            "        </ServiceInformation>\n" +
            "    </ServiceMetadata>" +
            "   <Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
            "  <SignedInfo>\n" +
            "   <CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments\"/>\n" +
            "   <SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/>\n" +
            "   <Reference URI=\"\">\n" +
            "    <Transforms>\n" +
            "     <Transform\n" +
            "      Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/>\n" +
            "    </Transforms>\n" +
            "   <DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/>\n" +
            "    <DigestValue>7/XTsHaBSOnJ/jXD5v0zL6VKYsk=</DigestValue>\n" +
            "   </Reference>\n" +
            "  </SignedInfo>\n" +
            "  <SignatureValue>ZRW+yWq0BdSAsoyAvLMeeODEermYbOnL/G67eZZilf3LVPJ/sI7h1gyrjtEHFymsYcO9ltB91m+VwOWIaPK74A==</SignatureValue>\n" +
            "  <KeyInfo>\n" +
            "   <X509Data>" +
            "       <X509SubjectName>1.2.840.113549.1.9.1=#160e73656e64657240746573742e6265,CN=senderCN,OU=B4,O=DIGIT,L=Brussels,ST=BE,C=BE</X509SubjectName>\n" +
            "       <X509Certificate>MIICpTCCAg6gAwIBAgIBATANBgkqhkiG9w0BAQUFADB4MQswCQYDVQQGEwJCRTELMAkGA1UECAwC\n" +
            "QkUxETAPBgNVBAcMCEJydXNzZWxzMQ4wDAYDVQQKDAVESUdJVDELMAkGA1UECwwCQjQxDzANBgNV\n" +
            "BAMMBnJvb3RDTjEbMBkGCSqGSIb3DQEJARYMcm9vdEB0ZXN0LmJlMB4XDTE1MDMxNzE2MTkwN1oX\n" +
            "DTI1MDMxNDE2MTkwN1owfDELMAkGA1UEBhMCQkUxCzAJBgNVBAgMAkJFMREwDwYDVQQHDAhCcnVz\n" +
            "c2VsczEOMAwGA1UECgwFRElHSVQxCzAJBgNVBAsMAkI0MREwDwYDVQQDDAhzZW5kZXJDTjEdMBsG\n" +
            "CSqGSIb3DQEJARYOc2VuZGVyQHRlc3QuYmUwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBANxL\n" +
            "UPjIn7R0CsHf86kIwNzCu+6AdmWM8fBLUHL+VXT6ayr1kwgGbFMb/vUUX6a46jRCiZBM+9IK1Hpj\n" +
            "g9QX/QIQiWtvD+yDr6jUxahZ/w13kqFG/K81IVu9DwLBoiNwDvQ6l6UbvMvV+1nWy3gjRcKlFs/C\n" +
            "+E2uybgJxSM/sMkbAgMBAAGjOzA5MB8GA1UdIwQYMBaAFHCVSh4WnWR8MGBGedr+bJH96tc4MAkG\n" +
            "A1UdEwQCMAAwCwYDVR0PBAQDAgTwMA0GCSqGSIb3DQEBBQUAA4GBAK6idNRxyeBmqPoSKxq7Ck3e\n" +
            "j6R2QPyWbwZ+6/S7iCRt8PfgOu++Yu5YEjlUX1hlkbQKF/JuKTLqxNnKIE6Ef65+JP2ZaI9O2wdz\n" +
            "pRclAhAd00XbNKpyipr4jMdWmu2U8vyBBwn/utG1ZrLhAUiqnPvmaQrResiGHM2xzCmVwtse</X509Certificate>\n" +
            "   </X509Data>\n" +
            "  </KeyInfo>\n" +
            " </Signature>\n" +
            "</SignedServiceMetadata>";

    public static final String SERVICE_METADATA_URL_URN_GERMANY_NCPB = "/cipa-smp-full-webapp/ehealth-participantid-qns%3A%3Aurn%3Agermany%3Ancpb/services/epsos-docid-qns%3A%3Aurn%3A%3Aepsos%3Aservices%23%23epsos-21";
    public static final String SERVICE_METADATA_BODY_URN_GERMANY_NCPB = "<ServiceMetadata xmlns=\"http://docs.oasis-open.org/bdxr/ns/SMP/2014/07\" \n" +
            "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "    xmlns:ns=\"urn:esens:smp\">\n" +
            "        <ServiceInformation>\n" +
            "            <ParticipantIdentifier scheme=\"ehealth-participantid-qns\">urn:germany:ncpb</ParticipantIdentifier>\n" +
            "            <DocumentIdentifier scheme=\"epsos-docid-qns\">urn::epsos:services##epsos-21</DocumentIdentifier>\n" +
            "            <ProcessList>\n" +
            "                <Process>\n" +
            "                    <ProcessIdentifier scheme=\"ehealth-procid-qns\">urn:germany:ncpb:epsosPatientService::List</ProcessIdentifier>\n" +
            "                    <ServiceEndpointList>\n" +
            "                        <Endpoint transportProfile=\"urn:ihe:iti:2013:xcpd\">\n" +
            "                            <EndpointURI>http://germany/ncp/patient/list</EndpointURI>\n" +
            "                            <RequireBusinessLevelSignature>false</RequireBusinessLevelSignature>\n" +
            "                            <MinimumAuthenticationLevel>urn:epSOS:loa:1</MinimumAuthenticationLevel>\n" +
            "                            <ServiceActivationDate>2015-04-29T12:55:39Z</ServiceActivationDate>\n" +
            "                            <ServiceExpirationDate>2015-04-29T12:55:39Z</ServiceExpirationDate>\n" +
            "<Certificate>\n" +
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
            "</Certificate>\n" +
            "                            <ServiceDescription></ServiceDescription>\n" +
            "                            <TechnicalContactUrl>http://germany/contact</TechnicalContactUrl>\n" +
            "                            <TechnicalInformationUrl>http://germany/contact</TechnicalInformationUrl>\n" +
            "                        </Endpoint>\n" +
            "                    </ServiceEndpointList>\n" +
            "                </Process>\n" +
            "            </ProcessList>\n" +
            "        </ServiceInformation>\n" +
            "    </ServiceMetadata>";
}
