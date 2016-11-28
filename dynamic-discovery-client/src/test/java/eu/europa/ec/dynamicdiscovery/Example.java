package eu.europa.ec.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.core.fetcher.impl.DefaultURLFetcher;
import eu.europa.ec.dynamicdiscovery.core.locator.DefaultBDXRLocator;
import eu.europa.ec.dynamicdiscovery.core.reader.impl.DefaultBDXRReader;
import eu.europa.ec.dynamicdiscovery.core.security.impl.DefaultProxy;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ServiceMetadata;

/**
 * Created by rodrfla on 23/11/2016.
 */
public class Example {

    public static void main(String[] args) throws TechnicalException {
        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new DefaultBDXRLocator("ehealth.acc.edelivery.tech.ec.europa.eu"))
                .fetcher(new DefaultURLFetcher(new DefaultProxy("158.169.9.13", 8012, "j50b107", "34i6fv7")))
                .reader(new DefaultBDXRReader())
                .build();

        ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("urn:poland:ncpb", "ehealth-actorid-qns");
        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn::epsos##services:extended:epsos::107", "ehealth-resid-qns");

        ServiceMetadata result = smpClient.getServiceMetadata(participantIdentifier, documentIdentifier);
        System.out.println(result.getDocumentIdentifier());
    }

}

