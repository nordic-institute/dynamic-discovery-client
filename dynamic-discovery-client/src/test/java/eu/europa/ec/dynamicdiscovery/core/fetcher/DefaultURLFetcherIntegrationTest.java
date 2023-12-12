/*
 * #%L
 * dynamic-discovery-cli
 * %%
 * Copyright (C) 2016 - 2023 European Commission | eDelivery | Dynamic Discovery Client
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
package eu.europa.ec.dynamicdiscovery.core.fetcher;


import eu.europa.ec.dynamicdiscovery.core.fetcher.impl.DefaultURLFetcher;
import eu.europa.ec.dynamicdiscovery.exception.ConnectionException;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.security.KeyStore;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DefaultURLFetcherIntegrationTest {
    private static String PASSWD = "test1234";
    private static String KEYSTORE_TYPE = "PKCS12";


    private static Server serverHTTP;
    private static URI serverHTTPUri;
    private static URI serverHTTPSUri;

    @BeforeAll
    public static void startHTTPJetty() throws Exception {
        // Create Server
        serverHTTP = new Server();
        // configure http
        ServerConnector httpConnector = new ServerConnector(serverHTTP);
        httpConnector.setPort(0); // auto-bind to available port
        serverHTTP.addConnector(httpConnector);
        // --------------------------------------------------
        // configure https

        SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
        sslContextFactory.setNeedClientAuth(true);
        sslContextFactory.setExcludeProtocols("TLSv1.1");
        sslContextFactory.setKeyStorePath("src/test/resources/truststore/server-keystore.p12");
        sslContextFactory.setKeyStorePassword(PASSWD);
        sslContextFactory.setKeyManagerPassword(PASSWD);
        sslContextFactory.setTrustStorePath("src/test/resources/truststore/tls-truststore.p12");
        sslContextFactory.setTrustStorePassword(PASSWD);
        sslContextFactory.setCertAlias("localhost");
        sslContextFactory.setIncludeCipherSuites(
                "TLS_RSA_WITH_AES_256_CBC_SHA256",
                "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
                "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256");


        /*sslContextFactory.setExcludeCipherSuites("SSL_RSA_WITH_DES_CBC_SHA",
                "SSL_DHE_RSA_WITH_DES_CBC_SHA", "SSL_DHE_DSS_WITH_DES_CBC_SHA",
                "SSL_RSA_EXPORT_WITH_RC4_40_MD5",
                "SSL_RSA_EXPORT_WITH_DES40_CBC_SHA",
                "SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA",
                "SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA");*/

        // SSL HTTP Configuration
        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setSecureScheme("https");


        HttpConfiguration https_config = new HttpConfiguration(http_config);
        https_config.addCustomizer(new SecureRequestCustomizer());
        ServerConnector sslConnector = new ServerConnector(serverHTTP,
                new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
                new HttpConnectionFactory(https_config));
        sslConnector.setPort(0);

        serverHTTP.addConnector(sslConnector);


        // add context handler
        ServletContextHandler context = new ServletContextHandler();
        ServletHolder defaultServ = new ServletHolder("default", DefaultServlet.class);
        defaultServ.setInitParameter("resourceBase", "./target/test-classes/response/");
        defaultServ.setInitParameter("dirAllowed", "true");
        context.addServlet(defaultServ, "/");
        serverHTTP.setHandler(context);

        // Start Server
        serverHTTP.start();

        // Determine Base URI for Server
        String host = httpConnector.getHost();
        if (host == null) {
            host = "localhost";
        }
        serverHTTPUri = new URI(String.format("http://%s:%d/", host, httpConnector.getLocalPort()));
        serverHTTPSUri = new URI(String.format("https://%s:%d/", host, sslConnector.getLocalPort()));
    }


    @AfterAll
    public static void stopJetty() {
        try {
            serverHTTP.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testSimpleHTTPFetch() throws Exception {
        // given
        DefaultURLFetcher testInstance = new DefaultURLFetcher.Builder()
                .build();
        //when
        FetcherResponse response = testInstance.fetch(serverHTTPUri.resolve("oasis-smp-1.0/extension.xml"));
        //then
        assertNotNull(response);
    }

    @Disabled//fails with eu.europa.ec.dynamicdiscovery.exception.ConnectionException: Error occurred while retrieving [/oasis-smp-1.0/extension.xml]: Error: [InvalidAlgorithmParameterException: the trustAnchors parameter must be non-empty]
    @Test
    void testSimpleHTTPSFetchOK() throws Exception {
        KeyStore clientKeystore = CommonUtil.loadKeystore("truststore/server-keystore.p12", KEYSTORE_TYPE, PASSWD);
        KeyStore clientTruststore = CommonUtil.loadKeystore("truststore/tls-truststore.p12", KEYSTORE_TYPE, PASSWD);
        assertNotNull(clientTruststore);

        DefaultURLFetcher testInstance = new DefaultURLFetcher.Builder()
                .setHttpSchemeEnabled(false)
                .tlsKeystore(clientKeystore, PASSWD.toCharArray())
                .tlsTruststore(clientTruststore)
                .build();

        FetcherResponse response = testInstance.fetch(serverHTTPSUri.resolve("oasis-smp-1.0/extension.xml"));

        assertNotNull(response);
    }

    @Test
    void testSimpleHTTPSFetchMissmatchCipherSuite() throws Exception {
        KeyStore clientKeystore = CommonUtil.loadKeystore("truststore/server-keystore.p12", KEYSTORE_TYPE, PASSWD);
        KeyStore clientTruststore = CommonUtil.loadKeystore("truststore/tls-truststore.p12", KEYSTORE_TYPE, PASSWD);
        assertNotNull(clientTruststore);

        DefaultURLFetcher testInstance = new DefaultURLFetcher.Builder()
                .setHttpSchemeEnabled(false)
                .tlsCipherSuites("TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384")
                .tlsKeystore(clientKeystore, PASSWD.toCharArray())
                .tlsTruststore(clientTruststore)
                .build();

        ConnectionException result = assertThrows(ConnectionException.class, ()
                -> testInstance.fetch(serverHTTPSUri.resolve("oasis-smp-1.0/extension.xml")));

        assertNotNull(result);
        MatcherAssert.assertThat(result.getMessage(), CoreMatchers.containsString("SSLHandshakeException: Received fatal alert: handshake_failure"));

    }

    @Test
    void testSimpleHTTPSFetchMissmatchTLSVersion() throws Exception {
        KeyStore clientKeystore = CommonUtil.loadKeystore("truststore/server-keystore.p12", KEYSTORE_TYPE, PASSWD);
        KeyStore clientTruststore = CommonUtil.loadKeystore("truststore/tls-truststore.p12", KEYSTORE_TYPE, PASSWD);
        assertNotNull(clientTruststore);

        DefaultURLFetcher testInstance = new DefaultURLFetcher.Builder()
                .setHttpSchemeEnabled(false)
                .tlsVersions("TLSv1.1")
                .tlsKeystore(clientKeystore, PASSWD.toCharArray())
                .tlsTruststore(clientTruststore)
                .build();

        ConnectionException result = assertThrows(ConnectionException.class, ()
                -> testInstance.fetch(serverHTTPSUri.resolve("oasis-smp-1.0/extension.xml")));

        assertNotNull(result);
        MatcherAssert.assertThat(result.getMessage(), CoreMatchers.containsString("SSLHandshakeException: No appropriate protocol"));

    }

    @Test
    void testSimpleHTTPSFetchFailMissingTrustStore() throws Exception {
        KeyStore clientKeystore = CommonUtil.loadKeystore("truststore/server-keystore.p12", KEYSTORE_TYPE, PASSWD);

        DefaultURLFetcher testInstance = new DefaultURLFetcher.Builder()
                .setHttpSchemeEnabled(false)
                .tlsKeystore(clientKeystore, PASSWD.toCharArray())
                .build();

        ConnectionException result = assertThrows(ConnectionException.class, ()
                -> testInstance.fetch(serverHTTPSUri.resolve("oasis-smp-1.0/extension.xml")));

        assertNotNull(result);
        MatcherAssert.assertThat(result.getMessage(), CoreMatchers.containsString("unable to find valid certification path to requested target"));

    }

    @Test
    void testSimpleHTTPSFetchFailMissingClientKey() throws Exception {
        KeyStore clientTruststore = CommonUtil.loadKeystore("truststore/tls-truststore.p12", KEYSTORE_TYPE, PASSWD);
        assertNotNull(clientTruststore);

        DefaultURLFetcher testInstance = new DefaultURLFetcher.Builder()
                .setHttpSchemeEnabled(false)
                .tlsTruststore(clientTruststore)
                .build();

        ConnectionException result = assertThrows(ConnectionException.class, ()
                -> testInstance.fetch(serverHTTPSUri.resolve("oasis-smp-1.0/extension.xml")));
        assertNotNull(result);
    }

    @Test
    void testDisableHTTP() {

        DefaultURLFetcher testInstance = new DefaultURLFetcher.Builder()
                .setHttpSchemeEnabled(false)
                .build();

        ConnectionException result = assertThrows(ConnectionException.class, () -> testInstance.fetch(serverHTTPUri.resolve("oasis-smp-1.0/extension.xml")));

        assertNotNull(result);
        MatcherAssert.assertThat(result.getMessage(), CoreMatchers.containsString("http protocol is not supported"));
    }
}
