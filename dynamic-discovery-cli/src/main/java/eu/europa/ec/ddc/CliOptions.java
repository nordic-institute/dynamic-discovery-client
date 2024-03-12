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

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * Enumeration of the command line options
 */
public enum CliOptions {
    COMMAND_GET("get", false, "Command discovers and fetch metadata from SMP"),
    COMMAND_DNS("dns", false, "Command discovers SMP location"),

    OPTION_RESOURCE_IDENTIFIER("ri", "resource-identifier", true, true, "resource (party) identifier, example: 0088:98765digit"),
    OPTION_RESOURCE_SCHEME("rs", "resource-scheme", true, false, "resource (party) scheme, example: iso6523-actorid-upis"),
    OPTION_HELP("h", "help", false, false, "Prints help"),
    OPTION_SUBRESOURCE_IDENTIFIER("si", "subresource-identifier", true, false, "Subresource identifier: ex: Invoice"),
    OPTION_SUBRESOURCE_SCHEME("ss", "subresource-scheme", true, false, "Subresource scheme :org:xml"),
    OPTION_DNS_DOMAIN("d", "domain", true, false, "Network DNS domain: eq.: acc.edelivery.tech.ec.europa.eu"),
    OPTION_NAPTR_SERVICE("s", "services", true, false, "Comma separated NAPTR service value as: Meta:SMP,meta:cppa"),
    OPTION_OUTPUT("o", "output", true, false, "Output filename. If file already exists it is overwritten." +
            " If not provided, output is printed to console"),
    OPTION_RECORD_TYPE("t", "dns-type", true, false, "List of DNS record types, CNAME,NAPTR"),
    OPTION_TRUSTSTORE_FILEPATH("tf", "truststore-filepath", true, false, "TLS truststore file path"),
    OPTION_ACCESS_TOKEN_NAME("atn", "access-token-name", true, false, "Access token name"),
    OPTION_ACCESS_TOKEN_VALUE("atv", "access-token-value", true, false, "Access token value"),
    OPTION_TRUSTSTORE_PASSWORD("tp", "truststore-password", true, false, "TLS truststore password"),
    OPTIONS_TRUSTSTORE_TYPE("tt", "truststore-type", true, false, "TLS truststore type: Default PKCS12"),
    OPTION_KEYSTORE_FILEPATH("kf", "keystore-filepath", true, false, "Client TLS keystore file path"),
    OPTION_KEYSTORE_PASSWORD("kp", "keystore-password", true, false, "Client TLS keystore password"),
    OPTION_KEYSTORE_TYPE("kt", "keystore-type", true, false, "Client TLS keystore type: Default PKCS12"),
    OPTIONS_KEYSTORE_KEY_PASSWORD("kkp", "keystore-key-password", true, false, "Client TLS keystore alias password"),
    OPTIONS_SMP_URL("smp", "smp-url", true, false, "If provided, SMP URL is used instead of DNS discovery"),;
    private final Option option;

    CliOptions(String name, boolean hasArg, String desc) {

        option = org.apache.commons.cli.Option.builder(name)
                .desc(desc)
                .hasArg(hasArg)
                .desc(desc)
                .build();
    }

    CliOptions(String name, String longName, boolean hasArg, boolean required, String desc) {

        option = org.apache.commons.cli.Option.builder(name)
                .longOpt(longName)
                .desc(desc)
                .hasArg(hasArg)
                .required(required)
                .desc(desc)
                .build();
    }

    public Option getOption() {
        return option;
    }

    public String getName() {
        return option.getOpt();
    }


    /**
     * Returns the list of command line options
     *
     * @return list of command line options.
     */
    public static Options getCommandList() {
        Options options = new Options();
        options.addOption(COMMAND_GET.getOption());
        options.addOption(COMMAND_DNS.getOption());
        options.addOption(OPTION_HELP.getOption());
        return options;
    }

    public static Options getCommandDnsOptions() {
        Options options = new Options();
        // add command
        options.addOption(COMMAND_DNS.getOption());
        // add options
        options.addOption(OPTION_HELP.getOption());
        options.addOption(OPTION_RESOURCE_IDENTIFIER.getOption());
        options.addOption(OPTION_RESOURCE_SCHEME.getOption());

        options.addOption(OPTION_DNS_DOMAIN.getOption());
        options.addOption(OPTION_RECORD_TYPE.getOption());
        options.addOption(OPTION_NAPTR_SERVICE.getOption());
        return options;
    }


    public static Options getCommandGetOptions() {
        Options options = new Options();
        // add command
        options.addOption(COMMAND_GET.getOption());
        // add options
        options.addOption(OPTION_HELP.getOption());
        options.addOption(OPTION_RESOURCE_IDENTIFIER.getOption());
        options.addOption(OPTION_RESOURCE_SCHEME.getOption());
        options.addOption(OPTION_RESOURCE_IDENTIFIER.getOption());
        options.addOption(OPTION_SUBRESOURCE_IDENTIFIER.getOption());
        options.addOption(OPTION_SUBRESOURCE_SCHEME.getOption());

        options.addOption(OPTION_DNS_DOMAIN.getOption());
        options.addOption(OPTION_RECORD_TYPE.getOption());
        options.addOption(OPTION_NAPTR_SERVICE.getOption());
        options.addOption(OPTION_OUTPUT.getOption());

        options.addOption(OPTION_TRUSTSTORE_FILEPATH.getOption());
        options.addOption(OPTION_TRUSTSTORE_PASSWORD.getOption());
        options.addOption(OPTIONS_TRUSTSTORE_TYPE.getOption());
        options.addOption(OPTION_KEYSTORE_FILEPATH.getOption());
        options.addOption(OPTION_KEYSTORE_PASSWORD.getOption());
        options.addOption(OPTION_KEYSTORE_TYPE.getOption());
        options.addOption(OPTION_ACCESS_TOKEN_NAME.getOption());
        options.addOption(OPTION_ACCESS_TOKEN_VALUE.getOption());
        options.addOption(OPTIONS_SMP_URL.getOption());
        return options;
    }
}
