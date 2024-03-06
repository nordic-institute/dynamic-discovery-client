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
