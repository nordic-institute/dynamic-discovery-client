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
package eu.europa.ec.dynamicdiscovery.core.provider.impl;

import eu.europa.ec.dynamicdiscovery.core.provider.IMetadataProvider;
import eu.europa.ec.dynamicdiscovery.model.identifiers.*;

import java.net.URI;

/**
 * @author Flávio W. R. Santos
 * @author Erlend Klakegg Bergheim
 * @since 1.0
 */
public class DefaultProvider implements IMetadataProvider {
    ParticipantIdentifierFormatter participantIdentifierFormatter = new ParticipantIdentifierFormatter();
    DocumentIdentifierFormatter documentIdentifierFormatter = new DocumentIdentifierFormatter();


    @Override
    public URI resolveForParticipantIdentifier(URI smpURI, SMPParticipantIdentifier participantIdentifier) {
        String participantPathParameter = participantIdentifierFormatter.urlEncodedFormat(participantIdentifier);
        return URI.create(smpURI.toString() + String.format("/%s", participantPathParameter)).normalize();
    }

    @Override
    public URI resolveServiceMetadata(URI smpURI, SMPParticipantIdentifier participantIdentifier,
                                      SMPDocumentIdentifier documentIdentifier) {
        String participantPathParameter = participantIdentifierFormatter.urlEncodedFormat(participantIdentifier);
        String documentPathParameter = documentIdentifierFormatter.urlEncodedFormat(documentIdentifier);

        return URI.create(smpURI.toString() + String.format("/%s/services/%s", participantPathParameter,documentPathParameter)).normalize();
    }

    public String format(SMPParticipantIdentifier identifier){
        return participantIdentifierFormatter.format(identifier);
    }
    public String urlEncodedFormat(SMPParticipantIdentifier identifier){
        return participantIdentifierFormatter.urlEncodedFormat(identifier);
    }

    public String format(SMPDocumentIdentifier identifier){
        return documentIdentifierFormatter.format(identifier);
    }

    public String urlEncodedFormat(SMPDocumentIdentifier identifier){
        return documentIdentifierFormatter.urlEncodedFormat(identifier);
    }
}
