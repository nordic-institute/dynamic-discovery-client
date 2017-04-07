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
package eu.europa.ec.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import org.junit.Assert;
import org.junit.Test;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceGroupType;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.UnmarshalException;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

public class ServiceGroupTest {

    @Test
    public void serviceGroupTestOk() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/response/service_group_urn_poland_ncpb.xml");
        FetcherResponse fetcherResponse = new FetcherResponse(inputStream);
        Object result = unmarshal(fetcherResponse);
        ServiceGroupType serviceGroup = ((org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceGroupType) result);
        Assert.assertNotNull(serviceGroup);
        Assert.assertNotNull(serviceGroup.getServiceMetadataReferenceCollection());
        Assert.assertNotNull(serviceGroup.getServiceMetadataReferenceCollection().getServiceMetadataReference());
        Assert.assertFalse(serviceGroup.getServiceMetadataReferenceCollection().getServiceMetadataReference().isEmpty());
        Assert.assertEquals("ehealth-actorid-qns",serviceGroup.getParticipantIdentifier().getScheme());
        Assert.assertEquals("urn:poland:ncpb",serviceGroup.getParticipantIdentifier().getValue());
        Assert.assertEquals("http://cipa-smp-full-webapp/ehealth-actorid-qns::urn:poland:ncpb/services/epsos-docid-qns%3A%3Aurn%3A%3Aepsos%3Aservices%23%23epsos-21", serviceGroup.getServiceMetadataReferenceCollection().getServiceMetadataReference().get(0).getHref());
    }

    @Test
    public void serviceGroupNoDocumentsTest() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/response/service_group_urn_poland_ncpb_no_document.xml");
        FetcherResponse fetcherResponse = new FetcherResponse(inputStream);
        Object result = unmarshal(fetcherResponse);
        ServiceGroupType serviceGroup = ((org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceGroupType) result);
        Assert.assertNotNull(serviceGroup);
        Assert.assertEquals("ehealth-actorid-qns",serviceGroup.getParticipantIdentifier().getScheme());
        Assert.assertEquals("urn:poland:ncpb",serviceGroup.getParticipantIdentifier().getValue());
        Assert.assertNotNull(serviceGroup.getServiceMetadataReferenceCollection());
        Assert.assertNotNull(serviceGroup.getServiceMetadataReferenceCollection().getServiceMetadataReference());
        Assert.assertTrue(serviceGroup.getServiceMetadataReferenceCollection().getServiceMetadataReference().isEmpty());
    }

    @Test(expected = UnmarshalException.class)
    public void serviceGroupNotValidTest() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/response/service_group_urn_poland_ncpb_not_valid.xml");
        FetcherResponse fetcherResponse = new FetcherResponse(inputStream);
        Object result = unmarshal(fetcherResponse);
        ServiceGroupType serviceGroup = ((org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceGroupType) result);
    }

    public Object unmarshal(FetcherResponse fetcherResponse) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceGroupType.class);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        Document document = documentBuilderFactory.newDocumentBuilder().parse(fetcherResponse.getInputStream());
        return ((JAXBElement)jaxbContext.createUnmarshaller().unmarshal(document)).getValue();
    }
}