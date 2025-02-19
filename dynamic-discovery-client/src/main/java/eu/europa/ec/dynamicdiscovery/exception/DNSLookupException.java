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
package eu.europa.ec.dynamicdiscovery.exception;

/**
 * @author Flávio W. R. Santos
 */
public class DNSLookupException extends TechnicalException {

    public DNSLookupException(String message) {
        this(DDCExceptionCode.DNS_QUERY_ERROR, message);
    }

    public DNSLookupException(DDCExceptionCode smpExceptionCode, String message) {
        super(smpExceptionCode, message);
    }

    public DNSLookupException(String message, Throwable cause) {
        super(message, cause);
    }

    public DNSLookupException(DDCExceptionCode smpExceptionCode, String message, Throwable cause) {
        super(smpExceptionCode, message, cause);
    }
}
