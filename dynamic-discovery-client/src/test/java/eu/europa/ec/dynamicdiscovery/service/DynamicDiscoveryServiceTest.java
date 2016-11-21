/*
 * (C) Copyright 2016 Dynamic Discovery Client
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
package eu.europa.ec.dynamicdiscovery.service;

import eu.europa.ec.dynamicdiscovery.AbstractTest;
import eu.europa.ec.dynamicdiscovery.DynamicDiscovery;
import eu.europa.ec.dynamicdiscovery.DynamicDiscoveryBuilder;
import eu.europa.ec.dynamicdiscovery.core.locator.DefaultBDXRLocator;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.locator.DefaultDNSLookupMock;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import org.junit.Assert;
import org.junit.Test;

import java.net.URI;

public class DynamicDiscoveryServiceTest extends AbstractTest {

    @Test
    public void metadataLocatorCNAMETest() throws Exception {
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis");
        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("acc.edelivery.tech.ec.europa.eu"))
                .build();
        Assert.assertEquals("http://b-ed520c91b58f3e9f19714d8170aac5af.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu", smpClient.getService().getMetadataLocator().lookup(participantIdentifier).toString());
    }

    @Test
    public void metadataLocatorNAPTRTest() throws Exception {
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis");
        DefaultDNSLookupMock defaultDNSLookup = new DefaultDNSLookupMock();
        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .build();

        Assert.assertEquals("http://smp-mock-1.ehealth.eu:8888", smpClient.getService().getMetadataLocator().lookup(participantIdentifier).toString());
    }

    @Test
    public void metadataProviderForDocumentIdentifiersTest() throws Exception {
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis");
        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", "bdxr-docid-qns");
        DefaultDNSLookupMock defaultDNSLookup = new DefaultDNSLookupMock();
        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .build();

        Assert.assertEquals("http://smp.ec.europa.eu/iso6523-actorid-upis%3A%3A9925%3A0367302178", smpClient.getService().getMetadataProvider().resolveDocumentIdentifiers(new URI("http://smp.ec.europa.eu/"), participantIdentifier).toString());
    }

    @Test
    public void metadataProviderForServiceMetadataTest() throws Exception {
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis");
        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", "bdxr-docid-qns");
        DefaultDNSLookupMock defaultDNSLookup = new DefaultDNSLookupMock();
        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .build();

        Assert.assertEquals("http://smp.ec.europa.eu/iso6523-actorid-upis%3A%3A9925%3A0367302178/services/bdxr-docid-qns%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3ACreditNote-2%3A%3ACreditNote%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns014%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol5a%3Aver2.0%3A%3A2.1", smpClient.getService().getMetadataProvider().resolveServiceMetadata(new URI("http://smp.ec.europa.eu/"), participantIdentifier, documentIdentifier).toString());
    }

    @Test(expected = DNSLookupException.class)
    public void metadataFetcherForServiceMetadataTestNotOk() throws Exception {
        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis");
        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", "bdxr-docid-qns");
        DefaultDNSLookupMock defaultDNSLookup = new DefaultDNSLookupMock();
        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("acc.edelivery.tech.ec.europa.eu", defaultDNSLookup))
                .build();
        URI provider = smpClient.getService().getMetadataProvider().resolveServiceMetadata(new URI("http://smp.ec.europa.eu/"), participantIdentifier, documentIdentifier);
    }
}
