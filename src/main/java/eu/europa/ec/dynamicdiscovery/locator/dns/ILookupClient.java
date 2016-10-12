package eu.europa.ec.dynamicdiscovery.locator.dns;

import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;

/**
 * Created by FlavioSantos on 12-Oct-16.
 */
public interface ILookupClient {

    public Record[] run();

    public void build(String uri, int recordType) throws TextParseException;

    public int getResultCode();
}

