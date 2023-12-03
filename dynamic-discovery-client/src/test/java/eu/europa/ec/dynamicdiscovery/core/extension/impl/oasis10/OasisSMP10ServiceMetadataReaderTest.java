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
package eu.europa.ec.dynamicdiscovery.core.extension.impl.oasis10;

import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.SMPEndpoint;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import eu.europa.ec.dynamicdiscovery.model.SMPTransportProfile;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPProcessIdentifier;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import gen.eu.europa.ec.ddc.api.smp10.EndpointType;
import gen.eu.europa.ec.ddc.api.smp10.SignedServiceMetadata;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.w3c.dom.Document;

import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import java.security.cert.X509Certificate;
import java.time.OffsetDateTime;
import java.util.stream.Stream;

import static eu.europa.ec.dynamicdiscovery.util.TestCaseConstants.PARTICIPANT_IDENTIFIER_ISO6253_02;
import static java.time.OffsetDateTime.now;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Joze Rihtarsic
 * @since 2.0
 */
class OasisSMP10ServiceMetadataReaderTest {

    private static Stream<Arguments> testActivationArguments() {
        return Stream.of(
                Arguments.of(now().minusDays(1), now().plusDays(1), true),
                Arguments.of(now().minusDays(2), now().minusDays(1), false),
                Arguments.of(now().plusDays(1), now().plusDays(2), false),
                Arguments.of(null, now().plusDays(1), true),
                Arguments.of(null, now().minusDays(1), false),
                Arguments.of(now().minusDays(1), null, true),
                Arguments.of(now().plusDays(1), null, false)
        );
    }

    OasisSMP10ServiceMetadataReader testInstance = new OasisSMP10ServiceMetadataReader();

    @Test
    void testDestroyUnmarshaller() {
        Unmarshaller unmarshaller = testInstance.getUnmarshaller();
        assertNotNull(unmarshaller);
        Unmarshaller unmarshaller1 = testInstance.getUnmarshaller();
        assertEquals(unmarshaller, unmarshaller1);
        testInstance.destroyUnmarshaller();
        Unmarshaller unmarshaller3 = testInstance.getUnmarshaller();
        assertNotEquals(unmarshaller, unmarshaller3);
    }

    @Test
    void testHandlesTrue() {
        // given
        QName qName = new QName("http://docs.oasis-open.org/bdxr/ns/SMP/2016/05", "SignedServiceMetadata");
        Class targetClass = SMPServiceMetadata.class;
        // when
        boolean result = testInstance.handles(qName, targetClass);
        // then
        assertTrue(result);
    }

    @Test
    void testHandlesFalse() {
        // given
        QName qName = new QName("http://docs.oasis-open.org/bdxr/ns/SMP/2016/Wrong", "SignedServiceMetadata");
        Class targetClass = SMPServiceMetadata.class;
        // when
        boolean result = testInstance.handles(qName, targetClass);
        // then
        assertFalse(result);
    }

    @Test
    void testParseOK() throws Exception {
        // given
        Document doc = CommonUtil.getOasisSMP10DocumentFromXmlFile("signed_service_metadata_signed_valid_iso6523");
        // when
        SMPServiceMetadata result = testInstance.parse(doc);
        // then
        assertNotNull(result);
        assertNotNull(result.getParticipantIdentifier());
        assertTrue(result.isWrapperFor(SignedServiceMetadata.class));
        assertNotNull(result.unwrap(SignedServiceMetadata.class));
        assertEquals(PARTICIPANT_IDENTIFIER_ISO6253_02, result.getParticipantIdentifier());
        assertEquals(1, result.getEndpoints().size());
        SMPEndpoint endpoint = result.getEndpoints().get(0);
        assertEquals("https://test.erechnung.gv.at/as4/msh/", endpoint.getAddress());
        assertEquals(1, result.getEndpoints().get(0).getProcessIdentifiers().size());
        assertEquals(endpoint.getProcessIdentifier(), endpoint.getProcessIdentifiers().get(0));
        assertEquals(new SMPProcessIdentifier("urn:www.cenbii.eu:profile:bii05:ver2.0", "cenbii-procid-ubl"), endpoint.getProcessIdentifiers().get(0));
        assertEquals(new SMPTransportProfile("bdxr-transport-ebms3-as4-v1p0"), endpoint.getTransportProfile());

        assertNotNull(endpoint.getCertificate());

        assertEquals(1, endpoint.getCertificates().size());
        assertEquals(endpoint.getCertificate(), endpoint.getCertificates().get(SMPEndpoint.DEFAULT_CERTIFICATE));

        // verify signature
        assertNull(result.getSignerCertificate());
    }

