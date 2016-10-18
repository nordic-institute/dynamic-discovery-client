package eu.europa.ec.dynamicdiscovery.core.security;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.X509Certificate;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 * @author Erlend Klakegg Bergheim - erlend.klakegg.bergheim@difi.no
 */
public class XmldsigVerifier {

    public XmldsigVerifier() {
    }

    public static X509Certificate verify(Document document) throws SecurityException {
        try {
            NodeList e = document.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "Signature");
            if (e.getLength() == 0) {
                throw new SecurityException("Cannot find Signature element");
            } else {
                XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
                X509KeySelector keySelector = new X509KeySelector();
                DOMValidateContext valContext = new DOMValidateContext(keySelector, e.item(0));

                XMLSignature signature = fac.unmarshalXMLSignature(valContext);
                if (!signature.validate(valContext)) {
                    throw new SecurityException("Signature failed.");
                } else {
                    // logger.debug("Signature passed.");
                    return keySelector.getCertificate();
                }
            }
        } catch (SecurityException var6) {
            throw var6;
        } catch (Exception var7) {
            // logger.warn(var7.getMessage(), var7);
            throw new SecurityException("Unable to verify document signature.", var7);
        }
    }

    public static boolean verify(byte[] signedData) throws SAXException, IOException, ParserConfigurationException, MarshalException, XMLSignatureException {
        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

        // Instantiate the document to be signed.
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(signedData));

        // Find Signature element.
        NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
        boolean coreValidity = false;
        if (nl.getLength() == 0) {
            throw new SAXException("Cannot find Signature element");
        }

        // Create a DOMValidateContext and specify a KeySelector
        // and document context.

        //VerificationResult result = new VerificationResult();

        for (int i = 0; i < nl.getLength(); i++) {
            DOMValidateContext valContext = new DOMValidateContext(new X509KeySelector(), nl.item(i));

            // Unmarshal the XMLSignature.
            XMLSignature signature = fac.unmarshalXMLSignature(valContext);

            // Validate the XMLSignature.
            coreValidity = signature.validate(valContext);


            //  result.setValid(coreValidity);

            // Check core validation status.
            if (coreValidity == false) {
                boolean sv = signature.getSignatureValue().validate(valContext);
                //result.addError("signature validation status: " + sv);

                if (sv == false) {
                    // Check the validation status of each Reference.
                    for (Object o : signature.getSignedInfo().getReferences()) {
                        Reference r = (Reference) o;

                        boolean refValid = r.validate(valContext);
                        // result.addError("ref[" + r.getURI() + "] validity status: " + refValid);
                    }
                }

                break;
            }
        }

        return coreValidity;
    }
}
