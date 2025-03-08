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

    @Override
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
    @Override
    public void setSchemeMaxLength(Integer schemeMaxLength) {
        this.schemeMaxLength = schemeMaxLength;
    }

    @Override
    public Integer getValueMaxLength() {
        return valueMaxLength;
    }

    @Override
    public void setValueMaxLength(Integer valueMaxLength) {
        this.valueMaxLength = valueMaxLength;
    }

    @Override
    public Pattern getValueValidationPattern() {
        return valuePattern;
    }

    @Override
    public void setValueValidationPattern(Pattern valueRegExp) {
        this.valuePattern = valueRegExp;
    }

    @Override
    public Pattern getSchemeValidationPattern() {
        return schemePattern;
    }

    @Override
    public void setSchemeValidationPattern(Pattern schemePattern) {
        this.schemePattern = schemePattern;
    }
}
