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
import org.junit.Assert;
import org.junit.Test;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceInformationType;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.SignedServiceMetadata;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class ServiceMetadataTest {

    @Test
    public void addParticipantIdentifierTest() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/response/service_metadata_urn_poland_ncpb.xml");
        FetcherResponse fetcherResponse = new FetcherResponse(inputStream, "http://docs.oasis-open.org/bdxr/ns/SMP/2016/05");
        Object result = unmarshal(fetcherResponse);
        ServiceInformationType serviceInformationType = ((org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceMetadata) result).getServiceInformation();
        ServiceMetadata serviceMetadata = new ServiceMetadata(null, serviceInformationType);
        Assert.assertEquals("ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb", serviceMetadata.getParticipantIdentifier().urlencoded());
        Assert.assertEquals("urn:poland:ncpb", serviceInformationType.getParticipantIdentifier().getValue());
        Assert.assertEquals("ehealth-actorid-qns", serviceInformationType.getParticipantIdentifier().getScheme());
    }

    @Test
    public void addDocumentIdentifierTest() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/response/service_metadata_urn_poland_ncpb.xml");
        FetcherResponse fetcherResponse = new FetcherResponse(inputStream, "http://docs.oasis-open.org/bdxr/ns/SMP/2016/05");
        Object result = unmarshal(fetcherResponse);
        ServiceInformationType serviceInformationType = ((org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceMetadata) result).getServiceInformation();
        ServiceMetadata serviceMetadata = new ServiceMetadata(null, serviceInformationType);
        Assert.assertEquals("ehealth-resid-qns::urn::epsos##services:extended:epsos::107", serviceMetadata.getDocumentIdentifier().getFullIdentifier());
        Assert.assertEquals("urn::epsos##services:extended:epsos::107", serviceInformationType.getDocumentIdentifier().getValue());
        Assert.assertEquals("ehealth-resid-qns", serviceInformationType.getDocumentIdentifier().getScheme());
    }

    @Test
    public void addEndpointTest() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/response/service_metadata_urn_poland_ncpb.xml");
        FetcherResponse fetcherResponse = new FetcherResponse(inputStream, "http://docs.oasis-open.org/bdxr/ns/SMP/2016/05");
        Object result = unmarshal(fetcherResponse);
        ServiceInformationType serviceInformationType = ((org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceMetadata) result).getServiceInformation();
        ServiceMetadata serviceMetadata = new ServiceMetadata(null, serviceInformationType);
        Assert.assertEquals(1, serviceMetadata.getEndpoints().size());
        Assert.assertEquals("urn:epsosPatientService::List", serviceMetadata.getEndpoints().get(0).getProcessIdentifier().getIdentifier());
        Assert.assertEquals(serviceMetadata.getEndpoints().get(0).getProcessIdentifier().getIdentifier(), serviceInformationType.getProcessList().getProcesses().get(0).getProcessIdentifier().getValue());
        Assert.assertEquals(serviceMetadata.getEndpoints().get(0).getProcessIdentifier().getScheme(), serviceInformationType.getProcessList().getProcesses().get(0).getProcessIdentifier().getScheme());
        Assert.assertEquals(serviceMetadata.getEndpoints().size(), serviceInformationType.getProcessList().getProcesses().get(0).getServiceEndpointList().getEndpoints().size());
        Assert.assertEquals(serviceMetadata.getEndpoints().get(0).getTransportProfile().getIdentifier(), serviceInformationType.getProcessList().getProcesses().get(0).getServiceEndpointList().getEndpoints().get(0).getTransportProfile());
        Assert.assertEquals(serviceMetadata.getEndpoints().get(0).getAddress(), serviceInformationType.getProcessList().getProcesses().get(0).getServiceEndpointList().getEndpoints().get(0).getEndpointURI());
        X509Certificate actualCertificate = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(serviceInformationType.getProcessList().getProcesses().get(0).getServiceEndpointList().getEndpoints().get(0).getCertificate()));
        X509Certificate expectedCertificate = serviceMetadata.getEndpoints().get(0).getCertificate();
        Assert.assertEquals(expectedCertificate.getSubjectDN().toString(), actualCertificate.getSubjectDN().toString());
        Assert.assertEquals("ehealth-procid-qns", serviceMetadata.getEndpoints().get(0).getProcessIdentifier().getScheme());
        Assert.assertEquals("urn:ihe:iti:2013:xcpd", serviceMetadata.getEndpoints().get(0).getTransportProfile().getIdentifier());
        Assert.assertEquals("http://poland/ncp/patient/list", serviceMetadata.getEndpoints().get(0).getAddress());
        Assert.assertEquals("CN=IHE Europe CA, O=IHE Europe, C=FR", serviceMetadata.getEndpoints().get(0).getCertificate().getIssuerDN().toString());
    }

    @Test
    public void getEndpointTest() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/response/service_metadata_urn_poland_ncpb.xml");
        FetcherResponse fetcherResponse = new FetcherResponse(inputStream, "http://docs.oasis-open.org/bdxr/ns/SMP/2016/05");
        Object result = unmarshal(fetcherResponse);
        ServiceInformationType serviceInformationType = ((org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceMetadata) result).getServiceInformation();
        ServiceMetadata serviceMetadata = new ServiceMetadata(null, serviceInformationType);
        ProcessIdentifier processIdentifier = new ProcessIdentifier(serviceInformationType.getProcessList().getProcesses().get(0).getProcessIdentifier().getValue(), serviceInformationType.getProcessList().getProcesses().get(0).getProcessIdentifier().getScheme());
        TransportProfile[] transportProfiles = new TransportProfile[]{new TransportProfile(serviceInformationType.getProcessList().getProcesses().get(0).getServiceEndpointList().getEndpoints().get(0).getTransportProfile())};
        Assert.assertEquals(serviceMetadata.getEndpoints().size(), serviceInformationType.getProcessList().getProcesses().get(0).getServiceEndpointList().getEndpoints().size());
        Assert.assertEquals(transportProfiles[0].getIdentifier(), serviceMetadata.getEndpoint(processIdentifier, transportProfiles).getTransportProfile().getIdentifier());
        Assert.assertEquals(processIdentifier.getIdentifier(), serviceMetadata.getEndpoint(processIdentifier, transportProfiles).getProcessIdentifier().getIdentifier());
    }

    public Object unmarshal(FetcherResponse fetcherResponse) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceMetadata.class, SignedServiceMetadata.class);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        Document document = documentBuilderFactory.newDocumentBuilder().parse(fetcherResponse.getInputStream());
        return jaxbContext.createUnmarshaller().unmarshal(document);
    }
}