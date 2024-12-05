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

import eu.europa.ec.dynamicdiscovery.core.extension.IObjectReader;
import eu.europa.ec.dynamicdiscovery.core.reader.impl.AbstractXMLResponseReader;
import eu.europa.ec.dynamicdiscovery.exception.BindException;
import eu.europa.ec.dynamicdiscovery.exception.SMPExceptionCode;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *  AbstractObjectReader providing the common functionality for the reader objects
 * @param <T> - target type of object
 * @param <C> - source type of object to be parsed
 *
 * @author Joze Rihtarsic
 * @since 2.2
 */
public abstract class AbstractObjectReader<T, C> implements IObjectReader<T, C> {
    // The exception code to target action for the exception
    SMPExceptionCode smpExceptionCode;

    protected AbstractObjectReader(SMPExceptionCode smpExceptionCode) {
        this.smpExceptionCode = smpExceptionCode;
    }

    protected abstract void destroyUnmarshaller();

    protected abstract void destroyMarshaller();

    protected abstract Unmarshaller getUnmarshaller();

    protected abstract Marshaller getMarshaller();

    public void serializeNativeAny(Object jaxbObject, OutputStream outputStream, boolean prettyPrint) throws TechnicalException {
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
            throw new BindException(smpExceptionCode, "Error occurred while serializing the ServiceGroup", e);
        }
    }

    @Override
    public T parse(Document document) throws TechnicalException {
        return parseAndValidateSignature(document, null, null);
    }

    @Override
    public Object parseNativeAny(Document document) throws TechnicalException {
        try {
            return getUnmarshaller().unmarshal(document);
        } catch (JAXBException e) {
            throw new BindException(smpExceptionCode, "Error occurred while parsing the document", e);
        }
    }

    @Override
    public Object parseNativeAny(InputStream inputStream) throws TechnicalException {
        try {
            DocumentBuilder db = AbstractXMLResponseReader.createDocumentBuilder();
            // just to validate DISALLOW_DOCTYPE_FEATURE parse to Document
            Document document = db.parse(inputStream);
            return parseNativeAny(document);
        } catch (SAXException | IOException e) {
            throw new BindException(smpExceptionCode, "Error occurred while SignedServiceMetadata serviceGroup", e);
        }
    }

    public Document objectToDocument(C sourceObject) throws TechnicalException {
        try {
            DocumentBuilder db = AbstractXMLResponseReader.createDocumentBuilder();
            Document document = db.newDocument();
            getMarshaller().marshal(sourceObject, document);
            return document;
        } catch (JAXBException e) {
            throw new BindException(SMPExceptionCode.SERVICE_GROUP, "Error occurred while parsing serviceGroup", e);
        }
    }
}
