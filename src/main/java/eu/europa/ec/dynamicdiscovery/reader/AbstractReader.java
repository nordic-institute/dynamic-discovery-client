package eu.europa.ec.dynamicdiscovery.reader;

import org.oasis_open.docs.bdxr.ns.smp._2014._07.ServiceGroup;
import org.oasis_open.docs.bdxr.ns.smp._2014._07.ServiceMetadata;
import org.oasis_open.docs.bdxr.ns.smp._2014._07.SignedServiceMetadata;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public abstract class AbstractReader implements IMetadataReader {

    protected JAXBContext jaxbContext;

    public AbstractReader() {
        try {
            jaxbContext = JAXBContext.newInstance(new Class[]{ServiceGroup.class, SignedServiceMetadata.class, ServiceMetadata.class});
        } catch (JAXBException exc) {
            throw new RuntimeException(exc.getMessage(), exc);
        }
    }
}
