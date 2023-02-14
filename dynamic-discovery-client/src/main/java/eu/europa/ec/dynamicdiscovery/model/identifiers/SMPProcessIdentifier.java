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
package eu.europa.ec.dynamicdiscovery.model.identifiers;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author Flávio W. R. Santos
 * @author Erlend Klakegg Bergheim
 * @since 1.0
 */
public class SMPProcessIdentifier extends IdentifierType {


    public SMPProcessIdentifier(String identifier, String scheme) {
        super(identifier, scheme);
    }

    public SMPProcessIdentifier(String identifier) {
        this(identifier, null);
    }


    @Override
    public String toString() {
        return "ProcessIdentifier{" +
                "identifier='" + identifier + '\'' +
                ", scheme='" + scheme + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SMPProcessIdentifier) {
            SMPProcessIdentifier processIdentifier = (SMPProcessIdentifier) obj;
            return new EqualsBuilder()
                    .append(identifier, processIdentifier.getIdentifier())
                    .append(scheme, processIdentifier.getScheme())
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
