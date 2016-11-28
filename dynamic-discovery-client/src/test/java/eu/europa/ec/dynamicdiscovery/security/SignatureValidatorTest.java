package eu.europa.ec.dynamicdiscovery.security;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.core.security.impl.SignatureValidatorImpl;
import eu.europa.ec.dynamicdiscovery.exception.SignatureException;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.security.cert.X509Certificate;

public class SignatureValidatorTest {

    @Test
    public void verifyValidSignature() throws Exception {
        FetcherResponse fetcherResponse = new FetcherResponse(CommonUtil.getStreamFromXmlFile("signed_service_metadata_urn_poland_ncpb"), "http://docs.oasis-open.org/bdxr/ns/SMP/2016/05");
        ISignatureValidator signatureValidator = new SignatureValidatorImpl();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        Document document = documentBuilderFactory.newDocumentBuilder().parse(fetcherResponse.getInputStream());
        X509Certificate certificate = (X509Certificate) signatureValidator.verify(document);
        Assert.assertNotNull(certificate);
        Assert.assertEquals("CN=SMP Mock Services, OU=DIGIT, O=European Commision, C=BE",certificate.getSubjectDN().toString());
    }

    @Test(expected = SignatureException.class)
    public void verifyNotValidSignature() throws Exception {
        FetcherResponse fetcherResponse = new FetcherResponse(CommonUtil.getStreamFromXmlFile("signed_service_metadata_9915_123456789_invalid_signature"), "http://docs.oasis-open.org/bdxr/ns/SMP/2016/05");
        ISignatureValidator signatureValidator = new SignatureValidatorImpl();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        Document document = documentBuilderFactory.newDocumentBuilder().parse(fetcherResponse.getInputStream());
        X509Certificate certificate = (X509Certificate) signatureValidator.verify(document);
    }
}
