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
package eu.europa.ec.dynamicdiscovery.core.security;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.crypto.*;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import java.security.Key;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Iterator;

public class XmldsigVerifier {

    public static X509Certificate verify(Document document) throws Exception {
        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
        NodeList nl = document.getDocumentElement().getChildNodes();
        if (nl.getLength() == 0) {
            throw new Exception("Unable to find child nodes on the element");
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
            throw new Exception("Unable to get the signature");
        }

        eu.europa.ec.dynamicdiscovery.core.security.X509KeySelector keySelector = new eu.europa.ec.dynamicdiscovery.core.security.X509KeySelector();
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
        }

        return keySelector.getCertificate();
    }

    public class X509KeySelector extends KeySelector {
        public KeySelectorResult select(KeyInfo keyInfo, KeySelector.Purpose purpose, AlgorithmMethod method,
                                        XMLCryptoContext context) throws KeySelectorException {
            Iterator ki = keyInfo.getContent().iterator();
            while (ki.hasNext()) {
                XMLStructure info = (XMLStructure) ki.next();
                if (!(info instanceof X509Data))
                    continue;
                X509Data x509Data = (X509Data) info;
                Iterator xi = x509Data.getContent().iterator();
                while (xi.hasNext()) {
                    Object o = xi.next();
                    if (!(o instanceof X509Certificate))
                        continue;
                    final PublicKey key = ((X509Certificate) o).getPublicKey();
                    // Make sure the algorithm is compatible
                    // with the method.
                    if (algEquals(method.getAlgorithm(), key.getAlgorithm())) {
                        return new KeySelectorResult() {
                            public Key getKey() {
                                return key;
                            }
                        };
                    }
                }
            }
            throw new KeySelectorException("No key found!");
        }

        boolean algEquals(String algURI, String algName) {
            if ((algName.equalsIgnoreCase("DSA") && algURI.equalsIgnoreCase(SignatureMethod.DSA_SHA1))
                    || (algName.equalsIgnoreCase("RSA") && algURI.equalsIgnoreCase(SignatureMethod.RSA_SHA1))) {
                return true;
            } else {
                return false;
            }
        }
    }
}