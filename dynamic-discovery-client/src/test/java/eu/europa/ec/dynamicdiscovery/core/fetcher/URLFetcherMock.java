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

import eu.europa.ec.dynamicdiscovery.exception.DNSLookupException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.util.CommonUtil;
import eu.europa.ec.dynamicdiscovery.util.TestCaseConstants;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.net.URI;


/**
 * @author Flávio W. R. Santos
 * @since 1.0
 */
public class URLFetcherMock implements IMetadataFetcher {

    public enum LookupType {
        NAPTR, CNAME, STATIC;
    }

    private byte[] bodyResponse;
    private String smpAlias;
    private String serviceUrl;

    public URLFetcherMock() {
    }

    public void setParameters(LookupType lookupType, String serviceUrl, String responseFileName, String smpAlias) throws Exception {

        this.bodyResponse = CommonUtil.getContentFromOasisSMP10XmlResource(responseFileName);
        this.serviceUrl = serviceUrl;

        if (lookupType == LookupType.CNAME && StringUtils.isEmpty(smpAlias)) {
            throw new DNSLookupException("SMP alias represented by MD5 must be not null");
        }
        if (!StringUtils.isEmpty(smpAlias)) {
            this.smpAlias = "http://" + smpAlias + (!smpAlias.endsWith("/") ? "/" : "");
        }
    }

    public void setParameters(LookupType lookupType, String serviceUrl, String responseFileName) throws Exception {
        setParameters(lookupType, serviceUrl, responseFileName, null);
    }

    @Override
    public FetcherResponse fetch(URI uri) throws TechnicalException {
        return switchResponse(uri);
    }

    private FetcherResponse switchResponse(URI uri) throws TechnicalException {
        String uriPath = uri.getRawPath();
        if (!StringUtils.equals(uriPath, serviceUrl)) {
            throw new DNSLookupException("Not supported.");
        }

        if (StringUtils.startsWithAny(uri.toString(), smpAlias, TestCaseConstants.SMP_DOMAIN_ALIAS, TestCaseConstants.SMP_DOMAIN, TestCaseConstants.SMP_STATIC_DOMAIN)) {
            return new FetcherResponse(new ByteArrayInputStream(bodyResponse));
        }

        throw new DNSLookupException("Not supported.");

    }

}
