package eu.europa.ec.dynamicdiscovery.core.extension;

import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import org.w3c.dom.Document;

import javax.xml.namespace.QName;

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

    T parseAndValidateSignature(Document document, ISignatureValidator signatureValidator) throws TechnicalException;
}
