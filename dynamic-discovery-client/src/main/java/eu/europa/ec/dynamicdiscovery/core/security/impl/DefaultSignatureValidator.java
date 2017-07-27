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
package eu.europa.ec.dynamicdiscovery.core.security.impl;

import eu.europa.ec.dynamicdiscovery.core.security.AbstractSignatureValidator;
import eu.europa.ec.dynamicdiscovery.core.security.X509KeySelector;
import eu.europa.ec.dynamicdiscovery.exception.SignatureException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import org.apache.commons.lang3.StringUtils;
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
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Iterator;

public class DefaultSignatureValidator extends AbstractSignatureValidator {

    private String regexCertificateSubjectValidation;

    public DefaultSignatureValidator(KeyStore trustStore, String regexCertificateSubjectValidation) throws TechnicalException {
        super(trustStore);
        this.regexCertificateSubjectValidation = regexCertificateSubjectValidation;
    }

    public DefaultSignatureValidator(KeyStore trustStore) throws TechnicalException {
        this(trustStore, null);
    }

    @Override
    public X509Certificate verify(Document document) throws TechnicalException {
        X509Certificate certificate = verifySignature(document);
        verifyCertificate(certificate);
        verifyCertificateSubject(certificate);

        return certificate;
    }

    private X509Certificate verifySignature(Document document) throws TechnicalException {
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

    private void verifyCertificateSubject(X509Certificate signerCertificate) throws TechnicalException {
        if (!StringUtils.isEmpty(regexCertificateSubjectValidation)) {
            if (!signerCertificate.getSubjectX500Principal().toString().matches(regexCertificateSubjectValidation)) {
                throw new SignatureException("Certificate subject is not accepted according to the pattern.");
            }
        }
    }

    private void verifyCertificate(X509Certificate signerCertificate) throws TechnicalException {
        try {
            for (String alias : Collections.list(trustStore.aliases())) {

                //Checks if certificate is under the truststore and is trusted
                KeyStore.Entry entry = trustStore.getEntry(alias, null);
                if (!trustStore.entryInstanceOf(alias, KeyStore.TrustedCertificateEntry.class)) {
                    continue;
                }

                //Checks if certificate is X509Certificate type
                KeyStore.TrustedCertificateEntry certificateEntry =
                        (KeyStore.TrustedCertificateEntry) trustStore.getEntry(alias, null);
                Certificate trustedCertificate = certificateEntry.getTrustedCertificate();
                if (!(trustedCertificate instanceof X509Certificate)) {
                    continue;
                }

                // Verify trust
                if (signerCertificate.equals(trustedCertificate) || isSignedBy(signerCertificate, trustedCertificate)) {
                    return;
                }
            }

            throw new IllegalStateException("TrustStore does not contain Issuer CA.");

        } catch (Exception exc) {
            throw new SignatureException(exc.getMessage(), exc);
        }

    }

    private boolean isSignedBy(Certificate signed, Certificate signer) {
        try {
            signed.verify(signer.getPublicKey());
            return true;
        } catch (CertificateException | NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException | java.security.SignatureException e) {
            return false;
        }
    }
}
