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
package eu.europa.ec.dynamicdiscovery.core.fetcher;

import eu.europa.ec.dynamicdiscovery.core.fetcher.impl.DefaultURLFetcher;
import eu.europa.ec.dynamicdiscovery.core.security.IProxyConfiguration;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.routing.HttpRoutePlanner;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHost;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

/**
 * @author Flávio W. R. Santos
 * @author Sebastian-Ion TINCU
 * @since 1.13
 */
@ExtendWith(MockitoExtension.class)
class DefaultURLFetcherTest {

    @Captor
    private ArgumentCaptor<CloseableHttpClient> httpClientCaptor;

    @Captor
    private ArgumentCaptor<HttpGet> httpGetCaptor;

    private CloseableHttpClient httpClient= Mockito.mock(CloseableHttpClient.class);

    private HttpGet httpGet= Mockito.mock(HttpGet.class);

    private CloseableHttpResponse response = Mockito.mock(CloseableHttpResponse.class);

    private HttpEntity httpEntity  = Mockito.mock(HttpEntity.class);

    private CredentialsProvider credentialsProvider  = Mockito.mock(CredentialsProvider.class);

    private IProxyConfiguration proxyConfiguration = Mockito.mock(IProxyConfiguration.class);

    private HttpRoutePlanner routePlanner = Mockito.mock(HttpRoutePlanner.class);

    private DefaultURLFetcher testInstance = new DefaultURLFetcher.Builder()
            .proxyConfiguration(proxyConfiguration)
                .routePlanner(routePlanner)
                .build();


    private String targetHost;

    private HttpHost proxyHost;

    @Test
    void testConnect() throws Exception {
        Mockito.doReturn(response).when(httpClient).execute(any(HttpUriRequest.class));
        Mockito.doReturn(httpEntity).when(response).getEntity();
        Mockito.doReturn(new ByteArrayInputStream("Dummy Content".getBytes())).when(httpEntity).getContent();
        Mockito.doReturn(200).when(response).getCode();
        //WHEN
        FetcherResponse fetcherResponse = testInstance.connect(httpClient, httpGet);

        //THEN
        assertNotNull(fetcherResponse);
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
        assertEquals("It was not able to retrieve data from SMP server using NAPTR record according to OASIS BDX specification.", result.getMessage());

    }

    @Test
    void testConnectForCNAMEException() throws Exception {
        //GIVEN
        String cnameStr = "http://b-06f7d7be87633d898ff33f4f4a45212f.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu";
        URI naptrURI = new URI(cnameStr);
        Mockito.doThrow(new IOException("Dummy Exception")).when(httpClient).execute(any(HttpUriRequest.class));
        Mockito.doReturn(naptrURI).when(httpGet).getUri();

        //WHEN THEN
        DNSLookupException result = assertThrows(DNSLookupException.class, () -> testInstance.connect(httpClient, httpGet));
        assertEquals("It was not able to retrieve data from SMP server using CNAME record according to PEPPOL BUSDOX specification.", result.getMessage());
    }

    @Test
    void testConnectForException404() throws Exception {
        Mockito.doReturn(response).when(httpClient).execute(any(HttpUriRequest.class));
        Mockito.doReturn(new URI("http://test.eu/schema::party")).when(httpGet).getUri();
        testConnectForExceptions("SMP lookup address http://test.eu/schema::party not found - response 404", 404);
    }

    @Test
    void testConnectForException500() throws Exception {
        Mockito.doReturn(response).when(httpClient).execute(any(HttpUriRequest.class));
        Mockito.doReturn(new URI("http://test.eu/schema::party")).when(httpGet).getUri();

        testConnectForExceptions("Got Http error code 500 trying to access SMP URL:http://test.eu/schema::party", 500);
    }

    @Test
    void fetch_proxyIgnoresTargetHost() throws Exception {
        // GIVEN
        givenTargetHost("ec.europa.eu");
        givenIgnoringConnectWithCapture();
        givenProxyForTargetHost(targetHost, Boolean.TRUE);

        // WHEN
        whenFetchingFromTheTarget();

        // THEN
        thenNoProxyConfigurationConfigured();
    }

