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
package eu.europa.ec.dynamicdiscovery.core.extension.impl.oasis10;

import eu.europa.ec.dynamicdiscovery.core.extension.impl.AbstractServiceGroupReader;
import eu.europa.ec.dynamicdiscovery.exception.DDCRuntimeException;
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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
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
public class OasisSMP10ServiceGroupReader extends AbstractServiceGroupReader<ServiceGroup> {
    static final Logger LOG = LoggerFactory.getLogger(OasisSMP10ServiceGroupReader.class);
    static final String REFERENCE_DOCUMENT_SEPARATOR = "/services/";
    private static final ThreadLocal<Unmarshaller> jaxbUnmarshaller = ThreadLocal.withInitial(() -> {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ServiceGroup.class);
            return jaxbContext.createUnmarshaller();
        } catch (JAXBException ex) {
            LOG.error("Error occurred while initializing JAXBContext for ServiceGroup. Cause message: ["
                    + ExceptionUtils.getRootCauseMessage(ex) + "]", ex);
        }
        return null;
    });

    private static final ThreadLocal<Marshaller> jaxbMarshaller = ThreadLocal.withInitial(() -> {
        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(ServiceGroup.class);
            return jaxbContext.createMarshaller();
        } catch (JAXBException ex) {
            LOG.error("Error occurred while initializing JAXBContext for ServiceGroup. Cause message: ["
                    + ExceptionUtils.getRootCauseMessage(ex) + "]", ex);
        }
        return null;
    });

    private static final QName PARSE_ELEMENT = new QName(OasisSMP10Extension.NAMESPACE, "ServiceGroup");

    @Override
    public Unmarshaller getUnmarshaller() {
        return jaxbUnmarshaller.get();
    }

    @Override
    public Marshaller getMarshaller() {
        return jaxbMarshaller.get();
    }


    /**
     * Removes the current thread's ServiceGroup Unmarshaller for this thread-local variable. If this thread-local variable
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
    public boolean handles(QName qName, Class<?> clazz) {
        return PARSE_ELEMENT.equals(qName) && clazz == SMPServiceGroup.class;
    }

    @Override
    protected SMPParticipantIdentifier getParticipantIdentifier(ServiceGroup serviceGroup) {
        ParticipantIdentifierType identifierType = serviceGroup.getParticipantIdentifier();
        return new SMPParticipantIdentifier(identifierType.getValue(), identifierType.getScheme());
    }

    @Override
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
