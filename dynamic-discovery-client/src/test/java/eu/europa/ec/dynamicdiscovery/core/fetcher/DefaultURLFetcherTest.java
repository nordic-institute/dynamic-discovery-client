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
package eu.europa.ec.dynamicdiscovery.core.fetcher;

import eu.europa.ec.dynamicdiscovery.core.fetcher.impl.DefaultURLFetcher;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;


public class DefaultURLFetcherTest {

    private DefaultURLFetcher defaultURLFetcher;
    private HttpClient httpClient;
    private HttpGet httpGet;

    @Test
    public void testConnect() throws Exception {
        //GIVEN
        setup(200);

        //WHEN
        FetcherResponse fetcherResponse = defaultURLFetcher.connect(httpClient, httpGet);

        //THEN
        Assert.assertNotNull(fetcherResponse);
    }

    @Test(expected = DNSLookupException.class)
    public void testConnectForNAPTRException() throws Exception {
        //GIVEN
        setup(200);
        String naptStr = "DALXFO3CDYE5ZSLF5WAVCYQ3XGERI6ONUBJU5WAH3T77THFWCGEQ.ehealth-actorid-qns.ehealth.acc.edelivery.tech.ec.europa.eu";
        URI naptrURI = new URI(naptStr);
        Mockito.doThrow(new IOException("Dummy Exception")).when(httpClient).execute(any(HttpUriRequest.class));
        Mockito.doReturn(naptrURI).when(httpGet).getURI();

        //WHEN THEN
        try {
            FetcherResponse fetcherResponse = defaultURLFetcher.connect(httpClient, httpGet);
        } catch (Exception exc) {
            Assert.assertEquals("It was not able to retrieve data from SMP server using NAPTR record according to OASIS BDX specification.", exc.getMessage());
            throw exc;
        }
    }

    @Test(expected = DNSLookupException.class)
    public void testConnectForCNAMEException() throws Exception {
        //GIVEN
        setup(200);
        String cnameStr = "http://b-06f7d7be87633d898ff33f4f4a45212f.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu";
        URI naptrURI = new URI(cnameStr);
        Mockito.doThrow(new IOException("Dummy Exception")).when(httpClient).execute(any(HttpUriRequest.class));
        Mockito.doReturn(naptrURI).when(httpGet).getURI();

        //WHEN THEN
        try {
            FetcherResponse fetcherResponse = defaultURLFetcher.connect(httpClient, httpGet);
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

    private void setup(int errorCode) throws Exception {
        defaultURLFetcher = new DefaultURLFetcher();
        httpClient = mock(HttpClient.class);
        httpGet = mock(HttpGet.class);

        HttpResponse response = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        HttpEntity httpEntity = mock(HttpEntity.class);

        Mockito.doReturn(response).when(httpClient).execute(any(HttpUriRequest.class));
        Mockito.doReturn(new URI("http://test.eu/schema::party")).when(httpGet).getURI();

        Mockito.doReturn(httpEntity).when(response).getEntity();
        Mockito.doReturn(new ByteArrayInputStream("Dummy Content".getBytes())).when(httpEntity).getContent();
        Mockito.doReturn(statusLine).when(response).getStatusLine();
        Mockito.doReturn(errorCode).when(statusLine).getStatusCode();
    }

    private void testConnectForExceptions(String errorMessage, int errorCode) throws Exception {
        //GIVEN
        setup(errorCode);

        //WHEN THEN
        try {
            FetcherResponse fetcherResponse = defaultURLFetcher.connect(httpClient, httpGet);
        } catch (Exception exc) {
            Assert.assertEquals(errorMessage, exc.getMessage());
            throw exc;
        }
    }
}