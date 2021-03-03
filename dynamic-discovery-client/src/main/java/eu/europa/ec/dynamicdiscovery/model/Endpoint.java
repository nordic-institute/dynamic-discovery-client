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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.security.cert.X509Certificate;

/**
 * @author Flávio W. R. Santos
 * @author Erlend Klakegg Bergheim
 *
 * @deprecated Replaced by {@link org.oasis_open.docs.bdxr.ns.smp._2016._05.EndpointType}
 */
@Deprecated
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


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Endpoint) {
            Endpoint endpoint = (Endpoint) obj;
            return new EqualsBuilder()
                    .append(processIdentifier, endpoint.getProcessIdentifier())
                    .append(transportProfile, endpoint.getTransportProfile())
                    .append(address, endpoint.getAddress())
                    .append(certificate, endpoint.getCertificate())
                    .isEquals();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(processIdentifier)
                .append(transportProfile)
                .append(address)
                .append(certificate)
                .toHashCode();
    }
}
