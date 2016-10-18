package eu.europa.ec.dynamicdiscovery.core.fetcher;

import java.io.InputStream;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 * @author Erlend Klakegg Bergheim - erlend.klakegg.bergheim@difi.no
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