    @Test
    void fetch_proxyDoesNotIgnoreTargetHost() throws Exception {
        // GIVEN
        givenTargetHost("ec.europa.eu");
        givenIgnoringConnect();
        givenTargetHostNotIgnoredByProxy(targetHost);

        // WHEN
        whenFetchingFromTheTarget();

        // THEN
        thenProxyConfigurationConfigured();
    }

    @Test
    void fetch() throws Exception {
        // GIVEN
        givenTargetHost("ec.europa.eu");
        givenIgnoringConnect();
        givenTargetHostNotIgnoredByProxy(targetHost);
        givenCredentialsProvider();
        givenProxyHost();

        // WHEN
        whenFetchingFromTheTarget();

        // THEN
        thenHttpConnectionDetailsCorrect();
    }

    private void testConnectForExceptions(String errorMessage, int errorCode) {
        //GIVEN
        givenErrorCode(errorCode);

        //WHEN THEN
        DNSLookupException result = assertThrows(DNSLookupException.class, () -> testInstance.connect(httpClient, httpGet));
        assertEquals(errorMessage, result.getMessage());
    }

    private void givenErrorCode(int errorCode) {
        Mockito.doReturn(errorCode).when(response).getCode();
    }

    private void givenTargetHost(String targetHost) {
        this.targetHost = targetHost;
    }

    private void givenIgnoringConnectWithCapture() throws TechnicalException {
        testInstance = Mockito.spy(testInstance);
        Mockito.doReturn(null).when(testInstance).connect(any(CloseableHttpClient.class), any(HttpGet.class));
    }

    private void givenIgnoringConnect() throws TechnicalException {
        testInstance = Mockito.spy(testInstance);
        Mockito.doReturn(null).when(testInstance).connect(any(CloseableHttpClient.class), any(HttpGet.class));
    }

    private void givenTargetHostIgnoredByProxy(String targetHost) {
        givenProxyForTargetHost(targetHost, Boolean.TRUE);
    }

    private void givenTargetHostNotIgnoredByProxy(String targetHost) {
        givenProxyForTargetHost(targetHost, Boolean.FALSE);
    }

    private void givenProxyForTargetHost(String targetHost, Boolean ignored) {
        Mockito.when(proxyConfiguration.isNonProxyHost(targetHost)).thenReturn(ignored);
    }

    private void givenProxyHost() {
        proxyHost = new HttpHost("127.0.0.1", 8080);
        Mockito.when(proxyConfiguration.getProxyHost(targetHost)).thenReturn(proxyHost);
    }

    private void givenCredentialsProvider() {
        Mockito.when(proxyConfiguration.getProxyCredentials(targetHost)).thenReturn(credentialsProvider);
    }

    private void whenFetchingFromTheTarget() throws TechnicalException, URISyntaxException {
        testInstance.fetch(new URI("https://" + targetHost));
    }

    private void thenNoProxyConfigurationConfigured() {
        Mockito.verify(proxyConfiguration, Mockito.never()).getProxyCredentials(targetHost);
        Mockito.verify(proxyConfiguration, Mockito.never()).getProxyHost(targetHost);
    }

    private void thenProxyConfigurationConfigured() {
        Mockito.verify(proxyConfiguration).getProxyCredentials(targetHost);
        Mockito.verify(proxyConfiguration).getProxyHost(targetHost);
    }

    private void thenHttpConnectionDetailsCorrect() throws TechnicalException, IllegalAccessException {
        Mockito.verify(testInstance).connect(httpClientCaptor.capture(), httpGetCaptor.capture());
        HttpClient httpClient = httpClientCaptor.getValue();
        HttpGet httpGet = httpGetCaptor.getValue();

        HttpRoutePlanner actualRoutePlanner = (HttpRoutePlanner) FieldUtils.readField(httpClient, "routePlanner", true);
        CredentialsProvider actualCredentialsProvider = (CredentialsProvider) FieldUtils.readField(httpClient, "credentialsProvider", true);
        HttpHost actualProxyHost = (HttpHost) FieldUtils.readField(httpGet.getConfig(), "proxy", true);

        assertSame(routePlanner, actualRoutePlanner);
        assertSame(proxyHost, actualProxyHost);
        assertSame(credentialsProvider, actualCredentialsProvider);
    }
}
