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
package eu.europa.ec.dynamicdiscovery.model.identifiers.types;

import java.util.regex.Pattern;

/**
 * Simple OASIS SMP party identifier formatter.
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */
public class PeppolPartyIdFormatterType extends OasisSMPFormatterType {
    static final Pattern PEPPOL_SCHEME_PATTERN = Pattern.compile("^[a-zA-Z0-9]+-[a-zA-Z0-9]+-[a-zA-Z0-9]+$");
    static final int PEPPOL_SCHEME_MAX_SIZE = 25;
    static final int PEPPOL_VALUE_MAX_SIZE = 50;


    public PeppolPartyIdFormatterType() {
        setWildcardEnabled(true);
        setValueMaxLength(PEPPOL_VALUE_MAX_SIZE);
        setSchemeMandatory(false);
        setSchemeMaxLength(PEPPOL_SCHEME_MAX_SIZE);
        setSchemeValidationPattern(PEPPOL_SCHEME_PATTERN);
    }

    @Override
    public String getInvalidSchemeMessage(String scheme, String identifier) {
        return "The Scheme Identifier MUST take the form {domain}-{identifierArea}-{identifierType} such as for example 'busdox-actorid-upis'. It may only contain the following characters: [a-z0-9]+-[a-z0-9]+-[a-z0-9]+";
    }

}
