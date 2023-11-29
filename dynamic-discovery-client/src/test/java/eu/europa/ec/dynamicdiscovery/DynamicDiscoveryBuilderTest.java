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
                .locator(new DefaultBDXRLocator.Builder().addTopDnsDomain("acc.edelivery.tech.ec.europa.eu").build())
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
