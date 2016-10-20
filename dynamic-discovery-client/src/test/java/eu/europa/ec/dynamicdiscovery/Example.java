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
package eu.europa.ec.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.core.fetcher.URLFetcher;
import eu.europa.ec.dynamicdiscovery.core.locator.BDXRLocator;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.core.security.ProxyConfiguration;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ServiceMetadata;

import java.util.List;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public class Example {

    public static void main(String[] args) throws Exception {
        final DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(new BDXRLocator())
                .fetcher(new URLFetcher(new ProxyConfiguration("127.0.0.1", 8012, "user", "password")))
                .build();

        List<DocumentIdentifier> documentIdentifiers = smpClient.getDocumentIdentifiers(new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis"));

        DocumentIdentifier documentIdentifier = new DocumentIdentifier("urn::epsos:services##epsos-21", "epsos-docid-qns");
        ServiceMetadata sm = smpClient.getServiceMetadata(new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis"), documentIdentifier);
    }
}
