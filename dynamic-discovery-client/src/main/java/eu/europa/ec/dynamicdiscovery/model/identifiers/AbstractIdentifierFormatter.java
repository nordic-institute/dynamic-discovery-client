package eu.europa.ec.dynamicdiscovery.model.identifiers;


import eu.europa.ec.dynamicdiscovery.enums.DNSLookupHashType;
import eu.europa.ec.dynamicdiscovery.exception.MalformedIdentifierException;
import eu.europa.ec.dynamicdiscovery.model.identifiers.types.AbstractFormatterType;
import eu.europa.ec.dynamicdiscovery.model.identifiers.types.FormatterType;
import eu.europa.ec.dynamicdiscovery.model.identifiers.types.OasisSMPFormatterType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.*;

/**
 * Formatter for printing and parsing identifier objects.
 * <p>
 * This class provides parsing and formatting method for identifier objects as:
 * ParticipantIdentifierType, DocumentIdentifier, and ProcessIdentifier.
 * <b>Parsing the identifier</b>
 * Parse method tries to detect the scheme and identifier part of the identifier string using the
 * regular expression and separator sequence.
 * The regular expression allows setting complex parsing templates, while the split separator is much raster.
 *
 * <ul>
 * <li>Using <b>Regular expressing:</b>Regular expression uses named groups &lt;scheme> and &lt;identifier> to identify the scheme and identifier.
 * if the regular expression is null or does not match, the parsing fallback to "split" with separator."
 * </li>
 * <li>Using <b>separator:</b>Separator splits regular expression on the first occurrence of the 'separator' sequence.</li>
 * </ul>
 * If no parsing is successful, then the scheme is set to null, and the identifier part has the input value.
 * In case the schemeMandatory is set to true and the scheme is null, the MalformedIdentifierException is thrown.
 *
 * @author Joze Rihtarsic
 * @since 4.3
 */
