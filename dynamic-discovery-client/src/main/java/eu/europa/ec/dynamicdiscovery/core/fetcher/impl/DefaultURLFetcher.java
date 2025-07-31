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
package eu.europa.ec.dynamicdiscovery.core.fetcher.impl;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.fetcher.IDocumentFetcher;
import eu.europa.ec.dynamicdiscovery.core.security.ICredentialProvider;
import eu.europa.ec.dynamicdiscovery.core.security.IProxyConfiguration;
import eu.europa.ec.dynamicdiscovery.exception.DDCExceptionCode;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.routing.HttpRoutePlanner;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.net.URI;

/**
 * The default implementation of the {@link IDocumentFetcher} interface. This class is responsible for fetching the
 * metadata from the SMP server using the provided URI. It also handles the authentication and proxy settings.
 *
 * @author Flávio W. R. Santos
 * @author Erlend Klakegg Bergheim
 * @author Sebastian-Ion TINCU
 * @author Joze RIHTARSIC
 * @since 1.13
 */
public class DefaultURLFetcher extends AbstractURLFetcher {

    private DefaultURLFetcher(HttpClientConnectionManager connectionManager,
                              HttpRoutePlanner routePlanner,
                              ICredentialProvider credentialProvider,
                              IProxyConfiguration proxyConfiguration) {
        super(connectionManager, routePlanner, credentialProvider, proxyConfiguration);
    }

    @Override
    public FetcherResponse fetch(URI documentURI) throws TechnicalException {
        LOG.debug("Fetch data for participantURI [{}]", documentURI);

        String targetHostname = documentURI.getHost();
        RequestConfig requestConfig = createRequestConfig(targetHostname);
        CloseableHttpClient httpClient = createHttpClient(documentURI);
        HttpGet httpGet = new HttpGet(documentURI);
        httpGet.setConfig(requestConfig);

        try {
            InputStream inputStream = connect(httpClient, httpGet);
            return new FetcherResponse(inputStream, documentURI);
        } catch (TechnicalException e) {
            e.setSmpExceptionCode(DDCExceptionCode.SERVICE_GROUP);
            throw e;
        }
    }

    public static class Builder extends AbstractFetcherBuilder<Builder> {


        public Builder() {
            super();
        }

        public Builder(SSLContext sslContext) {
            super(sslContext);
        }

        @Override
        protected Builder self() {
            return this;
        }

        /**
         * Build DefaultURLFetcher
         *
         * @return configured URL fetcher
         */
        @Override
        public DefaultURLFetcher build() {
            final HttpClientConnectionManager connectionManager = buildHttpClientConnectionManager();
            return new DefaultURLFetcher(connectionManager, routePlanner, credentialProvider, proxyConfiguration);
        }
    }
}
