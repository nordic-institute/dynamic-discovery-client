/*
 * Copyright 2016 Dynamic Discovery Client Project
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/servlets/Docbb6d.pdf?id=31979
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */
package eu.europa.ec.dynamicdiscovery.model;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 * @author Erlend Klakegg Bergheim - erlend.klakegg.bergheim@difi.no
 */
public class ProcessIdentifier {

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
