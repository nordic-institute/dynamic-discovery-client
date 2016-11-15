/*
 * (C) Copyright 2016 Dynamic Discovery Client
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
 *@author Erlend Klakegg Bergheim - erlend.klakegg.bergheim@difi.no
 *
 */
package eu.europa.ec.dynamicdiscovery.core.fetcher;

import eu.europa.ec.dynamicdiscovery.core.security.IProxyConfiguration;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedInputStream;
import java.net.URI;

public class URLFetcher implements IMetadataFetcher {
    private HttpClient httpClient;
    private IProxyConfiguration proxyConfiguration;

    public URLFetcher(IProxyConfiguration proxyConfiguration) {
        this.proxyConfiguration = proxyConfiguration;
    }

    public URLFetcher() {
        this(null);
    }

    public FetcherResponse fetch(URI uri) throws DNSLookupException {
        if (this.proxyConfiguration != null) {
            proxyConfiguration.build(uri);
            return connect(this.proxyConfiguration.getHttpclient(), this.proxyConfiguration.getHttpget());
        } else {
            return connect(HttpClients.createDefault(), new HttpGet(uri));
        }
    }

    public FetcherResponse connect(HttpClient httpClient, HttpGet httpGet) throws DNSLookupException {
        try {
            HttpResponse response = httpClient.execute(httpGet);
            switch (response.getStatusLine().getStatusCode()) {
                case 200:
                    return new FetcherResponse(new BufferedInputStream(response.getEntity().getContent()), response.containsHeader("X-SMP-Namespace") ? response.getFirstHeader("X-SMP-Namespace").getValue() : null);
                case 404:
                    throw new DNSLookupException("Not supported.");
                default:
                    throw new DNSLookupException(String.format("Received code %s for lookup.", new Object[]{Integer.valueOf(response.getStatusLine().getStatusCode())}));
            }
        } catch (Exception exc) {
            throw new DNSLookupException(exc.getMessage(), exc);
        }
    }
}