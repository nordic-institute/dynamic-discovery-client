/*
 * (C) Copyright 2016-2021 - European Commission | Dynamic Discovery Client
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
 */
package eu.europa.ec.dynamicdiscovery.core.locator.dns;

import eu.europa.ec.dynamicdiscovery.enums.DNSLookupType;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import org.xbill.DNS.Record;

import java.util.List;

/**
 * @author Flávio W. R. Santos
 */
public interface IDNSLookup {

    List<Record> getAllNaptrRecords(SMPParticipantIdentifier participantIdentifier, String uri) throws TechnicalException;
    List<Record> getAllCNameRecords(SMPParticipantIdentifier participantIdentifier, String uri) throws TechnicalException;

    List<Record> getAllRecordsForType(SMPParticipantIdentifier participantIdentifier, String uri, DNSLookupType recordType) throws TechnicalException;

    String naptrUrlValueLookup(SMPParticipantIdentifier participantIdentifier, String uri) throws TechnicalException;

    boolean dnsRecordNotExists(SMPParticipantIdentifier participantIdentifier, String participantURI, DNSLookupType type) throws TechnicalException;
}
