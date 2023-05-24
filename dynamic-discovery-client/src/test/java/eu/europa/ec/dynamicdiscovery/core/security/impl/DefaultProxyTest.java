/*
 * (C) Copyright 2020 - European Commission | Dynamic Discovery Client
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
package eu.europa.ec.dynamicdiscovery.core.security.impl;

import eu.europa.ec.dynamicdiscovery.exception.ConnectionException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.Credentials;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.core5.http.HttpHost;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Flávio W. R. Santos
 * @author Sebastian-Ion TINCU
 * @since 1.13
 */
class DefaultProxyTest {

    @Test
    void testSetupConstructorWitCredentials() throws Exception {
        DefaultProxy defaultProxy = new DefaultProxy("127.0.0.1", 8000, "user", "password");

        assertNotNull(defaultProxy.getProxyCredentials("127.0.0.1"));
    }

    @Test
    void testSetupConstructorWithoutCredentials() throws Exception {
        DefaultProxy defaultProxy = new DefaultProxy("127.0.0.1", 8000, null, null);
        assertNull(defaultProxy.getProxyCredentials("127.0.0.1"));
    }

    @Test
    void testSetupConstructorWithNoProxyHosts() throws Exception {
        DefaultProxy defaultProxy = new DefaultProxy("127.0.0.1", 8000, null, null, "localhost|127.0.0.1");
        assertNull(defaultProxy.getProxyCredentials("127.0.0.1"));
    }

    @Test
    void testSetupConstructorInvalidPassword() throws Exception {
        testSetupForExceptions("127.0.0.1", 8111, "user", "", "Password for Proxy user is missing.");
    }

    @Test
    void testSetupConstructorInvalidServerAddress() throws Exception {
        testSetupForExceptions("", 8000, "user", "password", "Server configuration for Proxy Authentication is missing.");
    }

    @Test
    void testSetupConstructorInvalidServerPort() throws Exception {
        testSetupForExceptions("127.0.0.1", 0, "user", "password", "Server configuration for Proxy Authentication is missing.");
    }

    @Test
    void testProxyHost() throws Exception {
        DefaultProxy defaultProxy = new DefaultProxy("127.0.0.1", 8000, "user", "password");

        HttpHost proxyHost = defaultProxy.getProxyHost(new URI("dummy.test.ec.eu").getHost());

        assertNotNull(proxyHost);
    }

    @Test
    void testProxyHostNoProxyFoHost() throws Exception {
        for (String host : new String[]{"dummy.test.ec.eu", "localhost|dummy.test.ec.eu",
                "localhost|dummy.test.ec.eu|127.0.0.1", "localhost|*.test.ec.eu|127.0.0.1"}) {
            DefaultProxy defaultProxy = new DefaultProxy("127.0.0.1", 8000, "user", "password", host);

            HttpHost proxyHost = defaultProxy.getProxyHost(new URI("http://dummy.test.ec.eu/schema::identifier").getHost());

            assertNull(proxyHost);
        }
    }

    @Test
    void testProxyHostNoProxyFoIPAddress() throws Exception {
        for (String host : new String[]{"10.48.0.28", "10.48.0.*", "localhost|10.48.0.28",
                "localhost|10.48.0.*|127.0.0.1", "localhost|10.48.0.28|127.0.0.1"}) {
            DefaultProxy defaultProxy = new DefaultProxy("127.0.0.1", 8000, "user", "password", host);

            HttpHost proxyHost = defaultProxy.getProxyHost(new URI("http://10.48.0.28/schema::identifier").getHost());

            assertNull(proxyHost);
        }
    }

    @Test
    void isNonProxyHost_BlankConfiguration() throws Exception {
        // GIVEN
        String nonProxyHosts = "";
        DefaultProxy defaultProxy = new DefaultProxy("127.0.0.1", 8080, null, null, nonProxyHosts);
        String[] nonProxyHostsField = (String[]) FieldUtils.readField(defaultProxy, "nonProxyHosts", true);
        assertNull(nonProxyHostsField);

        // WHEN
        boolean result = defaultProxy.isNonProxyHost("ec.europa.eu");

        // THEN
        assertFalse(result);
    }

