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

import org.junit.Assert;
import org.junit.Test;

public class DocumentIdentifierTest {

    @Test
    public void checkFullIdentifierNoSchemeTest() throws Exception {
        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn::epsos##services:extended:epsos::107");
        Assert.assertEquals("urn::epsos##services:extended:epsos::107", documentIdentifier.getIdentifier());
        Assert.assertNull(documentIdentifier.getScheme());
        Assert.assertEquals("urn::epsos##services:extended:epsos::107", documentIdentifier.getFullIdentifier());
    }

    @Test
    public void checkFullIdentifierTest() throws Exception {
        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns");
        Assert.assertEquals("urn::epsos##services:extended:epsos::107", documentIdentifier.getIdentifier());
        Assert.assertEquals("ehealth-resid-qns", documentIdentifier.getScheme());
        Assert.assertEquals("ehealth-resid-qns::urn::epsos##services:extended:epsos::107", documentIdentifier.getFullIdentifier());
    }

    @Test
    public void checkEncodedURLTest() throws Exception {
        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns");
        Assert.assertEquals("ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A107", documentIdentifier.urlencoded());
    }

    @Test
    public void checkIdentifierNotCaseManagedTest() throws Exception {
        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns");
        Assert.assertEquals("urn::epsos##services:extended:epsos::107", documentIdentifier.getIdentifier());
        Assert.assertNotEquals("URN::EPSOS##SERVICES:EXTENDED:EPSOS::107", documentIdentifier.getIdentifier());

        documentIdentifier = new DocumentIdentifier("URN::EPSOS##SERVICES:EXTENDED:EPSOS::107", "ehealth-resid-qns");
        Assert.assertNotEquals("urn::epsos##services:extended:epsos::107", documentIdentifier.getIdentifier());
        Assert.assertEquals("URN::EPSOS##SERVICES:EXTENDED:EPSOS::107", documentIdentifier.getIdentifier());
    }

    @Test
    public void checkSchemeNotCaseManagedTest() throws Exception {
        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns");
        Assert.assertEquals("ehealth-resid-qns", documentIdentifier.getScheme());
        Assert.assertNotEquals("EHEALTH-RESID-QNS", documentIdentifier.getScheme());

         documentIdentifier = new DocumentIdentifier("urn::epsos##services:extended:epsos::107", "EHEALTH-RESID-QNS");
        Assert.assertNotEquals("ehealth-resid-qns", documentIdentifier.getScheme());
        Assert.assertEquals("EHEALTH-RESID-QNS", documentIdentifier.getScheme());
    }
}