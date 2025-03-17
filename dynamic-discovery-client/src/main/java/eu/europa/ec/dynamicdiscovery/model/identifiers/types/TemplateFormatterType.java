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
import eu.europa.ec.dynamicdiscovery.exception.MalformedIdentifierException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.replaceEach;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * Configurable formatter for parsing and serializing identifiers.
 * <p>
 * Example for formatTemplate  "${" + SPLIT_GROUP_SCHEME_NAME + "}:${" + SPLIT_GROUP_SCHEME_NAME + "}";
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */
public class TemplateFormatterType  extends AbstractFormatterType {
    private static final Logger LOG = LoggerFactory.getLogger(TemplateFormatterType.class);


    public static final Pattern PARSE_VARIABLE_NAMES = Pattern.compile("\\$\\{(\\w+)}");
    public static final String SPLIT_GROUP_SCHEME_NAME = "scheme";
    public static final String SPLIT_GROUP_IDENTIFIER_NAME = "identifier";
    public static final String SPLIT_GROUP_SCHEME_TAG = "${" + SPLIT_GROUP_SCHEME_NAME + "}";
    public static final String SPLIT_GROUP_IDENTIFIER_TAG = "${" + SPLIT_GROUP_IDENTIFIER_NAME + "}";

    protected static final String[] REPLACE_TAGS = new String[] { SPLIT_GROUP_SCHEME_TAG, SPLIT_GROUP_IDENTIFIER_TAG };

    private final DNSLookupFormatType dnsLookupFormatType;

    private final Pattern splitRegularExpression;
    private final Pattern schemaPattern;
    private final String formatTemplate;
    private final String formatTemplateNullScheme;

    private final String lookupSuffixTemplate;
    private final Pattern lookupSuffixSplitPattern;


    public TemplateFormatterType(Pattern matchSchema, String formatTemplate, Pattern splitRegularExpression) {
        this(matchSchema, formatTemplate, formatTemplate, splitRegularExpression, DNSLookupFormatType.ALL_IN_HASH);
    }

    public TemplateFormatterType(Pattern matchSchema, String formatTemplate, String formatTemplateNullScheme, Pattern splitRegularExpression, DNSLookupFormatType dnsLookupFormatType) {
        this(matchSchema, formatTemplate, formatTemplateNullScheme, splitRegularExpression, dnsLookupFormatType, null, null);
    }

    public TemplateFormatterType(Pattern matchSchema, String formatTemplate, String formatTemplateNullScheme,
                                 Pattern splitRegularExpression,
                                 DNSLookupFormatType dnsLookupFormatType,
                                 String lookupSuffixTemplate,
                                 Pattern lookupSuffixSplitPattern) {
        this.schemaPattern = matchSchema;
        this.formatTemplate = formatTemplate;
        this.formatTemplateNullScheme = formatTemplateNullScheme;
        this.splitRegularExpression = splitRegularExpression;
        this.dnsLookupFormatType = dnsLookupFormatType;
        this.lookupSuffixTemplate = lookupSuffixTemplate;
        this.lookupSuffixSplitPattern = lookupSuffixSplitPattern;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSchemeValid(final String scheme) {
        if (StringUtils.isBlank(scheme)) {
            LOG.debug("TemplateFormatterType does not support identifiers with Null/Blank scheme");
            return false;
        }
        if (schemaPattern == null) {
            LOG.debug("TemplateFormatterType schemaPattern is not defined - all schemes are valid");
            return true;
        }
        Matcher matcher = schemaPattern.matcher(scheme);
        return matcher.matches();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isType(final String value) {
        if (StringUtils.isBlank(value)) {
            LOG.debug("Formatter does not support Null/Blank identifiers ");
            return false;
        }
        if (splitRegularExpression == null) {
            LOG.debug("TemplateFormatterType split regular expression is not defined!");
            return false;
        }
        Matcher matcher = splitRegularExpression.matcher(value);
        return matcher.matches();
    }

    @Override
    public String format(String scheme, String identifier, boolean noDelimiterOnEmptyScheme) {
        return replaceEach(scheme == null ? formatTemplateNullScheme : formatTemplate, REPLACE_TAGS, new String[]{scheme, identifier});

    }

    @Override
    public String format(final String scheme, final String identifier) {
        return format(scheme, identifier, false);
    }

    @Override
    public String[] parse(final String value) {
        String partyIdPrivate = value.trim();
        Matcher matcher = splitRegularExpression.matcher(trim(partyIdPrivate));
        if (!matcher.matches()) {
            throw new MalformedIdentifierException("Identifier: [" + partyIdPrivate + "] does not match regular expression [" + splitRegularExpression.pattern() + "]");
        }
        return new String[]{
                getGroupByName(matcher, SPLIT_GROUP_SCHEME_NAME),
                getGroupByName(matcher, SPLIT_GROUP_IDENTIFIER_NAME)
        };
    }

    private String getGroupByName(Matcher matcher, String groupName) {
        String result = null;
        try {
            result = matcher.group(groupName);
        } catch (IllegalArgumentException arg) {
            LOG.debug("Group [{}] was not found for pattern: [{}].", groupName, matcher.pattern());
        }
        return result;
    }


    @Override
    public DNSLookupFormatType getDNSFormatType() {
        // default ALL_IN_HASH
        return dnsLookupFormatType == null ? DNSLookupFormatType.ALL_IN_HASH : dnsLookupFormatType;
    }


    /**
     *  Returns only the hash value as part of the lookup request
     * @param scheme          scheme part of identifier
     * @param identifier      value part of identifier
     * @param dnsType dns lookup type
     * @return  hash value
     */
    @Override
    public String dnsLookupSuffix(final String scheme, final String identifier, DNSLookupHashType dnsType) {
        if (this.lookupSuffixTemplate == null || this.lookupSuffixSplitPattern == null) {
            return super.dnsLookupSuffix(scheme, identifier, dnsType);
        }
        String formattedValue = format(scheme, identifier);
        Matcher matcher = lookupSuffixSplitPattern.matcher(formattedValue);
        if (!matcher.matches()) {
            throw new MalformedIdentifierException("Identifier: [" + formattedValue + "] does not match regular expression [" + lookupSuffixSplitPattern.pattern() + "]");
        }



        List<String> variableNames = getVariableNames(lookupSuffixTemplate);
        String [] groupValues = variableNames.stream()
                .map(matcher::group)
                .toArray(String[]::new);

        String [] variablePlaceholders = variableNames.stream()
                .map(val -> "${"+val+"}")
                .toArray(String[]::new);
        return replaceEach(lookupSuffixTemplate, variablePlaceholders, groupValues);
    }

    private List<String> getVariableNames(String value){
        if (StringUtils.isBlank(value) && !value.contains("${")){
            return Collections.emptyList();
        }

        Matcher matcher = PARSE_VARIABLE_NAMES.matcher(value);
        List<String> result = new ArrayList<>();
        while (matcher.find()) {
            String variable = matcher.group(1);
            if (!result.contains(variable)){
                result.add(variable);
            }
        }
        return result;
    }
}
