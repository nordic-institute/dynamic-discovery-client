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

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.fetcher.IMetadataFetcher;
import eu.europa.ec.dynamicdiscovery.core.fetcher.SMPParticipantIdentifierLookupResult;
import eu.europa.ec.dynamicdiscovery.core.fetcher.impl.DefaultURLFetcher;
import eu.europa.ec.dynamicdiscovery.core.locator.IMetadataLocator;
import eu.europa.ec.dynamicdiscovery.core.provider.IMetadataProvider;
import eu.europa.ec.dynamicdiscovery.core.provider.impl.DefaultProvider;
import eu.europa.ec.dynamicdiscovery.core.reader.IMetadataReader;
import eu.europa.ec.dynamicdiscovery.core.security.SignatureValidationContext;
import eu.europa.ec.dynamicdiscovery.exception.DDCInvalidData;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.SMPExceptionCode;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.*;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPProcessIdentifier;
import eu.europa.ec.dynamicdiscovery.service.IDynamicDiscoveryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static eu.europa.ec.dynamicdiscovery.core.security.SignatureValidationContext.CertificateValidationStrategy.*;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * Implementation of the Dynamic Discovery Service. This class is responsible for the lookup of the service metadata
 * and the service endpoint. It uses the {@link IMetadataLocator} to find the SMP URI for a given participant identifier,
 * the {@link IMetadataProvider} to resolve the service metadata URI and the {@link IMetadataFetcher} to fetch the metadata.
 * The service metadata is then parsed by the {@link IMetadataReader} to retrieve {@link SMPServiceGroup},
 * {@link SMPServiceMetadata} and {@link SMPEndpoint} .
 *
 * The method lookupEndpoint is used to find the endpoint for a given participant, document and process identifiers and
 * transport profile. If redirection is enabled and the endpoint contains a redirect, the redirection is resolved.
 *
 * @author Flávio W. R. Santos
 * @author Joze Rihtarsic
 * @since 1.0
 */
public class DynamicDiscoveryService implements IDynamicDiscoveryService {
    static final Logger LOG = LoggerFactory.getLogger(DynamicDiscoveryService.class);
    private IMetadataLocator metadataLocator;
    private IMetadataProvider metadataProvider;
    private IMetadataFetcher metadataFetcher;
    private IMetadataReader metadataReader;

    boolean redirectionEnabled = false;
    boolean defaultEndpointForEmptyProcess = false;

