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
package eu.europa.ec.dynamicdiscovery.core.fetcher;

import java.net.URI;

/**
 * @author Cosmin Baciu
 * @since 2.1
 */
public class SMPParticipantIdentifierLookupResult {

    protected URI smpURI;
    protected URI participantUnderSmpURI;

    protected FetcherResponse fetcherResponse;

    public SMPParticipantIdentifierLookupResult() {
    }

    public SMPParticipantIdentifierLookupResult(URI smpURI, URI participantUnderSmpURI, FetcherResponse fetcherResponse) {
        this.smpURI = smpURI;
        this.participantUnderSmpURI = participantUnderSmpURI;
        this.fetcherResponse = fetcherResponse;
    }

    public URI getSmpURI() {
        return smpURI;
    }

    public void setSmpURI(URI smpURI) {
        this.smpURI = smpURI;
    }

    public URI getParticipantUnderSmpURI() {
        return participantUnderSmpURI;
    }

    public void setParticipantUnderSmpURI(URI participantUnderSmpURI) {
        this.participantUnderSmpURI = participantUnderSmpURI;
    }

    public FetcherResponse getFetcherResponse() {
        return fetcherResponse;
    }

    public void setFetcherResponse(FetcherResponse fetcherResponse) {
        this.fetcherResponse = fetcherResponse;
    }
}
