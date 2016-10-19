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

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 * @author Erlend Klakegg Bergheim - erlend.klakegg.bergheim@difi.no
 */
public class Endpoint {

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
