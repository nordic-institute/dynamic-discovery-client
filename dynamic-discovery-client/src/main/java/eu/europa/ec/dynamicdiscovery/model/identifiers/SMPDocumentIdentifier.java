/*
 * #%L
 * dynamic-discovery-cli
 * %%
 * Copyright (C) 2016 - 2023 European Commission | eDelivery | Dynamic Discovery Client
 * %%
 * Licensed under the LGPL, Version 2.1 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * [PROJECT_HOME]\license\lgpl2-1\license.txt or https://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
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
