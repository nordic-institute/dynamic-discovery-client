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
import java.util.Collections;
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
    List<String> serviceTypes;
    String contextPath;
    String subContextPath;
    OasisSMP20ServiceGroupReader serviceGroupReader;
    OasisSMP20ServiceMetadataReader serviceMetadataReader;

    /**
     * Default constructor with default lookup service: oasis-bdxr-smp-2 and Meta:SMP
     * and default context paths: bdxr-smp-2 and subcontext /services
     * and ignoring invalid services set to true.
     */
    public OasisSMP20Extension() {
        this.contextPath = DEFAULT_PUBLISHER_URL_CONTEXT;
        this.subContextPath = DEFAULT_SUBRESOURCE_URL_CONTEXT;
        this.serviceTypes = new ArrayList<>(Collections.singletonList(DEFAULT_LOOKUP_SERVICE));
        initParsers(true);
    }


    /**
     * Constructor allowing to fine tune the extension configuration
     * @param builder the builder with the configuration options
     */
    private OasisSMP20Extension(Builder builder) {
        this.contextPath = builder.contextPath != null ? builder.contextPath : DEFAULT_PUBLISHER_URL_CONTEXT;
        this.subContextPath = builder.subContextPath != null ? builder.subContextPath : DEFAULT_SUBRESOURCE_URL_CONTEXT;
        this.serviceTypes = new ArrayList<>(builder.naprServices);
        if (this.serviceTypes.isEmpty()) {
            this.serviceTypes.add(DEFAULT_LOOKUP_SERVICE);
        }
        initParsers(builder.ignoreInvalidServices);
    }

    protected void initParsers(boolean ignoreInvalidServices) {
        this.serviceMetadataReader = new OasisSMP20ServiceMetadataReader(ignoreInvalidServices);
        this.serviceGroupReader = new OasisSMP20ServiceGroupReader();
        parsers = Arrays.asList(
                serviceGroupReader,
                serviceMetadataReader
        );
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
        return contextPath;
    }

    @Override
    public String subContextPath() {
        return subContextPath;
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

    /**
     * Builder for {@link OasisSMP20Extension}
     */
    public static class Builder {
        private boolean ignoreInvalidServices = false;
        private List<String> naprServices = new ArrayList<>();
        private String contextPath;
        private String subContextPath;

        public Builder ignoreInvalidServices(boolean ignoreInvalidServices) {
            this.ignoreInvalidServices = ignoreInvalidServices;
            return this;
        }

        public Builder addNaptrService(String service) {
            this.naprServices.add(service);
            return this;
        }

        public Builder addNaptrServices(String ... services) {
            this.naprServices.addAll(Arrays.asList(services));
            return this;
        }
        public Builder contextPath(String contextPath) {
            this.contextPath = contextPath;
            return this;
        }
        public Builder subContextPath(String subContextPath) {
            this.subContextPath = subContextPath;
            return this;
        }

        public OasisSMP20Extension build() {
            return new OasisSMP20Extension(this);
        }
    }

}
