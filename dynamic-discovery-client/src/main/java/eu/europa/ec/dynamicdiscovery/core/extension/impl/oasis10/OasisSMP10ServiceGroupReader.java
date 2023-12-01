/*
 * Copyright 2016-2023 - European Commission | Dynamic Discovery Client
 *
 * https://ec.europa.eu/digital-building-blocks/code/projects/EDELIVERY/repos/dynamic-discovery-client/browse
 *
 * Licensed under the LGPL, Version 2.1 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     dynamic-discovery\License_LGPL-2.1.txt or https://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.europa.ec.dynamicdiscovery.core.extension.impl.oasis10;

import eu.europa.ec.dynamicdiscovery.core.extension.IObjectReader;
import eu.europa.ec.dynamicdiscovery.core.reader.impl.AbstractXMLResponseReader;
import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.exception.BindException;
import eu.europa.ec.dynamicdiscovery.exception.DDCRuntimeException;
import eu.europa.ec.dynamicdiscovery.exception.SMPExceptionCode;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceGroup;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import gen.eu.europa.ec.ddc.api.smp10.ParticipantIdentifierType;
import gen.eu.europa.ec.ddc.api.smp10.ServiceGroup;
import gen.eu.europa.ec.ddc.api.smp10.ServiceMetadataReferenceType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Purpose of the class it to provide the Oasis SMP 1.0 service group parser
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */
public class OasisSMP10ServiceGroupReader implements IObjectReader<SMPServiceGroup> {
    static final Logger LOG = LoggerFactory.getLogger(OasisSMP10ServiceGroupReader.class);
    static final String REFERENCE_DOCUMENT_SEPARATOR = "/services/";
    private static final ThreadLocal<Unmarshaller> jaxbUnmarshaller = ThreadLocal.withInitial(() -> {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ServiceGroup.class);
            return jaxbContext.createUnmarshaller();
        } catch (JAXBException ex) {
            LOG.error("Error occurred while initializing JAXBContext for ServiceGroup. Cause message:" + ex, ex);
        }
        return null;
    });

    private static final ThreadLocal<Marshaller> jaxbMarshaller = ThreadLocal.withInitial(() -> {
        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(ServiceGroup.class);
            return jaxbContext.createMarshaller();
        } catch (JAXBException ex) {
            LOG.error("Error occurred while initializing JAXBContext for ServiceGroup. Cause message:" + ex, ex);
        }
        return null;
    });

    public Unmarshaller getUnmarshaller() {
        return jaxbUnmarshaller.get();
    }

    public Marshaller getMarshaller() {
        return jaxbMarshaller.get();
    }

    private static final QName PARSE_ELEMENT = new QName(OasisSMP10Extension.NAMESPACE, "ServiceGroup");

    /**
     * Removes the current thread's ServiceGroup Unmarshaller for this thread-local variable. If this thread-local variable
     * is subsequently read by the current thread, its value will be reinitialized by invoking its initialValue method.
     */
    public void destroyUnmarshaller() {
        jaxbUnmarshaller.remove();
    }

    public void destroyMarshaller() {
        jaxbMarshaller.remove();
    }

    @Override
    public boolean handles(QName qName, Class<?> clazz) {
        return PARSE_ELEMENT.equals(qName) && clazz == SMPServiceGroup.class;
    }

    @Override
    public SMPServiceGroup parse(Document document) throws TechnicalException {

        ServiceGroup serviceGroup = parseNative(document);
        return new SMPServiceGroup(getParticipantIdentifier(serviceGroup),
                getDocumentIdentifiers(serviceGroup), serviceGroup);
    }

    @Override
    public ServiceGroup parseNative(Document document) throws TechnicalException {

        try {
            return (ServiceGroup) jaxbUnmarshaller.get().unmarshal(document);
        } catch (JAXBException e) {
            LOG.error("Error  type: [{}], to string [{}]", e.getCause().getClass(), ExceptionUtils.getRootCauseMessage(e));
            throw new BindException(SMPExceptionCode.SERVICE_GROUP, "Error occurred while parsing document serviceGroup", e);
        }
    }


    @Override
    public ServiceGroup parseNative(InputStream inputStream) throws TechnicalException {
        try {
            DocumentBuilder db = AbstractXMLResponseReader.createDocumentBuilder();
            // just to validate DISALLOW_DOCTYPE_FEATURE parse to Document
            Document document = db.parse(inputStream);
            return parseNative(document);
        } catch (SAXException | IOException e) {
            LOG.error("Error  type: [{}], to string [{}]", e.getClass(), e);
            throw new BindException(SMPExceptionCode.SERVICE_GROUP, "Error occurred while parsing serviceGroup from input stream", e);
        }
    }

    @Override
    public void serializeNative(Object jaxbObject, OutputStream outputStream, boolean prettyPrint) throws TechnicalException {
        if (jaxbObject == null) {
            return;
        }
        Marshaller marshaller = getMarshaller();
        // Pretty Print XML
        try {
            if (prettyPrint) {
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, prettyPrint);
            }
            // to remove xmlDeclaration
            marshaller.marshal(jaxbObject, outputStream);
        } catch (JAXBException e) {
            throw new BindException(SMPExceptionCode.SERVICE_GROUP, "Error occurred while serializing the ServiceGroup", e);
        }
    }

    @Override
    public SMPServiceGroup parseAndValidateSignature(Document document, ISignatureValidator signatureValidator) throws TechnicalException {
        // the SMP service group is not singed. Ignore signatureValidator
        return parse(document);
    }

    protected SMPParticipantIdentifier getParticipantIdentifier(ServiceGroup serviceGroup) {
        ParticipantIdentifierType identifierType = serviceGroup.getParticipantIdentifier();
        return new SMPParticipantIdentifier(identifierType.getValue(), identifierType.getScheme());
    }

    protected List<SMPDocumentIdentifier> getDocumentIdentifiers(ServiceGroup serviceGroup) {

        if (serviceGroup == null
                || serviceGroup.getServiceMetadataReferenceCollection() == null
                || serviceGroup.getServiceMetadataReferenceCollection().getServiceMetadataReferences() == null) {
            return Collections.emptyList();
        }
        List<ServiceMetadataReferenceType> serviceMetadataReferences =
                serviceGroup.getServiceMetadataReferenceCollection().getServiceMetadataReferences();

        return serviceMetadataReferences.stream()
                .map(this::getDocumentIdentifierFromReference)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    protected SMPDocumentIdentifier getDocumentIdentifierFromReference(ServiceMetadataReferenceType reference) {
        if (reference == null
                || StringUtils.isBlank(reference.getHref())
                || !StringUtils.contains(reference.getHref(), REFERENCE_DOCUMENT_SEPARATOR)) {
            return null;
        }
        String substr = StringUtils.substringAfter(reference.getHref(), REFERENCE_DOCUMENT_SEPARATOR);
        try {
            String[] parts = URLDecoder.decode(substr, "UTF-8").split("::", 2);
            return new SMPDocumentIdentifier(parts[1], parts[0]);
        } catch (UnsupportedEncodingException e) {
            throw new DDCRuntimeException("Error occurred while decoding string [" + substr + "].", e);
        }
    }
}
