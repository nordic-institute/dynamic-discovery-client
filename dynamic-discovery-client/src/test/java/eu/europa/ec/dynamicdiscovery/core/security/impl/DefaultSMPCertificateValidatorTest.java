package eu.europa.ec.dynamicdiscovery.core.security.impl;

import eu.europa.ec.dynamicdiscovery.core.security.SignatureValidationContext;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import org.junit.jupiter.api.Test;

import javax.security.auth.x500.X500Principal;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DefaultSMPCertificateValidatorTest {


    @Test
    void validateSMPCertificate_withTrustedCertificatesStrategy() throws CertificateException, TechnicalException {
        X509Certificate mockCertificate = mock(X509Certificate.class);
        when(mockCertificate.getSubjectX500Principal()).thenReturn(mock(X500Principal.class));
        SignatureValidationContext mockContext = mock(SignatureValidationContext.class);
        when(mockContext.getCertificateValidationStrategy()).thenReturn(SignatureValidationContext.CertificateValidationStrategy.TRUSTED_CERTIFICATES);
        when(mockContext.getTrustedCertificates()).thenReturn(List.of(mockCertificate));

        DefaultSMPCertificateValidator validator = new DefaultSMPCertificateValidator(mock(KeyStore.class), null);
        validator.validateSMPCertificate(mockCertificate, mockContext);

        verify(mockContext).getCertificateValidationStrategy();
    }

    // Test for TRUSTSTORE case
    @Test
    void validateSMPCertificate_withTrustStoreStrategy() throws CertificateException, TechnicalException {
        X509Certificate mockCertificate = mock(X509Certificate.class);
        when(mockCertificate.getSubjectX500Principal()).thenReturn(mock(X500Principal.class));
        SignatureValidationContext mockContext = mock(SignatureValidationContext.class);
        when(mockContext.getCertificateValidationStrategy()).thenReturn(SignatureValidationContext.CertificateValidationStrategy.TRUSTSTORE);

        DefaultSMPCertificateValidator validator = spy(new DefaultSMPCertificateValidator(mock(KeyStore.class), null));
        doNothing().when(validator).verifyTrust(mockCertificate);
        doNothing().when(validator).verifyCertificateSubject(mockCertificate);

        validator.validateSMPCertificate(mockCertificate, mockContext);

        verify(validator).verifyTrust(mockCertificate);
        verify(validator).verifyCertificateSubject(mockCertificate);
    }

    // Test for CERTIFICATE_SUBJECT_VALIDATION_AND_TRUSTSTORE case
    @Test
    void validateSMPCertificate_withCertificateSubjectValidationAndTrustStoreStrategy() throws CertificateException, TechnicalException {
        X509Certificate mockCertificate = mock(X509Certificate.class);
        when(mockCertificate.getSubjectX500Principal()).thenReturn(mock(X500Principal.class));
        SignatureValidationContext mockContext = mock(SignatureValidationContext.class);
        when(mockContext.getCertificateValidationStrategy()).thenReturn(SignatureValidationContext.CertificateValidationStrategy.CERTIFICATE_SUBJECT_VALIDATION_AND_TRUSTSTORE);

        DefaultSMPCertificateValidator validator = spy(new DefaultSMPCertificateValidator(mock(KeyStore.class), null));
        doNothing().when(validator).verifyTrust(mockCertificate);
        doNothing().when(validator).validateCertificateSubjectMatch(mockCertificate, mockContext);

        validator.validateSMPCertificate(mockCertificate, mockContext);

        verify(validator).verifyTrust(mockCertificate);
        verify(validator).validateCertificateSubjectMatch(mockCertificate, mockContext);
    }

    @Test
    void validateCertificateWithTrustedList_validCertificate() throws TechnicalException {
        X509Certificate mockCertificate = mock(X509Certificate.class);
        SignatureValidationContext mockContext = mock(SignatureValidationContext.class);
        when(mockContext.getTrustedCertificates()).thenReturn(List.of(mockCertificate));

        DefaultSMPCertificateValidator validator = new DefaultSMPCertificateValidator(mock(KeyStore.class), null);

        assertDoesNotThrow(() -> validator.validateCertificateWithTrustedList(mockCertificate, mockContext, "TestCert"));
    }

    // Test for null SignatureValidationContext
    @Test
    void validateCertificateWithTrustedList_nullContext() throws TechnicalException {
        X509Certificate mockCertificate = mock(X509Certificate.class);
        DefaultSMPCertificateValidator validator = new DefaultSMPCertificateValidator(mock(KeyStore.class), null);

        CertificateException exception = assertThrows(CertificateException.class,
                () -> validator.validateCertificateWithTrustedList(mockCertificate, null, "TestCert"));

        assertEquals("SignatureValidationContext is null!", exception.getMessage());
    }

    // Test for empty trusted certificate list
    @Test
    void validateCertificateWithTrustedList_emptyTrustedList() throws TechnicalException {
        X509Certificate mockCertificate = mock(X509Certificate.class);
        SignatureValidationContext mockContext = mock(SignatureValidationContext.class);
        when(mockContext.getTrustedCertificates()).thenReturn(Collections.emptyList());

        DefaultSMPCertificateValidator validator = new DefaultSMPCertificateValidator(mock(KeyStore.class), null);

        CertificateException exception = assertThrows(CertificateException.class,
                () -> validator.validateCertificateWithTrustedList(mockCertificate, mockContext, "TestCert"));

        assertEquals("Trusted certificate list is empty!", exception.getMessage());
    }

    // Test for certificate not in the trusted list
    @Test
    void validateCertificateWithTrustedList_certificateNotInTrustedList() throws TechnicalException {
        X509Certificate mockCertificate = mock(X509Certificate.class);
        X509Certificate anotherCertificate = mock(X509Certificate.class);
        SignatureValidationContext mockContext = mock(SignatureValidationContext.class);
        when(mockContext.getTrustedCertificates()).thenReturn(List.of(anotherCertificate));

        DefaultSMPCertificateValidator validator = new DefaultSMPCertificateValidator(mock(KeyStore.class), null);

        CertificateException exception = assertThrows(CertificateException.class,
                () -> validator.validateCertificateWithTrustedList(mockCertificate, mockContext, "TestCert"));

        assertEquals("Provided certificate list does not contain trusted leaf certificate for [TestCert]", exception.getMessage());
    }
}