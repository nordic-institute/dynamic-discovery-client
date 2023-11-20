
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
package eu.europa.ec.dynamicdiscovery.core.provider;

import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;

import java.net.URI;
import java.util.List;

/**
 * @author Flávio W. R. Santos
 */
public interface IMetadataProvider {

    URI resolveForParticipantIdentifier(URI smpURI, SMPParticipantIdentifier participantIdentifier);

    URI resolveServiceMetadata(URI smpURI, SMPParticipantIdentifier participantIdentifier, SMPDocumentIdentifier documentIdentifier) throws TechnicalException;

    void setWildcardSchemes(List<String> wildcardSchemes);

    List<String> getWildcardSchemes();
}
