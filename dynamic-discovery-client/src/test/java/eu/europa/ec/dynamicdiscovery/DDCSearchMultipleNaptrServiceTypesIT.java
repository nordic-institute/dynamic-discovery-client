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

import eu.europa.ec.dynamicdiscovery.core.extension.IExtension;
import eu.europa.ec.dynamicdiscovery.core.extension.impl.oasis10.OasisSMP10Extension;
import eu.europa.ec.dynamicdiscovery.core.extension.impl.oasis20.OasisSMP20Extension;
import eu.europa.ec.dynamicdiscovery.core.fetcher.URLFetcherMock;
import eu.europa.ec.dynamicdiscovery.core.locator.PublisherLookupResult;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.impl.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.core.locator.impl.DefaultBDXRLocator;
import eu.europa.ec.dynamicdiscovery.core.reader.impl.DefaultBDXRReader;
import eu.europa.ec.dynamicdiscovery.core.security.impl.DefaultSignatureValidator;
import eu.europa.ec.dynamicdiscovery.enums.DNSLookupType;
import eu.europa.ec.dynamicdiscovery.model.SMPServiceGroup;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.service.impl.DynamicDiscoveryService;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import eu.europa.ec.dynamicdiscovery.util.DNSUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static eu.europa.ec.dynamicdiscovery.util.DNSUtils.createMockPublisherLookupNaptrResult;
import static eu.europa.ec.dynamicdiscovery.util.TestCaseConstants.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * The purpose of this test is to verify the behavior of the DDC when multiple NAPTR records are present for the same participant.
 * The DDC should be able to handle the different scenarios and return the correct document identifier. Below are the scenarios that should be tested:
 * <ul>
 *     <li>eDelivery SMP 1.0 only</li>*
 *     <li>eDelivery SMP 2.0 only</li>
 *     <li>eDelivery SMP 2.0 with "eDelivery SMP 2 MAY" use SMP 1.0 NAPTR service"</li>
 *     <li>eDelivery SMP 2.0 with 1.0 fallback</li>
 *     <li>eDelivery SMP 2.0 with "eDelivery SMP 2 MAY" and 1.0 fallback</li>
 * <ul>
 *  *
 * @author Joze RIHTARSIC
 * @since 3.0
 */
class DDCSearchMultipleNaptrServiceTypesIT {
    private static final Logger LOG = LoggerFactory.getLogger(DDCSearchMultipleNaptrServiceTypesIT.class);


    /**
     * eDelivery SMP 1.0 only (regardless of the presence of other NAPTR records and documents)
     */
    @ParameterizedTest
    @CsvSource({
            "'Success: Only SMP 1.0 is registers', oasis-smp-1.0, service_group_valid_iso6523, 'Meta:SMP', false, DNSLookupException, ''",
            "'Success: SMP 1.0 and SMP 2.0 are registered', oasis-smp-1.0, service_group_valid_iso6523, 'Meta:SMP,oasis-bdxr-smp-2', false, DNSLookupException, ''",
            "'Fail: Only SMP 2.0 NAPTR is registered, but with SMP 1.0 document', oasis-smp-1.0, service_group_valid_iso6523, 'oasis-bdxr-smp-2', true, DNSLookupException, 'Non of the extensions supports Publisher lookup results (e.g. NAPTR services)'",
            "'Fail: Only SMP 2.0 is registered', oasis-smp-2.0, service_group_unsigned_valid_iso6523, 'oasis-bdxr-smp-2', true, DNSLookupException, 'Non of the extensions supports Publisher lookup results (e.g. NAPTR services)'",
            "'Fail: Only SMP 2.0 is registered under SMP 1.0 DNS record and SMP path', oasis-smp-2.0, service_group_unsigned_valid_iso6523, 'Meta:SMP', true,  DocumentParseException, 'No parser registered for '",
            "'Fail: No NAPTR records registered', oasis-smp-1.0, service_group_valid_iso6523, '', true, DNSLookupException, 'can not be resolved!'"
    })
    void testDDCConfigurationForOasisSMP10Only(String desc, String documentType, String filename, String naptrServices,
                                               boolean expectedError, String exceptionClassName, String expectedMessage) throws Exception {
        LOG.info(desc);
        // given
        OasisSMP10Extension extension = new OasisSMP10Extension();

        testDDCConfiguration(List.of(extension), "", documentType, filename, naptrServices, expectedError, exceptionClassName, expectedMessage);

    }


