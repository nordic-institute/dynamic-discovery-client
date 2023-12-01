/*
 * (C) Copyright 2016-2023 - European Commission | Dynamic Discovery Client
 *
 * https://ec.europa.eu/digital-building-blocks/code/projects/EDELIVERY/repos/dynamic-discovery-client/browse
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
package eu.europa.ec.dynamicdiscovery.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Flávio W. R. Santos
 * @author Adrien Ferial
 */
class HashUtilTest {

    @Test
   void testNAPTRHash() throws Exception {
        String participantId = HashUtil.getSHA256HashBase32("urn:poland:ncpb");
        assertEquals("DALXFO3CDYE5ZSLF5WAVCYQ3XGERI6ONUBJU5WAH3T77THFWCGEQ", participantId);
    }

    @Test
   void testCNAMEHash() throws Exception {
        String participantId = HashUtil.getMD5Hash("urn:poland:ncpb");
        assertEquals("b-adb4c6d3821d142c684b13ed269fad65", "b-" + participantId);
    }
}
