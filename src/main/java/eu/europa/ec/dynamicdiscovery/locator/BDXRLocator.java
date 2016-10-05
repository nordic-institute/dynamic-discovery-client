package eu.europa.ec.dynamicdiscovery.locator;

import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.util.HashUtil;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

/**
 * Created by rodrfla on 30/09/2016.
 */
public class BDXRLocator extends AbstractLocator {

    public BDXRLocator() {
        super();
    }

    public BDXRLocator(String hostname) {
        super(hostname);
    }

    public URI lookup(ParticipantIdentifier participantIdentifier) throws Exception {
        try {
            String e = HashUtil.getSHA256HashBase32(participantIdentifier.getIdentifier());
          //  lookupManager();
           // System.exit(0);
            return new URI(String.format("http://%s.%s.%s", new Object[]{e, participantIdentifier.getScheme(), super.hostname}));
        } catch (URISyntaxException var3) {
            throw new Exception(var3.getMessage(), var3);
        }
    }


    public void lookupManager() throws InterruptedException,
            UnknownHostException, TextParseException {

        // Resolver resolver = new SimpleResolver("ec.europa.eu");
        //  Lookup.setDefaultResolver(resolver);

        //  Lookup.setDefaultSearchPath(LOCAL_SEARCH_PATH);
        //  Lookup.setDefaultCache(new Cache(), DClass.IN);

        Lookup lookup = new Lookup("ec.europa.eu", Type.ANY);
        Record[] records = lookup.run();

        if (lookup.getResult() == Lookup.SUCCESSFUL) {
            for (Record record : records) {
                System.out
                        .println("record: " + record);
              /*  NAPTRRecord naptrRecord = (NAPTRRecord) record;
                if (naptrRecord.getFlags().contains("a")) {
                    // replacement contains A/AAAA target
                    Name nodeName = naptrRecord.getReplacement();
                    System.out
                            .println("Candidate node: " + nodeName.toString());

                    Lookup addressLookup = new Lookup(nodeName, Type.A);
                    addressLookup.setCredibility(Credibility.ANY);
                    Record[] addressRecords = addressLookup.run();
                    if (addressLookup.getResult() == Lookup.SUCCESSFUL) {
                        for (Record addressRecord : addressRecords) {
                            String nodeAddress = ((ARecord) addressRecord)
                                    .getAddress().getHostAddress();
                            System.out.println("\t" + nodeAddress);
                        }
                    }
                }
                System.out.println();*/
            }
        }

    }
}
