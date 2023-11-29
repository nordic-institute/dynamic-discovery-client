/*
 * Copyright 2017-2023 European Commission | eDelivery Dynamic Discovery Client
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence attached in file: LICENSE-EUPL-v1.2-EN.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
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
