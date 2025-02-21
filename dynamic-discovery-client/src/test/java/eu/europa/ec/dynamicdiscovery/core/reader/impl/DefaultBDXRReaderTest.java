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

import eu.europa.ec.dynamicdiscovery.core.extension.impl.oasis10.OasisSMP10Extension;
import eu.europa.ec.dynamicdiscovery.core.extension.impl.oasis20.OasisSMP20Extension;
import eu.europa.ec.dynamicdiscovery.core.extension.impl.peppol.PeppolSMPExtension;
import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.core.security.impl.DefaultSignatureValidator;
import eu.europa.ec.dynamicdiscovery.exception.BindException;
import eu.europa.ec.dynamicdiscovery.model.SMPEndpoint;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceGroup;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import gen.eu.europa.ec.ddc.api.smp10.ServiceGroup;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import java.io.InputStream;
import java.security.KeyStore;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Flávio W. R. Santos
 * @since 1.0
 */
class DefaultBDXRReaderTest {
    private static final String TRUSTSTORE_PATH = "truststore/truststoreForTrustedCertificate.ts";

    ISignatureValidator signatureValidatorMock = Mockito.mock(ISignatureValidator.class);

    @ParameterizedTest
    @CsvSource({"oasis-smp-1.0, service_group_unsigned_invalid_iso6523_DTD-01, 'DOCTYPE is disallowed'",
            "oasis-smp-1.0, service_group_unsigned_invalid_iso6523_DTD-02, 'DOCTYPE is disallowed'",
            "oasis-smp-1.0, service_group_unsigned_invalid_iso6523_DTD-03, 'DOCTYPE is disallowed'",
            "oasis-smp-2.0, service_group_unsigned_invalid_iso6523_DTD-01, 'DOCTYPE is disallowed'",
            "oasis-smp-2.0, service_group_unsigned_invalid_iso6523_DTD-02, 'DOCTYPE is disallowed'",
            "oasis-smp-2.0, service_group_unsigned_invalid_iso6523_DTD-03, 'DOCTYPE is disallowed'",
            "oasis-smp-1.0, service_metadata_unsigned_invalid_iso6523_DTD-01, 'DOCTYPE is disallowed'",
            "oasis-smp-1.0, service_metadata_unsigned_invalid_iso6523_DTD-02, 'DOCTYPE is disallowed'",
            "oasis-smp-1.0, service_metadata_unsigned_invalid_iso6523_DTD-03, 'DOCTYPE is disallowed'",
            "oasis-smp-1.0, service_metadata_unsigned_invalid_iso6523_DTD-01, 'DOCTYPE is disallowed'",
            "oasis-smp-2.0, service_metadata_unsigned_invalid_iso6523_DTD-02, 'DOCTYPE is disallowed'",
            "oasis-smp-2.0, service_metadata_unsigned_invalid_iso6523_DTD-03, 'DOCTYPE is disallowed'",
            "peppol, service_group_invalid_iso6523_DTD-01, 'DOCTYPE is disallowed'", // should report error even if extension is not registered
            "peppol, service_metadata_invalid_iso6523_DTD-01, 'DOCTYPE is disallowed'"})
        // should report error even if extension is not registered

    void testDefaultBDXRReaderFailForDocType(String standard, String filename, String error) throws Exception {
        //given
        KeyStore keyStore = CommonUtil.loadTrustStore(TRUSTSTORE_PATH);
        DefaultBDXRReader testInstance = new DefaultBDXRReader.Builder()
                .signatureValidator(new DefaultSignatureValidator(keyStore))
                .build();
        InputStream xmlStream = CommonUtil.getISForName(filename, standard);
        FetcherResponse fetcherResponse = new FetcherResponse(xmlStream);

        BindException result = assertThrows(BindException.class, () -> testInstance.getSubresource(fetcherResponse));
        MatcherAssert.assertThat(result.getMessage(), org.hamcrest.Matchers.containsString(error));
    }


    @Test
    void testGetServiceGroup() throws Exception {
        KeyStore keyStore = CommonUtil.loadTrustStore(TRUSTSTORE_PATH);
        DefaultBDXRReader defaultBDXRReader = new DefaultBDXRReader.Builder()
                .signatureValidator(new DefaultSignatureValidator(keyStore))
                .build();
        FetcherResponse fetcherResponse = new FetcherResponse(CommonUtil.getInputStreamFromOasisSMP10XmlResource("service_group_urn_poland_ncpb"));
        SMPServiceGroup serviceGroup = defaultBDXRReader.getResource(fetcherResponse);

        assertNotNull(serviceGroup);
        assertNotNull(serviceGroup.getParticipantIdentifier());
        assertEquals("urn:poland:ncpb", serviceGroup.getParticipantIdentifier().getIdentifier());
        assertEquals("ehealth-actorid-qns", serviceGroup.getParticipantIdentifier().getScheme());
        assertEquals(2, serviceGroup.getDocumentIdentifiers().size());
        assertTrue(serviceGroup.isWrapperFor(ServiceGroup.class));
        assertNotNull(serviceGroup.unwrap(ServiceGroup.class));
    }

