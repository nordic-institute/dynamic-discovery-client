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

import eu.europa.ec.dynamicdiscovery.core.locator.dns.IDNSLookup;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.impl.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.core.locator.impl.IMetadataLocator;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.util.HashUtil;
import org.xbill.DNS.TextParseException;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

public class DefaultBDXRLocator implements IMetadataLocator {

    private String hostname;
    private IDNSLookup dnsLookup;

    public DefaultBDXRLocator(String hostname) {
        this(hostname, new DefaultDNSLookup());
    }

    public DefaultBDXRLocator(String hostname, IDNSLookup dnsLookup) {
        this.hostname = hostname;
        this.dnsLookup = dnsLookup;
    }

    @Override
    public URI lookup(ParticipantIdentifier participantIdentifier) throws TechnicalException {
        URI participantIdentifierURI = naptrLookup(participantIdentifier);
        if (participantIdentifierURI == null) {
            participantIdentifierURI = cnameLookup(participantIdentifier);
        }

        return participantIdentifierURI;
    }

    @Override
    public URI lookup(String participantIdentifier, String participantScheme) throws TechnicalException {
        return this.lookup(new ParticipantIdentifier(participantIdentifier, participantScheme));
    }


    private URI cnameLookup(ParticipantIdentifier participantIdentifier) throws TechnicalException {
        try {
            String e = HashUtil.getMD5Hash(participantIdentifier.getIdentifier());
            return new URI(String.format("http://b-%s.%s.%s", new Object[]{e, participantIdentifier.getScheme(), hostname}));
        } catch (URISyntaxException | UnsupportedEncodingException | NoSuchAlgorithmException exc) {
            throw new DNSLookupException(exc.getMessage(), exc);
        }
    }

    private URI naptrLookup(ParticipantIdentifier participantIdentifier) throws TechnicalException {
        try {
            String participantIdHashed = HashUtil.getSHA256HashBase32(participantIdentifier.getIdentifier());
            String smpURI = naptrLookupFetcher(participantIdentifier, String.format("%s.%s.%s", new Object[]{participantIdHashed, participantIdentifier.getScheme(), hostname}));
            return new URI(smpURI);
        } catch (URISyntaxException | UnsupportedEncodingException | NoSuchAlgorithmException | TextParseException exc) {
            throw new DNSLookupException(exc.getMessage(), exc);
        } catch (TechnicalException | NullPointerException exc) {
            //It was not possible to lookup using NAPTR, CNAME lookup will be used instead
            return null;
        }
    }

    public String naptrLookupFetcher(ParticipantIdentifier participantIdentifier, String participantURI) throws TechnicalException, TextParseException {
        return getDnsLookup().lookupFetcher(participantIdentifier, participantURI);
    }

    @Override
    public IDNSLookup getDnsLookup() {
        return dnsLookup;
    }
}
