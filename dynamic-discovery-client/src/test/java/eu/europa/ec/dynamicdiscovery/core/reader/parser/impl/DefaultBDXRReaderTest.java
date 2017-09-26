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
 *
 */
package eu.europa.ec.dynamicdiscovery.core.reader.parser.impl;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.reader.impl.DefaultBDXRReader;
import eu.europa.ec.dynamicdiscovery.core.security.impl.DefaultSignatureValidator;
import eu.europa.ec.dynamicdiscovery.exception.BindException;
import eu.europa.ec.dynamicdiscovery.model.ServiceGroup;
import eu.europa.ec.dynamicdiscovery.model.ServiceMetadata;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import org.junit.Assert;
import org.junit.Test;

public class DefaultBDXRReaderTest {

    @Test
    public void testGetServiceGroup() throws Exception {
        DefaultBDXRReader defaultBDXRReader = new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts")));
        FetcherResponse fetcherResponse = new FetcherResponse(CommonUtil.getStreamFromXmlFile("service_group_urn_poland_ncpb"));
        ServiceGroup serviceGroup = defaultBDXRReader.getServiceGroup(fetcherResponse);

        Assert.assertNotNull(serviceGroup);
    }

    @Test(expected = BindException.class)
    public void testGetServiceGroupNullContent() throws Exception {
        DefaultBDXRReader defaultBDXRReader = new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts")));
        FetcherResponse fetcherResponse = new FetcherResponse(null);
        ServiceGroup serviceGroup = defaultBDXRReader.getServiceGroup(fetcherResponse);
    }

    @Test
    public void testGetServiceMetadata() throws Exception {
        DefaultBDXRReader defaultBDXRReader = new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts")));
        FetcherResponse fetcherResponse = new FetcherResponse(CommonUtil.getStreamFromXmlFile("signed_service_metadata_invalid_certificate"));
        ServiceMetadata serviceMetadata = defaultBDXRReader.getServiceMetadata(fetcherResponse);

        Assert.assertNotNull(serviceMetadata);
    }

    @Test(expected = BindException.class)
    public void testGetServiceMetadataNotOk() throws Exception {
        DefaultBDXRReader defaultBDXRReader = new DefaultBDXRReader(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts")));
        FetcherResponse fetcherResponse = new FetcherResponse(null);
        ServiceMetadata serviceMetadata = defaultBDXRReader.getServiceMetadata(fetcherResponse);
    }
}
