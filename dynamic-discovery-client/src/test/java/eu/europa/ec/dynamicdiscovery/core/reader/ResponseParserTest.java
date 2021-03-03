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
package eu.europa.ec.dynamicdiscovery.core.reader;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.reader.parser.impl.ServiceGroupResponseParserImpl;
import eu.europa.ec.dynamicdiscovery.core.reader.parser.impl.SignedServiceMetadataResponseParserImpl;
import eu.europa.ec.dynamicdiscovery.core.security.impl.DefaultSignatureValidator;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ServiceGroup;
import eu.europa.ec.dynamicdiscovery.model.ServiceMetadata;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import org.junit.Assert;
import org.junit.Test;

import java.security.KeyStore;
import java.util.List;

/**
 * @author Flávio W. R. Santos
 */
public class ResponseParserTest {

    @Test
    public void parseServiceMetadataTest() throws Exception {
        FetcherResponse fetcherResponse = new FetcherResponse(CommonUtil.getStreamFromXmlFile("signed_service_metadata_urn_poland_ncpb"));
        KeyStore keyStore = CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts");
        SignedServiceMetadataResponseParserImpl responseParser = new SignedServiceMetadataResponseParserImpl( new DefaultSignatureValidator(keyStore));
        ServiceMetadata serviceMetadata = responseParser.getServiceMetadata(fetcherResponse);
        Assert.assertEquals("urn:poland:ncpb", serviceMetadata.getParticipantIdentifier().getIdentifier());
        Assert.assertEquals("ehealth-actorid-qns", serviceMetadata.getParticipantIdentifier().getScheme());
        Assert.assertEquals(1, serviceMetadata.getEndpoints().size());
        Assert.assertEquals("urn:epsosPatientService::List", serviceMetadata.getProcessIdentifiers().get(0).getIdentifier());
        Assert.assertEquals("ehealth-procid-qns", serviceMetadata.getProcessIdentifiers().get(0).getScheme());
        Assert.assertEquals("urn::epsos##services:extended:epsos::107", serviceMetadata.getDocumentIdentifier().getIdentifier());
        Assert.assertEquals("ehealth-resid-qns", serviceMetadata.getDocumentIdentifier().getScheme());
        Assert.assertEquals("ehealth-resid-qns::urn::epsos##services:extended:epsos::107", serviceMetadata.getDocumentIdentifier().getFullIdentifier());
        Assert.assertEquals(CommonUtil.getStringFromXmlFile("signed_service_metadata_urn_poland_ncpb"), serviceMetadata.getResponseBody());
        Assert.assertNotNull(serviceMetadata.getDocumentIdentifier());
    }

    @Test
    public void parseDocumentIdentifierTest() throws Exception {
        FetcherResponse fetcherResponse = new FetcherResponse(CommonUtil.getStreamFromXmlFile("service_group_urn_poland_ncpb"));
        KeyStore keyStore = CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts");
        ServiceGroupResponseParserImpl responseParser = new ServiceGroupResponseParserImpl();
        ServiceGroup serviceGroup = responseParser.getServiceGroup(fetcherResponse);
        List<DocumentIdentifier> documentIdentifiers = serviceGroup.getDocumentIdentifiers();
        Assert.assertEquals(2, documentIdentifiers.size());
        Assert.assertEquals("urn::epsos:services##epsos-21", documentIdentifiers.get(0).getIdentifier());
        Assert.assertEquals("epsos-docid-qns", documentIdentifiers.get(0).getScheme());
        Assert.assertEquals("urn::epsos##services:extended:epsos::107", documentIdentifiers.get(1).getIdentifier());
        Assert.assertEquals("ehealth-resid-qns", documentIdentifiers.get(1).getScheme());
        Assert.assertEquals(CommonUtil.getStringFromXmlFile("service_group_urn_poland_ncpb"), serviceGroup.getResponseBody());
    }

    //BUG EDELIVERY-2484
    @Test
    public void parseServiceMetadataWithEmptyCertificateTest() throws Exception {
        //given
        FetcherResponse fetcherResponse = new FetcherResponse(CommonUtil.getStreamFromXmlFile("signed_service_metadata_empty_certificate"));
        KeyStore keyStore = CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts");
        SignedServiceMetadataResponseParserImpl responseParser = new SignedServiceMetadataResponseParserImpl(new DefaultSignatureValidator(keyStore));

        //when
        ServiceMetadata serviceMetadata = responseParser.getServiceMetadata(fetcherResponse);

        //then
        byte [] certificate = serviceMetadata.getOriginalServiceMetadata().getServiceMetadata().getServiceInformation().getProcessList().getProcess().get(0).getServiceEndpointList().getEndpoint().get(0).getCertificate();
        Assert.assertEquals(0, certificate.length);
    }

    //BUG EDELIVERY-2484
    @Test
    public void parseServiceMetadataWithInvalidCertificateTest() throws Exception {
        //given
        FetcherResponse fetcherResponse = new FetcherResponse(CommonUtil.getStreamFromXmlFile("signed_service_metadata_invalid_certificate"));
        KeyStore keyStore = CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts");
        SignedServiceMetadataResponseParserImpl responseParser = new SignedServiceMetadataResponseParserImpl(new DefaultSignatureValidator(keyStore));

        //when
        ServiceMetadata serviceMetadata = responseParser.getServiceMetadata(fetcherResponse);

        //then
        byte [] certificate = serviceMetadata.getOriginalServiceMetadata().getServiceMetadata().getServiceInformation().getProcessList().getProcess().get(0).getServiceEndpointList().getEndpoint().get(0).getCertificate();
        Assert.assertEquals("base64 encoded invalid certificate content", new String(certificate));
        Assert.assertNull(serviceMetadata.getEndpoints().get(0).getCertificate());
    }
}

