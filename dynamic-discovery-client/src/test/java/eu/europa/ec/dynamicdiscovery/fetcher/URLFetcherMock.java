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
 *
 */
package eu.europa.ec.dynamicdiscovery.fetcher;

import com.github.tomakehurst.wiremock.client.WireMock;
import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.fetcher.IMetadataFetcher;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import eu.europa.ec.dynamicdiscovery.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URI;

import static com.github.tomakehurst.wiremock.client.WireMock.post;

public class URLFetcherMock implements IMetadataFetcher {

    public enum LookupType {
        NAPTR, CNAME;
    }

    private LookupType lookupType;
    private String serviceUrl;
    private String bodyResponse;
    private String smpAlias;

    public URLFetcherMock() {
    }

    public void setParameters(LookupType lookupType, String serviceUrl, String responseFileName, String smpAlias) throws TechnicalException {
        this.lookupType = lookupType;
        this.serviceUrl = serviceUrl;
        this.bodyResponse = CommonUtil.getStringFromXmlFile(responseFileName);

        if (lookupType == LookupType.CNAME && StringUtils.isEmpty(smpAlias)) {
            throw new DNSLookupException("SMP alias represented by MD5 must be not null");
        }
        if (!StringUtils.isEmpty(smpAlias)) {
            this.smpAlias = "http://" + smpAlias + (!smpAlias.endsWith("/") ? "/" : "");
        }
    }

    public void setParameters(LookupType lookupType, String serviceUrl, String responseFileName) throws TechnicalException {
        setParameters(lookupType, serviceUrl, responseFileName, null);
    }

    @Override
    public FetcherResponse fetch(URI uri) throws TechnicalException {
        return switchResponse(uri);
    }

    private FetcherResponse switchResponse(URI uri) throws TechnicalException {
        try {
            WireMock.stubFor(post(WireMock.urlEqualTo(serviceUrl))
                    .willReturn(WireMock.aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/soap+xml")
                            .withBody(bodyResponse)));

            HttpClient client = HttpClientBuilder.create().build();

            String uriStr;
            if (isCNAME()) {
                uriStr = uri.toString().replace(smpAlias, Constants.SMP_DOMAIN);
            } else {
                uriStr = uri.toString().replace(Constants.SMP_DOMAIN_ALIAS, Constants.SMP_DOMAIN);

            }
            HttpPost request = new HttpPost(uriStr);
            HttpResponse response = client.execute(request);

            switch (response.getStatusLine().getStatusCode()) {
                case 200:
                    return new FetcherResponse(new BufferedInputStream(response.getEntity().getContent()), response.containsHeader("X-SMP-Namespace") ? response.getFirstHeader("X-SMP-Namespace").getValue() : null);
                case 404:
                    throw new DNSLookupException("Not supported.");
                default:
                    throw new DNSLookupException(String.format("Received code %s for lookup.", new Object[]{Integer.valueOf(response.getStatusLine().getStatusCode())}));
            }
        } catch (IOException exc) {
            throw new DNSLookupException(exc.getMessage(), exc);
        }
    }

    private boolean isCNAME() {
        if (lookupType != null && lookupType == LookupType.CNAME) {
            return true;
        }
        return false;
    }
}