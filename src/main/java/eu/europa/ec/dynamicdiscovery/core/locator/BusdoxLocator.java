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
package eu.europa.ec.dynamicdiscovery.core.locator;

import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.util.HashUtil;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 * @author Erlend Klakegg Bergheim - erlend.klakegg.bergheim@difi.no
 */
public class BusdoxLocator extends AbstractLocator {

    public BusdoxLocator() {
        super(null);
    }

    public BusdoxLocator(String hostname) {
        super(hostname, null);
    }

    public URI lookup(ParticipantIdentifier participantIdentifier) {
        try {
            String e = HashUtil.getMD5Hash(participantIdentifier.getIdentifier());
            return new URI(String.format("http://b-%s.%s.%s", new Object[]{e, participantIdentifier.getScheme(), super.hostname}));
        } catch (URISyntaxException | UnsupportedEncodingException | NoSuchAlgorithmException exc) {
            throw new RuntimeException(exc.getMessage(), exc);
        }
    }
}
