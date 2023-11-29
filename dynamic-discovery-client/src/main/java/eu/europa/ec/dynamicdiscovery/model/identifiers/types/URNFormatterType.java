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
package eu.europa.ec.dynamicdiscovery.model.identifiers.types;

import eu.europa.ec.dynamicdiscovery.enums.DNSLookupFormatType;

import java.util.regex.Pattern;


/**
 * Special case TemplateFormatterType for custom urn identifiers
 * The format is defined by template: "${scheme}:${identifier}" with separator character ':'.
 *
 * @author Joze RIHTARSIC
 * @since 2.0
 */
public class URNFormatterType extends TemplateFormatterType {

    public URNFormatterType(Pattern splitRegularExpression) {
        super(null, "${scheme}:${identifier}", "${identifier}", splitRegularExpression, DNSLookupFormatType.ALL_IN_HASH);
    }
}
