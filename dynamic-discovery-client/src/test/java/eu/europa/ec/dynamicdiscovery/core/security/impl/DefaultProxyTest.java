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
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.HttpHost;
import org.junit.Assert;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.*;

/**
 * @author Flávio W. R. Santos
 * @author Sebastian-Ion TINCU
 * @since 1.13
 */
public class DefaultProxyTest {

    @Test
    public void testSetupConstructorWitCredentials() throws Exception {
        DefaultProxy defaultProxy = new DefaultProxy("127.0.0.1", 8000, "user", "password");

        assertNotNull(defaultProxy.getProxyCredentials("127.0.0.1"));
    }

    @Test
    public void testSetupConstructorWithoutCredentials() throws Exception {
        DefaultProxy defaultProxy = new DefaultProxy("127.0.0.1", 8000, null, null);
        assertNull(defaultProxy.getProxyCredentials("127.0.0.1"));
    }

    @Test
    public void testSetupConstructorWithNoProxyHosts() throws Exception {
        DefaultProxy defaultProxy = new DefaultProxy("127.0.0.1", 8000, null, null,"localhost|127.0.0.1");
        assertNull(defaultProxy.getProxyCredentials("127.0.0.1"));
    }

    @Test
    public void testSetupConstructorInvalidPassword() throws Exception {
        testSetupForExceptions("127.0.0.1", 8111, "user", "", "Password for Proxy user is missing.");
    }

    @Test
    public void testSetupConstructorInvalidServerAddress() throws Exception {
        testSetupForExceptions("", 8000, "user", "password", "Server configuration for Proxy Authentication is missing.");
    }

    @Test
    public void testSetupConstructorInvalidServerPort() throws Exception {
        testSetupForExceptions("127.0.0.1", 0, "user", "password", "Server configuration for Proxy Authentication is missing.");
    }

    @Test
    public void testProxyHost() throws Exception {
        DefaultProxy defaultProxy = new DefaultProxy("127.0.0.1", 8000, "user", "password");

        HttpHost proxyHost = defaultProxy.getProxyHost(new URI("dummy.test.ec.eu").getHost());

        assertNotNull(proxyHost);
    }

    @Test
    public void testProxyHostNoProxyFoHost() throws Exception {
        for (String host: new String[]{"dummy.test.ec.eu", "localhost|dummy.test.ec.eu",
                "localhost|dummy.test.ec.eu|127.0.0.1",  "localhost|*.test.ec.eu|127.0.0.1"}) {
            DefaultProxy defaultProxy = new DefaultProxy("127.0.0.1", 8000, "user", "password", host);

            HttpHost proxyHost = defaultProxy.getProxyHost(new URI("http://dummy.test.ec.eu/schema::identifier").getHost());

            assertNull("for nohosts:" + host, proxyHost);
        }
    }

    @Test
    public void testProxyHostNoProxyFoIPAddress() throws Exception {
        for (String host: new String[]{"10.48.0.28", "10.48.0.*","localhost|10.48.0.28",
                "localhost|10.48.0.*|127.0.0.1","localhost|10.48.0.28|127.0.0.1"}) {
            DefaultProxy defaultProxy = new DefaultProxy("127.0.0.1", 8000, "user", "password", host);

            HttpHost proxyHost = defaultProxy.getProxyHost(new URI("http://10.48.0.28/schema::identifier").getHost());

            assertNull("for nohosts:" + host, proxyHost);
        }
    }

    @Test
    public void isNonProxyHost_BlankConfiguration() throws Exception {
        // GIVEN
        String nonProxyHosts = "";
        DefaultProxy defaultProxy = new DefaultProxy("127.0.0.1", 8080, null, null, nonProxyHosts);
        String[] nonProxyHostsField = (String[]) FieldUtils.readField(defaultProxy, "nonProxyHosts", true);
        Assert.assertNull("Non proxy hosts should have been initialised with null", nonProxyHostsField);

        // WHEN
        boolean result = defaultProxy.isNonProxyHost("ec.europa.eu");

        // THEN
        Assert.assertFalse("Should have returned the host as not being ignored by the proxy when the non proxy host configuration is blank", result);
    }

    @Test
    public void isNonProxyHost_MatchRegexConfiguration() throws Exception {
        // GIVEN
        String nonProxyHosts = "*.europa.eu";
        DefaultProxy defaultProxy = new DefaultProxy("127.0.0.1", 8080, null, null, nonProxyHosts);
        String[] nonProxyHostsField = (String[]) FieldUtils.readField(defaultProxy, "nonProxyHosts", true);
        Assert.assertArrayEquals("Non proxy hosts should have been correctly initialised", new String[] {"*.europa.eu"}, nonProxyHostsField);

        // WHEN
        boolean result = defaultProxy.isNonProxyHost("ec.europa.eu");

        // THEN
        Assert.assertTrue("Should have returned the host as being ignored by the proxy when it matches the regex non proxy host configuration", result);
    }

