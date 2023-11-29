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