    @Test
    void parseAndValidateSignature() throws Exception {
        // given
        Document doc = CommonUtil.getOasisSMP10DocumentFromXmlFile("signed_service_metadata_signed_valid_iso6523");
        ISignatureValidator signatureValidator = Mockito.mock(ISignatureValidator.class);
        X509Certificate cert = Mockito.mock(X509Certificate.class);
        Mockito.doReturn(cert).when(signatureValidator).verify(Mockito.any(Document.class));
        // when
        SMPServiceMetadata result = testInstance.parseAndValidateSignature(doc, signatureValidator);
        // then
        assertNotNull(result);
        assertNotNull(result.getParticipantIdentifier());
        assertTrue(result.isWrapperFor(SignedServiceMetadata.class));
        assertNotNull(result.unwrap(SignedServiceMetadata.class));
        assertEquals(PARTICIPANT_IDENTIFIER_ISO6253_02, result.getParticipantIdentifier());
        assertEquals(1, result.getEndpoints().size());
        SMPEndpoint endpoint = result.getEndpoints().get(0);
        assertEquals("https://test.erechnung.gv.at/as4/msh/", endpoint.getAddress());
        assertEquals(1, result.getEndpoints().get(0).getProcessIdentifiers().size());
        assertEquals(endpoint.getProcessIdentifier(), endpoint.getProcessIdentifiers().get(0));
        assertEquals(new SMPProcessIdentifier("urn:www.cenbii.eu:profile:bii05:ver2.0", "cenbii-procid-ubl"), endpoint.getProcessIdentifiers().get(0));

        assertNotNull(endpoint.getCertificate());
        assertNotNull(endpoint.getCertificates());
        assertEquals(1, endpoint.getCertificates().size());
        assertEquals(endpoint.getCertificate(), endpoint.getCertificates().get(SMPEndpoint.DEFAULT_CERTIFICATE));

        assertEquals(cert, result.getSignerCertificate());
        Mockito.verify(signatureValidator).verify(Mockito.any(Document.class));

    }

    @Test
    void parseAndValidateSignatureInvalid() throws Exception {
        // given
        Document doc = CommonUtil.getOasisSMP10DocumentFromXmlFile("signed_service_metadata_signed_valid_iso6523");
        ISignatureValidator signatureValidator = Mockito.mock(ISignatureValidator.class);
        TechnicalException signatureException = Mockito.mock(TechnicalException.class);
        Mockito.doThrow(signatureException).when(signatureValidator).verify(Mockito.any(Document.class));
        // when
        TechnicalException technicalException = assertThrows(TechnicalException.class, () -> testInstance.parseAndValidateSignature(doc, signatureValidator));

        // then
        assertEquals(technicalException, signatureException);
    }

    @ParameterizedTest(name = "{index}: Test expire service")
    @MethodSource("testActivationArguments")
    void testIsServiceValid(OffsetDateTime activateDate, OffsetDateTime expireDate, boolean isValid) {
        EndpointType endpoint = new EndpointType();
        endpoint.setServiceActivationDate(activateDate);
        endpoint.setServiceExpirationDate(expireDate);

        assertEquals(isValid, testInstance.isServiceValid(endpoint));
    }


}
