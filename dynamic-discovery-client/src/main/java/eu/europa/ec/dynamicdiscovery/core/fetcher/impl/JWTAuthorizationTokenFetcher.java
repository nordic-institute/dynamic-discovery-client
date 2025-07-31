/*
 * #%L
 * dynamic-discovery-cli
 * %%
 * Copyright (C) 2025 - 2025 European Commission | eDelivery | Dynamic Discovery Client
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

import eu.europa.ec.dynamicdiscovery.core.security.ICredentialProvider;
import eu.europa.ec.dynamicdiscovery.core.security.IProxyConfiguration;
import eu.europa.ec.dynamicdiscovery.core.fetcher.JwtResponse;
import eu.europa.ec.dynamicdiscovery.exception.DDCExceptionCode;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.routing.HttpRoutePlanner;
import org.apache.hc.core5.http.io.entity.StringEntity;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;


/**
 * Fetches a JWT authorization token from a specified URI.
 *
 * @author Joze Rihtarsic
 * @since 3.1
 */
public class JWTAuthorizationTokenFetcher extends AbstractURLFetcher {

    private final String clientId;

    private JWTAuthorizationTokenFetcher(HttpClientConnectionManager connectionManager,
                                         HttpRoutePlanner routePlanner,
                                         ICredentialProvider credentialProvider,
                                         IProxyConfiguration proxyConfiguration,
                                         String clientId) {
        super(connectionManager, routePlanner, credentialProvider, proxyConfiguration);
        this.clientId = clientId;
    }

    @Override
    public JwtResponse fetch(URI documentURI) throws TechnicalException {
        String body = "grant_type=client_credentials&client_id="+ clientId+"&scope=oots-smp-domain oots-smp-group-be";
        return fetchToken(documentURI, body);
    }

    /**
     * Fetches a JWT token from the given URI.
     *
     * @param tokenEndpoint The URI of the token endpoint.
     * @param requestBody   The request body to send (e.g., client credentials).
     * @return The JWT token as a string.
     * @throws TechnicalException If an error occurs during the fetch.
     */
    public JwtResponse fetchToken(URI tokenEndpoint, String requestBody) throws TechnicalException {


        String targetHostname = tokenEndpoint.getHost();
        RequestConfig requestConfig = createRequestConfig(targetHostname);
        CloseableHttpClient httpClient = createHttpClient(tokenEndpoint);
        HttpPost httpPost = new HttpPost(tokenEndpoint);
        httpPost.setEntity(new StringEntity(requestBody, Charset.defaultCharset()));
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setConfig(requestConfig);

        try {
            InputStream inputStream = connect(httpClient, httpPost);
            return new JwtResponse(inputStream);
        } catch (TechnicalException e) {
            e.setSmpExceptionCode(DDCExceptionCode.SERVICE_GROUP);
            throw e;
        }
    }


    public static class Builder extends AbstractFetcherBuilder<Builder> {
        String clientId;

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

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        @Override
        public JWTAuthorizationTokenFetcher build() {
            final HttpClientConnectionManager connectionManager = buildHttpClientConnectionManager();
            return new JWTAuthorizationTokenFetcher(connectionManager, routePlanner,
                    credentialProvider,
                    proxyConfiguration,
                    clientId);
        }
    }
}