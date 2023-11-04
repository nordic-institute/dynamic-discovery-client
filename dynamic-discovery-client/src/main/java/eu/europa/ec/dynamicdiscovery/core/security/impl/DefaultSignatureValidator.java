/*
 * (C) Copyright 2016-2021 - European Commission | Dynamic Discovery Client
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
 */
package eu.europa.ec.dynamicdiscovery.core.security.impl;

import eu.europa.ec.dynamicdiscovery.core.security.ISMPCertificateValidator;
import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
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
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;

/**
 * @author Flávio W. R. Santos
 */
public class DefaultSignatureValidator implements ISignatureValidator {
    static final Logger LOG = LoggerFactory.getLogger(DefaultSignatureValidator.class);

    ISMPCertificateValidator certificateValidator;

    public DefaultSignatureValidator(KeyStore trustStore) throws TechnicalException {
        this(trustStore, null);
    }

    public DefaultSignatureValidator(KeyStore trustStore, String regexCertificateSubjectValidation) throws TechnicalException {
        this(new DefaultSMPCertificateValidator(trustStore, regexCertificateSubjectValidation));
    }

    public DefaultSignatureValidator(ISMPCertificateValidator smpCertificateValidator) {
        this.certificateValidator = smpCertificateValidator;
    }

    public ISMPCertificateValidator getCertificateValidator() {
        return certificateValidator;
    }

    @Override
    public X509Certificate verify(Document document) throws TechnicalException {
        LOG.debug("Verifying signature");

        SignatureValidatorUtil signatureValidatorUtil = new SignatureValidatorUtil();
        X509Certificate certificate = signatureValidatorUtil.verifySignature(document);
        if (certificate == null){
            return null;
        }

        try {
            certificateValidator.validateSMPCertificate(certificate);
        } catch (CertificateException e) {
            throw new SignatureException(e.getMessage(), e);
        }
        return certificate;
    }




}
