/*
 * (C) Copyright 2016-2021 - European Commission | Dynamic Discovery Client
 *
 * https://ec.europa.eu/cefdigital/code/projects/EDELIVERY/repos/dynamic-discovery-client/browse
 *
 * Licensed under the LGPL, Version 2.1 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     dynamic-discovery\License_LGPL-2.1.txt or https://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.europa.ec.dynamicdiscovery.util;

import eu.europa.ec.dynamicdiscovery.enums.DNSLookupHashType;
import eu.europa.ec.dynamicdiscovery.exception.DDCRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Base32;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

/**
 * @author Flávio W. R. Santos
 * @author Adrien Ferial
 * @since 1.0
 */
public class HashUtil {
    protected HashUtil() {
    }

    /**
     * Returns the MD5 hash of the given String
     *
     * @param stringToBeHashed
     * @return the MD5 hash of the given string
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String getMD5Hash(String stringToBeHashed) throws NoSuchAlgorithmException {
        return getHash(stringToBeHashed, "MD5", false, false);
    }

    /**
     * Returns the SHA224 hash of the given String
     *
     * @param stringToBeHashed
     * @return the SHA224 hash of the given string
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String getSHA224Hash(String stringToBeHashed) throws NoSuchAlgorithmException {
        return getHash(stringToBeHashed, "SHA224", false, false);
    }

    /**
     * Returns the SHA256 hash BASE 32 of the given String
     *
     * @param stringToBeHashed
     * @return the SHA256 hash BASE 32 of the given string
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String getSHA256HashBase32(String stringToBeHashed) throws NoSuchAlgorithmException {
        return getHash(stringToBeHashed, "SHA256", true, true);
    }

    /**
     * Returns the hash of the given String
     *
     * @param stringToBeHashed input value for hashing
     * @param algorithm        hash algorithm
     * @param isBase32         result encoding. if true it returns base32 encoded else result is  hexadecimal encoded
     * @return the hash of the given string
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    private static String getHash(String stringToBeHashed, String algorithm, boolean isBase32, boolean toUpperCase) throws NoSuchAlgorithmException {

        if (Security.getProvider("BC") == null) {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        }
        MessageDigest md = MessageDigest.getInstance(algorithm, new org.bouncycastle.jce.provider.BouncyCastleProvider());
        md.reset();
        md.update(stringToBeHashed.getBytes(StandardCharsets.UTF_8));
        byte[] hashBytes = md.digest();

        if (isBase32) {
            String base32Value = Base32.toBase32String(hashBytes);
            base32Value = base32Value.replaceAll("=*$", "");
            // remove padding
            return updateStringCase(base32Value, toUpperCase);
        } else {
            //convert the byte to hex format method 2
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return updateStringCase(hexString.toString(), toUpperCase);
        }
    }

    /**
     * Method updates string character case
     *
     * @param value       value to update the case
     * @param toUpperCase if true it returns upper case string, else it returns lower case string.
     * @return updated case string
     */
    public static String updateStringCase(String value, boolean toUpperCase) {
        return toUpperCase ? StringUtils.upperCase(value) : StringUtils.lowerCase(value);
    }

    public static String getDnsDiscoveryHash(String value, DNSLookupHashType dnsType) {
        try {
            return StringUtils.trimToEmpty(dnsType.getPrefix()) + getHash(value, dnsType.getAlgorithm(), dnsType.isBase32(), dnsType.isUpperCase());
        } catch (NoSuchAlgorithmException e) {
            throw new DDCRuntimeException("Hash algorithm implementation [" + dnsType.getAlgorithm() + "] not exist!", e);
        }
    }
}
