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
package eu.europa.ec.dynamicdiscovery.core.extension.impl.oasis10;

import eu.europa.ec.dynamicdiscovery.core.extension.impl.AbstractExtension;

import java.util.Arrays;

/**
 * Oasis SMP 1.0 extension providing je ServiceGroup and SignedServiceMetadata parser
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */
public class OasisSMP10Extension extends AbstractExtension {

    public static final String NAMESPACE = "http://docs.oasis-open.org/bdxr/ns/SMP/2016/05";

    final OasisSMP10ServiceGroupReader serviceGroupReader;
    final OasisSMP10ServiceMetadataReader serviceMetadataReader;

    public OasisSMP10Extension() {
        this(false);
    }

    public OasisSMP10Extension(boolean ignoreInvalidServices) {
        serviceGroupReader = new OasisSMP10ServiceGroupReader();
        serviceMetadataReader = new OasisSMP10ServiceMetadataReader(ignoreInvalidServices);
        parsers = Arrays.asList(serviceGroupReader, serviceMetadataReader);
    }

    public void setIgnoreInvalidServices(boolean ignoreInvalidServices) {
        this.serviceMetadataReader.setIgnoreInvalidServices(ignoreInvalidServices);
    }
}
