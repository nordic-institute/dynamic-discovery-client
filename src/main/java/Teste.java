import eu.europa.ec.dynamicdiscovery.DynamicDiscovery;
import eu.europa.ec.dynamicdiscovery.DynamicDiscoveryBuilder;
import eu.europa.ec.dynamicdiscovery.ServiceMetadata;
import eu.europa.ec.dynamicdiscovery.fetcher.URLFetcher;
import eu.europa.ec.dynamicdiscovery.locator.BDXRLocator;
import eu.europa.ec.dynamicdiscovery.model.*;

import java.util.List;

/**
 * Created by rodrfla on 30/09/2016.
 */
public class Teste {

    public static void main(String[] args) throws Exception {
        final DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new BDXRLocator("edelivery.tech.ec.europa.eu"))
               //.locator(new BusdoxLocator("edelivery.tech.ec.europa.eu"))
//                .fetcher(new URLFetcher(new ProxyConfiguration("158.169.9.13", 8012, "j50b107", "34i6fv7")))
                .fetcher(new URLFetcher())
                .build();

        List<DocumentIdentifier> documentIdentifiers = smpClient.getDocumentIdentifiers(new ParticipantIdentifier("9925:0367302178")); //"0037:01841111111111")
        //final DocumentIdentifier documentIdentifier = new DocumentIdentifier(documentId);
        for (DocumentIdentifier doc : documentIdentifiers) {
            System.out.println("11"+doc.toString());
        }
        //  final ParticipantIdentifier participantIdentifier = new ParticipantIdentifier(receiverId, receiverIdType);
        final ProcessIdentifier processIdentifier = new ProcessIdentifier("urn:www.cenbii.eu:profile:bii04:ver1.0", "cenbii-procid-ubl");

        final ServiceMetadata sm = smpClient.getServiceMetadata(new ParticipantIdentifier("9925:0367302178"), documentIdentifiers.get(0));
        System.out.println("22"+sm.getDocumentIdentifier());
        System.out.println("22"+sm.getParticipantIdentifier());

        for (Endpoint endpoint : sm.getEndpoints()) {
            System.out.println("33"+endpoint.getProcessIdentifier());
            System.out.println(endpoint.getTransportProfile());
            System.out.println(endpoint.toString());
            Endpoint endpointDummy = sm.getEndpoint(endpoint.getProcessIdentifier(), endpoint.getTransportProfile(), TransportProfile.AS2_1_0); //bdxr-transport-ebms3-as4-v1p0
            System.out.println("ENDPOINT " + endpointDummy);
        }

        // Endpoint endpoint = sm.getEndpoint(processIdentifier, new TransportProfile("bdxr-transport-ebms3-as4-v1p0"), TransportProfile.AS4); //bdxr-transport-ebms3-as4-v1p0

        // final ProcessIdentifier processIdentifier = new ProcessIdentifier(processId, processIdType);
        System.out.println(documentIdentifiers.size());
    }


}
