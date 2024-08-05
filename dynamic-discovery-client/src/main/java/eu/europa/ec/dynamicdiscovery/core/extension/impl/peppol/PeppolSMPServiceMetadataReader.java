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
package eu.europa.ec.dynamicdiscovery.core.extension.impl.peppol;

import eu.europa.ec.dynamicdiscovery.core.extension.impl.AbstractServiceMetadataReader;
import eu.europa.ec.dynamicdiscovery.model.SMPEndpoint;
import eu.europa.ec.dynamicdiscovery.model.SMPRedirect;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPProcessIdentifier;
import eu.europa.ec.dynamicdiscovery.util.NamespaceUtil;
import gen.eu.europa.ec.ddc.api.addressing.AttributedURIType;
import gen.eu.europa.ec.ddc.api.addressing.EndpointReferenceType;
import gen.eu.europa.ec.ddc.api.peppol.*;
import gen.eu.europa.ec.ddc.api.peppol.identifiers.transport.DocumentIdentifier;
import gen.eu.europa.ec.ddc.api.peppol.identifiers.transport.ParticipantIdentifierType;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Cosmin Baciu
 * @since 2.1
 */
public class PeppolSMPServiceMetadataReader extends AbstractServiceMetadataReader<SignedServiceMetadata> {
    static final Logger LOG = LoggerFactory.getLogger(PeppolSMPServiceMetadataReader.class);
    private static final ThreadLocal<Unmarshaller> jaxbUnmarshaller = ThreadLocal.withInitial(() -> {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(SignedServiceMetadata.class, ServiceMetadata.class);
            return jaxbContext.createUnmarshaller();
        } catch (JAXBException ex) {
            LOG.error("Error occurred while initializing JAXBContext for SignedServiceMetadata. Cause message: ["
                    + ExceptionUtils.getRootCauseMessage(ex) + "]", ex);
        }
        return null;
    });

    private static final ThreadLocal<Marshaller> jaxbMarshaller = ThreadLocal.withInitial(() -> {
        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(SignedServiceMetadata.class, ServiceMetadata.class);
            return jaxbContext.createMarshaller();
        } catch (JAXBException ex) {
            LOG.error("Error occurred while initializing JAXBContext for SignedServiceMetadata. Cause message: ["
                    + ExceptionUtils.getRootCauseMessage(ex) + "]", ex);
        }
        return null;
    });

    private static final QName PARSE_ELEMENT = new QName(PeppolSMPExtension.NAMESPACE, "SignedServiceMetadata");


    public PeppolSMPServiceMetadataReader() {
    }

    public PeppolSMPServiceMetadataReader(boolean ignoreInvalidServices) {
        super(ignoreInvalidServices);
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
    public Marshaller getMarshaller() {
        return jaxbMarshaller.get();
    }

    @Override
    public Unmarshaller getUnmarshaller() {
        return jaxbUnmarshaller.get();
    }

    @Override
    public boolean handles(QName qName, Class<?> clazz) {
        return NamespaceUtil.supportedQNameMatchesProvided(PARSE_ELEMENT, SMPServiceMetadata.class, qName, clazz);
    }

    @Override
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

    @Override
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

    @Override
    protected List<SMPEndpoint> readEndpoints(SignedServiceMetadata serviceMetadata) {

        if (serviceMetadata.getServiceMetadata() == null) {
            LOG.debug("No ServiceMetadata defined for the SignedServiceMetadata.");
            return Collections.emptyList();
        }
        ServiceMetadata smd = serviceMetadata.getServiceMetadata();
        if (smd.getServiceInformation() == null) {
            if (smd.getRedirect() != null) {
                LOG.debug("Parse redirect defined for the SignedServiceMetadata.");
                SMPEndpoint redirect = new SMPEndpoint.Builder()
                        .redirect(readRedirect(smd.getRedirect())).build();
                return Collections.singletonList(redirect);
            }
            LOG.debug("No ServiceInformation defined for the SignedServiceMetadata.");
            return Collections.emptyList();
        }

        if (smd.getServiceInformation().getProcessList() == null ||
                smd.getServiceInformation().getProcessList().getProcesses().isEmpty()) {
            LOG.debug("No endpoint defined for the SignedServiceMetadata.");
            return Collections.emptyList();
        }
        List<ProcessType> processTypes = serviceMetadata.getServiceMetadata().getServiceInformation().getProcessList().getProcesses();
        return processTypes.stream().map(this::readEndpointsForProcess)
                .filter(smpEndpoints -> !smpEndpoints.isEmpty())
                .flatMap(java.util.Collection::stream).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    protected SMPEndpoint readEndpointForProcess(EndpointType endpointType, SMPProcessIdentifier processIdentifier) {

        if (endpointType == null) {
            LOG.debug("Null endpoint type for process [{}]", processIdentifier.getIdentifier());
            return null;
        }

        final String endpointUrl = getEndpointUrl(endpointType);
        if (!(isIgnoreInvalidServices() || isServiceValid(endpointType.getServiceActivationDate(), endpointType.getServiceExpirationDate()))) {
            LOG.debug("Ignore not-active/expired service for process [{}], transport [{}], url [{}]", processIdentifier.getIdentifier(),
                    endpointType.getTransportProfile(), endpointUrl);
            return null;
        }

        X509Certificate certificate = getX509Certificate(endpointType, endpointUrl);
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

    /**
     * Method reads the redirect from Peppol SMP ServiceMetadata and return
     * the SMPRedirect
     *
     * @param redirect
     * @return
     */
    public SMPRedirect readRedirect(RedirectType redirect) {
        if (redirect == null) {
            return null;
        }
        String redirectUrl = redirect.getHref();
        String certificateUID = redirect.getCertificateUID();

        return new SMPRedirect.Builder()
                .redirectUrl(redirectUrl)
                .certificateUID(certificateUID)
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

    protected X509Certificate getX509Certificate(EndpointType endpointType, String addressUrl) {
        if (endpointType == null) {
            LOG.debug("Null endpoint type  [{}] to read the certificate", addressUrl);
            return null;
        }
        final String certificateBase64 = endpointType.getCertificate();
        return getX509CertificateFromBase64(certificateBase64, addressUrl);
    }
}
