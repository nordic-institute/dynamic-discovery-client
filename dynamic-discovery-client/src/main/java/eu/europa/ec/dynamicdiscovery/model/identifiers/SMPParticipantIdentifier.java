/*
 * Copyright 2017-2023 European Commission | CEF eDelivery
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence attached in file: LICENCE-EUPL-v1.2.pdf
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 */
package eu.europa.ec.dynamicdiscovery.model.identifiers;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author Flávio W. R. Santos
 * @author Erlend Klakegg Bergheim
 */
public class SMPParticipantIdentifier extends IdentifierType {

    public SMPParticipantIdentifier(String identifier, String scheme) {
        super(identifier, scheme);
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
        if (obj instanceof SMPParticipantIdentifier) {
            SMPParticipantIdentifier participantIdentifier = (SMPParticipantIdentifier) obj;
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
