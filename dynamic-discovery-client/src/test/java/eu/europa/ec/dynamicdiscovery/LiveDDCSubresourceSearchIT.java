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
package eu.europa.ec.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.core.extension.impl.peppol.PeppolSMPExtension;
import eu.europa.ec.dynamicdiscovery.core.fetcher.impl.DefaultURLFetcher;
import eu.europa.ec.dynamicdiscovery.core.locator.impl.DefaultBDXRLocator;
import eu.europa.ec.dynamicdiscovery.core.provider.WildcardUtil;
import eu.europa.ec.dynamicdiscovery.core.provider.impl.DefaultDocumentRequestProvider;
import eu.europa.ec.dynamicdiscovery.core.reader.impl.DefaultBDXRReader;
import eu.europa.ec.dynamicdiscovery.core.security.impl.DefaultSignatureValidator;
import eu.europa.ec.dynamicdiscovery.enums.DNSLookupType;
import eu.europa.ec.dynamicdiscovery.exception.*;
import eu.europa.ec.dynamicdiscovery.model.SMPEndpoint;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceGroup;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import eu.europa.ec.dynamicdiscovery.model.SMPTransportProfile;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPProcessIdentifier;
import eu.europa.ec.dynamicdiscovery.service.impl.DynamicDiscoveryService;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import gen.eu.europa.ec.ddc.api.peppol.SignedServiceMetadata;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static eu.europa.ec.dynamicdiscovery.util.TestCaseConstants.BUSDOX_DOCID_QNS;
import static eu.europa.ec.dynamicdiscovery.util.TestCaseConstants.PEPPOL_DOCTYPE_WILDCARD;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Purpose of this test is to perform live tests against the SMP service. The tests use
 * dns domain: acc.edelivery.tech.ec.europa.eu with NAPTR and CNAME DNS lookup types.
 * The test is designed to lookup  Peppol SMP service metadata for a given participant identifier.
 * <ul<
 * <li>iso6523-actorid-upis::9925:EDELIVERY_TEST1</li>
 * <li>iso6523-actorid-upis::9901:pint_c4_jp_sb</li>
 * <li>iso6523-actorid-upis::9901:eDeliveryNotRegisteredParticipant</li>
 * <li>iso6523-actorid-upis::9925:EDELIVERY_TEST3</li>
 * </ul>
 * Make sure participants exists in live SMP service with the given document identifier.
 *
 * @author Cosmin Baciu
 * @since 2.1
 */
@EnabledIfSystemProperty(named = "live.tests", matches = "true")
public class LiveDDCSubresourceSearchIT {

    static final Logger LOG = LoggerFactory.getLogger(LiveDDCSubresourceSearchIT.class);
    private static final String EDELIVERY_TECH_EC_EUROPA_EU = "acc.edelivery.tech.ec.europa.eu";

