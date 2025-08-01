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
import eu.europa.ec.dynamicdiscovery.exception.DDCRuntimeException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.routing.HttpRoutePlanner;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.DefaultHostnameVerifier;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.security.*;

/**
 * Abstract base class for building fetchers with SSL/TLS configuration and proxy support.
 * This class provides methods to configure TLS versions, cipher suites, hostname validation,
 * proxy settings, and credential providers.
 *
 * @param <T> the type of the builder extending this abstract class
 *
 * @author Joze Rihtarsic
 * @since 3.1
 */
public abstract class AbstractFetcherBuilder<T extends AbstractFetcherBuilder<T>> {
    protected SSLContextBuilder sslContextBuilder = SSLContexts.custom();
    protected SSLContext sslContext = null;
    protected String[] tlsVersions;
    protected String[] tlsCipherSuites;
    protected boolean noHostnameValidation;

    protected boolean httpSchemeEnabled = true;
    protected boolean httpsSchemeEnabled = true;

    protected IProxyConfiguration proxyConfiguration;
    protected ICredentialProvider credentialProvider;
    protected HttpRoutePlanner routePlanner;




    /**
     * Default constructor that initializes the custom SSLContextBuilder for TLS configuration with
     *  provided keystore and truststore TLS versions and cipher suites.
     */
    public AbstractFetcherBuilder() {
        sslContextBuilder = SSLContexts.custom();
    }

    /**
     * Creates a Builder that uses an existing SSLContext
     *
     * @param sslContext is the sslContext to be used
     */
    public AbstractFetcherBuilder(SSLContext sslContext) {
        this.sslContext = sslContext;
    }

    public T tlsVersions(final String... tlsVersions) {
        this.tlsVersions = tlsVersions;
        return self();
    }

    public T tlsTruststore(final KeyStore truststore) throws NoSuchAlgorithmException, KeyStoreException {
        this.sslContextBuilder.loadTrustMaterial(truststore, null);
        return self();
    }

    public T tlsKeystore(final KeyStore keystore, final char[] password) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        this.sslContextBuilder.loadKeyMaterial(keystore, password);
        return self();
    }

    public T tlsCipherSuites(final String... tlsCipherSuites) {
        this.tlsCipherSuites = tlsCipherSuites;
        return self();
    }

    public T noHostnameValidation(final boolean noHostnameValidation) {
        this.noHostnameValidation = noHostnameValidation;
        return self();
    }

    public T proxyConfiguration(final IProxyConfiguration proxyConfiguration) {
        this.proxyConfiguration = proxyConfiguration;
        return self();
    }

    public T credentialProvider(final ICredentialProvider credentialProvider) {
        this.credentialProvider = credentialProvider;
        return self();
    }

    public T routePlanner(final HttpRoutePlanner routePlanner) {
        this.routePlanner = routePlanner;
        return self();
    }

    public T httpSchemeEnabled(final boolean enableHttpScheme) {
        this.httpSchemeEnabled = enableHttpScheme;
        return self();
    }

    public T httpsSchemeEnabled(final boolean httpsSchemeEnabled) {
        this.httpsSchemeEnabled = httpsSchemeEnabled;
        return self();
    }

    /**
     * Builds the SSLConnectionSocketFactory with the configured TLS versions, cipher suites,
     * and hostname validation settings.
     *
     * @return a configured SSLConnectionSocketFactory
     * @throws DDCRuntimeException if there is an error in TLS configuration
     */
    protected SSLConnectionSocketFactory buildSSLConnectionSocketFactory() {
        try {
            SSLConnectionSocketFactoryBuilder builder = SSLConnectionSocketFactoryBuilder.create()
                    .setCiphers(this.tlsCipherSuites)
                    .setTlsVersions(this.tlsVersions)
                    .setHostnameVerifier(this.noHostnameValidation ? NoopHostnameVerifier.INSTANCE : new DefaultHostnameVerifier());

            if (this.sslContextBuilder != null) {
                builder = builder.setSslContext(this.sslContextBuilder.build());
            }
            if (this.sslContext != null) {
                builder = builder.setSslContext(this.sslContext);
            }
            return builder.build();
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            throw new DDCRuntimeException("TLS configuration error: " + ExceptionUtils.getRootCauseMessage(e), e);
        }
    }

    /**
     * Builds the HttpClientConnectionManager with the configured HTTP and HTTPS schemes.
     *
     * @return a configured HttpClientConnectionManager
     */
    protected HttpClientConnectionManager buildHttpClientConnectionManager() {

        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
        if (httpSchemeEnabled) {
            registryBuilder.register("http", new PlainConnectionSocketFactory());
        }
        if (httpsSchemeEnabled) {
            registryBuilder.register("https", buildSSLConnectionSocketFactory());
        }

        return  new BasicHttpClientConnectionManager(registryBuilder.build());
    }

    /**
     * Returns the current instance of the builder.
     * This method is used to allow method chaining in subclasses.
     *
     * @return the current instance of the builder
     */
    protected abstract T self();

    /**
     * Builds the fetcher instance based on the configured parameters.
     * Subclasses must implement this method to create the specific fetcher instance.
     * @return an instance of AbstractURLFetcher
     */
    public abstract AbstractURLFetcher build();

}