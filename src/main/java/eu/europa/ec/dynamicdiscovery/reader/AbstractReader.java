package eu.europa.ec.dynamicdiscovery.reader;

import org.oasis_open.docs.bdxr.ns.smp._2014._07.ServiceGroupType;
import org.oasis_open.docs.bdxr.ns.smp._2014._07.ServiceMetadataType;
import org.oasis_open.docs.bdxr.ns.smp._2014._07.SignedServiceMetadataType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public abstract class AbstractReader implements IMetadataReader {

    public static final String NAMESPACE = "http://docs.oasis-open.org/bdxr/ns/SMP/2014/07";
    protected JAXBContext jaxbContext;

    public AbstractReader() {
        try {
            jaxbContext = JAXBContext.newInstance(new Class[]{ServiceGroupType.class, SignedServiceMetadataType.class, ServiceMetadataType.class});
        } catch (JAXBException exc) {
            throw new RuntimeException(exc.getMessage(), exc);
        }
    }
}
