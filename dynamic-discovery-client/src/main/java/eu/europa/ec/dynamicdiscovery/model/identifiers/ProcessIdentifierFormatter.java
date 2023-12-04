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
package eu.europa.ec.dynamicdiscovery.model.identifiers;

/**
 * Formatter for the ProcessIdentifier with default null split regular expression and
 * '::' as split separator. For details see the {@link AbstractIdentifierFormatter}
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */
public class ProcessIdentifierFormatter extends AbstractIdentifierFormatter<SMPProcessIdentifier> {


    @Override
    protected String getSchemeFromObject(SMPProcessIdentifier object) {
        return object != null ? object.getScheme() : null;
    }

    @Override
    protected String getIdentifierFromObject(SMPProcessIdentifier object) {
        return object != null ? object.getIdentifier() : null;
    }

    @Override
    protected SMPProcessIdentifier createObject(String scheme, String identifier) {
        return new SMPProcessIdentifier(identifier, scheme);
    }

    @Override
    protected void updateObject(SMPProcessIdentifier identifierObject, String scheme, String identifier) {
        identifierObject.setScheme(scheme);
        identifierObject.setIdentifier(identifier);
    }
}
