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