    @Test
    public void isNonProxyHost_MatchNonRegexConfiguration() throws Exception {
        // GIVEN
        String nonProxyHosts = "ec.europa.eu";
        DefaultProxy defaultProxy = new DefaultProxy("127.0.0.1", 8080, null, null, nonProxyHosts);
        String[] nonProxyHostsField = (String[]) FieldUtils.readField(defaultProxy, "nonProxyHosts", true);
        Assert.assertArrayEquals("Non proxy hosts should have been correctly initialised", new String[] {"ec.europa.eu"}, nonProxyHostsField);

        // WHEN
        boolean result = defaultProxy.isNonProxyHost("ec.europa.eu");

        // THEN
        Assert.assertTrue("Should have returned the host as being ignored by the proxy when it matches the non regex non proxy host configuration", result);
    }

    @Test
    public void isNonProxyHost_DoesNotMatchMultipleHostConfiguration() throws Exception {
        // GIVEN
        String nonProxyHosts = "|127.0.0.1||*.testa.eu|europarl.europa.eu";
        DefaultProxy defaultProxy = new DefaultProxy("127.0.0.1", 8080, null, null, nonProxyHosts);
        String[] nonProxyHostsField = (String[]) FieldUtils.readField(defaultProxy, "nonProxyHosts", true);
        Assert.assertArrayEquals("Non proxy hosts should have been correctly initialised",
                new String[] {"", "127.0.0.1", "", "*.testa.eu", "europarl.europa.eu"}, nonProxyHostsField);

        // WHEN
        boolean result = defaultProxy.isNonProxyHost("ec.europa.eu");

        // THEN
        Assert.assertFalse("Should have returned the host as being ignored by the proxy when it doesn't match any of the multiple non proxy hosts configuration", result);
    }

    @Test
    public void getProxyCredentials_nonProxyHost() throws Exception {
        // GIVEN
        String nonProxyHosts = "*.eu";
        DefaultProxy defaultProxy = new DefaultProxy("127.0.0.1", 8080, null, null, nonProxyHosts);
        String[] nonProxyHostsField = (String[]) FieldUtils.readField(defaultProxy, "nonProxyHosts", true);
        Assert.assertArrayEquals("Non proxy hosts should have been correctly initialised", new String[] {"*.eu"}, nonProxyHostsField);

        // WHEN
        CredentialsProvider result = defaultProxy.getProxyCredentials("ec.europa.eu");

        // THEN
        Assert.assertNull("Should have returned no credentials for a target host being ignored by the proxy", result);
    }

    @Test
    public void getProxyCredentials_noUserProvided() throws Exception {
        // GIVEN
        String user = "";
        DefaultProxy defaultProxy = new DefaultProxy("127.0.0.1", 8080, user, "password");

        // WHEN
        CredentialsProvider result = defaultProxy.getProxyCredentials("ec.europa.eu");

        // THEN
        Assert.assertNull("Should have returned no credentials for a proxy being configured without a user", result);
    }

    @Test
    public void getProxyCredentials() throws Exception {
        // GIVEN
        DefaultProxy defaultProxy = new DefaultProxy("127.0.0.1", 8080, "user", "password");

        // WHEN
        CredentialsProvider result = defaultProxy.getProxyCredentials("ec.europa.eu");
        Credentials credentials = result.getCredentials(new AuthScope("127.0.0.1", 8080), null);

        // THEN
        Assert.assertEquals("Should have returned correct principal name for a proxy being configured with user credentials for a target host not being ignored by the proxy",
                "user", credentials.getUserPrincipal().getName());
        Assert.assertEquals("Should have returned correct password for a proxy being configured with user credentials for a target host not being ignored by the proxy",
                "password", new String(credentials.getPassword()));
    }

    private void testSetupForExceptions(String serverAddress, int serverPort, String user, String password, String errorMessage) throws Exception {
        try {
            DefaultProxy defaultProxy = new DefaultProxy(serverAddress, serverPort, user, password);
            fail();
        } catch (Exception exc) {
            Assert.assertEquals(errorMessage, exc.getMessage());
            Assert.assertEquals(ConnectionException.class, exc.getClass());
        }
    }
}
