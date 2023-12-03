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

import eu.europa.ec.dynamicdiscovery.core.extension.IObjectReader;
import eu.europa.ec.dynamicdiscovery.core.reader.impl.AbstractXMLResponseReader;
import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.exception.BindException;
import eu.europa.ec.dynamicdiscovery.exception.SMPExceptionCode;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.SMPEndpoint;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPProcessIdentifier;
import gen.eu.europa.ec.ddc.api.smp20.ServiceMetadata;
import gen.eu.europa.ec.ddc.api.smp20.aggregate.Certificate;
import gen.eu.europa.ec.ddc.api.smp20.aggregate.Endpoint;
import gen.eu.europa.ec.ddc.api.smp20.aggregate.Process;
import gen.eu.europa.ec.ddc.api.smp20.aggregate.ProcessMetadata;
import gen.eu.europa.ec.ddc.api.smp20.basic.ParticipantID;
import gen.eu.europa.ec.ddc.api.smp20.basic.ServiceID;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.cert.CertificateFactory;
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
public class OasisSMP20ServiceMetadataReader implements IObjectReader<SMPServiceMetadata> {
    static final Logger LOG = LoggerFactory.getLogger(OasisSMP20ServiceMetadataReader.class);
    private static final ThreadLocal<Unmarshaller> jaxbUnmarshaller = ThreadLocal.withInitial(() -> {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ServiceMetadata.class);
            return jaxbContext.createUnmarshaller();
        } catch (JAXBException ex) {
            LOG.error("Error occurred while initializing JAXBContext for ServiceMetadata. Cause message:" + ex, ex);
        }
        return null;
    });

    private static final ThreadLocal<Marshaller> jaxbMarshaller = ThreadLocal.withInitial(() -> {
        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(ServiceMetadata.class);
            return jaxbContext.createMarshaller();
        } catch (JAXBException ex) {
            LOG.error("Error occurred while initializing JAXBContext for ServiceMetadata. Cause message:" + ex, ex);
        }
        return null;
    });

    private static Marshaller getMarshaller() {
        return jaxbMarshaller.get();
    }


    /**
     * Skip out-dated services
     * If the current system date is not in the interval - skip the service
     * <ActivationDate>2016-06-06T11:06:02.000+02:00</ActivationDate>
     * <ExpirationDate>2026-06-06T11:06:02+02:00</ExpirationDate>
     */

    boolean ignoreInvalidServices = false;


    public OasisSMP20ServiceMetadataReader() {
    }

    public OasisSMP20ServiceMetadataReader(boolean ignoreInvalidServices) {
        this.ignoreInvalidServices = ignoreInvalidServices;
    }

    /**
     * Removes the current thread's ServiceMetadata Unmarshaller for this thread-local variable. If this thread-local variable
     * is subsequently read by the current thread, its value will be reinitialized by invoking its initialValue method.
     */
    public void destroyUnmarshaller() {
        jaxbUnmarshaller.remove();
    }

    public void destroyMarshaller() {
        jaxbMarshaller.remove();
    }

    public Unmarshaller getUnmarshaller() {
        return jaxbUnmarshaller.get();
    }

    private static final QName PARSE_ELEMENT
            = new QName("http://docs.oasis-open.org/bdxr/ns/SMP/2/ServiceMetadata", "ServiceMetadata");

    public boolean isIgnoreInvalidServices() {
        return ignoreInvalidServices;
    }

    public void setIgnoreInvalidServices(boolean ignoreInvalidServices) {
        this.ignoreInvalidServices = ignoreInvalidServices;
    }


    @Override
    public boolean handles(QName qName, Class<?> clazz) {
        return PARSE_ELEMENT.equals(qName) && clazz == SMPServiceMetadata.class;
    }

    @Override
    public SMPServiceMetadata parse(Document document) throws TechnicalException {
        return parseAndValidateSignature(document, null);
    }

    @Override
    public ServiceMetadata parseNative(Document document) throws TechnicalException {
        try {
            return (ServiceMetadata) jaxbUnmarshaller.get().unmarshal(document);
        } catch (JAXBException e) {
            throw new BindException(SMPExceptionCode.SERVICE_METADATA, "Error occurred while parsing ServiceMetadata", e);
        }
    }

    public Document objectToDocument(ServiceMetadata serviceMetadata) throws TechnicalException {
        try {
            DocumentBuilder db = AbstractXMLResponseReader.createDocumentBuilder();
            Document document = db.newDocument();
            getMarshaller().marshal(serviceMetadata, document);
            return document;
        } catch (JAXBException e) {
            throw new BindException(SMPExceptionCode.SERVICE_METADATA, "Error occurred while parsing ServiceMetadata object", e);
        }
    }

    @Override
    public void serializeNative(Object jaxbObject, OutputStream outputStream, boolean prettyPrint) throws TechnicalException {
        if (jaxbObject == null) {
            return;
        }
        Marshaller jaxbMarshaller = getMarshaller();
        // Pretty Print XML
        try {
            if (prettyPrint) {
                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, prettyPrint);
            }
            // to remove xmlDeclaration
            jaxbMarshaller.marshal(jaxbObject, outputStream);
        } catch (JAXBException e) {
            throw new BindException(SMPExceptionCode.SERVICE_METADATA, "Error occurred while serializing the ServiceGroup", e);
        }
    }

    @Override
    public ServiceMetadata parseNative(InputStream inputStream) throws TechnicalException {
        try {
            DocumentBuilder db = AbstractXMLResponseReader.createDocumentBuilder();
            // just to validate DISALLOW_DOCTYPE_FEATURE parse to Document
            Document document = db.parse(inputStream);
            return parseNative(document);
        } catch (SAXException | IOException e) {
            throw new BindException(SMPExceptionCode.SERVICE_METADATA, "Error occurred while parsing ServiceMetadata", e);
        }
    }

    @Override
    public SMPServiceMetadata parseAndValidateSignature(Document document, ISignatureValidator signatureValidator) throws TechnicalException {
        ServiceMetadata serviceMetadata = parseNative(document);

        X509Certificate certificate = signatureValidator != null ? signatureValidator.verify(document) : null;

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

    /**
     * Read the Oasis SMP 2.0 ServiceMetadata and extract participant identifier and return the SMPParticipantIdentifier
     *
     * @param serviceMetadata Oasis SMP 2.0 ServiceMetadata
     * @return object SMPParticipantIdentifier with schema and id of participant identifier.
     */
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
                        .addCertificates(getX509Certificates(endpointType))
                        .activationDate(endpointType.getActivationDate() == null ? null : endpointType.getActivationDate().getValue())
                        .expirationDate(endpointType.getExpirationDate() == null ? null : endpointType.getExpirationDate().getValue())
                        .build();
                LOG.debug("Add endpoint for process [{}], transport [{}], url [{}]", processIdentifiers,
                        transportProfile,
                        addressURI);
                endpoints.add(endpoint);
            }
        }
        return endpoints;
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
    protected Map<String, X509Certificate> getX509Certificates(Endpoint endpointType) {
        if (endpointType == null ||
                endpointType.getCertificates() == null ||
                endpointType.getCertificates().isEmpty()
        ) {
            return Collections.emptyMap();
        }
        List<Certificate> certificates = endpointType.getCertificates();

        Map<String, X509Certificate> map = new HashMap<>();
        for (Certificate certificate : certificates) {
            X509Certificate cert = getCertificate(certificate);
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

    protected X509Certificate getCertificate(Certificate certificate) {
        if (certificate == null ||
                certificate.getContentBinaryObject() == null ||
                certificate.getContentBinaryObject().getValue() == null) {
            return null;
        }


        try (InputStream is = new ByteArrayInputStream(certificate.getContentBinaryObject().getValue())) {
            return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(is);
        } catch (Exception e) {
            return null;
        }
    }

}
