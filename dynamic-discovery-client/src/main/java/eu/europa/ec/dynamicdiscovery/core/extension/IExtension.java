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

import javax.xml.namespace.QName;

/**
 * Extension interface - to discovery the right parser implementation for XML element (QName) and
 * target clazz.
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */
public interface IExtension {
    boolean handles(QName qName, Class<?> clazz);

    <T> IObjectReader<T> getParser(QName qName, Class<T> clazz);
}