    /**
     * eDelivery SMP 2.0 only
     */
    @ParameterizedTest
    @CsvSource({
            "'Success: Only SMP 2.0 is registers',  oasis-smp-2.0, service_group_unsigned_valid_iso6523, 'oasis-bdxr-smp-2', false, '', ''",
            "'Success: SMP 1.0 and SMP 2.0 are registered', oasis-smp-2.0, service_group_unsigned_valid_iso6523, 'Meta:SMP,oasis-bdxr-smp-2', false, '', ''",
            "'Fail: Only SMP 1.0 NAPTR is registered, but with SMP 2.0 document', oasis-smp-2.0, service_group_unsigned_valid_iso6523, 'Meta:SMP', true, DNSLookupException, 'Non of the extensions supports Publisher lookup results (e.g. NAPTR services)'",
            "'Fail: Only SMP 1.0 is registered',oasis-smp-1.0, service_group_valid_iso6523, 'Meta:SMP', true, DNSLookupException, 'Non of the extensions supports Publisher lookup results (e.g. NAPTR services)'",
            "'Fail: Only SMP 1.0 is registered under SMP 2.0 DNS record and SMP path', oasis-smp-1.0, service_group_valid_iso6523, 'oasis-bdxr-smp-2', true,  DocumentParseException, 'No parser registered for '",
            "'Fail: No NAPTR records registered', oasis-smp-1.0, service_group_valid_iso6523, '', true, DNSLookupException, 'can not be resolved!'"
    })
    void testDDCConfigurationForOasisSMP20Only(String desc, String documentType, String filename, String naptrServices,
                                               boolean expectedError, String exceptionClassName, String expectedMessage) throws Exception {
        LOG.info(desc);
        // given
        OasisSMP20Extension extension = new OasisSMP20Extension();

        testDDCConfiguration(List.of(extension), "/bdxr-smp-2", documentType, filename, naptrServices, expectedError, exceptionClassName, expectedMessage);
    }


    /**
     * eDelivery SMP 2.0 with May Meta:SMP ( Meta:SMP is registered!)
     */
    @ParameterizedTest
    @CsvSource({
            "'Success: Only SMP 2.0 is registers', /bdxr-smp-2, oasis-smp-2.0, service_group_unsigned_valid_iso6523, 'oasis-bdxr-smp-2', false, '', ''",
            "'Success: SMP 1.0 and SMP 2.0 are registered', /bdxr-smp-2,oasis-smp-2.0, service_group_unsigned_valid_iso6523, 'Meta:SMP,oasis-bdxr-smp-2', false, '', ''",
            "'Success: MAY option: Only SMP 1.0 NAPTR is registered, but with SMP 2.0 document', /bdxr-smp-2, oasis-smp-2.0, service_group_unsigned_valid_iso6523, 'Meta:SMP', false,'',''",
            "'Fail: Only SMP 1.0 is registered','', oasis-smp-1.0, service_group_valid_iso6523, 'Meta:SMP', true, DNSLookupException, 'Can not fetch Document for participant '",
            "'Fail: Only SMP 1.0 is registered under SMP 2.0 DNS record and SMP path', /bdxr-smp-2, oasis-smp-1.0, service_group_valid_iso6523, 'oasis-bdxr-smp-2', true,  DocumentParseException, 'No parser registered for '",
            "'Fail: No NAPTR records registered', /bdxr-smp-2, oasis-smp-1.0, service_group_valid_iso6523, '', true, DNSLookupException, 'can not be resolved!'"
    })
    void testDDCConfigurationForOasisSMP20WithMay10Naptr(String desc,String urlExpectedContext, String documentType, String filename, String naptrServices,
                                               boolean expectedError, String exceptionClassName, String expectedMessage) throws Exception {
        LOG.info(desc);
        // given
        OasisSMP20Extension extension = new OasisSMP20Extension();
        extension.setSMP10LookupNaptrServiceEnabled(true);

        testDDCConfiguration(List.of(extension), urlExpectedContext, documentType, filename, naptrServices, expectedError, exceptionClassName, expectedMessage);
    }

    /**
     * eDelivery SMP 2.0 with "eDelivery SMP 2 MAY" and 1.0 fallback
     */

