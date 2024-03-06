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
package eu.europa.ec.dynamicdiscovery.core.security.impl;

import eu.europa.ec.dynamicdiscovery.core.security.ICredentialProvider;
import org.apache.hc.client5.http.auth.Credentials;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;

/**
 * A credential provider for managing the access token credentials.
 *
 * @author Joze Rihtarsic
 * @since 2.1
 */
public class AccessTokenCredentialProvider implements ICredentialProvider {

    UsernamePasswordCredentials credentials;


    public AccessTokenCredentialProvider(String accessTokenName, char[] accessTokenValue) {
        credentials = new UsernamePasswordCredentials(accessTokenName,accessTokenValue);
    }

    public Credentials getCredentials() {
        return credentials;
    }
}
