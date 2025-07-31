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
package eu.europa.ec.dynamicdiscovery.core.security.impl;

import eu.europa.ec.dynamicdiscovery.core.fetcher.JwtResponse;
import eu.europa.ec.dynamicdiscovery.core.fetcher.impl.JWTAuthorizationTokenFetcher;
import eu.europa.ec.dynamicdiscovery.core.security.ICredentialProvider;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;

import java.net.URI;

/**
 * A credential provider that fetches JWT tokens from a specified authorization server URI.
 *
 * @author Joze Rihtarsic
 * @since 3.1
 */
public class JwtTokenCredentialProvider implements ICredentialProvider {


    private final URI authorizationServerUri;
    private final JWTAuthorizationTokenFetcher jwtFetcher;
    JwtCredentials jwtCredentials;

    public JwtTokenCredentialProvider(Builder builder) {
        this.authorizationServerUri = builder.uri;
        this.jwtFetcher = builder.jwtFetcher;
    }

    public JwtCredentials getCredentials() {
        if (jwtCredentials == null || jwtCredentials.isExpired()) {
            jwtCredentials = fetchJwtCredentials();
        }
        return jwtCredentials;
    }

    private JwtCredentials fetchJwtCredentials() {
        JwtResponse response;
        try {
            response = jwtFetcher.fetch(authorizationServerUri);
        } catch (TechnicalException e) {
            throw new RuntimeException(e);
        }
        if (response == null || response.getJwtCredentials() == null) {
            return null;
        }
        return response.getJwtCredentials();
    }

    public static class Builder {
        private URI uri;
        private JWTAuthorizationTokenFetcher jwtFetcher;
        JwtCredentials jwtResponse;

        public Builder authorizationServerURI(String uri) {
            this.uri = URI.create(uri);
            return this;
        }

        public Builder jwtFetcher(JWTAuthorizationTokenFetcher jwtFetcher) {
            this.jwtFetcher = jwtFetcher;
            return this;
        }

        public JwtTokenCredentialProvider build() {
            if (uri == null || jwtFetcher == null) {
                throw new IllegalArgumentException("URI and JWT fetcher must be provided");
            }
            return new JwtTokenCredentialProvider(this);
        }
    }
}
