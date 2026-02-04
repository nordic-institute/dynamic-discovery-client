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
package eu.europa.ec.dynamicdiscovery.core.provider;

import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.platform.commons.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * This class provides utility methods for wildcard matching. At the implementation time,
 * the wildcard is not adopted by the eDelivery SMP profile, and it is based on the PEPPOL
 * EDN policy for identifiers. The will-card is implemented as generic implementation so other
 * network profiles can use it. For details please check the:
 * <a href="https://docs.peppol.eu/edelivery/policies/PEPPOL-EDN-Policy-for-use-of-identifiers-4.3.0-2024-10-03.pdf">PEPPOL-EDN-Policy-for-use-of-identifiers-4.3.0-2024-10-03.pdf</a>
 *
 * @author Joze RIHTARSIC
 * @since 3.0
 */
class WildcardUtilTest {
    WildcardUtil testInstance = new WildcardUtil();

    @ParameterizedTest
    @CsvSource({
            "'scheme1::a', 'scheme1::A', 'scheme1::a'",
            "'scheme1::a', 'scheme1::b', ''",
            "'scheme1::a;scheme1::b', 'scheme1::B', 'scheme1::b'",
            "'scheme1::a;scheme2::a', 'scheme2::A', 'scheme2::a'",
            "'scheme1::a;scheme2::b', 'scheme3::b', ''"
    })
    void testGetDocumentIdentifierWithExactCaseInsensitiveMatch(String registeredIdentifiers,
                                                                String searchIdentifier,
                                                                String expectedStr) {
        List<SMPDocumentIdentifier> discoveredDocumentIdentifiers = parseIdentifiers(registeredIdentifiers);
        SMPDocumentIdentifier documentIdentifierToCheck = parseIdentifier(searchIdentifier);
        SMPDocumentIdentifier expected = parseIdentifier(expectedStr);

        SMPDocumentIdentifier result = testInstance.getDocumentIdentifierWithExactCaseInsensitiveMatch(discoveredDocumentIdentifiers, documentIdentifierToCheck);
        assertEquals(expected, result);
    }

    /*
     * Test following  examples:
     * <ul>
     *   <li>SMP registration a*</li>
     *   <ul>
     *     <li>Matches e.g. a, a@b or a@b@c@d</li>
     *     <li>Does not match e.g. b, b@a or b@a@c</li>
     *   </ul>
     *   <li>SMP registration a@b*</li>
     *   <ul>
     *     <li>Matches e.g. a@b, a@b@c, a@b@c@d</li>
     *     <li>Does not match e.g. a, a@c, b@a, or c@a@b</li>
     *   </ul>
     *   <li>SMP has a registration for a* and a@b*</li>
     *   <ul>
     *     <li>Senders wanting to send a@b@c must choose the SMP endpoint offered by a@b*</li>
     *     <li>Senders wanting to send a@b must choose the SMP endpoint offered by a@b*</li>
     *     <li>Senders wanting to send a@c must choose the SMP endpoint offered by a*</li>
     *     <li>Senders wanting to send a must choose the SMP endpoint offered by a*</li>
     *     <li>Senders wanting to send b@c will not find a matching SMP endpoint</li>
     *   </ul>
     * </ul>
     */
    @ParameterizedTest
    @CsvSource({
            "'scheme1::a*', 'scheme1::a@b', 'scheme1::a*'",
            "'scheme1::a*', 'scheme1::a@b@c', 'scheme1::a*'",
            "'scheme1::a', 'scheme1::b@a',''",
            "'scheme1::a*', 'scheme1::b@a@c',''",
            "'scheme1::a*;scheme1::a@b*', 'scheme1::a@b@c', 'scheme1::a@b*'",
            "'scheme1::a@b*;scheme1::a*', 'scheme1::a@b@c', 'scheme1::a@b*'",
            "'scheme1::a*;scheme1::a@b*', 'scheme1::a@b', 'scheme1::a@b*'",
            "'scheme1::a*;scheme1::a@b*', 'scheme1::a@c', 'scheme1::a*'",
            "'scheme1::a*;scheme1::a@b*', 'scheme1::b@c', ''",
    })
    void testGetWildcardDocumentIdentifierWithLongestMatch(String registeredIdentifiers,
                                                           String searchIdentifier,
                                                           String expectedStr) {
        List<SMPDocumentIdentifier> discoveredDocumentIdentifiers = parseIdentifiers(registeredIdentifiers);
        SMPDocumentIdentifier documentIdentifierToCheck = parseIdentifier(searchIdentifier);
        SMPDocumentIdentifier expected = parseIdentifier(expectedStr);

        SMPDocumentIdentifier result = testInstance.getWildcardDocumentIdentifierWithLongestMatch(discoveredDocumentIdentifiers, documentIdentifierToCheck);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @CsvSource({
            "'scheme1,scheme2,scheme3', 'scheme1', true",
            "'scheme1,scheme2,scheme3', 'scheme4', false",
            "'scheme1,scheme2,scheme3', 'scheme2', true",
            "'', 'scheme1', false",
            "'scheme1', '', false"
    })
    void testIsWildcardScheme(String wildcardSchemesStr, String scheme, boolean expected) {
        List<String> wildcardSchemes = wildcardSchemesStr.isEmpty() ? Collections.emptyList() : Arrays.asList(wildcardSchemesStr.split(","));
        boolean result = testInstance.isWildcardScheme(wildcardSchemes, scheme);
        assertEquals(expected, result);

    }

    private List<SMPDocumentIdentifier> parseIdentifiers(String identifiersStr) {
        return Arrays.stream(identifiersStr.split(";"))
                .map(this::parseIdentifier)
                .collect(Collectors.toList());
    }

    private SMPDocumentIdentifier parseIdentifier(String identifierStr) {
        if (StringUtils.isBlank(identifierStr)) {
            return null;
        }
        String[] parts = identifierStr.split("::");
        return new SMPDocumentIdentifier(parts[1], parts[0]);
    }
}
