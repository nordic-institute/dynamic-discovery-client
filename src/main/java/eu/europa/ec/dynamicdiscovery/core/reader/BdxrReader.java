package eu.europa.ec.dynamicdiscovery.core.reader;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.security.XmldsigVerifier;
import eu.europa.ec.dynamicdiscovery.exception.BindException;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.*;
import eu.europa.ec.dynamicdiscovery.model.ServiceMetadata;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import org.oasis_open.docs.bdxr.ns.smp._2014._07.*;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.net.URLDecoder;
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

    public BdxrReader() {
        super();
    }

    public List<DocumentIdentifier> parseDocumentIdentifiers(FetcherResponse fetcherResponse) throws BindException {
        try {
            List<DocumentIdentifier> documentIdentifiers = new ArrayList<>();
            Object object = jaxbContext.createUnmarshaller().unmarshal(CommonUtil.convertToSource(fetcherResponse.getInputStream()), ServiceGroup.class);
            ServiceGroup serviceGroup = (ServiceGroup) ((JAXBElement) object).getValue();
            ServiceMetadataReferenceCollectionType serviceMetadataReferenceCollection = serviceGroup.getServiceMetadataReferenceCollection();
            if (serviceGroup != null && serviceMetadataReferenceCollection != null) {
                List<ServiceMetadataReferenceType> serviceMetadataReference = serviceGroup.getServiceMetadataReferenceCollection().getServiceMetadataReferences();
                if (serviceMetadataReference != null && !serviceMetadataReference.isEmpty()) {
                    Iterator iterator = serviceGroup.getServiceMetadataReferenceCollection().getServiceMetadataReferences().iterator();
                    while (iterator.hasNext()) {
                        ServiceMetadataReferenceType reference = (ServiceMetadataReferenceType) iterator.next();
                        String[] parts = URLDecoder.decode(reference.getHref().split("/services/")[1], "UTF-8").split("::", 2);
                        documentIdentifiers.add(new DocumentIdentifier(parts[1], parts[0]));
                    }
                }
            }
            return documentIdentifiers;
        } catch (Exception exc) {
            throw new BindException(exc.getMessage(), exc);
        }
    }

    public ServiceMetadata parseServiceMetadata(FetcherResponse fetcherResponse) throws BindException {
        try {
            Document document = CommonUtil.parse(fetcherResponse.getInputStream());
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Object result = null;
            org.oasis_open.docs.bdxr.ns.smp._2014._07.ServiceMetadata unmarshalledServiceMetadata = null;
            try {
                //result = (JAXBElement) unmarshaller.unmarshal(CommonUtil.convertToSource(document), org.oasis_open.docs.bdxr.ns.smp._2014._07.ServiceMetadata.class);
                result = unmarshaller.unmarshal(CommonUtil.convertToSource(document));
            } catch (Exception exc) {
                exc.printStackTrace();
                // result = (JAXBElement) unmarshaller.unmarshal(CommonUtil.convertToSource(document), org.oasis_open.docs.bdxr.ns.smp._2014._07.SignedServiceMetadata.class);
            }

            //  Object o = result.getValue();
            ServiceMetadata serviceMetadata = new ServiceMetadata();
            if (result instanceof SignedServiceMetadata) {
                serviceMetadata.setSigner(XmldsigVerifier.verify(document));
                unmarshalledServiceMetadata = ((SignedServiceMetadata) result).getServiceMetadata();
            }

            if (!(result instanceof org.oasis_open.docs.bdxr.ns.smp._2014._07.ServiceMetadata)) {
                throw new Exception("ServiceMetadata element not found.");
            } else {
                unmarshalledServiceMetadata = (org.oasis_open.docs.bdxr.ns.smp._2014._07.ServiceMetadata) result;
                ServiceInformationType serviceInformation = unmarshalledServiceMetadata.getServiceInformation();
                serviceMetadata.setParticipantIdentifier(new ParticipantIdentifier(serviceInformation.getParticipantIdentifier().getValue(), serviceInformation.getParticipantIdentifier().getScheme()));
                serviceMetadata.setDocumentIdentifier(new DocumentIdentifier(serviceInformation.getDocumentIdentifier().getValue(), serviceInformation.getDocumentIdentifier().getScheme()));
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                Iterator iterator = serviceInformation.getProcessList().getProcesses().iterator();

                while (iterator.hasNext()) {
                    ProcessType processType = (ProcessType) iterator.next();
                    eu.europa.ec.dynamicdiscovery.model.ProcessIdentifier processIdentifier = new eu.europa.ec.dynamicdiscovery.model.ProcessIdentifier(processType.getProcessIdentifier().getValue(), processType.getProcessIdentifier().getScheme());
                    Iterator var12 = processType.getServiceEndpointList().getEndpoints().iterator();

                    while (var12.hasNext()) {
                        EndpointType endpointType = (EndpointType) var12.next();
                        serviceMetadata.addEndpoint(new Endpoint(processIdentifier, new TransportProfile(endpointType.getTransportProfile()), endpointType.getEndpointURI(), (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(endpointType.getCertificate()))));
                    }
                }

                return serviceMetadata;
            }
        } catch (Exception exc) {
            throw new BindException(exc.getMessage(), exc);
        }
    }
}
