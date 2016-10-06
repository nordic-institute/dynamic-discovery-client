package eu.europa.ec.dynamicdiscovery.model;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 * @author Erlend Klakegg Bergheim - erlend.klakegg.bergheim@difi.no
 */
public class ProcessIdentifier {
    private static final long serialVersionUID = 7486398061021950763L;
    private String identifier;
    private String scheme;

    public ProcessIdentifier(String identifier, String scheme) {
        this.identifier = identifier;
        this.scheme = scheme;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getScheme() {
        return this.scheme;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            ProcessIdentifier that = (ProcessIdentifier) o;
            if (this.identifier != null) {
                if (!this.identifier.equals(that.identifier)) {
                    return false;
                }
            } else if (that.identifier != null) {
                return false;
            }

            boolean var10000;
            label51:
            {
                if (this.scheme != null) {
                    if (!this.scheme.equals(that.scheme)) {
                        break label51;
                    }
                } else if (that.scheme != null) {
                    break label51;
                }

                var10000 = true;
                return var10000;
            }

            var10000 = false;
            return var10000;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.identifier != null ? this.identifier.hashCode() : 0;
        result = 31 * result + (this.scheme != null ? this.scheme.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProcessIdentifier{" +
                "identifier='" + identifier + '\'' +
                ", scheme='" + scheme + '\'' +
                '}';
    }
}
