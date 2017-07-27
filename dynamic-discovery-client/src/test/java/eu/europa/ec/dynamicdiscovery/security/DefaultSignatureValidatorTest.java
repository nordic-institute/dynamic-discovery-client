/*
 * (C) Copyright 2016 - European Commission | Dynamic Discovery Client
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
 *
 * @author Flávio W. R. Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 *
 */
package eu.europa.ec.dynamicdiscovery.security;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.core.security.impl.DefaultSignatureValidator;
import eu.europa.ec.dynamicdiscovery.exception.SignatureException;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

public class DefaultSignatureValidatorTest {

    @Test
    public void testVerifyValidSignature() throws Exception {
        KeyStore keyStore = CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts");
        ISignatureValidator signatureValidator = new DefaultSignatureValidator(keyStore);
        Document document = parseDocument("signed_service_metadata_urn_poland_ncpb");
        X509Certificate certificate = (X509Certificate) signatureValidator.verify(document);
        Assert.assertNotNull(certificate);
        Assert.assertEquals("CN=SMP Mock Services, OU=DIGIT, O=European Commision, C=BE", certificate.getSubjectDN().toString());
    }

    @Test(expected = SignatureException.class)
    public void testVerifyNotValidSignature() throws Exception {
        KeyStore keyStore = CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts");
        ISignatureValidator signatureValidator = new DefaultSignatureValidator(keyStore);
        Document document = parseDocument("signed_service_metadata_9915_123456789_invalid_signature");
        Certificate certificate = signatureValidator.verify(document);
    }

    @Test
    public void testVerifyValidSignerCertificate() throws Exception {
        KeyStore keyStore = CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts");
        ISignatureValidator signatureValidator = new DefaultSignatureValidator(keyStore);
        Document document = parseDocument("signed_service_metadata_urn_poland_ncpb");
        X509Certificate certificate = (X509Certificate) signatureValidator.verify(document);
        Assert.assertNotNull(certificate);
        Assert.assertEquals("CN=SMP Mock Services, OU=DIGIT, O=European Commision, C=BE", certificate.getSubjectDN().toString());
    }

    @Test
    public void testVerifyNotTrustedSignerCertificate() throws Exception {
        KeyStore keyStore = CommonUtil.loadTrustStore("truststore/truststoreForNotTrustedCertificate.ts");
        ISignatureValidator signatureValidator = new DefaultSignatureValidator(keyStore);
        Document document = parseDocument("signed_service_metadata_urn_poland_ncpb");
        try {
            X509Certificate certificate = (X509Certificate) signatureValidator.verify(document);
        } catch (SignatureException exc) {
            Assert.assertEquals("TrustStore does not contain Issuer CA.", exc.getMessage());
            Assert.assertTrue(exc instanceof SignatureException);
            return;
        }
        Assert.fail("Exception should have been thrown");
    }

    @Test
    public void testIsSignedByIntermediateCA() throws Exception {
        KeyStore trustStore = CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate-OnlyIntermediateCA.ts");
        Certificate certificate = CommonUtil.loadCertificate("certificate/eDelivery_SMP_TEST_1.cer");

        ISignatureValidator signatureValidator = new DefaultSignatureValidator(trustStore);
        ReflectionTestUtils.invokeSetterMethod(signatureValidator, "verifyCertificate", certificate);
    }

    @Test(expected = Exception.class)
    public void testIsSignedByRootCA() throws Exception {
        KeyStore trustStore = CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate-OnlyRootCA.ts");
        Certificate certificate = CommonUtil.loadCertificate("certificate/eDelivery_SMP_TEST_1.cer");

        ISignatureValidator signatureValidator = new DefaultSignatureValidator(trustStore);
        ReflectionTestUtils.invokeSetterMethod(signatureValidator, "verifyCertificate", certificate);
        Assert.fail("Exception should have been thrown");
    }

    @Test
    public void testIsSignedByRootAndIntermediateCA() throws Exception {
        KeyStore trustStore = CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate-RootAndIntermediateCA.ts");
        Certificate certificate = CommonUtil.loadCertificate("certificate/eDelivery_SMP_TEST_1.cer");

        ISignatureValidator signatureValidator = new DefaultSignatureValidator(trustStore);
        ReflectionTestUtils.invokeSetterMethod(signatureValidator, "verifyCertificate", certificate);
    }

    @Test
    public void testIsSignedByCertificateItself() throws Exception {
        KeyStore trustStore = CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate-CertificateItself.ts");
        Certificate certificate = CommonUtil.loadCertificate("certificate/eDelivery_SMP_TEST_1.cer");

        ISignatureValidator signatureValidator = new DefaultSignatureValidator(trustStore);
        ReflectionTestUtils.invokeSetterMethod(signatureValidator, "verifyCertificate", certificate);
    }

    @Test(expected = Exception.class)
    public void testIsSignedByDifferentAndNotOkCA() throws Exception {
        KeyStore trustStore = CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts");
        Certificate certificate = CommonUtil.loadCertificate("certificate/eDelivery_SMP_TEST_1.cer");

        ISignatureValidator signatureValidator = new DefaultSignatureValidator(trustStore);
        ReflectionTestUtils.invokeSetterMethod(signatureValidator, "verifyCertificate", certificate);
        Assert.fail("Exception should have been thrown");
    }

    private Document parseDocument(String fileName) throws Exception {
        FetcherResponse fetcherResponse = new FetcherResponse(CommonUtil.getStreamFromXmlFile(fileName));
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        return documentBuilderFactory.newDocumentBuilder().parse(fetcherResponse.getInputStream());
    }
}
