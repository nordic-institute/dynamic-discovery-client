package eu.europa.ec.dynamicdiscovery.locator.dns;

import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public class LookupClient implements ILookupClient {

    private Lookup lookup;

    public LookupClient(String uri, int recordType) throws TextParseException {
        build(uri, recordType);
    }

    public LookupClient(Lookup lookup) throws TextParseException {
        this.lookup = lookup;
    }

    public Record[] run() {
        return lookup.run();
    }

    public void build(String uri, int recordType) throws TextParseException {
        lookup = new Lookup(uri, recordType);
    }

    public int getResultCode() {
        return lookup.getResult();
    }
}

