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
