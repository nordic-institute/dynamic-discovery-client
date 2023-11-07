package eu.europa.ec.dynamicdiscovery.core.extension.impl.peppol;

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
import gen.eu.europa.ec.ddc.api.addressing.AttributedURIType;
import gen.eu.europa.ec.ddc.api.addressing.EndpointReferenceType;
import gen.eu.europa.ec.ddc.api.peppol.EndpointType;
import gen.eu.europa.ec.ddc.api.peppol.ProcessType;
import gen.eu.europa.ec.ddc.api.peppol.ServiceMetadata;
import gen.eu.europa.ec.ddc.api.peppol.SignedServiceMetadata;
import gen.eu.europa.ec.ddc.api.peppol.identifiers.transport.DocumentIdentifier;
import gen.eu.europa.ec.ddc.api.peppol.identifiers.transport.ParticipantIdentifierType;
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
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Cosmin Baciu
 * @since 2.1
 */
public class PeppolSMPServiceMetadataReader implements IObjectReader<SMPServiceMetadata> {
    static final Logger LOG = LoggerFactory.getLogger(PeppolSMPServiceMetadataReader.class);
    private static final ThreadLocal<Unmarshaller> jaxbUnmarshaller = ThreadLocal.withInitial(() -> {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(SignedServiceMetadata.class, ServiceMetadata.class);
            return jaxbContext.createUnmarshaller();
        } catch (JAXBException ex) {
            LOG.error("Error occurred while initializing JAXBContext for SignedServiceMetadata. Cause message:" + ex, ex);
        }
        return null;
    });

    private static final ThreadLocal<Marshaller> jaxbMarshaller = ThreadLocal.withInitial(() -> {
        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(SignedServiceMetadata.class, ServiceMetadata.class);
            return jaxbContext.createMarshaller();
        } catch (JAXBException ex) {
            LOG.error("Error occurred while initializing JAXBContext for SignedServiceMetadata. Cause message:" + ex, ex);
        }
        return null;
    });

    private static Marshaller getMarshaller() {
        return jaxbMarshaller.get();
    }

    private static final QName PARSE_ELEMENT = new QName(PeppolSMPExtension.NAMESPACE, "SignedServiceMetadata");

    /**
     * Skip out-dated services
     * If the current system date is not in the interval - skip the service
     * <ServiceActivationDate>2016-06-06T11:06:02.000+02:00</ServiceActivationDate>
     * <ServiceExpirationDate>2026-06-06T11:06:02+02:00</ServiceExpirationDate>
     */

    boolean ignoreInvalidServices = false;

    public PeppolSMPServiceMetadataReader() {
    }

    public PeppolSMPServiceMetadataReader(boolean ignoreInvalidServices) {
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

    public boolean isIgnoreInvalidServices() {
        return ignoreInvalidServices;
    }

    public void setIgnoreInvalidServices(boolean ignoreInvalidServices) {
        this.ignoreInvalidServices = ignoreInvalidServices;
    }

    @Override
    public boolean handles(QName qName, Class<?> clazz) {
        return PeppolNamespaceUtil.supportedQNameMatchesProvided(PARSE_ELEMENT, SMPServiceMetadata.class, qName, clazz);
    }

    @Override
    public SMPServiceMetadata parse(Document document) throws TechnicalException {
        return parseAndValidateSignature(document, null);
    }

    @Override
    public Object parseNative(Document document) throws TechnicalException {
        try {
            return jaxbUnmarshaller.get().unmarshal(document);
        } catch (JAXBException e) {
            throw new BindException(SMPExceptionCode.SERVICE_METADATA, "Error occurred while parsing serviceGroup", e);
        }
    }

    @Override
    public Object parseNative(InputStream inputStream) throws TechnicalException {
        try {
            DocumentBuilder db = AbstractXMLResponseReader.createDocumentBuilder();
            // just to validate DISALLOW_DOCTYPE_FEATURE parse to Document
            Document document = db.parse(inputStream);
            return parseNative(document);
        } catch (SAXException | IOException e) {
            throw new BindException(SMPExceptionCode.SERVICE_METADATA, "Error occurred while SignedServiceMetadata", e);
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
    public SMPServiceMetadata parseAndValidateSignature(Document document, ISignatureValidator signatureValidator) throws TechnicalException {

        SignedServiceMetadata serviceMetadata = (SignedServiceMetadata) parseNative(document);
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

    protected SMPParticipantIdentifier readParticipantIdentifier(SignedServiceMetadata serviceMetadata) {
        if (serviceMetadata.getServiceMetadata() == null ||
                serviceMetadata.getServiceMetadata().getServiceInformation() == null ||
                serviceMetadata.getServiceMetadata().getServiceInformation().getParticipantIdentifier() == null) {
            LOG.debug("No SMPParticipantIdentifier defined for the SignedServiceMetadata.");
            return null;
        }


        ParticipantIdentifierType identifierType = serviceMetadata.getServiceMetadata().getServiceInformation().getParticipantIdentifier();
        return new SMPParticipantIdentifier(StringUtils.trim(identifierType.getValue()),
                StringUtils.trim(identifierType.getScheme()));
    }

    protected SMPDocumentIdentifier readDocumentIdentifier(SignedServiceMetadata serviceMetadata) {
        if (serviceMetadata.getServiceMetadata() == null ||
                serviceMetadata.getServiceMetadata().getServiceInformation() == null ||
                serviceMetadata.getServiceMetadata().getServiceInformation().getDocumentIdentifier() == null) {
            LOG.debug("No SMPDocumentIdentifier defined for the SignedServiceMetadata.");
            return null;
        }
        DocumentIdentifier identifierType = serviceMetadata.getServiceMetadata().getServiceInformation().getDocumentIdentifier();

        return new SMPDocumentIdentifier(StringUtils.trim(identifierType.getValue()), StringUtils.trim(identifierType.getScheme()));
    }


    protected SMPEndpoint readEndpointForProcess(EndpointType endpointType, SMPProcessIdentifier processIdentifier) {
        final String endpointUrl = getEndpointUrl(endpointType);
        if (!(isIgnoreInvalidServices() || isServiceValid(endpointType))) {
            LOG.debug("Ignore not-active/expired service for process [{}], transport [{}], url [{}]", processIdentifier.getIdentifier(), endpointType.getTransportProfile(), endpointUrl);
            return null;
        }

        X509Certificate certificate = getX509Certificate(endpointType);
        LOG.debug("Found transport for process: [{}], transport [{}], url [{}]", processIdentifier.getIdentifier(), endpointType.getTransportProfile(), endpointUrl);

        return new SMPEndpoint.Builder()
                .addProcessIdentifier(processIdentifier)
                .transportProfile(endpointType.getTransportProfile())
                .address(endpointUrl)
                .addCertificate(SMPEndpoint.DEFAULT_CERTIFICATE, certificate)
                .activationDate(endpointType.getServiceActivationDate())
                .expirationDate(endpointType.getServiceExpirationDate())
                .serviceDescription(endpointType.getServiceDescription())
                .technicalContactUrl(endpointType.getTechnicalContactUrl())
                .technicalInformationUrl(endpointType.getTechnicalInformationUrl())
                .build();
    }

    private String getEndpointUrl(EndpointType endpointType) {
        final EndpointReferenceType endpointReference = endpointType.getEndpointReference();
        if (endpointReference == null) {
            return null;
        }
        final AttributedURIType address = endpointReference.getAddress();
        if (address == null) {
            return null;
        }
        return address.getValue();
    }

    /**
     * Method validates if service is valid!
     *
     * @param endpointType the endpoint to validate its data
     * @return true if endpoint is valid.
     */
    public boolean isServiceValid(EndpointType endpointType) {
        OffsetDateTime currentDateTime = OffsetDateTime.now();
        if (endpointType.getServiceActivationDate() != null && currentDateTime.isBefore(endpointType.getServiceActivationDate())) {
            LOG.debug("Service is not yet active. Start datetime [{}], current dateTime [{}]", endpointType.getServiceActivationDate(), currentDateTime);
            return false;
        }
        if (endpointType.getServiceExpirationDate() != null && currentDateTime.isAfter(endpointType.getServiceExpirationDate())) {
            LOG.debug("Service is expired. Expire datetime [{}], current datetime [{}]", endpointType.getServiceExpirationDate(), currentDateTime);
            return false;
        }
        return true;
    }

    protected List<SMPEndpoint> readEndpointsForProcess(ProcessType processType) {

        if (processType == null ||
                processType.getServiceEndpointList() == null ||
                processType.getServiceEndpointList().getEndpoints().isEmpty()) {
            LOG.debug("No endpoint defined for the processType.");
            return Collections.emptyList();
        }


        final SMPProcessIdentifier processIdentifier = processType.getProcessIdentifier() != null ?
                new SMPProcessIdentifier(processType.getProcessIdentifier().getValue(), processType.getProcessIdentifier().getScheme()) : null;

        List<EndpointType> endpointTypes = processType.getServiceEndpointList().getEndpoints();
        return endpointTypes.stream().map(endpointType -> readEndpointForProcess(endpointType, processIdentifier)).collect(Collectors.toList());
    }

    protected List<SMPEndpoint> readEndpoints(SignedServiceMetadata serviceMetadata) {

        if (serviceMetadata.getServiceMetadata() == null ||
                serviceMetadata.getServiceMetadata().getServiceInformation() == null ||
                serviceMetadata.getServiceMetadata().getServiceInformation().getProcessList() == null ||
                serviceMetadata.getServiceMetadata().getServiceInformation().getProcessList().getProcesses().isEmpty()) {
            LOG.debug("No endpoint defined for the SignedServiceMetadata.");
            return Collections.emptyList();
        }
        List<ProcessType> processTypes = serviceMetadata.getServiceMetadata().getServiceInformation().getProcessList().getProcesses();
        return processTypes.stream().map(this::readEndpointsForProcess)
                .filter(smpEndpoints -> !smpEndpoints.isEmpty())
                .flatMap(java.util.Collection::stream).filter(Objects::nonNull)
                .collect(Collectors.toList());

    }

    protected X509Certificate getX509Certificate(EndpointType endpointType) {
        final String certificateBase64 = endpointType.getCertificate();
        if (endpointType == null || StringUtils.isBlank(certificateBase64)) {
            LOG.debug("Null endpoint type or certificate. Return null certificate");
            return null;
        }
        final byte[] certificateBytes = Base64.getMimeDecoder().decode(certificateBase64);
        try (InputStream is = new ByteArrayInputStream(certificateBytes)) {
            return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(is);
        } catch (Exception e) {
            LOG.error("Can not parse Certificate for endpoint [{}]!", getEndpointUrl(endpointType), e);
            return null;
        }
    }
}
