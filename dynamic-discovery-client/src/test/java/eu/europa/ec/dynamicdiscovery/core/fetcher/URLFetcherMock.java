/*
 * (C) Copyright 2016-2021 - European Commission | Dynamic Discovery Client
 *
 * https://ec.europa.eu/cefdigital/code/projects/EDELIVERY/repos/dynamic-discovery-client/browse
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

import eu.europa.ec.dynamicdiscovery.core.provider.WildcardUtil;
import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import eu.europa.ec.dynamicdiscovery.util.TestCaseConstants;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static eu.europa.ec.dynamicdiscovery.util.TestCaseConstants.WILDCARD_SCHEME;


/**
 * @author Flávio W. R. Santos
 * @since 1.0
 */
public class URLFetcherMock implements IMetadataFetcher {

    public enum LookupType {
        NAPTR, CNAME, STATIC;
    }

    private String smpAlias;
    private Map<String, byte[]> responses = new HashMap<>();

    public URLFetcherMock() {
    }

    public void setParameters(String resourceType, LookupType lookupType, String serviceUrl, String responseFileName, String smpAlias) throws Exception {
        byte[] bodyResponse = getBodyResponse(resourceType, responseFileName);

        if (lookupType == LookupType.CNAME && StringUtils.isEmpty(smpAlias)) {
            throw new DNSLookupException("SMP alias represented by MD5 must be not null");
        }

        responses.put(serviceUrl, bodyResponse);

        if (!StringUtils.isEmpty(smpAlias)) {
            this.smpAlias = "http://" + smpAlias + (!smpAlias.endsWith("/") ? "/" : "");
        }
    }

    public void addParameters(String resourceType, String responseFileName, String serviceUrl) throws Exception {
        byte[] bodyResponse = getBodyResponse(resourceType, responseFileName);
        responses.put(serviceUrl, bodyResponse);
    }

    protected byte[] getBodyResponse(String resourceType, String responseFileName) throws Exception {
        if (CommonUtil.OASIS_SMP_10.equals(resourceType)) {
            return CommonUtil.getContentFromOasisSMP10XmlResource(responseFileName);
        }
        if (CommonUtil.OASIS_SMP_20.equals(resourceType)) {
            return CommonUtil.getContentFromOasisSMP20XmlResource(responseFileName);
        }
        if (CommonUtil.OASIS_SMP_10.equals(resourceType)) {
            return CommonUtil.getContentFromPeppolXmlResource(responseFileName);
        }
        throw new RuntimeException("Unknown resource type [" + resourceType + "]");
    }

    public void setParameters(LookupType lookupType, String serviceUrl, String responseFileName) throws Exception {
        setParameters(CommonUtil.OASIS_SMP_10, lookupType, serviceUrl, responseFileName, null);
    }

    @Override
    public FetcherResponse fetch(URI uri) throws TechnicalException {
        return switchResponse(uri);
    }

    private FetcherResponse switchResponse(URI uri) throws TechnicalException {
        String uriPath = uri.getRawPath();

        if (!matches(uriPath)) {
            throw new DNSLookupException("Not supported.");
        }

        final byte[] responseValue = responses.get(uriPath);
        if (responseValue != null) {
            return new FetcherResponse(new ByteArrayInputStream(responseValue));
        }

        final Map.Entry<String, byte[]> defaultEntry = responses.entrySet().iterator().next();

        //default matching
        if (StringUtils.startsWithAny(uri.toString(), smpAlias, TestCaseConstants.SMP_DOMAIN_ALIAS, TestCaseConstants.SMP_DOMAIN, TestCaseConstants.SMP_STATIC_DOMAIN)) {
            return new FetcherResponse(new ByteArrayInputStream(defaultEntry.getValue()));
        }

        throw new DNSLookupException("Not supported.");
    }

    protected boolean matches(String uriPath) {
        for (Map.Entry<String, byte[]> response : responses.entrySet()) {
            final String serviceURL = response.getKey();

            //wildcard match
            if (uriPath.contains(WILDCARD_SCHEME)) {
                final String serviceURLDecoded = decodeURl(serviceURL);

                //recorded service URL does not contain wildcard scheme
                if(!serviceURLDecoded.contains(WILDCARD_SCHEME)) {
                    return false;
                }

                String uriPathDecoded = decodeURl(uriPath);
                WildcardUtil wildcardUtil = new WildcardUtil();
                final String documentIdentifierWildcardPrefix = wildcardUtil.getValueUntilWildcardCharacter(serviceURLDecoded);
                if (uriPathDecoded.contains(documentIdentifierWildcardPrefix)) {
                    return true;
                }
            } else {//exact match
                if (StringUtils.equals(uriPath, serviceURL)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static String decodeURl(String serviceURL) {
        final String serviceURLDecoded;
        try {
            serviceURLDecoded = URLDecoder.decode(serviceURL, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return serviceURLDecoded;
    }


}
