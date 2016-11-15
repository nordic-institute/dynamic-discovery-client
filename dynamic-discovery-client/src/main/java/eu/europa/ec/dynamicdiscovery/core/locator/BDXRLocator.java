/*
 * (C) Copyright 2016 Dynamic Discovery Client
 *
 * https://ec.europa.eu/cefdigital/code/projects/EDELIVERY/repos/dynamic-discovery-client/browse
 *
 * Licensed under the LGPL, Version 2.1 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     dynamic-discovery\License_LGPL-2.1.txt or https://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Flávio W. R. Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 *
 */
package eu.europa.ec.dynamicdiscovery.core.locator;

import eu.europa.ec.dynamicdiscovery.core.locator.dns.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.IDNSLookup;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.util.HashUtil;
import org.xbill.DNS.TextParseException;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Flávio W. R. Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public class BDXRLocator extends AbstractLocator {

    public BDXRLocator(String hostname) {
        this(hostname, new DefaultDNSLookup());
    }

    public BDXRLocator(String hostname, IDNSLookup dnsLookup) {
        super(hostname, dnsLookup);
    }

    public URI lookup(ParticipantIdentifier participantIdentifier) throws TechnicalException {
        URI uri = naptrLookup(participantIdentifier);
        if (uri == null) {
            uri = cnameLookup(participantIdentifier);
        }
        if (uri == null) {
            throw new DNSLookupException(String.format("DNS Lookup was not able to retrieve information using NAPTR and/or CNAME for the participant [ %s ]", new Object[]{participantIdentifier.getIdentifier()}));
        }

        return uri;
    }

    private URI cnameLookup(ParticipantIdentifier participantIdentifier) {
        try {
            String e = HashUtil.getMD5Hash(participantIdentifier.getIdentifier());
            return new URI(String.format("http://b-%s.%s.%s", new Object[]{e, participantIdentifier.getScheme(), super.hostname}));
        } catch (URISyntaxException | UnsupportedEncodingException | NoSuchAlgorithmException exc) {
            throw new RuntimeException(exc.getMessage(), exc);
        }
    }

    private URI naptrLookup(ParticipantIdentifier participantIdentifier) {
        URI uri = null;
        try {
            String participantIdHashed = HashUtil.getSHA256HashBase32(participantIdentifier.getIdentifier());
            String smpURI = naptrLookupFetcher(participantIdentifier, String.format("%s.%s.%s", new Object[]{participantIdHashed, participantIdentifier.getScheme(), super.hostname}));
            uri = new URI(smpURI);
        } catch (URISyntaxException | UnsupportedEncodingException | NoSuchAlgorithmException | TextParseException exc) {
            throw new RuntimeException(exc.getMessage(), exc);
        } catch (TechnicalException | NullPointerException exc) {
            //It was not possible to lookup using NAPTR, CNAME lookup will be used instead
        }
        return uri;
    }

    private String naptrLookupFetcher(ParticipantIdentifier participantIdentifier, String uri) throws TechnicalException, TextParseException {
        return getDnsLookup().lookupFetcher(participantIdentifier, uri);
    }
}
