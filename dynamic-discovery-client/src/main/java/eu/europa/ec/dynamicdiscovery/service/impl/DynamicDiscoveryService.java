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
package eu.europa.ec.dynamicdiscovery.service.impl;

import eu.europa.ec.dynamicdiscovery.core.extension.IExtension;
import eu.europa.ec.dynamicdiscovery.core.extension.impl.oasis10.OasisSMP10Extension;
import eu.europa.ec.dynamicdiscovery.core.extension.impl.oasis20.OasisSMP20Extension;
import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.fetcher.IDocumentFetcher;
import eu.europa.ec.dynamicdiscovery.core.fetcher.impl.DefaultURLFetcher;
import eu.europa.ec.dynamicdiscovery.core.locator.IPublisherLocator;
import eu.europa.ec.dynamicdiscovery.core.locator.PublisherLookupResult;
import eu.europa.ec.dynamicdiscovery.core.provider.IDocumentRequestProvider;
import eu.europa.ec.dynamicdiscovery.core.provider.PublisherRequest;
import eu.europa.ec.dynamicdiscovery.core.provider.WildcardUtil;
import eu.europa.ec.dynamicdiscovery.core.provider.impl.DefaultDocumentRequestProvider;
import eu.europa.ec.dynamicdiscovery.core.reader.IDocumentReader;
import eu.europa.ec.dynamicdiscovery.core.reader.ISMPDocumentReader;
import eu.europa.ec.dynamicdiscovery.core.reader.impl.DefaultBDXRReader;
import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.core.security.SignatureValidationContext;
import eu.europa.ec.dynamicdiscovery.exception.*;
import eu.europa.ec.dynamicdiscovery.model.*;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPProcessIdentifier;
import eu.europa.ec.dynamicdiscovery.service.ISMPDynamicDiscoveryService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.stream.Collectors;

import static eu.europa.ec.dynamicdiscovery.core.security.SignatureValidationContext.CertificateValidationStrategy.*;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * Implementation of the Dynamic Discovery Service. This class is responsible for the lookup of the service metadata
 * and the service endpoint. It uses the {@link IPublisherLocator} to find the SMP URI for a given participant identifier,
 * the {@link IDocumentRequestProvider} to resolve the service metadata URI and the {@link IDocumentFetcher} to fetch the metadata.
 * The service metadata is then parsed by the {@link IDocumentReader} to retrieve {@link SMPServiceGroup},
 * {@link SMPServiceMetadata} and {@link SMPEndpoint} .
 * <p>
 * The method lookupEndpoint is used to find the endpoint for a given participant, document and process identifiers and
 * transport profile. If redirection is enabled and the endpoint contains a redirect, the redirection is resolved.
 *
 * @author Flávio W. R. Santos
 * @author Joze Rihtarsic
 * @since 1.0
 */
public class DynamicDiscoveryService implements ISMPDynamicDiscoveryService {
    static final Logger LOG = LoggerFactory.getLogger(DynamicDiscoveryService.class);
    private final IPublisherLocator publisherLocator;
    private final IDocumentRequestProvider documentRequestProvider;
    private final IDocumentFetcher documentFetcher;
    private final ISMPDocumentReader documentReader;

    WildcardUtil wildcardUtil = new WildcardUtil();
    protected List<String> wildcardSubresourceSchemes = new ArrayList<>();

    final List<IExtension> listExtensions = new ArrayList<>();

    boolean redirectionEnabled = false;
    boolean defaultEndpointForEmptyProcess = false;

    protected DynamicDiscoveryService(DynamicDiscoveryService.Builder builder) {
        this.publisherLocator = builder.publisherLocator;
        this.documentRequestProvider = builder.documentRequestProvider;
        this.documentFetcher = builder.documentFetcher;
        this.documentReader = builder.documentReader;
        this.listExtensions.addAll(builder.listExtensions);
        this.wildcardSubresourceSchemes.addAll(builder.wildcardSubresourceSchemes);
    }

