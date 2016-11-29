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
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

public class SignatureValidatorTest {

    @Test
    public void verifyValidSignature() throws Exception {
        FetcherResponse fetcherResponse = new FetcherResponse(CommonUtil.getStreamFromXmlFile("signed_service_metadata_urn_poland_ncpb"), "http://docs.oasis-open.org/bdxr/ns/SMP/2016/05");
        ISignatureValidator signatureValidator = new DefaultSignatureValidator();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        Document document = documentBuilderFactory.newDocumentBuilder().parse(fetcherResponse.getInputStream());
        X509Certificate certificate = (X509Certificate) signatureValidator.verify(document);
        Assert.assertNotNull(certificate);
        Assert.assertEquals("CN=SMP Mock Services, OU=DIGIT, O=European Commision, C=BE", certificate.getSubjectDN().toString());
    }

    @Test(expected = SignatureException.class)
    public void verifyNotValidSignature() throws Exception {
        FetcherResponse fetcherResponse = new FetcherResponse(CommonUtil.getStreamFromXmlFile("signed_service_metadata_9915_123456789_invalid_signature"), "http://docs.oasis-open.org/bdxr/ns/SMP/2016/05");
        ISignatureValidator signatureValidator = new DefaultSignatureValidator();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        Document document = documentBuilderFactory.newDocumentBuilder().parse(fetcherResponse.getInputStream());
        Certificate certificate = signatureValidator.verify(document);
    }
}
