/*
 * (C) Copyright 2016 Dynamic Discovery Client
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
 *
 * @author Flávio W. R. Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 *
 */
package eu.europa.ec.dynamicdiscovery.util;

import com.google.common.io.BaseEncoding;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

public class HashUtil {

    public HashUtil() {
    }

    /**
     * Returns the MD5 hash of the given String
     *
     * @param stringToBeHashed
     * @return the MD5 hash of the given string
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String getMD5Hash(String stringToBeHashed) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return getHash(stringToBeHashed, "MD5", false);
    }

    /**
     * Returns the SHA224 hash of the given String
     *
     * @param stringToBeHashed
     * @return the SHA224 hash of the given string
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String getSHA224Hash(String stringToBeHashed) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return getHash(stringToBeHashed, "SHA224", false);
    }

    /**
     * Returns the SHA256 hash BASE 32 of the given String
     *
     * @param stringToBeHashed
     * @return the SHA256 hash BASE 32 of the given string
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String getSHA256HashBase32(String stringToBeHashed) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return getHash(stringToBeHashed, "SHA256", true);
    }

    /**
     * Returns the hash of the given String
     *
     * @param stringToBeHashed
     * @return the hash of the given string
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    private static String getHash(String stringToBeHashed, String algorithm, boolean isBase32) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        }
        MessageDigest md = MessageDigest.getInstance(algorithm, new org.bouncycastle.jce.provider.BouncyCastleProvider());
        md.reset();
        md.update(stringToBeHashed.getBytes(StandardCharsets.UTF_8));
        byte[] hashBytes = md.digest();

        if (isBase32) {
            //convert the byte to BASE32 - noPadding '='
            BaseEncoding base32
                    = BaseEncoding.base32().omitPadding();
            return base32.encode(hashBytes);
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
            return hexString.toString();
        }
    }
}
