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

    final static Logger LOG = LoggerFactory.getLogger(DefaultSMPCertificateValidator.class);

    protected KeyStore trustStore;
    protected Pattern regexCertificateSubjectValidation;

    public DefaultSMPCertificateValidator(KeyStore trustStore, String regexCertificateSubjectValidation) throws TechnicalException {
        this.trustStore = trustStore;
        this.regexCertificateSubjectValidation = StringUtils.isBlank(regexCertificateSubjectValidation)?null:
                Pattern.compile(regexCertificateSubjectValidation);
        if (this.trustStore == null) {
            throw new SignatureException("TrustStore must be not null for signature validation.");
        }
    }

    @Override
    public void validateSMPCertificate(X509Certificate certificate) throws CertificateException {
        verifyTrust(certificate);
        verifyCertificateSubject(certificate);
        LOG.debug("Certificate % is trusted",certificate.getSubjectX500Principal().getName());
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
        if (regexCertificateSubjectValidation!=null) {
            String subject = signerCertificate.getSubjectX500Principal().toString();
            Matcher matcher = regexCertificateSubjectValidation.matcher(subject);
            if (!matcher.matches()) {
                throw new CertificateException(String.format("Given certificate: %s does not match configured regex: %s.", subject, regexCertificateSubjectValidation.pattern()));
            }
            LOG.debug("Given certificate: %s  match the configured regex: %s.", subject, regexCertificateSubjectValidation.pattern());
        } else {
            LOG.debug("Null regular expression for subject verification!");
        }
    }


    /**
     * Method verifies if certificate has trust anchor in truststore. Trust anchor is certificate itself
     * or direct issuer.
     *
     * @param signerCertificate
     * @throws CertificateException
     */
    private void verifyTrust(X509Certificate signerCertificate) throws CertificateException {
        try {
            for (String alias : Collections.list(trustStore.aliases())) {

                if (isAliasCertificateTrustAnchor(signerCertificate, alias)) {
                    LOG.debug("Certificate with alias [{}] is trust anchor of the certificate [{}]!", alias,
                            signerCertificate.getSubjectDN().getName());
                    return;
                }
            }
            throw new CertificateException("TrustStore does not contain trusted direct Issuer or the Certificate.");
        } catch (RuntimeException | KeyStoreException exc) {
            throw new CertificateException("Runtime exception:" + exc.getMessage(), exc);
        }
    }

    /**
     * Method verifies if certificate with given alias is valid trust anchor for signer certificate. TrustAnchor is
     * the certificate itself or direct issuer!
     *
     * @param signerCertificate
     * @param alias
     * @return true is certificate with given alias in truststore is valid trust achor
     * @throws CertificateException
     */
    protected boolean isAliasCertificateTrustAnchor(X509Certificate signerCertificate, String alias) throws CertificateException {
        //Checks if certificate is under the truststore and is trusted
        String certName = signerCertificate.getSubjectX500Principal().getName();
        try {
            KeyStore.Entry entry = trustStore.getEntry(alias, null);
            if (!trustStore.entryInstanceOf(alias, KeyStore.TrustedCertificateEntry.class)) {
                LOG.warn("Certificate with alias [{}] is not Trusted certificate entry!", alias);
                return false;
            }

            //Checks if certificate is X509Certificate type
            KeyStore.TrustedCertificateEntry certificateEntry =
                    (KeyStore.TrustedCertificateEntry) trustStore.getEntry(alias, null);
            Certificate trustedCertificate = certificateEntry.getTrustedCertificate();
            if (!(trustedCertificate instanceof X509Certificate)) {
                LOG.warn("Certificate with alias [{}] is not X509Certificate! Only X509Certificate type is supported!", alias);
                return false;
            }

            // Verify trust
            if (signerCertificate.equals(trustedCertificate)) {
                LOG.debug("Certificate with alias [{}] is direct trust anchor of the certificate [{}]!", alias,
                        certName);
                return true;
            }

            if (isSignedBy(signerCertificate, trustedCertificate, certName, alias)) {
                LOG.debug("Certificate with alias [{}] is 'chain' trust anchor of the certificate [{}]!", alias,
                        certName);
                return true;
            }
        } catch (NoSuchAlgorithmException | KeyStoreException | UnrecoverableEntryException exc) {
            throw new CertificateException("Truststore exception occurred when  accessing certificate with alias:" + alias
                    + ". Error message:" + exc.getMessage(), exc);
        }
        LOG.debug("Certificate with alias [{}] is not trust anchor of the certificate [{}]!", alias,
                certName);
        return false;

    }

    private boolean isSignedBy(Certificate signed, Certificate signer, String signedCertificateName, String alias) throws CertificateException {
        try {
            signed.verify(signer.getPublicKey());
            LOG.debug("Certificate [{}] is signed by certificate with alias [{}] from truststore.",signedCertificateName, alias);
            return true;
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException | java.security.SignatureException e) {
            LOG.error("Error occurred while verifying signature of the certificate ["+signedCertificateName
                    +"] with certificate from truststore with alias ["+alias+"].",e);
            return false;
        }
    }
}
