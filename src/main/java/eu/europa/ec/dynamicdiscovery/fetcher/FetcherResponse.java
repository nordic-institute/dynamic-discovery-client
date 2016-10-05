package eu.europa.ec.dynamicdiscovery.fetcher;

import java.io.InputStream;

/**
 * Created by rodrfla on 30/09/2016.
 */
public class FetcherResponse {
    private InputStream inputStream;
    private String namespace;

    public FetcherResponse(InputStream inputStream, String namespace) {
        this.inputStream = inputStream;
        this.namespace = namespace;
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    public String getNamespace() {
        return this.namespace;
    }
}
