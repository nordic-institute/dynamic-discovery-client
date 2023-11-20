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
 * @author Flávio W. R. Santos
 * @since 1.0
 *
 */
public class BindException extends TechnicalException {

    public BindException(String message) {
        super(message);
    }

    public BindException(SMPExceptionCode smpExceptionCode, String message) {
        super(smpExceptionCode, message);
    }

    public BindException(String message, Throwable cause) {
        super(message, cause);
    }

    public BindException(SMPExceptionCode smpExceptionCode, String message, Throwable cause) {
        super(smpExceptionCode, message, cause);
    }
}
