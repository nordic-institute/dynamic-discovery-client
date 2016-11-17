/*
 * (C) Copyright 2016 Dynamic Discovery Client
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

public class DefaultProxy implements IProxyConfiguration {

    private String user;
    private String password;
    private String serverAddress;
    private int serverPort;
    private HttpClient httpclient;
    private HttpGet httpget;

    public DefaultProxy(String serverAddress, int serverPort, String user, String password) throws ConnectionException {
        if (StringUtils.isEmpty(user) || StringUtils.isEmpty(password)) {
            throw new ConnectionException("UserCredential for Proxy Authentication is missing.");
        }

        if (StringUtils.isEmpty(serverAddress) || serverPort == 0) {
            throw new ConnectionException("Server configuration for Proxy Authentication is missing.");
        }

        this.user = user;
        this.password = password;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    @Override
    public void build(URI uri) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                new AuthScope(this.serverAddress, this.serverPort),
                new UsernamePasswordCredentials(this.user, this.password));
        this.httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credentialsProvider).build();
        HttpHost proxy = new HttpHost(this.serverAddress, this.serverPort);
        this.httpget = new HttpGet(uri);
        httpget.setConfig(RequestConfig.custom()
                .setProxy(proxy)
                .build());
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
