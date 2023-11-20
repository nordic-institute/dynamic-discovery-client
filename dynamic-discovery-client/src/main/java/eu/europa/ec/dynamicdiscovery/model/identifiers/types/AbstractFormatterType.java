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
 * @author Joze Rihtarsic
 * @since 4.3
 */
public abstract class AbstractFormatterType implements  FormatterType {

    boolean isSchemeMandatory = true;
    boolean wildcardEnabled = true;
    Integer schemeMaxLength;
    Integer valueMaxLength;
    Pattern valuePattern;
    Pattern schemePattern;


    @Override
    public boolean isSchemeMandatory() {
        return isSchemeMandatory;
    }

    public void setSchemeMandatory(boolean schemeMandatory) {
        isSchemeMandatory = schemeMandatory;
    }

    @Override
    public boolean isWildcardEnabled() {
        return wildcardEnabled;
    }

    @Override
    public void setWildcardEnabled(boolean wildcardEnabled) {
        this.wildcardEnabled = wildcardEnabled;
    }

    @Override
    public Integer getSchemeMaxLength() {
        return schemeMaxLength;
    }

    public void setSchemeMaxLength(Integer schemeMaxLength) {
        this.schemeMaxLength = schemeMaxLength;
    }

    @Override
    public Integer getValueMaxLength() {
        return valueMaxLength;
    }

    public void setValueMaxLength(Integer valueMaxLength) {
        this.valueMaxLength = valueMaxLength;
    }

    @Override
    public Pattern getValueValidationPattern() {
        return valuePattern;
    }

    public void setValueValidationPattern(Pattern valueRegExp) {
        this.valuePattern = valueRegExp;
    }

    public Pattern getSchemeValidationPattern() {
        return schemePattern;
    }

    public void setSchemeValidationPattern(Pattern schemePattern) {
        this.schemePattern = schemePattern;
    }
}
