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
package eu.europa.ec.dynamicdiscovery.core.extension.impl.oasis20;

import eu.europa.ec.dynamicdiscovery.core.extension.impl.AbstractExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Oasis SMP 2.0 extension providing je ServiceGroup and SignedServiceMetadata parser
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */
public class OasisSMP20Extension extends AbstractExtension {

    public static final String DEFAULT_PUBLISHER_URL_CONTEXT = "bdxr-smp-2";
    public static final String DEFAULT_SUBRESOURCE_URL_CONTEXT = "/services";
    public static final String DEFAULT_LOOKUP_SERVICE = "oasis-bdxr-smp-2";
    public static final String FALLBACK_LOOKUP_SERVICE = "Meta:SMP";
    final List<String> serviceTypes;
    final OasisSMP20ServiceGroupReader serviceGroupReader;
    final OasisSMP20ServiceMetadataReader serviceMetadataReader;

    public OasisSMP20Extension() {
        this(false, DEFAULT_LOOKUP_SERVICE);
    }


    public OasisSMP20Extension(boolean ignoreInvalidServices, String... services) {
        serviceGroupReader = new OasisSMP20ServiceGroupReader();
        serviceMetadataReader = new OasisSMP20ServiceMetadataReader(ignoreInvalidServices);

        parsers = Arrays.asList(
                serviceGroupReader,
                serviceMetadataReader
        );
        serviceTypes = new ArrayList<>(Arrays.asList(services));
    }

    public void setIgnoreInvalidServices(boolean ignoreInvalidServices) {
        this.serviceMetadataReader.setIgnoreInvalidServices(ignoreInvalidServices);
    }

    @Override
    public List<String> lookupServices() {
        return serviceTypes;
    }

    @Override
    public String contextPath() {
        return DEFAULT_PUBLISHER_URL_CONTEXT;
    }

    @Override
    public String subContextPath() {
        return DEFAULT_SUBRESOURCE_URL_CONTEXT;
    }

    public boolean isSMP10LookupNaptrServiceEnabled() {
        return serviceTypes.contains(FALLBACK_LOOKUP_SERVICE);
    }

    /**
     * Enable or disable the lookup service for the SMP 1.0 NAPTR service: Meta:SMP as a fallback.
     *
     * @param enabled true to enable, false to disable.
     * @return true if this enabled service list changed as a result of the call
     */
    public boolean setSMP10LookupNaptrServiceEnabled(boolean enabled) {

        if (enabled && !serviceTypes.contains(FALLBACK_LOOKUP_SERVICE)) {
            return serviceTypes.add(FALLBACK_LOOKUP_SERVICE);
        } else if (!enabled) {
            return serviceTypes.remove(FALLBACK_LOOKUP_SERVICE);
        }
        return false;
    }

    @Override
    public String getExtensionIdentifier() {
        return "oasis-smp-2.0";
    }
}
