package eu.europa.ec.dynamicdiscovery.core.extension.impl.peppol;

import eu.europa.ec.dynamicdiscovery.model.SMPServiceGroup;
import org.junit.jupiter.api.Test;

import javax.xml.namespace.QName;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Cosmin Baciu
 * @since 2.1
 */
public class PeppolNamespaceUtilTest {

    @Test
    void testSupportedQNameWithoutLocalNamespacePrefix() {
        final QName handled = new QName(PeppolSMPExtension.NAMESPACE, "ServiceGroup");
        QName toCheck = new QName(PeppolSMPExtension.NAMESPACE, "ServiceGroup");
        assertTrue(PeppolNamespaceUtil.supportedQNameMatchesProvided(handled, SMPServiceGroup.class, toCheck, SMPServiceGroup.class));
    }

    @Test
    void testSupportedQNameWithLocalNamespacePrefix() {
        final QName handled = new QName(PeppolSMPExtension.NAMESPACE, "ServiceGroup");
        QName toCheck = new QName(PeppolSMPExtension.NAMESPACE, "smp:ServiceGroup");
        assertTrue(PeppolNamespaceUtil.supportedQNameMatchesProvided(handled, SMPServiceGroup.class, toCheck, SMPServiceGroup.class));
    }
}
