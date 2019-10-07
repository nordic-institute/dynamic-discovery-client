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
package eu.europa.ec.dynamicdiscovery.model;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceInformationType;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.SignedServiceMetadataType;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class ServiceMetadataTest {

    @Test
    public void addParticipantIdentifierTest() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/response/signed_service_metadata_urn_poland_ncpb.xml");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        org.apache.commons.io.IOUtils.copy(inputStream, baos);

        String responseBodyStr = IOUtils.toString(new ByteArrayInputStream(baos.toByteArray()), StandardCharsets.UTF_8);
        FetcherResponse fetcherResponse = new FetcherResponse(new ByteArrayInputStream(baos.toByteArray()));
        SignedServiceMetadataType signedServiceMetadataType = (SignedServiceMetadataType) unmarshal(fetcherResponse);
        ServiceInformationType serviceInformationType = signedServiceMetadataType.getServiceMetadata().getServiceInformation();
        ServiceMetadata serviceMetadata = new ServiceMetadata(signedServiceMetadataType, null, responseBodyStr);
        Assert.assertEquals("ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb", serviceMetadata.getParticipantIdentifier().urlencoded());
        Assert.assertEquals("urn:poland:ncpb", serviceInformationType.getParticipantIdentifier().getValue());
        Assert.assertEquals("ehealth-actorid-qns", serviceInformationType.getParticipantIdentifier().getScheme());
        baos.close();
    }


    @Test
    public void addDocumentIdentifierTest() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/response/signed_service_metadata_urn_poland_ncpb.xml");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        org.apache.commons.io.IOUtils.copy(inputStream, baos);

        String responseBodyStr = IOUtils.toString(new ByteArrayInputStream(baos.toByteArray()), StandardCharsets.UTF_8);
        FetcherResponse fetcherResponse = new FetcherResponse(new ByteArrayInputStream(baos.toByteArray()));
        SignedServiceMetadataType signedServiceMetadataType = (SignedServiceMetadataType) unmarshal(fetcherResponse);
        ServiceMetadata serviceMetadata = new ServiceMetadata(signedServiceMetadataType, null, responseBodyStr);
        Assert.assertEquals("ehealth-resid-qns::urn::epsos##services:extended:epsos::107", serviceMetadata.getDocumentIdentifier().getFullIdentifier());
        Assert.assertEquals("urn::epsos##services:extended:epsos::107", serviceMetadata.getDocumentIdentifier().getIdentifier());
        Assert.assertEquals("ehealth-resid-qns", serviceMetadata.getDocumentIdentifier().getScheme());
        Assert.assertNotNull(StringUtils.isEmpty(serviceMetadata.getResponseBody()));
        baos.close();
    }


    @Test
    public void addDocumentIdentifierEBMSTestServiceText() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/response/signed_service_metadata_EBMS_Test_Service.xml");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        org.apache.commons.io.IOUtils.copy(inputStream, baos);

        String responseBodyStr = IOUtils.toString(new ByteArrayInputStream(baos.toByteArray()), StandardCharsets.UTF_8);
        FetcherResponse fetcherResponse = new FetcherResponse(new ByteArrayInputStream(baos.toByteArray()));
        SignedServiceMetadataType signedServiceMetadataType = (SignedServiceMetadataType) unmarshal(fetcherResponse);
        ServiceMetadata serviceMetadata = new ServiceMetadata(signedServiceMetadataType, null, responseBodyStr);
        Assert.assertEquals("::http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/test", serviceMetadata.getDocumentIdentifier().getFullIdentifier());
        Assert.assertEquals("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/test", serviceMetadata.getDocumentIdentifier().getIdentifier());
        Assert.assertEquals("", serviceMetadata.getDocumentIdentifier().getScheme());
        Assert.assertNotNull(StringUtils.isEmpty(serviceMetadata.getResponseBody()));
        baos.close();
    }

    @Test
    public void addEndpointTest() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/response/signed_service_metadata_urn_poland_ncpb.xml");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        org.apache.commons.io.IOUtils.copy(inputStream, baos);

        String responseBodyStr = IOUtils.toString(new ByteArrayInputStream(baos.toByteArray()), StandardCharsets.UTF_8);
        FetcherResponse fetcherResponse = new FetcherResponse(new ByteArrayInputStream(baos.toByteArray()));
        SignedServiceMetadataType signedServiceMetadataType = (SignedServiceMetadataType) unmarshal(fetcherResponse);
        ServiceInformationType serviceInformationType = signedServiceMetadataType.getServiceMetadata().getServiceInformation();
        ServiceMetadata serviceMetadata = new ServiceMetadata(signedServiceMetadataType, null, responseBodyStr);
        Assert.assertEquals(1, serviceMetadata.getEndpoints().size());
        Assert.assertEquals("urn:epsosPatientService::List", serviceMetadata.getEndpoints().get(0).getProcessIdentifier().getIdentifier());
        Assert.assertEquals(serviceMetadata.getEndpoints().get(0).getProcessIdentifier().getIdentifier(), serviceInformationType.getProcessList().getProcess().get(0).getProcessIdentifier().getValue());
        Assert.assertEquals(serviceMetadata.getEndpoints().get(0).getProcessIdentifier().getScheme(), serviceInformationType.getProcessList().getProcess().get(0).getProcessIdentifier().getScheme());
        Assert.assertEquals(serviceMetadata.getEndpoints().size(), serviceInformationType.getProcessList().getProcess().get(0).getServiceEndpointList().getEndpoint().size());
        Assert.assertEquals(serviceMetadata.getEndpoints().get(0).getTransportProfile().getIdentifier(), serviceInformationType.getProcessList().getProcess().get(0).getServiceEndpointList().getEndpoint().get(0).getTransportProfile());
        Assert.assertEquals(serviceMetadata.getEndpoints().get(0).getAddress(), serviceInformationType.getProcessList().getProcess().get(0).getServiceEndpointList().getEndpoint().get(0).getEndpointURI());
        X509Certificate actualCertificate = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(serviceInformationType.getProcessList().getProcess().get(0).getServiceEndpointList().getEndpoint().get(0).getCertificate()));
        X509Certificate expectedCertificate = serviceMetadata.getEndpoints().get(0).getCertificate();
        Assert.assertEquals(expectedCertificate.getSubjectDN().toString(), actualCertificate.getSubjectDN().toString());
        Assert.assertEquals("ehealth-procid-qns", serviceMetadata.getEndpoints().get(0).getProcessIdentifier().getScheme());
        Assert.assertEquals("urn:ihe:iti:2013:xcpd", serviceMetadata.getEndpoints().get(0).getTransportProfile().getIdentifier());
        Assert.assertEquals("http://poland/ncp/patient/list", serviceMetadata.getEndpoints().get(0).getAddress());
        Assert.assertEquals("CN=IHE Europe CA, O=IHE Europe, C=FR", serviceMetadata.getEndpoints().get(0).getCertificate().getIssuerDN().toString());
        baos.close();
    }

    @Test
    public void addProcessIdentifierEBMSTestServiceTest() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/response/signed_service_metadata_EBMS_Test_Service.xml");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        org.apache.commons.io.IOUtils.copy(inputStream, baos);

        String responseBodyStr = IOUtils.toString(new ByteArrayInputStream(baos.toByteArray()), StandardCharsets.UTF_8);

        FetcherResponse fetcherResponse = new FetcherResponse(new ByteArrayInputStream(baos.toByteArray()));
        SignedServiceMetadataType signedServiceMetadataType = (SignedServiceMetadataType) unmarshal(fetcherResponse);


        ServiceMetadata serviceMetadata = new ServiceMetadata(signedServiceMetadataType, null, responseBodyStr);


        Assert.assertEquals(1, serviceMetadata.getEndpoints().size());
        Assert.assertEquals("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/service",
                serviceMetadata.getEndpoints().get(0).getProcessIdentifier().getIdentifier());
        Assert.assertEquals("", serviceMetadata.getEndpoints().get(0).getProcessIdentifier().getScheme());

        // test other data
        Assert.assertEquals("bdxr-transport-ebms3-as4-v1p0", serviceMetadata.getEndpoints().get(0).getTransportProfile().getIdentifier());
        Assert.assertEquals("https://mypage.eu", serviceMetadata.getEndpoints().get(0).getAddress());
        baos.close();
    }


    @Test
    public void getEndpointTest() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/response/signed_service_metadata_urn_poland_ncpb.xml");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        org.apache.commons.io.IOUtils.copy(inputStream, baos);

        String responseBodyStr = IOUtils.toString(new ByteArrayInputStream(baos.toByteArray()), StandardCharsets.UTF_8);
        FetcherResponse fetcherResponse = new FetcherResponse(new ByteArrayInputStream(baos.toByteArray()));
        SignedServiceMetadataType signedServiceMetadataType = (SignedServiceMetadataType) unmarshal(fetcherResponse);
        ServiceInformationType serviceInformationType = signedServiceMetadataType.getServiceMetadata().getServiceInformation();
        ServiceMetadata serviceMetadata = new ServiceMetadata(signedServiceMetadataType, null, responseBodyStr);
        ProcessIdentifier processIdentifier = new ProcessIdentifier(serviceInformationType.getProcessList().getProcess().get(0).getProcessIdentifier().getValue(), serviceInformationType.getProcessList().getProcess().get(0).getProcessIdentifier().getScheme());
        TransportProfile[] transportProfiles = new TransportProfile[]{new TransportProfile(serviceInformationType.getProcessList().getProcess().get(0).getServiceEndpointList().getEndpoint().get(0).getTransportProfile())};
        Assert.assertEquals(serviceMetadata.getEndpoints().size(), serviceInformationType.getProcessList().getProcess().get(0).getServiceEndpointList().getEndpoint().size());
        Assert.assertEquals(transportProfiles[0].getIdentifier(), serviceMetadata.getEndpoint(processIdentifier, transportProfiles).getTransportProfile().getIdentifier());
        Assert.assertEquals(processIdentifier.getIdentifier(), serviceMetadata.getEndpoint(processIdentifier, transportProfiles).getProcessIdentifier().getIdentifier());
        baos.close();
    }

    public Object unmarshal(FetcherResponse fetcherResponse) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceMetadataType.class, SignedServiceMetadataType.class);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        Document document = documentBuilderFactory.newDocumentBuilder().parse(fetcherResponse.getInputStream());
        return ((JAXBElement) jaxbContext.createUnmarshaller().unmarshal(document)).getValue();
    }
}