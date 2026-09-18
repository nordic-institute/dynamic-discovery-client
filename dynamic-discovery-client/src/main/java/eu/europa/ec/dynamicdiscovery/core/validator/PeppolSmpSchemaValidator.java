/*-
 * #%L
 * dynamic-discovery-client
 * %%
 * Copyright (C) 2016 - 2025 European Commission | eDelivery | Dynamic Discovery Client
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

public class PeppolSmpSchemaValidator {
    /**
     * Class has only static members.
     */
    private PeppolSmpSchemaValidator() {

    }

    /**
     * thread safe validator
     */
    private static final ThreadLocal<Validator> peppolSMPValidator = ThreadLocal.withInitial(() -> {
        URL xsdFilePath = OasisSmpSchemaValidator.class.getResource("/xsd/peppol/peppol-smp-types-v1.xsd");
        return generateValidatorForSchema(xsdFilePath);
    });

    private static final ThreadLocal<Validator> peppolBCardValidator = ThreadLocal.withInitial(() -> {
        URL xsdFilePath = OasisSmpSchemaValidator.class.getResource("/xsd/peppol/peppol-directory-business-card-20180621.xsd");
        return generateValidatorForSchema(xsdFilePath);
    });

    /**
     * ThreadLocal variables are supposed to be garbage collected once the holding thread is no longer alive.
     * Memory leaks can occur when holding threads are re-used which is the case on application servers using pool of threads.
     * To avoid such problems, it is recommended to always clean up ThreadLocal variables using the destroyValidators() method.
     */
    public void destroyValidators() {
        peppolSMPValidator.remove();
        peppolBCardValidator.remove();
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

    private static Validator getPeppolSMPValidator() {
        return peppolSMPValidator.get();
    }
    private static Validator getPeppolBCardValidator() {
        return peppolBCardValidator.get();
    }

    public static void validatePeppolSMPSchema(byte[] xmlBody) throws XmlInvalidAgainstSchemaException {
        validatePeppolSMPSchema(new ByteArrayInputStream(xmlBody));
    }

    public static void validatePeppolBCardSchema(byte[] xmlBody) throws XmlInvalidAgainstSchemaException {
        validatePeppolBCardSchema(new ByteArrayInputStream(xmlBody));
    }

    public static void validatePeppolSMPSchema(InputStream xmlBody) throws XmlInvalidAgainstSchemaException {
        try {
            getPeppolSMPValidator().validate(new StreamSource(xmlBody));
        } catch (SAXException | IOException e) {
            throw new XmlInvalidAgainstSchemaException(e.getMessage(), e);
        }
    }

    public static void validatePeppolBCardSchema(InputStream xmlBody) throws XmlInvalidAgainstSchemaException {
        try {
            getPeppolSMPValidator().validate(new StreamSource(xmlBody));
        } catch (SAXException | IOException e) {
            throw new XmlInvalidAgainstSchemaException(e.getMessage(), e);
        }
    }
}
