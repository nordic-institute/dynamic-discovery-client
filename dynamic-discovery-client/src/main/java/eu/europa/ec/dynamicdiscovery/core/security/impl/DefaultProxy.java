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
package eu.europa.ec.dynamicdiscovery.core.security.impl;

import eu.europa.ec.dynamicdiscovery.core.security.IProxyConfiguration;
import eu.europa.ec.dynamicdiscovery.exception.ConnectionException;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.core5.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A configuration for managing the host, credentials and non-proxy host definitions when setting up an HTTP proxy.
 *
 * @author Sebastian-Ion TINCU
 * @author Flávio W. R. Santos
 * @since 1.13
 */
public class DefaultProxy implements IProxyConfiguration {
    static final Logger LOG = LoggerFactory.getLogger(DefaultProxy.class);

    private final String user;
    private final String password;
    private final String serverAddress;
    private final String[] nonProxyHosts;
    private final int serverPort;

    public DefaultProxy(String serverAddress, int serverPort) throws ConnectionException {
        this(serverAddress, serverPort, null, null, null);
    }

    public DefaultProxy(String serverAddress, int serverPort, String user, String password) throws ConnectionException {
        this(serverAddress, serverPort, user, password, null);
    }

    public DefaultProxy(String serverAddress, int serverPort, String user, String password, String nonProxyHosts) throws ConnectionException {
        // test server configuration
        if (StringUtils.isEmpty(serverAddress) || serverPort == 0) {
            throw new ConnectionException("Server configuration for Proxy Authentication is missing.");
        }

        // if given username than also password must be given
        if (!StringUtils.isEmpty(user) && StringUtils.isEmpty(password)) {
            throw new ConnectionException("Password for Proxy user is missing.");
        }

        this.user = user;
        this.password = password;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.nonProxyHosts = StringUtils.isBlank(nonProxyHosts) ? null : nonProxyHosts.split("\\|");
    }

    @Override
    public boolean isNonProxyHost(String target) {
        int nphLength = this.nonProxyHosts != null ? this.nonProxyHosts.length : 0;
        if (nonProxyHosts == null || nphLength < 1) {
            LOG.debug("host [{}] DEFAULT (0 non proxy host)", target);
            return false;
        }
        for (String nonProxyHost : nonProxyHosts) {
            if (target.matches((nonProxyHost.startsWith("*") ? "." : "") + nonProxyHost)) {
                LOG.debug(" host [{}] matches nonProxyHost [{}] : NO PROXY", target, nonProxyHost);
                return true;
            }
        }
        LOG.debug(" host [{}] DEFAULT (no match of {} non proxy host)", target, nonProxyHosts);
        return false;
    }

    @Override
    public HttpHost getProxyHost(String target) {
        if (isNonProxyHost(target)) {
            LOG.info("No proxy required for host [{}]", target);
            return null;
        }

        HttpHost proxyHost = new HttpHost(this.serverAddress, this.serverPort);

        LOG.info("Configured proxy host [{}]", proxyHost);
        return proxyHost;
    }

    @Override
    public CredentialsProvider getProxyCredentials(String target) {
        if (isNonProxyHost(target)) {
            LOG.info("No credentials required for non-proxy host [{}]", target);
            return null;
        }

        if (StringUtils.isEmpty(user)) {
            LOG.info("Username is empty so no credentials to be returned");
            return null;
        }

        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                new AuthScope(this.serverAddress, this.serverPort),
                new UsernamePasswordCredentials(this.user, this.password.toCharArray()));

        LOG.info("Configured proxy credentials");
        return credentialsProvider;
    }
}
