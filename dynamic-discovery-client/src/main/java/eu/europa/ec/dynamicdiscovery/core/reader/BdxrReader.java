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
package eu.europa.ec.dynamicdiscovery.core.reader;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.security.XmldsigVerifier;
import eu.europa.ec.dynamicdiscovery.exception.BindException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.*;
import eu.europa.ec.dynamicdiscovery.model.ProcessIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ServiceMetadata;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BdxrReader extends AbstractReader {

    public BdxrReader() {
        super();
    }

    @Override
    public List<DocumentIdentifier> parseDocumentIdentifiers(FetcherResponse fetcherResponse) throws TechnicalException {
        try {
            List<DocumentIdentifier> documentIdentifiers = new ArrayList<>();
            Object object = jaxbContext.createUnmarshaller().unmarshal(CommonUtil.convertToSource(fetcherResponse.getInputStream()), ServiceGroup.class);
            ServiceGroup serviceGroup = (ServiceGroup) ((JAXBElement) object).getValue();
            ServiceMetadataReferenceCollectionType serviceMetadataReferenceCollection = serviceGroup.getServiceMetadataReferenceCollection();
            if (serviceGroup != null && serviceMetadataReferenceCollection != null) {
                List<ServiceMetadataReferenceType> serviceMetadataReference = serviceGroup.getServiceMetadataReferenceCollection().getServiceMetadataReferences();
                if (serviceMetadataReference != null && !serviceMetadataReference.isEmpty()) {
                    Iterator iterator = serviceGroup.getServiceMetadataReferenceCollection().getServiceMetadataReferences().iterator();
                    while (iterator.hasNext()) {
                        ServiceMetadataReferenceType reference = (ServiceMetadataReferenceType) iterator.next();
                        String[] parts = URLDecoder.decode(reference.getHref().split("/services/")[1], "UTF-8").split("::", 2);
                        documentIdentifiers.add(new DocumentIdentifier(parts[1], parts[0]));
                    }
                }
            }
            return documentIdentifiers;
        } catch (Exception exc) {
            throw new BindException(exc.getMessage(), exc);
        }
    }

    @Override
    public ServiceMetadata parseServiceMetadata(FetcherResponse fetcherResponse) throws TechnicalException {
        try {
            Document document = CommonUtil.parse(fetcherResponse.getInputStream());
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceMetadata unmarshalledServiceMetadata = null;
            Object result = unmarshaller.unmarshal(CommonUtil.convertToSource(document));

            ServiceMetadata serviceMetadata = new ServiceMetadata();
            if (result instanceof SignedServiceMetadata) {
                serviceMetadata.setSigner(XmldsigVerifier.verify(document));
                result = ((SignedServiceMetadata) result).getServiceMetadata();
            }

            if (!(result instanceof org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceMetadata)) {
                throw new BindException("ServiceMetadata element not found.");
            }

            unmarshalledServiceMetadata = (org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceMetadata) result;
            ServiceInformationType serviceInformation = unmarshalledServiceMetadata.getServiceInformation();
            serviceMetadata.setParticipantIdentifier(new ParticipantIdentifier(serviceInformation.getParticipantIdentifier().getValue(), serviceInformation.getParticipantIdentifier().getScheme()));
            serviceMetadata.setDocumentIdentifier(new DocumentIdentifier(serviceInformation.getDocumentIdentifier().getValue(), serviceInformation.getDocumentIdentifier().getScheme()));
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            Iterator iterator = serviceInformation.getProcessList().getProcesses().iterator();

            while (iterator.hasNext()) {
                ProcessType processType = (ProcessType) iterator.next();
                ProcessIdentifier processIdentifier = new ProcessIdentifier(processType.getProcessIdentifier().getValue(), processType.getProcessIdentifier().getScheme());
                Iterator var12 = processType.getServiceEndpointList().getEndpoints().iterator();

                while (var12.hasNext()) {
                    EndpointType endpointType = (EndpointType) var12.next();
                    serviceMetadata.addEndpoint(new Endpoint(processIdentifier, new TransportProfile(endpointType.getTransportProfile()), endpointType.getEndpointURI(), (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(endpointType.getCertificate()))));
                }
            }

            return serviceMetadata;

        } catch (ParserConfigurationException | IOException | SAXException | CertificateException | JAXBException exc) {
            throw new BindException(exc.getMessage(), exc);
        }
    }
}
