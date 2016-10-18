package eu.europa.ec.dynamicdiscovery.model;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 * @author Erlend Klakegg Bergheim - erlend.klakegg.bergheim@difi.no
 */
public class DocumentIdentifier implements IDocumentIdentifier {
    private String identifierScheme;
    private String rootNamespace;
    private String documentElementLocalName;
    private String subTypeIdentifier;

    private String scheme;
    private String customizationId;
    private String xmlNamespace;
    private String xmlRootElement;
    private String xmlVersion;

    public DocumentIdentifier(String documentIdentifier, String scheme) {
        String[] parts = documentIdentifier.split("::|##"); //"::|##",3
        if (parts.length == 4) {
            this.xmlNamespace = parts[0];
            this.xmlRootElement = parts[1];
            this.customizationId = parts[2];
            this.xmlVersion = parts[3];

            identifierScheme = parts[0];
            rootNamespace = parts[1];
            documentElementLocalName = parts[2];
            subTypeIdentifier = parts[3];
        } else {
            this.customizationId = documentIdentifier;
        }
        this.scheme = scheme;
    }

    public String getScheme() {
        return this.scheme;
    }

    public String getIdentifier() {
        if (StringUtils.isEmpty(this.xmlNamespace) && StringUtils.isEmpty(this.xmlRootElement) && StringUtils.isEmpty(this.xmlVersion)) {
            return this.customizationId;
        }
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
