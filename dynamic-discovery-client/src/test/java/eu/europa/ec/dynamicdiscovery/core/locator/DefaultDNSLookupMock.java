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
package eu.europa.ec.dynamicdiscovery.core.locator;

import eu.europa.ec.dynamicdiscovery.core.locator.dns.impl.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import org.xbill.DNS.DClass;
import org.xbill.DNS.NAPTRRecord;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

import java.util.ArrayList;
import java.util.List;

public class DefaultDNSLookupMock extends DefaultDNSLookup {

    @Override
    public List<Record> getAllRecords(ParticipantIdentifier participantIdentifier, String uri) throws TechnicalException {
        try {
            List<Record> records = new ArrayList<>();
            records.add(new NAPTRRecord(DefaultDNSLookupTest.createParticipantDNSNameObjectBDXL(participantIdentifier.getIdentifier(), participantIdentifier.getScheme(), "ehealth.acc.edelivery.tech.ec.europa.eu."), DClass.IN, 60, 100, 10, "U", "Meta:SMP", "!^.*$!http://smp-mock-1.ehealth.eu:8888!", Name.fromString(".")));

            return records;
        } catch (Exception e) {
            throw new DNSLookupException(e.getMessage(), e);
        }
    }
}
