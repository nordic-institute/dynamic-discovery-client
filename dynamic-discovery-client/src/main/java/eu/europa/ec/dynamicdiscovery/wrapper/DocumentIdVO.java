package eu.europa.ec.dynamicdiscovery.wrapper;

import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.net.URLEncoder;

/**
 * Created by rodrfla on 28/07/2017.
 */
public class DocumentIdVO {

    private String identifier;
    private String scheme;

    public DocumentIdVO(String documentIdentifier, String scheme) {
        this.identifier = documentIdentifier;
        this.scheme = scheme;
    }

    public String getScheme() {
        return this.scheme;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getFullIdentifier() {
        return String.format("%s::%s", this.scheme, this.identifier);
    }

    public String urlencoded() {
        try {
            return URLEncoder.encode(String.format("%s::%s", this.scheme, this.identifier), "UTF-8");
        } catch (Exception exc) {
            throw new IllegalStateException(exc.getMessage(), exc);
        }
    }

    @Override
    public String toString() {
        return "DocumentIdVO {" +
                "identifier='" + identifier + '\'' +
                ", scheme='" + scheme + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DocumentIdentifier) {
            DocumentIdentifier otherDocIdentifier = (DocumentIdentifier) obj;
            return new EqualsBuilder()
                    .append(identifier, otherDocIdentifier.getIdentifier())
                    .append(scheme, otherDocIdentifier.getScheme())
                    .isEquals();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(identifier)
                .append(scheme).toHashCode();
    }
}
