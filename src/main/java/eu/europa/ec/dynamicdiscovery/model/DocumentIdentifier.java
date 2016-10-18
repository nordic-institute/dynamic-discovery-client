package eu.europa.ec.dynamicdiscovery.model;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public class DocumentIdentifier {
    private String documentIdentifier;
    private String scheme;

    public DocumentIdentifier(String documentIdentifier, String scheme) {
        this.documentIdentifier = documentIdentifier;
        this.scheme = scheme;
    }

    public String getScheme() {
        return this.scheme;
    }

    public String getDocumentIdentifier() {
        return documentIdentifier;
    }

    public String getIdentifier() {
        return String.format("%s::%s", new Object[]{this.scheme, this.documentIdentifier});
    }

    public String urlencoded() {
        try {
            return URLEncoder.encode(String.format("%s::%s", new Object[]{this.scheme, this.documentIdentifier}), "UTF-8");
        } catch (UnsupportedEncodingException var2) {
            throw new IllegalStateException("UTF-8 not supported.");
        }
    }
}
