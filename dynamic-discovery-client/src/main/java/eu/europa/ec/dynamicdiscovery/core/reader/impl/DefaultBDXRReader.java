/*
 * Copyright 2017-2023 European Commission | CEF eDelivery
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence attached in file: LICENCE-EUPL-v1.2.pdf
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 */
package eu.europa.ec.dynamicdiscovery.core.reader.impl;

import eu.europa.ec.dynamicdiscovery.core.extension.IExtension;
import eu.europa.ec.dynamicdiscovery.core.extension.IObjectReader;
import eu.europa.ec.dynamicdiscovery.core.extension.impl.oasis10.OasisSMP10Extension;
import eu.europa.ec.dynamicdiscovery.core.extension.impl.oasis20.OasisSMP20Extension;
import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.reader.IMetadataReader;
import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.enums.DNSLookupType;
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
    final ISignatureValidator signatureValidator;
    final List<IExtension> listExtensions = new ArrayList<>();

    private DefaultBDXRReader(Builder builder) {
        signatureValidator = builder.signatureValidator;
        listExtensions.addAll(builder.listExtensions);
    }

    /**
     * @deprecated use builder
     */
    @Deprecated
    public DefaultBDXRReader(ISignatureValidator signatureValidator) {
        this(Arrays.asList(new OasisSMP10Extension(),
                        new OasisSMP20Extension()),
                signatureValidator);
    }

    /**
     * @deprecated use builder
     */
    @Deprecated
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


    public static class Builder {

        static final List<DNSLookupType> DEFAULT_LOOKUPS = new ArrayList<>(Arrays.asList(DNSLookupType.NAPTR, DNSLookupType.CNAME));
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
                listExtensions.addAll(Arrays.asList(new OasisSMP10Extension(), new OasisSMP20Extension()));
            }
        }

    }


}
