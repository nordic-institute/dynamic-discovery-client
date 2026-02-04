package eu.europa.ec.dynamicdiscovery.core.security.impl;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class JwtCredentialsTest {

    @Test
    void fingerprintIsDeterministicAndChangesWithToken() {
        JwtCredentials credentials1 = new JwtCredentials();
        JwtCredentials credentials2 = new JwtCredentials();

        credentials1.setAccessToken("token-A");
        credentials2.setAccessToken("token-A");

        String fingerprint1 = credentials1.getUserPrincipal().getName();
        String fingerprint2 = credentials2.getUserPrincipal().getName();

        assertEquals(fingerprint1, fingerprint2, "Same token should produce same principal fingerprint");

        credentials2.setAccessToken("token-B");
        String fingerprint3 = credentials2.getUserPrincipal().getName();

        assertNotEquals(fingerprint1, fingerprint3, "Different token should produce different principal fingerprint");
    }

    @Test
    void toStringDoesNotExposeAccessToken() {
        JwtCredentials credentials = new JwtCredentials();
        String token = "header.payload.signature";
        credentials.setAccessToken(token);

        String value = credentials.toString();

        assertTrue(value.contains("accessToken='[redacted fingerprint="),
                "toString() should contain redacted accessToken with fingerprint");
        assertFalse(value.contains(token), "Access token must not be present in toString()");
    }

    @Test
    void toStringFingerprintHasExpectedFormat() {
        JwtCredentials credentials = new JwtCredentials();
        credentials.setAccessToken("header.payload.signature");

        String value = credentials.toString();

        Matcher matcher = Pattern.compile("accessToken='\\[redacted fingerprint=([^]]+)]'").matcher(value);
        assertTrue(matcher.find(), "toString() should contain redacted fingerprint in expected format");

        String fingerprint = matcher.group(1);
        assertEquals(24, fingerprint.length(), "Fingerprint should be 24 hex chars (12 bytes)");
        assertTrue(fingerprint.matches("[0-9a-f]+"), "Fingerprint should be lowercase hex");
    }

    @Test
    void userPrincipalDoesNotExposeAccessToken() {
        JwtCredentials credentials = new JwtCredentials();
        String token = "header.payload.signature";
        credentials.setAccessToken(token);

        String principalName = credentials.getUserPrincipal().getName();

        assertTrue(principalName.startsWith("jwtfp:"), "Principal name should be prefixed with jwtfp:");
        assertFalse(principalName.contains(token), "Principal name must not contain the raw token");
    }

    @Test
    void nullAccessTokenIsHandled() {
        JwtCredentials credentials = new JwtCredentials();
        credentials.setAccessToken(null);

        String value = credentials.toString();
        String principalName = credentials.getUserPrincipal().getName();

        assertTrue(value.contains("accessToken='[redacted fingerprint=null]'"), "Null token should render as 'null'");
        assertEquals("jwtfp:null", principalName, "Principal name should be stable for null token");
    }

    @Test
    void emptyAccessTokenIsHandled() {
        JwtCredentials credentials = new JwtCredentials();
        credentials.setAccessToken("");

        String value = credentials.toString();
        String principalName = credentials.getUserPrincipal().getName();

        assertTrue(value.contains("accessToken='[redacted fingerprint=empty]'"), "Empty token should render as 'empty'");
        assertEquals("jwtfp:empty", principalName, "Principal name should be stable for empty token");
    }
}
