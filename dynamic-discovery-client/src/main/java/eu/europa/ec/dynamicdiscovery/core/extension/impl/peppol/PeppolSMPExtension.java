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
package eu.europa.ec.dynamicdiscovery.core.extension.impl.peppol;

import eu.europa.ec.dynamicdiscovery.core.extension.impl.AbstractExtension;
import eu.europa.ec.dynamicdiscovery.core.extension.impl.oasis10.OasisSMP10Extension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Peppol SMP extension providing the ServiceGroup and SignedServiceMetadata parser
 *
 * @author Cosmin Baciu
 * @since 2.1
 */
public class PeppolSMPExtension extends AbstractExtension {

    public static final String NAMESPACE = "http://busdox.org/serviceMetadata/publishing/1.0/";
    public static final String DEFAULT_PUBLISHER_URL_CONTEXT = "/";
    public static final String DEFAULT_SUBRESOURCE_URL_CONTEXT = "/services";
    public static final String DEFAULT_LOOKUP_SERVICE = "Meta:SMP";

    List<String> serviceTypes;
    String contextPath;
    String subContextPath;
    PeppolSMPServiceGroupReader serviceGroupReader;
    PeppolSMPServiceMetadataReader serviceMetadataReader;

    public PeppolSMPExtension() {
        serviceTypes = Collections.singletonList(DEFAULT_LOOKUP_SERVICE);
        contextPath = DEFAULT_PUBLISHER_URL_CONTEXT;
        subContextPath = DEFAULT_SUBRESOURCE_URL_CONTEXT;
        initParsers(false);
    }

    protected PeppolSMPExtension(PeppolSMPExtension.Builder builder) {
        this.contextPath = builder.contextPath != null ? builder.contextPath : DEFAULT_PUBLISHER_URL_CONTEXT;
        this.subContextPath = builder.subContextPath != null ? builder.subContextPath : DEFAULT_SUBRESOURCE_URL_CONTEXT;
        this.serviceTypes = new ArrayList<>(builder.naprServices);
        if (this.serviceTypes.isEmpty()) {
            this.serviceTypes.add(DEFAULT_LOOKUP_SERVICE);
        }
        initParsers(builder.ignoreInvalidServices);
    }

    protected void initParsers(boolean ignoreInvalidServices) {
        serviceGroupReader = new PeppolSMPServiceGroupReader();
        serviceMetadataReader = new PeppolSMPServiceMetadataReader(ignoreInvalidServices);
        parsers = Arrays.asList(serviceGroupReader, serviceMetadataReader);
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

    @Override
    public String getExtensionIdentifier() {
        return "peppol-smp-1";
    }

    /**
     * Builder for {@link OasisSMP10Extension}
     */
    public static class Builder {
        private boolean ignoreInvalidServices = false;
        private List<String> naprServices = new ArrayList<>();
        private String contextPath;
        private String subContextPath;

        public PeppolSMPExtension.Builder ignoreInvalidServices(boolean ignoreInvalidServices) {
            this.ignoreInvalidServices = ignoreInvalidServices;
            return this;
        }

        public PeppolSMPExtension.Builder addNaptrService(String service) {
            this.naprServices.add(service);
            return this;
        }

        public PeppolSMPExtension.Builder addNaptrServices(String... services) {
            this.naprServices.addAll(Arrays.asList(services));
            return this;
        }

        public PeppolSMPExtension.Builder contextPath(String contextPath) {
            this.contextPath = contextPath;
            return this;
        }

        public PeppolSMPExtension.Builder subContextPath(String subContextPath) {
            this.subContextPath = subContextPath;
            return this;
        }

        public PeppolSMPExtension build() {
            return new PeppolSMPExtension(this);
        }
    }
}
