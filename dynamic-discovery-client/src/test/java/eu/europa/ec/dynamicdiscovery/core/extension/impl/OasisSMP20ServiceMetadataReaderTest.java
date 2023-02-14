package eu.europa.ec.dynamicdiscovery.core.extension.impl;

import eu.europa.ec.dynamicdiscovery.model.SMPEndpoint;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import eu.europa.ec.dynamicdiscovery.model.SMPTransportProfile;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPProcessIdentifier;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import gen.eu.europa.ec.ddc.api.smp20.ServiceMetadata;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import static eu.europa.ec.dynamicdiscovery.util.TestCaseConstants.PARTICIPANT_IDENTIFIER_ISO6253_02;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
 * @author Joze Rihtarsic
 * @since 2.0
 */
class OasisSMP20ServiceMetadataReaderTest {

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

}
