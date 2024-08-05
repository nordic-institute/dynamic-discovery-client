/*
 * #%L
 * dynamic-discovery-cli
 * %%
 * Copyright (C) 2016 - 2023 European Commission | eDelivery | Dynamic Discovery Client
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
package eu.europa.ec.dynamicdiscovery.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the SMP redirect information.
 * It contains the redirect URL and the certificate used to sign the redirect.
 *
 * @author Joze Rihtarsic
 * @since 2.2
 */
public class SMPRedirect {

    private String redirectUrl;
    Map<String, X509Certificate> redirectCertificateMap;
    String certificateUID;

    private SMPRedirect(Builder builder) {
        this.redirectUrl = builder.redirectUrl;
        this.certificateUID = builder.certificateUID;
        this.redirectCertificateMap = builder.redirectCertificates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return new EqualsBuilder()
                .append(redirectUrl, this.redirectUrl)
                .append(certificateUID, this.certificateUID)
                .append(redirectCertificateMap, this.redirectCertificateMap)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(redirectUrl)
                .append(redirectCertificateMap)
                .append(certificateUID)
                .toHashCode();
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public Map<String, X509Certificate> getRedirectCertificate() {
        return redirectCertificateMap;
    }

    public String getCertificateUID() {
        return certificateUID;
    }

    public static class Builder {
        private String redirectUrl;
        private String certificateUID;
        private Map<String, X509Certificate> redirectCertificates = new HashMap<>();


        public Builder redirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
            return this;
        }

        public Builder certificateUID(String certificateUID) {
            this.certificateUID = certificateUID;
            return this;
        }

        public Builder addRedirectCertificates(Map<String, X509Certificate> redirectCertificates) {
            this.redirectCertificates.putAll(redirectCertificates);
            return this;
        }

        public Builder addRedirectCertificate(String key, X509Certificate redirectCertificate) {
            this.redirectCertificates.put(key, redirectCertificate);
            return this;
        }

        public SMPRedirect build() {
            return new SMPRedirect(this);
        }
    }
}
