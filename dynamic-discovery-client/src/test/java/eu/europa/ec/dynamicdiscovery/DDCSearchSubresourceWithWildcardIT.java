/*-
 * #%L
 * dynamic-discovery-client
 * %%
 * Copyright (C) 2016 - 2025 European Commission | eDelivery | Dynamic Discovery Client
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

import eu.europa.ec.dynamicdiscovery.core.extension.impl.oasis10.OasisSMP10Extension;
import eu.europa.ec.dynamicdiscovery.core.extension.impl.peppol.PeppolSMPExtension;
import eu.europa.ec.dynamicdiscovery.core.fetcher.URLFetcherMock;
import eu.europa.ec.dynamicdiscovery.core.locator.PublisherLookupResult;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.impl.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.core.locator.impl.DefaultBDXRLocator;
import eu.europa.ec.dynamicdiscovery.core.provider.impl.DefaultDocumentRequestProvider;
import eu.europa.ec.dynamicdiscovery.core.reader.impl.DefaultBDXRReader;
import eu.europa.ec.dynamicdiscovery.core.security.ISignatureValidator;
import eu.europa.ec.dynamicdiscovery.exception.DNSFetchException;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceMetadata;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.service.impl.DynamicDiscoveryService;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import eu.europa.ec.dynamicdiscovery.util.DNSUtils;
import eu.europa.ec.dynamicdiscovery.util.TestCaseConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.slf4j.Logger;

import java.util.List;

import static eu.europa.ec.dynamicdiscovery.util.DNSUtils.TEST_TOP_DOMAIN_01;
import static eu.europa.ec.dynamicdiscovery.util.DNSUtils.createMockPublisherLookupNaptrResult;
import static eu.europa.ec.dynamicdiscovery.util.TestCaseConstants.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Purpose of the class is to test the wildcard subresource lookup.
 */
public class DDCSearchSubresourceWithWildcardIT {
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(DDCSearchSubresourceWithWildcardIT.class);


    @ParameterizedTest
    @CsvSource({"Resolve Wildcard identifier," +
                    CommonUtil.OASIS_SMP_10 + "," +
                    WILDCARD_SCHEME + "," +
                    "service_metadata_valid_iso6523_wildcard," +
                    "service_group_valid_iso6523_wildcard," +
                    "/iso6523-actorid-upis%3A%3A9925%3A0367302178/services/bdx-docid-wildcard%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3ACreditNote-2%3A%3ACreditNote%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns014%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol5a%2A%3A%3A2.1," +
                    "urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:invoice::2.1," +
                    "urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a*::2.1",
            "Resolve Wildcard identifier With Exact Match," +
                    CommonUtil.OASIS_SMP_10 + "," +
                    WILDCARD_SCHEME + "," +
                    "service_metadata_valid_iso6523_wildcard_exact_match," +
                    "service_group_valid_iso6523_wildcard_with_exact_match," +
                    "/iso6523-actorid-upis%3A%3A9925%3A0367302178/services/bdx-docid-wildcard%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3ACreditNote-2%3A%3ACreditNote%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns014%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol5a%3A%3A2.1," +
                    "urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a::2.1," +
                    "urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a::2.1",
            "Resolve Wildcard identifier Which hMatches the Exact Wildcard identifier," +
                    CommonUtil.OASIS_SMP_10 + "," +
                    WILDCARD_SCHEME + "," +
                    "service_metadata_valid_iso6523_wildcard," +
                    "service_group_valid_iso6523_wildcard," +
                    "/iso6523-actorid-upis%3A%3A9925%3A0367302178/services/bdx-docid-wildcard%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3ACreditNote-2%3A%3ACreditNote%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns014%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol5a%2A%3A%3A2.1," +
                    "urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a*::2.1," +
                    "urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a*::2.1",
            "Resolve PEPPOL document type Wildcard identifier Which Matches the case insensitive identifier," +
                    CommonUtil.PEPPOL + "," +
                    PEPPOL_DOCTYPE_WILDCARD + "," +
                    "signed_service_metadata_valid_iso6523_wildcard," +
                    "peppol_service_group_valid_iso6523_wildcard," +
                    "/iso6523-actorid-upis%3A%3A9925%3A0367302178/services/peppol-doctype-wildcard%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3AInvoice-2%3A%3AInvoice%23%23urn%3Apeppol%3Apint%3Abilling-3.0%40jp%3Apeppol-1%2A%3A%3A2.1," +
                    "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:peppol:pint:billing-3.0@jp:peppol-1:invoice::2.1," +
                    "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:peppol:pint:billing-3.0@jp:peppol-1*::2.1",
    })
void testSubresourceWildcardDiscoveryOK( String name,
                                          String documentType,
                                          String wildcardScheme,
                                          String filenameSubresourceWildcard,
                                          String filenameResourceWildcard,
                                          String subresourceURLPath,
                                          String identifier,
                                          String expectedIdentifier) throws Exception {

        LOG.info(name);
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(documentType,
                URLFetcherMock.LookupType.CNAME,
                subresourceURLPath,
                filenameSubresourceWildcard,
                PARTY_9925_0367302178_CNAME_QUERY + "." + TEST_TOP_DOMAIN_01);
        urlFetcherURL.addParameters(documentType, filenameResourceWildcard, TestCaseConstants.PARTY_9925_0367302178_SERVICE_GROUP_URL);

        final SMPDocumentIdentifier smpDocumentIdentifierToCheck = new SMPDocumentIdentifier(
                identifier,
                wildcardScheme);
        getDocumentWithWildcardSchemeAndAssert(urlFetcherURL, smpDocumentIdentifierToCheck, expectedIdentifier, wildcardScheme);

    }

