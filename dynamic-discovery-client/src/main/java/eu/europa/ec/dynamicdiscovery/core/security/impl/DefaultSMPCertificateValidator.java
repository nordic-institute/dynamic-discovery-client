package eu.europa.ec.dynamicdiscovery.core.security.impl;

import eu.europa.ec.dynamicdiscovery.core.security.ISMPCertificateValidator;
import eu.europa.ec.dynamicdiscovery.exception.SignatureException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import org.apache.commons.lang3.StringUtils;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;


public class DefaultSMPCertificateValidator implements ISMPCertificateValidator {

    protected KeyStore trustStore;
    protected String regexCertificateSubjectValidation;

    public DefaultSMPCertificateValidator(KeyStore trustStore, String regexCertificateSubjectValidation) throws TechnicalException {
        this.trustStore = trustStore;
        this.regexCertificateSubjectValidation = regexCertificateSubjectValidation;
        if (this.trustStore == null) {
            throw new SignatureException("TrustStore must be not null for signature validation.");
        }
    }

    @Override
    public void validateSMPCertificate(X509Certificate certificate) throws CertificateException {
        verifyCertificate(certificate);
        verifyCertificateSubject(certificate);
    }


    private void verifyCertificateSubject(X509Certificate signerCertificate) throws CertificateException {
        if (!StringUtils.isEmpty(regexCertificateSubjectValidation)) {
            if (!signerCertificate.getSubjectX500Principal().toString().matches(regexCertificateSubjectValidation)) {
                throw new CertificateException(String.format("Given certificate: %s does not match configured regex: %s.", signerCertificate.getSubjectX500Principal(), regexCertificateSubjectValidation));
            }
        }
    }

    private void verifyCertificate(X509Certificate signerCertificate) throws CertificateException {
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

            throw new CertificateException("TrustStore does not contain Issuer CA.");

        } catch (RuntimeException exc) {
            throw new CertificateException("Runtime exception:" + exc.getMessage(), exc);
        } catch (NoSuchAlgorithmException | KeyStoreException | UnrecoverableEntryException exc) {
            throw new CertificateException("Truststore exception:" + exc.getMessage(), exc);
        }

    }

    private boolean isSignedBy(Certificate signed, Certificate signer) throws CertificateException {
        try {
            signed.verify(signer.getPublicKey());
            return true;
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException | java.security.SignatureException e) {
            return false;
        }
    }
}
