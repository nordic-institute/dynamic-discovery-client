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
import eu.europa.ec.dynamicdiscovery.exception.ConnectionException;
import eu.europa.ec.dynamicdiscovery.exception.DDCRuntimeException;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.util.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.hc.client5.http.UnsupportedSchemeException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.routing.HttpRoutePlanner;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.DefaultHostnameVerifier;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.io.*;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.*;

import static org.apache.commons.lang3.StringUtils.lowerCase;
import static org.apache.commons.lang3.StringUtils.startsWithAny;

/**
 * @author Flávio W. R. Santos
 * @author Erlend Klakegg Bergheim
 * @author Sebastian-Ion TINCU
 * @since 1.13
 */
public class DefaultURLFetcher implements IMetadataFetcher {
    static final Logger LOG = LoggerFactory.getLogger(DefaultURLFetcher.class);

    private final IProxyConfiguration proxyConfiguration;

    private final HttpRoutePlanner routePlanner;

    private final HttpClientConnectionManager connectionManager;

    /**
     * @deprecated Use the DefaultURLFetcher.Builder to build the fetcher
     */
    @Deprecated
    public DefaultURLFetcher() {
        this(null, null, null);
    }

    /**
     * @param proxyConfiguration
     * @deprecated Use the DefaultURLFetcher.Builder to build the fetcher
     */
    @Deprecated
    public DefaultURLFetcher(IProxyConfiguration proxyConfiguration) {
        this(null, null, proxyConfiguration);
    }

    /**
     * @param routePlanner
     * @deprecated Use the DefaultURLFetcher.Builder to build the fetcher
     */
    @Deprecated
    public DefaultURLFetcher(HttpRoutePlanner routePlanner) {
        this(null, routePlanner, null);
    }

    /**
     * @param proxyConfiguration
     * @deprecated Use the DefaultURLFetcher.Builder to build the fetcher
     */
    @Deprecated
    public DefaultURLFetcher(HttpRoutePlanner routePlanner, IProxyConfiguration proxyConfiguration) {
        this(null, routePlanner, proxyConfiguration);
    }

    private DefaultURLFetcher(HttpClientConnectionManager connectionManager, HttpRoutePlanner routePlanner, IProxyConfiguration proxyConfiguration) {
        this.routePlanner = routePlanner;
        this.proxyConfiguration = proxyConfiguration;
        this.connectionManager = connectionManager;
    }

    @Override
    public FetcherResponse fetch(URI participantUnderSmpURI) throws TechnicalException {
        LOG.debug("Fetch data for participantURI [{}]", participantUnderSmpURI);

        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setRoutePlanner(routePlanner);
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();


        String participantUnderSmpURIHost = participantUnderSmpURI.getHost();
        if (proxyConfiguration != null && !proxyConfiguration.isNonProxyHost(participantUnderSmpURIHost)) {
            LOG.debug("Fetch data using proxy");
            httpClientBuilder.setDefaultCredentialsProvider(
                    proxyConfiguration.getProxyCredentials(participantUnderSmpURIHost));
            requestConfigBuilder.setProxy(proxyConfiguration.getProxyHost(participantUnderSmpURIHost));
        }

        HttpGet httpGet = new HttpGet(participantUnderSmpURI);
        httpGet.setConfig(requestConfigBuilder.build());

        return connect(httpClientBuilder.build(), httpGet);
    }

