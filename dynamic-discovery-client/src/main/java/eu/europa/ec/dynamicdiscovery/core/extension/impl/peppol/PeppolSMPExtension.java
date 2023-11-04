package eu.europa.ec.dynamicdiscovery.core.extension.impl.peppol;

import eu.europa.ec.dynamicdiscovery.core.extension.IExtension;
import eu.europa.ec.dynamicdiscovery.core.extension.IObjectReader;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.List;

/**
 * Peppol SMP extension providing the ServiceGroup and SignedServiceMetadata parser
 *
 * @author Cosmin Baciu
 * @since 2.1
 */
public class PeppolSMPExtension implements IExtension {

    public static final String NAMESPACE = "http://busdox.org/serviceMetadata/publishing/1.0/";

    final List<IObjectReader<?>> parsers;
    final PeppolSMPServiceGroupReader serviceGroupReader;
    final PeppolSMPServiceMetadataReader serviceMetadataReader;

    public PeppolSMPExtension() {
        this(false);
    }

    public PeppolSMPExtension(boolean ignoreInvalidServices) {
        serviceGroupReader =  new PeppolSMPServiceGroupReader();
        serviceMetadataReader =  new PeppolSMPServiceMetadataReader(ignoreInvalidServices);
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
