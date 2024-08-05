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
package eu.europa.ec.dynamicdiscovery.core.extension.impl;

import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.core.security.SignatureValidationContext;
import eu.europa.ec.dynamicdiscovery.exception.SMPExceptionCode;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.SMPEndpoint;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.List;

/**
 * Abstract class for reading service metadata
 *
 * @param <C> the type of the object to be parsed (Oasis SMP 1./2.0 , Peppol SMP, etc.
 */
public abstract class AbstractServiceMetadataReader<C> extends AbstractObjectReader<SMPServiceMetadata, C> {
    static final Logger LOG = LoggerFactory.getLogger(AbstractServiceMetadataReader.class);
    /**
     * Skip out-dated services
     * If the current system date is not in the interval - skip the service
     * <ServiceActivationDate>2016-06-06T11:06:02.000+02:00</ServiceActivationDate>
     * <ServiceExpirationDate>2026-06-06T11:06:02+02:00</ServiceExpirationDate>
     */

    boolean ignoreInvalidServices;

    protected AbstractServiceMetadataReader() {
        this(false);
    }

    protected AbstractServiceMetadataReader(boolean ignoreInvalidServices) {
        super(SMPExceptionCode.SERVICE_METADATA);
        this.ignoreInvalidServices = ignoreInvalidServices;
    }

    public boolean isIgnoreInvalidServices() {
        return ignoreInvalidServices;
    }

    public void setIgnoreInvalidServices(boolean ignoreInvalidServices) {
        this.ignoreInvalidServices = ignoreInvalidServices;
    }

    @Override
    public SMPServiceMetadata parseAndValidateSignature(Document document, ISignatureValidator signatureValidator, SignatureValidationContext context) throws TechnicalException {

        C serviceMetadata = parseNative(document);
        X509Certificate certificate = signatureValidator != null ? signatureValidator.verify(document, context) : null;

        SMPParticipantIdentifier participantIdentifierType = readParticipantIdentifier(serviceMetadata);
        SMPDocumentIdentifier documentIdentifier = readDocumentIdentifier(serviceMetadata);
        List<SMPEndpoint> endpoints = readEndpoints(serviceMetadata);

        return new SMPServiceMetadata.Builder()
                .participantIdentifier(participantIdentifierType)
                .documentIdentifier(documentIdentifier)
                .addEndpoints(endpoints)
                .object(serviceMetadata)
                .signerCertificate(certificate).build();
    }

    protected abstract SMPParticipantIdentifier readParticipantIdentifier(C serviceMetadata);

    protected abstract SMPDocumentIdentifier readDocumentIdentifier(C serviceMetadata);

    protected abstract List<SMPEndpoint> readEndpoints(C serviceMetadata);

    /**
     * Method reads the X509Certificate for base64 encoded certificate string
     *
     * @param certificateBase64 the base64 encoded certificate
     * @param endpointUrl       the endpoint url
     * @return the X509Certificate or null if certificate is null or can not be parsed
     */
    protected X509Certificate getX509CertificateFromBase64(String certificateBase64, String endpointUrl) {

        if (StringUtils.isBlank(certificateBase64)) {
            LOG.debug("Null certificate for endpoint [{}]", endpointUrl);
            return null;
        }
        final byte[] certificateBytes = Base64.getMimeDecoder().decode(certificateBase64);
        return getX509CertificateFromByteArray(certificateBytes, endpointUrl);
    }

    protected X509Certificate getX509CertificateFromByteArray(byte[] certificateBytes, String endpointUrl) {
        if (certificateBytes == null) {
            LOG.debug("Null certificate content for the endpoint [{}]", endpointUrl);
            return null;
        }

        try (InputStream is = new ByteArrayInputStream(certificateBytes)) {
            return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(is);
        } catch (Exception e) {
            LOG.error("Can not parse Certificate for endpoint [{}]!", endpointUrl, e);
            return null;
        }
    }

    /**
     * Method validates if service is valid - that is if current date is between
     * activation and expiration dates!
     *
     * @param serviceActivationDate the service activation date
     * @param serviceExpirationDate the service expiration date
     * @return true if endpoint is valid.
     */
    public boolean isServiceValid(OffsetDateTime serviceActivationDate, OffsetDateTime serviceExpirationDate) {
        OffsetDateTime currentDateTime = OffsetDateTime.now();
        if (serviceActivationDate != null && currentDateTime.isBefore(serviceActivationDate)) {
            LOG.debug("Service is not yet active. Start datetime [{}], current dateTime [{}]", serviceActivationDate, currentDateTime);
            return false;
        }
        if (serviceExpirationDate != null && currentDateTime.isAfter(serviceExpirationDate)) {
            LOG.debug("Service is expired. Expire datetime [{}], current datetime [{}]", serviceExpirationDate, currentDateTime);
            return false;
        }
        return true;
    }
}
