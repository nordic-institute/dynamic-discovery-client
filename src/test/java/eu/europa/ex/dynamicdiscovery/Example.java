/*
 * Copyright 2016 Dynamic Discovery Client Project
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/servlets/Docbb6d.pdf?id=31979
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */
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
