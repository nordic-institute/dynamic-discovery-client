/*
 * (C) Copyright 2016-2021 - European Commission | Dynamic Discovery Client
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
 */
package eu.europa.ec.dynamicdiscovery.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.net.URLEncoder;

import static org.apache.commons.lang3.StringUtils.*;

/**
 * @author Flávio W. R. Santos
 * @author Erlend Klakegg Bergheim
 */
public class ParticipantIdentifier {
    public static final String EBCORE_IDENTIFIER_PREFIX = "urn:oasis:names:tc:ebcore:partyid-type";

    private String identifier;
    private String scheme;


    public ParticipantIdentifier(String identifier, String scheme) {
        this.identifier = lowerCase(trim(identifier));
        this.scheme = trim(scheme);
        normalizeIdentifier();
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getScheme() {
        return this.scheme;
    }

    public String urlencoded() {
        try {
            return URLEncoder.encode(isOasisPartyIdentifierType() || StringUtils.isBlank(this.scheme) ? this.identifier
                            : String.format("%s::%s", this.scheme, this.identifier),
                    "UTF-8");
        } catch (Exception exc) {
            throw new IllegalStateException(exc.getMessage(), exc);
        }
    }

    public void normalizeIdentifier() {
        if (isNotBlank(scheme) && startsWithIgnoreCase(scheme, EBCORE_IDENTIFIER_PREFIX)) {
            identifier = appendIfMissing(lowerCase(scheme), ":") + identifier;
            scheme = null;
        }
    }

    public boolean isOasisPartyIdentifierType() {
        return startsWith(identifier, EBCORE_IDENTIFIER_PREFIX);
    }

    @Override
    public String toString() {
        return "ParticipantIdentifier{" +
                "identifier='" + identifier + '\'' +
                ", scheme='" + scheme + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ParticipantIdentifier) {
            ParticipantIdentifier participantIdentifier = (ParticipantIdentifier) obj;
            return new EqualsBuilder()
                    .append(identifier, participantIdentifier.getIdentifier())
                    .append(scheme, participantIdentifier.getScheme())
                    .isEquals();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(identifier)
                .append(scheme)
                .toHashCode();
    }
}
