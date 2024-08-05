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

import eu.europa.ec.dynamicdiscovery.core.security.ISMPCertificateValidator;
import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.core.security.SignatureValidationContext;
import eu.europa.ec.dynamicdiscovery.exception.SignatureException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

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
    public X509Certificate verify(Document document, SignatureValidationContext context) throws SignatureException {
        LOG.debug("Verifying signature");

        SignatureValidatorUtil signatureValidatorUtil = new SignatureValidatorUtil();
        X509Certificate certificate = signatureValidatorUtil.verifySignature(document);
        if (certificate == null) {
            return null;
        }

        try {
            certificateValidator.validateSMPCertificate(certificate, context);
        } catch (CertificateException e) {
            throw new SignatureException(e.getMessage(), e);
        }
        return certificate;
    }


}
