package eu.europa.ec.dynamicdiscovery.security;

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
 * Created by rodrfla on 05/10/2016.
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
