/*
 * Copyright 2016 Dynamic Discovery Client Project
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/servlets/Docbb6d.pdf?id=31979
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */
package eu.europa.ec.dynamicdiscovery.core.security;

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

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public class ProxyConfiguration {

    private String user;
    private String password;
    private String serverAddress;
    private int serverPort;
    private HttpClient httpclient;
    private HttpGet httpget;

    public ProxyConfiguration(String serverAddress, int serverPort, String user, String password) throws ConnectionException {
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

    public HttpClient getHttpclient() {
        return httpclient;
    }

    public HttpGet getHttpget() {
        return httpget;
    }
}
