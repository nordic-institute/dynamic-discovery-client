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

import eu.europa.ec.dynamicdiscovery.core.fetcher.IDocumentFetcher;
import eu.europa.ec.dynamicdiscovery.core.security.ICredentialProvider;
import eu.europa.ec.dynamicdiscovery.core.security.IProxyConfiguration;
import eu.europa.ec.dynamicdiscovery.core.security.impl.JwtCredentials;
import eu.europa.ec.dynamicdiscovery.core.security.impl.JwtTokenCredentialProvider;
import eu.europa.ec.dynamicdiscovery.exception.ConnectionException;
import eu.europa.ec.dynamicdiscovery.exception.DDCFetchException;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.util.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.hc.client5.http.UnsupportedSchemeException;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.Credentials;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.routing.HttpRoutePlanner;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.io.*;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.apache.commons.lang3.StringUtils.lowerCase;
import static org.apache.commons.lang3.StringUtils.startsWithAny;


/**
 * Abstract base class for URL fetchers that provides common functionality for fetching documents from URLs.
 * It handles proxy configuration, authentication, and connection management.
 *
 * @author Joze Rihtarsic
 * @since 3.1
 */
public abstract class AbstractURLFetcher implements IDocumentFetcher {
    protected static final Logger LOG = LoggerFactory.getLogger(AbstractURLFetcher.class);

    protected final IProxyConfiguration proxyConfiguration;
    protected final ICredentialProvider credentialProvider;
    protected final HttpRoutePlanner routePlanner;
    protected final HttpClientConnectionManager connectionManager;

    protected AbstractURLFetcher(AbstractFetcherBuilder<?> builder) {
        this.connectionManager = builder.buildHttpClientConnectionManager();
        this.routePlanner = builder.routePlanner;
        this.credentialProvider = builder.credentialProvider;
        this.proxyConfiguration = builder.proxyConfiguration;
    }


    protected BasicCredentialsProvider buildAuthenticationForTarget(URI targetUri, BasicCredentialsProvider provider) {
        if (credentialProvider == null) {
            LOG.debug("No credential provider set for target url [{}].", targetUri);
            return provider;
        }

        return buildAuthenticationForTarget(
                new HttpHost(targetUri.getScheme(), targetUri.getHost(), targetUri.getPort()),
                credentialProvider.getCredentials(), provider);
    }

    protected CloseableHttpClient createHttpClient(URI documentURI) {
        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setRoutePlanner(routePlanner);


        // set authentication for target uri
        BasicCredentialsProvider credentialsProvider = buildAuthenticationForTarget(documentURI, null);
        // set proxy
        String participantUnderSmpURIHost = documentURI.getHost();
        if (proxyConfiguration != null && !proxyConfiguration.isNonProxyHost(participantUnderSmpURIHost)) {
            LOG.debug("Fetch data using proxy");
            HttpHost proxyHost = proxyConfiguration.getProxyHost(participantUnderSmpURIHost);
            // set proxy authentication
            credentialsProvider = buildAuthenticationForTarget(
                    proxyHost,
                    proxyConfiguration.getProxyCredentials(),
                    credentialsProvider);
        }

        if (credentialProvider != null) {
            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        }
        return httpClientBuilder.build();
    }

    protected RequestConfig createRequestConfig(String targetHostname) {
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        // set proxy
        if (proxyConfiguration != null && !proxyConfiguration.isNonProxyHost(targetHostname)) {
            LOG.debug("Fetch data using proxy for target hostname: [{}]", targetHostname);
            HttpHost proxyHost = proxyConfiguration.getProxyHost(targetHostname);
            // set proxy authentication
            requestConfigBuilder.setProxy(proxyHost);
        }

        return requestConfigBuilder.build();
    }

    protected BasicCredentialsProvider buildAuthenticationForTarget(HttpHost targetHost,
                                                                    Credentials credentials,
                                                                    BasicCredentialsProvider provider) {
        if (credentials == null) {
            LOG.debug("No credential provided for target url [{}].", targetHost);
            return provider;
        }

        if (StringUtils.equalsIgnoreCase(targetHost.getSchemeName(), "http")) {
            LOG.warn("Unsafe use of credentials for uri [{}].", targetHost);
        }
        if (provider == null) {
            provider = new BasicCredentialsProvider();
        }

        AuthScope authScope = new AuthScope(targetHost);
        provider.setCredentials(authScope, credentials);
        return provider;
    }

    /**
     * Connect to the SMP server and retrieve the data.
     *
     * @param httpClient  the http client to connect to the SMP server
     * @param httpRequest the http  request configuration
     * @return the fetcher response containing the data
     * @throws TechnicalException the technical exception
     */
    protected InputStream connect(CloseableHttpClient httpClient, HttpUriRequestBase httpRequest) throws TechnicalException {

        if (credentialProvider instanceof JwtTokenCredentialProvider) {
            JwtCredentials credentials = ((JwtTokenCredentialProvider) credentialProvider).getCredentials();
            httpRequest.setHeader("Authorization", "Bearer " + credentials.getAccessToken());
        }

        try (CloseableHttpResponse response = httpClient.execute(httpRequest)) {
            switch (response.getCode()) {
                case 200:
                    try (final BufferedInputStream bufferedInputStream = new BufferedInputStream(response.getEntity().getContent())) {
                        return toInMemoryFetcherResponse(bufferedInputStream);
                    }
                case 404:
                    throw new DNSLookupException("SMP lookup address " + httpRequest.getUri() + " not found - response 404");
                default:
                    throw new DDCFetchException("Got Http error code " + response.getCode() + " trying to access URL:" + httpRequest.getUri());
            }
        } catch (DNSLookupException exc) {
            throw exc;
        } catch (SSLException exc) {
            throw new ConnectionException("Error occurred while retrieving [" + httpRequest.getRequestUri() + "]: Error: [" + ExceptionUtils.getRootCauseMessage(exc) + "]", exc);
        } catch (SocketException | UnsupportedSchemeException exc) {
            throw new ConnectionException("TLS Error occurred while retrieving [" + httpRequest.getRequestUri() + "]: Error: [" + ExceptionUtils.getRootCauseMessage(exc) + "]", exc);
        } catch (Exception exc) {
            String message = "It was not able to retrieve data from SMP server using NAPTR record according to OASIS BDX specification.";
            String uri = lowerCase(getUriFromHttpRequest(httpRequest));
            if (Strings.CI.startsWithAny(uri, "http://b-", "https://b-")) {
                message = "It was not able to retrieve data from SMP server using CNAME record according to PEPPOL BUSDOX specification.";
            }
            throw new DNSLookupException(message, exc);
        }
    }

    /**
     * Convert input stream to in-memory bytearray response. This is used to avoid the need to keep the connection open
     * while processing the data. The input stream is closed after the data is read.
     * <p>
     * The SMP documents are expected to be small, so loading the entire response in memory is acceptable, but
     * future versions may consider using a different approach to avoid loading the entire response in memory.
     *
     * @param inputStream input stream from the document source
     * @return in-memory fetcher response
     * @throws IOException if an I/O error occurs
     */
    protected InputStream toInMemoryFetcherResponse(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            IOUtils.copy(inputStream, baos);
            return new ByteArrayInputStream(baos.toByteArray());
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
}