    @Override
    public void setRedirectionEnabled(boolean redirectionEnabled) {
        this.redirectionEnabled = redirectionEnabled;
    }

    @Override
    public void setDefaultEndpointForEmptyProcess(boolean defaultEndpointForEmptyProcess) {
        this.defaultEndpointForEmptyProcess = defaultEndpointForEmptyProcess;
    }

    @Override
    public SMPServiceGroup getResource(SMPParticipantIdentifier participantIdentifier) throws TechnicalException {
        final FetcherResponse fetcherResponse = retrieveResourceForIdentifier(participantIdentifier);
        if (fetcherResponse == null) {
            LOG.debug("No document found for participant [{}].", participantIdentifier);
            return null;
        }
        // if only target extension
        List<IExtension> filteredExtensions = getExtensions().stream().filter(
                        extension -> StringUtils.equals(extension.getExtensionIdentifier(),
                                fetcherResponse.getExtensionIdentifier()))
                .collect(Collectors.toList());

        return documentReader.getResource(fetcherResponse, filteredExtensions);
    }


    @Override
    public SMPServiceMetadata getSubresource(SMPParticipantIdentifier participantIdentifier, SMPDocumentIdentifier documentIdentifier) throws TechnicalException {
        final FetcherResponse fetcherResponse = retrieveSubresourceForIdentifiers(participantIdentifier, documentIdentifier);
        return documentReader.getSubresource(fetcherResponse, getExtensions());
    }

    /**
     * Method retrieves the resource for the (participant) identifier. First it lookup
     * the publisher address for given resource/participant identifier, and then follows
     * the list of registered extensions to and tries to fetch the data for the resource.
     * The method returns first successfully downloaded resource or null.
     *
     * @param identifier the participant identifier
     * @return the first discovered document from the publisher
     * @throws TechnicalException in case of any technical error during the lookup process
     */
    protected FetcherResponse retrieveResourceForIdentifier(SMPParticipantIdentifier identifier) throws TechnicalException {
        // lookup the publisher addresses based on the resource identifier
        List<PublisherLookupResult> lookupResult = lookupPublisherAddresses(identifier);
        LOG.debug("Got lookup results: [{}] for resource/participant: [{}].", lookupResult, identifier);
        // get extensions for the resource
        List<IExtension> filteredExtension = filterExtensions(lookupResult);

        for (IExtension extension : filteredExtension) {
            // get possible Lookup results for the extension
            List<PublisherRequest> resultsForExtension = generatePublisherRequestsForResource(extension, lookupResult, identifier);
            for (PublisherRequest request : resultsForExtension) {
                try {
                    FetcherResponse fetcherResponse = (FetcherResponse) documentFetcher.fetch(request.getResourceUri());
                    if (fetcherResponse != null) {
                        fetcherResponse.setExtensionIdentifier(extension.getExtensionIdentifier());
                        return fetcherResponse;
                    }
                } catch (TechnicalException | DDCRuntimeException e) {
                    LOG.info("Error during fetching the extension for request [{}] and extension [{}] with cause error [{}]",
                            request, extension, ExceptionUtils.getRootCauseMessage(e));
                }
            }
            LOG.debug("No document found for extension [{}]", extension);
        }
        throw new DNSLookupException(DDCExceptionCode.SERVICE_GROUP, "Can not fetch Document for participant [" + identifier + "]!");
    }

