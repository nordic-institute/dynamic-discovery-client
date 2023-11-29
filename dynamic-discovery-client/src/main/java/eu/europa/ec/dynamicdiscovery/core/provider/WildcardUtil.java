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
package eu.europa.ec.dynamicdiscovery.core.provider;

import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Cosmin Baciu
 * @since 2.1
 */
public class WildcardUtil {

    private static final Logger LOG = LoggerFactory.getLogger(WildcardUtil.class);

    public static final String WILDCARD_CHARACTER = "*";

    /**
     * Gets the document identifier using a wildcard match(peppol-doctype-wildcard scheme)
     */
    public SMPDocumentIdentifier getWildcardDocumentIdentifierWithLongestMatch(List<SMPDocumentIdentifier> discoveredDocumentIdentifiers, SMPDocumentIdentifier documentIdentifierToCheck) {
        final List<SMPDocumentIdentifier> wildcardDocumentIdentifierCandidates = discoveredDocumentIdentifiers.stream()
                .filter(smpDocumentIdentifier -> {
                            final String discoveredSmpDocumentScheme = smpDocumentIdentifier.getScheme();
                            final String discoveredSmpDocumentIdentifierIdentifier = smpDocumentIdentifier.getIdentifier();
                            if (documentIdentifierToCheck.getScheme().equals(discoveredSmpDocumentScheme)) {
                                final String documentIdentifierWildcardPrefix = getValueUntilWildcardCharacter(discoveredSmpDocumentIdentifierIdentifier);
                                if (StringUtils.containsIgnoreCase(documentIdentifierToCheck.getIdentifier(), documentIdentifierWildcardPrefix)) {
                                    return true;
                                }
                            }
                            return false;
                        }
                ).collect(Collectors.toList());
        if (wildcardDocumentIdentifierCandidates == null || wildcardDocumentIdentifierCandidates.size() == 0) {
            LOG.debug("Not SMP document identifier candidates matching [{}]", documentIdentifierToCheck);
            return null;
        }

        final SMPDocumentIdentifier wildcardDocumentIdentifierWithLongestMatch = getDocumentIdentifierWithWildcardLongestMatch(wildcardDocumentIdentifierCandidates);
        return wildcardDocumentIdentifierWithLongestMatch;
    }

    public String getValueUntilWildcardCharacter(String smpDocumentIdentifierIdentifier) {
        final String documentIdentifierWilcardPrefix = StringUtils.substringBefore(smpDocumentIdentifierIdentifier, WILDCARD_CHARACTER);
        return documentIdentifierWilcardPrefix;
    }

    /**
     * Get the longest document identifier having a wildcard match
     */
    protected SMPDocumentIdentifier getDocumentIdentifierWithWildcardLongestMatch(List<SMPDocumentIdentifier> documentIdentifierCandidates) {
        if (documentIdentifierCandidates == null || documentIdentifierCandidates.size() == 0) {
            return null;
        }

        SMPDocumentIdentifier result = null;

        for (SMPDocumentIdentifier documentIdentifierCandidate : documentIdentifierCandidates) {
            result = getIdentifierWithLongestWildcardValue(result, documentIdentifierCandidate);
        }
        LOG.debug("Found document identifier with the longest identifier match [{}]", result.getIdentifier());
        return result;
    }

    /**
     * Returns the document identifier having the longest identifier value
     */
    protected SMPDocumentIdentifier getIdentifierWithLongestWildcardValue(SMPDocumentIdentifier document, SMPDocumentIdentifier documentCandidate) {
        if (document == null) {
            return documentCandidate;
        }
        if (document.getIdentifier().length() > documentCandidate.getIdentifier().length()) {
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
