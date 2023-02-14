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
package eu.europa.ec.dynamicdiscovery.core.reader.impl;

import eu.europa.ec.dynamicdiscovery.core.extension.IExtension;
import eu.europa.ec.dynamicdiscovery.core.extension.IObjectReader;
import eu.europa.ec.dynamicdiscovery.core.extension.impl.OasisSMP10Extension;
import eu.europa.ec.dynamicdiscovery.core.extension.impl.OasisSMP20Extension;
import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.reader.IMetadataReader;
import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceGroup;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Flávio W. R. Santos
 * @since 1.0
 */
public class DefaultBDXRReader extends AbstractXMLResponseReader implements IMetadataReader {
    static final Logger LOG = LoggerFactory.getLogger(DefaultBDXRReader.class);
    private ISignatureValidator signatureValidator;


    List<IExtension> listExtensions = new ArrayList<>();

    public DefaultBDXRReader(ISignatureValidator signatureValidator) {
        this(Arrays.asList(new OasisSMP10Extension(),
                new OasisSMP20Extension()),
                signatureValidator);
    }

    public DefaultBDXRReader(List<IExtension> listExtensions, ISignatureValidator signatureValidator) {
        // register default parser
        this.listExtensions.addAll(listExtensions);
        this.signatureValidator = signatureValidator;
    }

    public DefaultBDXRReader addExtension(IExtension extension) {
        listExtensions.add(extension);
        return this;
    }

    public List<IExtension> getExtensions() {
        return listExtensions;
    }


    @Override
    public <T> IObjectReader<T> getParser(QName qName, Class<T> clazz) {
        Optional<IExtension> optionalIExtension = listExtensions.stream().filter(parser -> parser.handles(qName, clazz)).findFirst();
        if (!optionalIExtension.isPresent()) {
            LOG.error("No parses registered for [{}] and class [{}]", qName, clazz);
            return null;
        }
        return optionalIExtension.get().getParser(qName, clazz);
    }

    @Override
    public SMPServiceGroup getServiceGroup(FetcherResponse fetcherResponse) throws TechnicalException {
        return readObject(fetcherResponse, SMPServiceGroup.class, this.signatureValidator);
    }

    @Override
    public SMPServiceMetadata getServiceMetadata(FetcherResponse fetcherResponse) throws TechnicalException {
        return readObject(fetcherResponse, SMPServiceMetadata.class, this.signatureValidator);
    }


}