    protected FetcherResponse retrieveSubresourceForIdentifiers(SMPParticipantIdentifier resourceIdentifier,
                                                                SMPDocumentIdentifier documentIdentifier) throws TechnicalException {

        // lookup the publisher addresses based on the resource identifier
        List<PublisherLookupResult> lookupResult = lookupPublisherAddresses(resourceIdentifier);
        LOG.info("Got lookup results: [{}] for resource/participant: [{}].", lookupResult, resourceIdentifier);
        List<IExtension> filteredExtension = filterExtensions(lookupResult);

        for (IExtension extension : filteredExtension) {
            // get possible Lookup results for the extension
            List<PublisherRequest> resultsForExtension = generatePublisherRequestsForResource(extension, lookupResult, resourceIdentifier);
            for (PublisherRequest resourceRequest : resultsForExtension) {
                FetcherResponse fetcherResponse = getFetcherResponse(resourceRequest, documentIdentifier, extension);
                if (fetcherResponse != null) {
                    return fetcherResponse;
                }
            }
            LOG.debug("No document found or can be retrieved for the extension [{}]", extension.getExtensionIdentifier());
        }
        throw new DDCFetchException("No document found for resource identifier: [" + resourceIdentifier
                + "] and document identifier: [" + documentIdentifier + "]");
    }

    protected List<IExtension> filterExtensions(List<PublisherLookupResult> lookupResults) throws DNSLookupException {
        List<IExtension> filteredExtension = new ArrayList<>();
        for (IExtension extension : getExtensions()) {
            if (lookupResults.stream().anyMatch(lr -> extension.isLookupServiceSupported(lr.getDnsLookupType(), lr.getServiceType()))) {
                filteredExtension.add(extension);
            }
        }

        if (filteredExtension.isEmpty()) {
            throw new DNSLookupException(DDCExceptionCode.SERVICE_GROUP, "Non of the extensions supports Publisher lookup results (e.g. NAPTR services)!");
        }
        return filteredExtension;
    }

    protected FetcherResponse getFetcherResponse(PublisherRequest resourceRequest, SMPDocumentIdentifier documentIdentifier, IExtension extension) {
        boolean isWildcardScheme = wildcardUtil.isWildcardScheme(getWildcardSubresourceSchemes(), documentIdentifier.getScheme());
        SMPDocumentIdentifier targetDocumentIdentifier = documentIdentifier;
        if (isWildcardScheme) {
            LOG.debug("Wildcard scheme found for document identifier: [{}]", documentIdentifier);
            // match identifier.
            try {
                targetDocumentIdentifier = getDocumentIdentifierWithWildcardMatch(resourceRequest, documentIdentifier, Collections.singletonList(extension));
            } catch (DocumentParseException e) {
                LOG.debug("Can not parse resource document with extension: [{}]  to resolve wildcard identifier: [{}]. Error: [{}]",
                        documentIdentifier, extension.getExtensionIdentifier(), ExceptionUtils.getRootCauseMessage(e));
                return null;
            } catch (Exception e) {
                throw new DDCFetchException("Can not resolve wildcard identifier [" + documentIdentifier
                        + "]! Error retrieving document identifiers from URI: [" + resourceRequest.getResourceIdentifier() + "]");
            }
            // can parse documetns but can not resolve wildcard identifier
            if (targetDocumentIdentifier == null) {
                throw new DDCFetchException("Can not resolve wildcard identifier [" + documentIdentifier
                        + "]! Error retrieving document identifiers from URI: [" + resourceRequest.getResourceIdentifier() + "]");

            }

        }
        PublisherRequest subresourceRequest = documentRequestProvider.createRequestForSubresource(resourceRequest,
                extension.subContextPath(),
                targetDocumentIdentifier);
        try {

            FetcherResponse fetcherResponse = (FetcherResponse) documentFetcher.fetch(subresourceRequest.getSubresourceUri());
            if (fetcherResponse != null) {
                return fetcherResponse;
            }
        } catch (TechnicalException | DDCRuntimeException e) {
            LOG.info("Error during fetching the extension for request [{}] and extension [{}] with cause error [{}]",
                    subresourceRequest, extension, ExceptionUtils.getRootCauseMessage(e));
            // throw error if subresource is not found
            throw new DDCFetchException("Can not fetch document [" + documentIdentifier + "]! Error retrieving document identifiers from URI: ["
                    + subresourceRequest.getSubresourceUri() + "]", e);
        }
        return null;
    }