    /**
     * eDelivery SMP 2.0 with May Meta:SMP ( Meta:SMP is registered!)
     */
    @ParameterizedTest
    @CsvSource({
            "'Success: Only SMP 2.0 is registers', /bdxr-smp-2, oasis-smp-2.0, service_group_unsigned_valid_iso6523, 'oasis-bdxr-smp-2', false, '', ''",
            "'Success: SMP 1.0 and SMP 2.0 are registered', /bdxr-smp-2,oasis-smp-2.0, service_group_unsigned_valid_iso6523, 'Meta:SMP,oasis-bdxr-smp-2', false, '', ''",
            "'Success: MAY option: Only SMP 1.0 NAPTR is registered, but with SMP 2.0 document', /bdxr-smp-2, oasis-smp-2.0, service_group_unsigned_valid_iso6523, 'Meta:SMP', false,'',''",
            "'Success: Only SMP 1.0 is registered','', oasis-smp-1.0, service_group_valid_iso6523, 'Meta:SMP', false, '', ''",
            "'Fail: Only SMP 1.0 is registered under SMP 2.0 DNS record and SMP path', /bdxr-smp-2, oasis-smp-1.0, service_group_valid_iso6523, 'oasis-bdxr-smp-2', true,  DocumentParseException, 'No parser registered for '",
            "'Fail: No NAPTR records registered', /bdxr-smp-2, oasis-smp-1.0, service_group_valid_iso6523, '', true, DNSLookupException, 'can not be resolved!'"
    })
    void testDDCConfigurationForOasisSMP20WithMay10NaptrAndOasisSMP10(String desc,String urlExpectedContext, String documentType, String filename, String naptrServices,
                                                         boolean expectedError, String exceptionClassName, String expectedMessage) throws Exception {
        LOG.info(desc);
        // given
        OasisSMP20Extension extensionSMP20 = new OasisSMP20Extension();
        extensionSMP20.setSMP10LookupNaptrServiceEnabled(true);
        OasisSMP10Extension extensionSMP10 = new OasisSMP10Extension();

        testDDCConfiguration(List.of(extensionSMP20, extensionSMP10 ), urlExpectedContext, documentType, filename, naptrServices, expectedError, exceptionClassName, expectedMessage);
    }

    void testDDCConfiguration(List<IExtension> extensions, String documentContext,  String documentType, String filename, String naptrServices,
                              boolean expectedError, String exceptionClassName, String expectedMessage) throws Exception {
        SMPParticipantIdentifier participantIdentifier = PARTY_9925_0367302178_IDENTIFIER;
        // set the DNS lookup
        DefaultDNSLookup defaultDNSLookup = mock(DefaultDNSLookup.class);

        if (StringUtils.isNotBlank(naptrServices)) {
            String[] naptrServicesArray = naptrServices.split(",");
            List<PublisherLookupResult> publisherLookupResult = createMockPublisherLookupNaptrResult(participantIdentifier,
                    PUBLISHER_URL_02,
                    naptrServicesArray);

            Mockito.when(defaultDNSLookup.naptrUrlValueLookup(participantIdentifier,
                            PARTY_9925_0367302178_NAPTR_QUERY + "." + DNSUtils.TEST_TOP_DOMAIN_02))
                    .thenReturn(publisherLookupResult);
        }


        // set the URL fetcher
        URLFetcherMock urlFetcherURL = new URLFetcherMock();
        urlFetcherURL.setParameters(documentType, URLFetcherMock.LookupType.NAPTR,
                documentContext+  PARTY_9925_0367302178_SERVICE_GROUP_URL,
                filename, null);

        // build the client
        DynamicDiscoveryService smpClient = new DynamicDiscoveryService.Builder()
                .addExtensions(extensions)
                .publisherLocator(new DefaultBDXRLocator.Builder()
                        .addTopDnsDomain(DNSUtils.TEST_TOP_DOMAIN_02)
                        .addDnsLookupType(DNSLookupType.NAPTR)
                        .dnsLookup(defaultDNSLookup)
                        .build())
                .documentReader(new DefaultBDXRReader.Builder()
                        .signatureValidator(new DefaultSignatureValidator(CommonUtil.loadTrustStore("truststore/truststoreForTrustedCertificate.ts")))
                        .build())
                .documentFetcher(urlFetcherURL)
                .build();
        // when

        if (expectedError) {
            Exception ex = assertThrows(Exception.class, () -> smpClient.getResource(participantIdentifier));
            assertEquals(exceptionClassName, ex.getClass().getSimpleName());
            assertThat(ex.getMessage(), containsString(expectedMessage));
        } else {
            SMPServiceGroup result = smpClient.getResource(participantIdentifier);
            // then
            assertNotNull(result);
            assertNotNull(result.getParticipantIdentifier());
            assertEquals(participantIdentifier.getIdentifier(), result.getParticipantIdentifier().getIdentifier());
            assertEquals(participantIdentifier.getScheme(), result.getParticipantIdentifier().getScheme());
        }
    }

}

