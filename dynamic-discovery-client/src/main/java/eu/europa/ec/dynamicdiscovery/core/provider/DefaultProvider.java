/*
 * (C) Copyright 2016 Dynamic Discovery Client
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
 * @author Erlend Klakegg Bergheim - erlend.klakegg.bergheim@difi.no
 *
 */
package eu.europa.ec.dynamicdiscovery.core.provider;

import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;

import java.net.URI;

public class DefaultProvider implements IMetadataProvider {

    public DefaultProvider() {
    }

    @Override
    public URI resolveDocumentIdentifiers(URI location, ParticipantIdentifier participantIdentifier) {
        return location.resolve(String.format("/%s", new Object[]{participantIdentifier.urlencoded()}));
    }

    @Override
    public URI resolveServiceMetadata(URI location, ParticipantIdentifier participantIdentifier, DocumentIdentifier documentIdentifier) {
        return location.resolve(String.format("/%s/services/%s", new Object[]{participantIdentifier.urlencoded(), documentIdentifier.urlencoded()}));
    }
}
