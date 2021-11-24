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
package eu.europa.ec.dynamicdiscovery.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Flávio W. R. Santos
 */
public class ProcessIdentifierTest {

    @Test
    public void checkEbMS30TestService() throws Exception {
        ProcessIdentifier processIdentifier = new ProcessIdentifier("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/service",null);
        Assert.assertEquals("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/service", processIdentifier.getIdentifier());
        Assert.assertEquals("", processIdentifier.getScheme());
    }

    @Test
    public void checkEbMS30TestServiceEmpty() throws Exception {
        ProcessIdentifier processIdentifier = new ProcessIdentifier("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/service","");
        Assert.assertEquals("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/service", processIdentifier.getIdentifier());
        Assert.assertEquals("", processIdentifier.getScheme());
    }

    @Test
    public void checkFullIdentifierTest() throws Exception {
        ProcessIdentifier processIdentifier = new ProcessIdentifier("urn:www.cenbii.eu:profile:bii05:ver2.0", "cenbii-procid-ubl");
        Assert.assertEquals("urn:www.cenbii.eu:profile:bii05:ver2.0", processIdentifier.getIdentifier());
        Assert.assertEquals("cenbii-procid-ubl", processIdentifier.getScheme());
    }

    @Test
    public void checkIdentifierNotCaseManagedTest() throws Exception {
        ProcessIdentifier processIdentifier = new ProcessIdentifier("urn:www.cenbii.eu:profile:bii05:ver2.0", "cenbii-procid-ubl");
        Assert.assertEquals("urn:www.cenbii.eu:profile:bii05:ver2.0", processIdentifier.getIdentifier());
        Assert.assertNotEquals("URN:WWW.CENBII.EU:PROFILE:BII05:VER2.0", processIdentifier.getIdentifier());

        processIdentifier = new ProcessIdentifier("URN:WWW.CENBII.EU:PROFILE:BII05:VER2.0", "cenbii-procid-ubl");
        Assert.assertNotEquals("urn:www.cenbii.eu:profile:bii05:ver2.0", processIdentifier.getIdentifier());
        Assert.assertEquals("URN:WWW.CENBII.EU:PROFILE:BII05:VER2.0", processIdentifier.getIdentifier());
    }

    @Test
    public void checkSchemeNotCaseManagedTest() throws Exception {
        ProcessIdentifier processIdentifier = new ProcessIdentifier("urn:www.cenbii.eu:profile:bii05:ver2.0", "cenbii-procid-ubl");
        Assert.assertEquals("cenbii-procid-ubl", processIdentifier.getScheme());
        Assert.assertNotEquals("CENBII-PROCID-UBL", processIdentifier.getScheme());

        processIdentifier = new ProcessIdentifier("urn:www.cenbii.eu:profile:bii05:ver2.0", "CENBII-PROCID-UBL");
        Assert.assertNotEquals("cenbii-procid-ubl", processIdentifier.getScheme());
        Assert.assertEquals("CENBII-PROCID-UBL", processIdentifier.getScheme());
    }
}