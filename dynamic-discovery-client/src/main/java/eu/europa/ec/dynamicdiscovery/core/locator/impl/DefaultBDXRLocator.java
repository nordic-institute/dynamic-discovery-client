/*
 * (C) Copyright 2016 - European Commission | Dynamic Discovery Client
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
package eu.europa.ec.dynamicdiscovery.core.locator.impl;

import eu.europa.ec.dynamicdiscovery.core.fetcher.impl.DefaultURLFetcher;
import eu.europa.ec.dynamicdiscovery.core.locator.IMetadataLocator;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.IDNSLookup;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.impl.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.util.HashUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.TextParseException;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

public class DefaultBDXRLocator implements IMetadataLocator {
    final static Logger LOG = LoggerFactory.getLogger(DefaultBDXRLocator.class);

    private String domain;
    private IDNSLookup dnsLookup;

    public DefaultBDXRLocator(String domain) {
        this(domain, new DefaultDNSLookup());
    }

    public DefaultBDXRLocator(String domain, IDNSLookup dnsLookup) {
        this.domain = domain;
        this.dnsLookup = dnsLookup;
    }

    @Override
    public URI lookup(ParticipantIdentifier participantIdentifier) throws TechnicalException {
        URI participantIdentifierURI = naptrLookup(participantIdentifier);
        if (participantIdentifierURI == null) {
            LOG.debug("Did not find NAPTR record try cname lookup for participant: " + participantIdentifier);
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
            URI uri =  new URI(String.format("http://b-%s.%s.%s", e, participantIdentifier.getScheme(), domain));
            LOG.debug("Created CNAME lookup with url: "+uri+" for participant" + participantIdentifier.toString());
            return uri;
        } catch (URISyntaxException | UnsupportedEncodingException | NoSuchAlgorithmException exc) {
            throw new DNSLookupException(exc.getMessage(), exc);
        }
    }

    private URI naptrLookup(ParticipantIdentifier participantIdentifier) throws TechnicalException {
        try {
            LOG.debug("Start napr search for participant " + participantIdentifier );
            String participantIdHashed = HashUtil.getSHA256HashBase32(participantIdentifier.getIdentifier());
            String naptrURI =  String.format("%s.%s.%s",participantIdHashed, participantIdentifier.getScheme(), domain);
            String smpURI = naptrLookupFetcher(participantIdentifier,naptrURI);
            LOG.debug("Got URL: "+smpURI+" for participant " + participantIdentifier + " with naprt query url: " + naptrURI);
            return new URI(smpURI);
        } catch (URISyntaxException | UnsupportedEncodingException | NoSuchAlgorithmException | TextParseException exc) {
            throw new DNSLookupException(exc.getMessage(), exc);
        } catch (TechnicalException | NullPointerException exc) {
            LOG.debug("Naptr lookup was not possible, CNAME lookup will be used instead for participant" + participantIdentifier.toString());
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
