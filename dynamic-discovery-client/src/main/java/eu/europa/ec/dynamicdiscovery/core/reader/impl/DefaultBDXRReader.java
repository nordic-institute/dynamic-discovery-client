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
package eu.europa.ec.dynamicdiscovery.core.reader.impl;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.reader.IMetadataReader;
import eu.europa.ec.dynamicdiscovery.core.reader.parser.ServiceGroupResponseParser;
import eu.europa.ec.dynamicdiscovery.core.reader.parser.SignedServiceMetadataResponseParser;
import eu.europa.ec.dynamicdiscovery.core.security.AbstractSignatureValidator;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ServiceMetadata;
import eu.europa.ec.dynamicdiscovery.wrapper.DocumentIdVO;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceGroupType;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.SignedServiceMetadataType;

import javax.xml.bind.JAXBException;
import java.util.List;

public class DefaultBDXRReader implements IMetadataReader {

    private ServiceGroupResponseParser serviceGroupResponseParser;
    private SignedServiceMetadataResponseParser signedServiceMetadataResponseParser;

    public DefaultBDXRReader(AbstractSignatureValidator signatureValidator) throws JAXBException {
        serviceGroupResponseParser = new ServiceGroupResponseParser(signatureValidator);
        signedServiceMetadataResponseParser = new SignedServiceMetadataResponseParser(signatureValidator);
    }

    public ServiceGroupType getServiceGroup(FetcherResponse fetcherResponse) throws TechnicalException {
        return serviceGroupResponseParser.getServiceGroup(fetcherResponse);
    }

    @Deprecated
    public List<DocumentIdentifier> getDocumentIdentifiers(FetcherResponse fetcherResponse) throws TechnicalException {
        return serviceGroupResponseParser.parseDocumentIdentifier(fetcherResponse);
    }

    public SignedServiceMetadataType getSignedServiceMetadata(FetcherResponse fetcherResponse) throws TechnicalException {
        return signedServiceMetadataResponseParser.getSignedServiceMetadata(fetcherResponse);
    }

    @Deprecated
    public ServiceMetadata getServiceMetadata(FetcherResponse fetcherResponse) throws TechnicalException {
        return signedServiceMetadataResponseParser.parseServiceMetadata(fetcherResponse);
    }
}
