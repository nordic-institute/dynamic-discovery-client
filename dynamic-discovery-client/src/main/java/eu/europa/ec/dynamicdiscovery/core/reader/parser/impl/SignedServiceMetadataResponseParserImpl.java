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
package eu.europa.ec.dynamicdiscovery.core.reader.parser.impl;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.reader.parser.ISignedServiceMetadataResponseParser;
import eu.europa.ec.dynamicdiscovery.core.security.AbstractSignatureValidator;
import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.exception.BindException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.ServiceMetadata;
import org.apache.commons.io.IOUtils;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.SignedServiceMetadataType;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.cert.Certificate;

public class SignedServiceMetadataResponseParserImpl implements ISignedServiceMetadataResponseParser {

    private final String DISALLOW_DOCTYPE_FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";

    private Unmarshaller unmarshaller;
    private DocumentBuilderFactory documentBuilderFactory;
    private ISignatureValidator signatureValidator;

    public SignedServiceMetadataResponseParserImpl(ISignatureValidator signatureValidator) {
        try {
            this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
            this.documentBuilderFactory.setNamespaceAware(true);
            this.documentBuilderFactory.setFeature(DISALLOW_DOCTYPE_FEATURE, true);
            this.unmarshaller = JAXBContext.newInstance(SignedServiceMetadataType.class).createUnmarshaller();
            this.signatureValidator = signatureValidator;
        } catch (Exception exc) {
            throw new IllegalStateException(exc.getMessage(), exc);
        }
    }

    public ServiceMetadata getServiceMetadata(FetcherResponse fetcherResponse) throws TechnicalException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            IOUtils.copy(fetcherResponse.getInputStream(), baos);
            String responseBodyStr = IOUtils.toString(new ByteArrayInputStream(baos.toByteArray()), StandardCharsets.UTF_8);

            Document document = this.documentBuilderFactory.newDocumentBuilder().parse(new ByteArrayInputStream(baos.toByteArray()));
            SignedServiceMetadataType signedServiceMetadataType = (SignedServiceMetadataType) ((JAXBElement) this.unmarshaller.unmarshal(document)).getValue();
            Certificate certificate = this.signatureValidator.verify(document);

            return new ServiceMetadata(signedServiceMetadataType, certificate, responseBodyStr);
        } catch (TechnicalException exc) {
            throw exc;
        } catch (Exception exc) {
            throw new BindException(exc.getMessage(), exc);
        }
    }
}
