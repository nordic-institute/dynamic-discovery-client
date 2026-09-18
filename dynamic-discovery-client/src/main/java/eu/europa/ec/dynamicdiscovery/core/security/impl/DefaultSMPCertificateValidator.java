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
import eu.europa.ec.dynamicdiscovery.core.security.SignatureValidationContext;
import eu.europa.ec.dynamicdiscovery.exception.SignatureException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DefaultSMPCertificateValidator implements ISMPCertificateValidator {

    static final Logger LOG = LoggerFactory.getLogger(DefaultSMPCertificateValidator.class);

    protected KeyStore trustStore;
    protected Pattern regexCertificateSubjectValidation;

    public DefaultSMPCertificateValidator(KeyStore trustStore, String regexCertificateSubjectValidation) throws TechnicalException {
        this.trustStore = trustStore;
        this.regexCertificateSubjectValidation = StringUtils.isBlank(regexCertificateSubjectValidation) ? null :
                Pattern.compile(regexCertificateSubjectValidation);
        if (this.trustStore == null) {
            throw new SignatureException("TrustStore must be not null for signature validation.");
        }
    }

    @Override
    public void validateSMPCertificate(X509Certificate certificate) throws CertificateException {
        validateSMPCertificate(certificate, null);
    }

    @Override
    public void validateSMPCertificate(X509Certificate certificate, SignatureValidationContext context) throws CertificateException {
        String certName = certificate.getSubjectX500Principal().getName();
        LOG.debug("Validate Certificate [{}].", certName);
        // check if certificate is valid
        certificate.checkValidity();
        //validate if certificate is trusted
        // if cvs is null, use default strategy TRUSTSTORE
        SignatureValidationContext.CertificateValidationStrategy cvs =
                Optional.ofNullable(context).map(SignatureValidationContext::getCertificateValidationStrategy)
                        .orElse(SignatureValidationContext.CertificateValidationStrategy.TRUSTSTORE);

        switch (cvs) {
            case TRUSTED_CERTIFICATES:
                validateCertificateWithTrustedList(certificate, context, certName);
                break;
            case TRUSTSTORE:
                verifyTrust(certificate);
                verifyCertificateSubject(certificate);
                break;
            case CERTIFICATE_SUBJECT_VALIDATION_AND_TRUSTSTORE:
                verifyTrust(certificate);
                validateCertificateSubjectMatch(certificate, context);
                break;
            default:
                throw new CertificateException("Unknown certificate validation strategy: " + context);
        }
        LOG.debug("Certificate % is valid and trusted [{}].", certName);
    }

    protected void validateCertificateSubjectMatch(X509Certificate certificate, SignatureValidationContext context) throws CertificateException {
        if (context == null) {
            throw new CertificateException("SignatureValidationContext is null!");
        }
        String certificateUUID = context.getCertificateUID();
        if (StringUtils.isBlank(certificateUUID)) {
            throw new CertificateException("Certificate UID is null!");
        }
        if (!certificate.getSubjectX500Principal().getName().equals(certificateUUID)) {
            throw new CertificateException("Certificate UID [" + certificateUUID + "] does not match the certificate subject ["
                    + certificate.getSubjectX500Principal().getName() + "]");
        }

        LOG.debug("Certificate subject UID [{}] matches the provided certificate subject",  certificateUUID );
    }

    protected void validateCertificateWithTrustedList(X509Certificate certificate, SignatureValidationContext context, String certName) throws CertificateException {
        if (context == null) {
            throw new CertificateException("SignatureValidationContext is null!");
        }
        List<X509Certificate> certificateList = context.getTrustedCertificates();
        if (certificateList == null || certificateList.isEmpty()) {
            throw new CertificateException("Trusted certificate list is empty!");
        }
        if (!certificateList.contains(certificate)) {
            throw new CertificateException("Provided certificate list does not contain trusted leaf certificate for ["
                    + certName + "]");
        }
    }

    /**
     * Validate certificate subject. Because of the legacy implementation certificate string representation
     * is extracted as
     * signerCertificate.getSubjectX500Principal().toString()
     *
     * @param signerCertificate
     * @throws CertificateException
     */
    protected void verifyCertificateSubject(X509Certificate signerCertificate) throws CertificateException {
        if (regexCertificateSubjectValidation != null) {
            String patternString = regexCertificateSubjectValidation.pattern();
            String subject = signerCertificate.getSubjectX500Principal().toString();
            Matcher matcher = regexCertificateSubjectValidation.matcher(subject);
            if (!matcher.matches()) {
                throw new CertificateException(String.format("Given certificate: [%s] does not match configured regex: [%s].",
                        subject, patternString));
            }
            LOG.debug("Given certificate: [{}] match the configured regex: [{}].", subject, patternString);
        } else {
            LOG.debug("Null regular expression for subject verification!");
        }
    }


    /**
     * Method verifies if certificate has trust anchor in truststore. Trust anchor is certificate itself
     * or direct issuer.
     *
     * @param signedCertificate
     * @throws CertificateException
     */
    protected void verifyTrust(X509Certificate signedCertificate) throws CertificateException {
        try {
            for (String signerCertificateAlias : Collections.list(trustStore.aliases())) {

                if (isAliasCertificateTrustAnchor(signedCertificate, signerCertificateAlias)) {
                    LOG.debug("Certificate with alias [{}] is trust anchor of the certificate [{}]!", signerCertificateAlias,
                            signedCertificate.getSubjectDN().getName());
                    return;
                }
            }
            throw new CertificateException("TrustStore does not contain trusted direct Issuer or the leaf certificate for [" + signedCertificate.getSubjectDN() + "]");
        } catch (RuntimeException | KeyStoreException exc) {
            throw new CertificateException("Could not verify trust for certificate [" + signedCertificate.getSubjectDN() + "]:" + exc.getMessage(), exc);
        }
    }

    /**
     * Method verifies if certificate with given alias is valid trust anchor for signer certificate. TrustAnchor is
     * the certificate itself or direct issuer!
     *
     * @param signedCertificate
     * @param signerCertificateAlias
     * @return true is certificate with given alias in truststore is valid trust achor
     * @throws CertificateException
     */
    protected boolean isAliasCertificateTrustAnchor(X509Certificate signedCertificate, String signerCertificateAlias) throws CertificateException {
        //Checks if certificate is under the truststore and is trusted
        String certName = signedCertificate.getSubjectX500Principal().getName();
        try {
            if (!trustStore.entryInstanceOf(signerCertificateAlias, KeyStore.TrustedCertificateEntry.class)) {
                LOG.warn("Certificate with alias [{}] is not Trusted certificate entry!", signerCertificateAlias);
                return false;
            }

            //Checks if certificate is X509Certificate type
            KeyStore.TrustedCertificateEntry signerCertificateEntry = (KeyStore.TrustedCertificateEntry) trustStore.getEntry(signerCertificateAlias, null);
            Certificate trustedCertificate = signerCertificateEntry.getTrustedCertificate();
            if (!(trustedCertificate instanceof X509Certificate)) {
                LOG.warn("Certificate with alias [{}] is not X509Certificate! Only X509Certificate type is supported!", signerCertificateAlias);
                return false;
            }

            X509Certificate x509TrustedCertificate = (X509Certificate) signerCertificateEntry.getTrustedCertificate();

            // Verify trust
            if (signedCertificate.equals(trustedCertificate)) {
                LOG.debug("Certificate with alias [{}] is direct trust anchor of the certificate [{}]!", signerCertificateAlias,
                        certName);
                return true;
            }

            if (isSignedBy(signedCertificate, x509TrustedCertificate, certName, signerCertificateAlias)) {
                LOG.debug("Certificate with alias [{}] is 'chain' trust anchor of the certificate [{}]!", signerCertificateAlias,
                        certName);
                return true;
            }
            // check if trusted certificate is still valid
            x509TrustedCertificate.checkValidity();
        } catch (NoSuchAlgorithmException | KeyStoreException |
                 UnrecoverableEntryException exc) {
            throw new CertificateException("Truststore exception occurred when  accessing certificate with alias:" + signerCertificateAlias
                    + ". Error message:" + exc.getMessage(), exc);
        }
        LOG.debug("Certificate with alias [{}] is not trust anchor of the certificate [{}]!", signerCertificateAlias,
                certName);
        return false;

    }

    private boolean isSignedBy(Certificate signed, Certificate signer, String signedCertificateName, String alias) throws CertificateException {
        try {
            signed.verify(signer.getPublicKey());
            LOG.debug("Certificate [{}] is signed by certificate with alias [{}] from truststore.", signedCertificateName, alias);
            return true;
        } catch (NoSuchAlgorithmException | InvalidKeyException |
                 NoSuchProviderException |
                 java.security.SignatureException e) {
            //in case there are multiple entries in the truststore, we don't want to log an error if the validation fails
            LOG.debug("Error occurred while verifying signature of the certificate [" + signedCertificateName
                    + "] with certificate from truststore with alias [" + alias + "].", e);
            return false;
        }
    }
}
