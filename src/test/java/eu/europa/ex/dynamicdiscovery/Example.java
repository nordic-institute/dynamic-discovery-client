package eu.europa.ex.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.DynamicDiscovery;
import eu.europa.ec.dynamicdiscovery.DynamicDiscoveryBuilder;
import eu.europa.ec.dynamicdiscovery.core.fetcher.URLFetcher;
import eu.europa.ec.dynamicdiscovery.core.locator.BDXRLocator;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.core.security.ProxyConfiguration;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public class Example {

    public static void main(String[] args) throws Exception {
        final DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new BDXRLocator("edelivery.tech.ec.europa.eu", new DefaultDNSLookup()))
                .fetcher(new URLFetcher(new ProxyConfiguration("158.169.9.13", 8012, "j50b107", "34i6fv7")))
                .build();

        //List<DocumentIdentifier> documentIdentifiers = smpClient.getDocumentIdentifiers(new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis"));
        //ServiceMetadata sm = smpClient.getServiceMetadata(new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis"), documentIdentifiers.get(0));
    }
}
