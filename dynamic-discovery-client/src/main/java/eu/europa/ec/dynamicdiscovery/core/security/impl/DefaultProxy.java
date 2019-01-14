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
 *
 */
package eu.europa.ec.dynamicdiscovery.core.security.impl;

import eu.europa.ec.dynamicdiscovery.core.security.IProxyConfiguration;
import eu.europa.ec.dynamicdiscovery.exception.ConnectionException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import java.net.URI;
import java.util.Arrays;

import org.apache.log4j.Logger;

public class DefaultProxy implements IProxyConfiguration {
   final static Logger LOG = Logger.getLogger(DefaultProxy.class);

    private String user;
    private String password;
    private String serverAddress;
    private String[] nonProxyHosts;
    private int serverPort;
    private HttpClient httpclient;
    private HttpGet httpget;

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
        this.nonProxyHosts = StringUtils.isBlank(nonProxyHosts)?null: nonProxyHosts.split("\\|");
    }

    @Override
    public void build(URI uri) {
        // create get request
        this.httpget = new HttpGet(uri);

        // check if noproxy
        if(!doesTargetMatchNonProxy(uri.getHost())){
            // create host for proxy
            HttpHost proxy = new HttpHost(this.serverAddress, this.serverPort);
            // set credentials to http client if username exists
            if (!StringUtils.isEmpty(user)) {
                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(
                        new AuthScope(this.serverAddress, this.serverPort),
                        new UsernamePasswordCredentials(this.user, this.password));
                this.httpclient = HttpClients.custom()
                        .setDefaultCredentialsProvider(credentialsProvider).build();

            } else {
                this.httpclient = HttpClients.createDefault();
            }
            httpget.setConfig(RequestConfig.custom()
                    .setProxy(proxy)
                    .build());
        } else {
            this.httpclient = HttpClients.createDefault();
            this.httpget.setConfig(RequestConfig.custom().build());
        }


    }

    private boolean doesTargetMatchNonProxy(String uriHost) {
        int nphLength = this.nonProxyHosts != null ? this.nonProxyHosts.length : 0;
        if (nonProxyHosts == null || nphLength < 1) {
            LOG.debug("host:'"+uriHost+"' : DEFAULT (0 non proxy host)");
            return false;
        }
        for (String nonProxyHost : nonProxyHosts) {
            if (uriHost.matches((nonProxyHost.startsWith("*")?".":"")+nonProxyHost)) {
                LOG.debug(" host:'"+uriHost+"' matches nonProxyHost '"+nonProxyHost+"' : NO PROXY");
                return true;
            }
        }
        LOG.debug(" host:'"+uriHost+"' : DEFAULT  (no match of "+Arrays.toString(nonProxyHosts)+" non proxy host)");
        return false;
    }

    @Override
    public HttpClient getHttpclient() {
        return httpclient;
    }

    @Override
    public HttpGet getHttpget() {
        return httpget;
    }

}
