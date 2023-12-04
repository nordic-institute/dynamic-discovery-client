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


import eu.europa.ec.dynamicdiscovery.DynamicDiscovery;
import eu.europa.ec.dynamicdiscovery.DynamicDiscoveryBuilder;
import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.impl.DefaultDNSLookup;
import eu.europa.ec.dynamicdiscovery.core.locator.impl.DefaultBDXRLocator;
import eu.europa.ec.dynamicdiscovery.core.provider.impl.DefaultProvider;
import eu.europa.ec.dynamicdiscovery.core.reader.impl.DefaultBDXRReader;
import eu.europa.ec.dynamicdiscovery.enums.DNSLookupType;
import eu.europa.ec.dynamicdiscovery.exception.DDCRuntimeException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.apache.commons.lang3.StringUtils.split;

public class DdcMain {
    public static void main(String[] args) {

        DdcMain ddc = new DdcMain();
        Options options = ddc.getOptions();

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;//not a good practice, it serves it purpose

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(1);
        }

        try {
            ddc.run(cmd);
        } catch (TechnicalException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method returns all options
     *
     * @return
     */
    protected Options getOptions() {
        Options options = new Options();

        Option partyIdentifier = new Option("pi", "party-identifier", true, "party identifier: ex: 0088:98765digit");
        partyIdentifier.setRequired(true);
        options.addOption(partyIdentifier);

        Option partyScheme = new Option("ps", "party-scheme", true, "party identifier: iso6523-actorid-upis ");
        partyScheme.setRequired(false);
        options.addOption(partyScheme);

        Option domain = new Option("d", "domain", true, "Network DNS domain: eq.: acc.edelivery.tech.ec.europa.eu ");
        domain.setRequired(true);
        options.addOption(domain);

        Option service = new Option("s", "services", true, "Comma separated NAPTR service value as: Meta:SMP,meta:cppa");
        service.setRequired(false);
        options.addOption(service);
/*
        Option dnsLookupTypes = new Option("r", "lookup-type", true, "List of DNS record types, CNAME,NAPTR");
        dnsLookupTypes.setRequired(false);
        options.addOption(dnsLookupTypes);
*/
        Option output = new Option("o", "output", true, "output file");
        output.setRequired(false);
        options.addOption(output);

        return options;
    }

    protected void run(CommandLine cmd) throws TechnicalException, IOException {
        // read parameters
        List<String> services = stream(split(cmd.getOptionValue("services")))
                .map(StringUtils::trimToNull)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        String identifier = StringUtils.trim(cmd.getOptionValue("party-identifier"));
        String scheme = StringUtils.trim(cmd.getOptionValue("party-scheme"));
        SMPParticipantIdentifier participantIdentifier = new SMPParticipantIdentifier(identifier, scheme);

        String domain = cmd.getOptionValue("domain");
        String outputFilePath = cmd.getOptionValue("output");
        outputFilePath = StringUtils.isBlank(outputFilePath) ? identifier + ".xml" : outputFilePath;


        // configure ddc client
        DefaultDNSLookup testDNSLookup = new DefaultDNSLookup.Builder()
                .addRequiredNaptrServices(services)
                .build();

        DefaultBDXRLocator testBDXRLocator = new DefaultBDXRLocator.Builder()
                .addTopDnsDomain(domain)
                .addDnsLookupType(DNSLookupType.NAPTR)
                .dnsLookup(testDNSLookup).build();

        DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                .provider(new DefaultProvider())
                .reader(new DefaultBDXRReader(null))
                .locator(testBDXRLocator)
                .build();

        // lookup and download data
        URI uri = smpClient.getService().getMetadataLocator().lookup(identifier, scheme);
        if (uri ==null) {
            throw new DDCRuntimeException("Can not resolve party identifier");
        }
        uri = smpClient.getService().getMetadataProvider().resolveForParticipantIdentifier(uri, participantIdentifier);

        FetcherResponse response = smpClient.getService().getMetadataFetcher().fetch(uri);
        Files.copy(response.getInputStream(), Paths.get(outputFilePath), StandardCopyOption.REPLACE_EXISTING);
    }
}


