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
package eu.europa.ec.dynamicdiscovery.core.fetcher;

import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;

import java.net.URI;

/**
 * This interface is responsible for fetching the document from the URI address.
 * The implementation takes care of URI scheme specific handling (e.g. HTTP, HTTPS, etc.),
 * proxy settings, authentication, and other network related settings.
 *
 * @author Flávio W. R. Santos
 * @author Erlend Klakegg Bergheim
 * @since 1.0
 */
public interface IDocumentFetcher {

    /**
     * Fetches the metadata from the SMP server. The response is returned as a {@link FetcherResponse}.
     * which contains input stream of the response.
     *
     * @param documentURI the URI of the document to fetch.
     * @return the response {@link FetcherResponse} from the SMP server
     * @throws TechnicalException if any error occurs during the fetch
     */
    IFetcherResponse fetch(URI documentURI) throws TechnicalException;
}
