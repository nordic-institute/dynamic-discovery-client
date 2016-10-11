package eu.europa.ex.dynamicdiscovery.service;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.github.tomakehurst.wiremock.client.WireMock.post;

/**
 * Created by rodrfla on 10/10/2016.
 */
public class WebServerMock {
    private static Logger logger = LoggerFactory.getLogger(WebServerMock.class);

    public WebServerMock() {
        MockitoAnnotations.initMocks(this);
    }

    public void requestByPost(String url, String body) {
        WireMock.stubFor(post(WireMock.urlEqualTo(url))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/soap+xml")
                        .withBody(body)));

        logger.info("Requesting POST \n" +
                body);
    }

    public InputStream responseByPost(String url, String body) throws IOException {
        WireMock.stubFor(post(WireMock.urlEqualTo(url))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/soap+xml")
                        .withBody(body)));

        if (!url.contains("http://localhost:8080")) {
            url = "http://localhost:8080/" + url;
        }
        URL obj = new URL("http://localhost:8080/" + url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");

        InputStream inputStream = con.getInputStream();
        return inputStream;
    }

}
