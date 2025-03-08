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
 * Extension interface - to discovery the right parser implementation for XML element (QName) and
 * target clazz.
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */
public interface IExtension {
    Logger LOG = LoggerFactory.getLogger(IExtension.class.getName());


    boolean handles(QName qName, Class<?> clazz);
    <T, C> IObjectReader<T, C> getParser(QName qName, Class<T> clazz);

    /**
     * Method returns ordered list of services which must be used for the lookup.
     * If the list is empty, any service can be used. The returned URL results are used in
     * ordered manner.
     *
     *
     * @return list of services
     */
    List<String> lookupServices();
    String contextPath();
    String subContextPath();
    String getExtensionIdentifier();

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
