/*
 * Copyright 2016-2023 - European Commission | Dynamic Discovery Client
 *
 * https://ec.europa.eu/digital-building-blocks/code/projects/EDELIVERY/repos/dynamic-discovery-client/browse
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

import eu.europa.ec.dynamicdiscovery.model.identifiers.types.EBCorePartyIdFormatterType;
import eu.europa.ec.dynamicdiscovery.model.identifiers.types.PeppolPartyIdFormatterType;

/**
 * Formatter for the ParticipantIdentifier with default "ebCoreParty" split regular expression and
 * '::' as split separator. For details see the {@link AbstractIdentifierFormatter}
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */
public class ParticipantIdentifierFormatter extends AbstractIdentifierFormatter<SMPParticipantIdentifier> {

    public ParticipantIdentifierFormatter() {
        addFormatterTypes(new EBCorePartyIdFormatterType());
        setDefaultFormatter(new PeppolPartyIdFormatterType());
        this.defaultFormatter = new PeppolPartyIdFormatterType();
    }

    public void setWildcardEnabled(boolean enable){
        this.formatterTypes.forEach(formatterType -> formatterType.setWildcardEnabled(enable));
        getDefaultFormatter().setWildcardEnabled(enable);
    }

    @Override
    protected String getSchemeFromObject(SMPParticipantIdentifier object) {
        return object != null ? object.getScheme() : null;
    }

    @Override
    protected String getIdentifierFromObject(SMPParticipantIdentifier object) {
        return object != null ? object.getIdentifier() : null;
    }

    @Override
    protected SMPParticipantIdentifier createObject(String scheme, String identifier) {
        return new SMPParticipantIdentifier(identifier, scheme);
    }

    @Override
    protected void updateObject(SMPParticipantIdentifier identifierObject, String scheme, String identifier) {
        identifierObject.setScheme(scheme);
        identifierObject.setIdentifier(identifier);
    }
}
