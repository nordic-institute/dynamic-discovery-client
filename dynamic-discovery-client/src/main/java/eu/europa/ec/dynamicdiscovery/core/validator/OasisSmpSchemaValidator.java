/*
 * Copyright 2016-2023 - European Commission | Dynamic Discovery Client
 *
 * https://ec.europa.eu/digital-building-blocks/code/projects/EDELIVERY/repos/dynamic-discovery-client/browse
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
package eu.europa.ec.dynamicdiscovery.core.validator;

import eu.europa.ec.dynamicdiscovery.exception.XmlInvalidAgainstSchemaException;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Interface for classes which provide the common discovered SMP data: ServiceGroup, ServiceMetadata using various standards
 * This interface describes a standard mechanism to access wrapped resources represented by their proxy, to permit direct access to the resource delegates.
 *
 * @author Joze Rihtarsic
 * @Since: 2.0
 */
public class OasisSmpSchemaValidator {

    /**
     * Class has only static members.
     */
    private OasisSmpSchemaValidator() {

    }

    /**
     * thread safe validator
     */
    private static final ThreadLocal<Validator> oasisSMP10Validator = ThreadLocal.withInitial(() -> {
        URL xsdFilePath = OasisSmpSchemaValidator.class.getResource("/xsd/bdx-smp-1.0/bdx-smp-201605.xsd");
        return generateValidatorForSchema(xsdFilePath);
    });

    private static final ThreadLocal<Validator> oasisSMP20ServiceGroupValidator = ThreadLocal.withInitial(() -> {
        URL xsdFilePath = OasisSmpSchemaValidator.class.getResource("/xsd/bdx-smp-2.0/ServiceGroup-2.0.xsd");
        return generateValidatorForSchema(xsdFilePath);
    });

    private static final ThreadLocal<Validator> oasisSMP20ServiceMetadataValidator = ThreadLocal.withInitial(() -> {
        URL xsdFilePath = OasisSmpSchemaValidator.class.getResource("/xsd/bdx-smp-2.0/ServiceMetadata-2.0.xsd");
        return generateValidatorForSchema(xsdFilePath);
    });

    /**
     * ThreadLocal variables are supposed to be garbage collected once the holding thread is no longer alive.
     * Memory leaks can occur when holding threads are re-used which is the case on application servers using pool of threads.
     * To avoid such problems, it is recommended to always clean up ThreadLocal variables using the destroyValidators() method.
     */
    public void destroyValidators() {
        oasisSMP10Validator.remove();
        oasisSMP20ServiceGroupValidator.remove();
        oasisSMP20ServiceMetadataValidator.remove();
    }


    private static Validator generateValidatorForSchema(URL xsdFilePath) {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            Schema schema = schemaFactory.newSchema(xsdFilePath);
            Validator vaInstance = schema.newValidator();
            vaInstance.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            vaInstance.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            return vaInstance;
        } catch (SAXException e) {
            throw new IllegalStateException("Unable to initialize BDX SMP OASIS XSD schema validator.", e);
        }
    }
    private static Validator getOasisSMP10Validator() {
        return oasisSMP10Validator.get();
    }

    private static Validator getOasisSMP20ServiceGroupValidator() {
        return oasisSMP20ServiceGroupValidator.get();
    }

    private static Validator getOasisSMP20ServiceMetadataValidator() {
        return oasisSMP20ServiceMetadataValidator.get();
    }

    public static void validateOasisSMP10Schema(byte[] xmlBody) throws XmlInvalidAgainstSchemaException {
        validateOasisSMP10Schema(new ByteArrayInputStream(xmlBody));
    }

    public static void validateOasisSMP10Schema(InputStream xmlBody) throws XmlInvalidAgainstSchemaException {
        try {
            getOasisSMP10Validator().validate(new StreamSource(xmlBody));
        } catch (SAXException | IOException e) {
            throw new XmlInvalidAgainstSchemaException(e.getMessage(), e);
        }
    }

    public static void validateOasisSMP20ServiceGroupSchema(byte[] xmlBody) throws XmlInvalidAgainstSchemaException {
        validateOasisSMP20ServiceGroupSchema(new ByteArrayInputStream(xmlBody));
    }

    public static void validateOasisSMP20ServiceGroupSchema(InputStream xmlBody) throws XmlInvalidAgainstSchemaException {
        try {
            getOasisSMP20ServiceGroupValidator().validate(new StreamSource(xmlBody));
        } catch (SAXException | IOException e) {
            throw new XmlInvalidAgainstSchemaException(e.getMessage(), e);
        }
    }

    public static void validateOasisSMP20ServiceMetadataSchema(byte[] xmlBody) throws XmlInvalidAgainstSchemaException {
        validateOasisSMP20ServiceMetadataSchema(new ByteArrayInputStream(xmlBody));
    }

    public static void validateOasisSMP20ServiceMetadataSchema(InputStream xmlBody) throws XmlInvalidAgainstSchemaException {
        try {
            getOasisSMP20ServiceMetadataValidator().validate(new StreamSource(xmlBody));
        } catch (SAXException | IOException e) {
            throw new XmlInvalidAgainstSchemaException(e.getMessage(), e);
        }
    }
}
