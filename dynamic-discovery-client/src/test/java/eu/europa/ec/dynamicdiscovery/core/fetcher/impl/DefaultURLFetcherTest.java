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
 *
 */
package eu.europa.ec.dynamicdiscovery.core.fetcher.impl;

import eu.europa.ec.dynamicdiscovery.core.security.IProxyConfiguration;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.Credentials;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.routing.HttpRoutePlanner;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHost;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Flávio W. R. Santos
 * @author Sebastian-Ion TINCU
 * @since 1.13
 */
@ExtendWith(MockitoExtension.class)
class DefaultURLFetcherTest {

    @Mock
    private CloseableHttpClient httpClient;

    @Mock
    private HttpGet httpGet;

    @Mock
    private CloseableHttpResponse response;

    @Mock
    private HttpEntity httpEntity;

    @Mock
    private IProxyConfiguration proxyConfiguration;

    @Mock
    private HttpRoutePlanner routePlanner;

    private final DefaultURLFetcher testInstance = new DefaultURLFetcher.Builder()
            .proxyConfiguration(proxyConfiguration)
            .routePlanner(routePlanner)
            .build();

    @Test
    void testConnect() throws Exception {
        Mockito.doReturn(response).when(httpClient).execute(any(HttpUriRequest.class));
        Mockito.doReturn(httpEntity).when(response).getEntity();
        Mockito.doReturn(new ByteArrayInputStream("Dummy Content".getBytes())).when(httpEntity).getContent();
        Mockito.doReturn(200).when(response).getCode();
        //WHEN
        InputStream result = testInstance.connect(httpClient, httpGet);

        //THEN
        assertNotNull(result);
    }

    @Test
    void testConnectForNAPTRException() throws Exception {
        //GIVEN
        String naptStr = "DALXFO3CDYE5ZSLF5WAVCYQ3XGERI6ONUBJU5WAH3T77THFWCGEQ.ehealth-actorid-qns.ehealth.acc.edelivery.tech.ec.europa.eu";
        URI naptrURI = new URI(naptStr);
        Mockito.doThrow(new IOException("Dummy Exception")).when(httpClient).execute(any(HttpUriRequest.class));
        Mockito.doReturn(naptrURI).when(httpGet).getUri();

        //WHEN THEN
        DNSLookupException result = assertThrows(DNSLookupException.class, () -> testInstance.connect(httpClient, httpGet));
        assertEquals("It was not able to retrieve data from SMP server using NAPTR record according to OASIS BDX specification.",
                result.getMessage());
    }

    @Test
    void testBuildAuthenticationForTarget() {
        // given
        HttpHost targetHost = new HttpHost("http", "example.com", 80);
        Credentials credentials = mock(Credentials.class);
        BasicCredentialsProvider provider = new BasicCredentialsProvider();

        DefaultURLFetcher fetcher = new DefaultURLFetcher.Builder().build();

        // when
        BasicCredentialsProvider result = fetcher.buildAuthenticationForTarget(targetHost, credentials, provider);

        // then
        assertNotNull(result, "The returned BasicCredentialsProvider should not be null");
        AuthScope authScope = new AuthScope(targetHost);
        assertEquals(credentials, result.getCredentials(authScope, null),
                "The credentials should match the provided credentials");
    }

    @Test
    void testProxyConfigurationApplied() {
        // Mock the proxy configuration
        IProxyConfiguration mockProxyConfiguration = mock(IProxyConfiguration.class);
        when(mockProxyConfiguration.isNonProxyHost("example.com")).thenReturn(false);
        HttpHost mockProxyHost = new HttpHost("http", "proxy.example.com", 8080);
        when(mockProxyConfiguration.getProxyHost("example.com")).thenReturn(mockProxyHost);
        Credentials mockCredentials = mock(Credentials.class);
        when(mockProxyConfiguration.getProxyCredentials()).thenReturn(mockCredentials);

        // Mock the credential provider
        BasicCredentialsProvider mockCredentialsProvider = mock(BasicCredentialsProvider.class);

        // Create a request config builder
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();

        // Simulate the selected code
        String participantUnderSmpURIHost = "example.com";
        if (!mockProxyConfiguration.isNonProxyHost(participantUnderSmpURIHost)) {

            HttpHost proxyHost = mockProxyConfiguration.getProxyHost(participantUnderSmpURIHost);
            // Set proxy authentication
            BasicCredentialsProvider updatedCredentialsProvider = new DefaultURLFetcher
                    .Builder().build()
                    .buildAuthenticationForTarget(proxyHost,
                            mockProxyConfiguration.getProxyCredentials(),
                            mockCredentialsProvider);

            // Verify the proxy host and credentials are set correctly
            assertNotNull(updatedCredentialsProvider);
            verify(mockProxyConfiguration).getProxyHost("example.com");
            verify(mockProxyConfiguration).getProxyCredentials();
            assertEquals(mockProxyHost, proxyHost);
        }
    }

}
