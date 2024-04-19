/*
 * #%L
 * dynamic-discovery-cli
 * %%
 * Copyright (C) 2016 - 2023 European Commission | eDelivery | Dynamic Discovery Client
 * %%
 * Licensed under the LGPL, Version 2.1 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * [PROJECT_HOME]\license\lgpl2-1\license.txt or https://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package eu.europa.ec.dynamicdiscovery.core.reader.impl;

import eu.europa.ec.dynamicdiscovery.core.extension.IExtension;
import eu.europa.ec.dynamicdiscovery.core.extension.IObjectReader;
import eu.europa.ec.dynamicdiscovery.core.extension.impl.oasis10.OasisSMP10Extension;
import eu.europa.ec.dynamicdiscovery.core.extension.impl.oasis20.OasisSMP20Extension;
import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.reader.IMetadataReader;
import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.core.security.SignatureValidationContext;
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
 * Default implementation of the {@link IMetadataReader} interface. This class is
 * responsible for reading the XML data from the response and returning the corresponding
 * object. The various XML types (OasisSMP 1.0, OasisSMP 2.0, etc.) are handled by the
 * extensions that are registered with the reader. The reader will delegate the parsing
 * of the XML data to the appropriate extension based on the QName and the class of the object.
 * <p>
 * It also validates the signature of the response if it is signed.
 * The list of trusted certificates is provided for the redirection case.
 *
 * @author Flávio W. R. Santos
 * @author Joze Rihtarsic
 * @since 1.0
 */
public class DefaultBDXRReader extends AbstractXMLResponseReader implements IMetadataReader {
    static final Logger LOG = LoggerFactory.getLogger(DefaultBDXRReader.class);
    final ISignatureValidator signatureValidator;
    final List<IExtension> listExtensions = new ArrayList<>();

    private DefaultBDXRReader(Builder builder) {
        signatureValidator = builder.signatureValidator;
        listExtensions.addAll(builder.listExtensions);
    }

    public List<IExtension> getExtensions() {
        return listExtensions;
    }


    @Override
    public <T, C> IObjectReader<T, C> getParser(QName qName, Class<T> clazz) {
        Optional<IExtension> optionalIExtension = listExtensions.stream().filter(parser -> parser.handles(qName, clazz)).findFirst();
        if (!optionalIExtension.isPresent()) {
            LOG.error("No parses registered for [{}] and class [{}]", qName, clazz);
            return null;
        }
        return optionalIExtension.get().getParser(qName, clazz);
    }

    @Override
    public SMPServiceGroup getServiceGroup(FetcherResponse fetcherResponse, SignatureValidationContext context) throws TechnicalException {
        return readObject(fetcherResponse, SMPServiceGroup.class, this.signatureValidator, context);
    }

    @Override
    public SMPServiceMetadata getServiceMetadata(FetcherResponse fetcherResponse, SignatureValidationContext context) throws TechnicalException {
        return readObject(fetcherResponse, SMPServiceMetadata.class, this.signatureValidator, context);
    }

    public static class Builder {

        private ISignatureValidator signatureValidator;
        List<IExtension> listExtensions = new ArrayList<>();

        public Builder addExtension(IExtension extension) {
            this.listExtensions.add(extension);
            return this;
        }

        public Builder addExtensions(List<IExtension> extensions) {
            this.listExtensions.addAll(extensions);
            return this;
        }

        public Builder signatureValidator(ISignatureValidator signatureValidator) {
            this.signatureValidator = signatureValidator;
            return this;
        }

        public DefaultBDXRReader build() {
            validate();
            return new DefaultBDXRReader(this);
        }

        private void validate() {
            if (listExtensions.isEmpty()) {
                LOG.debug("No extensions were added to the reader. Adding default Oasis SMP 1.0/2.0 extensions");
                listExtensions.addAll(Arrays.asList(new OasisSMP10Extension(), new OasisSMP20Extension()));
            }
        }

    }
}
