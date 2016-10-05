package eu.europa.ec.dynamicdiscovery.model;

/**
 * Created by rodrfla on 30/09/2016.
 */
public class TransportProfile {

    public static final TransportProfile START = new TransportProfile("busdox-transport-start");
    public static final TransportProfile AS2_1_0 = new TransportProfile("busdox-transport-as2-ver1p0");
    public static final TransportProfile AS4 = new TransportProfile("busdox-transport-ebms3-as4");
    private String identifier;

    public TransportProfile(String identifier) {
        this.identifier = identifier;
    }

    public boolean equals(Object o) {
        if(this == o) {
            return true;
        } else if(o != null && this.getClass() == o.getClass()) {
            boolean var10000;
            label35: {
                TransportProfile that = (TransportProfile)o;
                if(this.identifier != null) {
                    if(this.identifier.equals(that.identifier)) {
                        break label35;
                    }
                } else if(that.identifier == null) {
                    break label35;
                }

                var10000 = false;
                return var10000;
            }

            var10000 = true;
            return var10000;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.identifier != null?this.identifier.hashCode():0;
    }

    @Override
    public String toString() {
        return "TransportProfile{" +
                "identifier='" + identifier + '\'' +
                '}';
    }
}
