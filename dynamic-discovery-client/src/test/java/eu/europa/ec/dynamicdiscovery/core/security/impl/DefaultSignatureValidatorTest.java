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
package eu.europa.ec.dynamicdiscovery.core.security.impl;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.exception.SignatureException;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Flávio W. R. Santos
 */
class DefaultSignatureValidatorTest {

    @Test
    void testVerifyValidSignature() throws Exception {
        KeyStore keyStore = CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts");
        ISignatureValidator signatureValidator = new DefaultSignatureValidator(keyStore);
        Document document = parseDocument("signed_service_metadata_urn_poland_ncpb");
        X509Certificate certificate = (X509Certificate) signatureValidator.verify(document);
        assertNotNull(certificate);
        assertEquals("CN=SMP Mock Services, OU=DIGIT, O=European Commision, C=BE", certificate.getSubjectDN().toString());
    }

    @Test
    void testVerifyNotValidSignature() throws Exception {
        KeyStore keyStore = CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts");
        ISignatureValidator signatureValidator = new DefaultSignatureValidator(keyStore);
        Document document = parseDocument("signed_service_metadata_urn_poland_ncpb_invalid_signature");
        assertThrows(SignatureException.class, () -> signatureValidator.verify(document));
    }

    @Test
    void testVerifyValidSignerCertificate() throws Exception {
        KeyStore keyStore = CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts");
        ISignatureValidator signatureValidator = new DefaultSignatureValidator(keyStore);
        Document document = parseDocument("signed_service_metadata_urn_poland_ncpb");
        X509Certificate certificate = (X509Certificate) signatureValidator.verify(document);
        assertNotNull(certificate);
        assertEquals("CN=SMP Mock Services, OU=DIGIT, O=European Commision, C=BE", certificate.getSubjectDN().toString());
    }

    @Test
    void testVerifyNotTrustedSignerCertificate() throws Exception {
        KeyStore keyStore = CommonUtil.loadTrustStore("truststore/truststoreForNotTrustedCertificate.ts");
        ISignatureValidator signatureValidator = new DefaultSignatureValidator(keyStore);
        Document document = parseDocument("signed_service_metadata_urn_poland_ncpb");

        SignatureException result = assertThrows(SignatureException.class, () -> signatureValidator.verify(document));

        assertEquals("TrustStore does not contain trusted direct Issuer or the Certificate.", result.getMessage());
    }

    @Test
    void testIsSignedByIntermediateCA() throws Exception {
        testSignedBy("truststore/truststoreForTrustedCertificate-OnlyIntermediateCA.ts",
                "certificate/eDelivery_SMP_TEST_1.cer", null, "verifyTrust");
    }

    @Test
    void testIsSignedByRootCA() {
        assertThrows(Exception.class,
                () -> testSignedBy("truststore/truststoreForTrustedCertificate-OnlyRootCA.ts", "certificate/eDelivery_SMP_TEST_1.cer", null, "verifyCertificate"));
    }

    @Test
    void testIsSignedByRootAndIntermediateCA() throws Exception {
        testSignedBy("truststore/truststoreForTrustedCertificate-RootAndIntermediateCA.ts",
                "certificate/eDelivery_SMP_TEST_1.cer", null, "verifyTrust");
    }

    @Test
    void testIsSignedByCertificateItself() throws Exception {
        testSignedBy("truststore/truststoreForTrustedCertificate-CertificateItself.ts",
                "certificate/eDelivery_SMP_TEST_1.cer", null, "verifyTrust");

    }

    @Test
    void testIsSignedByDifferentAndNotOkCA() {
        assertThrows(Exception.class,
                () -> testSignedBy("truststore/truststoreForTrustedCertificate.ts", "certificate/eDelivery_SMP_TEST_1.cer", null, "verifyCertificate"));
    }

    @Test
    void testVerifyCertificateSubjectAcceptsAll() throws Exception {
        testSignedBy("truststore/truststoreForTrustedCertificate-CertificateItself.ts", "certificate/eDelivery_SMP_TEST_1.cer", "^.*$", "verifyCertificateSubject");
    }

    @Test
    void testVerifyCertificateSubjectAcceptsDefinedCNOnly() throws Exception {
        testSignedBy("truststore/truststoreForTrustedCertificate-CertificateItself.ts", "certificate/eDelivery_SMP_TEST_1.cer", "^CN=eDelivery_SMP_TEST_1.*$", "verifyCertificateSubject");
    }

    @Test
    void testVerifyCertificateSubjectAcceptsDefinedCNOnly1() throws Exception {
        testSignedBy("truststore/truststoreForTrustedCertificate-CertificateItself.ts", "certificate/eDelivery_SMP_TEST_1.cer", "^(CN=eDelivery_SMP_TEST_8|CN=eDelivery_SMP_TEST_1).*$", "verifyCertificateSubject");
    }

    @Test
    void testVerifyCertificateSubjectAcceptsDefinedCNOnly2() throws Exception {
        testSignedBy("truststore/truststoreForTrustedCertificate-CertificateItself.ts", "certificate/eDelivery_SMP_TEST_1.cer", "^CN=eDelivery_SMP_\\\\*.*$", "verifyCertificateSubject");
    }

    @Test
    void testVerifyCertificateSubjectAcceptsDefinedCNOnlyFail() {
        assertThrows(Exception.class, () ->
                testSignedBy("truststore/truststoreForTrustedCertificate-CertificateItself.ts", "certificate/eDelivery_SMP_TEST_1.cer", "^CN=eDelivery_SMP_TEST_8.*$", "verifyCertificateSubject"));
    }

    private void testSignedBy(String trustStorePath, String certificatePath, String regex, String methodName) throws Exception {
        KeyStore trustStore = CommonUtil.loadTrustStore(trustStorePath);
        Certificate certificate = CommonUtil.loadCertificate(certificatePath);

        DefaultSignatureValidator signatureValidator = new DefaultSignatureValidator(trustStore, regex);

        ReflectionTestUtils.invokeSetterMethod(signatureValidator.getCertificateValidator(), methodName, certificate);
    }


    private Document parseDocument(String fileName) throws Exception {
        FetcherResponse fetcherResponse = new FetcherResponse(CommonUtil.getInputStreamFromOasisSMP10XmlResource(fileName));
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        return documentBuilderFactory.newDocumentBuilder().parse(fetcherResponse.getInputStream());
    }
}
