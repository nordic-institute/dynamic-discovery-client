package eu.europa.ex.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.DynamicDiscovery;
import eu.europa.ec.dynamicdiscovery.DynamicDiscoveryBuilder;
import eu.europa.ec.dynamicdiscovery.fetcher.URLFetcher;
import eu.europa.ec.dynamicdiscovery.locator.BDXRLocator;
import eu.europa.ec.dynamicdiscovery.locator.dns.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.security.ProxyConfiguration;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public class Example {

    public static void main(String[] args) throws Exception {
        final DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new BDXRLocator("edelivery.tech.ec.europa.eu", new DefaultDNSLookup()))
                .fetcher(new URLFetcher(new ProxyConfiguration("127.0.0.1", 8080, "user", "pass")))
                .build();

        //List<DocumentIdentifier> documentIdentifiers = smpClient.getDocumentIdentifiers(new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis"));
        //ServiceMetadata sm = smpClient.getServiceMetadata(new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis"), documentIdentifiers.get(0));
    }
}
