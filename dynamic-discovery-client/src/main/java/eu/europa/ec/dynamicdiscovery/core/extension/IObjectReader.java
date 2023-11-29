/*
 * Copyright 2017-2023 European Commission | eDelivery Dynamic Discovery Client
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence attached in file: LICENSE-EUPL-v1.2-EN.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 */
package eu.europa.ec.dynamicdiscovery.core.extension;

import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import org.w3c.dom.Document;
import javax.xml.namespace.QName;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Object implementing this class can read the Document and produces the object <T>
 *
 * @param <T>
 * @author Joze Rihtarsic
 * @since 2.0
 */
public interface IObjectReader<T> {

    boolean handles(QName qName, Class<?> clazz);

    T parse(Document document) throws TechnicalException;

    Object parseNative(Document document) throws TechnicalException;

    Object parseNative(InputStream document) throws TechnicalException;

    public void serializeNative(Object jaxbObject, OutputStream outputStream, boolean prettyPrint) throws TechnicalException;

    T parseAndValidateSignature(Document document, ISignatureValidator signatureValidator) throws TechnicalException;
}
