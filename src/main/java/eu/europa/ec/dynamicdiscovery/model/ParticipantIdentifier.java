package eu.europa.ec.dynamicdiscovery.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by rodrfla on 30/09/2016.
 */
public class ParticipantIdentifier {
    private static final long serialVersionUID = -8052874032415088055L;
    private String identifier;
    private String scheme;

    public ParticipantIdentifier(String identifier, String scheme) {
        this.identifier = identifier.trim().toLowerCase();
        this.scheme = scheme;
    }

    public ParticipantIdentifier(String identifier) {
        this(identifier, "iso6523-actorid-upis");
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getScheme() {
        return this.scheme;
    }

    public String urlencoded() {
        try {
            return URLEncoder.encode(String.format("%s::%s", new Object[]{this.scheme, this.identifier}), "UTF-8");
        } catch (UnsupportedEncodingException var2) {
            throw new IllegalStateException("UTF-8 not supported.");
        }
    }

    public boolean equals(Object o) {
        if(this == o) {
            return true;
        } else if(o != null && this.getClass() == o.getClass()) {
            ParticipantIdentifier that = (ParticipantIdentifier)o;
            if(this.identifier != null) {
                if(!this.identifier.equals(that.identifier)) {
                    return false;
                }
            } else if(that.identifier != null) {
                return false;
            }

            boolean var10000;
            label51: {
                if(this.scheme != null) {
                    if(!this.scheme.equals(that.scheme)) {
                        break label51;
                    }
                } else if(that.scheme != null) {
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
        int result = this.identifier != null?this.identifier.hashCode():0;
        result = 31 * result + (this.scheme != null?this.scheme.hashCode():0);
        return result;
    }
}
