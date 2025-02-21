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
package eu.europa.ec.dynamicdiscovery.core.reader;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.core.security.SignatureValidationContext;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;

/**
 * The implementation of this interface should be able to read the data from the FetcherResponse
 * and return the SMPServiceGroup or SMPServiceMetadata object. It should also be able to validate the
 * signature of the response if data is signed. When the list of trusted certificates is provided, the
 * implementation must validate the signature certificate against the trusted list. The list
 * is provided for the redirection cases.
 *
 * @author Flávio W. R. Santos
 * @author Erlend Klakegg Bergheim
 * @author Joze Rihtarsic
 * @since 1.0
 */
public interface IDocumentReader<R, S> {

    default R getResource(FetcherResponse fetcherResponse) throws TechnicalException {
        return getResource(fetcherResponse, null);
    }

    R getResource(FetcherResponse fetcherResponse, SignatureValidationContext context) throws TechnicalException;

    default S getSubresource(FetcherResponse fetcherResponse) throws TechnicalException {
        return getSubresource(fetcherResponse, null);
    }

    S getSubresource(FetcherResponse fetcherResponse, SignatureValidationContext context) throws TechnicalException;
}

