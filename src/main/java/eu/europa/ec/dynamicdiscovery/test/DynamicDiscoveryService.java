package eu.europa.ec.dynamicdiscovery.test;

import eu.europa.ec.dynamicdiscovery.DynamicDiscovery;
import eu.europa.ec.dynamicdiscovery.ServiceMetadata;
import eu.europa.ec.dynamicdiscovery.model.*;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * Created by rodrfla on 30/09/2016.
 */
public class DynamicDiscoveryService {

    public static final String SMLZONE_KEY = "domibus.smlzone";
    @Resource(name = "domibusProperties")
    private Properties domibusProperties;

    public Endpoint lookupInformation(final String receiverId, final String receiverIdType, final String documentId, final String processId, final String processIdType)  {

        final String smlInfo = domibusProperties.getProperty(SMLZONE_KEY);
        if (smlInfo == null) {
           //throw new Exception("SML Zone missing. Configure in domibus-configuration.xml");
        }

        final DynamicDiscovery smpClient = null;/*DynamicDiscoveryBuilder.newInstance()
                .locator(new BusdoxLocator(smlInfo))
                .build();*/
        try {
            final ParticipantIdentifier participantIdentifier = new ParticipantIdentifier(receiverId, receiverIdType);
            final DocumentIdentifier documentIdentifier = new DocumentIdentifier(documentId,"busdox-docid-qns");

            final ProcessIdentifier processIdentifier = new ProcessIdentifier(processId, processIdType);

            final ServiceMetadata sm = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);

            final Endpoint endpoint;
            endpoint = sm.getEndpoint(processIdentifier, new TransportProfile("bdxr-transport-ebms3-as4-v1p0"), TransportProfile.AS4);

            if (endpoint == null) {
                throw new Exception("Receiver does not support reception of " + documentId + " for process " + processId + " using the AS4 Protocol");
            }
            return endpoint;

        } catch (final Exception e) {
           // throw new Exception("Receiver does not support reception of " + documentId + " for process " + processId + " using the AS4 Protocol", e);
        } //catch (final Exception e) {
            //LOG.error(e);
            //throw new Exception("Could not fetch metadata from SMP", e);
        //}

        //REMOVER
        return null;
    }
}
