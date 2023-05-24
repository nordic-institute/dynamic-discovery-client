package eu.europa.ec.dynamicdiscovery.core.extension;

import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import gen.eu.europa.ec.ddc.api.smp10.ServiceGroup;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Object implementing this class can read the Document and produces the object <T>
 *
 * @param <T>
 * @author Joze Rihtarsic
 * @since 2.0
 */
public interface IObjectReader<T> {

    boolean handles(QName qName, Class<?> clazz);

    T parse(Document document) throws TechnicalException;

    Object parseNative(Document document) throws TechnicalException;

    Object parseNative(InputStream document) throws TechnicalException;

    public void serializeNative(Object jaxbObject, OutputStream outputStream, boolean prettyPrint) throws TechnicalException;

    T parseAndValidateSignature(Document document, ISignatureValidator signatureValidator) throws TechnicalException;
}
