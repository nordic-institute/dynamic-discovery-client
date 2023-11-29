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
import eu.europa.ec.dynamicdiscovery.enums.DNSLookupHashType;
import eu.europa.ec.dynamicdiscovery.exception.DDCRuntimeException;
import eu.europa.ec.dynamicdiscovery.exception.MalformedIdentifierException;
import eu.europa.ec.dynamicdiscovery.util.HashUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.StringUtils.length;

/**
 * Formatter type interface for formatting and parsing party identifiers
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */
public interface FormatterType {

    /**
     * Method  returns true if scheme is valid .
     *
     * @param scheme identifier scheme part
     * @return return true if scheme is valid by formatter configuration else return false
     */
    boolean isSchemeValid(final String scheme);

    /**
     * Method  returns true if identifier is supported by the formatter for parsing and formatting, else it returns false.
     *
     * @param value identifier value
     * @return return true if identifier is supported by the formatter else return false
     */
    boolean isType(final String value);


    /**
     * Method  returns true if identifier is supported by the formatter for parsing and formatting, else it returns false. The mehtod formats the value before checking with
     * method boolean isType(final String value)
     *
     * @param scheme     identifier scheme part
     * @param identifier identifier value
     * @return return true if identifier is supported by the formatter else return false
     */
    default boolean isType(final String scheme, final String identifier) {
        return isType(format(scheme, identifier));
    }

    boolean isWildcardEnabled();

    void setWildcardEnabled(boolean wildcardEnabled);

    boolean isSchemeMandatory();

    Integer getSchemeMaxLength();

    Integer getValueMaxLength();

    Pattern getValueValidationPattern();

    String format(final String scheme, final String identifier);

    String format(final String scheme, final String identifier, boolean noDelimiterOnEmptyScheme);

    // always returns array size 2 with first element as scheme and second as identifier part.
    String[] parse(final String value);


    DNSLookupFormatType getDNSFormatType();

    default String dnsLookupFormat(final String scheme, final String identifier, DNSLookupHashType dnsType) {
        switch (getDNSFormatType()) {
            case ALL_IN_HASH:
                return dnsLookupFormatAllInHash(scheme, identifier, dnsType, true);
            case SCHEMA_AFTER_HASH:
                return dnsLookupFormatSchemaAfterHash(scheme, identifier, dnsType, true);
            default:
                throw new DDCRuntimeException("DNS lookup [" + getDNSFormatType() + "] is not supported!");
        }
    }

    default String dnsLookupHash(final String scheme, final String identifier, DNSLookupHashType dnsType) {
        switch (getDNSFormatType()) {
            case ALL_IN_HASH:
                return dnsLookupFormatAllInHash(scheme, identifier, dnsType, false);
            case SCHEMA_AFTER_HASH:
                return dnsLookupFormatSchemaAfterHash("", identifier, dnsType, false);
            default:
                throw new DDCRuntimeException("DNS lookup [" + getDNSFormatType() + "] is not supported!");
        }
    }

    default String dnsLookupFormatAllInHash(final String scheme, final String identifier, DNSLookupHashType dnsType, boolean withPrefix) {
        // wildcard case see the document "Software Architecture Document"  bdmsl_allowed_wildcard
        if (isWildcardEnabled() && StringUtils.equals("*", identifier)) {
            return "*";
        }
        String hashValue = format(scheme, identifier, true);
        return HashUtil.getDnsDiscoveryHash(hashValue, dnsType, withPrefix);
    }


    default String dnsLookupFormatSchemaAfterHash(final String scheme, final String identifier, DNSLookupHashType dnsType, boolean withPrefix) {
        // wildcard case see the document "Software Architecture Document"  bdmsl_allowed_wildcard
        String value = isWildcardEnabled() && StringUtils.equals("*", identifier) ? identifier :
                HashUtil.getDnsDiscoveryHash(identifier, dnsType, withPrefix);

        return value + (StringUtils.isEmpty(scheme) ? "" : "." + scheme);
    }

    /**
     * Strict validation of the scheme. If scheme is not supported or is blank and is mandatory, exception is thrown.
     *
     * @param scheme     Identifier scheme part
     * @param identifier the complete identifier using identifier and scheme part
     */
    default void validateScheme(String scheme, String identifier) {
        String trimmedScheme = trim(scheme);

        boolean isSchemeBlank = isBlank(trimmedScheme);
        if (isSchemeBlank) {
            if (isSchemeMandatory()) {
                throw new MalformedIdentifierException("Invalid Identifier: [" + identifier + "]. Can not detect schema!");
            }
            // else just terminate the validation
            return;
        }
        Integer schemeMaxLength = getSchemeMaxLength();
        if (schemeMaxLength != null && schemeMaxLength < length(trimmedScheme)) {
            throw new MalformedIdentifierException("A scheme identifier MUST NOT exceed " + schemeMaxLength + " characters!");
        }
        if (!isSchemeValid(trimmedScheme)) {
            throw new MalformedIdentifierException(getInvalidSchemeMessage(trimmedScheme, identifier));
        }
    }

    /**
     * Strict validation of the scheme. If scheme is not supported or is blank and is mandatory, exception is thrown.
     *
     * @param value      Identifier scheme part
     * @param identifier the complete identifier using identifier and scheme part
     */
    default void validateValue(String value, String identifier) {
        String trimmedValue = trim(value);

        if (isBlank(trimmedValue)) {
            throw new MalformedIdentifierException("Identifier must not be 'null' or empty");
        }
        Integer valueMaxLength = getValueMaxLength();

        if (valueMaxLength != null && valueMaxLength < length(trimmedValue)) {
            throw new MalformedIdentifierException("A identifier value MUST NOT exceed " + valueMaxLength + " characters!");
        }
        Pattern valueValidationPattern = getValueValidationPattern();
        if (trimmedValue != null && valueValidationPattern != null && !valueValidationPattern.matcher(trimmedValue).matches()) {
            throw new MalformedIdentifierException(String.format("Identifier value " + trimmedValue + " is illegal."));
        }
    }

    default String getInvalidSchemeMessage(String scheme, String identifier) {
        return "Invalid Identifier: [" + identifier + "]. Invalid scheme [" + scheme + "]!";
    }

}

