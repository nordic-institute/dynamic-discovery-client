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
package eu.europa.ec.dynamicdiscovery.core.locator.dns;

import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import org.apache.commons.lang3.StringUtils;
import org.xbill.DNS.*;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public class DefaultDNSLookup implements IDNSLookup {

    private ILookupClient lookupClient;

    @Override
    public String lookupFetcher(ParticipantIdentifier participantIdentifier, String uri) throws TechnicalException, TextParseException {
        List<Record> records = getAllRecords(uri, participantIdentifier);

        String smpAddress = null;
        String naptrRegex = null;
        for (Record record : records) {
            NAPTRRecord naptrRecord = (NAPTRRecord) record;
            String regex = ".*?(http.*[^!])";
            naptrRegex = naptrRecord.getRegexp();
            Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher m = p.matcher(naptrRecord.getRegexp());
            if (m.find()) {
                smpAddress = m.group(1);
            }
        }

        if (StringUtils.isEmpty(smpAddress)) {
            throw new DNSLookupException(String.format("DNS Lookup for NATPR record failed, CODE: ", new Object[]{naptrRegex}));
        }

        return smpAddress;
    }

    public String lookupFetcher(LookupClient lookupClient, ParticipantIdentifier participantIdentifier, String uri) throws TechnicalException, TextParseException {
        this.lookupClient = lookupClient;
        return lookupFetcher(participantIdentifier, uri);
    }

    @Override
    public List<Record> getAllRecords(Object... parameters) throws DNSLookupException, TextParseException {

        if (parameters == null || parameters.length != 2) {
            throw new DNSLookupException(String.format("Parameters for NAPTR Loopup are NULL or Incorrect [%s].", new Object[]{parameters}));
        }
        String uri = (String) parameters[0];
        String participantId = ((ParticipantIdentifier) parameters[1]).getIdentifier();
        Record[] records = runLookup(uri, Type.NAPTR);
        if (lookupClient.getResultCode() != Lookup.SUCCESSFUL) {
            throw new DNSLookupException(String.format("NAPTR Lookup for participant [ %s ] has failed. Lookup result CODE [ %s ]", new Object[]{participantId, lookupClient.getResultCode()}));
        }

        return Arrays.asList(records);
    }

    public Record[] runLookup(String uri, Integer recordType) throws TextParseException {
        if (lookupClient == null) {
            lookupClient = new LookupClient(uri, recordType);
        }
        return lookupClient.run();
    }
}
