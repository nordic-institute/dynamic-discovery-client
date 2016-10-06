package eu.europa.ec.dynamicdiscovery.reader;

import eu.europa.ec.dynamicdiscovery.ServiceMetadata;
import eu.europa.ec.dynamicdiscovery.exception.BindException;
import eu.europa.ec.dynamicdiscovery.fetcher.FetcherResponse;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 * @author Erlend Klakegg Bergheim - erlend.klakegg.bergheim@difi.no
 */
public class MultiReader implements IMetadataReader {
    // private static Logger logger = LoggerFactory.getLogger(MultiReader.class);
    private static final Pattern rootTagPattern = Pattern.compile("<(\\w*:{0,1}[^<?]*)>", 8);
    private static final Pattern namespacePattern = Pattern.compile("xmlns:{0,1}([a-z0-9]*)\\w*=\\w*\"(.+?)\"", 8);
    private BusdoxReader busdoxReader;
    private BdxrReader bdxrReader;

    public MultiReader() {
        super();
        busdoxReader = new BusdoxReader();
        bdxrReader = new BdxrReader();
    }

    public List<DocumentIdentifier> parseDocumentIdentifiers(FetcherResponse fetcherResponse) throws BindException {
        if (fetcherResponse.getNamespace() == null) {
            fetcherResponse = this.detect(fetcherResponse);
        }

        if ("http://busdox.org/serviceMetadata/publishing/1.0/".equalsIgnoreCase(fetcherResponse.getNamespace())) {
            return this.busdoxReader.parseDocumentIdentifiers(fetcherResponse);
        } else if ("http://docs.oasis-open.org/bdxr/ns/SMP/2014/07".equalsIgnoreCase(fetcherResponse.getNamespace())) {
            return this.bdxrReader.parseDocumentIdentifiers(fetcherResponse);
        } else {
            throw new BindException(String.format("Unknown namespace: %s", new Object[]{fetcherResponse.getNamespace()}));
        }
    }

    public ServiceMetadata parseServiceMetadata(FetcherResponse fetcherResponse) throws BindException {//, SecurityException {
        if (fetcherResponse.getNamespace() == null) {
            fetcherResponse = this.detect(fetcherResponse);
        }

        if ("http://busdox.org/serviceMetadata/publishing/1.0/".equalsIgnoreCase(fetcherResponse.getNamespace())) {
            return this.busdoxReader.parseServiceMetadata(fetcherResponse);
        } else if ("http://docs.oasis-open.org/bdxr/ns/SMP/2014/07".equalsIgnoreCase(fetcherResponse.getNamespace())) {
            return this.bdxrReader.parseServiceMetadata(fetcherResponse);
        } else {
            throw new BindException(String.format("Unknown namespace: %s", new Object[]{fetcherResponse.getNamespace()}));
        }
    }

    public FetcherResponse detect(FetcherResponse fetcherResponse) throws BindException {
        try {
            byte[] e = new byte[1024];
            fetcherResponse.getInputStream().read(e);
            Matcher matcher = rootTagPattern.matcher(new String(e));
            if (matcher.find()) {
                String rootElement = matcher.group(1).trim();
                // logger.debug("Root element: {}", rootElement);
                String rootNs = rootElement.split(" ", 2)[0].contains(":") ? rootElement.substring(0, rootElement.indexOf(":")) : "";
                // logger.debug("Namespace: {}", rootNs);
                Matcher nsMatcher = namespacePattern.matcher(rootElement);

                while (nsMatcher.find()) {
                    //   logger.debug(nsMatcher.group(0));
                    if (nsMatcher.group(1).equals(rootNs)) {
                        return new FetcherResponse(new SequenceInputStream(new ByteArrayInputStream(e), fetcherResponse.getInputStream()), nsMatcher.group(2));
                    }
                }
            }

            throw new BindException("Unable to detect namespace.");
        } catch (IOException var7) {
            throw new BindException(var7.getMessage(), var7);
        }
    }
}
