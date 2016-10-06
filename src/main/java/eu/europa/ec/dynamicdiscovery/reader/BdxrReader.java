package eu.europa.ec.dynamicdiscovery.reader;

import eu.europa.ec.dynamicdiscovery.ServiceMetadata;
import eu.europa.ec.dynamicdiscovery.exception.BindException;
import eu.europa.ec.dynamicdiscovery.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.model.*;
import eu.europa.ec.dynamicdiscovery.security.XmldsigVerifier;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import org.oasis_open.docs.bdxr.ns.smp._2014._07.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.dom.DOMSource;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 * @author Erlend Klakegg Bergheim - erlend.klakegg.bergheim@difi.no
 */
public class BdxrReader extends AbstractReader {
    private static Logger logger = LoggerFactory.getLogger(BdxrReader.class);

    public BdxrReader() {
        super();
    }

    public List<DocumentIdentifier> parseDocumentIdentifiers(FetcherResponse fetcherResponse) throws BindException {
        try {
            List<DocumentIdentifier> documentIdentifiers = new ArrayList<>();
            Unmarshaller e = jaxbContext.createUnmarshaller();
            ServiceGroupType serviceGroup = (ServiceGroupType) ((JAXBElement) e.unmarshal(fetcherResponse.getInputStream())).getValue();
            ServiceMetadataReferenceCollectionType serviceMetadataReferenceCollection = serviceGroup.getServiceMetadataReferenceCollection();
            if (serviceGroup != null && serviceMetadataReferenceCollection != null) {
                List<ServiceMetadataReferenceType> serviceMetadataReference = serviceGroup.getServiceMetadataReferenceCollection().getServiceMetadataReference();
                if (serviceMetadataReference != null && !serviceMetadataReference.isEmpty()) {
                    Iterator iterator = serviceGroup.getServiceMetadataReferenceCollection().getServiceMetadataReference().iterator();
                    while (iterator.hasNext()) {
                        ServiceMetadataReferenceType reference = (ServiceMetadataReferenceType) iterator.next();
                        String[] parts = URLDecoder.decode(reference.getHref().split("/services/")[1], "UTF-8").split("::", 2);
                        documentIdentifiers.add(new DocumentIdentifier(parts[1], parts[0]));
                    }
                }
            }
            return documentIdentifiers;
        } catch (UnsupportedEncodingException | JAXBException exc) {
            logger.error("Document Identifier Parser Exception", exc);
            throw new BindException(exc.getMessage(), exc);
        }
    }

    public ServiceMetadata parseServiceMetadata(FetcherResponse fetcherResponse) throws BindException, SecurityException {
        try {
            Document e = CommonUtil.parse(fetcherResponse.getInputStream());
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            JAXBElement result = (JAXBElement) unmarshaller.unmarshal(new DOMSource(e));
            Object o = result.getValue();
            ServiceMetadata serviceMetadata = new ServiceMetadata();
            if (o instanceof SignedServiceMetadataType) {
                serviceMetadata.setSigner(XmldsigVerifier.verify(e));
                o = ((SignedServiceMetadataType) o).getServiceMetadata();
            }

            if (!(o instanceof ServiceMetadataType)) {
                throw new Exception("ServiceMetadata element not found.");
            } else {
                ServiceInformationType serviceInformation = ((ServiceMetadataType) o).getServiceInformation();
                serviceMetadata.setParticipantIdentifier(new ParticipantIdentifier(serviceInformation.getParticipantIdentifier().getValue(), serviceInformation.getParticipantIdentifier().getScheme()));
                serviceMetadata.setDocumentIdentifier(new DocumentIdentifier(serviceInformation.getDocumentIdentifier().getValue(), serviceInformation.getDocumentIdentifier().getScheme()));
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                Iterator var9 = serviceInformation.getProcessList().getProcess().iterator();

                while (var9.hasNext()) {
                    ProcessType processType = (ProcessType) var9.next();
                    ProcessIdentifier processIdentifier = new ProcessIdentifier(processType.getProcessIdentifier().getValue(), processType.getProcessIdentifier().getScheme());
                    Iterator var12 = processType.getServiceEndpointList().getEndpoint().iterator();

                    while (var12.hasNext()) {
                        EndpointType endpointType = (EndpointType) var12.next();
                        serviceMetadata.addEndpoint(new Endpoint(processIdentifier, new TransportProfile(endpointType.getTransportProfile()), endpointType.getEndpointURI(), (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(endpointType.getCertificate()))));
                    }
                }

                return serviceMetadata;
            }
        } catch (JAXBException var14) {
            throw new RuntimeException(var14.getMessage(), var14);
        } catch (CertificateException var15) {
            throw new RuntimeException(var15.getMessage(), var15);
        } catch (Exception var16) {
            throw new RuntimeException(var16.getMessage(), var16);
        }
    }

}
