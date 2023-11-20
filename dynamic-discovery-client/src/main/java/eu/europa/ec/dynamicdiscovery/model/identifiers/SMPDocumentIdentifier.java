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

import java.net.URI;

/**
 * @author Flávio W. R. Santos
 * @author Cosmin Baciu
 * @since 1.0
 */
public class SMPDocumentIdentifier extends IdentifierType {

    /**
     * The URI of this document identifier. Filled in case the DocumentIdentifier was retrieved from the ServiceGroup
     */
    protected URI documentIdentifierSmpURI;

    public SMPDocumentIdentifier(String documentIdentifier, String scheme) {
        super(documentIdentifier, scheme);
    }

    public SMPDocumentIdentifier(String documentIdentifier) {
        this(documentIdentifier, null);
    }

    public SMPDocumentIdentifier(String documentIdentifier, String scheme, URI documentIdentifierSmpURI) {
        this(documentIdentifier, scheme);
        this.documentIdentifierSmpURI = documentIdentifierSmpURI;
    }

    public URI getDocumentIdentifierSmpURI() {
        return documentIdentifierSmpURI;
    }

    public void setDocumentIdentifierSmpURI(URI documentIdentifierSmpURI) {
        this.documentIdentifierSmpURI = documentIdentifierSmpURI;
    }

    @Override
    public String toString() {
        return "DocumentIdentifier{" +
                "identifier='" + identifier + '\'' +
                ", scheme='" + scheme + '\'' +
                ", documentIdentifierSmpURI='" + documentIdentifierSmpURI + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SMPDocumentIdentifier) {
            SMPDocumentIdentifier otherDocIdentifier = (SMPDocumentIdentifier) obj;
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