public abstract class AbstractIdentifierFormatter<T> {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractIdentifierFormatter.class);

    protected AbstractFormatterType defaultFormatter = new OasisSMPFormatterType();

    protected List<String> caseSensitiveSchemas;
    protected List<FormatterType> formatterTypes = new ArrayList<>();

    protected Integer maxSchemeLength = null;

    /**
     * Formats the object according to formatTemplate. If template is 'blank' the scheme and identifier are concatenated
     * with separator
     *
     * @param value Identifier object to format it to string
     * @return String representation of the identifier
     */
    public String format(T value) {
        return format(getSchemeFromObject(value), getIdentifierFromObject(value));
    }

    /**
     * Method locates formatter based on scheme
     *
     * @param scheme identifier scheme
     * @return FormatterType. If not Formatter is found it returns DEFAULT_FORMATTER
     */
    public FormatterType findFormatter(String scheme, String identifier) {
        LOG.debug("Find formatter for scheme: [{}] and identifier: [{}]", scheme, identifier);
        Optional<FormatterType> optionalFormatterType = formatterTypes.stream().filter(formatterType ->
                formatterType.isType(scheme, identifier)).findFirst();

        return optionalFormatterType.orElse(getDefaultFormatter());
    }

    /**
     * Method locates formatter based on identifier
     *
     * @param identifier concatenated identifier using scheme and  value
     * @return FormatterType. If not Formatter is found it returns DEFAULT_FORMATTER
     */
    public FormatterType findFormatterByIdentifier(String identifier) {
        Optional<FormatterType> optionalFormatterType = formatterTypes.stream().filter(formatterType ->
                formatterType.isType(identifier)).findFirst();

        return optionalFormatterType.orElse(getDefaultFormatter());
    }

    /**
     * Method add formatter to the list of identifier formatter
     *
     * @param formatterTypes formatter of the identifier
     */
    public void addFormatterTypes(FormatterType ... formatterTypes) {
        if (formatterTypes == null || formatterTypes.length == 0)
            return;

        this.formatterTypes.addAll(Arrays.asList(formatterTypes));
    }

    /**
     * Method replaces the list of formatters with the new list
     *
     * @param formatterTypes formatter of the identifier
     */
    public void setFormatterTypes(List<FormatterType> formatterTypes) {
        this.formatterTypes.clear();
        this.formatterTypes.addAll(formatterTypes);
    }

    /**
     * Method returns the list of formatters
     *
     * @return formatterTypes formatter of the identifier
     */
    public List<FormatterType> getFormatterTypes() {
        return this.formatterTypes;
    }

    public void setDefaultFormatter(AbstractFormatterType defaultFormatter) {
        if (defaultFormatter == null) {
            LOG.warn("Can not set null default formatter!");
            return;
        }
        this.defaultFormatter = defaultFormatter;
    }

    /**
     * Formats the object according to formatTemplate. If template is 'blank' the scheme and identifier are concatenated
     * with separator
     *
     * @param scheme     scheme part to format it to string
     * @param identifier Identifier part to format it to string
     * @return String representation of the identifier
     */
    public String format(String scheme, String identifier) {
        // find the formatter
        FormatterType formatter = findFormatter(scheme, identifier);
        return formatter.format(scheme, identifier);
    }

    /**
     * Formats the object according to formatTemplate. If template is 'blank' the scheme and identifier are concatenated
     * with separator
     *
     * @param scheme                   scheme part to format it to string
     * @param identifier               Identifier part to format it to string
     * @param noDelimiterOnBlankScheme if true not delimiter is added when scheme is blank
     * @return String representation of the identifier
     */
    public String format(String scheme, String identifier, boolean noDelimiterOnBlankScheme) {
        FormatterType formatter = findFormatter(scheme, identifier);
        return formatter.format(scheme, identifier, noDelimiterOnBlankScheme);
    }

    /**
     * Formats the object according to formatTemplate. If template is 'blank' the scheme and identifier are concatenated
     * with separator
     *
     * @param identifierObject  the identifier object with scheme and identifier values
     * @param dnsLookupHashType dns lookup type
     * @return String representation of the dns lookup identifier
     */
    public String dnsLookupFormat(T identifierObject, DNSLookupHashType dnsLookupHashType) {
        // find the formatter
        String scheme = getSchemeFromObject(identifierObject);
        String identifier = getIdentifierFromObject(identifierObject);
        return dnsLookupFormat(scheme, identifier, dnsLookupHashType);
    }

    /**
     * Formats the object according to formatTemplate. If template is 'blank' the scheme and identifier are concatenated
     * with separator
     *
     * @param scheme            scheme part to format it to string
     * @param identifier        Identifier part to format it to string
     * @param dnsLookupHashType dns lookup type
     * @return String representation of the dns lookup identifier
     */
    public String dnsLookupFormat(String scheme, String identifier, DNSLookupHashType dnsLookupHashType) {
        // find the formatter
        String nScheme = trim(scheme);
        String nIdentifier = trim(identifier);
        nIdentifier = isCaseInsensitiveSchema(nScheme)? lowerCase(nIdentifier): nIdentifier;
        FormatterType formatter = findFormatter(nScheme, identifier);

        return formatter.dnsLookupFormat(nScheme, nIdentifier, dnsLookupHashType);
    }

    /**
     *  Returns only the hash value as part of the lookup request
     * @param scheme          scheme part of identifier
     * @param value      value part of identifier
     * @param dnsLookupHashType dns lookup type
     * @return  hash value
     */
    public String dnsLookupHash(String scheme, String value, DNSLookupHashType dnsLookupHashType) {
        // find the formatter
        FormatterType formatter = findFormatter(scheme, value);
        return formatter.dnsLookupHash(scheme, value, dnsLookupHashType);
    }


    /**
     * Parse identifier.
     * <p>
     * Method parse the identifier.
     *
     * @param identifier
     * @return
     */
    public T parse(final String identifier) {
        if (isBlank(identifier)) {
            throw new MalformedIdentifierException("Can not parse empty identifier value!");
        }

        String pIdentifier = trim(identifier);

        // find the formatter
        FormatterType formatter = findFormatterByIdentifier(pIdentifier);
        String[] parseResult = formatter.parse(pIdentifier);
        String scheme = parseResult[0];
        String value = parseResult[1];

        formatter.validateScheme(scheme, pIdentifier);
        formatter.validateValue(value, pIdentifier);
        return createObject(scheme, value);
    }


    public AbstractFormatterType getDefaultFormatter() {
        return defaultFormatter;
    }

    /**
     * Method parses the object then it validates if scheme is case-sensitive and lower case the values accordingly.
     *
     * @param value
     * @return
     */
    public T normalizeIdentifier(final String value) {
        T result = parse(value);
        String schema = getSchemeFromObject(result);

        if (isCaseInsensitiveSchema(schema)) {
            String identifier = getIdentifierFromObject(result);
            updateObject(result, lowerCase(schema), lowerCase(identifier));
        }
        return result;
    }


    /**
     * Method normalize the identifier using the format/parse and sets schema and identifier to lower case if
     * identifier is case-insensitive.
     *
     * <ul>
     * <li><b>eDelivery example:</b> scheme [null], party id: [urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088:123456789]</li>
     * <li><b>oasis SMP example:</b> scheme [urn:oasis:names:tc:ebcore:partyid-type:iso6523], party id: [0088:123456789]</li>
     * <li><b>ebCore party ID example:</b>scheme [urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088], party id: [123456789]/li>
     * </ul>
     * <p>
     * Must always result in the same normalized object:
     * scheme [urn:oasis:names:tc:ebcore:partyid-type:iso6523:0088]: party id: [123456789]
     *
     * @param value the identifier object with scheme and identifier values
     * @return the normalized identifier object
     */
    public T normalize(final T value) {
        return normalize(getSchemeFromObject(value), getIdentifierFromObject(value));
    }

    /**
     * Method normalize the identifier using the format/parse and sets schema and identifier to lower case if
     *
     * @param scheme    scheme part of identifier
     * @param identifier identifier part of identifier
     * @return
     */
    public T normalize(String scheme, String identifier) {
        if (StringUtils.isBlank(scheme) && StringUtils.isBlank(identifier)) {
            throw new MalformedIdentifierException("Identifier must not be 'null' or empty");
        }
        return normalizeIdentifier(format(scheme, identifier));
    }

    /**
     * Return true if identifier schema is not defined in list of case-sensitive schemas, else return false.
     *
     * @param schema
     * @return
     */
    public boolean isCaseInsensitiveSchema(String schema) {
        if (StringUtils.isEmpty(schema)) {
            LOG.debug("Empty/null schemas are case insensitive.");
            return true;
        }
        if (caseSensitiveSchemas == null || caseSensitiveSchemas.isEmpty()) {
            LOG.debug("Case sensitive schemas are not configure. return default value [false] for schema's [{}] case sensitive validation!", schema);
            return true;
        }
        return caseSensitiveSchemas.stream().noneMatch(schema::equalsIgnoreCase);
    }

    public String urlEncodedFormat(T value) {
        return urlEncode(format(value));
    }

    public String urlEncode(String s) {
        try {
            String encodedString = URLEncoder.encode(s, UTF_8.name());
            // fix spaces %20 instead of +
            return StringUtils.replace(encodedString, "+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new MalformedIdentifierException("Unsupported UTF-8 Encoding. Please enable UTF-8 encoding!", e);
        }
    }

    protected abstract String getSchemeFromObject(T object);

    protected abstract String getIdentifierFromObject(T object);

    protected abstract T createObject(String scheme, String identifier);

    protected abstract void updateObject(T object, String scheme, String identifier);


    public List<String> getCaseSensitiveSchemas() {
        return caseSensitiveSchemas;
    }

    public AbstractIdentifierFormatter<T> caseSensitiveSchemas(List<String> caseSensitiveSchemas) {
        this.caseSensitiveSchemas = caseSensitiveSchemas;
        return this;
    }

    public void setCaseSensitiveSchemas(List<String> caseSensitiveSchemas) {
        this.caseSensitiveSchemas = caseSensitiveSchemas;
    }

    public boolean isSchemeMandatory() {
        return defaultFormatter.isSchemeMandatory();
    }

    public void setSchemeMandatory(boolean schemeMandatory) {
        this.defaultFormatter.setSchemeMandatory(schemeMandatory);
    }

    public Pattern getSchemeValidationPattern() {
        return defaultFormatter.getSchemeValidationPattern();
    }

    public void setSchemeValidationPattern(Pattern schemeValidationPattern) {
        this.defaultFormatter.setSchemeValidationPattern(schemeValidationPattern);
    }

    public Integer getMaxSchemeLength() {
        return maxSchemeLength;
    }

    public void setMaxSchemeLength(Integer maxSchemeLength) {
        this.maxSchemeLength = maxSchemeLength;
    }
}

