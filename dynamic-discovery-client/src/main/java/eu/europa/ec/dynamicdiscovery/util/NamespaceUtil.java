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
