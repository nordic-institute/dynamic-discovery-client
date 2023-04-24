package eu.europa.ec.dynamicdiscovery.core.extension.impl;

import eu.europa.ec.dynamicdiscovery.core.extension.IObjectReader;
import eu.europa.ec.dynamicdiscovery.core.reader.impl.AbstractXMLResponseReader;
import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.exception.BindException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceGroup;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import gen.eu.europa.ec.ddc.api.smp20.ServiceGroup;
import gen.eu.europa.ec.ddc.api.smp20.aggregate.ServiceReference;
import gen.eu.europa.ec.ddc.api.smp20.basic.ParticipantID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Purpose of the class it to provide the Oasis SMP 2.0 service group parser
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */
public class OasisSMP20ServiceGroupReader implements IObjectReader<SMPServiceGroup> {
    static final Logger LOG = LoggerFactory.getLogger(OasisSMP20ServiceGroupReader.class);
    private static final ThreadLocal<Unmarshaller> jaxbUnmarshaller = ThreadLocal.withInitial(() -> {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ServiceGroup.class);
            return jaxbContext.createUnmarshaller();
        } catch (JAXBException ex) {
            LOG.error("Error occurred while initializing JAXBContext for ServiceGroup. Cause message:" +  ex, ex);
        }
        return null;
    });

    private static final ThreadLocal<Marshaller> jaxbMarshaller = ThreadLocal.withInitial(() -> {
        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(ServiceGroup.class);
            return jaxbContext.createMarshaller();
        } catch (JAXBException ex) {
            LOG.error("Error occurred while initializing JAXBContext for ServiceGroup. Cause message:" +  ex, ex);
        }
        return null;
    });

    public Marshaller getMarshaller() {
        return jaxbMarshaller.get();
    }

    public Unmarshaller getUnmarshaller() {
        return jaxbUnmarshaller.get();
    }

    private static final QName PARSE_ELEMENT = new QName("http://docs.oasis-open.org/bdxr/ns/SMP/2/ServiceGroup", "ServiceGroup");

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
            throw new BindException("Error occurred while parsing serviceGroup", e);
        }
    }

    public Document objectToDocument(ServiceGroup serviceGroup) throws TechnicalException {
        try {
            DocumentBuilder db = AbstractXMLResponseReader.createDocumentBuilder();
            Document document = db.newDocument();
            getMarshaller().marshal(serviceGroup, document);
            return document;
        } catch (JAXBException e) {
            throw new BindException("Error occurred while parsing serviceGroup", e);
        }
    }

    @Override
    public ServiceGroup parseNative(InputStream inputStream) throws TechnicalException {
        try {
            return (ServiceGroup) jaxbUnmarshaller.get().unmarshal(inputStream);
        } catch (JAXBException e) {
            throw new BindException("Error occurred while parsing serviceGroup", e);
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
            throw new BindException("Error occurred while serializing the ServiceGroup", e);
        }
    }

    @Override
    public SMPServiceGroup parseAndValidateSignature(Document document, ISignatureValidator signatureValidator) throws TechnicalException {
        // the SMP service group is not singed. Ignore signatureValidator
        return parse(document);
    }

    protected SMPParticipantIdentifier getParticipantIdentifier(ServiceGroup serviceGroup) {
        ParticipantID identifier = serviceGroup.getParticipantID();
        return identifier == null ? null : new SMPParticipantIdentifier(identifier.getValue(), identifier.getSchemeID());
    }

    /**
     * Method reads the Oasis SMP 2.0 Service group and returns all the DocumentIdentifiers
     *
     * @param serviceGroup the Oasis SMP 2.0 Service group
     * @return List of SMPDocumentIdentifier
     */
    protected List<SMPDocumentIdentifier> getDocumentIdentifiers(ServiceGroup serviceGroup) {
        if (serviceGroup == null || serviceGroup.getServiceReferences() == null) {
            return Collections.emptyList();
        }
        return serviceGroup.getServiceReferences().stream()
                .map(ServiceReference::getID)
                .filter(Objects::nonNull)
                .map(id -> new SMPDocumentIdentifier(id.getValue(), id.getSchemeID()))
                .collect(Collectors.toList());
    }
}
