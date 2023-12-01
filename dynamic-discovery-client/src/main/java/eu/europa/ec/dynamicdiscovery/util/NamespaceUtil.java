/*
 * Copyright 2016-2023 - European Commission | Dynamic Discovery Client
 *
 * https://ec.europa.eu/digital-building-blocks/code/projects/EDELIVERY/repos/dynamic-discovery-client/browse
 *
 * Licensed under the LGPL, Version 2.1 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     dynamic-discovery\License_LGPL-2.1.txt or https://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.europa.ec.dynamicdiscovery.util;

import org.apache.commons.lang3.StringUtils;

import javax.xml.namespace.QName;

/**
 * @author Cosmin Baciu
 * @since 2.1
 */
public class NamespaceUtil {

    private static final String LOCAL_NAMESPACE_SEPARATOR = ":";

    public static boolean supportedQNameMatchesProvided(QName supported, Class<?> clazzSupported, QName qNameToCheck, Class<?> clazzToCheck) {
        final String handledNamespaceURI = supported.getNamespaceURI();
        final String handledLocalPart = supported.getLocalPart();

        String localPartToCheck = qNameToCheck.getLocalPart();
        if(StringUtils.contains(localPartToCheck, LOCAL_NAMESPACE_SEPARATOR)) {
            localPartToCheck = StringUtils.substringAfter(localPartToCheck, LOCAL_NAMESPACE_SEPARATOR);
        }

        final String namespaceURIToCheck = qNameToCheck.getNamespaceURI();
        return handledNamespaceURI.equals(namespaceURIToCheck)
                && handledLocalPart.equals(localPartToCheck)
                && clazzToCheck == clazzSupported;
    }
}
