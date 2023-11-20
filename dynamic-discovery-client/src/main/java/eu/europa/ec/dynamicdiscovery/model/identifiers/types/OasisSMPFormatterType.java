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
    static final String SEPARATOR = "::";

    DNSLookupFormatType dnsLookupFormatType = null;

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
        return (isBlank(scheme) && noDelimiterOnEmptyScheme ? "" : trimToEmpty(scheme) + SEPARATOR) + trimToEmpty(identifier);

    }

    @Override
    public String format(final String scheme, final String identifier) {
        // for OASIS SMP 1.0 the separator :: is mandatory also when scheme is null!
        return format(scheme, identifier, false);
    }

    @Override
    public String[] parse(final String value) {
        String pValue = trim(value);
        String[] splitValue = StringUtils.splitByWholeSeparatorPreserveAllTokens(pValue, SEPARATOR, 2);
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
