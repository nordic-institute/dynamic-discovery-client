package eu.europa.ec.dynamicdiscovery.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by rodrfla on 30/09/2016.
 */
public class DocumentIdentifier {
    private static final long serialVersionUID = -3748163459655880167L;
    private String scheme;
    private String customizationId;
    private String xmlNamespace;
    private String xmlRootElement;
    private String xmlVersion;

    public DocumentIdentifier(String documentIdentifier) {
        this(documentIdentifier, "busdox-docid-qns");
    }

    public DocumentIdentifier(String documentIdentifier, String scheme) {
        String[] parts = documentIdentifier.split("::|##");
        this.xmlNamespace = parts[0];
        this.xmlRootElement = parts[1];
        this.customizationId = parts[2];
        this.xmlVersion = parts[3];
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
