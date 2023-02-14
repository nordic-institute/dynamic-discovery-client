package eu.europa.ec.dynamicdiscovery.enums;

/**
 * Enumeration list of possible DNS lookup format types
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */
public enum DNSLookupFormatType {
    /**
     * The complete participant identifier is in hash
     * hashed(format(<identifier>,<schema>)>
     */
    ALL_IN_HASH,
    /**
     * schema is appended after hashed identifier
     * hashed(<identifier>)>[.<schema>]
     */
    SCHEMA_AFTER_HASH
}
