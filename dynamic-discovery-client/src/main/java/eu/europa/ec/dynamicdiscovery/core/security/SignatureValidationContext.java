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
package eu.europa.ec.dynamicdiscovery.core.security;

import java.security.cert.X509Certificate;
import java.util.List;

/**
 * The SignatureValidationContext class contains additional information for the
 * signature validation such as the certificate trust validation strategy.
 *
 * @author Joze Rihtarsic
 * @since 2.2
 */
public class SignatureValidationContext {
    public enum CertificateValidationStrategy {
        /**
         * The certificate is validated against the trust store.
         */
        TRUSTSTORE,
        /**
         * The certificate is validated against the trusted list of certificates.
         */
        TRUSTED_CERTIFICATES,
        /**
         * The certificate is validated against the trusted certificates and the trust store.
         */
        CERTIFICATE_SUBJECT_VALIDATION_AND_TRUSTSTORE

    }

    protected SignatureValidationContext(Builder builder) {
        this.certificateValidationStrategy = builder.certificateValidationStrategy;
        this.trustedCertificates = builder.trustedCertificates;
        this.certificateUID = builder.certificateUID;
    }

    CertificateValidationStrategy certificateValidationStrategy;


    List<X509Certificate> trustedCertificates;

    /**
     * Holds the Subject Unique Identifier of the certificate of the destination SMP.
     * A client SHOULD validate that the Subject Unique Identifier of the certificate used
     * to sign the resource at the destination SMP matches the Subject Unique Identifier
     * published in the redirecting SMP.
     * For details see <a href="http://docs.oasis-open.org/bdxr/bdx-smp/v1.0/os/bdx-smp-v1.0-os.htm">Service Metadata Publishing (SMP) Version 1.0</a>
     */
    String certificateUID;


    public CertificateValidationStrategy getCertificateValidationStrategy() {
        return certificateValidationStrategy;
    }

    public List<X509Certificate> getTrustedCertificates() {
        return trustedCertificates;
    }

    public String getCertificateUID() {
        return certificateUID;
    }

    /**
     * The Builder class for the SignatureValidationContext.
     */
    public static class Builder {
        // Default value is TRUSTSTORE
        private CertificateValidationStrategy certificateValidationStrategy = CertificateValidationStrategy.TRUSTSTORE;
        private List<X509Certificate> trustedCertificates;
        private String certificateUID;

        public Builder certificateValidationStrategy(CertificateValidationStrategy certificateValidationStrategy) {
            this.certificateValidationStrategy = certificateValidationStrategy;
            return this;
        }

        public Builder trustedCertificates(List<X509Certificate> trustedCertificates) {
            this.trustedCertificates = trustedCertificates;
            return this;
        }

        public Builder certificateUID(String certificateUID) {
            this.certificateUID = certificateUID;
            return this;
        }

        public SignatureValidationContext build() {
            return new SignatureValidationContext(this);
        }
    }
}