    @Test
    void isNonProxyHost_MatchRegexConfiguration() throws Exception {
        // GIVEN
        String nonProxyHosts = "*.europa.eu";
        DefaultProxy defaultProxy = new DefaultProxy("127.0.0.1", 8080, null, null, nonProxyHosts);
        String[] nonProxyHostsField = (String[]) FieldUtils.readField(defaultProxy, "nonProxyHosts", true);
        assertArrayEquals(new String[]{"*.europa.eu"}, nonProxyHostsField);

        // WHEN
        boolean result = defaultProxy.isNonProxyHost("ec.europa.eu");

        // THEN
        assertTrue(result);
    }

    @Test
    void isNonProxyHost_MatchNonRegexConfiguration() throws Exception {
        // GIVEN
        String nonProxyHosts = "ec.europa.eu";
        DefaultProxy defaultProxy = new DefaultProxy("127.0.0.1", 8080, null, null, nonProxyHosts);
        String[] nonProxyHostsField = (String[]) FieldUtils.readField(defaultProxy, "nonProxyHosts", true);
        assertArrayEquals(new String[]{"ec.europa.eu"}, nonProxyHostsField);

        // WHEN
        boolean result = defaultProxy.isNonProxyHost("ec.europa.eu");

        // THEN
        assertTrue(result);
    }

    @Test
    void isNonProxyHost_DoesNotMatchMultipleHostConfiguration() throws Exception {
        // GIVEN
        String nonProxyHosts = "|127.0.0.1||*.testa.eu|europarl.europa.eu";
        DefaultProxy defaultProxy = new DefaultProxy("127.0.0.1", 8080, null, null, nonProxyHosts);
        String[] nonProxyHostsField = (String[]) FieldUtils.readField(defaultProxy, "nonProxyHosts", true);
        assertArrayEquals(
                new String[]{"", "127.0.0.1", "", "*.testa.eu", "europarl.europa.eu"}, nonProxyHostsField);

        // WHEN
        boolean result = defaultProxy.isNonProxyHost("ec.europa.eu");

        // THEN
        assertFalse(result);
    }

    @Test
    void getProxyCredentials_nonProxyHost() throws Exception {
        // GIVEN
        String nonProxyHosts = "*.eu";
        DefaultProxy defaultProxy = new DefaultProxy("127.0.0.1", 8080, null, null, nonProxyHosts);
        String[] nonProxyHostsField = (String[]) FieldUtils.readField(defaultProxy, "nonProxyHosts", true);
        assertArrayEquals(new String[]{"*.eu"}, nonProxyHostsField);

        // WHEN
        CredentialsProvider result = defaultProxy.getProxyCredentials("ec.europa.eu");

        // THEN
        assertNull(result);
    }

    @Test
    void getProxyCredentials_noUserProvided() throws Exception {
        // GIVEN
        String user = "";
        DefaultProxy defaultProxy = new DefaultProxy("127.0.0.1", 8080, user, "password");

        // WHEN
        CredentialsProvider result = defaultProxy.getProxyCredentials("ec.europa.eu");

        // THEN
        assertNull(result);
    }

    @Test
    void getProxyCredentials() throws Exception {
        // GIVEN
        DefaultProxy defaultProxy = new DefaultProxy("127.0.0.1", 8080, "user", "password");

        // WHEN
        CredentialsProvider result = defaultProxy.getProxyCredentials("ec.europa.eu");
        Credentials credentials = result.getCredentials(new AuthScope("127.0.0.1", 8080), null);

        // THEN
        assertEquals("user", credentials.getUserPrincipal().getName(),
                "Should have returned correct principal name for a proxy being configured with user credentials for a target host not being ignored by the proxy");
        assertEquals("password", new String(credentials.getPassword()),
                "Should have returned correct password for a proxy being configured with user credentials for a target host not being ignored by the proxy");
    }

    private void testSetupForExceptions(String serverAddress, int serverPort, String user, String password, String errorMessage) throws Exception {
        try {
            DefaultProxy defaultProxy = new DefaultProxy(serverAddress, serverPort, user, password);
            fail();
        } catch (Exception exc) {
            assertEquals(errorMessage, exc.getMessage());
            assertEquals(ConnectionException.class, exc.getClass());
        }
    }
}
