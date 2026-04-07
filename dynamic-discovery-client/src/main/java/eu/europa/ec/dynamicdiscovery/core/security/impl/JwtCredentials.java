/*
 * #%L
 * dynamic-discovery-cli
 * %%
 * Copyright (C) 2025 - 2025 European Commission | eDelivery | Dynamic Discovery Client
 * %%
 * Licensed under the LGPL, Version 2.1 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * [PROJECT_HOME]\license\lgpl2-1\license.txt or https://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package eu.europa.ec.dynamicdiscovery.core.security.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.hc.client5.http.auth.Credentials;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Represents JWT credentials used for authentication.
 * This class is serializable to allow for easy storage and transmission of JWT credentials.
 *
 * @author Joze Rihtarsic
 * @since 3.1
 */
public class JwtCredentials implements Credentials, Serializable {


    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("refresh_expires_in")
    private int refreshExpiresIn;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("not-before-policy")
    private int notBeforePolicy;

    @JsonProperty("scope")
    private String scope;

    @Override
    public Principal getUserPrincipal() {
        return () -> "jwtfp:" + tokenFingerprint(accessToken); // JWT tokens do not have a specific user principal. Avoid leaking the raw JWT
    }

    @Override
    public char[] getPassword() {
        return null; // JWT tokens do not have a password
    }

    // Getters and setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public int getRefreshExpiresIn() {
        return refreshExpiresIn;
    }

    public void setRefreshExpiresIn(int refreshExpiresIn) {
        this.refreshExpiresIn = refreshExpiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int getNotBeforePolicy() {
        return notBeforePolicy;
    }

    public void setNotBeforePolicy(int notBeforePolicy) {
        this.notBeforePolicy = notBeforePolicy;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * Checks if the JWT token is expired based on the current time.
     *
     * @return true if the token is expired, false otherwise
     */
    public boolean isExpired() {
        long currentTime = System.currentTimeMillis() / 1000; // Convert to seconds
        return (expiresIn > 0 && (currentTime - notBeforePolicy) >= expiresIn);
    }

    @Override
    public String toString() {
        return "JwtCredentials{" +
                "accessToken='[redacted fingerprint=" + tokenFingerprint(accessToken) + "]'" +
                ", expiresIn=" + expiresIn +
                ", refreshExpiresIn=" + refreshExpiresIn +
                ", tokenType='" + tokenType + '\'' +
                ", notBeforePolicy=" + notBeforePolicy +
                ", scope='" + scope + '\'' +
                '}';
    }

    private static String tokenFingerprint(String token) {
        if (token == null) return "null";
        if (token.isEmpty()) return "empty";

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));

            int bytesToEncode = Math.min(12, hash.length);
            byte[] truncated = Arrays.copyOf(hash, bytesToEncode);
            return Hex.toHexString(truncated);
        } catch (NoSuchAlgorithmException e) {
            return "unavailable";
        }
    }
}
