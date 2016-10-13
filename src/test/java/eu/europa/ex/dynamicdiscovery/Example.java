package eu.europa.ex.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.DynamicDiscovery;
import eu.europa.ec.dynamicdiscovery.DynamicDiscoveryBuilder;
import eu.europa.ec.dynamicdiscovery.ServiceMetadata;
import eu.europa.ec.dynamicdiscovery.fetcher.URLFetcher;
import eu.europa.ec.dynamicdiscovery.locator.BDXRLocator;
import eu.europa.ec.dynamicdiscovery.locator.dns.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ProcessIdentifier;

import java.util.List;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public class Example {

    public static void main(String[] args) throws Exception {
        final DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new BDXRLocator("edelivery.tech.ec.europa.eu"))
                //.locator(new BDXRLocator("edelivery.tech.ec.europa.eu", new DefaultDNSLookup()))
                //.locator(new BusdoxLocator("edelivery.tech.ec.europa.eu"))
                //.fetcher(new URLFetcher(new ProxyConfiguration("192.168.1.01", 8059, "user", "password")))
                .fetcher(new URLFetcher())
                .build();

        List<DocumentIdentifier> documentIdentifiers = smpClient.getDocumentIdentifiers(new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis"));
        ProcessIdentifier processIdentifier = new ProcessIdentifier("urn:www.cenbii.eu:profile:bii04:ver1.0", "cenbii-procid-ubl");
        ServiceMetadata sm = smpClient.getServiceMetadata(new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis"), documentIdentifiers.get(0));
    }
}
