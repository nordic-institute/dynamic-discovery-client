package eu.europa.ec.dynamicdiscovery.core.extension.impl;

import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.core.security.SignatureValidationContext;
import eu.europa.ec.dynamicdiscovery.exception.DDCExceptionCode;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import javax.xml.namespace.QName;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AbstractObjectReaderTest {

    private AbstractObjectReader<Object, Object> objectReader;
    private Marshaller mockMarshaller;
    private Unmarshaller mockUnmarshaller;

    @BeforeEach
    void setUp() {
        mockMarshaller = mock(Marshaller.class);
        mockUnmarshaller = mock(Unmarshaller.class);

        objectReader = new AbstractObjectReader<>(DDCExceptionCode.SERVICE_GROUP) {
            @Override
            public boolean handles(QName qName, Class<?> clazz) {
                return false;
            }

            @Override
            public Object parseAndValidateSignature(Document document,
                                                    ISignatureValidator signatureValidator,
                                                    SignatureValidationContext context) {
                return null;
            }

            @Override
            protected void destroyUnmarshaller() {
                // No-op for testing
            }

            @Override
            protected void destroyMarshaller() {
                // No-op for testing
            }

            @Override
            protected Unmarshaller getUnmarshaller() {
                return mockUnmarshaller;
            }

            @Override
            protected Marshaller getMarshaller() {
                return mockMarshaller;
            }
        };
    }

    @Test
    void testSerializeNativeAny() throws Exception {
        Object testObject = new Object();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        objectReader.serializeNativeAny(testObject, outputStream, true);

        verify(mockMarshaller).setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        verify(mockMarshaller).marshal(eq(testObject), eq(outputStream));
    }

    @Test
    void testSerializeNativeAny_NullObject() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        objectReader.serializeNativeAny(null, outputStream, true);

        verify(mockMarshaller, never()).marshal(any(), (OutputStream) any());
    }

    @Test
    void testParseNativeAny_Document() throws Exception {
        Document mockDocument = mock(Document.class);
        Object expectedObject = new Object();

        when(mockUnmarshaller.unmarshal(mockDocument)).thenReturn(expectedObject);

        Object result = objectReader.parseNativeAny(mockDocument);

        assertEquals(expectedObject, result);
        verify(mockUnmarshaller).unmarshal(mockDocument);
    }
}