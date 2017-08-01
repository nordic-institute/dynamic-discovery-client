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
package eu.europa.ec.dynamicdiscovery.reader;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.reader.parser.ServiceGroupResponseParser;
import eu.europa.ec.dynamicdiscovery.core.reader.parser.SignedServiceMetadataResponseParser;
import eu.europa.ec.dynamicdiscovery.core.security.impl.DefaultSignatureValidator;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ServiceMetadata;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import org.junit.Assert;
import org.junit.Test;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.SignedServiceMetadataType;

import java.security.KeyStore;
import java.util.List;

public class ResponseParserTest {

    @Test
    public void parseServiceMetadataTest() throws Exception {
        FetcherResponse fetcherResponse = new FetcherResponse(CommonUtil.getStreamFromXmlFile("service_metadata_urn_poland_ncpb"));
        KeyStore keyStore = CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts");
        SignedServiceMetadataResponseParser responseParser = new SignedServiceMetadataResponseParser(new DefaultSignatureValidator(keyStore));
        ServiceMetadata serviceMetadata = responseParser.parseServiceMetadata(fetcherResponse);
        Assert.assertEquals("urn:poland:ncpb", serviceMetadata.getParticipantIdentifier().getIdentifier());
        Assert.assertEquals("ehealth-actorid-qns", serviceMetadata.getParticipantIdentifier().getScheme());
        Assert.assertEquals(1, serviceMetadata.getEndpoints().size());
        Assert.assertEquals("urn:epsosPatientService::List", serviceMetadata.getProcessIdentifiers().get(0).getIdentifier());
        Assert.assertEquals("ehealth-procid-qns", serviceMetadata.getProcessIdentifiers().get(0).getScheme());
        Assert.assertEquals("urn::epsos##services:extended:epsos::107", serviceMetadata.getDocumentIdentifier().getIdentifier());
        Assert.assertEquals("ehealth-resid-qns", serviceMetadata.getDocumentIdentifier().getScheme());
        Assert.assertEquals("ehealth-resid-qns::urn::epsos##services:extended:epsos::107", serviceMetadata.getDocumentIdentifier().getFullIdentifier());
        Assert.assertNotNull(serviceMetadata.getDocumentIdentifier());
    }

    @Test
    public void parseDocumentIdentifierTest() throws Exception {
        FetcherResponse fetcherResponse = new FetcherResponse(CommonUtil.getStreamFromXmlFile("service_group_urn_poland_ncpb"));
        KeyStore keyStore = CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts");
        ServiceGroupResponseParser responseParser = new ServiceGroupResponseParser(new DefaultSignatureValidator(keyStore));
        List<DocumentIdentifier> documentIdentifiers = responseParser.parseDocumentIdentifier(fetcherResponse);
        Assert.assertEquals(2, documentIdentifiers.size());
        Assert.assertEquals("urn::epsos:services##epsos-21", documentIdentifiers.get(0).getIdentifier());
        Assert.assertEquals("epsos-docid-qns", documentIdentifiers.get(0).getScheme());
        Assert.assertEquals("urn::epsos##services:extended:epsos::107", documentIdentifiers.get(1).getIdentifier());
        Assert.assertEquals("ehealth-resid-qns", documentIdentifiers.get(1).getScheme());
    }
}

