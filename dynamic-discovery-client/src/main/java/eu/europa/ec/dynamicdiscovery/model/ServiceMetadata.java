/*
 * (C) Copyright 2016 Dynamic Discovery Client
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
 *
 * @author Flávio W. R. Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 * @author Erlend Klakegg Bergheim - erlend.klakegg.bergheim@difi.no
 *
 */
package eu.europa.ec.dynamicdiscovery.model;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ServiceMetadata {

    private ParticipantIdentifier participantIdentifier;
    private DocumentIdentifier documentIdentifier;
    private List<ProcessIdentifier> processIdentifiers;
    private List<TransportProfile> transportProfiles;
    private X509Certificate signer;
    private List<Endpoint> endpoints;

    public ServiceMetadata() {
        processIdentifiers = new ArrayList<>();
        transportProfiles = new ArrayList<>();
        endpoints = new ArrayList<>();
    }

    public ParticipantIdentifier getParticipantIdentifier() {
        return this.participantIdentifier;
    }

    public void setParticipantIdentifier(ParticipantIdentifier participantIdentifier) {
        this.participantIdentifier = participantIdentifier;
    }

    public DocumentIdentifier getDocumentIdentifier() {
        return this.documentIdentifier;
    }

    public void setDocumentIdentifier(DocumentIdentifier documentIdentifier) {
        this.documentIdentifier = documentIdentifier;
    }

    public List<ProcessIdentifier> getProcessIdentifiers() {
        return this.processIdentifiers;
    }

    public List<TransportProfile> getTransportProfiles() {
        return this.transportProfiles;
    }

    public void addEndpoint(Endpoint endpoint) {
        if (!this.processIdentifiers.contains(endpoint.getProcessIdentifier())) {
            this.processIdentifiers.add(endpoint.getProcessIdentifier());
        }

        this.endpoints.add(endpoint);
    }

    public List<Endpoint> getEndpoints() {
        return this.endpoints;
    }

    public Endpoint getEndpoint(ProcessIdentifier processIdentifier, TransportProfile... transportProfiles) {
        TransportProfile[] var3 = transportProfiles;
        int var4 = transportProfiles.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            TransportProfile transportProfile = var3[var5];
            Iterator var7 = this.endpoints.iterator();

            while (var7.hasNext()) {
                Endpoint endpoint = (Endpoint) var7.next();
                if (endpoint.getTransportProfile().equals(transportProfile) && endpoint.getProcessIdentifier().equals(processIdentifier)) {
                    return endpoint;
                }
            }
        }

        return null;
    }

    public X509Certificate getSigner() {
        return this.signer;
    }

    public void setSigner(X509Certificate signer) {
        this.signer = signer;
    }
}
