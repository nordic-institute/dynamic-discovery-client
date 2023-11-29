/*
 * Copyright 2017-2023 European Commission | eDelivery Dynamic Discovery Client
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence attached in file: LICENSE-EUPL-v1.2-EN.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 */
package eu.europa.ec.dynamicdiscovery.exception;

/**
 * @author Flávio W. R. Santos
 */
public abstract class TechnicalException extends Exception {

    protected SMPExceptionCode smpExceptionCode;

    protected TechnicalException(String message) {
        super(message);
    }

    protected TechnicalException(SMPExceptionCode smpExceptionCode, String message) {
        super(message);
        this.smpExceptionCode = smpExceptionCode;
    }

    protected TechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

    protected TechnicalException(SMPExceptionCode smpExceptionCode, String message, Throwable cause) {
        super(message, cause);
        this.smpExceptionCode = smpExceptionCode;
    }

    public SMPExceptionCode getSmpExceptionCode() {
        return smpExceptionCode;
    }

    public void setSmpExceptionCode(SMPExceptionCode smpExceptionCode) {
        this.smpExceptionCode = smpExceptionCode;
    }
}
