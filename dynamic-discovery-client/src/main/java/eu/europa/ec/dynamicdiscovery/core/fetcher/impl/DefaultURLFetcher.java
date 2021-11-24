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
 */
package eu.europa.ec.dynamicdiscovery.core.fetcher.impl;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.fetcher.IMetadataFetcher;
import eu.europa.ec.dynamicdiscovery.core.security.IProxyConfiguration;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.impl.client.HttpClients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.net.URI;

/**
 * @author Flávio W. R. Santos
 * @author Erlend Klakegg Bergheim
 * @author Sebastian-Ion TINCU
 * @since 1.13
 */
public class DefaultURLFetcher implements IMetadataFetcher {
    final static Logger LOG = LoggerFactory.getLogger(DefaultURLFetcher.class);

    private IProxyConfiguration proxyConfiguration;

    private HttpRoutePlanner routePlanner;

    public DefaultURLFetcher(IProxyConfiguration proxyConfiguration) {
        this.proxyConfiguration = proxyConfiguration;
    }

    public DefaultURLFetcher() {
    }

    public DefaultURLFetcher(HttpRoutePlanner routePlanner, IProxyConfiguration proxyConfiguration) {
        this.routePlanner = routePlanner;
        this.proxyConfiguration = proxyConfiguration;
    }

    public DefaultURLFetcher(HttpRoutePlanner routePlanner) {
        this.routePlanner = routePlanner;
    }

    @Override
    public FetcherResponse fetch(URI participantUnderSmpURI) throws TechnicalException {
        LOG.debug("Fetch data for participantURI [{}]", participantUnderSmpURI);

        String participantUnderSmpURIHost = participantUnderSmpURI.getHost();
        HttpGet httpGet = new HttpGet(participantUnderSmpURI);
        httpGet.setConfig(RequestConfig.custom()
                .build());
        HttpClient httpClient = HttpClients.custom()
                .setRoutePlanner(routePlanner)
                .build();

        if (proxyConfiguration != null && !proxyConfiguration.isNonProxyHost(participantUnderSmpURIHost)) {
            LOG.debug("Fetch data using proxy");
            httpClient = HttpClients.custom()
                    .setRoutePlanner(routePlanner)
                    .setDefaultCredentialsProvider(proxyConfiguration.getProxyCredentials(participantUnderSmpURIHost))
                    .build();
            httpGet.setConfig(RequestConfig.custom()
                    .setProxy(proxyConfiguration.getProxyHost(participantUnderSmpURIHost))
                    .build());
        }
        return connect(httpClient, httpGet);
    }

    public FetcherResponse connect(HttpClient httpClient, HttpGet httpGet) throws TechnicalException {
        try {
            HttpResponse response = httpClient.execute(httpGet);
            switch (response.getStatusLine().getStatusCode()) {
                case 200:
                    return new FetcherResponse(new BufferedInputStream(response.getEntity().getContent()));
                case 404:
                    throw new DNSLookupException("SMP lookup address "+httpGet.getURI()+" not found - response 404");
                default:
                    throw new DNSLookupException("Got Http error code "+response.getStatusLine().getStatusCode()+" trying to access SMP URL:" +httpGet.getURI());
            }
        } catch (TechnicalException exc) {
            LOG.error("Fetching data failed for participantURI: [{}]. Error: [{}]", httpGet.getRequestLine() , ExceptionUtils.getRootCauseMessage(exc), exc);
            throw exc;
        } catch (Exception exc) {
            String message = "It was not able to retrieve data from SMP server using NAPTR record according to OASIS BDX specification.";
            String uri = httpGet.getURI().toString().toLowerCase();
            if (uri.startsWith("http://b-") || uri.startsWith("https://b-")) {
                message = "It was not able to retrieve data from SMP server using CNAME record according to PEPPOL BUSDOX specification.";
            }
            LOG.error("Fetching data failed for participantURI: [{}]. Error: [{}]. Message: [{}]", httpGet.getRequestLine() , ExceptionUtils.getRootCauseMessage(exc),  message , exc);
            throw new DNSLookupException(message, exc);
        }
    }
}