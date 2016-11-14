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
package eu.europa.ec.dynamicdiscovery.util;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public class Constants {
    public static final String SMP_DOMAIN = "http://localhost:8090/cipa-smp-full-webapp/";
    public static final String SMP_DOMAIN_ALIAS = "http://smp.ec.europa.eu/";

    public static final String SERVICE_GROUP_URL_9925_0367302178 = "/cipa-smp-full-webapp/iso6523-actorid-upis%3A%3A9925%3A0367302178";
    public static final String SERVICE_GROUP_BODY_9925_0367302178 = "<ServiceGroup xmlns=\"http://docs.oasis-open.org/bdxr/ns/SMP/2016/05\">\n" +
            "<ParticipantIdentifier scheme=\"iso6523-actorid-upis\">9925:0367302178</ParticipantIdentifier>" +
            "<ServiceMetadataReferenceCollection>\n" +
            "<ServiceMetadataReference href=\"http://cipa-smp-full-webapp/iso6523-actorid-upis%3A%3A9915%3A0367302178/services/bdx-docid-qns%3A%3Aurn%3A%3Aepsos%3Aservices%23%23epsos-21\"/>\n" +
            "<ServiceMetadataReference href=\"http://cipa-smp-full-webapp/iso6523-actorid-upis%3A%3A9915%3A0367302178/services/bdx-docid-qns%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3ACreditNote-2%3A%3ACreditNote%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns014%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol5a%3Aver2.0%3A%3A2.1\"/>\n" +
            "</ServiceMetadataReferenceCollection>" +
            "</ServiceGroup>";

    public static final String SERVICE_GROUP_URL_URN_POLAND_NCPB = "/cipa-smp-full-webapp/ehealth-actorid-qns%3A%3Aurn%3Apoland:ncpb";
    public static final String SERVICE_GROUP_BODY_URN_POLAND_NCPB = "<ServiceGroup xmlns=\"http://docs.oasis-open.org/bdxr/ns/SMP/2016/05\">\n" +
            "<ParticipantIdentifier scheme=\"ehealth-actorid-qns\">urn:poland:ncpb</ParticipantIdentifier>" +
            "<ServiceMetadataReferenceCollection>\n" +
            "<ServiceMetadataReference href=\"http://cipa-smp-full-webapp/ehealth-actorid-qns::urn:poland:ncpb/services/epsos-docid-qns%3A%3Aurn%3A%3Aepsos%3Aservices%23%23epsos-21\"/>\n" +
            "<ServiceMetadataReference href=\"http://cipa-smp-full-webapp/ehealth-actorid-qns::urn:poland:ncpb/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A107\"/>\n" +
            "</ServiceMetadataReferenceCollection>" +
            "</ServiceGroup>";

    public static final String SERVICE_METADATA_URL_URN_POLAND_NCPB = "/cipa-smp-full-webapp/ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A107";
    public static final String SERVICE_METADATA_BODY_URN_POLAND_NCPB = "<ServiceMetadata xmlns=\"http://docs.oasis-open.org/bdxr/ns/SMP/2016/05\">"+
            "<ServiceInformation>" +
            "<ParticipantIdentifier scheme=\"ehealth-actorid-qns\">urn:poland:ncpb</ParticipantIdentifier>" +
            "<DocumentIdentifier scheme=\"ehealth-resid-qns\">urn::epsos##services:extended:epsos::107</DocumentIdentifier>" +
            "<ProcessList>" +
            "<Process>" +
            "<ProcessIdentifier scheme=\"ehealth-procid-qns\">urn:epsosPatientService::List</ProcessIdentifier>" +
            "<ServiceEndpointList>" +
            "<Endpoint transportProfile=\"urn:ihe:iti:2013:xcpd\">" +
            "<EndpointURI>http://poland/ncp/patient/list</EndpointURI>" +
            "<RequireBusinessLevelSignature>false</RequireBusinessLevelSignature>" +
            "<MinimumAuthenticationLevel>urn:epSOS:loa:1</MinimumAuthenticationLevel>" +
            "<ServiceActivationDate>2016-06-06T11:06:02.000+02:00</ServiceActivationDate>" +
            "<ServiceExpirationDate>2026-06-06T11:06:02+02:00</ServiceExpirationDate>" +
            "<Certificate>" +
            "MIID7jCCA1egAwIBAgICA+YwDQYJKoZIhvcNAQENBQAwOjELMAkGA1UEBhMCRlIxEzARBgNVBAoMCklIRSBFdXJvcGUxFjAUBgNVBAMMDUlIRSBFdXJvcGUgQ0EwHhcNMTYwNjAxMTQzNTUzWhcNMjYwNjAxMTQzNTUzWjCBgzELMAkGA1UEBhMCUFQxDDAKBgNVBAoMA01vSDENMAsGA1UECwwEU1BNUzENMAsGA1UEKgwESm9hbzEOMAwGA1UEBRMFQ3VuaGExHTAbBgNVBAMMFHFhZXBzb3MubWluLXNhdWRlLnB0MRkwFwYDVQQMDBBTZXJ2aWNlIFByb3ZpZGVyMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1eN4qPSSRZqjVFG9TlcPlxf2WiSimQK9L1nf9Z/s0ezeGQjCukDeDq/Wzqd9fpHhaMMq+XSSOtyEtIr5K/As4kFrViONUUkG12J6UllSWogp0NYFwA4wIqKSFiTnQS5/nRTs05oONCCGILCyJNNeO53JzPlaq3/QbPLssuSAr6XucPE8wBBGM8b/TsB2G/zjG8yuSTgGbhaZekq/Vnf9ftj1fr/vJDDAQgH6Yvzd88Z0DACJPHfW1p4F/OWLI386Bq7g/bo1DUPAyEwlf+CkLgJWRKki3yJlOCIZ9enMA5O7rfeG3rXdgYGmWS7tNEgKXxgC+heiYvi7ZWd7M+/SUwIDAQABo4IBMzCCAS8wPgYDVR0fBDcwNTAzoDGgL4YtaHR0cHM6Ly9nYXplbGxlLmloZS5uZXQvcGtpL2NybC82NDMvY2FjcmwuY3JsMDwGCWCGSAGG+EIBBAQvFi1odHRwczovL2dhemVsbGUuaWhlLm5ldC9wa2kvY3JsLzY0My9jYWNybC5jcmwwPAYJYIZIAYb4QgEDBC8WLWh0dHBzOi8vZ2F6ZWxsZS5paGUubmV0L3BraS9jcmwvNjQzL2NhY3JsLmNybDAfBgNVHSMEGDAWgBTsMw4TyCJeouFrr0N7el3Sd3MdfjAdBgNVHQ4EFgQU1GQ/K1ykIwWFgiONzWJLQzufF/8wDAYDVR0TAQH/BAIwADAOBgNVHQ8BAf8EBAMCBSAwEwYDVR0lBAwwCgYIKwYBBQUHAwEwDQYJKoZIhvcNAQENBQADgYEAZ7t1Qkr9wz3q6+WcF6p/YX7Jr0CzVe7w58FvJFk2AsHeYkSlOyO5hxNpQbs1L1v6JrcqziNFrh2QKGT2v6iPdWtdCT8HBLjmuvVWxxnfzYjdQ0J+kdKMAEV6EtWU78OqL60CCtUZKXE/NKJUq7TTUCFP2fwiARy/t1dTD2NZo8c=" +
            "</Certificate>" +
            "<ServiceDescription>" +
            "This is the epSOS Patient Service List for the Polish NCP" +
            "</ServiceDescription>" +
            "<TechnicalContactUrl>http://poland/contact</TechnicalContactUrl>" +
            "<TechnicalInformationUrl>http://poland/contact</TechnicalInformationUrl>" +
            "<Extension>" +
            "</Extension>" +
            "</Endpoint>" +
            "</ServiceEndpointList>" +
            "</Process>" +
            "</ProcessList>" +
            "</ServiceInformation>" +
            "</ServiceMetadata>";

    public static final String SIGNED_SERVICE_METADATA_URL_URN_POLAND_NCPB = "/cipa-smp-full-webapp/ehealth-actorid-qns::urn:poland:ncpb/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A107";
    public static final String SIGNED_SERVICE_METADATA_BODY_URN_POLAND_NCPB = "<SignedServiceMetadata xmlns=\"http://docs.oasis-open.org/bdxr/ns/SMP/2016/05\">\n" +
            "<ServiceMetadata>\n" +
            "<ServiceInformation>\n" +
            "<ParticipantIdentifier scheme=\"ehealth-actorid-qns\">urn:poland:ncpb</ParticipantIdentifier>\n" +
            "<DocumentIdentifier scheme=\"ehealth-resid-qns\">urn::epsos##services:extended:epsos::107</DocumentIdentifier>\n" +
            "<ProcessList>\n" +
            "<Process>\n" +
            "<ProcessIdentifier scheme=\"ehealth-procid-qns\">urn:epsosPatientService::List</ProcessIdentifier>\n" +
            "<ServiceEndpointList>\n" +
            "<Endpoint transportProfile=\"urn:ihe:iti:2013:xcpd\">\n" +
            "<EndpointURI>http://poland/ncp/patient/list</EndpointURI>\n" +
            "<RequireBusinessLevelSignature>false</RequireBusinessLevelSignature>\n" +
            "<MinimumAuthenticationLevel>urn:epSOS:loa:1</MinimumAuthenticationLevel>\n" +
            "<ServiceActivationDate>2016-06-06T11:06:02.000+02:00</ServiceActivationDate>\n" +
            "<ServiceExpirationDate>2026-06-06T11:06:02+02:00</ServiceExpirationDate>\n" +
            "<Certificate>\n" +
            "MIID7jCCA1egAwIBAgICA+YwDQYJKoZIhvcNAQENBQAwOjELMAkGA1UEBhMCRlIxEzARBgNVBAoMCklIRSBFdXJvcGUxFjAUBgNVBAMMDUlIRSBFdXJvcGUgQ0EwHhcNMTYwNjAxMTQzNTUzWhcNMjYwNjAxMTQzNTUzWjCBgzELMAkGA1UEBhMCUFQxDDAKBgNVBAoMA01vSDENMAsGA1UECwwEU1BNUzENMAsGA1UEKgwESm9hbzEOMAwGA1UEBRMFQ3VuaGExHTAbBgNVBAMMFHFhZXBzb3MubWluLXNhdWRlLnB0MRkwFwYDVQQMDBBTZXJ2aWNlIFByb3ZpZGVyMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1eN4qPSSRZqjVFG9TlcPlxf2WiSimQK9L1nf9Z/s0ezeGQjCukDeDq/Wzqd9fpHhaMMq+XSSOtyEtIr5K/As4kFrViONUUkG12J6UllSWogp0NYFwA4wIqKSFiTnQS5/nRTs05oONCCGILCyJNNeO53JzPlaq3/QbPLssuSAr6XucPE8wBBGM8b/TsB2G/zjG8yuSTgGbhaZekq/Vnf9ftj1fr/vJDDAQgH6Yvzd88Z0DACJPHfW1p4F/OWLI386Bq7g/bo1DUPAyEwlf+CkLgJWRKki3yJlOCIZ9enMA5O7rfeG3rXdgYGmWS7tNEgKXxgC+heiYvi7ZWd7M+/SUwIDAQABo4IBMzCCAS8wPgYDVR0fBDcwNTAzoDGgL4YtaHR0cHM6Ly9nYXplbGxlLmloZS5uZXQvcGtpL2NybC82NDMvY2FjcmwuY3JsMDwGCWCGSAGG+EIBBAQvFi1odHRwczovL2dhemVsbGUuaWhlLm5ldC9wa2kvY3JsLzY0My9jYWNybC5jcmwwPAYJYIZIAYb4QgEDBC8WLWh0dHBzOi8vZ2F6ZWxsZS5paGUubmV0L3BraS9jcmwvNjQzL2NhY3JsLmNybDAfBgNVHSMEGDAWgBTsMw4TyCJeouFrr0N7el3Sd3MdfjAdBgNVHQ4EFgQU1GQ/K1ykIwWFgiONzWJLQzufF/8wDAYDVR0TAQH/BAIwADAOBgNVHQ8BAf8EBAMCBSAwEwYDVR0lBAwwCgYIKwYBBQUHAwEwDQYJKoZIhvcNAQENBQADgYEAZ7t1Qkr9wz3q6+WcF6p/YX7Jr0CzVe7w58FvJFk2AsHeYkSlOyO5hxNpQbs1L1v6JrcqziNFrh2QKGT2v6iPdWtdCT8HBLjmuvVWxxnfzYjdQ0J+kdKMAEV6EtWU78OqL60CCtUZKXE/NKJUq7TTUCFP2fwiARy/t1dTD2NZo8c=\n" +
            "</Certificate>\n" +
            "<ServiceDescription>\n" +
            "This is the epSOS Patient Service List for the Polish NCP\n" +
            "</ServiceDescription>\n" +
            "<TechnicalContactUrl>http://poland/contact</TechnicalContactUrl>\n" +
            "<TechnicalInformationUrl>http://poland/contact</TechnicalInformationUrl>\n" +
            "<Extension>\n" +
            "<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
            "<SignedInfo>\n" +
            "<CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/>\n" +
            "<SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/>\n" +
            "<Reference URI=\"\">\n" +
            "<Transforms>\n" +
            "<Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/>\n" +
            "</Transforms>\n" +
            "<DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\"/>\n" +
            "<DigestValue>ey+nljKuyPHWTXkFBLB7HNdS0hPM9d4t5HTFQT2TlBQ=</DigestValue>\n" +
            "</Reference>\n" +
            "</SignedInfo>\n" +
            "<SignatureValue>\n" +
            "KnFuvpIutfkod+dF7KtAv6U7136MWCfqb6Z+LXorgQeA4xSmHP8Ic/V2o+w9ZmF3Ak/ImXVb3v24 JYaYmrCy3q2uEVKZ0IdFnsri5/ykEMUDI9aR3tkefcWKRK/FCb2+0jfqHMq11lii+CBBlwuTvoXE KmTJj7fZb/0DCUAezmg=\n" +
            "</SignatureValue>\n" +
            "<KeyInfo>\n" +
            "<X509Data>\n" +
            "<X509SubjectName>CN=Sample National Infrastructure,OU=Sante,C=PT</X509SubjectName>\n" +
            "<X509Certificate>\n" +
            "MIICAzCCAWygAwIBAgIEWCRzHjANBgkqhkiG9w0BAQsFADBGMQswCQYDVQQGEwJQVDEOMAwGA1UE CwwFU2FudGUxJzAlBgNVBAMMHlNhbXBsZSBOYXRpb25hbCBJbmZyYXN0cnVjdHVyZTAeFw0xNjEx MTAxMzE2NTBaFw0yNjExMTAxMzE2NTBaMEYxCzAJBgNVBAYTAlBUMQ4wDAYDVQQLDAVTYW50ZTEn MCUGA1UEAwweU2FtcGxlIE5hdGlvbmFsIEluZnJhc3RydWN0dXJlMIGfMA0GCSqGSIb3DQEBAQUA A4GNADCBiQKBgQCywt50WXEWIiWytRGcMqzeMM/EyxruNthPdiUEUTbs9un7lzGGjpfFMTgd83wJ haB6FgpaVd8V2w/JBdkim5Ltuhu2vA0d6hHOsa58neIfe4z1ZhswwNmB0+mDTjwnd/gg8IJyQhhY c5G4x7m0ZGdDKZDizjtDTEPTsl8D4FzBFwIDAQABMA0GCSqGSIb3DQEBCwUAA4GBACKxUpAx0PYm ZZi4DfAzBkQ0+CvQw/l6Yo8wonVdpcQXO3khpWIcXhgYhTLHwm8IwJLEyFatmMyCKklSA3CLebJU L4XH1GcdCg6oPKPUc+ovbgN7/iR265Elp4qHfpVteBijBTyZReH4oAK9hRhK1gLwtjI7vpjVaPXv vkV1fbrz\n" +
            "</X509Certificate>\n" +
            "</X509Data>\n" +
            "</KeyInfo>\n" +
            "</Signature>\n" +
            "</Extension>\n" +
            "</Endpoint>\n" +
            "</ServiceEndpointList>\n" +
            "</Process>\n" +
            "</ProcessList>\n" +
            "</ServiceInformation>\n" +
            "</ServiceMetadata>\n" +
            "<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
            "<SignedInfo>\n" +
            "<CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/>\n" +
            "<SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/>\n" +
            "<Reference URI=\"\">\n" +
            "<Transforms>\n" +
            "<Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/>\n" +
            "</Transforms>\n" +
            "<DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\"/>\n" +
            "<DigestValue>EchLfvR6YNZUIJJrblCj6dYdfKGe2xvL4Iz3FD6qfn0=</DigestValue>\n" +
            "</Reference>\n" +
            "</SignedInfo>\n" +
            "<SignatureValue>\n" +
            "Zhs0P/K7QGOQjV2KUSn4Vq+dwcgRUqFCGKtBQ/vbsV/hNuXBPjq+qOa2cCk2G3OeC/P5bmOV/HQL 1j+Hvuqx6B/0HdnDqBufVOcL9ina1+hdJ7wZzjH7oqaSlwBh7lE9WwSnv8uc4XAcHUUPvc2Dze7W btQzMht7ePi7YAM+7Y0=\n" +
            "</SignatureValue>\n" +
            "<KeyInfo>\n" +
            "<X509Data>\n" +
            "<X509SubjectName>\n" +
            "CN=SMP Mock Services,OU=DIGIT,O=European Commision,C=BE\n" +
            "</X509SubjectName>\n" +
            "<X509Certificate>\n" +
            "MIICIzCCAYygAwIBAgIEWCRzfjANBgkqhkiG9w0BAQsFADBWMQswCQYDVQQGEwJCRTEbMBkGA1UE CgwSRXVyb3BlYW4gQ29tbWlzaW9uMQ4wDAYDVQQLDAVESUdJVDEaMBgGA1UEAwwRU01QIE1vY2sg U2VydmljZXMwHhcNMTYxMTEwMTMxODE4WhcNMjYxMTEwMTMxODE4WjBWMQswCQYDVQQGEwJCRTEb MBkGA1UECgwSRXVyb3BlYW4gQ29tbWlzaW9uMQ4wDAYDVQQLDAVESUdJVDEaMBgGA1UEAwwRU01Q IE1vY2sgU2VydmljZXMwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBALrpN2GGqctPTP27g+zA DCmQxdOZgDQg5AeF/N5w0knZYy1GnqvAoXgLGHeS1l+2DKx4/E6SlcU6SLIGhVtpF+Gitdp+3to2 6FfV5qcCy4XKz1xm19r84ykXPWD835DbGB7o1HSlKx4+GmAr5eL2VH/zgINcJojam3gimvedoNWj AgMBAAEwDQYJKoZIhvcNAQELBQADgYEAXoh7T9eYOdjasnzPfsTeQ1ptEorj4pIZMRFjn2BWl+mZ K4XRn2+doLjN2dHremGyeKBgLb0Ulp9E9I5P8kxuIs7TjroxZofK9ixhfBv5rJhLcHy8XdrUYqAS awc3c5bM9fNxRWCMkNYNoSYVxPBdlS4zEeLNNzRY+wjrMNYIJR4=\n" +
            "</X509Certificate>\n" +
            "</X509Data>\n" +
            "</KeyInfo>\n" +
            "</Signature>\n" +
            "</SignedServiceMetadata>";

    public static final String SERVICE_METADATA_URL_9915_123456789 = "/cipa-smp-full-webapp/iso6523-actorid-upis%3A%3A9915%3A123456789/services/bdxr-docid-qns%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3ACreditNote-2%3A%3ACreditNote%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns014%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol5a%3Aver2.0%3A%3A2.1";
    public static final String SERVICE_METADATA_BODY_9915_123456789 = "<ServiceMetadata>\n" +
            "\t\t<ServiceInformation>\n" +
            "\t\t\t<ParticipantIdentifier scheme=\"iso6523-actorid-upis\">9915:123456789</ParticipantIdentifier>\n" +
            "\t\t\t<DocumentIdentifier scheme=\"bdxr-docid-qns\">urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1</DocumentIdentifier>\n" +
            "\t\t\t<ProcessList>\n" +
            "\t\t\t\t<Process>\n" +
            "\t\t\t\t\t<ProcessIdentifier scheme=\"cenbii-procid-ubl\">urn:www.cenbii.eu:profile:bii05:ver2.0</ProcessIdentifier>\n" +
            "\t\t\t\t\t<ServiceEndpointList>\n" +
            "\t\t\t\t\t\t<Endpoint transportProfile=\"bdxr-transport-ebms3-as4-v1p0\">\n" +
            "\t\t\t\t\t\t\t<EndpointURI>https://test.erechnung.gv.at/as4/msh/</EndpointURI>\n" +
            "\t\t\t\t\t\t\t<RequireBusinessLevelSignature>false</RequireBusinessLevelSignature>\t\t\t\t\t\t\t\n" +
            "\t\t\t\t\t\t\t<Certificate>\n" +
            "MIIEUjCCAzqgAwIBAgIQP9HNsiz9c3LG08fQy1VibDANBgkqhkiG9w0BAQsFADBX\n" +
            " MQswCQYDVQQGEwJESzEnMCUGA1UEChMeTkFUSU9OQUwgSVQgQU5EIFRFTEVDT00g\n" +
            " QUdFTkNZMR8wHQYDVQQDExZQRVBQT0wgQUNDRVNTIFBPSU5UIENBMB4XDTE1MDMw\n" +
            " NjAwMDAwMFoXDTE3MDMwNTIzNTk1OVowVzELMAkGA1UEBhMCQVQxFzAVBgNVBAMM\n" +
            " DkFQUF8xMDAwMDAwMTAxMS8wLQYDVQQKDCZCUlogKEZlZGVyYWwgQ29tcHV0aW5n\n" +
            " IENlbnRlciBBdXN0cmlhKTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEB\n" +
            " ALyMijnrnbXnZIJVR3VRQUBrLhdsvrCotuPw4V5WD5q/OSCZvI0nT3jESnzM+/q7\n" +
            " s8ElKXQv+dG4C2Qcr+7YYHXJD4dch67x7Advn65XM0Xk0ijUqKEFBJ7Jqei2Dw+y\n" +
            " cLwG3mYkxJFb721Nx04YYjMqGCzCC5/pLcPUyUJ/tjAx5ApEUimskI0PDpYY9fl/\n" +
            " vbn2JKa2VWt4L1MfnX86Gj5kNnkQ54qbRnxFlIm1EtlZCs41r3MocePk8mPkjzo/\n" +
            " M2QokJ6ACD8sZi4I4DH5Vux2cPE4zDjevmP4irvkfWuWdl1WCzD5/03UFHOsTXy2\n" +
            " MxYSr7+CETEPWlorL8cfeyUCAwEAAaOCARgwggEUMAkGA1UdEwQCMAAwCwYDVR0P\n" +
            " BAQDAgO4MGwGA1UdHwRlMGMwYaBfoF2GW2h0dHA6Ly9vbnNpdGVjcmwudmVyaXNp\n" +
            " Z24uY29tL0RpZ2l0YWxpc2VyaW5nc3N0eXJlbHNlbk9wZW5QRVBQT0xBQ0NFU1NQ\n" +
            " T0lOVENBL0xhdGVzdENSTC5jcmwwHwYDVR0jBBgwFoAUTfY+AFAohm01oPzvZqr6\n" +
            " IqEk240wHQYDVR0OBBYEFG3rnp87CACLoiMibj9s+7O4TrkKMDcGCCsGAQUFBwEB\n" +
            " BCswKTAnBggrBgEFBQcwAYYbaHR0cDovL3BraS1vY3NwLnN5bWF1dGguY29tMBMG\n" +
            " A1UdJQQMMAoGCCsGAQUFBwMCMA0GCSqGSIb3DQEBCwUAA4IBAQAYZgH8O69+BAie\n" +
            " KXNvL83Vg/v2BiBkoWruVT3sGPpzUS1hQ/vTEt9FxphPhi2Ofz/TGXeSMUffnGXT\n" +
            " 6Tntxcn5zsPuo7Km8o3EinXDHqXRHpoDo2nu2giOzHY5wFn2sI8lBnx4S8qAkljd\n" +
            " AIE2XLQIloBhCVCR3V3pWEKPvN1LUHYcCDvNQn0UvG6jtuIflvLevaJdg2DV5to9\n" +
            " RqS6UP6WGHvK9K+AmAp4snzQrgPBEutXYrKwojEDH/0k+30MTh8n8+V7YTKKqTeE\n" +
            " P7EtqZBwWyGZ48sLgWIvF7cmByacIzV5fF/OQZ4bRjc8ySvu+b0vPeUV0Ris++E9\n" +
            " Ab0lez1N\n" +
            "</Certificate>\n" +
            "\t\t\t\t\t\t\t<ServiceDescription>BRZ Test AP</ServiceDescription>\n" +
            "\t\t\t\t\t\t\t<TechnicalContactUrl>peppol-support@peppol.at</TechnicalContactUrl>\n" +
            "\t\t\t\t\t\t\t<TechnicalInformationUrl>http://www.peppol.at</TechnicalInformationUrl>\t\t\t\t\t\t\t\n" +
            "\t\t\t\t\t\t</Endpoint>\n" +
            "\t\t\t\t\t</ServiceEndpointList>\n" +
            "\t\t\t\t</Process>\n" +
            "\t\t\t</ProcessList>\n" +
            "\t\t</ServiceInformation>\n" +
            "\t</ServiceMetadata>";

    public static final String SIGNED_SERVICE_METADATA_URL_9915_123456789 = "/cipa-smp-full-webapp/iso6523-actorid-upis%3A%3A9915%3A123456789/services/bdxr-docid-qns%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3ACreditNote-2%3A%3ACreditNote%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns014%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol5a%3Aver2.0%3A%3A2.1";
    public static final String SIGNED_SERVICE_METADATA_BODY_9915_123456789 = "<SignedServiceMetadata xmlns=\"http://docs.oasis-open.org/bdxr/ns/SMP/2016/05\">\n" +
            "\t<ServiceMetadata>\n" +
            "\t\t<ServiceInformation>\n" +
            "\t\t\t<ParticipantIdentifier scheme=\"iso6523-actorid-upis\">9915:123456789</ParticipantIdentifier>\n" +
            "\t\t\t<DocumentIdentifier scheme=\"busdox-docid-qns\">urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1</DocumentIdentifier>\n" +
            "\t\t\t<ProcessList>\n" +
            "\t\t\t\t<Process>\n" +
            "\t\t\t\t\t<ProcessIdentifier scheme=\"cenbii-procid-ubl\">urn:www.cenbii.eu:profile:bii05:ver2.0</ProcessIdentifier>\n" +
            "\t\t\t\t\t<ServiceEndpointList>\n" +
            "\t\t\t\t\t\t<Endpoint transportProfile=\"bdxr-transport-ebms3-as4-v1p0\">\n" +
            "\t\t\t\t\t\t\t<EndpointURI>https://test.erechnung.gv.at/as4/msh/</EndpointURI>\n" +
            "\t\t\t\t\t\t\t<RequireBusinessLevelSignature>false</RequireBusinessLevelSignature>\t\t\t\t\t\t\t\n" +
            "\t\t\t\t\t\t\t<Certificate>\n" +
            "MIIEUjCCAzqgAwIBAgIQP9HNsiz9c3LG08fQy1VibDANBgkqhkiG9w0BAQsFADBX\n" +
            " MQswCQYDVQQGEwJESzEnMCUGA1UEChMeTkFUSU9OQUwgSVQgQU5EIFRFTEVDT00g\n" +
            " QUdFTkNZMR8wHQYDVQQDExZQRVBQT0wgQUNDRVNTIFBPSU5UIENBMB4XDTE1MDMw\n" +
            " NjAwMDAwMFoXDTE3MDMwNTIzNTk1OVowVzELMAkGA1UEBhMCQVQxFzAVBgNVBAMM\n" +
            " DkFQUF8xMDAwMDAwMTAxMS8wLQYDVQQKDCZCUlogKEZlZGVyYWwgQ29tcHV0aW5n\n" +
            " IENlbnRlciBBdXN0cmlhKTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEB\n" +
            " ALyMijnrnbXnZIJVR3VRQUBrLhdsvrCotuPw4V5WD5q/OSCZvI0nT3jESnzM+/q7\n" +
            " s8ElKXQv+dG4C2Qcr+7YYHXJD4dch67x7Advn65XM0Xk0ijUqKEFBJ7Jqei2Dw+y\n" +
            " cLwG3mYkxJFb721Nx04YYjMqGCzCC5/pLcPUyUJ/tjAx5ApEUimskI0PDpYY9fl/\n" +
            " vbn2JKa2VWt4L1MfnX86Gj5kNnkQ54qbRnxFlIm1EtlZCs41r3MocePk8mPkjzo/\n" +
            " M2QokJ6ACD8sZi4I4DH5Vux2cPE4zDjevmP4irvkfWuWdl1WCzD5/03UFHOsTXy2\n" +
            " MxYSr7+CETEPWlorL8cfeyUCAwEAAaOCARgwggEUMAkGA1UdEwQCMAAwCwYDVR0P\n" +
            " BAQDAgO4MGwGA1UdHwRlMGMwYaBfoF2GW2h0dHA6Ly9vbnNpdGVjcmwudmVyaXNp\n" +
            " Z24uY29tL0RpZ2l0YWxpc2VyaW5nc3N0eXJlbHNlbk9wZW5QRVBQT0xBQ0NFU1NQ\n" +
            " T0lOVENBL0xhdGVzdENSTC5jcmwwHwYDVR0jBBgwFoAUTfY+AFAohm01oPzvZqr6\n" +
            " IqEk240wHQYDVR0OBBYEFG3rnp87CACLoiMibj9s+7O4TrkKMDcGCCsGAQUFBwEB\n" +
            " BCswKTAnBggrBgEFBQcwAYYbaHR0cDovL3BraS1vY3NwLnN5bWF1dGguY29tMBMG\n" +
            " A1UdJQQMMAoGCCsGAQUFBwMCMA0GCSqGSIb3DQEBCwUAA4IBAQAYZgH8O69+BAie\n" +
            " KXNvL83Vg/v2BiBkoWruVT3sGPpzUS1hQ/vTEt9FxphPhi2Ofz/TGXeSMUffnGXT\n" +
            " 6Tntxcn5zsPuo7Km8o3EinXDHqXRHpoDo2nu2giOzHY5wFn2sI8lBnx4S8qAkljd\n" +
            " AIE2XLQIloBhCVCR3V3pWEKPvN1LUHYcCDvNQn0UvG6jtuIflvLevaJdg2DV5to9\n" +
            " RqS6UP6WGHvK9K+AmAp4snzQrgPBEutXYrKwojEDH/0k+30MTh8n8+V7YTKKqTeE\n" +
            " P7EtqZBwWyGZ48sLgWIvF7cmByacIzV5fF/OQZ4bRjc8ySvu+b0vPeUV0Ris++E9\n" +
            " Ab0lez1N\n" +
            "</Certificate>\n" +
            "\t\t\t\t\t\t\t<ServiceDescription>BRZ Test AP</ServiceDescription>\n" +
            "\t\t\t\t\t\t\t<TechnicalContactUrl>peppol-support@peppol.at</TechnicalContactUrl>\n" +
            "\t\t\t\t\t\t\t<TechnicalInformationUrl>http://www.peppol.at</TechnicalInformationUrl>\t\t\t\t\t\t\t\n" +
            "\t\t\t\t\t\t</Endpoint>\n" +
            "\t\t\t\t\t</ServiceEndpointList>\n" +
            "\t\t\t\t</Process>\n" +
            "\t\t\t</ProcessList>\n" +
            "\t\t</ServiceInformation>\n" +
            "\t</ServiceMetadata>\n" +
            "\t<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
            "\t\t<SignedInfo>\n" +
            "\t\t\t<CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/>\n" +
            "\t\t\t<SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/>\n" +
            "\t\t\t<Reference URI=\"\">\n" +
            "\t\t\t\t<Transforms>\n" +
            "\t\t\t\t\t<Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/>\n" +
            "\t\t\t\t</Transforms>\n" +
            "\t\t\t\t<DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\"/>\n" +
            "\t\t\t\t<DigestValue>EchLfvR6YNZUIJJrblCj6dYdfKGe2xvL4Iz3FD6qfn0=</DigestValue>\n" +
            "\t\t\t</Reference>\n" +
            "\t\t</SignedInfo>\n" +
            "\t\t<SignatureValue>\n" +
            "Zhs0P/K7QGOQjV2KUSn4Vq+dwcgRUqFCGKtBQ/vbsV/hNuXBPjq+qOa2cCk2G3OeC/P5bmOV/HQL 1j+Hvuqx6B/0HdnDqBufVOcL9ina1+hdJ7wZzjH7oqaSlwBh7lE9WwSnv8uc4XAcHUUPvc2Dze7W btQzMht7ePi7YAM+7Y0=\n" +
            "\t\t</SignatureValue>\n" +
            "\t\t<KeyInfo>\n" +
            "\t\t\t<X509Data>\n" +
            "\t\t\t\t<X509SubjectName>\n" +
            "CN=SMP Mock Services,OU=DIGIT,O=European Commision,C=BE\n" +
            "</X509SubjectName>\n" +
            "<X509Certificate>\n" +
            "MIICIzCCAYygAwIBAgIEWCRzfjANBgkqhkiG9w0BAQsFADBWMQswCQYDVQQGEwJCRTEbMBkGA1UE CgwSRXVyb3BlYW4gQ29tbWlzaW9uMQ4wDAYDVQQLDAVESUdJVDEaMBgGA1UEAwwRU01QIE1vY2sg U2VydmljZXMwHhcNMTYxMTEwMTMxODE4WhcNMjYxMTEwMTMxODE4WjBWMQswCQYDVQQGEwJCRTEb MBkGA1UECgwSRXVyb3BlYW4gQ29tbWlzaW9uMQ4wDAYDVQQLDAVESUdJVDEaMBgGA1UEAwwRU01Q IE1vY2sgU2VydmljZXMwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBALrpN2GGqctPTP27g+zA DCmQxdOZgDQg5AeF/N5w0knZYy1GnqvAoXgLGHeS1l+2DKx4/E6SlcU6SLIGhVtpF+Gitdp+3to2 6FfV5qcCy4XKz1xm19r84ykXPWD835DbGB7o1HSlKx4+GmAr5eL2VH/zgINcJojam3gimvedoNWj AgMBAAEwDQYJKoZIhvcNAQELBQADgYEAXoh7T9eYOdjasnzPfsTeQ1ptEorj4pIZMRFjn2BWl+mZ K4XRn2+doLjN2dHremGyeKBgLb0Ulp9E9I5P8kxuIs7TjroxZofK9ixhfBv5rJhLcHy8XdrUYqAS awc3c5bM9fNxRWCMkNYNoSYVxPBdlS4zEeLNNzRY+wjrMNYIJR4=\n" +
            "</X509Certificate>\n" +
            "\t\t\t</X509Data>\n" +
            "\t\t</KeyInfo>\n" +
            "\t</Signature>\n" +
            "</SignedServiceMetadata>";
}
