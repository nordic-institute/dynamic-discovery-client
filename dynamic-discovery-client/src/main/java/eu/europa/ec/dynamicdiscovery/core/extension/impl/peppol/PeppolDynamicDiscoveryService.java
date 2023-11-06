package eu.europa.ec.dynamicdiscovery.core.extension.impl.peppol;

import eu.europa.ec.dynamicdiscovery.DynamicDiscovery;
import eu.europa.ec.dynamicdiscovery.exception.SMPExceptionCode;
import eu.europa.ec.dynamicdiscovery.exception.SMPServiceMetadataException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceGroup;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Cosmin Baciu
 * @since 2.1
 */
public class PeppolDynamicDiscoveryService {

    private static final Logger LOG = LoggerFactory.getLogger(PeppolDynamicDiscoveryService.class);

    public static final String PEPPOL_DOCTYPE_WILDCARD = "peppol-doctype-wildcard";
    public static final String BUSDOX_DOCID_QNS = "busdox-docid-qns";
    public static final String WILDCARD_CHARACTER = "*";

    protected DynamicDiscovery smpClient;

    public PeppolDynamicDiscoveryService() {
    }

    public PeppolDynamicDiscoveryService(DynamicDiscovery smpClient) {
        this.smpClient = smpClient;
    }


    /**
     * Discovers the SMPServiceMetadata based on the document id value. It takes into account exact and wildcard matches
     *
     * @param participantIdentifier     The participant identifier eg 9925:EDELIVERY_TEST1
     * @param documentIdentifierToCheck The document identifier used for discovering the SMPServiceMetadata eg urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:cen.eu:en16931:2017#compliant#urn:fdc:peppol.eu:2017:poacc:billing:3.0::2.1
     * @return the discovered SMPServiceMetadata or null
     * @throws TechnicalException in case the participant cannot be discovered or no SMPServiceMetadata was found
     */
    public SMPServiceMetadata getServiceMetadata(SMPParticipantIdentifier participantIdentifier, String documentIdentifierToCheck) throws SMPServiceMetadataException {
        SMPServiceGroup serviceGroup = null;
        try {
            serviceGroup = smpClient.getServiceGroup(participantIdentifier);
        } catch (TechnicalException e) {
            throw new SMPServiceMetadataException(SMPExceptionCode.SERVICE_GROUP, "Could not find SMPServiceGroup for participant [" + participantIdentifier + "] and document identifier [" + documentIdentifierToCheck + "]", e);
        }

        //the document identifiers supported by the participant
        final List<SMPDocumentIdentifier> discoveredDocumentIdentifiers = serviceGroup.getDocumentIdentifiers();

        final SMPDocumentIdentifier exactMatchDocumentIdentifier = getExactMatchDocumentIdentifier(discoveredDocumentIdentifiers, documentIdentifierToCheck);
        if (exactMatchDocumentIdentifier != null) {
            LOG.debug("Found SMPDocumentIdentifier exact match [{}] for participant [{}] and document identifier [{}]. Fetching from SMP", participantIdentifier, documentIdentifierToCheck);
            try {
                final SMPServiceMetadata discoveredServiceMetadata = smpClient.getServiceMetadata(participantIdentifier, exactMatchDocumentIdentifier);
                return discoveredServiceMetadata;
            } catch (TechnicalException e) {
                throw new SMPServiceMetadataException(SMPExceptionCode.SERVICE_METADATA, "Could not find SMPServiceMetadata using exact match for participant [" + participantIdentifier + "] and document identifier [" + documentIdentifierToCheck + "]", e);
            }
        }

        final SMPDocumentIdentifier wildcardDocumentIdentifierWithLongestMatch = getWilcardDocumentIdentifierWithLongestMatch(discoveredDocumentIdentifiers, documentIdentifierToCheck);
        if (wildcardDocumentIdentifierWithLongestMatch != null) {
            LOG.debug("Found SMPDocumentIdentifier wildcard match [{}] for participant [{}] and document identifier [{}]. Fetching from SMP", participantIdentifier, documentIdentifierToCheck);
            try {
                final SMPServiceMetadata discoveredServiceMetadata = smpClient.getServiceMetadata(participantIdentifier, wildcardDocumentIdentifierWithLongestMatch);
                return discoveredServiceMetadata;
            } catch (TechnicalException e) {
                throw new SMPServiceMetadataException(SMPExceptionCode.SERVICE_METADATA, "Could not find SMPServiceMetadata using wildcard match for participant [" + participantIdentifier + "] and document identifier [" + documentIdentifierToCheck + "]", e);
            }
        }
        throw new SMPServiceMetadataException(SMPExceptionCode.SERVICE_METADATA, "Could not find SMPServiceMetadata for participant [" + participantIdentifier + "] and document identifier [" + documentIdentifierToCheck + "]");

    }

    /**
     * Gets the document identifier using a wildcard match(busdox-docid-qns scheme)
     */
    protected SMPDocumentIdentifier getExactMatchDocumentIdentifier(List<SMPDocumentIdentifier> discoveredDocumentIdentifiers, String documentIdentifierToCheck) {
        final SMPDocumentIdentifier exactMatchDocumentIdentifier = discoveredDocumentIdentifiers.stream()
                .filter(smpDocumentIdentifier -> {
                            final String smpDocumentScheme = smpDocumentIdentifier.getScheme();
                            final String smpDocumentIdentifierIdentifier = smpDocumentIdentifier.getIdentifier();
                            if (BUSDOX_DOCID_QNS.equals(smpDocumentScheme)) {
                                //exact match; the SMP document identifier must contain document identifier
                                if (StringUtils.containsIgnoreCase(smpDocumentIdentifierIdentifier, documentIdentifierToCheck)) {
                                    return true;
                                }
                            }
                            return false;
                        }
                ).findFirst()
                .orElse(null);
        return exactMatchDocumentIdentifier;
    }

    /**
     * Gets the document identifier using a wildcard match(peppol-doctype-wildcard scheme)
     */
    protected SMPDocumentIdentifier getWilcardDocumentIdentifierWithLongestMatch(List<SMPDocumentIdentifier> discoveredDocumentIdentifiers, String documentIdentifierToCheck) {
        final List<SMPDocumentIdentifier> wildcardDocumentIdentifierCandidates = discoveredDocumentIdentifiers.stream()
                .filter(smpDocumentIdentifier -> {
                            final String smpDocumentScheme = smpDocumentIdentifier.getScheme();
                            final String smpDocumentIdentifierIdentifier = smpDocumentIdentifier.getIdentifier();
                            if (PEPPOL_DOCTYPE_WILDCARD.equals(smpDocumentScheme)) {
                                final String documentIdentifierWilcardPrefix = getValueUntilWildcardCharacter(smpDocumentIdentifierIdentifier);
                                if (documentIdentifierToCheck.contains(documentIdentifierWilcardPrefix)) {
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

    protected String getValueUntilWildcardCharacter(String smpDocumentIdentifierIdentifier) {
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


    public DynamicDiscovery getSmpClient() {
        return smpClient;
    }

    public void setSmpClient(DynamicDiscovery smpClient) {
        this.smpClient = smpClient;
    }
}
