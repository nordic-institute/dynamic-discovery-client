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
package eu.europa.ec.ddc;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.fetcher.impl.DefaultURLFetcher;
import eu.europa.ec.dynamicdiscovery.core.fetcher.impl.JWTAuthorizationTokenFetcher;
import eu.europa.ec.dynamicdiscovery.core.locator.IPublisherLocator;
import eu.europa.ec.dynamicdiscovery.core.locator.PublisherLookupResult;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.impl.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.core.locator.impl.DefaultBDXRLocator;
import eu.europa.ec.dynamicdiscovery.core.locator.impl.StaticMapMetadataLocator;
import eu.europa.ec.dynamicdiscovery.core.provider.IDocumentRequestProvider;
import eu.europa.ec.dynamicdiscovery.core.provider.impl.DefaultDocumentRequestProvider;
import eu.europa.ec.dynamicdiscovery.core.reader.impl.DefaultBDXRReader;
import eu.europa.ec.dynamicdiscovery.core.security.impl.AccessTokenCredentialProvider;
import eu.europa.ec.dynamicdiscovery.core.security.impl.JwtTokenCredentialProvider;
import eu.europa.ec.dynamicdiscovery.enums.DNSLookupType;
import eu.europa.ec.dynamicdiscovery.exception.DDCExceptionCode;
import eu.europa.ec.dynamicdiscovery.exception.DDCRuntimeException;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.service.impl.DynamicDiscoveryService;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.util.Args;
import org.xbill.DNS.CNAMERecord;
import org.xbill.DNS.Record;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static eu.europa.ec.ddc.CliOptions.*;
import static java.util.Arrays.stream;
import static org.apache.commons.lang3.StringUtils.split;

/**
 * Main class for the Dynamic Discovery Client (DDC) command line interface
 */
public class DdcMain {
    private static final String LOG_ERROR = "[ERROR] ";

    public static void main(String[] args) throws RuntimeException {

        DdcMain ddc = new DdcMain();
        Options commands = CliOptions.getCommandList();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;//not a good practice, it serves it purpose

        try {
            cmd = parser.parse(commands, args, true);
        } catch (ParseException e) {
            System.out.println(LOG_ERROR + e.getMessage() + "\n\n");
            printCommandHelp(commands);
            return;
        }

        if (cmd == null || cmd.getOptions() == null || cmd.getOptions().length == 0) {
            printCommandHelp(commands);
            return;
        }

        if (cmd.hasOption(COMMAND_DNS.getOption())) {
            Options options = getCommandDnsOptions();
            try {
                cmd = parser.parse(options, args);
            } catch (ParseException e) {
                System.out.println(LOG_ERROR + e.getMessage() + "\n\n");
                printHelpOptions(options, COMMAND_DNS.getName());
                System.exit(1);
            }
            try {
                ddc.runDNS(cmd);
            } catch (TechnicalException | IOException e) {
                throw new RuntimeException(e);
            }
        } else if (cmd.hasOption(COMMAND_GET.getOption())) {
            Options options = getCommandGetOptions();
            try {
                cmd = parser.parse(options, args);
            } catch (ParseException e) {
                System.out.println(LOG_ERROR + e.getMessage() + "\n\n");
                printHelpOptions(options, COMMAND_GET.getName());
                System.exit(1);
            }
            try {
                ddc.runGet(cmd);
            } catch (TechnicalException | IOException | UnrecoverableKeyException | NoSuchAlgorithmException |
                     KeyStoreException | URISyntaxException e) {
                System.out.println(LOG_ERROR + e.getMessage() + "\n\n");
            }
        } else {
            printCommandHelp(commands);
        }

    }


    /**
     * Method prints help for options and exits application
     *
     * @param commands options
     */
    private static void printCommandHelp(Options commands) {
        printHelp(commands, "-command_name", "Commands:");
    }

    /**
     * Method prints help for options and exits application
     *
     * @param options options
     * @param command the command name
     */
    private static void printHelpOptions(Options options, String command) {
        printHelp(options, "-" + command, "'" + command + "' options:");
    }

    /**
     * Method prints help for options and exits application
     *
     * @param options the options
     * @param header  print header for the options
     */
    private static void printHelp(Options options, String command, String header) {
        HelpFormatter formatter = new HelpFormatter();


        formatter.printHelp("java -jar ddc.jar " + command + " [-options]",
                System.lineSeparator() + header + System.lineSeparator() + System.lineSeparator(), options,
                System.lineSeparator() + "Use \"java -jar ddc.jar " + command + " --help\" for usage of command_name.");
        // exit application
        System.exit(1);
    }

