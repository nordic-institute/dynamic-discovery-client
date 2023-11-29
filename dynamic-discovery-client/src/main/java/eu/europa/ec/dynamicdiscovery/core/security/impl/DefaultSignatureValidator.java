/*
 * Copyright 2017-2023 European Commission | eDelivery Dynamic Discovery Client
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence attached in file: LICENSE-EUPL-v1.2-EN.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
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
