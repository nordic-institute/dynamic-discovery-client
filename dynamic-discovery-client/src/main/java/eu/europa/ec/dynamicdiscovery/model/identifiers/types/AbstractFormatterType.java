package eu.europa.ec.dynamicdiscovery.model.identifiers.types;

import java.util.regex.Pattern;

/**
 * @author Joze Rihtarsic
 * @since 4.3
 */
public abstract class AbstractFormatterType implements  FormatterType {

    boolean isSchemeMandatory = true;
    boolean wildcardEnabled = true;
    Integer schemeMaxLength = null;
    Integer valueMaxLength = null;
    Pattern valuePattern = null;
    Pattern schemePattern = null;


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
