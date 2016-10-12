package eu.europa.ec.dynamicdiscovery.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 * @author Erlend Klakegg Bergheim - erlend.klakegg.bergheim@difi.no
 */
public class DocumentIdentifier {
    private static final long serialVersionUID = -3748163459655880167L;
    private String scheme;
    private String customizationId;
    private String xmlNamespace;
    private String xmlRootElement;
    private String xmlVersion;

    public DocumentIdentifier(String documentIdentifier, String scheme) {
        String[] parts = documentIdentifier.split("::|##");
        //"urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1""
        //urn::epsos##services:extended:epsos
        this.xmlNamespace = parts[0]; //urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2
        this.xmlRootElement = parts[1]; //CreditNote
        this.customizationId = parts[2]; //urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0
        if (parts.length > 3) {
            this.xmlVersion = parts[3]; // 2.1
        }
        this.scheme = scheme;
    }

    public String getScheme() {
        return this.scheme;
    }

    public String getIdentifier() {
        return String.format("%s::%s##%s::%s", new Object[]{this.xmlNamespace, this.xmlRootElement, this.customizationId, this.xmlVersion});
    }

    public String getCustomizationId() {
        return this.customizationId;
    }

    public String getXmlNamespace() {
        return this.xmlNamespace;
    }

    public String getXmlRootElement() {
        return this.xmlRootElement;
    }

    public String getXmlVersion() {
        return this.xmlVersion;
    }

    public String toString() {
        return String.format("%s::%s", new Object[]{this.scheme, this.getIdentifier()});
    }

    public String urlencoded() {
        try {
            return URLEncoder.encode(String.format("%s::%s", new Object[]{this.scheme, this.getIdentifier()}), "UTF-8");
        } catch (UnsupportedEncodingException var2) {
            throw new IllegalStateException("UTF-8 not supported.");
        }
    }
}
