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
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Iterator;

public class DefaultSignatureValidator implements ISignatureValidator {

    private KeyStore trustStore;

    public DefaultSignatureValidator() {
    }

    public DefaultSignatureValidator(KeyStore trustStore) throws TechnicalException {
        this.trustStore = trustStore;

        if (trustStore == null) {
            throw new SignatureException("TrustStore must be not null for signature validation.");
        }
    }

    @Override
    public Certificate verify(Document document) throws TechnicalException {
        Certificate certificate = verifySignature(document);
        if (trustStore != null) {
            verifyCertificate(trustStore, (X509Certificate) certificate);
        }

        return certificate;
    }

    private Certificate verifySignature(Document document) throws TechnicalException {
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
                throw new SignatureException("Unable to get the signature");
            }

            DOMValidateContext valContext = new DOMValidateContext(keySelector, signatureElement);
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

    private void verifyCertificate(KeyStore trustStore, X509Certificate signerCertificate) throws TechnicalException {
        try {
            Certificate certificateFound = null;
            for (String alias : Collections.list(trustStore.aliases())) {
                KeyStore.Entry entry = trustStore.getEntry(alias, null);
                if (!trustStore.entryInstanceOf(alias, KeyStore.TrustedCertificateEntry.class)) {
                    continue;
                }

                KeyStore.TrustedCertificateEntry certificateEntry =
                        (KeyStore.TrustedCertificateEntry) trustStore.getEntry(alias, null);
                Certificate certificateEmbedded = certificateEntry.getTrustedCertificate();
                if (!(certificateEmbedded instanceof X509Certificate)) {
                    continue;
                }

                if (!(signerCertificate).getIssuerDN().equals(((X509Certificate) certificateEmbedded).getSubjectDN())) {
                    continue;
                }
                if (certificateFound != null) {
                    throw new IllegalStateException("TrustStore has more than one issuing CA.");
                }
                certificateFound = certificateEmbedded;
            }
            if (certificateFound == null) {
                throw new IllegalStateException("TrustStore does not contain Issuer CA.");
            }
        } catch (Exception exc) {
            throw new SignatureException(exc.getMessage(), exc);
        }
    }
}
