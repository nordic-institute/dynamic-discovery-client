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

public class TransportProfileTest {

    @Test
    public void checkIdentifierNotCaseManagedTest() throws Exception {
        TransportProfile transportProfile = new TransportProfile("urn:ihe:iti:2013:xcpd");
        Assert.assertEquals("urn:ihe:iti:2013:xcpd", transportProfile.getIdentifier());
        Assert.assertNotEquals("URN:IHE:ITI:2013:XCPD", transportProfile.getIdentifier());

        transportProfile = new TransportProfile("URN:IHE:ITI:2013:XCPD");
        Assert.assertNotEquals("urn:ihe:iti:2013:xcpd", transportProfile.getIdentifier());
        Assert.assertEquals("URN:IHE:ITI:2013:XCPD", transportProfile.getIdentifier());
    }
}