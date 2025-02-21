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

import eu.europa.ec.dynamicdiscovery.core.extension.IObjectReader;
import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.core.security.SignatureValidationContext;
import eu.europa.ec.dynamicdiscovery.exception.BindException;
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

/**
 * Abstract  class with common methods for reading XML responses.
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */
public abstract class AbstractXMLResponseReader {
    static final Logger LOG = LoggerFactory.getLogger(AbstractXMLResponseReader.class);

    private static final String DISALLOW_DOCTYPE_FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
    private static final ThreadLocal<DocumentBuilder> threadLocalDocumentBuilder = ThreadLocal.withInitial(AbstractXMLResponseReader::createDocumentBuilder);

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

    public Document parse(FetcherResponse fetcherResponse) throws BindException {
        byte[] byteArray;
        try {
            byteArray = IOUtils.readResponseData(fetcherResponse);
        } catch (NullPointerException | IOException e) {
            throw new BindException("Error occurred while retrieving the data!", e);
        }

        try {
            return parse(new ByteArrayInputStream(byteArray));
        } catch (IOException | SAXException e) {
            throw new BindException("Error occurred while reading the data! " + ExceptionUtils.getRootCauseMessage(e), e);
        }
    }

    public <T> T readObject(FetcherResponse fetcherResponse, Class<T> clazz, ISignatureValidator iSignatureValidator) throws TechnicalException {
        return readObject(fetcherResponse, clazz, iSignatureValidator, null);
    }

    public <T, C> T readObject(FetcherResponse fetcherResponse, Class<T> clazz, ISignatureValidator iSignatureValidator, SignatureValidationContext context) throws TechnicalException {

        Document document = parse(fetcherResponse);
        QName rootQName = getRootElementQName(document);

        IObjectReader<T, C> parser = getParser(rootQName, clazz);
        if (parser == null) {
            throw new BindException("No parser registered for the document [" + rootQName + "]");
        }
        return parser.parseAndValidateSignature(document, iSignatureValidator, context);
    }

    public abstract <T, C> IObjectReader<T, C> getParser(QName qName, Class<T> clazz);

    protected DocumentBuilder getDocumentBuilder() {
        return threadLocalDocumentBuilder.get();
    }

    /**\
     * Method enables unload of the thread local document builder at the end of the thread.
     */
    public void unload() {
        threadLocalDocumentBuilder.remove();
    }


    public Document parse(InputStream inputStream) throws IOException, SAXException {
        DocumentBuilder builder = getDocumentBuilder();
        try {
            return builder.parse(inputStream);
        } finally {
            builder.reset();
        }
    }

    public QName getRootElementQName(Document document) {
        Element element = document.getDocumentElement();
        String namespace = element.getNamespaceURI();
        return new QName(namespace, element.getTagName());
    }
}
