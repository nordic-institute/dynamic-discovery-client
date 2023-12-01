/*
 * Copyright 2016-2023 - European Commission | Dynamic Discovery Client
 *
 * https://ec.europa.eu/digital-building-blocks/code/projects/EDELIVERY/repos/dynamic-discovery-client/browse
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
