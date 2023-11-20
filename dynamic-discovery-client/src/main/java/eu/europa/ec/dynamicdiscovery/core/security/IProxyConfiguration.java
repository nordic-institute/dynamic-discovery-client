/*
 * Copyright 2017-2023 European Commission | CEF eDelivery
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence attached in file: LICENCE-EUPL-v1.2.pdf
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
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
