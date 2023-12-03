/*-
 * #%L
 * dynamic-discovery-client
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
package eu.europa.ec.dynamicdiscovery.util;

import eu.europa.ec.dynamicdiscovery.core.extension.impl.peppol.PeppolSMPExtension;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceGroup;
import eu.europa.ec.dynamicdiscovery.util.NamespaceUtil;
import org.junit.jupiter.api.Test;

import javax.xml.namespace.QName;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Cosmin Baciu
 * @since 2.1
 */
public class NamespaceUtilTest {

    @Test
    void testSupportedQNameWithoutLocalNamespacePrefix() {
        final QName handled = new QName(PeppolSMPExtension.NAMESPACE, "ServiceGroup");
        QName toCheck = new QName(PeppolSMPExtension.NAMESPACE, "ServiceGroup");
        assertTrue(NamespaceUtil.supportedQNameMatchesProvided(handled, SMPServiceGroup.class, toCheck, SMPServiceGroup.class));
    }

    @Test
    void testSupportedQNameWithLocalNamespacePrefix() {
        final QName handled = new QName(PeppolSMPExtension.NAMESPACE, "ServiceGroup");
        QName toCheck = new QName(PeppolSMPExtension.NAMESPACE, "smp:ServiceGroup");
        assertTrue(NamespaceUtil.supportedQNameMatchesProvided(handled, SMPServiceGroup.class, toCheck, SMPServiceGroup.class));
    }
}
