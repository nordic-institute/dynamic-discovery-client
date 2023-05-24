package eu.europa.ec.dynamicdiscovery.model.identifiers;

/**
 * @author Joze Rihtarsic
 * @since 2.0
 */
public class IdentifierType {

    protected String identifier;
    protected String scheme;

    public IdentifierType() {
    }

    public IdentifierType(String identifier, String scheme) {
        this.identifier = identifier;
        this.scheme = scheme;
    }


    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }
}
