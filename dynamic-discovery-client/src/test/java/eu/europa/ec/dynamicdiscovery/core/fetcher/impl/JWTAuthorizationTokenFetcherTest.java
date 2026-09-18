package eu.europa.ec.dynamicdiscovery.core.fetcher.impl;

import eu.europa.ec.dynamicdiscovery.exception.ConnectionException;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

class JWTAuthorizationTokenFetcherTest {

    private static final URI HTTP_TOKEN_ENDPOINT = URI.create("http://localhost/auth/token");
    private static final URI HTTPS_TOKEN_ENDPOINT = URI.create("https://localhost/auth/token");

    @Test
    void httpSchemeIsRejectedByDefault() {
        JWTAuthorizationTokenFetcher fetcher = new JWTAuthorizationTokenFetcher.Builder()
                .clientId("client-id")
                .build();

        ConnectionException exception = assertThrows(ConnectionException.class,
                () -> fetcher.fetchToken(HTTP_TOKEN_ENDPOINT, "grant_type=client_credentials"));

        assertTrue(exception.getMessage().contains("http protocol is not supported"),
                "Unexpected message: " + exception.getMessage());
    }

    @Test
    void httpsSchemeIsAcceptedByDefault() {
        JWTAuthorizationTokenFetcher fetcher = new JWTAuthorizationTokenFetcher.Builder()
                .clientId("client-id")
                .build();

        assertDoesNotThrow(() -> fetcher.validateUriScheme(HTTPS_TOKEN_ENDPOINT));
    }

    @Test
    void httpSchemeCanBeEnabledExplicitly() {
        JWTAuthorizationTokenFetcher fetcher = new JWTAuthorizationTokenFetcher.Builder()
                .clientId("client-id")
                .httpSchemeEnabled(true)
                .build();

        assertDoesNotThrow(() -> fetcher.validateUriScheme(HTTP_TOKEN_ENDPOINT));
    }
}
