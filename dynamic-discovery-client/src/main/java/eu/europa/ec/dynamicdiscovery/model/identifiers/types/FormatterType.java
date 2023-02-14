package eu.europa.ec.dynamicdiscovery.model.identifiers.types;


import eu.europa.ec.dynamicdiscovery.enums.DNSLookupFormatType;
import eu.europa.ec.dynamicdiscovery.enums.DNSLookupHashType;
import eu.europa.ec.dynamicdiscovery.exception.DDCRuntimeException;
import eu.europa.ec.dynamicdiscovery.util.HashUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * Formatter type interface for formatting and parsing party identifiers
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */
public interface FormatterType {

    /**
     * Method  returns true if scheme is supported by the formatter for parsing and formatting, else it return false:.
     *
     * @param scheme identifier scheme part
     * @return return true if identifier is supported by the formatter else return false
     */
    boolean isTypeByScheme(final String scheme);

    /**
     * Method  returns true if identifier is supported by the formatter for parsing and formatting, else it return false:.
     *
     * @param value identifier value
     * @return return true if identifier is supported by the formatter else return false
     */
    boolean isType(final String value);

    String format(final String scheme, final String identifier);

    String format(final String scheme, final String identifier, boolean noDelimiterOnEmptyScheme);

    // always returns array size 2 with first element as scheme and second as identifier part.
    String[] parse(final String value);


    DNSLookupFormatType getDNSFormatType();

    default String dnsLookupFormat(final String scheme, final String identifier, DNSLookupHashType dnsType) {
        switch (getDNSFormatType()) {
            case ALL_IN_HASH:
                return dnsLookupFormatAllInHash(scheme, identifier, dnsType);
            case SCHEMA_AFTER_HASH:
                return dnsLookupFormatSchemaAfterHash(scheme, identifier, dnsType);
            default:
                throw new DDCRuntimeException("DNS lookup [" + getDNSFormatType() + "] is not supported!");
        }
    }

    default String dnsLookupFormatAllInHash(final String scheme, final String identifier, DNSLookupHashType dnsType) {
        String hashValue = format(scheme, identifier, true);
        return HashUtil.getDnsDiscoveryHash(hashValue, dnsType);
    }

    default String dnsLookupFormatSchemaAfterHash(final String scheme, final String identifier, DNSLookupHashType dnsType) {
        return HashUtil.getDnsDiscoveryHash(identifier, dnsType) + (StringUtils.isEmpty(scheme) ? "" : "." + scheme);
    }

}

