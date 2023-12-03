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

import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceGroup;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import gen.eu.europa.ec.ddc.api.smp20.ServiceGroup;
import gen.eu.europa.ec.ddc.api.smp20.aggregate.ServiceReference;
import gen.eu.europa.ec.ddc.api.smp20.basic.ID;
import gen.eu.europa.ec.ddc.api.smp20.basic.ParticipantID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.w3c.dom.Document;

import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import java.util.List;

import static eu.europa.ec.dynamicdiscovery.util.TestCaseConstants.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Joze Rihtarsic
 * @since 2.0
 */
class OasisSMP20ServiceGroupReaderTest {

    OasisSMP20ServiceGroupReader testInstance = new OasisSMP20ServiceGroupReader();

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
        QName qName = new QName("http://docs.oasis-open.org/bdxr/ns/SMP/2/ServiceGroup", "ServiceGroup");
        Class targetClass = SMPServiceGroup.class;
        // when
        boolean result = testInstance.handles(qName, targetClass);
        // then
        assertTrue(result);
    }

    @Test
    void testHandlesFalse() {
        // given
        QName qName = new QName("http://docs.oasis-open.org/bdxr/ns/SMP/2016/Wrong", "ServiceGroup");
        Class targetClass = SMPServiceGroup.class;
        // when
        boolean result = testInstance.handles(qName, targetClass);
        // then
        assertFalse(result);
    }

    @Test
    void testParseOK() throws Exception {
        // given
        Document doc = CommonUtil.getOasisSMP20DocumentFromXmlFile("service_group_unsigned_valid_iso6523");
        // when
        SMPServiceGroup result = testInstance.parse(doc);
        // then
        assertNotNull(result);
        assertNotNull(result.getParticipantIdentifier());
        assertTrue(result.isWrapperFor(ServiceGroup.class));
        assertNotNull(result.unwrap(ServiceGroup.class));
        assertEquals(PARTICIPANT_IDENTIFIER_ISO6253_01, result.getParticipantIdentifier());
        assertEquals(2, result.getDocumentIdentifiers().size());
        assertEquals(DOCUMENT_IDENTIFIER_001, result.getDocumentIdentifiers().get(0));
        assertEquals(DOCUMENT_IDENTIFIER_002, result.getDocumentIdentifiers().get(1));
    }

    @Test
    void testParseAndValidateSignature() throws Exception {
        Document doc = CommonUtil.getOasisSMP20DocumentFromXmlFile("service_group_unsigned_valid_iso6523");
        // when (the ISignatureValidator is ignored because service group is not signed)
        SMPServiceGroup result = testInstance.parseAndValidateSignature(doc, Mockito.mock(ISignatureValidator.class));
        // then
        assertNotNull(result);
        assertNotNull(result.getParticipantIdentifier());
        assertTrue(result.isWrapperFor(ServiceGroup.class));
        assertNotNull(result.unwrap(ServiceGroup.class));
        assertEquals(PARTICIPANT_IDENTIFIER_ISO6253_01, result.getParticipantIdentifier());
        assertEquals(2, result.getDocumentIdentifiers().size());
        assertEquals(DOCUMENT_IDENTIFIER_001, result.getDocumentIdentifiers().get(0));
        assertEquals(DOCUMENT_IDENTIFIER_002, result.getDocumentIdentifiers().get(1));
    }

    @Test
    void testGetParticipantIdentifier() {

        // given
        ServiceGroup serviceGroup = new ServiceGroup();
        ParticipantID identifier = new ParticipantID();
        identifier.setSchemeID("test-scheme-001");
        identifier.setValue("test-value-001");
        serviceGroup.setParticipantID(identifier);
        // when
        SMPParticipantIdentifier result = testInstance.getParticipantIdentifier(serviceGroup);
        // then
        assertNotNull(result);
        assertEquals(identifier.getValue(), result.getIdentifier());
        assertEquals(identifier.getSchemeID(), result.getScheme());
    }

    @Test
    void testGetDocumentIdentifiers() {
        // given
        ServiceGroup serviceGroup = new ServiceGroup();
        serviceGroup.getServiceReferences().add(getServiceReference("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns"));
        serviceGroup.getServiceReferences().add(getServiceReference("urn::epsos##services:extended:epsos::107::version-002", "ehealth-resid-qns"));

        // when
        List<SMPDocumentIdentifier> result = testInstance.getDocumentIdentifiers(serviceGroup);

        assertEquals(2, result.size());
        assertEquals(new SMPDocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns"), result.get(0));
        assertEquals(new SMPDocumentIdentifier("urn::epsos##services:extended:epsos::107::version-002", "ehealth-resid-qns"), result.get(1));


    }

    @Test
    void testGetDocumentIdentifiersNull() {
        // given
        ServiceGroup serviceGroup = new ServiceGroup();
        // when
        List<SMPDocumentIdentifier> result = testInstance.getDocumentIdentifiers(serviceGroup);
        assertTrue(result.isEmpty());
    }

    protected ServiceReference getServiceReference(String identifier, String schema) {
        ServiceReference serviceReference = new ServiceReference();
        serviceReference.setID(getId(identifier, schema));
        return serviceReference;
    }

    protected ID getId(String identifier, String schema) {
        ID id = new ID();
        id.setValue(identifier);
        id.setSchemeID(schema);
        return id;
    }

}
