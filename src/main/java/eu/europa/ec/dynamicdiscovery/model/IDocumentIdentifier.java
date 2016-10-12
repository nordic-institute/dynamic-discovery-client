package eu.europa.ec.dynamicdiscovery.model;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public interface IDocumentIdentifier {

    public String getScheme();

    public String getIdentifier();

    public String toString();

    public String urlencoded();
}
