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
package eu.europa.ec.dynamicdiscovery.core.reader.parser.impl;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.reader.parser.IServiceGroupResponseParser;
import eu.europa.ec.dynamicdiscovery.exception.BindException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ServiceGroup;
import eu.europa.ec.dynamicdiscovery.util.IOUtils;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceGroupType;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceMetadataReferenceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Flávio W. R. Santos
 */
public class ServiceGroupResponseParserImpl implements IServiceGroupResponseParser {
    final static Logger LOG = LoggerFactory.getLogger(ServiceGroupResponseParserImpl.class);
    private final String DISALLOW_DOCTYPE_FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";

    private Unmarshaller unmarshaller;
    private DocumentBuilderFactory documentBuilderFactory;

    public ServiceGroupResponseParserImpl() {
        try {
            this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
            this.documentBuilderFactory.setNamespaceAware(true);
            this.documentBuilderFactory.setFeature(DISALLOW_DOCTYPE_FEATURE, true);
            this.unmarshaller = JAXBContext.newInstance(ServiceGroupType.class).createUnmarshaller();
        } catch (Exception exc) {
            throw new IllegalStateException(exc.getMessage(), exc);
        }
    }

    @Override
    public ServiceGroup getServiceGroup(FetcherResponse fetcherResponse) throws TechnicalException {
        LOG.debug("Parse service group response");
        try {
            byte[] byteArray = IOUtils.readResponseData(fetcherResponse);
            String responseBodyStr = new String(byteArray, StandardCharsets.UTF_8);
            ServiceGroupType serviceGroupType = unmarshalServiceGroupType(new ByteArrayInputStream(byteArray));
            LOG.debug("ServiceGroup response parsed for scheme [{}], participant identifier [{}]" , serviceGroupType.getParticipantIdentifier().getScheme() ,
                    serviceGroupType.getParticipantIdentifier().getValue());
            return new ServiceGroup(serviceGroupType, responseBodyStr, getDocumentIdentifiers(serviceGroupType));
        } catch (TechnicalException exc) {
            throw exc;
        } catch (Exception exc) {
            throw new BindException(exc.getMessage(), exc);
        }
    }

    private ServiceGroupType unmarshalServiceGroupType(InputStream inputStream) throws TechnicalException {
        try {
            Document document = this.documentBuilderFactory.newDocumentBuilder().parse(inputStream);
            ServiceGroupType serviceGroupType = (ServiceGroupType) ((JAXBElement) this.unmarshaller.unmarshal(document)).getValue();
            return serviceGroupType;
        } catch (Exception exc) {
            throw new BindException(exc.getMessage(), exc);
        }
    }

    private List<DocumentIdentifier> getDocumentIdentifiers(ServiceGroupType serviceGroupType) throws TechnicalException {
        try {
            List<DocumentIdentifier> documentIdentifiers = new ArrayList<>();
            if (serviceGroupType != null && serviceGroupType.getServiceMetadataReferenceCollection() != null) {
                List<ServiceMetadataReferenceType> serviceMetadataReferences = serviceGroupType.getServiceMetadataReferenceCollection().getServiceMetadataReference();
                if (serviceMetadataReferences != null) {
                    Iterator<ServiceMetadataReferenceType> serviceMetadataReferenceTypeIterator = serviceMetadataReferences.iterator();
                    while (serviceMetadataReferenceTypeIterator.hasNext()) {
                        ServiceMetadataReferenceType reference = serviceMetadataReferenceTypeIterator.next();
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
