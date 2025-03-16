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
     * Method  returns true if identifier is supported by the formatter for parsing
     * and formatting, else it returns false. The method formats the value before
     * checking with method boolean isType(final String value)
     *
     * @param scheme     identifier scheme part
     * @param identifier identifier value
     * @return return true if identifier is supported by the formatter else return false
     */
    default boolean isType(final String scheme, final String identifier) {
        return isType(format(scheme, identifier));
    }

    boolean isSchemeMandatory();
    void setSchemeMandatory(boolean schemeMandatory);

    Integer getSchemeMaxLength();
    void setSchemeMaxLength(Integer schemeMaxLength);

    Integer getValueMaxLength();
    void setValueMaxLength(Integer valueMaxLength);

    Pattern getValueValidationPattern();
    void setValueValidationPattern(Pattern valueRegExp);

    Pattern getSchemeValidationPattern();
    void setSchemeValidationPattern(Pattern valueRegExp);

    /**
     * It enables/disables wildcard support. If enabled, the formatter
     * will use wildcard character '* when generating DNS lookup if that
     * is set in the identifier value, else it will use the actual value.
     * The feature was specific for Peppol SMP technical specification and is deprecated.
     * @return true if wildcard is enabled, else false
     */
    @Deprecated
    boolean isWildcardEnabled();
    /**
     * Feature which enables/disables wildcard support. If enabled, the formatter will use wildcard character '* when generating DNS lookup if that
     * is set in the identifier value, else it will use the actual value.
     * The feature was specific for Peppol SMP technical specification and is deprecated.
     * @return true if wildcard is enabled, else false
     */
    @Deprecated
    void setWildcardEnabled(boolean wildcardEnabled);

    String format(final String scheme, final String identifier);

    String format(final String scheme, final String identifier, boolean noDelimiterOnEmptyScheme);

    // always returns array size 2 with first element as scheme and second as identifier part.
    String[] parse(final String value);


    DNSLookupFormatType getDNSFormatType();


    /**
     *  Returns only the hash value as part of the lookup request
     * @param scheme          scheme part of identifier
     * @param identifier      value part of identifier
     * @param dnsType dns lookup type
     * @param withPrefix include prefix in the hash e.g. B- for CNAME
     * @return  hash value
     */
    default String dnsLookupHash(final String scheme, final String identifier, DNSLookupHashType dnsType, boolean withPrefix) {
        DNSLookupFormatType dnsFormatType = getDNSFormatType();
        switch (dnsFormatType) {
            case ALL_IN_HASH:
                return dnsLookupFormatAllInHash(scheme, identifier, dnsType, withPrefix);
            case SCHEMA_AFTER_HASH:
                return dnsLookupFormatSchemaAfterHash("", identifier, dnsType, withPrefix);
            default:
                throw new DDCRuntimeException("DNS lookup [" +dnsFormatType + "] is not supported!");
        }
    }

    default  String dnsLookupFormat(String scheme, String identifier, DNSLookupHashType dnsLookupHashType) {
        // find the formatter
        String hashPart = dnsLookupHash(scheme, identifier, dnsLookupHashType, true);
        String suffixPart = dnsLookupSuffix(scheme, identifier, dnsLookupHashType);
        return hashPart + ((isEmpty(suffixPart) ? "" : "." + suffixPart));
    }

    /**
     *  Returns only the hash value as part of the lookup request
     * @param scheme          scheme part of identifier
     * @param identifier      value part of identifier
     * @param dnsType dns lookup type
     * @return  hash value
     */
    default String dnsLookupSuffix(final String scheme, final String identifier, DNSLookupHashType dnsType) {
        DNSLookupFormatType dnsFormatType = getDNSFormatType();
        switch (dnsFormatType) {
            case ALL_IN_HASH:
                return null;
            case SCHEMA_AFTER_HASH:
                return scheme;
            default:
                throw new DDCRuntimeException("DNS lookup [" +dnsFormatType + "] is not supported!");
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
            throw new MalformedIdentifierException(String.format("Identifier value [" + trimmedValue + "] is illegal."));
        }
    }

    default String getInvalidSchemeMessage(String scheme, String identifier) {
        return "Identifier: [" + identifier + "] has invalid scheme [" + scheme + "] (Check the length or scheme pattern)!";
    }

}