    public DynamicDiscoveryService() {
        this.metadataProvider = new DefaultProvider.Builder().build();
        this.metadataFetcher = new DefaultURLFetcher.Builder().build();
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
    public SMPServiceGroup getServiceGroup(SMPParticipantIdentifier participantIdentifier) throws TechnicalException {
        final SMPParticipantIdentifierLookupResult lookupParticipantInSMP = lookupParticipantInSMP(participantIdentifier);
        final SMPServiceGroup serviceGroup = metadataReader.getServiceGroup(lookupParticipantInSMP.getFetcherResponse());
        serviceGroup.setServiceGroupSmpURI(lookupParticipantInSMP.getParticipantUnderSmpURI());
        return serviceGroup;
    }

    @Override
    public SMPServiceMetadata getServiceMetadata(SMPParticipantIdentifier participantIdentifier, SMPDocumentIdentifier documentIdentifier) throws TechnicalException {
        final FetcherResponse fetcherResponseForServiceMetadata = getFetcherResponseForServiceMetadata(participantIdentifier, documentIdentifier);
        return metadataReader.getServiceMetadata(fetcherResponseForServiceMetadata);
    }

    /**
     * Method returns endpoint for given participant, document, process identifiers and transport profile.
     * If redirectionEnabled is set to true and returned Endpoint contains redirect it tris to resolve the redirect as well.
     *
     * @param participantIdentifier participant identifier to discover endpoint
     * @param documentIdentifier the target document identifier (or action identifier for AS4)
     * @param processId process identifier (or service identifier for AS4)
     * @param processIdScheme process identifier scheme
     * @param transportProfile transport profile to define the transport protocol
     * @return endpoint for given parameters or null if no endpoint is found.
     * @throws TechnicalException if any error occurs during the lookup
     */
    @Override
    public SMPEndpoint discoverEndpoint(SMPParticipantIdentifier participantIdentifier, SMPDocumentIdentifier documentIdentifier,
                                        String processId, String processIdScheme, String transportProfile) throws TechnicalException {
        SMPServiceMetadata serviceMetadata = getServiceMetadata(participantIdentifier, documentIdentifier);
        return discoverEndpoint(serviceMetadata, processId, processIdScheme, transportProfile);
    }

    /**
     * Method returns endpoint for given serviceMetadata with process identifiers and transport profile.
     * If redirectionEnabled is set to true and returned Endpoint contains redirect it tris to resolve the redirect as well.
     *
     * @param serviceMetadata serviceMetadata
     * @param processId process identifier (or service identifier for AS4)
     * @param processIdScheme process identifier scheme
     * @param transportProfile transport profile to define the transport protocol
     * @return endpoint for given parameters or null if no endpoint is found.
     * @throws TechnicalException if any error occurs during the lookup
     */
    @Override
    public SMPEndpoint discoverEndpoint(SMPServiceMetadata serviceMetadata,
                                        String processId, String processIdScheme, String transportProfile) throws TechnicalException {
        SMPEndpoint endpoint = getEndpoint(serviceMetadata.getEndpoints(), processId, processIdScheme, transportProfile);
        if (redirectionEnabled && endpoint.getRedirect() != null) {
            LOG.debug("Endpoint has a redirection to URL[{}].", endpoint.getRedirect().getRedirectUrl());
            SignatureValidationContext.Builder svcBuilder = new SignatureValidationContext.Builder();
            Map<String, X509Certificate> redirectCertificateMap = endpoint.getRedirect().getRedirectCertificate();
            List<X509Certificate> listOfTrustedCertificates = redirectCertificateMap.values().stream().collect(Collectors.toList());
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
            endpoint = getEndpoint(serviceMetadata.getEndpoints(), processId, processIdScheme, transportProfile);
        }
        return endpoint;
    }

    private FetcherResponse getFetcherResponseForServiceMetadata(SMPParticipantIdentifier participantIdentifier, SMPDocumentIdentifier documentIdentifier) throws TechnicalException {
        final URI documentURI = getDocumentURI(participantIdentifier, documentIdentifier);
        LOG.info("Fetching service metadata using URI: [{}].", documentURI);
        return metadataFetcher.fetch(documentURI);
    }

    protected URI getDocumentURI(SMPParticipantIdentifier participantIdentifier, SMPDocumentIdentifier documentIdentifier) throws TechnicalException {
        final URI documentIdentifierSmpURI = documentIdentifier.getDocumentIdentifierSmpURI();
        //in case the document identifier was previously discovered from the ServiceGroup, we skip the DNS lookup and reuse the discovered URL
        if (documentIdentifierSmpURI != null) {
            LOG.info("Using service metadata from SMPDocumentIdentifier already discovered");
            return documentIdentifierSmpURI;
        }

        URI smpURI = lookupParticipantSMPUri(participantIdentifier);
        LOG.debug("Got SMP URI: [{}] for participant: [{}].", smpURI, participantIdentifier);
        return metadataProvider.resolveServiceMetadata(smpURI, participantIdentifier, documentIdentifier);
    }

    public SMPParticipantIdentifierLookupResult lookupParticipantInSMP(SMPParticipantIdentifier participantIdentifier) throws TechnicalException {
        URI smpURI = lookupParticipantSMPUri(participantIdentifier);
        URI participantUnderSmpURI = metadataProvider.resolveForParticipantIdentifier(smpURI, participantIdentifier);
        LOG.info("Get participant data / documents for URI: [{}].", participantUnderSmpURI);
        final FetcherResponse fetcherResponse = metadataFetcher.fetch(participantUnderSmpURI);
        return new SMPParticipantIdentifierLookupResult(smpURI, participantUnderSmpURI, fetcherResponse);
    }

    private URI lookupParticipantSMPUri(SMPParticipantIdentifier participantIdentifier) throws TechnicalException {
        URI smpURI = metadataLocator.lookup(participantIdentifier);
        if (smpURI == null) {
            throw new DNSLookupException(SMPExceptionCode.SERVICE_GROUP, "DNS record for participant [" + participantIdentifier + "] can not be resolved!");
        }
        LOG.debug("Got SMP URI: [{}] for participant: [{}].", smpURI, participantIdentifier);
        return smpURI;
    }


    /**
     * Method filters all SMPEndpoints by processId, processIdScheme and transportProfile.
     * If no endpoint is found, Empty collection is returned.
     *
     * @param smpEndpoints  list of all processes
     * @param processId     target process identifier
     * @param processIdScheme  target process identifier scheme
     * @param transportProfile list of targeted transport profiles
     * @return valid endpoint
     * @throws TechnicalException if filter values are null or empty
     */
    private SMPEndpoint getEndpoint(List<SMPEndpoint> smpEndpoints, String processId, String processIdScheme, String transportProfile) throws DDCInvalidData {

        if (StringUtils.isBlank(transportProfile)) {
            throw new DDCInvalidData("Null or empty transport profile");
        }

        if (StringUtils.isBlank(processId)) {
            throw new DDCInvalidData("Null or empty process Id");
        }

        LOG.debug("Search for a Endpoint with process  id: [{}], process scheme [{}] and transportProfile: [{}]]!",
                processId, processIdScheme, transportProfile);
        List<SMPEndpoint> endpoints = smpEndpoints.stream()
                .filter(processType -> smpEndpointMatchesProcessValues(processType, processId, processIdScheme))
                .filter(endpointType -> matchesEndpointTransport(endpointType, transportProfile))
                .collect(Collectors.toList());

        if (endpoints.isEmpty()) {
            LOG.warn("No Endpoints found for process id [{}] with scheme [{}] and transport [{}].",
                    processId, processIdScheme, transportProfile);
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
     * Method returns true if one of endpoint's process  (value and scheme)  matches filter parameters.
     * If the endpoint has no process identifiers the defaultEndpointForEmptyProcess value is returned.
     *
     * @param smpEndpoint
     * @param filterProcessId
     * @param filterProcessIdScheme
     * @return true if endpoint's is valid
     */
    protected boolean smpEndpointMatchesProcessValues(SMPEndpoint smpEndpoint, String filterProcessId, String filterProcessIdScheme) {

        if (hasEmptyProcessList(smpEndpoint)) {
            return defaultEndpointForEmptyProcess;
        }

        Optional<SMPProcessIdentifier> result = smpEndpoint.getProcessIdentifiers().stream().filter(smpProcessIdentifier -> {
            boolean match = StringUtils.equals(smpProcessIdentifier.getIdentifier(), filterProcessId)
                    && StringUtils.equals(smpProcessIdentifier.getScheme(), filterProcessIdScheme);

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
     * @param smpEndpoint
     * @return true if endpoint's process list is empty
     */
    protected boolean hasEmptyProcessList(SMPEndpoint smpEndpoint) {
        return smpEndpoint.getProcessIdentifiers() == null || smpEndpoint.getProcessIdentifiers().isEmpty();
    }

    protected boolean hasNotEmptyProcessList(SMPEndpoint smpEndpoint) {
        return !hasEmptyProcessList(smpEndpoint);
    }

    /**
     * This method exists to be used to filter list of endpointType for particular transportProfile.
     *
     * @param endpointType
     * @param transportProfileValue
     * @return true if endpoint's transport equals to search transport identifier
     */
    protected boolean matchesEndpointTransport(SMPEndpoint endpointType, String transportProfileValue) {
        final SMPTransportProfile transportProfile = endpointType.getTransportProfile();
        if (transportProfile == null) {
            return false;
        }

        boolean isValidTransport = StringUtils.equals(trim(transportProfile.getIdentifier()), trim(transportProfileValue));
        if (!isValidTransport) {
            LOG.debug("Search for endpoint with transport [{}], but found [{}]", transportProfileValue, transportProfile);
        }
        return isValidTransport;
    }


    private SMPServiceMetadata processRedirection(SMPRedirect redirect, SignatureValidationContext context) throws TechnicalException {
        URI redirectURI = URI.create(redirect.getRedirectUrl());
        LOG.info("Fetch document from redirection [{}].", redirectURI);
        final FetcherResponse fetcherResponseForServiceMetadata = metadataFetcher.fetch(redirectURI);

        return metadataReader.getServiceMetadata(fetcherResponseForServiceMetadata, context);
    }

    @Override
    public void setMetadataLocator(IMetadataLocator metadataLocator) {
        this.metadataLocator = metadataLocator;
    }

    @Override
    public void setMetadataProvider(IMetadataProvider metadataProvider) {
        this.metadataProvider = metadataProvider;
    }

    @Override
    public void setMetadataFetcher(IMetadataFetcher metadataFetcher) {
        this.metadataFetcher = metadataFetcher;
    }

    @Override
    public void setMetadataReader(IMetadataReader metadataReader) {
        this.metadataReader = metadataReader;
    }

    @Override
    public IMetadataLocator getMetadataLocator() {
        return metadataLocator;
    }

    @Override
    public IMetadataProvider getMetadataProvider() {
        return metadataProvider;
    }

    @Override
    public IMetadataFetcher getMetadataFetcher() {
        return metadataFetcher;
    }

    @Override
    public IMetadataReader getMetadataReader() {
        return metadataReader;
    }
}
