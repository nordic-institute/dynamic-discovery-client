package eu.europa.ec.dynamicdiscovery.core.fetcher.impl;

import eu.europa.ec.dynamicdiscovery.exception.ConnectionException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

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

    @Test
    void requestBodyIsFormUrlEncoded() throws Exception {
        JWTAuthorizationTokenFetcher fetcher = spy(new JWTAuthorizationTokenFetcher.Builder()
                .clientId("client:id&x=y")
                .scopes("openid profile")
                .build());
        doReturn(null).when(fetcher).fetchToken(any(URI.class), anyString());

        fetcher.fetch(HTTPS_TOKEN_ENDPOINT);

        ArgumentCaptor<String> body = ArgumentCaptor.forClass(String.class);
        verify(fetcher).fetchToken(eq(HTTPS_TOKEN_ENDPOINT), body.capture());
        assertEquals("grant_type=client_credentials&client_id=client%3Aid%26x%3Dy&scope=openid+profile", body.getValue());
    }

    @Test
    void scopeIsOmittedWhenBlank() throws Exception {
        JWTAuthorizationTokenFetcher fetcher = spy(new JWTAuthorizationTokenFetcher.Builder()
                .clientId("client-id")
                .scopes(" ")
                .build());
        doReturn(null).when(fetcher).fetchToken(any(URI.class), anyString());

        fetcher.fetch(HTTPS_TOKEN_ENDPOINT);

        ArgumentCaptor<String> body = ArgumentCaptor.forClass(String.class);
        verify(fetcher).fetchToken(eq(HTTPS_TOKEN_ENDPOINT), body.capture());
        assertEquals("grant_type=client_credentials&client_id=client-id", body.getValue());
    }
}
