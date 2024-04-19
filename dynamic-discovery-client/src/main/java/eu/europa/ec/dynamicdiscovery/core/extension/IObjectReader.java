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
package eu.europa.ec.dynamicdiscovery.core.extension;

import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.core.security.SignatureValidationContext;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import org.w3c.dom.Document;

import javax.xml.namespace.QName;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Object implementing this class can read the Document and produces the object <T>
 *
 * @param <T, C>
 * @author Joze Rihtarsic
 * @since 2.0
 */
public interface IObjectReader<T, C> {

    default void serializeNative(C jaxbObject, OutputStream outputStream, boolean prettyPrint) throws TechnicalException{
        serializeNativeAny(jaxbObject, outputStream, prettyPrint);
    }
    default  C parseNative(Document document) throws TechnicalException{
        return (C) parseNativeAny(document);
    }

    default C parseNative(InputStream document) throws TechnicalException{
        return (C) parseNativeAny(document);
    };

    Document objectToDocument(C sourceObject) throws TechnicalException;

    boolean handles(QName qName, Class<?> clazz);

    T parse(Document document) throws TechnicalException;

    Object parseNativeAny(Document document) throws TechnicalException;

    Object parseNativeAny(InputStream document) throws TechnicalException;

    void serializeNativeAny(Object jaxbObject, OutputStream outputStream, boolean prettyPrint) throws TechnicalException;

    T parseAndValidateSignature(Document document, ISignatureValidator signatureValidator, SignatureValidationContext context) throws TechnicalException;
}
