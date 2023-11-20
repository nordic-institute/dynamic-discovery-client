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
package eu.europa.ec.dynamicdiscovery.model;


import eu.europa.ec.dynamicdiscovery.core.reader.DataWrapper;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.util.SMPEqualsBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.net.URI;
import java.util.List;

/**
 * @author Flávio W. R. Santos
 */
public class SMPServiceGroup implements DataWrapper {

    private final Object serviceGroupType;

    private URI serviceGroupSmpURI;

    private final List<SMPDocumentIdentifier> documentIdentifiers;
    private final SMPParticipantIdentifier participantIdentifier;


    public SMPServiceGroup(SMPParticipantIdentifier participantIdentifier, List<SMPDocumentIdentifier> documentIdentifiers, Object serviceGroupType) {
        this.participantIdentifier = participantIdentifier;
        this.documentIdentifiers = documentIdentifiers;
        this.serviceGroupType = serviceGroupType;
    }

    public URI getServiceGroupSmpURI() {
        return serviceGroupSmpURI;
    }

    public void setServiceGroupSmpURI(URI serviceGroupSmpURI) {
        this.serviceGroupSmpURI = serviceGroupSmpURI;
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
