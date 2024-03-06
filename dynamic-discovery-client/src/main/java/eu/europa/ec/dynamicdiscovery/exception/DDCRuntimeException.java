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
 * DDCRuntimeException is the "unchecked exception" thrown when an exceptional condition has occurred. The error
 * does not need to be declared in a method or constructor's throws clause.
 *
 * NOTE: The error must not be used in regular negative flows of the dynamic discovery process.
 *
 * @since 2.0
 * @author Joze Rihtarsic
 */
public class DDCRuntimeException extends RuntimeException {


    public DDCRuntimeException(String message) {
        super(message);
    }

    public DDCRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
