package eu.europa.ec.dynamicdiscovery.enums;

/**
 * Enumeration list of possible DNS lookup hash types
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */
public enum DNSLookupHashType {
    /**
     * The complete participant identifier is in hash
     * hashed(format(<identifier>,<schema>)>
     */
    MD5_HEX("B-", "MD5", false, false),
    /**
     * schema is appended after hashed identifier
     * hashed(<identifier>)>[.<schema>]
     */
    SHA256_BASE32("", "SHA256", true, true);


    private final String algorithm;
    private final boolean isBase32;
    private final String prefix;
    private final boolean upperCase;


    DNSLookupHashType(String prefix, String algorithm, boolean isBase32, boolean upperCase) {
        this.algorithm = algorithm;
        this.isBase32 = isBase32;
        this.prefix = prefix;
        this.upperCase = upperCase;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public boolean isBase32() {
        return isBase32;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean isUpperCase() {
        return upperCase;
    }
}
