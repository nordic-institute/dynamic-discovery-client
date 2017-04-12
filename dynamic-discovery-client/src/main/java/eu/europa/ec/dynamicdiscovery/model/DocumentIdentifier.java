/*
 * (C) Copyright 2016 - European Commission | Dynamic Discovery Client
 *
 * https://ec.europa.eu/cefdigital/code/projects/EDELIVERY/repos/dynamic-discovery-client/browse
 *
 * Licensed under the LGPL, Version 2.1 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     dynamic-discovery\License_LGPL-2.1.txt or https://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Flávio W. R. Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 *
 */
package eu.europa.ec.dynamicdiscovery.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.net.URLEncoder;

public class DocumentIdentifier {
    private String identifier;
    private String scheme;

    public DocumentIdentifier(String documentIdentifier, String scheme) {
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
        String fullIdentifier = "";
        if(this.scheme != null) {
            fullIdentifier = String.format("%s::", this.scheme);
        }
        fullIdentifier += String.format("%s", this.identifier);
        return fullIdentifier;
    }

    public String urlencoded() {
        String urlEncoded = "";
        try {
            if(this.scheme != null) {
                urlEncoded = URLEncoder.encode(String.format("%s::", this.scheme), "UTF-8");
            }
            urlEncoded += URLEncoder.encode(String.format("%s", this.identifier), "UTF-8");
        } catch (Exception exc) {
            throw new IllegalStateException(exc.getMessage(), exc);
        }

        return urlEncoded;
    }

    @Override
    public String toString() {
        return "DocumentIdentifier{" +
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
