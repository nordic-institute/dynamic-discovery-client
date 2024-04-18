/*
 * #%L
 * dynamic-discovery-cli
 * %%
 * Copyright (C) 2016 - 2023 European Commission | eDelivery | Dynamic Discovery Client
 * %%
 * Licensed under the LGPL, Version 2.1 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * [PROJECT_HOME]\license\lgpl2-1\license.txt or https://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package eu.europa.ec.dynamicdiscovery.core.security.impl;

import eu.europa.ec.dynamicdiscovery.core.security.X509KeySelector;
import eu.europa.ec.dynamicdiscovery.exception.SignatureException;
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
import javax.xml.namespace.QName;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Utility class for validating XML signatures. To detect the xml signature element, 
 * and validate the signature using the java JCP XML Signature API.
 * <p>  
 * @author Flávio W. R. Santos
 * @author Cosmin Baciu
 * @Author Joze Rihtarsic
 * @since 2.0
 */
public class SignatureValidatorUtil {
    private static final Logger LOG = LoggerFactory.getLogger(SignatureValidatorUtil.class);
    private static final QName XMLDSIG_NS = new QName("http://www.w3.org/2000/09/xmldsig#", "Signature");

    private static final String SIGNATURE_SECURE_VALIDATION_PROPERTY = "org.jcp.xml.dsig.secureValidation";
    private static final String SIGNATURE_CACHE_REFERENCE_PROPERTY = "javax.xml.crypto.dsig.cacheReference";
    private static final String XML_SIGNATURE_FACTORY = "DOM";

    protected boolean signatureMandatory = false;

    /**
     * Verify the signature of the document and return the certificate used to sign it.
     * The Certificate must be provided in the signature element.
     *
     * @param document the document to verify
     * @return the certificate used for signing the document
     * @throws SignatureException if the signature is mandatory and signature is not found,
     * if more than one signature is found or the signature is not valid or the
     * certificate is not trusted
     */
    public X509Certificate verifySignature(Document document) throws SignatureException {
        try {
            X509KeySelector keySelector = new eu.europa.ec.dynamicdiscovery.core.security.X509KeySelector();
            List<Element> signatureElements = getChildSignatureElement(document.getDocumentElement());

            if (signatureElements.isEmpty()) {
                if (signatureMandatory) {
                    throw new SignatureException("Unable to get the signature");
                }
                return null;
            }
            if (signatureElements.size() > 1) {
                throw new SignatureException("Only one signature is expected in the document");
            }
            Element signatureElement = signatureElements.get(0);

            DOMValidateContext valContext = new DOMValidateContext(keySelector, signatureElement);
            valContext.setProperty(SIGNATURE_SECURE_VALIDATION_PROPERTY, Boolean.TRUE);
            valContext.setProperty(SIGNATURE_CACHE_REFERENCE_PROPERTY, Boolean.TRUE);
            XMLSignatureFactory fac = XMLSignatureFactory.getInstance(XML_SIGNATURE_FACTORY);

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
     * Get the signature element from the document. The method searches for the first child element of the
     * root element, all other signature  elements are ignored, because the document can have other signatures.
     * which are not relevant for Dynamic Discover data validation!

     * @param signatureHolder the parent element where the signature is located
     * @return the signature element
     * @throws SignatureException if the signature element is not found
     */
    private static List<Element> getChildSignatureElement(Element signatureHolder) throws SignatureException {
        NodeList nl = signatureHolder.getChildNodes();
        if (nl.getLength() == 0) {
            throw new SignatureException("Unable to find child nodes on the element");
        }
        int size = nl.getLength();
        // get alle signature elements as child of the signatureHolder element
        return IntStream.range(0, size).mapToObj(nl::item)
                .filter(n -> n.getNodeType() == Node.ELEMENT_NODE)
                .map(n -> (Element) n)
                .filter(el -> XMLDSIG_NS.equals(new QName(el.getNamespaceURI(), el.getLocalName())))
                .collect(Collectors.toList());
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