    protected void runGet(CommandLine cmd) throws TechnicalException, IOException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, URISyntaxException {
        // read parameters
        SMPParticipantIdentifier participantIdentifier = getResourceIdentifier(cmd);
        String domain = cmd.getOptionValue(OPTION_DNS_DOMAIN.getOption());
        String nameserver = cmd.getOptionValue(OPTION_DNS_NAMESERVER.getOption());
        String nameserverPort  = cmd.getOptionValue(OPTION_DNS_NAMESERVER_PORT.getOption());
        Integer port = null;
        if (StringUtils.isNotEmpty(nameserverPort)) {
            port = Integer.parseInt(nameserverPort);
        }
        String smpurl = cmd.getOptionValue(OPTIONS_SMP_URL.getOption());
        List<String> naptrServices = getNaptrServices(cmd);
        List<DNSLookupType> dnsLookupTypes = getDNSLookupTypes(cmd);
        AccessTokenCredentialProvider accessTokenCredentialProvider = getAccessToken(cmd);
        String srIdentifier = cmd.getOptionValue(OPTION_SUBRESOURCE_IDENTIFIER.getOption());
        String srScheme = cmd.getOptionValue(OPTION_SUBRESOURCE_SCHEME.getOption());

        SMPDocumentIdentifier subresourceIdentifier = null;
        if (StringUtils.isNotBlank(srIdentifier)) {
            subresourceIdentifier = new SMPDocumentIdentifier(srIdentifier, srScheme);
        }
        String outputFilePath = cmd.getOptionValue("output");
        outputFilePath = StringUtils.isBlank(outputFilePath) ? participantIdentifier.getIdentifier() + ".xml" : outputFilePath;

        KeyStore truststore = getTruststore(cmd);
        KeyStore keyStore = getKeystore(cmd);

        IPublisherLocator testBDXRLocator;
        if (StringUtils.isBlank(smpurl)) {
            // configure DNS lookup client if SMP URL is not provided
            DefaultDNSLookup testDNSLookup = new DefaultDNSLookup.Builder()
                    .nameserverPort(port)
                    .nameserver(nameserver)
                    .addRequiredNaptrServices(naptrServices)
                    .build();
            // configure BDXR locator
            testBDXRLocator = new DefaultBDXRLocator.Builder()
                    .addTopDnsDomain(domain)
                    .addDnsLookupTypes(dnsLookupTypes)
                    .dnsLookup(testDNSLookup).build();
        } else {
            // configure static BDXR locator
            testBDXRLocator = new StaticMapMetadataLocator(new URI(smpurl));
        }

        // configure URL fetcher
        DefaultURLFetcher.Builder testURLFetcherBuilder = new DefaultURLFetcher.Builder()
                .credentialProvider(accessTokenCredentialProvider)
                .tlsTruststore(truststore);

        if (keyStore != null) {
            String pwd = cmd.getOptionValue(OPTIONS_KEYSTORE_KEY_PASSWORD.getOption());
            if (StringUtils.isBlank(pwd)) {
                throw new IllegalArgumentException("Keystore key password is not defined");
            }
            testURLFetcherBuilder.tlsKeystore(keyStore, pwd.toCharArray());
            testURLFetcherBuilder.noHostnameValidation(cmd.hasOption(OPTION_NO_TLS_HOSTNAME_VALIDATION.getOption()));
        }



        JwtTokenCredentialProvider jwtTokenCredentialProvider = buildJwtTokenCredentialProvider(cmd, truststore, keyStore);
        if (jwtTokenCredentialProvider != null) {
            if (accessTokenCredentialProvider != null) {
                throw new IllegalArgumentException("Both JWT and Access Token authentication are configured. Please use only one.");
            }
            testURLFetcherBuilder.credentialProvider(jwtTokenCredentialProvider);
        }
        DefaultURLFetcher testURLFetcher = testURLFetcherBuilder.build();

        DynamicDiscoveryService smpClient = new DynamicDiscoveryService.Builder()
                .documentRequestProvider(new DefaultDocumentRequestProvider.Builder().build())
                .documentReader(new DefaultBDXRReader.Builder().build())
                .documentFetcher(testURLFetcher)
                .publisherLocator(testBDXRLocator)
                .build();

        // lookup and download data
        List<PublisherLookupResult> lookupResults = smpClient.getPublisherLocator().lookup(participantIdentifier);
        if (lookupResults == null || lookupResults.isEmpty()) {
            throw new DDCRuntimeException("Can not resolve party identifier");
        }

        IDocumentRequestProvider metadataProvider = smpClient.getDocumentRequestProvider();
        for (PublisherLookupResult lookupResult : lookupResults) {
            try {
                downloadResource(smpClient, metadataProvider, participantIdentifier, subresourceIdentifier, lookupResult, outputFilePath);
                break;
            } catch (TechnicalException e) {
                System.out.println("Error downloading resource from: " + lookupResult.getUrl() + " - " + e.getMessage());
            }
        }
    }


