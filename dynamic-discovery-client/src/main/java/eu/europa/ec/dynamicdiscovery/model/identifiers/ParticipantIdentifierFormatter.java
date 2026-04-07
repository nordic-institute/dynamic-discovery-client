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

import eu.europa.ec.dynamicdiscovery.model.identifiers.types.EBCorePartyIdFormatterType;
import eu.europa.ec.dynamicdiscovery.model.identifiers.types.OasisSMPFormatterType;
import eu.europa.ec.dynamicdiscovery.model.identifiers.types.PeppolPartyIdFormatterType;

import java.util.regex.Pattern;

/**
 * Formatter for the ParticipantIdentifier with default "ebCoreParty" split regular expression and
 * '::' as split separator. For details see the {@link AbstractIdentifierFormatter}
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */
public class ParticipantIdentifierFormatter extends AbstractIdentifierFormatter<SMPParticipantIdentifier, ParticipantIdentifierFormatter> {


    /**
     * Protected constructor for ParticipantIdentifierFormatter.
     * Use {@link Builder} to create an instance.
     *
     * @param builder the builder instance
     */
    protected ParticipantIdentifierFormatter(Builder builder) {
        super(builder);
    }

    public void setWildcardEnabled(boolean enable) {
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

    /**
     * Builder class for creating instances of ProcessIdentifierFormatter.
     * This class extends AbstractBuilder to provide a fluent interface for building the formatter.
     */
    public static class Builder extends AbstractBuilder<ParticipantIdentifierFormatter> {
        @Override
        public ParticipantIdentifierFormatter build() {
            return new ParticipantIdentifierFormatter(this);
        }

       public Builder initDefault() {
            formatterTypes.clear();
            formatterTypes.add(new EBCorePartyIdFormatterType());
            formatterTypes.add(new PeppolPartyIdFormatterType.Builder().build());
            defaultFormatter = new OasisSMPFormatterType.Builder()
                    .schemeMandatory(false)
                    .wildcardEnabled(true)
                    .schemeMaxLength(25)
                    .schemeValidationPattern(Pattern.compile("^[a-zA-Z0-9]+-[a-zA-Z0-9]+-[a-zA-Z0-9]+$"))
                    .build();
            return this;
        }
    }

}
