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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class provides utility methods for wildcard matching. At the implementation time,
 * the wildcard is not adopted by the eDelivery SMP profile, and it is based on the PEPPOL
 * EDN policy for identifiers. The will-card is implemented as generic implementation so other
 * network profiles can use it. For details please check the:
 * https://docs.peppol.eu/edelivery/policies/PEPPOL-EDN-Policy-for-use-of-identifiers-4.3.0-2024-10-03.pdf
 *
 * @author Cosmin BACIU
 * @since 2.1
 */
public class WildcardUtil {

    private static final Logger LOG = LoggerFactory.getLogger(WildcardUtil.class);
    // The wildcard character
    public static final char WILDCARD_CHARACTER = '*';

    /**
     * The method returns a document identifier from a list of discovered
     * document identifiers that exactly (case-insensitive) matches a given document identifier.
     * If the document identifier is not found, the method returns null.
     * @param discoveredDocumentIdentifiers the list of discovered document identifiers
     * @param documentIdentifierToCheck the target document identifier to validate the match
     * @return the document identifier that exactly (case-insensitive) matches the given document identifier or null if not found
     */
    public SMPDocumentIdentifier getDocumentIdentifierWithExactCaseInsensitiveMatch(
            List<SMPDocumentIdentifier> discoveredDocumentIdentifiers,
            SMPDocumentIdentifier documentIdentifierToCheck) {

        return discoveredDocumentIdentifiers.stream()
                .filter(smpDocumentIdentifier -> StringUtils.equalsIgnoreCase(documentIdentifierToCheck.getScheme(), smpDocumentIdentifier.getScheme())
                        && StringUtils.equalsIgnoreCase(documentIdentifierToCheck.getIdentifier(), smpDocumentIdentifier.getIdentifier())
                ).findFirst()
                .orElse(null);
    }


    /**
     * Gets the document identifier using a wildcard match(peppol-doctype-wildcard scheme) and exact match
     */
    public SMPDocumentIdentifier getWildcardDocumentIdentifierWithExactMatch(List<SMPDocumentIdentifier> discoveredDocumentIdentifiers,
                                                                             SMPDocumentIdentifier documentIdentifierToCheck) {
        return discoveredDocumentIdentifiers.stream()
                .filter(smpDocumentIdentifier -> StringUtils.equalsIgnoreCase(documentIdentifierToCheck.getScheme(), smpDocumentIdentifier.getScheme())
                        && StringUtils.equalsIgnoreCase(documentIdentifierToCheck.getIdentifier(), smpDocumentIdentifier.getIdentifier())
                ).findFirst()
                .orElse(null);
    }

    /**
     * Method filters a list of discovered document identifiers, that have a wildcard
     * match with the given document identifier, and returns the document identifier with the longest match.
     * Example:
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
     *
     * @param discoveredDocumentIdentifiers the list of discovered document identifiers
     * @param documentIdentifierToCheck the target document identifier to validate the wildcard match
     * @return the document identifier with the longest wildcard match or null if not found
     */
    public SMPDocumentIdentifier getWildcardDocumentIdentifierWithLongestMatch(List<SMPDocumentIdentifier> discoveredDocumentIdentifiers,
                                                                               SMPDocumentIdentifier documentIdentifierToCheck) {
        // filter the discovered document identifiers that have wildcard match
        final List<SMPDocumentIdentifier> wildcardDocumentIdentifierCandidates = discoveredDocumentIdentifiers.stream()
                .filter(smpDocumentIdentifier -> {
                            final String discoveredSmpDocumentScheme = smpDocumentIdentifier.getScheme();
                            final String discoveredSmpDocumentIdentifierIdentifier = smpDocumentIdentifier.getIdentifier();
                            // Case-sensitive check for the scheme for the document (See Peppol POLICY 2 Identifier Value casing)
                            if (StringUtils.equals(documentIdentifierToCheck.getScheme(), discoveredSmpDocumentScheme)) {
                                final String documentIdentifierWildcardPrefix = getValueUntilWildcardCharacter(discoveredSmpDocumentIdentifierIdentifier);
                                if (StringUtils.startsWithIgnoreCase(documentIdentifierToCheck.getIdentifier(), documentIdentifierWildcardPrefix)) {
                                    return true;
                                }
                            }
                            return false;
                        }
                ).collect(Collectors.toList());

        if (wildcardDocumentIdentifierCandidates == null || wildcardDocumentIdentifierCandidates.size() == 0) {
            LOG.debug("Not SMP document identifier candidates matching  [{}]", documentIdentifierToCheck);
            return null;
        }

        final SMPDocumentIdentifier wildcardDocumentIdentifierWithLongestMatch = getDocumentIdentifierWithWildcardLongestMatch(wildcardDocumentIdentifierCandidates);
        return wildcardDocumentIdentifierWithLongestMatch;
    }

    /** Method returns the substring before the first occurrence of a separator
     *  (The separator is not returned). If nothing is found, the string input is returned.
     *
     * @param smpDocumentIdentifierIdentifier the string identifier to be processed
     * @return the substring of the given string identifier until the wildcard (*) character or
     * input value (also null or empty string) if no wildcard is found.
     */
    public String getValueUntilWildcardCharacter(String smpDocumentIdentifierIdentifier) {
        final String documentIdentifierwildcardPrefix = StringUtils.substringBefore(smpDocumentIdentifierIdentifier, WILDCARD_CHARACTER);
        return documentIdentifierwildcardPrefix;
    }

    /**
     * Get the longest document identifier having a wildcard match
     *
     * @param documentIdentifierCandidates the list of document identifiers to find the longest wildcard match
     * @return the document identifier with the longest wildcard match or null if not found.
     */
    protected SMPDocumentIdentifier getDocumentIdentifierWithWildcardLongestMatch(List<SMPDocumentIdentifier> documentIdentifierCandidates) {
        if (documentIdentifierCandidates == null || documentIdentifierCandidates.isEmpty()) {
            return null;
        }

        SMPDocumentIdentifier result = null;

        for (SMPDocumentIdentifier documentIdentifierCandidate : documentIdentifierCandidates) {
            result = getIdentifierWithLongestWildcardValue(result, documentIdentifierCandidate);
        }
        if (result != null) {
            LOG.debug("Found document identifier with the longest identifier match [{}]", result.getIdentifier());
        }
        return result;
    }

    /**
     * Compare the length of two document identifiers and return the one with the longest wildcard value.
     * If the first document is null, the second document is returned.
     *
     * @param document the first document identifier
     * @param documentCandidate the second document identifier
     */
    protected SMPDocumentIdentifier getIdentifierWithLongestWildcardValue(SMPDocumentIdentifier document, SMPDocumentIdentifier documentCandidate) {
        if (document == null) {
            return documentCandidate;
        }
        StringUtils.length(document.getIdentifier());
        if (StringUtils.length(document.getIdentifier()) > StringUtils.length(documentCandidate.getIdentifier())) {
            LOG.debug("Document identifier [{}] is longer than [{}]", document.getIdentifier(), documentCandidate.getIdentifier());
            return document;
        }
        //we return the second document, it is not possible to have two equal document identifiers
        return documentCandidate;
    }

    public boolean isWildcardScheme(List<String> wildcardSchemes, String scheme) {
        if (wildcardSchemes != null && wildcardSchemes.contains(scheme)) {
            return true;
        }
        return false;
    }
}
