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
package eu.europa.ec.dynamicdiscovery.core.extension.impl;

import eu.europa.ec.dynamicdiscovery.core.extension.IExtension;
import eu.europa.ec.dynamicdiscovery.core.extension.IObjectReader;

import javax.xml.namespace.QName;
import java.util.List;

/**
 * AbstractExtension providing the common functionality for the OasisSMP 2.0 extensions.
 *
 * @author Joze Rihtarsic
 * @since 2.2
 */
public abstract class AbstractExtension implements IExtension {
    protected List<IObjectReader<?, ?>> parsers;

    @Override
    public boolean handles(QName qName, Class<?> clazz) {
        return getParser(qName, clazz) != null;
    }

    @Override
    public <T, C> IObjectReader<T, C> getParser(QName qName, Class<T> clazz) {
        return (IObjectReader<T, C>) parsers.stream()
                .filter(parser -> parser.handles(qName, clazz))
                .findFirst().orElse(null);
    }
}
