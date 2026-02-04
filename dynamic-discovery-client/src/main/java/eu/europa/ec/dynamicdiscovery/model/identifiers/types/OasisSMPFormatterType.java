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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.*;

/**
 * Simple OASIS SMP party identifier formatter.
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */
public class OasisSMPFormatterType extends AbstractFormatterType {
    private static final Logger LOG = LoggerFactory.getLogger(OasisSMPFormatterType.class);
    DNSLookupFormatType dnsLookupFormatType;
    Pattern matchPattern;

    protected OasisSMPFormatterType(Builder builder) {
        super(builder);
        this.dnsLookupFormatType = builder.dnsLookupFormatType;
        this.matchPattern = builder.matchPattern;
    }

    @Override
    public boolean isSchemeValid(final String scheme) {

        return StringUtils.isBlank(scheme) || schemePattern == null || schemePattern.matcher(trim(scheme)).matches();
    }

    @Override
    public boolean isType(final String value) {
        if (matchPattern == null) {
            // if no match pattern is defined, then the value is valid
            LOG.debug("No match pattern defined for OasisSMPFormatterType, assuming all values are valid.");
            return true;
        }
        return matchPattern.matcher(value).matches();

    }

    @Override
    public String format(String scheme, String identifier, boolean noDelimiterOnEmptyScheme) {
        return (isBlank(scheme) && noDelimiterOnEmptyScheme ? "" : trimToEmpty(scheme) + OASIS_SMP_SEPARATOR) + trimToEmpty(identifier);

    }

    @Override
    public String format(final String scheme, final String identifier) {
        // for OASIS SMP 1.0 the separator :: is mandatory also when scheme is null!
        return format(scheme, identifier, false);
    }

    @Override
    public String[] parse(final String value) {
        String pValue = trim(value);
        String[] splitValue = StringUtils.splitByWholeSeparatorPreserveAllTokens(pValue, OASIS_SMP_SEPARATOR, 2);
        // if only one value is returned set it to identifier
        // else the first element is scheme and second identifier
        String scheme = trim(splitValue.length == 1 ? null : splitValue[0]);
        String identifier = trim(splitValue[splitValue.length == 1 ? 0 : 1]);
        return new String[]{trimToNull(scheme), trimToNull(identifier)};
    }

    public void setDnsLookupFormatType(DNSLookupFormatType dnsLookupFormatType) {
        this.dnsLookupFormatType = dnsLookupFormatType;
    }

    @Override
    public DNSLookupFormatType getDNSFormatType() {
        // default SCHEMA_AFTER_HASH
        return dnsLookupFormatType == null ? DNSLookupFormatType.SCHEMA_AFTER_HASH : dnsLookupFormatType;
    }

    /**
     *  Builder for OasisSMPFormatterType formatter type creates the OasisSMPFormatterType with
     *  OASIS SMP specific parameters:
     *  <ul>
     *      <li><b>Wildcard enabled:</b> true</li>
     *      <li><b>Scheme mandatory:</b> false</li>
     *  </ul>
     */
    public static class Builder extends AbstractFormatterBuilder<OasisSMPFormatterType> {
        DNSLookupFormatType dnsLookupFormatType;
        Pattern matchPattern = null;

        public Builder() {
            // set default values
            wildcardEnabled = true;
            isSchemeMandatory =false;
        }

        public Builder pattern(Pattern matchPattern) {
            this.matchPattern = matchPattern;
            return this;
        }

        public Builder dnsLookupFormatType(DNSLookupFormatType dnsLookupFormatType) {
            this.dnsLookupFormatType = dnsLookupFormatType;
            return this;
        }

        @Override
        public OasisSMPFormatterType build() {
            return new OasisSMPFormatterType(this);
        }
    }
}
