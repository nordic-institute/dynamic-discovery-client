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
        String certName = certificate.getSubjectX500Principal().getName();
        LOG.debug("Validate Certificate [{}].", certName);
        // check if certificate is valid
        certificate.checkValidity();
        //validate if certificate is trusted
        verifyTrust(certificate);
        verifyCertificateSubject(certificate);
        LOG.debug("Certificate % is valid and trusted [{}].", certName);
    }

    /**
     * Validate certificate subject. Because of the legacy implementation certificate string representation
     * is extracted as
     * signerCertificate.getSubjectX500Principal().toString()
     *
     * @param signerCertificate
     * @throws CertificateException
     */
    private void verifyCertificateSubject(X509Certificate signerCertificate) throws CertificateException {
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
    private void verifyTrust(X509Certificate signedCertificate) throws CertificateException {
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
        } catch (NoSuchAlgorithmException | KeyStoreException | UnrecoverableEntryException exc) {
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
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException |
                 java.security.SignatureException e) {
            //in case there are multiple entries in the truststore, we don't want to log an error if the validation fails
            LOG.debug("Error occurred while verifying signature of the certificate [" + signedCertificateName
                    + "] with certificate from truststore with alias [" + alias + "].", e);
            return false;
        }
    }
}
