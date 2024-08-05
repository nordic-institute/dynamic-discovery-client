/*-
 * #%L
 * dynamic-discovery-client
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
package eu.europa.ec.dynamicdiscovery.core.extension.impl.oasis20;

import eu.europa.ec.dynamicdiscovery.model.SMPEndpoint;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import eu.europa.ec.dynamicdiscovery.model.SMPTransportProfile;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPProcessIdentifier;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import gen.eu.europa.ec.ddc.api.smp20.ServiceMetadata;
import gen.eu.europa.ec.ddc.api.smp20.aggregate.Certificate;
import gen.eu.europa.ec.ddc.api.smp20.aggregate.Endpoint;
import gen.eu.europa.ec.ddc.api.smp20.basic.ActivationDate;
import gen.eu.europa.ec.ddc.api.smp20.basic.ContentBinaryObject;
import gen.eu.europa.ec.ddc.api.smp20.basic.ExpirationDate;
import gen.eu.europa.ec.ddc.api.smp20.basic.TypeCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;

import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static eu.europa.ec.dynamicdiscovery.util.TestCaseConstants.PARTICIPANT_IDENTIFIER_ISO6253_02;
import static java.time.OffsetDateTime.now;
import static org.junit.jupiter.api.Assertions.*;

/*
 * @author Joze Rihtarsic
 * @since 2.0
 */
class OasisSMP20ServiceMetadataReaderTest {


    private static Stream<Arguments> testActivationArguments() {
        return Stream.of(
                Arguments.of(createActivationDate(now().minusDays(1)), createExpirationDate(now().plusDays(1)), true),
                Arguments.of(createActivationDate(now().minusDays(2)), createExpirationDate(now().minusDays(1)), false),
                Arguments.of(createActivationDate(now().plusDays(1)), createExpirationDate(now().plusDays(2)), false),
                Arguments.of(createActivationDate(null), createExpirationDate(now().plusDays(1)), true),
                Arguments.of(createActivationDate(null), createExpirationDate(now().minusDays(1)), false),
                Arguments.of(createActivationDate(now().minusDays(1)), null, true),
                Arguments.of(createActivationDate(now().plusDays(1)), null, false)
        );
    }

