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
package eu.europa.ec.dynamicdiscovery.core.reader.impl;

import eu.europa.ec.dynamicdiscovery.core.extension.IExtension;
import eu.europa.ec.dynamicdiscovery.core.extension.IObjectReader;
import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.core.security.SignatureValidationContext;
import eu.europa.ec.dynamicdiscovery.exception.DocumentParseException;
import eu.europa.ec.dynamicdiscovery.exception.DDCRuntimeException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.util.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Abstract  class with common methods for reading XML responses.
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */
public abstract class AbstractXMLResponseReader {
    static final Logger LOG = LoggerFactory.getLogger(AbstractXMLResponseReader.class);

    private static final String DISALLOW_DOCTYPE_FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
    private static final ThreadLocal<DocumentBuilder> threadLocalDocumentBuilder = ThreadLocal.withInitial(() -> createDocumentBuilder());

    public static DocumentBuilder createDocumentBuilder() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
            factory.setFeature(DISALLOW_DOCTYPE_FEATURE, true);
        } catch (ParserConfigurationException e) {
            LOG.warn("DocumentBuilderFactory initialization error. The feature [{}] is not supported by current factory. The feature is ignored.", DISALLOW_DOCTYPE_FEATURE);
        }

        try {
            return factory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            throw new DDCRuntimeException("Can not create new XML Document builder! Error: [" + ExceptionUtils.getRootCauseMessage(ex) + "]", ex);
        }
    }

    public Document parse(FetcherResponse fetcherResponse) throws DocumentParseException {
        byte[] byteArray;
        try {
            byteArray = IOUtils.readResponseData(fetcherResponse);
        } catch (NullPointerException | IOException e) {
            throw new DocumentParseException("Error occurred while retrieving the data!", e);
        }

        try {
            return parse(new ByteArrayInputStream(byteArray));
        } catch (IOException | SAXException e) {
            throw new DocumentParseException("Error occurred while reading the data! " + ExceptionUtils.getRootCauseMessage(e), e);
        }
    }

    /**
     * Read object from the fetcher response input stream. If the document is signed, the signature is validated.
     *
     * @param fetcherResponse - response from the fetcher containing the input stream document.
     * @param targetClazz - the target class of the object to be read. (e.g. SMPServiceGroup.class, SMPServiceMetadata.class)
     * @param extensions -  List of extensions to parse the document to the target object.
     * @param iSignatureValidator - the signature validator to validate the signature of the document.
     * @param context - additional context for the signature validation.
     * @return the instance/object of the targetClazz read from the document.
     * @throws TechnicalException - if an error occurs while reading the object from the document.
     */
    public <T, C> T readObject(FetcherResponse fetcherResponse,
                               Class<T> targetClazz,
                               List<IExtension> extensions,
                               ISignatureValidator iSignatureValidator,
                               SignatureValidationContext context) throws TechnicalException {

        Document document = parse(fetcherResponse);
        QName rootQName = getRootElementQName(document);

        IObjectReader<T, C> parser = getParser(rootQName, targetClazz, extensions);
        if (parser == null) {
            throw new DocumentParseException("No parser registered for the document [" + rootQName + "]");
        }
        return parser.parseAndValidateSignature(document, iSignatureValidator, context);
    }

    /**
     * Get parser for the given root element QName and targetClazz.
     * @param qName
     * @param targetClazz
     * @return the parser for the given root element QName and targetClazz.
     * @param <T> - the target class of the object to be read.
     * @param <C> - the root element type of the object to be read.
     * @param extensions -  List of extensions to read the document to the target object.
     */
    public abstract <T, C> IObjectReader<T, C> getParser(QName qName, Class<T> targetClazz, List<IExtension> extensions);

    protected DocumentBuilder getDocumentBuilder() {
        return threadLocalDocumentBuilder.get();
    }


    /**
     * Parse the input stream to a XML DOM Document.
     * @param inputStream - the input stream to be parsed.
     * @return the XML DOM Document parsed from the input stream.
     * @throws IOException - if an error occurs while reading the input stream.
     * @throws SAXException - if an error occurs while parsing the input stream.
     */
    public Document parse(InputStream inputStream) throws IOException, SAXException {
        DocumentBuilder builder = getDocumentBuilder();
        try {
            return builder.parse(inputStream);
        } finally {
            builder.reset();
        }
    }

    /**
     * Get the qualified name of the root element (QName) of the document.
     * @param document - the document to get the root element QName.
     * @return the root element QName of the document.
     */
    public QName getRootElementQName(Document document) {
        Element element = document.getDocumentElement();
        String namespace = element.getNamespaceURI();
        return new QName(namespace, element.getTagName());
    }
}
