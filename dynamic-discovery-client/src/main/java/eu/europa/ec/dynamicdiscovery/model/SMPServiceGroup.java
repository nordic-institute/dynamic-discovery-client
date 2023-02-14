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


import eu.europa.ec.dynamicdiscovery.core.reader.DataWrapper;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.util.SMPEqualsBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;

/**
 * @author Flávio W. R. Santos
 */
public class SMPServiceGroup implements DataWrapper {

    private final Object serviceGroupType;
    private final List<SMPDocumentIdentifier> documentIdentifiers;
    private final SMPParticipantIdentifier participantIdentifier;


    public SMPServiceGroup(SMPParticipantIdentifier participantIdentifier, List<SMPDocumentIdentifier> documentIdentifiers, Object serviceGroupType) {
        this.participantIdentifier = participantIdentifier;
        this.documentIdentifiers = documentIdentifiers;
        this.serviceGroupType = serviceGroupType;
    }

    public SMPParticipantIdentifier getParticipantIdentifier() {
        return participantIdentifier;
    }

    public List<SMPDocumentIdentifier> getDocumentIdentifiers() {
        return documentIdentifiers;
    }

    @Override
    public <T> T unwrap(Class<T> iface){
        return isWrapperFor(iface) ? (T) serviceGroupType : null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface)  {
        return serviceGroupType != null && serviceGroupType.getClass().isAssignableFrom(iface);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SMPServiceGroup that = (SMPServiceGroup) o;

        return new EqualsBuilder()
                .append(participantIdentifier, that.participantIdentifier)
                .isEquals()
                && new SMPEqualsBuilder()
                .append(documentIdentifiers, that.documentIdentifiers)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(documentIdentifiers)
                .append(participantIdentifier).toHashCode();
    }
}
