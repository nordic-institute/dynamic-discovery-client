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
package eu.europa.ec.dynamicdiscovery.core.locator.dns;

import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import org.apache.commons.lang3.StringUtils;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.NAPTRRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultDNSLookup implements IDNSLookup {

    private ILookupClient lookupClient;

    @Override
    public String lookupFetcher(ParticipantIdentifier participantIdentifier, String uri) throws TechnicalException {

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

    public String lookupFetcher(DefaultLookupClient lookupClient, ParticipantIdentifier participantIdentifier, String uri) throws TechnicalException {
        this.lookupClient = lookupClient;
        return lookupFetcher(participantIdentifier, uri);
    }

    @Override
    public List<Record> getAllRecords(Object... parameters) throws TechnicalException {
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

    public Record[] runLookup(String uri, Integer recordType) throws TechnicalException {
        if (lookupClient == null) {
            lookupClient = new DefaultLookupClient(uri, recordType);
        }
        return lookupClient.run();
    }
}
