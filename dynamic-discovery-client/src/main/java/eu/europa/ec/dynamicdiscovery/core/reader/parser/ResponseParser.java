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
 *
 */
package eu.europa.ec.dynamicdiscovery.core.reader.parser;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.security.AbstractSignatureValidator;
import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.exception.BindException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ServiceMetadata;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceGroup;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceMetadataReferenceType;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.SignedServiceMetadata;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URLDecoder;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ResponseParser {

    private Unmarshaller unmarshaller;
    private DocumentBuilderFactory documentBuilderFactory;
    private AbstractSignatureValidator signatureValidator;

    public ResponseParser(AbstractSignatureValidator signatureValidator) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceMetadata.class, SignedServiceMetadata.class, ServiceGroup.class);
            this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
            this.documentBuilderFactory.setNamespaceAware(true);
            this.unmarshaller = jaxbContext.createUnmarshaller();
            this.signatureValidator = signatureValidator;
        } catch (Exception exc) {
            throw new IllegalStateException(exc.getMessage(), exc);
        }
    }

    public ServiceMetadata parseServiceMetadata(FetcherResponse fetcherResponse) throws TechnicalException {
        try {
            Document document = this.documentBuilderFactory.newDocumentBuilder().parse(fetcherResponse.getInputStream());
            Object result = this.unmarshaller.unmarshal(document);
            Certificate certificate = null;
            if (result instanceof SignedServiceMetadata) {
                certificate = this.signatureValidator.verify(document);
                result = ((SignedServiceMetadata) result).getServiceMetadata();
            }

            if (!(result instanceof org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceMetadata)) {
                throw new BindException("ServiceMetadata element not found.");
            }

            org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceMetadata unmarshalledServiceMetadata = (org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceMetadata) result;
            return new ServiceMetadata(certificate, unmarshalledServiceMetadata.getServiceInformation());
        } catch (ParserConfigurationException | IOException | SAXException | JAXBException exc) {
            throw new BindException(exc.getMessage(), exc);
        }
    }

    public List<DocumentIdentifier> parseDocumentIdentifier(FetcherResponse fetcherResponse) throws TechnicalException {
        try {
            List<DocumentIdentifier> documentIdentifiers = new ArrayList<>();
            Document document = this.documentBuilderFactory.newDocumentBuilder().parse(fetcherResponse.getInputStream());
            ServiceGroup serviceGroup = (ServiceGroup) this.unmarshaller.unmarshal(document);
            if (serviceGroup != null && serviceGroup.getServiceMetadataReferenceCollection() != null) {
                List<ServiceMetadataReferenceType> serviceMetadataReference = serviceGroup.getServiceMetadataReferenceCollection().getServiceMetadataReferences();
                if (serviceMetadataReference != null && !serviceMetadataReference.isEmpty()) {
                    Iterator serviceMetadataReferenceTypeIterator = serviceGroup.getServiceMetadataReferenceCollection().getServiceMetadataReferences().iterator();
                    while (serviceMetadataReferenceTypeIterator.hasNext()) {
                        ServiceMetadataReferenceType reference = (ServiceMetadataReferenceType) serviceMetadataReferenceTypeIterator.next();
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
}
