package eu.europa.ec.dynamicdiscovery.core.security.impl;

import org.apache.hc.client5.http.auth.Credentials;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccessTokenCredentialProviderTest {

    @Test
    void testConstructorAndGetCredentials() {
        // GIVEN
        String accessTokenName = "testTokenName";
        char[] accessTokenValue = "testTokenValue".toCharArray();

        // WHEN
        AccessTokenCredentialProvider provider = new AccessTokenCredentialProvider(accessTokenName, accessTokenValue);
        Credentials credentials = provider.getCredentials();

        // THEN
        assertNotNull(credentials, "Credentials should not be null");
        assertInstanceOf(UsernamePasswordCredentials.class, credentials, "Credentials should be of type UsernamePasswordCredentials");
        UsernamePasswordCredentials usernamePasswordCredentials = (UsernamePasswordCredentials) credentials;
        assertEquals(accessTokenName, usernamePasswordCredentials.getUserPrincipal().getName(), "Access token name should match");
        assertArrayEquals(accessTokenValue, usernamePasswordCredentials.getPassword(), "Access token value should match");
    }

    @Test
    void testNullAccessTokenValue() {
        // GIVEN
        String accessTokenName = "testTokenName";
        char[] accessTokenValue = null;

        // WHEN
        AccessTokenCredentialProvider provider = new AccessTokenCredentialProvider(accessTokenName, accessTokenValue);
        Credentials credentials = provider.getCredentials();

        // THEN
        assertNotNull(credentials, "Credentials should not be null");
        assertInstanceOf(UsernamePasswordCredentials.class, credentials, "Credentials should be of type UsernamePasswordCredentials");
        UsernamePasswordCredentials usernamePasswordCredentials = (UsernamePasswordCredentials) credentials;
        assertEquals(accessTokenName, usernamePasswordCredentials.getUserPrincipal().getName(), "Access token name should match");
        assertNull(usernamePasswordCredentials.getPassword(), "Access token value should be null");
    }
}