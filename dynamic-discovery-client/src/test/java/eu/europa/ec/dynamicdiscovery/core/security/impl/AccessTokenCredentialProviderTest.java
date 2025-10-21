/*-
 * #%L
 * dynamic-discovery-client
 * %%
 * Copyright (C) 2016 - 2025 European Commission | eDelivery | Dynamic Discovery Client
 * %%
 * Licensed under the LGPL, Version 2.1 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * [PROJECT_HOME]\license\lgpl2-1\license.txt or https://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package eu.europa.ec.dynamicdiscovery.core.security.impl;

import org.apache.hc.client5.http.auth.Credentials;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccessTokenCredentialProviderTest {

    @Test
    void testConstructorAndGetCredentials() {
        // GIVEN
        String accessTokenName = "testTokenName";
        char[] accessTokenValue = "testTokenValue".toCharArray();

        // WHEN
        AccessTokenCredentialProvider provider = new AccessTokenCredentialProvider(accessTokenName, accessTokenValue);
        Credentials credentials = provider.getCredentials();

        // THEN
        assertNotNull(credentials, "Credentials should not be null");
        assertInstanceOf(UsernamePasswordCredentials.class, credentials, "Credentials should be of type UsernamePasswordCredentials");
        UsernamePasswordCredentials usernamePasswordCredentials = (UsernamePasswordCredentials) credentials;
        assertEquals(accessTokenName, usernamePasswordCredentials.getUserPrincipal().getName(), "Access token name should match");
        assertArrayEquals(accessTokenValue, usernamePasswordCredentials.getPassword(), "Access token value should match");
    }

    @Test
    void testNullAccessTokenValue() {
        // GIVEN
        String accessTokenName = "testTokenName";
        char[] accessTokenValue = null;

        // WHEN
        AccessTokenCredentialProvider provider = new AccessTokenCredentialProvider(accessTokenName, accessTokenValue);
        Credentials credentials = provider.getCredentials();

        // THEN
        assertNotNull(credentials, "Credentials should not be null");
        assertInstanceOf(UsernamePasswordCredentials.class, credentials, "Credentials should be of type UsernamePasswordCredentials");
        UsernamePasswordCredentials usernamePasswordCredentials = (UsernamePasswordCredentials) credentials;
        assertEquals(accessTokenName, usernamePasswordCredentials.getUserPrincipal().getName(), "Access token name should match");
        assertNull(usernamePasswordCredentials.getPassword(), "Access token value should be null");
    }
}