    /**
     * Method builds resource request and fetches all possible subresource/document Identifiers for the given resource. Then it tries to find
     * the best match for the given documentIdentifier.
     *
     * @param resourceRequest    resource request
     * @param documentIdentifier document identifier to be matched
     * @return the document identifier with the best match
     * @throws DDCFetchException if the document identifiers can not be retrieved from the resource URI.
     */
    protected SMPDocumentIdentifier getDocumentIdentifierWithWildcardMatch(PublisherRequest resourceRequest,
                                                                           SMPDocumentIdentifier documentIdentifier,
                                                                           List<IExtension> extensions) throws TechnicalException {

        URI resourceURI = resourceRequest.getResourceUri();
        LOG.debug("Get resource/participant's  documents for resource URI: [{}].", resourceURI);

        final FetcherResponse fetcherResponse = (FetcherResponse) documentFetcher.fetch(resourceURI);
        final SMPServiceGroup serviceGroup = documentReader.getResource(fetcherResponse, extensions);
        final List<SMPDocumentIdentifier> discoveredDocumentIdentifiers = new ArrayList<>(serviceGroup.getDocumentIdentifiers());
        //the document identifiers supported by the participant
        return getSmpDocumentIdentifierWithWildcardSchemeUsingExactOrLongestMatch(discoveredDocumentIdentifiers, documentIdentifier);
    }

    protected SMPDocumentIdentifier getSmpDocumentIdentifierWithWildcardSchemeUsingExactOrLongestMatch(List<SMPDocumentIdentifier> discoveredDocumentIdentifiers,
                                                                                                       SMPDocumentIdentifier documentIdentifierToCheck) {
        final SMPDocumentIdentifier wildcardDocumentIdentifierWithExactMatch = wildcardUtil.getDocumentIdentifierWithExactCaseInsensitiveMatch(discoveredDocumentIdentifiers, documentIdentifierToCheck);
        if (wildcardDocumentIdentifierWithExactMatch != null) {
            LOG.debug("Found SMPDocumentIdentifier wildcard scheme with exact match [{}] and document identifier [{}].",
                    wildcardDocumentIdentifierWithExactMatch, documentIdentifierToCheck);
            return wildcardDocumentIdentifierWithExactMatch;
        }

        final SMPDocumentIdentifier wildcardDocumentIdentifierWithLongestMatch = wildcardUtil.getWildcardDocumentIdentifierWithLongestMatch(discoveredDocumentIdentifiers, documentIdentifierToCheck);
        if (wildcardDocumentIdentifierWithLongestMatch != null) {
            LOG.debug("Found SMPDocumentIdentifier wildcard scheme with wildcard match [{}] and document identifier [{}].",
                    wildcardDocumentIdentifierWithLongestMatch, documentIdentifierToCheck);
            return wildcardDocumentIdentifierWithLongestMatch;
        }
        return null;
    }


    /**
     * Method generates ordered list of resource requests for the extension. Method follows the order of PublisherLookupResult
     * and generates requests for the extension services.
     *
     * @param extension     extension to generate requests
     * @param lookupResults list of lookup results
     * @return list of generated requests
     */
    protected List<PublisherRequest> generatePublisherRequestsForResource(IExtension extension,
                                                                          List<PublisherLookupResult> lookupResults,
                                                                          SMPParticipantIdentifier participantIdentifier) {
        // generate resource requests for publisher results and extension
        return lookupResults.stream()
                .filter(result -> extension.isLookupServiceSupported(result.getDnsLookupType(), result.getServiceType()))
                .map(result -> documentRequestProvider.createRequestForResource(result, extension.contextPath(), participantIdentifier))
                .collect(Collectors.toList());
    }


    /**
     * @inheritDoc
     */
    @Override
    public SMPEndpoint discoverEndpoint(SMPParticipantIdentifier participantIdentifier, SMPDocumentIdentifier documentIdentifier,
                                        String processId, String processIdScheme, List<String> transportProfiles) throws TechnicalException {
        SMPServiceMetadata serviceMetadata = getSubresource(participantIdentifier, documentIdentifier);
        return discoverEndpoint(serviceMetadata, processId, processIdScheme, transportProfiles);
    }

