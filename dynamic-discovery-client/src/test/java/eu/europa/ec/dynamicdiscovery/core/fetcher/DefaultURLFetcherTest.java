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
 *
 */
package eu.europa.ec.dynamicdiscovery.core.fetcher;

import eu.europa.ec.dynamicdiscovery.core.fetcher.impl.DefaultURLFetcher;
import eu.europa.ec.dynamicdiscovery.core.security.IProxyConfiguration;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.mockito.ArgumentMatchers.any;

/**
 * @author Flávio W. R. Santos
 * @author Sebastian-Ion TINCU
 * @since 1.13
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultURLFetcherTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private IProxyConfiguration proxyConfiguration;

    @Mock
    private HttpRoutePlanner routePlanner;

    @Mock
    private HttpGet httpGet;

    @Mock
    private HttpResponse response;

    @Mock
    private StatusLine statusLine;

    @Mock
    private HttpEntity httpEntity;

    @Captor
    private ArgumentCaptor<HttpClient> httpClientCaptor;

    @Captor
    private ArgumentCaptor<HttpGet> httpGetCaptor;

    @Mock
    private CredentialsProvider credentialsProvider;

    @InjectMocks
    private DefaultURLFetcher defaultURLFetcher = new DefaultURLFetcher();

    private String targetHost;

    private HttpHost proxyHost;

    @Before
    public void setup() throws Exception {
        Mockito.doReturn(response).when(httpClient).execute(any(HttpUriRequest.class));
        Mockito.doReturn(new URI("http://test.eu/schema::party")).when(httpGet).getURI();

        Mockito.doReturn(httpEntity).when(response).getEntity();
        Mockito.doReturn(new ByteArrayInputStream("Dummy Content".getBytes())).when(httpEntity).getContent();
        Mockito.doReturn(statusLine).when(response).getStatusLine();
    }

    @Test
    public void testConnect() throws Exception {
        //GIVEN
        givenErrorCode(200);

        //WHEN
        FetcherResponse fetcherResponse = defaultURLFetcher.connect(httpClient, httpGet);

        //THEN
        Assert.assertNotNull(fetcherResponse);
    }

    @Test(expected = DNSLookupException.class)
    public void testConnectForNAPTRException() throws Exception {
        //GIVEN
        givenErrorCode(200);
        String naptStr = "DALXFO3CDYE5ZSLF5WAVCYQ3XGERI6ONUBJU5WAH3T77THFWCGEQ.ehealth-actorid-qns.ehealth.acc.edelivery.tech.ec.europa.eu";
        URI naptrURI = new URI(naptStr);
        Mockito.doThrow(new IOException("Dummy Exception")).when(httpClient).execute(any(HttpUriRequest.class));
        Mockito.doReturn(naptrURI).when(httpGet).getURI();

        //WHEN THEN
        try {
            defaultURLFetcher.connect(httpClient, httpGet);
        } catch (Exception exc) {
            Assert.assertEquals("It was not able to retrieve data from SMP server using NAPTR record according to OASIS BDX specification.", exc.getMessage());
            throw exc;
        }
    }

    @Test(expected = DNSLookupException.class)
    public void testConnectForCNAMEException() throws Exception {
        //GIVEN
        givenErrorCode(200);
        String cnameStr = "http://b-06f7d7be87633d898ff33f4f4a45212f.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu";
        URI naptrURI = new URI(cnameStr);
        Mockito.doThrow(new IOException("Dummy Exception")).when(httpClient).execute(any(HttpUriRequest.class));
        Mockito.doReturn(naptrURI).when(httpGet).getURI();

        //WHEN THEN
        try {
            defaultURLFetcher.connect(httpClient, httpGet);
        } catch (Exception exc) {
            Assert.assertEquals("It was not able to retrieve data from SMP server using CNAME record according to PEPPOL BUSDOX specification.", exc.getMessage());
            throw exc;
        }
    }

    @Test(expected = DNSLookupException.class)
    public void testConnectForException404() throws Exception {
        testConnectForExceptions("SMP lookup address http://test.eu/schema::party not found - response 404", 404);
    }

    @Test(expected = DNSLookupException.class)
    public void testConnectForException500() throws Exception {
        testConnectForExceptions("Got Http error code 500 trying to access SMP URL:http://test.eu/schema::party", 500);
    }

    @Test
    public void fetch_proxyIgnoresTargetHost() throws Exception {
        // GIVEN
        givenTargetHost("ec.europa.eu");
        givenIgnoringConnectWithCapture();
        givenTargetHostIgnoredByProxy(targetHost);

        // WHEN
        whenFetchingFromTheTarget();

        // THEN
        thenNoProxyConfigurationConfigured();
    }

    @Test
    public void fetch_proxyDoesNotIgnoreTargetHost() throws Exception {
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
    public void fetch() throws Exception {
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

    private void testConnectForExceptions(String errorMessage, int errorCode) throws Exception {
        //GIVEN
        givenErrorCode(errorCode);

        //WHEN THEN
        try {
            defaultURLFetcher.connect(httpClient, httpGet);
        } catch (Exception exc) {
            Assert.assertEquals(errorMessage, exc.getMessage());
            throw exc;
        }
    }

    private void givenErrorCode(int errorCode) {
        Mockito.doReturn(errorCode).when(statusLine).getStatusCode();
    }

    private void givenTargetHost(String targetHost) {
        this.targetHost = targetHost;
    }

    private void givenIgnoringConnectWithCapture() throws TechnicalException {
        defaultURLFetcher = Mockito.spy(defaultURLFetcher);
        Mockito.doReturn(null).when(defaultURLFetcher).connect(any(HttpClient.class), any(HttpGet.class));
    }

    private void givenIgnoringConnect() throws TechnicalException {
        defaultURLFetcher = Mockito.spy(defaultURLFetcher);
        Mockito.doReturn(null).when(defaultURLFetcher).connect(any(HttpClient.class), any(HttpGet.class));
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
        defaultURLFetcher.fetch(new URI("https://" + targetHost));
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
        Mockito.verify(defaultURLFetcher).connect(httpClientCaptor.capture(), httpGetCaptor.capture());
        HttpClient httpClient = httpClientCaptor.getValue();
        HttpGet httpGet = httpGetCaptor.getValue();

        HttpRoutePlanner actualRoutePlanner = (HttpRoutePlanner) FieldUtils.readField(httpClient, "routePlanner", true);
        CredentialsProvider actualCredentialsProvider = (CredentialsProvider) FieldUtils.readField(httpClient, "credentialsProvider", true);
        HttpHost actualProxyHost = (HttpHost) FieldUtils.readField(httpGet.getConfig(), "proxy", true);

        Assert.assertSame("Should have configured the connection using the provided route planner", routePlanner, actualRoutePlanner);
        Assert.assertSame("Should have configured the connection using the provided proxy host", proxyHost, actualProxyHost);
        Assert.assertSame("Should have configured the connection using the provided credentials provider", credentialsProvider, actualCredentialsProvider);
    }
}