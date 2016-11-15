/*
 * (C) Copyright 2016 Dynamic Discovery Client
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
 */
package eu.europa.ec.dynamicdiscovery.core.security;

import eu.europa.ec.dynamicdiscovery.exception.SignatureException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.crypto.*;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import java.security.Key;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Iterator;

public class XmldsigVerifier {

    public static X509Certificate verify(Document document) throws TechnicalException {
        try {
            eu.europa.ec.dynamicdiscovery.core.security.X509KeySelector keySelector = new eu.europa.ec.dynamicdiscovery.core.security.X509KeySelector();
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