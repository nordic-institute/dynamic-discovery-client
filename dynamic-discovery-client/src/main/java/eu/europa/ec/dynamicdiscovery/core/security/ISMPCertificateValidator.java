package eu.europa.ec.dynamicdiscovery.core.security;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public interface ISMPCertificateValidator {

    void validateSMPCertificate(X509Certificate var2) throws CertificateException;
}