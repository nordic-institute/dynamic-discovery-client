package eu.europa.ec.dynamicdiscovery.core.extension.impl;

import eu.europa.ec.dynamicdiscovery.core.extension.IExtension;
import eu.europa.ec.dynamicdiscovery.core.extension.IObjectReader;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.List;

/**
 * Oasis SMP 2.0 extension providing je ServiceGroup and SignedServiceMetadata parser
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */
public class OasisSMP20Extension implements IExtension {


    List<IObjectReader<?>> parsers;

    final OasisSMP20ServiceGroupReader serviceGroupReader;
    final OasisSMP20ServiceMetadataReader serviceMetadataReader;


    public OasisSMP20Extension() {
        this(false);
    }

    public OasisSMP20Extension(boolean ignoreInvalidServices) {
        serviceGroupReader =  new OasisSMP20ServiceGroupReader();
        serviceMetadataReader =  new OasisSMP20ServiceMetadataReader(ignoreInvalidServices);

        parsers = Arrays.asList(
                serviceGroupReader,
                serviceMetadataReader
        );
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