    OasisSMP20ServiceMetadataReader testInstance = new OasisSMP20ServiceMetadataReader();

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
        QName qName = new QName("http://docs.oasis-open.org/bdxr/ns/SMP/2/ServiceMetadata", "ServiceMetadata");
        Class targetClass = SMPServiceMetadata.class;
        // when
        boolean result = testInstance.handles(qName, targetClass);
        // then
        assertTrue(result);
    }

    @Test
    void testHandlesFalse() {
        // given
        QName qName = new QName("http://docs.oasis-open.org/bdxr/ns/SMP/2016/Wrong", "ServiceMetadata");
        Class targetClass = SMPServiceMetadata.class;
        // when
        boolean result = testInstance.handles(qName, targetClass);
        // then
        assertFalse(result);
    }

    @Test
    void testParseOK() throws Exception {
        // given
        Document doc = CommonUtil.getOasisSMP20DocumentFromXmlFile("service_metadata_unsigned_valid_iso6523");
        // when
        SMPServiceMetadata result = testInstance.parse(doc);
        // then
        assertNotNull(result);
        assertNotNull(result.getParticipantIdentifier());
        assertNotNull(result.getDocumentIdentifier());
        assertTrue(result.isWrapperFor(ServiceMetadata.class));
        assertNotNull(result.unwrap(ServiceMetadata.class));
        assertEquals(PARTICIPANT_IDENTIFIER_ISO6253_02, result.getParticipantIdentifier());
        assertEquals(1, result.getEndpoints().size());
        SMPEndpoint endpoint = result.getEndpoints().get(0);
        assertEquals("https://ap.example.com/as4", endpoint.getAddress());
        assertEquals(1, result.getEndpoints().get(0).getProcessIdentifiers().size());
        assertEquals(endpoint.getProcessIdentifier(), endpoint.getProcessIdentifiers().get(0));
        assertEquals(new SMPProcessIdentifier("urn:www.cenbii.eu:profile:bii05:ver2.0", "cenbii-procid-ubl"), endpoint.getProcessIdentifiers().get(0));
        assertEquals(new SMPTransportProfile("bdxr-transport-ebms3-as4-v1p0"), endpoint.getTransportProfile());

        assertNotNull(endpoint.getCertificate());
        assertNotNull(endpoint.getCertificates());
        assertEquals(1, endpoint.getCertificates().size());
        assertEquals(endpoint.getCertificate(), endpoint.getCertificates().get(SMPEndpoint.DEFAULT_CERTIFICATE));

        // verify signature
        assertNull(result.getSignerCertificate());
    }


    @Test
    void testIsIgnoreInvalidServices() {
        boolean result = testInstance.isIgnoreInvalidServices();
        testInstance.setIgnoreInvalidServices(!result);
        assertEquals(!result, testInstance.isIgnoreInvalidServices());
    }

    @ParameterizedTest(name = "{index}: Test expire service")
    @MethodSource("testActivationArguments")
    void testIsServiceValid(ActivationDate activateDate, ExpirationDate expireDate, boolean isValid) {
        Endpoint endpoint = new Endpoint();
        endpoint.setActivationDate(activateDate);
        endpoint.setExpirationDate(expireDate);

        assertEquals(isValid, testInstance.isServiceValid(endpoint));
    }


    @Test
    void testGetX509CertificatesFromEndpointSingleNoCode() {
        List<Certificate> list = Collections.singletonList(createCertificate(null, "eDelivery_SMP_TEST_1"));
        Endpoint endpoint = new Endpoint();
        endpoint.getCertificates().addAll(list);

        Map<String, X509Certificate> result = testInstance.getX509CertificatesFromEndpoint(endpoint);

        assertEquals(1, result.size());

        assertTrue(result.containsKey(SMPEndpoint.DEFAULT_CERTIFICATE));
        assertNotNull(result.get(SMPEndpoint.DEFAULT_CERTIFICATE));
    }

    @Test
    void testGetX509CertificatesFromEndpointMultiple() {
        String code1 = "sign";
        String code2 = "encrypt";

        List<Certificate> list = Arrays.asList(createCertificate(code1, "eDelivery_SMP_TEST_1"),
                createCertificate(code2, "eDelivery_SMP_TEST_1"));
        Endpoint endpoint = new Endpoint();
        endpoint.getCertificates().addAll(list);

        Map<String, X509Certificate> result = testInstance.getX509CertificatesFromEndpoint(endpoint);

        assertEquals(2, result.size());

        assertTrue(result.containsKey(code1));
        assertTrue(result.containsKey(code2));
        assertNotNull(result.get(code1));
        assertNotNull(result.get(code2));
    }

    @Test
    void testGetX509CertificatesFromEndpointNull() {

        List<Certificate> list = Collections.singletonList(createCertificate(null, null));
        Endpoint endpoint = new Endpoint();
        endpoint.getCertificates().addAll(list);

        Map<String, X509Certificate> result = testInstance.getX509CertificatesFromEndpoint(endpoint);

        assertEquals(0, result.size());

    }

    @Test
    void testGetX509CertificatesFromEndpointDuplicate() {
        String code1 = "sign";

        List<Certificate> list = Arrays.asList(createCertificate(code1, "eDelivery_SMP_TEST_1"),
                createCertificate(code1, "eDelivery_SMP_TEST_1"));
        Endpoint endpoint = new Endpoint();
        endpoint.getCertificates().addAll(list);

        Map<String, X509Certificate> result = testInstance.getX509CertificatesFromEndpoint(endpoint);

        assertEquals(1, result.size());

        assertTrue(result.containsKey(code1));
        assertNotNull(result.get(code1));
    }

    public static Certificate createCertificate(String code, String certName) {

        Certificate cert = new Certificate();
        if (code != null) {
            TypeCode tc = new TypeCode();
            tc.setValue(code);
            cert.setTypeCode(tc);
        }
        ContentBinaryObject binaryObject = new ContentBinaryObject();
        if (certName != null) {
            try {
                binaryObject.setValue(CommonUtil.readAllBytesForResource("/certificate/" + certName + ".cer"));
            } catch (IOException e) {
                // test as invalid
                binaryObject.setValue("invalid cert data".getBytes());
            }
        }
        cert.setContentBinaryObject(binaryObject);


        return cert;
    }

    public static ActivationDate createActivationDate(OffsetDateTime activateDate) {
        ActivationDate date = new ActivationDate();
        date.setValue(activateDate);
        return date;
    }

    public static ExpirationDate createExpirationDate(OffsetDateTime activateDate) {
        ExpirationDate date = new ExpirationDate();
        date.setValue(activateDate);
        return date;
    }


}
