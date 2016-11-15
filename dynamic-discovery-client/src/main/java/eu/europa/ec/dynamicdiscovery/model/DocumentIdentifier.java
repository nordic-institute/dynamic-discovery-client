/*
 * (C) Copyright 2016 Dynamic Discovery Client
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
 *
 * @author Flávio W. R. Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 *
 */
package eu.europa.ec.dynamicdiscovery.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DocumentIdentifier {
    private String documentIdentifier;
    private String scheme;

    public DocumentIdentifier(String documentIdentifier, String scheme) {
        this.documentIdentifier = documentIdentifier;
        this.scheme = scheme;
    }

    public String getScheme() {
        return this.scheme;
    }

    public String getDocumentIdentifier() {
        return documentIdentifier;
    }

    public String getIdentifier() {
        return String.format("%s::%s", new Object[]{this.scheme, this.documentIdentifier});
    }

    public String urlencoded() {
        try {
            return URLEncoder.encode(String.format("%s::%s", new Object[]{this.scheme, this.documentIdentifier}), "UTF-8");
        } catch (UnsupportedEncodingException var2) {
            throw new IllegalStateException("UTF-8 not supported.");
        }
    }
}