    public FetcherResponse connect(CloseableHttpClient httpClient, HttpGet httpGet) throws TechnicalException {
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            switch (response.getCode()) {
                case 200:
                    try (final BufferedInputStream bufferedInputStream = new BufferedInputStream(response.getEntity().getContent())) {
                        return toInMemoryFetcherResponse(bufferedInputStream);
                    }
                case 404:
                    throw new DNSLookupException("SMP lookup address " + httpGet.getUri() + " not found - response 404");
                default:
                    throw new DNSLookupException("Got Http error code " + response.getCode() + " trying to access SMP URL:" + httpGet.getUri());
            }
        } catch (DNSLookupException exc) {
            throw exc;
        } catch (SSLException exc) {
            throw new ConnectionException("Error occurred while retrieving [" + httpGet.getRequestUri() + "]: Error: [" + ExceptionUtils.getRootCauseMessage(exc) + "]", exc);
        } catch (SocketException | UnsupportedSchemeException exc) {
            throw new ConnectionException("TLS Error occurred while retrieving [" + httpGet.getRequestUri() + "]: Error: [" + ExceptionUtils.getRootCauseMessage(exc) + "]", exc);
        } catch (Exception exc) {
            String message = "It was not able to retrieve data from SMP server using NAPTR record according to OASIS BDX specification.";
            String uri = lowerCase(getUriFromHttpRequest(httpGet));
            if (startsWithAny(uri, "http://b-", "https://b-")) {
                message = "It was not able to retrieve data from SMP server using CNAME record according to PEPPOL BUSDOX specification.";
            }
            throw new DNSLookupException(message, exc);
        }
    }

    public FetcherResponse toInMemoryFetcherResponse(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            IOUtils.copy(inputStream, baos);
            return new FetcherResponse(new ByteArrayInputStream(baos.toByteArray()));
        }
    }


    public String getUriFromHttpRequest(HttpRequest httpRequest) {
        try {
            return httpRequest.getUri().toString();
        } catch (URISyntaxException e) {
            LOG.error("Error occurred while resolving http get url", e);
        }
        return null;
    }

    /**
     * Default Fetcher builder
     */
    public static class Builder {

        private SSLContextBuilder sslContextBuilder = SSLContexts.custom();
        private String[] tlsVersions;
        private String[] tlsCipherSuites;
        private boolean noHostnameValidation;

        private boolean httpSchemeEnabled = true;
        private boolean httpsSchemeEnabled = true;

        private IProxyConfiguration proxyConfiguration;

        private HttpRoutePlanner routePlanner;


        /**
         * Set list of allowed TLS versions as TLSv1.1, TLSv1.2, TLSv1.3 etc
         *
         * @param tlsVersions is array of allowed TLS versions
         * @return builder
         */
        public Builder tlsVersions(final String... tlsVersions) {
            this.tlsVersions = tlsVersions;
            return this;
        }

        /**
         * Set the server truststore
         *
         * @param truststore truststore with server trust anchors such as server certificate or issuer chain of server certificate
         * @return this builder
         * @throws NoSuchAlgorithmException
         * @throws KeyStoreException
         */

        public Builder tlsTruststore(final KeyStore truststore) throws NoSuchAlgorithmException, KeyStoreException {
            this.sslContextBuilder.loadTrustMaterial(truststore, null);
            return this;
        }

        /**
         * Set client keystore with the client key for mutual TLS authentication
         *
         * @param keystore keystore
         * @param password password for the certificate
         * @return this builder
         * @throws UnrecoverableKeyException
         * @throws NoSuchAlgorithmException
         * @throws KeyStoreException
         */
        public Builder tlsKeystore(final KeyStore keystore, final char[] password) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
            this.sslContextBuilder.loadKeyMaterial(keystore, password);
            return this;
        }

        /**
         * Allowed cipher suites
         *
         * @param tlsCipherSuites
         * @return this builder
         */
        public Builder tlsCipherSuites(final String... tlsCipherSuites) {
            this.tlsCipherSuites = tlsCipherSuites;
            return this;
        }

        /**
         * Set not host validation - should be used only for testing purposes.
         *
         * @param noHostnameValidation
         * @return this builder
         */
        public Builder noHostnameValidation(final boolean noHostnameValidation) {
            this.noHostnameValidation = noHostnameValidation;
            return this;
        }

        /**
         * Set proxy configuration.
         *
         * @param proxyConfiguration
         * @return this builder
         */
        public Builder proxyConfiguration(final IProxyConfiguration proxyConfiguration) {
            this.proxyConfiguration = proxyConfiguration;
            return this;
        }

        /**
         * Enhanced proxy settings
         *
         * @param routePlanner
         * @return this builder
         */
        public Builder routePlanner(final HttpRoutePlanner routePlanner) {
            this.routePlanner = routePlanner;
            return this;
        }

        /**
         * enable/disable to use http scheme
         *
         * @param enableHttpScheme
         * @return this builder
         */
        public Builder setHttpSchemeEnabled(final boolean enableHttpScheme) {
            this.httpSchemeEnabled = enableHttpScheme;
            return this;
        }

        /**
         * enable/disable to use https scheme
         *
         * @param httpsSchemeEnabled
         * @return this builder
         */
        public Builder setHttpsSchemeEnabled(final boolean httpsSchemeEnabled) {
            this.httpsSchemeEnabled = httpsSchemeEnabled;
            return this;
        }

        /**
         * Build DefaultURLFetcher
         *
         * @return configured URL fetcher
         */
        public DefaultURLFetcher build() {

            RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
            if (httpSchemeEnabled) {
                registryBuilder.register("http", new PlainConnectionSocketFactory());
            }
            if (httpsSchemeEnabled) {
                registryBuilder.register("https", buildSSLConnectionSocketFactory());
            }

            final BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(registryBuilder.build());
            return new DefaultURLFetcher(connectionManager, routePlanner, proxyConfiguration);
        }

        private SSLConnectionSocketFactory buildSSLConnectionSocketFactory() {
            try {
                return SSLConnectionSocketFactoryBuilder.create()
                        .setCiphers(this.tlsCipherSuites)
                        .setTlsVersions(this.tlsVersions)
                        .setHostnameVerifier(this.noHostnameValidation ? NoopHostnameVerifier.INSTANCE : new DefaultHostnameVerifier())
                        .setSslContext(this.sslContextBuilder.build())
                        .build();
            } catch (KeyManagementException | NoSuchAlgorithmException e) {
                throw new DDCRuntimeException("TLS configuration error: " + ExceptionUtils.getRootCauseMessage(e), e);
            }
        }
    }
}
