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

import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;

import java.util.List;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public interface IDNSLookup {

    List<Record> getAllRecords(Object... parameters) throws DNSLookupException, TextParseException;

    String lookupFetcher(ParticipantIdentifier participantIdentifier, String uri) throws TechnicalException, TextParseException;
}
