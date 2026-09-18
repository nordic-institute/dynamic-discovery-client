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
package eu.europa.ec.dynamicdiscovery.model.identifiers.types;

import eu.europa.ec.dynamicdiscovery.enums.DNSLookupFormatType;

import java.util.regex.Pattern;

/**
 * Simple OASIS SMP party identifier formatter.
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */
public class PeppolPartyIdFormatterType extends OasisSMPFormatterType {
    static final Pattern PEPPOL_SCHEME_PATTERN = Pattern.compile("^(?i)(iso6523-actorid-upis)");
    static final Pattern PEPPOL_MATCH_PATTERN = Pattern.compile("^(?i)\\s*(iso6523-actorid-upis::).*$");
    static final Pattern PEPPOL_VALUE_PATTERN = Pattern.compile("^(?i)\\d{4}:.{1,135}$");
    static final int PEPPOL_SCHEME_MAX_SIZE = 25;
    /**
     *
     * Policy for use of Identifiers 4.4.0 states that the maximum size of the value part of the identifier is 135 characters.
     * e.g. 4 number  ICD + ':' + 130 characters for the value part of the identifier = 135.
     */
    static final int PEPPOL_VALUE_MAX_SIZE = 135;


    /**
     * @deprecated Use {@link OasisSMPFormatterType.Builder} to create an instance of this class.
     */
    @Deprecated
    public PeppolPartyIdFormatterType() {
        this(new PeppolPartyIdFormatterType.Builder());
    }

    protected PeppolPartyIdFormatterType(PeppolPartyIdFormatterType.Builder builder) {
        super(builder);
    }

    @Override
    public String getInvalidSchemeMessage(String scheme, String identifier) {
        return schemePattern != null ? super.getInvalidSchemeMessage(scheme, identifier) :
                "The Scheme Identifier MUST take the form {domain}-{identifierArea}-{identifierType} such as for example 'busdox-actorid-upis'. It may only contain the following characters: [a-z0-9]+-[a-z0-9]+-[a-z0-9]+";
    }


    /**
     * Builder for PeppolPartyIdFormatterType formatter type creates the OasisSMPFormatterType with
     * Peppol specific parameters (e.g. v4.4.0 Policy for use of Identifiers):
     * <ul>
     *     <li><b>Wildcard enabled:</b> true</li>
     *     <li><b>Scheme mandatory:</b> false</li>
     *     <li><b>Scheme max size:</b> 25 characters</li>
     *     <li><b>Value max size:</b> 135 characters: The numeric identifier scheme is always 4 digits and the ":" separator added make it 4 + 1 + 130 = 135</li>
     *     <li><b>Scheme pattern:</b> ^(?i)iso6523-actorid-upis</li>
     *     <li><b>Match pattern:</b> ^(?i)iso6523-actorid-upis</li>
     *     <li><b>Value pattern:</b> ^(?i)\\d{4}:.{1,130}$</li>
     * </ul>
     */
    public static class Builder extends OasisSMPFormatterType.Builder {

        public Builder() {
            wildcardEnabled = true;
            valueMaxLength = PEPPOL_VALUE_MAX_SIZE;
            isSchemeMandatory = true;
            schemeMaxLength = PEPPOL_SCHEME_MAX_SIZE;
            schemePattern = PEPPOL_SCHEME_PATTERN;
            matchPattern = PEPPOL_MATCH_PATTERN;
            valuePattern = PEPPOL_VALUE_PATTERN;

            dnsLookupFormatType = DNSLookupFormatType.SCHEMA_AFTER_HASH;
        }

        @Override
        public PeppolPartyIdFormatterType build() {
            return new PeppolPartyIdFormatterType(this);
        }
    }
}
