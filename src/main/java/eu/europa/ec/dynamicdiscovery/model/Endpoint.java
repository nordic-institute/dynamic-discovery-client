package eu.europa.ec.dynamicdiscovery.model;

import java.security.cert.X509Certificate;

/**
 * Created by rodrfla on 30/09/2016.
 */
public class Endpoint {
    private static final long serialVersionUID = 5892469135654700883L;
    private ProcessIdentifier processIdentifier;
    private TransportProfile transportProfile;
    private String address;
    private X509Certificate certificate;

    public Endpoint(ProcessIdentifier processIdentifier, TransportProfile transportProfile, String address, X509Certificate certificate) {
        this.processIdentifier = processIdentifier;
        this.transportProfile = transportProfile;
        this.address = address;
        this.certificate = certificate;
    }

    public ProcessIdentifier getProcessIdentifier() {
        return this.processIdentifier;
    }

    public TransportProfile getTransportProfile() {
        return this.transportProfile;
    }

    public String getAddress() {
        return this.address;
    }

    public X509Certificate getCertificate() {
        return this.certificate;
    }

    @Override
    public String toString() {
        return "Endpoint{" +
                "processIdentifier=" + processIdentifier +
                ", transportProfile=" + transportProfile +
                ", address='" + address + '\'' +
                ", certificate=" + certificate +
                '}';
    }
}
