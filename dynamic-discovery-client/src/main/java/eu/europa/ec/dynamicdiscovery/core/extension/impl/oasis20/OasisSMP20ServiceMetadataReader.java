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
package eu.europa.ec.dynamicdiscovery.core.extension.impl.oasis20;

import eu.europa.ec.dynamicdiscovery.core.extension.impl.AbstractServiceMetadataReader;
import eu.europa.ec.dynamicdiscovery.model.SMPEndpoint;
import eu.europa.ec.dynamicdiscovery.model.SMPRedirect;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPProcessIdentifier;
import gen.eu.europa.ec.ddc.api.smp20.ServiceMetadata;
import gen.eu.europa.ec.ddc.api.smp20.aggregate.Process;
import gen.eu.europa.ec.ddc.api.smp20.aggregate.*;
import gen.eu.europa.ec.ddc.api.smp20.basic.ParticipantID;
import gen.eu.europa.ec.ddc.api.smp20.basic.ServiceID;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import java.security.cert.X509Certificate;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.trim;

/**
 * Object reads the Oasis SMP 2.0 service metadata entity and returns the SMPServiceMetadata object
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */
public class OasisSMP20ServiceMetadataReader extends AbstractServiceMetadataReader<ServiceMetadata> {
    static final Logger LOG = LoggerFactory.getLogger(OasisSMP20ServiceMetadataReader.class);
    private static final QName PARSE_ELEMENT
            = new QName("http://docs.oasis-open.org/bdxr/ns/SMP/2/ServiceMetadata", "ServiceMetadata");


