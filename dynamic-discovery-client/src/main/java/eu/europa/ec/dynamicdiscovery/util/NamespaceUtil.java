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
