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

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import gen.eu.europa.ec.ddc.api.smp10.ServiceGroup;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.UnmarshalException;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Flávio W. R. Santos
 */
class ServiceGroupTest {

    @Test
    void serviceGroupTestOk() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/response/oasis-smp-1.0/service_group_urn_poland_ncpb.xml");
        FetcherResponse fetcherResponse = new FetcherResponse(inputStream);
        Object result = unmarshal(fetcherResponse);
        ServiceGroup serviceGroup = ((gen.eu.europa.ec.ddc.api.smp10.ServiceGroup) result);
        assertNotNull(serviceGroup);
        assertNotNull(serviceGroup.getServiceMetadataReferenceCollection());
        assertNotNull(serviceGroup.getServiceMetadataReferenceCollection().getServiceMetadataReferences());
        assertFalse(serviceGroup.getServiceMetadataReferenceCollection().getServiceMetadataReferences().isEmpty());
        assertEquals("ehealth-actorid-qns", serviceGroup.getParticipantIdentifier().getScheme());
        assertEquals("urn:poland:ncpb", serviceGroup.getParticipantIdentifier().getValue());
        assertEquals("http://cipa-smp-full-webapp/ehealth-actorid-qns::urn:poland:ncpb/services/epsos-docid-qns%3A%3Aurn%3A%3Aepsos%3Aservices%23%23epsos-21", serviceGroup.getServiceMetadataReferenceCollection().getServiceMetadataReferences().get(0).getHref());
    }

    @Test
    void serviceGroupNoDocumentsTest() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/response/oasis-smp-1.0/service_group_urn_poland_ncpb_no_document.xml");
        FetcherResponse fetcherResponse = new FetcherResponse(inputStream);
        Object result = unmarshal(fetcherResponse);
        ServiceGroup serviceGroup = ((gen.eu.europa.ec.ddc.api.smp10.ServiceGroup) result);
        assertNotNull(serviceGroup);
        assertEquals("ehealth-actorid-qns", serviceGroup.getParticipantIdentifier().getScheme());
        assertEquals("urn:poland:ncpb", serviceGroup.getParticipantIdentifier().getValue());
        assertNotNull(serviceGroup.getServiceMetadataReferenceCollection());
        assertNotNull(serviceGroup.getServiceMetadataReferenceCollection().getServiceMetadataReferences());
        assertTrue(serviceGroup.getServiceMetadataReferenceCollection().getServiceMetadataReferences().isEmpty());
    }

    @Test
    void serviceGroupNotValidTest() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/response/oasis-smp-1.0/service_group_urn_poland_ncpb_not_valid.xml");
        FetcherResponse fetcherResponse = new FetcherResponse(inputStream);

        UnmarshalException result = assertThrows(UnmarshalException.class, () -> unmarshal(fetcherResponse));
        MatcherAssert.assertThat(result.getMessage(), CoreMatchers.containsString("unexpected element"));
    }

    public Object unmarshal(FetcherResponse fetcherResponse) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(gen.eu.europa.ec.ddc.api.smp10.ServiceGroup.class);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        Document document = documentBuilderFactory.newDocumentBuilder().parse(fetcherResponse.getInputStream());
        return jaxbContext.createUnmarshaller().unmarshal(document);
    }
}
