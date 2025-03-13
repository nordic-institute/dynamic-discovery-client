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
import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.reader.IDocumentReader;
import eu.europa.ec.dynamicdiscovery.core.reader.ISMPDocumentReader;
import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.core.security.SignatureValidationContext;
import eu.europa.ec.dynamicdiscovery.exception.DDCInvalidDataException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceGroup;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import java.util.List;
import java.util.Optional;

/**
 * Default implementation of the {@link IDocumentReader} interface. This class is
 * responsible for reading the XML data from the response and returning the corresponding
 * object. The various XML types (OasisSMP 1.0, OasisSMP 2.0, etc.) are handled by the
 * extensions that are registered with the reader. The reader will delegate the parsing
 * of the XML data to the appropriate extension based on the QName and the class of the object.
 * <p>
 * It also validates the signature of the response if it is signed.
 * The list of trusted certificates is provided for the redirection case.
 *
 * @author Flávio W. R. SANTOS
 * @author Joze RIHTARSIC
 * @since 1.0
 */
public class DefaultBDXRReader extends AbstractXMLResponseReader implements ISMPDocumentReader {
    static final Logger LOG = LoggerFactory.getLogger(DefaultBDXRReader.class);
    final ISignatureValidator signatureValidator;


    private DefaultBDXRReader(Builder builder) {
        signatureValidator = builder.signatureValidator;
    }

    @Override
    public <T, C> IObjectReader<T, C> getParser(QName qName, Class<T> clazz, List<IExtension> extensions) {
        if (extensions == null) {
            throw new DDCInvalidDataException("Missing extensions to parse the data!");
        }
        Optional<IExtension> optionalIExtension = extensions.stream()
                .filter(parser -> parser.handles(qName, clazz)).findFirst();
        if (!optionalIExtension.isPresent()) {
            LOG.error("No parses registered for [{}] and class [{}]", qName, clazz);
            return null;
        }
        return optionalIExtension.get().getParser(qName, clazz);
    }

    @Override
    public SMPServiceGroup getResource(FetcherResponse fetcherResponse, List<IExtension> extensions, SignatureValidationContext context)
            throws TechnicalException {

        return readObject(fetcherResponse, SMPServiceGroup.class, extensions, this.signatureValidator, context);
    }

    @Override
    public SMPServiceMetadata getSubresource(FetcherResponse fetcherResponse, List<IExtension> listExtensions, SignatureValidationContext context) throws TechnicalException {
        return readObject(fetcherResponse, SMPServiceMetadata.class, listExtensions, this.signatureValidator, context);
    }

    public static class Builder {

        private ISignatureValidator signatureValidator;


        public Builder signatureValidator(ISignatureValidator signatureValidator) {
            this.signatureValidator = signatureValidator;
            return this;
        }

        public DefaultBDXRReader build() {
            return new DefaultBDXRReader(this);
        }

    }
}
