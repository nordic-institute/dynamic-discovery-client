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

import eu.europa.ec.dynamicdiscovery.exception.SignatureException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import org.w3c.dom.Document;

import java.security.cert.X509Certificate;

/**
 * Interface for signature validation. Implementations should be able to verify the signature of the document
 * and return the certificate used to sign it. The signature validation must also check if the certificate is trusted.
 * If the trusted list of certificates is provided and not empty the signing certificate must be in the list.
 * else the certificate trust is validated by the regular truststore validation.
 *
 * @author Flávio W. R. Santos
 * @author Joze Rihtarsic
 * @since 1.0
 */
public interface ISignatureValidator {

    default X509Certificate verify(Document document) throws SignatureException {
        return verify(document, null);
    }

    /**
     * Verify the signature of the document and return the certificate used to sign it.
     * If the trusted list of certificates is provided and not empty and the signing
     * certificate must be in the list else a Exception is thrown.
     *
     * @param document the document to verify
     * @param context  the signature validation context
     * @return the certificate used to sign the document
     * @throws TechnicalException if the signature is not valid or the certificate is not trusted
     */
    X509Certificate verify(Document document, SignatureValidationContext context) throws SignatureException;
}
