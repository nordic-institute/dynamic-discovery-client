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

import eu.europa.ec.dynamicdiscovery.enums.DNSLookupType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import java.util.List;

/**
 * Extension interface defining the lookup and parser methods. The extension is used
 * to provide data to the lookup service and to provide  parser implementations for
 * located document.
 *
 * @author Joze RIHTARSIC
 * @since 2.0
 */
public interface IExtension {
    Logger LOG = LoggerFactory.getLogger(IExtension.class.getName());


    /**
     * Method returns true if the extension can handle the given QName and class.
     * @param qName QName of the root element of the document
     * @param clazz class of the target object class to be parsed from the document
     * @return true if the extension can handle the given QName for the target class.
     */
    boolean handles(QName qName, Class<?> clazz);
    <T, C> IObjectReader<T, C> getParser(QName qName, Class<T> clazz);

    /**
     * Method returns ordered list of services which must be used for the lookup.
     * If the list is empty, any service can be used. The returned URL results are used in
     * ordered manner.
     *
     * @return list of services
     */
    List<String> lookupServices();


    /**
     *  Method returns the context path for the support document type. For example for the
     *  Oasis SMP 1.0 the context path is "/" and for the Oasis SMP 2.0 the context path is "/bdxr-smp-2".
     *  The context path is used by the DocumentRequestProvider  to create the document request from lookup results.
     * @return context path for the resource document supported by the extension.
     */
    String contextPath();

    /**
     * Method returns the sub context path for the support document type. For example for the Oasis SMP 1.0/2.0 the sub context path is "/services"
     * The sub context path is used by the tDocumentRequestProvider  to create the document request from lookup results.
     * @return context path for the subresource document supported by the extension.
     */
    String subContextPath();

    /**
     * Method returns the extension identifier. The identifier is used to identify the extension in the configuration.
     * @return extension identifier
     */
    String getExtensionIdentifier();

    /**
     * Method is the default implementation of the lookup service. The method is used to
     * provide the lookup service for the given lookup type and service.
     * @param lookupType lookup type: NAPTR, CNAME, NONE
     * @param service: service name to be used for the lookup: e.g. "Meta:SMP", "oasis-bdxr-smp-2", "Meta:CPPA3"
     * @return true if the lookup service is supported for the extension.
     */
    default boolean isLookupServiceSupported(DNSLookupType lookupType, String service) {
        switch (lookupType) {
            case NAPTR:
                return lookupServices().stream().anyMatch(s -> StringUtils.equalsIgnoreCase(s, service));
            case CNAME:
            case NONE:
                return true;
            default:
                LOG.info("Unknown lookup type: [{}]", lookupType);
                return false;
        }
    }
}
