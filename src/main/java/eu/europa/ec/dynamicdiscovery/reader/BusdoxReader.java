package eu.europa.ec.dynamicdiscovery.reader;

import eu.europa.ec.dynamicdiscovery.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.IMetadataReader;
import eu.europa.ec.dynamicdiscovery.ServiceMetadata;
import eu.europa.ec.dynamicdiscovery.model.*;
import eu.europa.ec.dynamicdiscovery.security.XmldsigVerifier;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import org.apache.commons.codec.binary.Base64;

import org.busdox.servicemetadata.publishing._1.*;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.dom.DOMSource;
import java.io.*;
import java.net.URLDecoder;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by rodrfla on 30/09/2016.
 */

public class BusdoxReader implements IMetadataReader {
    public static final String NAMESPACE = "http://busdox.org/serviceMetadata/publishing/1.0/";
    private static JAXBContext jaxbContext;

    public BusdoxReader() {
    }

    public List<DocumentIdentifier> parseDocumentIdentifiers(FetcherResponse fetcherResponse) throws Exception {
        try {
/*
            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();

            String line;

            br = new BufferedReader(new InputStreamReader(fetcherResponse.getInputStream()));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            System.out.println("*"+sb.toString()+"+");*/
            Unmarshaller e = jaxbContext.createUnmarshaller();
            ServiceGroupType serviceGroup = (ServiceGroupType) ((JAXBElement) e.unmarshal(CommonUtil.trim(fetcherResponse.getInputStream()))).getValue();
            ArrayList documentIdentifiers = new ArrayList();
            Iterator var5 = serviceGroup.getServiceMetadataReferenceCollection().getServiceMetadataReference().iterator();


            while (var5.hasNext()) {
                ServiceMetadataReferenceType reference = (ServiceMetadataReferenceType) var5.next();
                String[] parts = URLDecoder.decode(reference.getHref().split("/services/")[1], "UTF-8").split("::", 2);
                documentIdentifiers.add(new DocumentIdentifier(parts[1], parts[0]));
            }

            return documentIdentifiers;
        } catch (Exception var8) {
            //var8.printStackTrace();
            throw new RuntimeException(var8.getMessage(), var8);
        }
    }

    public ServiceMetadata parseServiceMetadata(FetcherResponse fetcherResponse) throws Exception, SecurityException {
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
                        serviceMetadata.addEndpoint(new Endpoint(processIdentifier, new TransportProfile(endpointType.getTransportProfile()), endpointType.getEndpointReference().getAddress().getValue(), (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(Base64.decodeBase64(endpointType.getCertificate())))));
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

    static {
        try {
            jaxbContext = JAXBContext.newInstance(new Class[]{ServiceGroupType.class, SignedServiceMetadataType.class, ServiceMetadataType.class});
        } catch (JAXBException var1) {
            throw new RuntimeException(var1.getMessage(), var1);
        }
    }
}