package eu.europa.ec.dynamicdiscovery.core.security.impl;

import eu.europa.ec.dynamicdiscovery.core.fetcher.JwtResponse;
import eu.europa.ec.dynamicdiscovery.core.fetcher.impl.JWTAuthorizationTokenFetcher;
import eu.europa.ec.dynamicdiscovery.exception.ConnectionException;
import eu.europa.ec.dynamicdiscovery.exception.DDCAuthorizationException;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtTokenCredentialProviderTest {

    private static final URI AUTHORIZATION_SERVER = URI.create("https://auth.example/token");

    private static JwtTokenCredentialProvider buildProvider(JWTAuthorizationTokenFetcher fetcher) {
        return new JwtTokenCredentialProvider.Builder()
                .authorizationServerURI(AUTHORIZATION_SERVER.toString())
                .jwtFetcher(fetcher)
                .build();
    }

    @Test
    void cachesUnexpiredCredentials() throws Exception {
        JWTAuthorizationTokenFetcher fetcher = mock(JWTAuthorizationTokenFetcher.class);
        JwtResponse response = mock(JwtResponse.class);
        JwtCredentials credentials = new JwtCredentials();
        credentials.setAccessToken("token");
        credentials.setExpiresIn(3600);
        when(fetcher.fetch(AUTHORIZATION_SERVER)).thenReturn(response);
        when(response.getJwtCredentials()).thenReturn(credentials);

        JwtTokenCredentialProvider provider = buildProvider(fetcher);

        assertSame(credentials, provider.getCredentials());
        assertSame(credentials, provider.getCredentials());
        verify(fetcher, times(1)).fetch(AUTHORIZATION_SERVER);
    }

    @Test
    void doesNotCacheCredentialsWithoutExpiresIn() throws Exception {
        JWTAuthorizationTokenFetcher fetcher = mock(JWTAuthorizationTokenFetcher.class);
        JwtResponse response = mock(JwtResponse.class);
        JwtCredentials credentials = new JwtCredentials();
        credentials.setAccessToken("token");
        when(fetcher.fetch(AUTHORIZATION_SERVER)).thenReturn(response);
        when(response.getJwtCredentials()).thenReturn(credentials);

        JwtTokenCredentialProvider provider = buildProvider(fetcher);

        assertSame(credentials, provider.getCredentials());
        assertSame(credentials, provider.getCredentials());
        verify(fetcher, times(2)).fetch(AUTHORIZATION_SERVER);
    }

    @Test
    void throwsWhenAccessTokenIsBlank() throws Exception {
        JWTAuthorizationTokenFetcher fetcher = mock(JWTAuthorizationTokenFetcher.class);
        JwtResponse response = mock(JwtResponse.class);
        JwtCredentials credentials = new JwtCredentials();
        credentials.setAccessToken(" ");
        credentials.setExpiresIn(3600);
        when(fetcher.fetch(AUTHORIZATION_SERVER)).thenReturn(response);
        when(response.getJwtCredentials()).thenReturn(credentials);

        JwtTokenCredentialProvider provider = buildProvider(fetcher);

        DDCAuthorizationException exception = assertThrows(DDCAuthorizationException.class, provider::getCredentials);
        assertTrue(exception.getMessage().contains("does not contain an access token"));
    }

    @Test
    void throwsWhenTokenTypeIsNotBearer() throws Exception {
        JWTAuthorizationTokenFetcher fetcher = mock(JWTAuthorizationTokenFetcher.class);
        JwtResponse response = mock(JwtResponse.class);
        JwtCredentials credentials = new JwtCredentials();
        credentials.setAccessToken("token");
        credentials.setTokenType("MAC");
        when(fetcher.fetch(AUTHORIZATION_SERVER)).thenReturn(response);
        when(response.getJwtCredentials()).thenReturn(credentials);

        JwtTokenCredentialProvider provider = buildProvider(fetcher);

        DDCAuthorizationException exception = assertThrows(DDCAuthorizationException.class, provider::getCredentials);
        assertTrue(exception.getMessage().contains("Unsupported token type [MAC]"));
    }

    @Test
    void acceptsBearerTokenTypeIgnoringCase() throws Exception {
        JWTAuthorizationTokenFetcher fetcher = mock(JWTAuthorizationTokenFetcher.class);
        JwtResponse response = mock(JwtResponse.class);
        JwtCredentials credentials = new JwtCredentials();
        credentials.setAccessToken("token");
        credentials.setTokenType("bearer");
        when(fetcher.fetch(AUTHORIZATION_SERVER)).thenReturn(response);
        when(response.getJwtCredentials()).thenReturn(credentials);

        JwtTokenCredentialProvider provider = buildProvider(fetcher);

        assertSame(credentials, provider.getCredentials());
    }

    @Test
    void throwsWhenResponseIsNull() throws Exception {
        JWTAuthorizationTokenFetcher fetcher = mock(JWTAuthorizationTokenFetcher.class);
        when(fetcher.fetch(AUTHORIZATION_SERVER)).thenReturn(null);

        JwtTokenCredentialProvider provider = buildProvider(fetcher);

        DDCAuthorizationException exception = assertThrows(DDCAuthorizationException.class, provider::getCredentials);
        assertTrue(exception.getMessage().contains(AUTHORIZATION_SERVER.toString()));
    }

    @Test
    void throwsWhenCredentialsCouldNotBeParsed() throws Exception {
        JWTAuthorizationTokenFetcher fetcher = mock(JWTAuthorizationTokenFetcher.class);
        JwtResponse response = mock(JwtResponse.class);
        when(fetcher.fetch(AUTHORIZATION_SERVER)).thenReturn(response);
        when(response.getJwtCredentials()).thenReturn(null);

        JwtTokenCredentialProvider provider = buildProvider(fetcher);

        assertThrows(DDCAuthorizationException.class, provider::getCredentials);
    }

    @Test
    void wrapsFetcherFailure() throws Exception {
        JWTAuthorizationTokenFetcher fetcher = mock(JWTAuthorizationTokenFetcher.class);
        ConnectionException cause = new ConnectionException("connection refused");
        when(fetcher.fetch(AUTHORIZATION_SERVER)).thenThrow(cause);

        JwtTokenCredentialProvider provider = buildProvider(fetcher);

        DDCAuthorizationException exception = assertThrows(DDCAuthorizationException.class, provider::getCredentials);
        assertSame(cause, exception.getCause());
    }
}
