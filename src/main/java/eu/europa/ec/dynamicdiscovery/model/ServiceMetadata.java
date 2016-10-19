/*
 * Copyright 2016 Dynamic Discovery Client Project
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/servlets/Docbb6d.pdf?id=31979
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */
package eu.europa.ec.dynamicdiscovery.model;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 * @author Erlend Klakegg Bergheim - erlend.klakegg.bergheim@difi.no
 */
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
