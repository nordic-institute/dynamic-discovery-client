/*
 * Copyright 2017-2023 European Commission | eDelivery Dynamic Discovery Client
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence attached in file: LICENSE-EUPL-v1.2-EN.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 */
package eu.europa.ec.dynamicdiscovery.model;

import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPProcessIdentifier;
import eu.europa.ec.dynamicdiscovery.util.SMPEqualsBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.security.cert.X509Certificate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class is Endpoint model for Oasis SMP 1.0, Oasis SMP 2.0
 *
 * @author Flávio W. R. Santos
 * @author Erlend Klakegg Bergheim
 */
public class SMPEndpoint {
    public static final String DEFAULT_CERTIFICATE = "DEFAULT";

    private List<SMPProcessIdentifier> processIdentifiers;
    private SMPTransportProfile transportProfile;
    private String address;
    private Map<String, X509Certificate> mapCertificates;
    private OffsetDateTime activationDate;
    private OffsetDateTime expirationDate;

    private String serviceDescription;

    private String technicalContactUrl;
    private String technicalInformationUrl;

    private SMPEndpoint(Builder builder) {
        this.processIdentifiers = builder.processIdentifiers;
        this.transportProfile = builder.transportProfile;
        this.address = builder.address;
        this.mapCertificates = builder.mapCertificates;
        this.activationDate = builder.activationDate;
        this.expirationDate = builder.expirationDate;
        this.serviceDescription = builder.serviceDescription;
        this.technicalContactUrl = builder.technicalContactUrl;
        this.technicalInformationUrl = builder.technicalInformationUrl;
    }

    /**
     * Legacy method for Oasis SMP 1.0 where only one process identifier can be added to the endpoint.
     *
     * @return the first process identifier in the list of processIdentifiers.
     * @deprecated Oasis SMP 2.0 uses multiple processIdentifiers for the SMP endpoint, Use {@link #getProcessIdentifiers()} } instead )
     */
    @Deprecated
    public SMPProcessIdentifier getProcessIdentifier() {
        return (this.processIdentifiers != null && !this.processIdentifiers.isEmpty()) ? this.processIdentifiers.get(0) : null;
    }

    public List<SMPProcessIdentifier> getProcessIdentifiers() {
        return this.processIdentifiers;
    }

    public SMPTransportProfile getTransportProfile() {
        return this.transportProfile;
    }

    public String getAddress() {
        return this.address;
    }

    public X509Certificate getCertificate() {
        return mapCertificates == null || mapCertificates.isEmpty() ? null : this.mapCertificates.get(DEFAULT_CERTIFICATE);
    }

    public OffsetDateTime getActivationDate() {
        return activationDate;
    }

    public OffsetDateTime getExpirationDate() {
        return expirationDate;
    }

    public Map<String, X509Certificate> getCertificates() {
        return mapCertificates;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public String getTechnicalContactUrl() {
        return technicalContactUrl;
    }

    public String getTechnicalInformationUrl() {
        return technicalInformationUrl;
    }

    @Override
    public String toString() {
        return "Endpoint{" +
                "processIdentifier=" + processIdentifiers +
                ", transportProfile=" + transportProfile +
                ", address='" + address + '\'' +
                ", certificate=" + mapCertificates +
                '}';
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SMPEndpoint) {
            SMPEndpoint endpoint = (SMPEndpoint) obj;
            return new EqualsBuilder()
                    .append(transportProfile, endpoint.getTransportProfile())
                    .append(address, endpoint.getAddress())
                    .append(mapCertificates, endpoint.getCertificates())
                    .isEquals() &&
                    new SMPEqualsBuilder()
                            .append(processIdentifiers, endpoint.processIdentifiers).build();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(processIdentifiers)
                .append(transportProfile)
                .append(address)
                .append(mapCertificates)
                .toHashCode();
    }


    public static class Builder {
        private List<SMPProcessIdentifier> processIdentifiers = new ArrayList<>();
        private SMPTransportProfile transportProfile;
        private String address;
        private Map<String, X509Certificate> mapCertificates = new HashMap<>();
        private OffsetDateTime activationDate;
        private OffsetDateTime expirationDate;

        private String serviceDescription;

        private String technicalContactUrl;
        private String technicalInformationUrl;

        public Builder serviceDescription(String serviceDescription) {
            this.serviceDescription = serviceDescription;
            return this;
        }

        public Builder technicalInformationUrl(String technicalInformationUrl) {
            this.technicalInformationUrl = technicalInformationUrl;
            return this;
        }

        public Builder technicalContactUrl(String technicalContactUrl) {
            this.technicalContactUrl = technicalContactUrl;
            return this;
        }

        public Builder addProcessIdentifier(SMPProcessIdentifier processIdentifier) {
            this.processIdentifiers.add(processIdentifier);
            return this;
        }

        public Builder addProcessIdentifiers(List<SMPProcessIdentifier> processIdentifiers) {
            this.processIdentifiers.addAll(processIdentifiers);
            return this;
        }

        public Builder transportProfile(SMPTransportProfile transportProfile) {
            this.transportProfile = transportProfile;
            return this;
        }

        public Builder transportProfile(String identifier) {
            this.transportProfile = new SMPTransportProfile(identifier);

            return this;
        }


        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder addCertificate(String code, X509Certificate certificate) {
            this.mapCertificates.put(code, certificate);
            return this;
        }

        public Builder addCertificates(Map<String, X509Certificate> certificates) {
            this.mapCertificates.putAll(certificates);
            return this;
        }

        public Builder activationDate(OffsetDateTime activationDate) {
            this.activationDate = activationDate;
            return this;
        }

        public Builder expirationDate(OffsetDateTime expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

        public SMPEndpoint build() {
            return new SMPEndpoint(this);
        }

    }
}