    @Test
    void getAllDocumentIdentifiersForParticipantHavingInvoiceCapability() throws Exception {
        final String toCheckParticipantIdentifierValue = "9925:EDELIVERY_TEST1";
        final String toCheckParticipantIdentifierScheme = "iso6523-actorid-upis";

        List<SMPDocumentIdentifier> toCheckDocumentIdentifierCapabilities = new ArrayList<>();

        toCheckDocumentIdentifierCapabilities.add(new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:cen.eu:en16931:2017#compliant#urn:fdc:peppol.eu:2017:poacc:billing:3.0::2.1",
                BUSDOX_DOCID_QNS));
        toCheckDocumentIdentifierCapabilities.add(new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:cen.eu:en16931:2017#compliant#urn:fdc:peppol.eu:2017:poacc:billing:3.0::2.1",
                BUSDOX_DOCID_QNS));
        toCheckDocumentIdentifierCapabilities.add(new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:ApplicationResponse-2::ApplicationResponse##urn:fdc:peppol.eu:poacc:trns:invoice_response:3::2.1",
                BUSDOX_DOCID_QNS));
        toCheckDocumentIdentifierCapabilities.add(new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:ApplicationResponse-2::ApplicationResponse##urn:fdc:peppol.eu:poacc:trns:mlr:3::2.1",
                BUSDOX_DOCID_QNS));
        toCheckDocumentIdentifierCapabilities.add(new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:Order-2::Order##urn:fdc:peppol.eu:poacc:trns:order:3::2.1",
                BUSDOX_DOCID_QNS));

        getDocumentIdentifierAndAssert(toCheckParticipantIdentifierValue,
                toCheckParticipantIdentifierScheme,
                5,
                toCheckDocumentIdentifierCapabilities);
    }

    @Test
    void getAllDocumentIdentifiersForParticipantHavingInvoiceWildcardCapability() throws Exception {
        final String toCheckParticipantIdentifierValue = "9901:pint_c4_jp_sb";
        final String toCheckParticipantIdentifierScheme = "iso6523-actorid-upis";

        List<SMPDocumentIdentifier> toCheckDocumentIdentifierCapabilities = new ArrayList<>();

        toCheckDocumentIdentifierCapabilities.add(new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:peppol:pint:billing-3.0@jp:peppol-1*::2.1",
                PEPPOL_DOCTYPE_WILDCARD));
        toCheckDocumentIdentifierCapabilities.add(new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:peppol:pint:billing-3.0*::2.1",
                PEPPOL_DOCTYPE_WILDCARD));
        toCheckDocumentIdentifierCapabilities.add(new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:peppol:pint:billing-3.0@jp:peppol-1*::2.1",
                PEPPOL_DOCTYPE_WILDCARD));
        toCheckDocumentIdentifierCapabilities.add(new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:peppol:pint:billing-3.0*::2.1",
                PEPPOL_DOCTYPE_WILDCARD));

        getDocumentIdentifierAndAssert(toCheckParticipantIdentifierValue,
                toCheckParticipantIdentifierScheme,
                4,
                toCheckDocumentIdentifierCapabilities);
    }

    private void getDocumentIdentifierAndAssert(String toCheckParticipantIdentifierValue,
                                                String toCheckParticipantIdentifierScheme,
                                                int expectedDocumentIdentifiers,
                                                List<SMPDocumentIdentifier> toCheckDocumentIdentifierCapabilities) throws Exception {
        SMPParticipantIdentifier toCheckParticipantIdentifier = new SMPParticipantIdentifier(toCheckParticipantIdentifierValue, toCheckParticipantIdentifierScheme);
        final DynamicDiscoveryService smpClient = createClient();

        //discover the participant document identifiers
        SMPServiceGroup serviceGroup = smpClient.getResource(toCheckParticipantIdentifier);
        List<SMPDocumentIdentifier> discoveredDocumentIdentifiers = serviceGroup != null ? serviceGroup.getDocumentIdentifiers() : Collections.emptyList();
        assertEquals(expectedDocumentIdentifiers, discoveredDocumentIdentifiers.size());


        toCheckDocumentIdentifierCapabilities.forEach(smpDocumentIdentifier -> {
            try {
                getAndAssertSMPDocumentIdentifier(
                        smpDocumentIdentifier,
                        discoveredDocumentIdentifiers,
                        smpClient,
                        toCheckParticipantIdentifier);
            } catch (TechnicalException e) {
                LOG.error("Error asserting document identifier [{}]", smpDocumentIdentifier, e);
                throw new RuntimeException(e);
            }
        });
    }

    private DynamicDiscoveryService createClient() throws Exception {
        DefaultURLFetcher urlFetcher = new DefaultURLFetcher.Builder().build();

        final KeyStore trustStore = CommonUtil.loadTrustStore("truststore/peppol-truststore.jks");
        final DefaultSignatureValidator defaultSignatureValidator = new DefaultSignatureValidator(trustStore);
        final DefaultBDXRReader bdxReader = new DefaultBDXRReader.Builder()
                .signatureValidator(defaultSignatureValidator)
                .build();

        final DefaultBDXRLocator defaultBDXRLocator = new DefaultBDXRLocator.Builder()
                .addDnsLookupType(DNSLookupType.NAPTR)
                .addDnsLookupType(DNSLookupType.CNAME)
                .addTopDnsDomain(EDELIVERY_TECH_EC_EUROPA_EU)
                .build();
        final DefaultDocumentRequestProvider defaultProvider = new DefaultDocumentRequestProvider.Builder()
                .build();

        //create the smp client
        return new DynamicDiscoveryService.Builder()
                .publisherLocator(defaultBDXRLocator)
                .addExtension(new PeppolSMPExtension())
                .wildcardSubresourceSchemes(PEPPOL_DOCTYPE_WILDCARD)
                .documentReader(bdxReader)
                .documentFetcher(urlFetcher)
                .documentRequestProvider(defaultProvider)
                .build();
    }

    private void getAndAssertSMPDocumentIdentifier(SMPDocumentIdentifier toCheckDocumentIdentifier,
                                                   List<SMPDocumentIdentifier> discoveredDocumentIdentifiers,
                                                   DynamicDiscoveryService smpClient,
                                                   SMPParticipantIdentifier toCheckParticipantIdentifier) throws TechnicalException {
        //get the document identifier to check from the list of already discovered document identifiers
        final SMPDocumentIdentifier discoveredDocumentIdentifier = discoveredDocumentIdentifiers.stream()
                .filter(smpDocumentIdentifier -> smpDocumentIdentifier.getIdentifier().equals(toCheckDocumentIdentifier.getIdentifier())
                        && smpDocumentIdentifier.getScheme().equals(toCheckDocumentIdentifier.getScheme()))
                .findFirst()
                .orElse(null);
        assertNotNull(discoveredDocumentIdentifier);

        //get the service metadata from SMP for the document identifier
        final SMPServiceMetadata discoveredServiceMetadata = smpClient.getSubresource(toCheckParticipantIdentifier, discoveredDocumentIdentifier);

        //assertions
        assertNotNull(discoveredServiceMetadata);

        //check participant
        final SMPParticipantIdentifier discoveredSmpParticipantIdentifier = discoveredServiceMetadata.getParticipantIdentifier();
        assertNotNull(toCheckParticipantIdentifier);

        assertTrue(StringUtils.equalsIgnoreCase(toCheckParticipantIdentifier.getIdentifier(), discoveredSmpParticipantIdentifier.getIdentifier()));
        assertEquals(toCheckParticipantIdentifier.getScheme(), discoveredSmpParticipantIdentifier.getScheme());

        //check discovered document identifier
        final SMPDocumentIdentifier documentIdentifier = discoveredServiceMetadata.getDocumentIdentifier();
        assertNotNull(documentIdentifier);
        assertEquals(discoveredDocumentIdentifier.getIdentifier(), documentIdentifier.getIdentifier());
        assertEquals(discoveredDocumentIdentifier.getScheme(), documentIdentifier.getScheme());
        assertNotNull(discoveredServiceMetadata.getSignerCertificate());
        final SignedServiceMetadata signedServiceMetadata = discoveredServiceMetadata.unwrap(SignedServiceMetadata.class);
        assertNotNull(signedServiceMetadata);
        assertNotNull(signedServiceMetadata.getServiceMetadata());
        assertNotNull(signedServiceMetadata.getSignature());

        //check endpoints
        final List<SMPEndpoint> endpoints = discoveredServiceMetadata.getEndpoints();
        assertNotNull(endpoints);
        assertEquals(1, endpoints.size());
        final SMPEndpoint smpEndpoint = endpoints.get(0);
        assertNotNull(smpEndpoint.getCertificate());

        assertNotNull(smpEndpoint.getTechnicalContactUrl());
        assertNotNull(smpEndpoint.getServiceDescription());

        //check transport profile
        final SMPTransportProfile transportProfile = smpEndpoint.getTransportProfile();

        //check process identifiers
        final List<SMPProcessIdentifier> processIdentifiers = smpEndpoint.getProcessIdentifiers();
        assertNotNull(processIdentifiers);
        assertEquals(1, processIdentifiers.size());
        final SMPProcessIdentifier smpProcessIdentifier = processIdentifiers.get(0);
        assertNotNull(smpProcessIdentifier.getIdentifier());
        assertNotNull(smpProcessIdentifier.getScheme());

        //check that getEndpoint method is returning correctly the SMPEndpoint instance
        final SMPEndpoint endpoint = discoveredServiceMetadata.getEndpoint(smpProcessIdentifier, transportProfile);
        assertNotNull(endpoint);
        assertEquals(smpEndpoint, endpoint);
    }


    @Test
    void getDocumentIdentifierForBusdoxSchemeNotSupportedByTheParticipant() throws Exception {
        final String toCheckParticipantIdentifierValue = "9925:EDELIVERY_TEST1";
        final String toCheckParticipantIdentifierScheme = "iso6523-actorid-upis";
        SMPParticipantIdentifier toCheckParticipantIdentifier = new SMPParticipantIdentifier(toCheckParticipantIdentifierValue, toCheckParticipantIdentifierScheme);

        //urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:peppol:pint:billing-3.0@jp:peppol-1*::2.1
        //urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:peppol:pint:billing-3.0*::2.1

        final DynamicDiscoveryService smpClient = createClient();
        //get the service metadata from SMP for using document identifier which is not supported by the participant
        final SMPDocumentIdentifier documentIdentifierNotRegisteredForParticipantBusdox = new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##notSupported",
                BUSDOX_DOCID_QNS);

        DDCFetchException result = assertThrows(DDCFetchException.class,
                () -> smpClient.getSubresource(toCheckParticipantIdentifier, documentIdentifierNotRegisteredForParticipantBusdox));
        assertThat(result.getMessage(), containsString("Can not fetch document [" + documentIdentifierNotRegisteredForParticipantBusdox+"]"));
    }

    @Test
    void getDocumentIdentifierForBusdoxSchemeSupportedByTheParticipant() throws Exception {
        final String toCheckParticipantIdentifierValue = "9925:EDELIVERY_TEST1";
        final String toCheckParticipantIdentifierScheme = "iso6523-actorid-upis";
        SMPParticipantIdentifier toCheckParticipantIdentifier = new SMPParticipantIdentifier(toCheckParticipantIdentifierValue, toCheckParticipantIdentifierScheme);

        //urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:peppol:pint:billing-3.0@jp:peppol-1*::2.1
        //urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:peppol:pint:billing-3.0*::2.1

        final DynamicDiscoveryService smpClient = createClient();
        //get the service metadata from SMP for using document identifier which is not supported by the participant
        final SMPDocumentIdentifier documentIdentifierNotRegisteredForParticipantBusdox = new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:cen.eu:en16931:2017#compliant#urn:fdc:peppol.eu:2017:poacc:billing:3.0::2.1",
                BUSDOX_DOCID_QNS);

        final SMPServiceMetadata discoveredServiceMetadata = smpClient.getSubresource(toCheckParticipantIdentifier, documentIdentifierNotRegisteredForParticipantBusdox);
        assertNotNull(discoveredServiceMetadata);
    }

    @Test
    void getServiceMetadataBasedOnProvidedDocumentWithWildcardMatch() throws Exception {
        final String toCheckParticipantIdentifierValue = "9901:pint_c4_jp_sb";
        final String toCheckParticipantIdentifierScheme = "iso6523-actorid-upis";
        SMPParticipantIdentifier toCheckParticipantIdentifier = new SMPParticipantIdentifier(toCheckParticipantIdentifierValue, toCheckParticipantIdentifierScheme);

        //Participant registered documents in SMP
        //urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:peppol:pint:billing-3.0@jp:peppol-1*::2.1
        //urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:peppol:pint:billing-3.0*::2.1

        final DynamicDiscoveryService smpClient = createClient();

        //get the service metadata from SMP for using document identifier
        final SMPDocumentIdentifier toCheckDocumentIdentifier = new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:peppol:pint:billing-3.0@jp:peppol-1",
                PEPPOL_DOCTYPE_WILDCARD);

        final SMPServiceMetadata discoveredServiceMetadata = smpClient.getSubresource(toCheckParticipantIdentifier, toCheckDocumentIdentifier);
        assertNotNull(discoveredServiceMetadata);
        assertTrue(StringUtils.containsIgnoreCase(toCheckDocumentIdentifier.getIdentifier(), new WildcardUtil().getValueUntilWildcardCharacter(discoveredServiceMetadata.getDocumentIdentifier().getIdentifier())));
    }

    @Test
    void getServiceMetadataBasedOnProvidedDocumentWithNoMatch() throws Exception {
        final String toCheckParticipantIdentifierValue = "9901:pint_c4_jp_sb";
        final String toCheckParticipantIdentifierScheme = "iso6523-actorid-upis";
        SMPParticipantIdentifier toCheckParticipantIdentifier = new SMPParticipantIdentifier(toCheckParticipantIdentifierValue, toCheckParticipantIdentifierScheme);

        final DynamicDiscoveryService smpClient = createClient();

        //get the service metadata from SMP for using document identifier which is not supported by the participant
        final SMPDocumentIdentifier toCheckDocumentIdentifier = new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##notExistent",
                PEPPOL_DOCTYPE_WILDCARD);

        final DDCFetchException exception = assertThrows(DDCFetchException.class,
                () -> smpClient.getSubresource(toCheckParticipantIdentifier, toCheckDocumentIdentifier));
        assertEquals(DDCExceptionCode.FETCH_EXCEPTION, exception.getSmpExceptionCode());
        assertThat(exception.getMessage(), containsString("Can not resolve wildcard identifier "));

    }

    @Test
    void getServiceMetadataForNonRegisteredParticipant() throws Exception {
        final String toCheckParticipantIdentifierValue = "9901:eDeliveryNotRegisteredParticipant";
        final String toCheckParticipantIdentifierScheme = "iso6523-actorid-upis";
        SMPParticipantIdentifier toCheckParticipantIdentifier = new SMPParticipantIdentifier(toCheckParticipantIdentifierValue, toCheckParticipantIdentifierScheme);

        final DynamicDiscoveryService smpClient = createClient();

        //get the service metadata from SMP for using document identifier which is not supported by the participant
        final SMPDocumentIdentifier toCheckDocumentIdentifier = new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##notExistent",
                PEPPOL_DOCTYPE_WILDCARD);

        final DNSLookupException exception = assertThrows(DNSLookupException.class, () ->
           smpClient.getSubresource(toCheckParticipantIdentifier, toCheckDocumentIdentifier)
        );
        assertThat(exception.getMessage(), containsString("can not be resolved!"));
        assertEquals(DDCExceptionCode.SERVICE_GROUP, exception.getSmpExceptionCode());

    }

    @Test
    void testGetServiceMetadataWithExactMatch() throws TechnicalException {
        //invoice capability to check
        final SMPDocumentIdentifier toCheckDocumentIdentifier = new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:cen.eu:en16931:2017#compliant#urn:fdc:peppol.eu:2017:poacc:billing:3.0::2.1",
                BUSDOX_DOCID_QNS);

        List<SMPDocumentIdentifier> supportedDocumentIdentifiers = new ArrayList<>();

        //exatch match
        final SMPDocumentIdentifier notMatchingDocumentIdentifier = new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:cen.eu:en16931:2017#compliant#notMatching",
                BUSDOX_DOCID_QNS);
        supportedDocumentIdentifiers.add(notMatchingDocumentIdentifier);

        //exatch match
        final SMPDocumentIdentifier exactMatchDocumentIdentifier = new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:cen.eu:en16931:2017#compliant#urn:fdc:peppol.eu:2017:poacc:billing:3.0::2.1",
                BUSDOX_DOCID_QNS);
        supportedDocumentIdentifiers.add(exactMatchDocumentIdentifier);

        //invoice capability with wildcard(this document type is not existing, it is just added for testing purposes)
        final SMPDocumentIdentifier wildcardDocumentIdentifier = new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:cen.eu:en16931:2017#compliant#urn:fdc:peppol.eu:2017:poacc:billing:3.0*",
                PEPPOL_DOCTYPE_WILDCARD);
        supportedDocumentIdentifiers.add(wildcardDocumentIdentifier);

        doTestGetServiceMetadataAndAssert(toCheckDocumentIdentifier, supportedDocumentIdentifiers);
    }

    @Test
    void testGetServiceMetadataWithWildcardMatch() throws TechnicalException {
        //invoice capability to check
        final SMPDocumentIdentifier toCheckDocumentIdentifier = new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:cen.eu:en16931:2017#compliant#urn:fdc:peppol.eu:2017:poacc:billing:3.0::2.1",
                PEPPOL_DOCTYPE_WILDCARD);


        List<SMPDocumentIdentifier> supportedDocumentIdentifiers = new ArrayList<>();

        //invoice capability with wildcard(this document type is not existing, it is just added for testing purposes)
        final SMPDocumentIdentifier notMatching = new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##notMatching*",
                PEPPOL_DOCTYPE_WILDCARD);
        supportedDocumentIdentifiers.add(notMatching);

        //invoice capability with wildcard(this document type is not existing, it is just added for testing purposes)
        final SMPDocumentIdentifier wildcardDocumentIdentifierShort = new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:cen.eu:en16931:2017#compliant#urn:fdc:peppol*",
                PEPPOL_DOCTYPE_WILDCARD);
        supportedDocumentIdentifiers.add(wildcardDocumentIdentifierShort);

        //invoice capability with wildcard(this document type is not existing, it is just added for testing purposes)
        final SMPDocumentIdentifier wildcardDocumentIdentifier = new SMPDocumentIdentifier(
                "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:cen.eu:en16931:2017#compliant#urn:fdc:peppol.eu:2017:poacc:billing:3.0*",
                PEPPOL_DOCTYPE_WILDCARD);
        supportedDocumentIdentifiers.add(wildcardDocumentIdentifier);

        //we expect that the longest match is used
        doTestGetServiceMetadataAndAssert(toCheckDocumentIdentifier, supportedDocumentIdentifiers);
    }

    private void doTestGetServiceMetadataAndAssert(SMPDocumentIdentifier toCheckDocumentIdentifier, List<SMPDocumentIdentifier> supportedDocumentIdentifiers) throws TechnicalException {
        SMPParticipantIdentifier participantIdentifier = Mockito.mock(SMPParticipantIdentifier.class);
        DynamicDiscoveryService dynamicDiscovery = Mockito.mock(DynamicDiscoveryService.class);

        //START record mocks

        final SMPServiceGroup serviceGroup = Mockito.mock(SMPServiceGroup.class);
        //record discovered supported documents
        Mockito.doReturn(supportedDocumentIdentifiers).when(serviceGroup).getDocumentIdentifiers();
        Mockito.doReturn(serviceGroup).when(dynamicDiscovery).getResource(participantIdentifier);

        //END record mocks

        //call the method under test
        dynamicDiscovery.getSubresource(participantIdentifier, toCheckDocumentIdentifier);

        //record capture
        ArgumentCaptor<SMPDocumentIdentifier> smpDocumentIdentifierArgumentCaptor = ArgumentCaptor.forClass(SMPDocumentIdentifier.class);
        verify(dynamicDiscovery, times(1)).getSubresource(ArgumentMatchers.any(), smpDocumentIdentifierArgumentCaptor.capture());

        //we check the discovered SMPServiceMetadata
        final SMPDocumentIdentifier capturedSmpDocumentIdentifier = smpDocumentIdentifierArgumentCaptor.getValue();

        //we expect that the exact match id done
        assertTrue(StringUtils.containsIgnoreCase(toCheckDocumentIdentifier.getIdentifier(), new WildcardUtil().getValueUntilWildcardCharacter(capturedSmpDocumentIdentifier.getIdentifier())));
    }


    @Disabled("Enable when testing looking up a participant while it is registered in the DNS. " +
            "Useful to check if this participant is not cached for a long time in the DNS cache.")
    @Test
    void lookupParticipantWhileItIsRegistered() throws Exception {
        final String toCheckParticipantIdentifierValue = "9925:EDELIVERY_TEST3";
        final String toCheckParticipantIdentifierScheme = "iso6523-actorid-upis";

        final DynamicDiscoveryService client = createClient();
        SMPParticipantIdentifier toCheckParticipantIdentifier = new SMPParticipantIdentifier(toCheckParticipantIdentifierValue, toCheckParticipantIdentifierScheme);
        SMPServiceGroup serviceGroup = null;
        final long start = System.currentTimeMillis();
        for (int i = 0; i < 5000; i++) {
            LOG.info("Checking participant");
            try {
                serviceGroup = client.getResource(toCheckParticipantIdentifier);
                if (serviceGroup != null) {
                    final long duration = System.currentTimeMillis() - start;
                    LOG.info("Found participant [{}] after [{}] sec", serviceGroup, duration / 1000);
                    break;
                }
            } catch (Exception e) {
                LOG.info("Participant not yet created [{}]", e.getMessage());
            }

            LOG.info("Sleeping");
            Thread.sleep(5000);
        }
        assertNotNull(serviceGroup);
    }
}
