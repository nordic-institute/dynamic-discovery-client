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
package eu.europa.ec.dynamicdiscovery;

import eu.europa.ec.dynamicdiscovery.core.extension.impl.PeppolSMPExtension;
import eu.europa.ec.dynamicdiscovery.core.fetcher.impl.DefaultURLFetcher;
import eu.europa.ec.dynamicdiscovery.core.locator.impl.DefaultBDXRLocator;
import eu.europa.ec.dynamicdiscovery.core.reader.impl.DefaultBDXRReader;
import eu.europa.ec.dynamicdiscovery.core.security.impl.DefaultSignatureValidator;
import eu.europa.ec.dynamicdiscovery.enums.DNSLookupType;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.SMPEndpoint;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import eu.europa.ec.dynamicdiscovery.model.SMPTransportProfile;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPProcessIdentifier;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import gen.eu.europa.ec.ddc.api.peppol.SignedServiceMetadata;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Cosmin Baciu
 */
class PeppolDocumentIdentifierIT {

    static final Logger LOG = LoggerFactory.getLogger(PeppolDocumentIdentifierIT.class);
    public static final String PEPPOL_DOCTYPE_WILDCARD = "peppol-doctype-wildcard";
    public static final String BUSDOX_DOCID_QNS = "busdox-docid-qns";

    @Test
    void getDocumentIdentifierWithParticipantHavingInvoiceCapability() throws Exception {
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

    private void getDocumentIdentifierAndAssert(String toCheckParticipantIdentifierValue,
                                                String toCheckParticipantIdentifierScheme,
                                                int expectedDocumentIdentifiers,
                                                List<SMPDocumentIdentifier> toCheckDocumentIdentifierCapabilities) throws Exception {
        SMPParticipantIdentifier toCheckParticipantIdentifier = new SMPParticipantIdentifier(toCheckParticipantIdentifierValue, toCheckParticipantIdentifierScheme);
        DefaultURLFetcher urlFetcher = new DefaultURLFetcher.Builder().build();

        final KeyStore trustStore = CommonUtil.loadTrustStore("truststore/peppol-truststore.jks");
        final DefaultSignatureValidator defaultSignatureValidator = new DefaultSignatureValidator(trustStore);
        final DefaultBDXRReader bdxReader = new DefaultBDXRReader.Builder()
                .addExtension(new PeppolSMPExtension())
                .signatureValidator(defaultSignatureValidator)
                .build();

        final DefaultBDXRLocator defaultBDXRLocator = new DefaultBDXRLocator.Builder()
                .addDnsLookupType(DNSLookupType.CNAME)
                .addTopDnsDomain("acc.edelivery.tech.ec.europa.eu")
                .build();

        //create the smp client
        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .locator(defaultBDXRLocator)
                .reader(bdxReader)
                .fetcher(urlFetcher)
                .build();

        //discover the participant document identifiers
        List<SMPDocumentIdentifier> discoveredDocumentIdentifiers = smpClient.getDocumentIdentifiers(toCheckParticipantIdentifier);
        assertEquals(expectedDocumentIdentifiers, discoveredDocumentIdentifiers.size());


        toCheckDocumentIdentifierCapabilities.stream().forEach(smpDocumentIdentifier -> {
            try {
                assertSMPDocumentIdentifier(
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

    private void assertSMPDocumentIdentifier(SMPDocumentIdentifier toCheckDocumentIdentifier,
                                             List<SMPDocumentIdentifier> discoveredDocumentIdentifiers,
                                             DynamicDiscovery smpClient,
                                             SMPParticipantIdentifier toCheckParticipantIdentifier) throws TechnicalException {
        //get the document identifier to check from the list of already discovered document identifiers
        final SMPDocumentIdentifier discoveredDocumentIdentifier = discoveredDocumentIdentifiers.stream()
                .filter(smpDocumentIdentifier -> smpDocumentIdentifier.getIdentifier().equals(toCheckDocumentIdentifier.getIdentifier())
                        && smpDocumentIdentifier.getScheme().equals(toCheckDocumentIdentifier.getScheme()))
                .findFirst()
                .orElse(null);
        assertNotNull(discoveredDocumentIdentifier);

        //get the service metadata from SMP for the document identifier
        final SMPServiceMetadata discoveredServiceMetadata = smpClient.getServiceMetadata(toCheckParticipantIdentifier, discoveredDocumentIdentifier);

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

        //check endpoint
        final List<SMPEndpoint> endpoints = discoveredServiceMetadata.getEndpoints();
        assertNotNull(endpoints);
        assertEquals(1, endpoints.size());
        final SMPEndpoint smpEndpoint = endpoints.get(0);
        assertNotNull(smpEndpoint.getCertificate());

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
    void getDocumentIdentifierWithParticipantHavingInvoiceWildcardCapability() throws Exception {
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

}
