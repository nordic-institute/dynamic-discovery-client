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
