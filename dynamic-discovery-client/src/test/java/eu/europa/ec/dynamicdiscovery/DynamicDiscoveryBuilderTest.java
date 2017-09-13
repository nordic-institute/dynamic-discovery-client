/*
 * (C) Copyright 2016 - European Commission | Dynamic Discovery Client
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
 *
 * @author Flávio W. R. Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 * @author Erlend Klakegg Bergheim - erlend.klakegg.bergheim@difi.no
 *
 */
package eu.europa.ec.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.core.locator.dns.impl.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.core.locator.impl.DefaultBDXRLocator;
import eu.europa.ec.dynamicdiscovery.core.reader.impl.DefaultBDXRReader;
import eu.europa.ec.dynamicdiscovery.core.security.impl.DefaultSignatureValidator;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import org.junit.Assert;
import org.junit.Test;

public class DynamicDiscoveryBuilderTest {

    @Test
    public void testDefaultParameters() throws Exception {
        DynamicDiscoveryBuilder builder = DynamicDiscoveryBuilder.newInstance();
        DynamicDiscovery smpClient = builder
                .locator(new DefaultBDXRLocator("acc.edelivery.tech.ec.europa.eu"))
                .reader(new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts"))))
                .build();
        Assert.assertNotNull(smpClient);
        Assert.assertNotNull(builder.getService());
        Assert.assertNotNull(builder.getService().getMetadataFetcher());
        Assert.assertNotNull(builder.getService().getMetadataLocator());
        Assert.assertNotNull(builder.getService().getMetadataLocator().getDnsLookup());
        Assert.assertEquals(DefaultDNSLookup.class, builder.getService().getMetadataLocator().getDnsLookup().getClass());
        Assert.assertNotNull(builder.getService().getMetadataProvider());
        Assert.assertNotNull(builder.getService().getMetadataReader());
    }
}
