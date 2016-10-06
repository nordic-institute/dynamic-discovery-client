package eu.europa.ec.dynamicdiscovery.security;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
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
}
