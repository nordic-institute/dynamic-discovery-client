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

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Interface for validating SMP certificates.
 *
 * @author Joze Rihtarsic
 * @since 1.7
 */
public interface ISMPCertificateValidator {

    /**
     * Validate the certificate. If trustedLeafCertificate is not null, the certificate
     * is validated against the default truststore. Else the certificate must be
     * one of the trustedLeafCertificate.
     *
     * @param certificate the certificate to validate
     * @param context     the signature validation context
     * @throws CertificateException if the certificate is not valid or not trusted
     */
    void validateSMPCertificate(X509Certificate certificate, SignatureValidationContext context) throws CertificateException;

    /**
     * Validate the certificate. The certificate must be trusted in the default truststore.
     *
     * @param certificate the certificate to validate
     * @throws CertificateException if the certificate is not valid or not trusted
     */
    void validateSMPCertificate(X509Certificate certificate) throws CertificateException;
}