    @Test
    void getOasis10DocumentIdentifierWithWildcardSchemeWithNoMatch() throws Exception {
        final String filenameSubresourceWildcard = "service_metadata_valid_iso6523_wildcard";
        final String filenameResourceWildcard = "service_group_valid_iso6523_wildcard";
        final String subresourceURLPath = "iso6523-actorid-upis%3A%3A9925%3A0367302178/services/bdx-docid-wildcard%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3ACreditNote-2%3A%3ACreditNote%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns014%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol5a%2A%3A%3A2.1";
        final String identifier = "Invoice-2::Invoice##urn:peppol:pint:billing-3.0@jp:peppol-1*::2.1";

        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(CommonUtil.OASIS_SMP_10,
                URLFetcherMock.LookupType.CNAME,
                subresourceURLPath,
                filenameSubresourceWildcard,
                PARTY_9925_0367302178_CNAME_QUERY + "." + TEST_TOP_DOMAIN_01);
        urlFetcherURL.addParameters(CommonUtil.OASIS_SMP_10, filenameResourceWildcard, TestCaseConstants.PARTY_9925_0367302178_SERVICE_GROUP_URL);

        //provide a document which is not matching
        final SMPDocumentIdentifier smpDocumentIdentifierToCheck = new SMPDocumentIdentifier(
                identifier,
                WILDCARD_SCHEME);

        DNSFetchException result = assertThrows(DNSFetchException.class, () -> getDocumentWithWildcardSchemeAndAssert(urlFetcherURL, smpDocumentIdentifierToCheck, null, WILDCARD_SCHEME));
        assertThat(result.getMessage(), containsString("Can not resolve wildcard identifier"));
    }

    /**
     * @param urlFetcherMock
     * @param smpDocumentIdentifierToCheck
     * @param expectedDiscoveredDocumentIdentifier
     * @param expectedDiscoveredDocumentScheme
     * @throws Exception
     */
    private void getDocumentWithWildcardSchemeAndAssert(URLFetcherMock urlFetcherMock,
                                                        SMPDocumentIdentifier smpDocumentIdentifierToCheck,
                                                        String expectedDiscoveredDocumentIdentifier,
                                                        String expectedDiscoveredDocumentScheme) throws Exception {
        SMPParticipantIdentifier participantIdentifier = PARTY_9925_0367302178_IDENTIFIER;

        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);

        List<PublisherLookupResult> publisherLookupResult = createMockPublisherLookupNaptrResult(participantIdentifier, TestCaseConstants.PUBLISHER_URL_02);
        Mockito.when(defaultDNSLookup.naptrUrlValueLookup(participantIdentifier,
                        PARTY_9925_0367302178_NAPTR_QUERY + "." + DNSUtils.TEST_TOP_DOMAIN_01))
                .thenReturn(publisherLookupResult);

        final ISignatureValidator emptyValidator = (document, trustedList) -> null;

        final DefaultBDXRLocator defaultBDXRLocator = new DefaultBDXRLocator.Builder()
                .addTopDnsDomain(DNSUtils.TEST_TOP_DOMAIN_01)
                .dnsLookup(defaultDNSLookup)
                .build();
        final DefaultBDXRReader bdxReader = new DefaultBDXRReader.Builder()
                .signatureValidator(emptyValidator)
                .build();
        final DefaultDocumentRequestProvider defaultProvider = new DefaultDocumentRequestProvider.Builder()
                .build();

        DynamicDiscoveryService smpClient = new DynamicDiscoveryService.Builder()
                .publisherLocator(defaultBDXRLocator)
                .documentReader(bdxReader)
                .addExtension(new OasisSMP10Extension())
                .addExtension(new PeppolSMPExtension())
                .wildcardSubresourceSchemes(expectedDiscoveredDocumentScheme)
                .documentFetcher(urlFetcherMock)
                .documentRequestProvider(defaultProvider)
                .build();

        final SMPServiceMetadata serviceMetadata = smpClient.getSubresource(participantIdentifier, smpDocumentIdentifierToCheck);
        assertNotNull(serviceMetadata);
        final SMPDocumentIdentifier documentIdentifier = serviceMetadata.getDocumentIdentifier();
        assertNotNull(documentIdentifier);

        assertEquals(expectedDiscoveredDocumentIdentifier, documentIdentifier.getIdentifier());
        assertEquals(expectedDiscoveredDocumentScheme, documentIdentifier.getScheme());
    }
}
