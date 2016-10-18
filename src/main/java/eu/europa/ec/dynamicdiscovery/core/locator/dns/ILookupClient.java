package eu.europa.ec.dynamicdiscovery.core.locator.dns;

import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public interface ILookupClient {

    public Record[] run();

    public void build(String uri, int recordType) throws TextParseException;

    public int getResultCode();
}

