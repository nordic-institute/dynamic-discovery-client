package eu.europa.ec.dynamicdiscovery.core.extension;

import javax.xml.namespace.QName;

/**
 * Extension interface - to discovery the right parser implementation for XML element (QName) and
 * target clazz.
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */
public interface IExtension {
    boolean handles(QName qName, Class<?> clazz);

    <T> IObjectReader<T> getParser(QName qName, Class<T> clazz);
}
