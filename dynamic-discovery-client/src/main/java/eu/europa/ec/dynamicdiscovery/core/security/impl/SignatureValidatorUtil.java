package eu.europa.ec.dynamicdiscovery.core.security.impl;

import eu.europa.ec.dynamicdiscovery.core.security.X509KeySelector;
import eu.europa.ec.dynamicdiscovery.exception.SignatureException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import java.security.cert.X509Certificate;
import java.util.Iterator;

/**
 * @author Flávio W. R. Santos
 * @author Cosmin Baciu
 */
public class SignatureValidatorUtil {

    private static final Logger LOG = LoggerFactory.getLogger(SignatureValidatorUtil.class);

    protected boolean signatureMandatory = false;

    public X509Certificate verifySignature(Document document) throws TechnicalException {
        try {
            X509KeySelector keySelector = new eu.europa.ec.dynamicdiscovery.core.security.X509KeySelector();
            XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
            NodeList nl = document.getDocumentElement().getChildNodes();
            if (nl.getLength() == 0) {
                throw new SignatureException("Unable to find child nodes on the element");
            }

            int size = nl.getLength();
            Element signatureElement = null;
            for (int i = 0; i < size; i++) {
                Node n = nl.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) n;
                    if (el.getLocalName().equals("Signature") && el.getNamespaceURI().equals("http://www.w3.org/2000/09/xmldsig#")) {
                        signatureElement = el;
                    }
                }
            }

            if (signatureElement == null) {
                if (signatureMandatory) {
                    throw new SignatureException("Unable to get the signature");
                }
                return null;

            }

            DOMValidateContext valContext = new DOMValidateContext(keySelector, signatureElement);
            valContext.setProperty("org.jcp.xml.dsig.secureValidation", Boolean.TRUE);
            valContext.setProperty("javax.xml.crypto.dsig.cacheReference", Boolean.TRUE);
            XMLSignature signature = fac.unmarshalXMLSignature(valContext);
            boolean coreValidity = signature.validate(valContext);

            if (!coreValidity) {
                boolean sv = signature.getSignatureValue().validate(valContext);
                if (!sv) {
                    logSignatureErrors(signature, valContext);
                }
                throw new SignatureException("Core Validity of the Signature is not valid.");
            }
            return keySelector.getCertificate();
        } catch (XMLSignatureException | MarshalException e) {
            throw new SignatureException(e.getMessage(), e);
        }
    }

    /**
     * Validate signature references and print invalid references to log.
     *
     * @param signature  XMLSignature signature
     * @param valContext signature context settings
     * @throws XMLSignatureException thrown when exceptional condition occurred during the XML signature validation process
     */
    protected void logSignatureErrors(XMLSignature signature, DOMValidateContext valContext) throws XMLSignatureException {
        // Check the validation status of each Reference.
        Iterator<Reference> i1 = signature.getSignedInfo().getReferences().iterator();
        while (i1.hasNext()) {
            Reference reference = i1.next();
            boolean refValid = reference.validate(valContext);
            if (!refValid) {
                LOG.error("Signature [{}] has invalid reference [{}]!", signature.getId(), reference.getId());
            }
        }
    }

    public boolean isSignatureMandatory() {
        return signatureMandatory;
    }

    public void setSignatureMandatory(boolean signatureMandatory) {
        this.signatureMandatory = signatureMandatory;
    }
}
