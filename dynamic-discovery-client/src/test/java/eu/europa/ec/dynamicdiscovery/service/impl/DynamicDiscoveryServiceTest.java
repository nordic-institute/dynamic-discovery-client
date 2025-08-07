package eu.europa.ec.dynamicdiscovery.service.impl;

import eu.europa.ec.dynamicdiscovery.core.extension.impl.oasis10.OasisSMP10Extension;
import eu.europa.ec.dynamicdiscovery.core.extension.impl.oasis20.OasisSMP20Extension;
import eu.europa.ec.dynamicdiscovery.core.extension.impl.peppol.PeppolSMPExtension;
import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.fetcher.IMetadataFetcher;
import eu.europa.ec.dynamicdiscovery.core.locator.IMetadataLocator;
import eu.europa.ec.dynamicdiscovery.core.reader.IMetadataReader;
import eu.europa.ec.dynamicdiscovery.core.reader.impl.DefaultBDXRReader;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.SMPEndpoint;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

import static org.apache.commons.lang3.StringUtils.trim;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DynamicDiscoveryServiceTest {

    IMetadataFetcher metadataFetcher = Mockito.mock(IMetadataFetcher.class);
    IMetadataLocator metadataLocator = Mockito.mock(IMetadataLocator.class);
    IMetadataReader metadataReader = new DefaultBDXRReader.Builder()
            .addExtension(new OasisSMP10Extension())
            .addExtension(new OasisSMP20Extension())
            .addExtension(new PeppolSMPExtension())
            .build();

    DynamicDiscoveryService testInstance = new DynamicDiscoveryService.Builder()
            .metadataLocator(metadataLocator)
            .metadataFetcher(metadataFetcher)
            .metadataReader(metadataReader)
            .build();


    @ParameterizedTest
    @CsvSource({
            "oasis-smp-1.0, signed_service_metadata_signed_valid_iso6523, " +
                    "urn:www.cenbii.eu:profile:bii05:ver2.0, cenbii-procid-ubl," +
                    " bdxr-transport-ebms3-as4-v1p0, " +
                    "https://test.erechnung.gv.at/as4/msh/",
            "oasis-smp-2.0, service_metadata_unsigned_valid_iso6523_draft, " +
                    "urn:www.cenbii.eu:profile:bii05:ver2.0, cenbii-procid-ubl, " +
                    "bdxr-transport-ebms3-as4-v1p0, " +
                    "https://ap.example.com/as4",
            "oasis-smp-2.0, service_metadata_unsigned_valid_iso6523_final, " +
                    "urn:www.cenbii.eu:profile:bii05:ver2.0, cenbii-procid-ubl, " +
                    "bdxr-transport-ebms3-as4-v1p0, " +
                    "https://ap.example.com/as4",
            "peppol, signed_service_metadata_valid_iso6523_wildcard, " +
                    "urn:fdc:peppol.eu:2017:poacc:billing:01:1.0, cenbii-procid-ubl, " +
                    "peppol-transport-as4-v2_0, " +
                    "https://localhost:8080/as4"})
    void testDiscoverEndpoint(String standard, String resourceName, String processIdentifierValue, String processIdentifierScheme, String transportProfileID, String result) throws TechnicalException, URISyntaxException {

        URI smpURI = new URI("http://example.local:1234/");
        FetcherResponse fetcherResponse = Mockito.mock(FetcherResponse.class);
        SMPParticipantIdentifier resourceId = Mockito.mock(SMPParticipantIdentifier.class);
        SMPDocumentIdentifier subresourceId = Mockito.mock(SMPDocumentIdentifier.class);
        InputStream serviceMetadataStream = getResourceAsStream(standard, resourceName);
        assertNotNull(serviceMetadataStream);
        // given
        Mockito.doReturn(smpURI).when(metadataLocator).lookup(Mockito.any());
        Mockito.doReturn(fetcherResponse).when(metadataFetcher).fetch(Mockito.any());
        Mockito.doReturn(serviceMetadataStream).when(fetcherResponse).getInputStream();

        // when
        SMPEndpoint endpoint = testInstance.discoverEndpoint(resourceId, subresourceId,
                processIdentifierValue, processIdentifierScheme, Collections.singletonList(transportProfileID));
        // then
        assertNotNull(endpoint);
        assertNotNull(endpoint.getTransportProfile());
        assertNotNull(endpoint.getProcessIdentifier());
        assertEquals(result, endpoint.getAddress());
        assertEquals(transportProfileID, trim(endpoint.getTransportProfile().getIdentifier()));
        assertEquals(processIdentifierValue, trim(endpoint.getProcessIdentifier().getIdentifier()));
        assertEquals(processIdentifierScheme, trim(endpoint.getProcessIdentifier().getScheme()));
        assertNotNull(endpoint.getCertificate());

    }

    @ParameterizedTest
    @CsvSource({
            "oasis-smp-1.0, signed_service_metadata_redirection, " +
                    "http://serviceMetadata2.eu/busdox-actoridupis%3A%3A0010%3A5798000000001/services/busdox-docidqns%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3AInvoice-2%3A%3AInvoice%23%23UBL-2.0",
            "oasis-smp-2.0, service_metadata_unsigned_redirection, " +
                    "https://ap.example.com/iso6523-actorid-upis::9915:123456789/services/bdx-docid-qns::Invoice:ver2.0",
            "peppol, signed_service_metadata_redirection, " +
                    "http://serviceMetadata2.eu/busdox-actoridupis%3A%3A0010%3A5798000000001/services/busdox-docidqns%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3AInvoice-2%3A%3AInvoice%23%23UBL-2.0"
    })
    void testDiscoverEndpointWithRedirectDisabled(String standard, String resourceName, String redirectURL) throws TechnicalException, URISyntaxException {

        String anyString = "anyString";
        URI smpURI = new URI("http://example.local:1234/");
        FetcherResponse fetcherResponse = Mockito.mock(FetcherResponse.class);
        SMPParticipantIdentifier resourceId = Mockito.mock(SMPParticipantIdentifier.class);
        SMPDocumentIdentifier subresourceId = Mockito.mock(SMPDocumentIdentifier.class);
        InputStream serviceMetadataStream = getResourceAsStream(standard, resourceName);
        assertNotNull(serviceMetadataStream);
        // given
        Mockito.doReturn(smpURI).when(metadataLocator).lookup(Mockito.any());
        Mockito.doReturn(fetcherResponse).when(metadataFetcher).fetch(Mockito.any());
        Mockito.doReturn(serviceMetadataStream).when(fetcherResponse).getInputStream();

        // when
        testInstance.setRedirectionEnabled(false); // make sure it is disabled
        SMPEndpoint endpoint = testInstance.discoverEndpoint(resourceId, subresourceId,
                anyString, anyString, Collections.singletonList("any"));
        // then
        assertNotNull(endpoint);
        assertNotNull(endpoint.getRedirect());
        assertEquals(redirectURL, endpoint.getRedirect().getRedirectUrl());
    }


    @ParameterizedTest
    @CsvSource({
            "oasis-smp-1.0,signed_service_metadata_redirection, signed_service_metadata_signed_valid_iso6523, " +
                    "urn:www.cenbii.eu:profile:bii05:ver2.0, cenbii-procid-ubl," +
                    " bdxr-transport-ebms3-as4-v1p0, " +
                    "https://test.erechnung.gv.at/as4/msh/",
            "oasis-smp-2.0, service_metadata_unsigned_redirection, service_metadata_unsigned_valid_iso6523_draft, " +
                    "urn:www.cenbii.eu:profile:bii05:ver2.0, cenbii-procid-ubl, " +
                    "bdxr-transport-ebms3-as4-v1p0, " +
                    "https://ap.example.com/as4",
            "oasis-smp-2.0, service_metadata_unsigned_redirection, service_metadata_unsigned_valid_iso6523_final, " +
                    "urn:www.cenbii.eu:profile:bii05:ver2.0, cenbii-procid-ubl, " +
                    "bdxr-transport-ebms3-as4-v1p0, " +
                    "https://ap.example.com/as4",
            "peppol, signed_service_metadata_redirection, signed_service_metadata_valid_iso6523_wildcard, " +
                    "urn:fdc:peppol.eu:2017:poacc:billing:01:1.0, cenbii-procid-ubl, " +
                    "peppol-transport-as4-v2_0, " +
                    "https://localhost:8080/as4"})
    void testDiscoverEndpointRedirectionEnabled(String standard, String resourceName,
                                                String redirectedResourceName, String processIdentifierValue, String processIdentifierScheme, String transportProfileID, String result) throws TechnicalException, URISyntaxException {

        URI smpURI = new URI("http://example.local:1234/");
        FetcherResponse fetcherResponse1 = Mockito.mock(FetcherResponse.class);
        FetcherResponse fetcherResponse2 = Mockito.mock(FetcherResponse.class);
        SMPParticipantIdentifier resourceId = Mockito.mock(SMPParticipantIdentifier.class);
        SMPDocumentIdentifier subresourceId = Mockito.mock(SMPDocumentIdentifier.class);
        InputStream serviceMetadataStream = getResourceAsStream(standard, resourceName);
        InputStream serviceMetadataRedirectedStream = getResourceAsStream(standard, redirectedResourceName);

        // given
        Mockito.doReturn(smpURI).when(metadataLocator).lookup(Mockito.any());
        // first return serviceMetadataStream and then serviceMetadataRedirectedStream
        Mockito.doReturn(fetcherResponse1, fetcherResponse2).when(metadataFetcher).fetch(Mockito.any());
        Mockito.doReturn(serviceMetadataStream).when(fetcherResponse1).getInputStream();
        Mockito.doReturn(serviceMetadataRedirectedStream).when(fetcherResponse2).getInputStream();

        // when
        testInstance.setRedirectionEnabled(true); // make sure it is enabled
        SMPEndpoint endpoint = testInstance.discoverEndpoint(resourceId, subresourceId,
                processIdentifierValue, processIdentifierScheme, Collections.singletonList(transportProfileID));
        // then
        Mockito.verify(metadataFetcher, Mockito.times(2)).fetch(Mockito.any());
        assertNotNull(endpoint);
        assertNotNull(endpoint.getTransportProfile());
        assertNotNull(endpoint.getProcessIdentifier());
        assertEquals(result, endpoint.getAddress());
        assertEquals(transportProfileID, trim(endpoint.getTransportProfile().getIdentifier()));
        assertEquals(processIdentifierValue, trim(endpoint.getProcessIdentifier().getIdentifier()));
        assertEquals(processIdentifierScheme, trim(endpoint.getProcessIdentifier().getScheme()));
        assertNotNull(endpoint.getCertificate());
    }

    private static InputStream getResourceAsStream(String standard, String resourceName) {
        return DynamicDiscoveryServiceTest.class
                .getResourceAsStream("/response/" + standard + "/" + resourceName + ".xml");
    }
}