    @Test
    void testGetServiceGroupNullContent() throws Exception {
        KeyStore keyStore = CommonUtil.loadTrustStore(TRUSTSTORE_PATH);
        DefaultBDXRReader defaultBDXRReader = new DefaultBDXRReader.Builder()
                .signatureValidator(new DefaultSignatureValidator(keyStore))
                .build();
        FetcherResponse fetcherResponse = new FetcherResponse(null);

        BindException result = assertThrows(BindException.class, () -> defaultBDXRReader.getResource(fetcherResponse));
        assertEquals("Error occurred while retrieving the data!", result.getMessage());
    }

    @Test
    void testGetServiceMetadata() throws Exception {
        KeyStore keyStore = CommonUtil.loadTrustStore(TRUSTSTORE_PATH);
        DefaultBDXRReader defaultBDXRReader = new DefaultBDXRReader.Builder()
                .signatureValidator(new DefaultSignatureValidator(keyStore))
                .build();
        FetcherResponse fetcherResponse = new FetcherResponse(CommonUtil.getInputStreamFromOasisSMP10XmlResource("signed_service_metadata_invalid_certificate"));
        SMPServiceMetadata serviceMetadata = defaultBDXRReader.getSubresource(fetcherResponse);

        assertNotNull(serviceMetadata);
    }

    @ParameterizedTest
    @CsvSource({"oasis-smp-1.0, service_group_valid_iso6523, 2",
            "oasis-smp-1.0, service_group_valid_iso6523_namespace, 2",
            "oasis-smp-2.0, service_group_unsigned_valid_iso6523, 2",
            "oasis-smp-2.0, service_group_unsigned_valid_iso6523_namespace, 2",
    })
    void testGetServiceGroupOk(String standard, String filename, int serviceMetadataCount) throws Exception {

        DefaultBDXRReader defaultBDXRReader = new DefaultBDXRReader.Builder()
                .signatureValidator(signatureValidatorMock)
                .addExtension(new OasisSMP10Extension())
                .addExtension(new OasisSMP20Extension())
                .addExtension(new PeppolSMPExtension())
                .build();

        InputStream serviceMetadataStream = CommonUtil.getISForName(filename, standard);
        FetcherResponse fetcherResponse = new FetcherResponse(serviceMetadataStream);
        SMPServiceGroup serviceGroup = defaultBDXRReader.getResource(fetcherResponse);

        assertNotNull(serviceGroup);
        assertEquals(serviceMetadataCount, serviceGroup.getDocumentIdentifiers().size());
    }

    @ParameterizedTest
    @CsvSource({"oasis-smp-1.0, signed_service_metadata_signed_valid_iso6523, false, false",
            "oasis-smp-1.0, signed_service_metadata_not-signed_valid_iso6523, false, false",
            "oasis-smp-1.0, signed_service_metadata_not-signed_valid_iso6523_namespace, false, false",
            "oasis-smp-1.0, signed_service_metadata_redirection, true, true",
            "oasis-smp-1.0, signed_service_metadata_redirection_namespace, true, true",
            "oasis-smp-2.0, service_metadata_unsigned_valid_iso6523, false, false",
            "oasis-smp-2.0, service_metadata_unsigned_valid_iso6523_namespace, false, false",
            "oasis-smp-2.0, service_metadata_unsigned_redirection, true, false",
            "peppol, signed_service_metadata_valid_iso6523_wildcard, false, false",
            "peppol, signed_service_metadata_redirection, true, true"})
    void testGetServiceMetadataOk(String standard, String filename, boolean isRedirect, boolean certUID) throws Exception {

        DefaultBDXRReader defaultBDXRReader = new DefaultBDXRReader.Builder()
                .signatureValidator(signatureValidatorMock)
                .addExtension(new OasisSMP10Extension())
                .addExtension(new OasisSMP20Extension())
                .addExtension(new PeppolSMPExtension())
                .build();

        InputStream serviceMetadataStream = CommonUtil.getISForName(filename, standard);
        FetcherResponse fetcherResponse = new FetcherResponse(serviceMetadataStream);
        SMPServiceMetadata serviceMetadata = defaultBDXRReader.getSubresource(fetcherResponse);

        assertNotNull(serviceMetadata);
        assertEquals(1, serviceMetadata.getEndpoints().size());
        SMPEndpoint endpoint = serviceMetadata.getEndpoints().get(0);
        if (isRedirect) {
            assertNotNull(endpoint.getRedirect());
            assertNotNull(endpoint.getRedirect().getRedirectUrl());
            if (certUID) {
                assertNotNull(endpoint.getRedirect().getCertificateUID());
            } else {
                assertFalse(endpoint.getRedirect().getRedirectCertificate().isEmpty());
            }

        } else {
            assertNotNull(endpoint.getProcessIdentifiers());
            assertNotNull(endpoint.getTransportProfile());
            assertNotNull(endpoint.getAddress());
            assertNotNull(endpoint.getCertificate());
        }
    }

    @Test
    void testGetServiceMetadataNotOk() throws Exception {
        KeyStore keyStore = CommonUtil.loadTrustStore(TRUSTSTORE_PATH);
        DefaultBDXRReader defaultBDXRReader = new DefaultBDXRReader.Builder()
                .signatureValidator(new DefaultSignatureValidator(keyStore))
                .build();
        FetcherResponse fetcherResponse = new FetcherResponse(null);

        BindException result = assertThrows(BindException.class, () -> defaultBDXRReader.getSubresource(fetcherResponse));
        assertEquals("Error occurred while retrieving the data!", result.getMessage());
    }

}
