package eu.europa.ec.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.model.*;

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
    private List<ProcessIdentifier> processIdentifiers = new ArrayList();
    private List<TransportProfile> transportProfiles = new ArrayList();
    private X509Certificate signer;
    private List<Endpoint> endpoints = new ArrayList();

    public ServiceMetadata() {
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