    private static final ThreadLocal<Unmarshaller> jaxbUnmarshaller = ThreadLocal.withInitial(() -> {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ServiceMetadata.class);
            return jaxbContext.createUnmarshaller();
        } catch (JAXBException ex) {
            LOG.error("Error occurred while initializing JAXBContext for ServiceMetadata. Cause message: ["
                    + ExceptionUtils.getRootCauseMessage(ex) + "]", ex);
        }
        return null;
    });

    private static final ThreadLocal<Marshaller> jaxbMarshaller = ThreadLocal.withInitial(() -> {
        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(ServiceMetadata.class);
            return jaxbContext.createMarshaller();
        } catch (JAXBException ex) {
            LOG.error("Error occurred while initializing JAXBContext for ServiceMetadata. Cause message: ["
                    + ExceptionUtils.getRootCauseMessage(ex) + "]", ex);
        }
        return null;
    });


    public OasisSMP20ServiceMetadataReader() {
    }

    public OasisSMP20ServiceMetadataReader(boolean ignoreInvalidServices) {
        super.setIgnoreInvalidServices(ignoreInvalidServices);
    }

    /**
     * Removes the current thread's ServiceMetadata Unmarshaller for this thread-local variable. If this thread-local variable
     * is subsequently read by the current thread, its value will be reinitialized by invoking its initialValue method.
     */
    @Override
    public void destroyUnmarshaller() {
        jaxbUnmarshaller.remove();
    }

    @Override
    public void destroyMarshaller() {
        jaxbMarshaller.remove();
    }

    @Override
    protected Marshaller getMarshaller() {
        return jaxbMarshaller.get();
    }

    @Override
    protected Unmarshaller getUnmarshaller() {
        return jaxbUnmarshaller.get();
    }


    @Override
    public boolean handles(QName qName, Class<?> clazz) {
        return PARSE_ELEMENT.equals(qName) && clazz == SMPServiceMetadata.class;
    }


    /**
     * Read the Oasis SMP 2.0 ServiceMetadata and extract participant identifier and return the SMPParticipantIdentifier
     *
     * @param serviceMetadata Oasis SMP 2.0 ServiceMetadata
     * @return object SMPParticipantIdentifier with schema and id of participant identifier.
     */
    @Override
    protected SMPParticipantIdentifier readParticipantIdentifier(ServiceMetadata serviceMetadata) {
        if (serviceMetadata.getParticipantID() == null) {
            return null;
        }
        ParticipantID identifierType = serviceMetadata.getParticipantID();
        return new SMPParticipantIdentifier(StringUtils.trim(identifierType.getValue()),
                StringUtils.trim(identifierType.getSchemeID()));
    }

    /**
     * Read the Oasis SMP 2.0 ServiceMetadata and extract document identifier and return the SMPDocumentIdentifier
     *
     * @param serviceMetadata Oasis SMP 2.0 ServiceMetadata
     * @return object SMPDocumentIdentifier with schema and id of document identifier.
     */
    @Override
    protected SMPDocumentIdentifier readDocumentIdentifier(ServiceMetadata serviceMetadata) {
        if (serviceMetadata.getServiceID() == null) {
            return null;
        }
        ServiceID identifierType = serviceMetadata.getServiceID();

        return new SMPDocumentIdentifier(StringUtils.trim(identifierType.getValue()), StringUtils.trim(identifierType.getSchemeID()));
    }

    /**
     * Read the Oasis SMP 2.0 ServiceMetadata and extract document identifier and return the SMPDocumentIdentifier.
     * The process with null process.getID() are ignored
     *
     * @param processMetadata Oasis SMP 2.0 ProcessMetadata
     * @return List of  SMPProcessIdentifiers with schema and id of process identifier.
     */
    protected List<SMPProcessIdentifier> readProcessIdentifier(ProcessMetadata processMetadata) {
        if (processMetadata == null) {
            return Collections.emptyList();
        }
        return processMetadata.getProcesses().stream()
                .map(Process::getID)
                .filter(Objects::nonNull)
                .map(processId ->
                        new SMPProcessIdentifier(trim(processId.getValue()),
                                trim(processId.getSchemeID())))
                .collect(Collectors.toList());
    }


    /**
     * Read the Oasis SMP 2.0 ServiceMetadata and extract endpoints
     *
     * @param serviceMetadata Oasis SMP 2.0 ServiceMetadata
     * @return list of SMPEndpoints with process list, certificate map and the endpoint URL address
     */
    @Override
    protected List<SMPEndpoint> readEndpoints(ServiceMetadata serviceMetadata) {

        List<SMPEndpoint> endpoints = new ArrayList<>();

        List<ProcessMetadata> processTypeIterator = serviceMetadata.getProcessMetadatas();
        for (ProcessMetadata processMetadata : processTypeIterator) {

            // get process identifiers
            List<SMPProcessIdentifier> processIdentifiers = readProcessIdentifier(processMetadata);

            for (Endpoint endpointType : processMetadata.getEndpoints()) {
                String transportProfile = endpointType.getTransportProfileID() == null ? null : endpointType.getTransportProfileID().getValue();
                String addressURI = endpointType.getAddressURI() == null ? null : endpointType.getAddressURI().getValue();

                if (!(isIgnoreInvalidServices() || isServiceValid(endpointType))) {
                    LOG.debug("Ignore not-active/expired service for process [{}], transport [{}], url [{}]", processIdentifiers,
                            transportProfile,
                            addressURI);
                    continue;
                }

                SMPEndpoint endpoint = new SMPEndpoint.Builder()
                        .addProcessIdentifiers(processIdentifiers)
                        .transportProfile(transportProfile)
                        .address(addressURI)
                        .addCertificates(getX509CertificatesFromEndpoint(endpointType))
                        .activationDate(endpointType.getActivationDate() == null ? null : endpointType.getActivationDate().getValue())
                        .expirationDate(endpointType.getExpirationDate() == null ? null : endpointType.getExpirationDate().getValue())
                        .build();
                LOG.debug("Add endpoint for process [{}], transport [{}], url [{}]", processIdentifiers,
                        transportProfile,
                        addressURI);
                endpoints.add(endpoint);
            }
            SMPRedirect redirect = readRedirect(processMetadata.getRedirect());
            if (!processMetadata.getEndpoints().isEmpty()) {
                if (redirect != null) {
                    LOG.warn("Process  [{}] has endpoint and redirect! The redirect is skipped!",
                            processIdentifiers);
                }
                continue;
            }
            // no endpoint, try to read redirect
            LOG.debug("No endpoint for process [{}], try to read redirect", processIdentifiers);
            if (redirect != null) {
                SMPEndpoint endpoint = new SMPEndpoint.Builder()
                        .addProcessIdentifiers(processIdentifiers)
                        .redirect(redirect)
                        .build();
                endpoints.add(endpoint);
            }
        }
        return endpoints;
    }

    /**
     * Method reads the redirect from Oasis SMP 2.0 ServiceMetadata and return
     * the SMPRedirect
     *
     * @param redirect
     * @return
     */
    public SMPRedirect readRedirect(Redirect redirect) {
        if (redirect == null) {
            return null;
        }
        String redirectUrl = redirect.getPublisherURI() != null ?
                redirect.getPublisherURI().getValue() : null;
        Map<String, X509Certificate> trustedCerts = getX509Certificates(redirect.getCertificates(), redirectUrl);
        return new SMPRedirect.Builder()
                .redirectUrl(redirectUrl)
                .addRedirectCertificates(trustedCerts)
                .build();
    }

    /**
     * Method validates if service is valid!
     *
     * @return
     */
    protected boolean isServiceValid(Endpoint endpointType) {
        OffsetDateTime currentDateTime = OffsetDateTime.now();
        if (endpointType.getActivationDate() != null &&
                endpointType.getActivationDate().getValue() != null &&
                currentDateTime.isBefore(endpointType.getActivationDate().getValue())) {
            LOG.debug("Service is not yet active. Start datetime [{}], current dateTime [{}]", endpointType.getActivationDate(), currentDateTime);
            return false;
        }
        if (endpointType.getExpirationDate() != null &&
                endpointType.getExpirationDate().getValue() != null &&
                currentDateTime.isAfter(endpointType.getExpirationDate().getValue())) {
            LOG.debug("Service is expired. Expire datetime [{}], current datetime [{}]", endpointType.getExpirationDate().getValue(), currentDateTime);
            return false;
        }
        return true;
    }

    /**
     * Method returns map of certificates and types
     *
     * @param endpointType
     * @return
     */
    protected Map<String, X509Certificate> getX509CertificatesFromEndpoint(Endpoint endpointType) {
        if (endpointType == null) {
            return Collections.emptyMap();
        }
        String urlAddress = endpointType.getAddressURI() == null ? null : endpointType.getAddressURI().getValue();
        return getX509Certificates(endpointType.getCertificates(), urlAddress);
    }

    protected Map<String, X509Certificate> getX509Certificates(List<Certificate> certificates, String urlAddress) {
        if (certificates == null ||
                certificates.isEmpty()
        ) {
            return Collections.emptyMap();
        }

        Map<String, X509Certificate> map = new HashMap<>();
        for (Certificate certificate : certificates) {
            X509Certificate cert = getCertificate(certificate, urlAddress);
            String key = getCertificateType(certificate);
            if (cert == null) {
                LOG.warn("Certificate with key [{}], is null. Skip entry", key);
                continue;
            }
            if (!map.containsKey(key)) {
                map.put(key, cert);
            } else {
                LOG.warn("Certificate with key [{}], is has duplicate key! Skip entry [{}]", key, cert);
            }
        }
        return map;
    }


    protected String getCertificateType(Certificate certificate) {
        if (certificate == null ||
                certificate.getTypeCode() == null ||
                certificate.getTypeCode().getValue() == null) {
            return SMPEndpoint.DEFAULT_CERTIFICATE;
        }
        return certificate.getTypeCode().getValue();
    }

    protected X509Certificate getCertificate(Certificate certificate, String urlAddress) {
        if (certificate == null ||
                certificate.getContentBinaryObject() == null ||
                certificate.getContentBinaryObject().getValue() == null) {
            return null;
        }
        return getX509CertificateFromByteArray(certificate.getContentBinaryObject().getValue(), urlAddress);
    }

}