    /**
     * @inheritDoc
     */
    @Override
    public SMPEndpoint discoverEndpoint(SMPServiceMetadata serviceMetadata,
                                        String processId, String processIdScheme,
                                        List<String> transportProfiles) throws TechnicalException {
        SMPEndpoint endpoint = getEndpoint(serviceMetadata.getEndpoints(), processId, processIdScheme, transportProfiles);
        if (endpoint == null) {
            LOG.debug("No Endpoint found for process id [{}] with scheme [{}] and transport [{}].",
                    processId, processIdScheme, transportProfiles);
            return null;
        }

        if (redirectionEnabled && endpoint.getRedirect() != null) {
            LOG.debug("Endpoint has a redirection to URL [{}].", endpoint.getRedirect().getRedirectUrl());
            SignatureValidationContext.Builder svcBuilder = new SignatureValidationContext.Builder();
            Map<String, X509Certificate> redirectCertificateMap = endpoint.getRedirect().getRedirectCertificate();
            List<X509Certificate> listOfTrustedCertificates = new ArrayList<>(redirectCertificateMap.values());
            if (!listOfTrustedCertificates.isEmpty()) {
                svcBuilder.certificateValidationStrategy(TRUSTED_CERTIFICATES)
                        .trustedCertificates(listOfTrustedCertificates);
            } else if (StringUtils.isNotBlank(endpoint.getRedirect().getCertificateUID())) {
                svcBuilder.certificateValidationStrategy(CERTIFICATE_SUBJECT_VALIDATION_AND_TRUSTSTORE)
                        .certificateUID(endpoint.getRedirect().getCertificateUID());
            } else {
                // signature certificate must be validated against truststore
                svcBuilder.certificateValidationStrategy(TRUSTSTORE);
            }
            serviceMetadata = processRedirection(endpoint.getRedirect(), svcBuilder.build());
            endpoint = getEndpoint(serviceMetadata.getEndpoints(), processId, processIdScheme, transportProfiles);
        }
        return endpoint;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void certificateExists(X509Certificate certificate,
                                  String certificateCode,
                                  SMPParticipantIdentifier participantIdentifier,
                                  SMPDocumentIdentifier documentIdentifier,
                                  SMPProcessIdentifier processIdentifier,
                                  SMPTransportProfile transportProfile) throws TechnicalException {

        final SMPEndpoint endpoint = discoverEndpoint(participantIdentifier, documentIdentifier,
                processIdentifier.getIdentifier(), processIdentifier.getScheme(),
                List.of(transportProfile.getIdentifier()));
        if (endpoint == null) {
            throw new DDCCertificateNotFoundException("No endpoint found for participant [" + participantIdentifier + "], document [" + documentIdentifier + "], process [" + processIdentifier + "] and transport [" + transportProfile + "]");
        }
        // check if the certificate is in the endpoint
        if (endpoint.getCertificates().entrySet().stream().noneMatch(
                entry -> (StringUtils.isBlank(certificateCode) || StringUtils.endsWithIgnoreCase(certificateCode, entry.getKey()))
                        && entry.getValue().equals(certificate))
        ) {
            throw new DDCCertificateNotFoundException("No certificate found for participant [" + participantIdentifier + "], document [" + documentIdentifier + "], process [" + processIdentifier + "] and transport [" + transportProfile + "]");
        }
        // log success
        LOG.info("Certificate match for participant [{}], document [{}], process [{}] and transport [{}].",
                participantIdentifier, documentIdentifier, processIdentifier, transportProfile);
    }


    /**
     * Method uses registered metadataLocator to lookup the participant's SMP addresses.
     * Multiple addresses can be returned since Oasis SMP 2.0 defines new DNS NAPTR records for the OASIS SMP 2.0 endpoint lookup.
     *
     * @param participantIdentifier the participant identifier
     * @return the URI of the participant's SMP
     * @throws TechnicalException               in case of any technical error during the lookup
     * @throws DDCInvalidConfigurationException if DDC is not correctly configured
     * @throws DNSLookupException               if the participant's SMP address can not be resolved
     */
    private List<PublisherLookupResult> lookupPublisherAddresses(SMPParticipantIdentifier participantIdentifier) throws TechnicalException {
        if (publisherLocator == null) {
            throw new DDCInvalidConfigurationException("Missing metadataLocator. The locator is required to lookup the participant's SMP address");
        }
        List<PublisherLookupResult> results = publisherLocator.lookup(participantIdentifier);
        if (results.isEmpty()) {
            throw new DNSLookupException(DDCExceptionCode.SERVICE_GROUP, "The SMP URL value for participant [" + participantIdentifier + "] can not be resolved!");
        }
        //
        LOG.debug("Got SMP address results: [{}] for participant: [{}].", results, participantIdentifier);
        return results;
    }

    /**
     * Method filters all SMPEndpoints by processId, processIdScheme and transportProfile.
     * If no endpoint is found, Empty collection is returned.
     *
     * @param smpEndpoints     list of all processes
     * @param processId        target process identifier
     * @param processIdScheme  target process identifier scheme
     * @param transportProfiles a list of transport profile to match against
     * @return valid endpoint
     * @throws DDCInvalidDataException if filter values are null or empty
     */
    private SMPEndpoint getEndpoint(List<SMPEndpoint> smpEndpoints, String processId,
                                    String processIdScheme, List<String> transportProfiles) throws DDCInvalidDataException {
        if (transportProfiles == null || transportProfiles.isEmpty() || transportProfiles.stream().allMatch(StringUtils::isEmpty)) {
            throw new DDCInvalidDataException("Null or empty transport profile");
        }

        if (StringUtils.isBlank(processId)) {
            throw new DDCInvalidDataException("Null or empty process Id");
        }
        String trimProcessIdScheme = trim(processIdScheme);
        String trimProcessId = trim(processId);
        List<String> trimTransportProfiles = transportProfiles.stream()
                .filter(StringUtils::isNotEmpty) // exclude any empty transport profiles
                .map(StringUtils::trim)
                .collect(Collectors.toList());

        LOG.debug("Search for a Endpoint with process  id: [{}], process scheme [{}] and transportProfiles: [{}]!",
                processId, processIdScheme, transportProfiles);
        List<SMPEndpoint> endpoints = smpEndpoints.stream()
                .filter(processType -> smpEndpointMatchesOrRedirect(processType, trimProcessId, trimProcessIdScheme, trimTransportProfiles))
                .collect(Collectors.toList());

        if (endpoints.isEmpty()) {
            LOG.warn("No Endpoints found for process id [{}] with scheme [{}] and transport [{}].",
                    processId, processIdScheme, transportProfiles);
            return null;
        }

        if (endpoints.size() == 1) {
            return endpoints.get(0);
        }
        // if more than one endpoint is found, return first with defined process identifiers
        // or the first endpoint in the list
        return endpoints.stream().filter(this::hasNotEmptyProcessList)
                .findFirst()
                .orElse(endpoints.get(0));
    }

    /**
     * Method returns validates  endpoint match for given processId, processIdScheme and transportProfiles or
     * if the endpoint is redirection.
     *
     * @param smpEndpoint           endpoint to validate
     * @param filterProcessId       filter process identifier value
     * @param filterProcessIdScheme filter process identifier scheme
     * @param filterTransportIds    filter transport profile values
     * @return true if endpoint matches the filter values or is redirection else false
     */
    protected boolean smpEndpointMatchesOrRedirect(SMPEndpoint smpEndpoint,
                                                   String filterProcessId, String filterProcessIdScheme,
                                                   List<String> filterTransportIds) {
        if (smpEndpointMatchesProcessValues(smpEndpoint, filterProcessId, filterProcessIdScheme)
                && matchesEndpointTransports(smpEndpoint, filterTransportIds)) {
            LOG.debug("Found matching Endpoint with process id: [{}] scheme [{}] and transport profiles [{}]",
                    filterProcessId, filterProcessIdScheme, filterTransportIds);
            return true;
        }

        if (smpEndpoint.getRedirect() != null) {
            LOG.debug("Found redirection Endpoint for process id: [{}] scheme [{}] and transport profiles [{}]",
                    filterProcessId, filterProcessIdScheme, filterTransportIds);
            return true;
        }
        return false;
    }

    /**
     * Method returns true if one of endpoint's process  (value and scheme)  matches filter parameters.
     * If the endpoint has no process identifiers the defaultEndpointForEmptyProcess value is returned.
     *
     * @param smpEndpoint           endpoint to validate
     * @param filterProcessId       target process identifier value
     * @param filterProcessIdScheme target process identifier scheme
     * @return true if endpoint's is matching to the filter parameters
     */
    protected boolean smpEndpointMatchesProcessValues(SMPEndpoint smpEndpoint, String filterProcessId, String filterProcessIdScheme) {
        if (hasEmptyProcessList(smpEndpoint)) {
            return defaultEndpointForEmptyProcess;
        }
        Optional<SMPProcessIdentifier> result = smpEndpoint.getProcessIdentifiers().stream().filter(smpProcessIdentifier -> {
            boolean match = StringUtils.equals(trim(smpProcessIdentifier.getIdentifier()), filterProcessId)
                    && StringUtils.equals(trim(smpProcessIdentifier.getScheme()), filterProcessIdScheme);
            LOG.debug("Search for process id [{}] with scheme [{}], found: [{}] with scheme [{}] which match [{}] to the search parameters!",
                    filterProcessId,
                    filterProcessIdScheme,
                    smpProcessIdentifier.getIdentifier(),
                    smpProcessIdentifier.getScheme(),
                    match);
            return match;
        }).findFirst();

        return result.isPresent();
    }

    /**
     * Method returns true if endpoint has no process identifiers.
     *
     * @param smpEndpoint endpoint to validate
     * @return true if endpoint's process list is empty
     */
    protected boolean hasEmptyProcessList(SMPEndpoint smpEndpoint) {
        return smpEndpoint.getProcessIdentifiers() == null || smpEndpoint.getProcessIdentifiers().isEmpty();
    }

    protected boolean hasNotEmptyProcessList(SMPEndpoint smpEndpoint) {
        return !hasEmptyProcessList(smpEndpoint);
    }

    /**
     * This method exists to be used to filter list of endpointType against any of the particular transportProfile values.
     *
     * @param endpointType          endpoint to validate
     * @param transportProfileValues target transport profile values
     * @return true if endpoint's transport equals to search transport identifier
     */
    protected boolean matchesEndpointTransports(SMPEndpoint endpointType, List<String> transportProfileValues) {
        final SMPTransportProfile transportProfile = endpointType.getTransportProfile();
        if (transportProfile == null) {
            return false;
        }

        boolean isValidTransport = transportProfileValues.stream().anyMatch(
                transportProfileValue -> StringUtils.equals(trim(transportProfile.getIdentifier()), trim(transportProfileValue)));
        if (!isValidTransport) {
            LOG.debug("Search for endpoint with transports [{}], but found [{}]", transportProfileValues, transportProfile);
        }
        return isValidTransport;
    }


    private SMPServiceMetadata processRedirection(SMPRedirect redirect, SignatureValidationContext context) throws TechnicalException {
        URI redirectURI = URI.create(redirect.getRedirectUrl());
        LOG.info("Fetch document from redirection [{}].", redirectURI);
        final FetcherResponse fetcherResponseForServiceMetadata = (FetcherResponse) documentFetcher.fetch(redirectURI);

        return documentReader.getSubresource(fetcherResponseForServiceMetadata, getExtensions(), context);
    }


    @Override
    public IPublisherLocator getPublisherLocator() {
        return publisherLocator;
    }

    @Override
    public IDocumentRequestProvider getDocumentRequestProvider() {
        return documentRequestProvider;
    }

    @Override
    public IDocumentFetcher getDocumentFetcher() {
        return documentFetcher;
    }

    @Override
    public IDocumentReader getDocumentReader() {
        return documentReader;
    }

    public List<IExtension> getExtensions() {
        return listExtensions;
    }

    public List<String> getWildcardSubresourceSchemes() {
        return wildcardSubresourceSchemes;
    }

    public static class Builder {

        private IPublisherLocator publisherLocator;
        private IDocumentRequestProvider documentRequestProvider;
        private IDocumentFetcher documentFetcher;
        private ISMPDocumentReader documentReader;
        protected List<String> wildcardSubresourceSchemes = new ArrayList<>();
        private final List<IExtension> listExtensions = new ArrayList<>();
        private ISignatureValidator signatureValidator;

        public Builder addExtension(IExtension extension) {
            this.listExtensions.add(extension);
            return this;
        }

        public Builder addExtensionsForClassNames(String... extensionClassNames) {
            for (String className : extensionClassNames) {
                try {
                    Class<?> extensionClass = Class.forName(className);
                    IExtension extension = (IExtension) extensionClass.getDeclaredConstructor(new Class[0]).newInstance();
                    this.listExtensions.add(extension);
                } catch (ClassNotFoundException | InstantiationException |
                         IllegalAccessException | NoSuchMethodException |
                         InvocationTargetException e) {
                    throw new DDCInvalidConfigurationException("Extension class [" + className + "] not found or can not be instantiated.", e);
                }
            }

            return this;
        }

        public Builder addExtensions(List<IExtension> extensions) {
            this.listExtensions.addAll(extensions);
            return this;
        }

        public Builder wildcardSubresourceSchemes(String... wildcardSchemes) {
            if (wildcardSchemes != null && wildcardSchemes.length > 0) {
                this.wildcardSubresourceSchemes.addAll(Arrays.asList(wildcardSchemes));
            }
            return this;
        }


        public Builder publisherLocator(IPublisherLocator publisherLocator) {
            this.publisherLocator = publisherLocator;
            return this;
        }

        public Builder documentRequestProvider(IDocumentRequestProvider requestProvider) {
            this.documentRequestProvider = requestProvider;
            return this;
        }

        public Builder signatureValidator(ISignatureValidator signatureValidator) {
            this.signatureValidator = signatureValidator;
            return this;
        }

        public Builder documentFetcher(IDocumentFetcher documentFetcher) {
            this.documentFetcher = documentFetcher;
            return this;
        }

        public Builder documentReader(ISMPDocumentReader documentReader) {
            this.documentReader = documentReader;
            return this;
        }

        public DynamicDiscoveryService build() {
            validate();
            return new DynamicDiscoveryService(this);
        }

        /**
         * Validate the configuration and create default locator, request provider, fetcher and reader if not set.
         */
        private void validate() {
            if (publisherLocator == null) {
                throw new DDCInvalidConfigurationException("publisherLocator is required");
            }
            if (documentFetcher == null) {
                documentFetcher = new DefaultURLFetcher.Builder().build();
            }

            if (documentReader == null) {
                documentReader = new DefaultBDXRReader.Builder()
                        .signatureValidator(signatureValidator)
                        .build();
            }

            if (documentRequestProvider == null) {
                documentRequestProvider = new DefaultDocumentRequestProvider
                        .Builder()
                        .build();
            }

            if (listExtensions.isEmpty()) {
                LOG.info("No extensions are registered. Registering the default extensions OasisSMP10Extension and OasisSMP20Extension");
                listExtensions.add(new OasisSMP10Extension());
                listExtensions.add(new OasisSMP20Extension());
            }
        }
    }
}
