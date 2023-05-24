/*
 * (C) Copyright 2016-2021 - European Commission | Dynamic Discovery Client
 *
 * https://ec.europa.eu/cefdigital/code/projects/EDELIVERY/repos/dynamic-discovery-client/browse
 *
 * Licensed under the LGPL, Version 2.1 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     dynamic-discovery\License_LGPL-2.1.txt or https://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.europa.ec.dynamicdiscovery.core.security;

import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.core5.http.HttpHost;

/**
 * A configuration that grants access to proxy details such as the proxy host and its credentials.
 *
 * @author Flávio W. R. Santos
 * @author Sebastian-Ion TINCU
 * @since 1.13
 */
public interface IProxyConfiguration {

    /**
     * Checks whether the proxy is going to be enabled or not for the target host passed in as a parameter.
     *
     * @param target The target host {@code String} representation
     * @return {@code true}, if the proxy is not going to be used for this host; {@code false}, otherwise.
     */
    boolean isNonProxyHost(String target);

    /**
     * Returns the host details of the proxy used for the target host passed in as a parameter.
     *
     * @param target The target host {@code String} representation
     * @return the host of the proxy; {@code null} otherwise, when the target host is a non-proxy one
     * @see #isNonProxyHost(String)
     */
    HttpHost getProxyHost(String target);

    /**
     * Returns the credentials of the proxy used for the target host passed in as a parameter.
     *
     * @param target The target host {@code String} representation
     * @return the credentials used to identify against the proxy; {@code null} otherwise, when the user is not provided
     * or when the target host is a non-proxy one
     * @see #isNonProxyHost(String)
     */
    CredentialsProvider getProxyCredentials(String target);

}
