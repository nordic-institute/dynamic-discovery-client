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

import eu.europa.ec.dynamicdiscovery.core.fetcher.JwtResponse;
import eu.europa.ec.dynamicdiscovery.exception.DDCExceptionCode;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.util.Args;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOG = LoggerFactory.getLogger(JWTAuthorizationTokenFetcher.class);
   private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String CONTENT_TYPE_FORM_URLENCODED = "application/x-www-form-urlencoded";
    private static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";

    private final String clientId;
    private final String scopes;

    private JWTAuthorizationTokenFetcher(Builder builder) {
        super(builder);
        Args.notBlank(builder.clientId, "Client ID must not be blank");
        this.clientId = builder.clientId;
        this.scopes = builder.scopes;
    }

    @Override
    public JwtResponse fetch(URI documentURI) throws TechnicalException {
        LOG.debug("Fetch JWT token for clientId [{}] from URI [{}]", clientId, documentURI);
        // build the request body for client credentials grant type
        StringBuilder bodyBuilder = new StringBuilder("grant_type=")
                .append(GRANT_TYPE_CLIENT_CREDENTIALS)
                .append("&client_id=")
                .append(clientId);
        if (StringUtils.isNotBlank(scopes)) {
            bodyBuilder.append("&scope=")
                    .append(scopes);
        }

        String body = bodyBuilder.toString();
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
        httpPost.setHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_FORM_URLENCODED);
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
        String scopes;

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

        public Builder scopes(String scopes) {
            this.scopes = scopes;
            return this;
        }

        @Override
        public JWTAuthorizationTokenFetcher build() {
            return new JWTAuthorizationTokenFetcher(this);
        }
    }
}