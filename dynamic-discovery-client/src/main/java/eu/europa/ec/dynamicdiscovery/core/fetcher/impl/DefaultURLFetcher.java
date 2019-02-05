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
 *@author Erlend Klakegg Bergheim - erlend.klakegg.bergheim@difi.no
 *
 */
package eu.europa.ec.dynamicdiscovery.core.fetcher.impl;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.fetcher.IMetadataFetcher;
import eu.europa.ec.dynamicdiscovery.core.security.IProxyConfiguration;
import eu.europa.ec.dynamicdiscovery.core.security.impl.DefaultProxy;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.net.URI;

public class DefaultURLFetcher implements IMetadataFetcher {
    final static Logger LOG = LoggerFactory.getLogger(DefaultURLFetcher.class);

    private IProxyConfiguration proxyConfiguration;

    public DefaultURLFetcher(IProxyConfiguration proxyConfiguration) {
        this.proxyConfiguration = proxyConfiguration;
    }

    public DefaultURLFetcher() {
        this(null);
    }

    @Override
    public FetcherResponse fetch(URI participantUnderSmpURI) throws TechnicalException {
        if (this.proxyConfiguration != null) {
            LOG.debug("Fetch data using proxy: " + (this.proxyConfiguration.getHttpget()!=null
                    && this.proxyConfiguration.getHttpget().getConfig()!=null ?
            this.proxyConfiguration.getHttpget().getConfig().getProxy(): "noProxy")+", participantURI:" + participantUnderSmpURI);
            proxyConfiguration.build(participantUnderSmpURI);
            return connect(this.proxyConfiguration.getHttpclient(), this.proxyConfiguration.getHttpget());
        } else {
            LOG.debug("Fetch data without proxy, participantURI: [{}]" , participantUnderSmpURI);
            return connect(HttpClients.createDefault(), new HttpGet(participantUnderSmpURI));
        }
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