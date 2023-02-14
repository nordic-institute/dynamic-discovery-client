/*
 * (C) Copyright 2016-2021 - European Commission | Dynamic Discovery Client
 *
 * https://ec.europa.eu/cefdigital/code/projects/EDELIVERY/repos/dynamic-discovery-client/browse
 *
 * Licensed under the LGPL, Version 2.1 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     dynamic-discovery\License_LGPL-2.1.txt or https://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.europa.ec.dynamicdiscovery.model;

import eu.europa.ec.dynamicdiscovery.core.reader.DataWrapper;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPProcessIdentifier;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Flávio W. R. Santos
 * @author Erlend Klakegg Bergheim
 * @since 1.0
 */
public class SMPServiceMetadata implements DataWrapper {
    private SMPParticipantIdentifier participantIdentifier;
    private SMPDocumentIdentifier documentIdentifier;
    private X509Certificate signerCertificate;
    private Object dataObject;
    private List<SMPEndpoint> endpoints;

    protected SMPServiceMetadata(Builder builder) {

        this.participantIdentifier = builder.participantIdentifier;
        this.documentIdentifier = builder.documentIdentifier;
        this.endpoints = builder.endpoints;
        this.dataObject = builder.dataObject;
        this.signerCertificate = builder.signerCertificate;
    }

    public List<SMPEndpoint> getEndpoints() {
        return this.endpoints;
    }

    public Certificate getSignerCertificate() {
        return this.signerCertificate;
    }


    public SMPEndpoint getEndpoint(SMPProcessIdentifier processIdentifier, SMPTransportProfile... transportProfiles) {
        if (transportProfiles == null) {
            return null;
        }

        return this.endpoints.stream()
                .filter(endpoint -> endpointMatch(endpoint, processIdentifier, Arrays.asList(transportProfiles)))
                .findFirst().orElse(null);

    }

    protected boolean endpointMatch(SMPEndpoint endpoint, SMPProcessIdentifier processIdentifier, List<SMPTransportProfile> transportProfiles) {
        if (endpoint == null
                || endpoint.getProcessIdentifiers() == null
                || endpoint.getTransportProfile() == null
                || processIdentifier == null
                || transportProfiles == null
                || transportProfiles.isEmpty()) {
            return false;
        }

        return endpoint.getProcessIdentifiers().contains(processIdentifier)
                && transportProfiles.contains(endpoint.getTransportProfile());
    }


    public SMPParticipantIdentifier getParticipantIdentifier() {
        return this.participantIdentifier;
    }

    public SMPDocumentIdentifier getDocumentIdentifier() {
        return this.documentIdentifier;
    }


    @Override
    public <T> T unwrap(Class<T> iface) {
        return isWrapperFor(iface) ? (T) dataObject : null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        return dataObject != null && dataObject.getClass().isAssignableFrom(iface);
    }

    public static class Builder {

        private SMPParticipantIdentifier participantIdentifier;
        private SMPDocumentIdentifier documentIdentifier;
        private X509Certificate signerCertificate;
        private Object dataObject;
        private List<SMPEndpoint> endpoints = new ArrayList<>();


        public Builder participantIdentifier(SMPParticipantIdentifier participantIdentifier) {
            this.participantIdentifier = participantIdentifier;
            return this;
        }

        public Builder documentIdentifier(SMPDocumentIdentifier documentIdentifier) {
            this.documentIdentifier = documentIdentifier;
            return this;
        }

        public Builder signerCertificate(X509Certificate signerCertificate) {
            this.signerCertificate = signerCertificate;
            return this;
        }

        public Builder object(Object dataObject) {
            this.dataObject = dataObject;
            return this;
        }

        public Builder addEndpoint(SMPEndpoint endpoint) {
            this.endpoints.add(endpoint);
            return this;
        }

        public Builder addEndpoints(List<SMPEndpoint> endpoints) {
            this.endpoints.addAll(endpoints);
            return this;
        }

        public SMPServiceMetadata build() {

            return new SMPServiceMetadata(this);
        }

    }

}
