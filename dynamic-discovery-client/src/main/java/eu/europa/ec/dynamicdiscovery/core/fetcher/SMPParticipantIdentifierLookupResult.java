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
