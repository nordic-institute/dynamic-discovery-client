/*
 * (C) Copyright 2016 - European Commission | Dynamic Discovery Client
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

import eu.europa.ec.dynamicdiscovery.exception.BindException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.service.impl.DynamicDiscoveryService;
import org.apache.log4j.Logger;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.EndpointType;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.ExtensionType;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.ProcessType;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.SignedServiceMetadataType;

import java.io.ByteArrayInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ServiceMetadata {
    final static Logger LOG = Logger.getLogger(ServiceMetadata.class);
    private ParticipantIdentifier participantIdentifier;
    private DocumentIdentifier documentIdentifier;
    private Certificate signer;
    private String responseBody;
    private SignedServiceMetadataType signedServiceMetadataType;
    private List<Endpoint> endpoints;
    private List<ProcessIdentifier> processIdentifiers;
    private List<TransportProfile> transportProfiles;

    public ServiceMetadata(SignedServiceMetadataType signedServiceMetadataType, Certificate certificate, String responseBody) throws TechnicalException {
        if (signedServiceMetadataType == null) {
            throw new IllegalStateException("SignedServiceMetadataType must be not null");
        }
        this.processIdentifiers = new ArrayList<>();
        this.transportProfiles = new ArrayList<>();
        this.endpoints = new ArrayList<>();
        this.signer = certificate;
        this.signedServiceMetadataType = signedServiceMetadataType;
        this.responseBody = responseBody;
        addParticipantIdentifier();
        addDocumentIdentifier();
        addEndpoint();
    }

    @Deprecated
    public List<ProcessIdentifier> getProcessIdentifiers() {
        return this.processIdentifiers;
    }

    @Deprecated
    public List<TransportProfile> getTransportProfiles() {
        return this.transportProfiles;
    }

    @Deprecated
    public List<Endpoint> getEndpoints() {
        return this.endpoints;
    }

    @Deprecated
    public Certificate getSigner() {
        return this.signer;
    }

    @Deprecated
    private void addParticipantIdentifier() {
        this.participantIdentifier = new ParticipantIdentifier(signedServiceMetadataType.getServiceMetadata().getServiceInformation().getParticipantIdentifier().getValue(), signedServiceMetadataType.getServiceMetadata().getServiceInformation().getParticipantIdentifier().getScheme());
    }

    @Deprecated
    private void addDocumentIdentifier() {
        this.documentIdentifier = new DocumentIdentifier(signedServiceMetadataType.getServiceMetadata().getServiceInformation().getDocumentIdentifier().getValue(), signedServiceMetadataType.getServiceMetadata().getServiceInformation().getDocumentIdentifier().getScheme());
    }

    @Deprecated
    private void addEndpoint() throws TechnicalException {
        try {
            Iterator processTypeIterator = signedServiceMetadataType.getServiceMetadata().getServiceInformation().getProcessList().getProcess().iterator();
            while (processTypeIterator.hasNext()) {
                ProcessType processType = (ProcessType) processTypeIterator.next();

                ProcessIdentifier processIdentifier = new ProcessIdentifier(processType.getProcessIdentifier().getValue(), processType.getProcessIdentifier().getScheme());
                LOG.debug("Found process: " +processIdentifier.getIdentifier());
                Iterator endpointTypeIterator = processType.getServiceEndpointList().getEndpoint().iterator();

                while (endpointTypeIterator.hasNext()) {
                    EndpointType endpointType = (EndpointType) endpointTypeIterator.next();
                    X509Certificate certificate = getX509Certificate(endpointType);
                    LOG.debug("Found transport for process: " +processIdentifier.getIdentifier()
                            + ", transport " + endpointType.getTransportProfile()
                            + ", url " + endpointType.getEndpointURI());
                    Endpoint endpoint = new Endpoint(processIdentifier, new TransportProfile(endpointType.getTransportProfile()), endpointType.getEndpointURI(), certificate);
                    if (!this.processIdentifiers.contains(endpoint.getProcessIdentifier())) {
                        this.processIdentifiers.add(endpoint.getProcessIdentifier());
                    }
                    this.endpoints.add(endpoint);
                }
            }
        } catch (Exception exc) {
            throw new BindException(exc.getMessage(), exc);
        }
    }

    private X509Certificate getX509Certificate(EndpointType endpointType) {
        try{
            return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(endpointType.getCertificate()));
        } catch (Exception e){
            return null;
        }
    }

    @Deprecated
    public Endpoint getEndpoint(ProcessIdentifier processIdentifier, TransportProfile... transportProfiles) {
        if (transportProfiles != null) {
            for (int i = 0; i < transportProfiles.length; ++i) {
                TransportProfile transportProfile = transportProfiles[i];
                if (this.endpoints != null) {
                    Iterator iterator = this.endpoints.iterator();
                    while (iterator.hasNext()) {
                        Endpoint endpoint = (Endpoint) iterator.next();
                        if (endpoint.getTransportProfile().equals(transportProfile) && endpoint.getProcessIdentifier().equals(processIdentifier)) {
                            return endpoint;
                        }
                    }
                }
            }
        }

        return null;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public ParticipantIdentifier getParticipantIdentifier() {
        return this.participantIdentifier;
    }

    public DocumentIdentifier getDocumentIdentifier() {
        return this.documentIdentifier;
    }

    public SignedServiceMetadataType getOriginalServiceMetadata() {
        return signedServiceMetadataType;
    }
}