    protected JwtTokenCredentialProvider buildJwtTokenCredentialProvider(CommandLine cmd, KeyStore tlsTruststore, KeyStore tlsKeystore) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        String jwtAuthorizationUrl = cmd.getOptionValue(OPTIONS_JWT_AUTHORIZATION_SERVER_URL.getOption());
        if (StringUtils.isBlank(jwtAuthorizationUrl)) {
            return null; // no JWT authentication configured
        }
        String jwtClientId = cmd.getOptionValue(OPTIONS_JWT_CLIENT_ID.getOption());
        String jwtScope = cmd.getOptionValue(OPTIONS_JWT_SCOPE.getOption());
        String passwd = cmd.getOptionValue(OPTIONS_KEYSTORE_KEY_PASSWORD.getOption());
        boolean noTlsHostnameValidation = cmd.hasOption(OPTION_NO_TLS_HOSTNAME_VALIDATION.getOption());
        Args.notBlank(jwtAuthorizationUrl, "JWT authorization server URL");
        Args.notBlank(jwtClientId, "jwt claim: client id");
        Objects.requireNonNull(tlsKeystore, "tls keystore must not be null");
        Objects.requireNonNull(tlsTruststore, "tls truststore must not be null");

        JWTAuthorizationTokenFetcher fetcher = new JWTAuthorizationTokenFetcher.Builder()
                .clientId(jwtClientId)
                .scopes(jwtScope)
                .tlsKeystore(tlsKeystore, passwd.toCharArray())
                .tlsTruststore(tlsTruststore)
                .noHostnameValidation(noTlsHostnameValidation)
                .build();
// configure JWTCredentialProvider
        return new JwtTokenCredentialProvider.Builder()
                .jwtFetcher(fetcher)
                .authorizationServerURI(jwtAuthorizationUrl)
                .build();
    }

    protected void downloadResource(DynamicDiscoveryService smpClient, IDocumentRequestProvider metadataProvider,
                                    SMPParticipantIdentifier participantIdentifier, SMPDocumentIdentifier subresourceIdentifier,
                                    PublisherLookupResult lookupResult, String outputFilePath) throws TechnicalException, IOException {

        URI url;
        if (subresourceIdentifier == null) {
            url = metadataProvider.createRequestForResource(lookupResult, participantIdentifier).getResourceUri();
        } else {
            url = metadataProvider.createRequestForSubresource(lookupResult, participantIdentifier, subresourceIdentifier).getSubresourceUri();
        }
        FetcherResponse response = (FetcherResponse) smpClient.getDocumentFetcher().fetch(url);
        Files.copy(response.getInputStream(), Paths.get(outputFilePath), StandardCopyOption.REPLACE_EXISTING);
    }

    protected void runDNS(CommandLine cmd) throws TechnicalException, IOException {
        // read parameters
        SMPParticipantIdentifier participantIdentifier = getResourceIdentifier(cmd);
        String domain = cmd.getOptionValue("domain");
        List<String> naptrServices = getNaptrServices(cmd);
        List<DNSLookupType> dnsLookupTypes = getDNSLookupTypes(cmd);
        String nameserver = cmd.getOptionValue(OPTION_DNS_NAMESERVER.getOption());
        String nameserverPort  = cmd.getOptionValue(OPTION_DNS_NAMESERVER_PORT.getOption());
        Integer port = null;
        if (StringUtils.isNotEmpty(nameserverPort)) {
            port = Integer.parseInt(nameserverPort);
        }

        // configure ddc client
        DefaultDNSLookup testDNSLookup = new DefaultDNSLookup.Builder()
                .nameserverPort(port)
                .nameserver(nameserver)
                .addRequiredNaptrServices(naptrServices)
                .build();

        DefaultBDXRLocator testBDXRLocator = new DefaultBDXRLocator.Builder()
                .addTopDnsDomain(domain)
                .dnsLookup(testDNSLookup).build();
        System.out.println("Resolving DNS for participant: [" + participantIdentifier + "] and domain: ["
                + domain + "]" + System.lineSeparator());


        if (dnsLookupTypes.contains(DNSLookupType.CNAME)) {
            resolveCNameQuery(participantIdentifier, domain, testDNSLookup, testBDXRLocator);
        }
        if (dnsLookupTypes.contains(DNSLookupType.NAPTR)) {
            resolveNaptrQuery(participantIdentifier, domain, testDNSLookup, testBDXRLocator);
        }
    }


    private void resolveCNameQuery(SMPParticipantIdentifier participantIdentifier, String domain,
                                   DefaultDNSLookup testDNSLookup,
                                   DefaultBDXRLocator testBDXRLocator)
            throws TechnicalException {
        String cnameQuery = testBDXRLocator.buildCNameDNSQuery(participantIdentifier, domain);
        System.out.println("CNAME query: " + cnameQuery);
        List<Record> result = testDNSLookup.getAllRecordsForType(participantIdentifier, cnameQuery, DNSLookupType.CNAME);
        try {
            while (true) {
                if (result != null && !result.isEmpty() && result.get(0).getType() == 5) {
                    CNAMERecord record = (CNAMERecord) result.get(0);
                    System.out.println(record);
                    result = testDNSLookup.getAllRecordsForType(participantIdentifier, record.getTarget().toString(), DNSLookupType.CNAME);
                } else {
                    break;
                }
            }
        } catch (DNSLookupException e) {
            if (e.getSmpExceptionCode() != DDCExceptionCode.INVALID_DNS_TYPE) {
                throw e;
            }
        }
        System.out.println(System.lineSeparator());
    }


    private void resolveNaptrQuery(SMPParticipantIdentifier participantIdentifier, String domain,
                                   DefaultDNSLookup testDNSLookup,
                                   DefaultBDXRLocator testBDXRLocator)
            throws TechnicalException {
        String naptrQuery = testBDXRLocator.buildNaptrDNSQuery(participantIdentifier, domain);
        System.out.println("NAPTR query: " + naptrQuery);
        List<Record> result = testDNSLookup.getAllRecordsForType(participantIdentifier, naptrQuery, DNSLookupType.NAPTR);
        // print results.
        result.forEach(System.out::println);
        System.out.println(System.lineSeparator());
    }

    private List<DNSLookupType> getDNSLookupTypes(CommandLine cmd) {
        Option option = OPTION_RECORD_TYPE.getOption();
        if (cmd.hasOption(option)) {
            return stream(split(cmd.getOptionValue(option), ","))
                    .map(StringUtils::trimToNull)
                    .filter(StringUtils::isNotBlank)
                    .map(DNSLookupType::valueOf)
                    .collect(Collectors.toList());
        }
        return Collections.singletonList(DNSLookupType.NAPTR);
    }

    private AccessTokenCredentialProvider getAccessToken(CommandLine cmd) {
        Option optionATN = OPTION_ACCESS_TOKEN_NAME.getOption();
        Option optionATV = OPTION_ACCESS_TOKEN_VALUE.getOption();
        if (cmd.hasOption(optionATN) && cmd.hasOption(optionATV)) {
            return new AccessTokenCredentialProvider(cmd.getOptionValue(optionATN),
                    cmd.getOptionValue(optionATV).toCharArray());
        }
        return null;
    }

    // Return list of naptr services from command line. If not option is defined then default Meta:SMP and Meta:SMP2 are used
    private List<String> getNaptrServices(CommandLine cmd) {
        Option option = OPTION_NAPTR_SERVICE.getOption();
        if (cmd.hasOption(option)) {
            return stream(split(cmd.getOptionValue(option)))
                    .map(StringUtils::trimToNull)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private SMPParticipantIdentifier getResourceIdentifier(CommandLine cmd) {
        String identifier = StringUtils.trim(cmd.getOptionValue(OPTION_RESOURCE_IDENTIFIER.getOption()));
        String scheme = StringUtils.trim(cmd.getOptionValue(OPTION_RESOURCE_SCHEME.getOption()));

        return new SMPParticipantIdentifier(identifier, scheme);
    }

    private static KeyStore getKeystore(CommandLine cmd) {
        String keystorePath = cmd.getOptionValue(OPTION_KEYSTORE_FILEPATH.getOption());
        if (StringUtils.isBlank(keystorePath)) {
            return null;
        }

        if (StringUtils.isBlank(cmd.getOptionValue(OPTION_KEYSTORE_PASSWORD.getOption()))) {
            throw new IllegalArgumentException("Keystore password is not defined");
        }
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(Files.newInputStream(Paths.get(keystorePath)),
                    cmd.getOptionValue(OPTION_KEYSTORE_PASSWORD.getOption()).toCharArray());
            return keyStore;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error loading keystore", e);
        }
    }


    private static KeyStore getTruststore(CommandLine cmd) {
        String keystorePath = cmd.getOptionValue(OPTION_TRUSTSTORE_FILEPATH.getOption());
        if (StringUtils.isBlank(keystorePath)) {
            return null;
        }
        if (StringUtils.isBlank(cmd.getOptionValue(OPTION_TRUSTSTORE_PASSWORD.getOption()))) {
            throw new IllegalArgumentException("Truststore password is not defined");
        }
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(Files.newInputStream(Paths.get(keystorePath)),
                    cmd.getOptionValue(OPTION_TRUSTSTORE_PASSWORD.getOption()).toCharArray());
            return keyStore;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error loading truststore", e);
        }
    }
}


