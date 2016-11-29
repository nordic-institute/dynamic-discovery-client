package eu.europa.ec.dynamicdiscovery.core.security.impl;

import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.core.security.X509KeySelector;
import eu.europa.ec.dynamicdiscovery.exception.SignatureException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
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
import java.security.cert.Certificate;
import java.util.Iterator;

public class SignatureValidatorImpl implements ISignatureValidator {

    @Override
    public Certificate verify(Document document) throws TechnicalException {
        try {
            X509KeySelector keySelector = new eu.europa.ec.dynamicdiscovery.core.security.X509KeySelector();
            XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
            NodeList nl = document.getDocumentElement().getChildNodes();
            if (nl.getLength() == 0) {
                throw new SignatureException("Unable to find child nodes on the element");
            }

            int size = nl.getLength();
            Element signatureel = null;
            for (int i = 0; i < size; i++) {
                Node n = nl.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) n;
                    if (el.getLocalName().equals("Signature") && el.getNamespaceURI().equals("http://www.w3.org/2000/09/xmldsig#")) {
                        signatureel = el;
                    }
                }
            }

            if (signatureel == null) {
                throw new SignatureException("Unable to get the signature");
            }

            DOMValidateContext valContext = new DOMValidateContext(keySelector, signatureel);
            valContext.setProperty("javax.xml.crypto.dsig.cacheReference", Boolean.TRUE);
            XMLSignature signature = fac.unmarshalXMLSignature(valContext);
            boolean coreValidity = signature.validate(valContext);

            if (coreValidity == false) {
                boolean sv = signature.getSignatureValue().validate(valContext);
                if (sv == false) {
                    // Check the validation status of each Reference.
                    Iterator i1 = signature.getSignedInfo().getReferences().iterator();
                    for (int j = 0; i1.hasNext(); j++) {
                        boolean refValid = ((Reference) i1.next()).validate(valContext);
                    }
                }
                throw new SignatureException("Core Validity of the Signature is not valid.");
            }
            return keySelector.getCertificate();
        } catch (XMLSignatureException | MarshalException e) {
            throw new SignatureException(e.getMessage(), e);
        }
    }
}
