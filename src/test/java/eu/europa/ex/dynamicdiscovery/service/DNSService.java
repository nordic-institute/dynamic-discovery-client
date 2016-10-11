package eu.europa.ex.dynamicdiscovery.service;

import org.xbill.DNS.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by rodrfla on 30/08/2016.
 * <p>
 * This class can be used to test data consistency between the DNS and the database
 */
public class DNSService {
    private String dnsServer;
    private String dnsZoneName;
    private ZoneTransferIn axfr;

    public DNSService() throws TextParseException, UnknownHostException {
        this("ddnsext.tech.ec.europa.eu", "acc.edelivery.tech.ec.europa.eu");
    }

    public DNSService(String dnsServer, String dnsZoneName) throws TextParseException, UnknownHostException {
        if (!dnsZoneName.endsWith(".")) {
            dnsServer += '.';
        }
        this.dnsServer = dnsServer;
        this.dnsZoneName = dnsZoneName;
        this.axfr = ZoneTransferIn.newAXFR(Name.fromString(dnsZoneName), dnsServer, null);
    }

    public List<Record> getAllRecords() throws IOException, ZoneTransferException {
        List<Record> records = this.axfr.run();
        return records;
    }
}
