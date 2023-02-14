/*
 * (C) Copyright 2016-2021 - European Commission | Dynamic Discovery Client
 *
 * https://ec.europa.eu/cefdigital/code/projects/EDELIVERY/repos/dynamic-discovery-client/browse
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
package eu.europa.ec.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.core.locator.dns.impl.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.core.locator.impl.DefaultBDXRLocator;
import eu.europa.ec.dynamicdiscovery.core.reader.impl.DefaultBDXRReader;
import eu.europa.ec.dynamicdiscovery.core.security.impl.DefaultSignatureValidator;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Flávio W. R. Santos
 * @author Erlend Klakegg Bergheim
 */
class DynamicDiscoveryBuilderTest {

    @Test
   void testDefaultParameters() throws Exception {
        DynamicDiscoveryBuilder builder = DynamicDiscoveryBuilder.newInstance();
        DynamicDiscovery smpClient = builder
                .locator(new DefaultBDXRLocator("acc.edelivery.tech.ec.europa.eu"))
                .reader(new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts"))))
                .build();
        assertNotNull(smpClient);
        assertNotNull(builder.getService());
        assertNotNull(builder.getService().getMetadataFetcher());
        assertNotNull(builder.getService().getMetadataLocator());
        assertNotNull(builder.getService().getMetadataLocator().getDnsLookup());
        assertEquals(DefaultDNSLookup.class, builder.getService().getMetadataLocator().getDnsLookup().getClass());
        assertNotNull(builder.getService().getMetadataProvider());
        assertNotNull(builder.getService().getMetadataReader());
    }
}
