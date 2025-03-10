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

import static org.apache.commons.lang3.StringUtils.*;

/**
 * Simple OASIS SMP party identifier formatter.
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */
public class OasisSMPFormatterType extends AbstractFormatterType {

    DNSLookupFormatType dnsLookupFormatType;

    public OasisSMPFormatterType() {
        setWildcardEnabled(true);
        setSchemeMandatory(false);
    }

    @Override
    public boolean isSchemeValid(final String scheme) {

        return StringUtils.isBlank(scheme) || schemePattern == null || schemePattern.matcher(trim(scheme)).matches();
    }

    @Override
    public boolean isType(final String value) {
        // the value should start with valid scheme
        return true;
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


}
