package eu.europa.ec.dynamicdiscovery.core.extension.impl.oasis10;

import eu.europa.ec.dynamicdiscovery.core.extension.IExtension;
import eu.europa.ec.dynamicdiscovery.core.extension.IObjectReader;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.List;

/**
 * Oasis SMP 1.0 extension providing je ServiceGroup and SignedServiceMetadata parser
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */
public class OasisSMP10Extension implements IExtension {

    public static final String NAMESPACE = "http://docs.oasis-open.org/bdxr/ns/SMP/2016/05";

    final List<IObjectReader<?>> parsers;
    final OasisSMP10ServiceGroupReader serviceGroupReader;
    final OasisSMP10ServiceMetadataReader serviceMetadataReader;

    public OasisSMP10Extension() {
        this(false);
    }

    public OasisSMP10Extension(boolean ignoreInvalidServices) {
        serviceGroupReader =  new OasisSMP10ServiceGroupReader();
        serviceMetadataReader =  new OasisSMP10ServiceMetadataReader(ignoreInvalidServices);
        parsers = Arrays.asList(serviceGroupReader,serviceMetadataReader);
    }

    public void setIgnoreInvalidServices(boolean ignoreInvalidServices) {
        this.serviceMetadataReader.setIgnoreInvalidServices(ignoreInvalidServices);
    }

    @Override
    public boolean handles(QName qName, Class<?> clazz) {
        return getParser(qName, clazz) != null;
    }

    @Override
    public <T> IObjectReader<T> getParser(QName qName, Class<T> clazz) {
        return (IObjectReader<T>) parsers.stream()
                .filter(parser -> parser.handles(qName, clazz)).findFirst().orElse(null);
    }
}
