/*
 * (C) Copyright 2016 - European Commission | Dynamic Discovery Client
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
 * @author Erlend Klakegg Bergheim - erlend.klakegg.bergheim@difi.no
 *
 */
package eu.europa.ec.dynamicdiscovery.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.ExtensionType;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.security.cert.X509Certificate;

/**
 * @deprecated Replaced by {@link org.oasis_open.docs.bdxr.ns.smp._2016._05.ExtensionType}
 */
@Deprecated
public class Extension {

    private String extensionID;
    private String extensionName;
    private String extensionAgencyID;
    private String extensionAgencyName;
    private String extensionAgencyURI;
    private String extensionVersionID;
    private String extensionURI;
    private String extensionReasonCode;
    private String extensionReason;
    private Object any;

    public Extension(ExtensionType extensionType) {
        this.extensionID = extensionType.getExtensionID();
        this.extensionName = extensionType.getExtensionName();
        this.extensionAgencyID = extensionType.getExtensionAgencyID();
        this.extensionAgencyName = extensionType.getExtensionAgencyName();
        this.extensionAgencyURI = extensionType.getExtensionAgencyURI();
        this.extensionVersionID = extensionType.getExtensionVersionID();
        this.extensionURI = extensionType.getExtensionURI();
        this.extensionVersionID = extensionType.getExtensionVersionID();
        this.extensionReasonCode = extensionType.getExtensionReasonCode();
        this.extensionReason = extensionType.getExtensionReason();
        this.any = extensionType.getAny();
    }

    public String getExtensionID() {
        return extensionID;
    }

    public String getExtensionName() {
        return extensionName;
    }

    public String getExtensionAgencyID() {
        return extensionAgencyID;
    }

    public String getExtensionAgencyName() {
        return extensionAgencyName;
    }

    public String getExtensionAgencyURI() {
        return extensionAgencyURI;
    }

    public String getExtensionVersionID() {
        return extensionVersionID;
    }

    public String getExtensionURI() {
        return extensionURI;
    }

    public String getExtensionReasonCode() {
        return extensionReasonCode;
    }

    public String getExtensionReason() {
        return extensionReason;
    }

    public Object getAny() {
        return any;
    }

    public void setAny(Object any) {
        this.any = any;
    }

    @Override
    public String toString() {
        return "Extension{" +
                "extensionID=" + extensionID +
                ",extensionName=" + extensionName +
                ",extensionAgencyID='" + extensionAgencyID + '\'' +
                ",extensionAgencyName=" + extensionAgencyName +
                ",extensionAgencyURI=" + extensionAgencyURI +
                ",extensionVersionID=" + extensionVersionID +
                ",extensionURI=" + extensionURI +
                ",extensionReasonCode=" + extensionReasonCode +
                "extensionReason=" + extensionReason +
                ",Object=" + any +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Extension) {
            Extension endpoint = (Extension) obj;
            return new EqualsBuilder()
                    .append(extensionID, endpoint.getExtensionID())
                    .append(extensionName, endpoint.getExtensionName())
                    .append(extensionAgencyID, endpoint.getExtensionAgencyID())
                    .append(extensionAgencyName, endpoint.getExtensionAgencyName())
                    .append(extensionAgencyURI, endpoint.getExtensionAgencyURI())
                    .append(extensionVersionID, endpoint.getExtensionVersionID())
                    .append(extensionURI, endpoint.getExtensionURI())
                    .append(extensionReasonCode, endpoint.getExtensionReasonCode())
                    .append(extensionReason, endpoint.getExtensionReason())
                    .isEquals();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(extensionID)
                .append(extensionName)
                .append(extensionAgencyID)
                .append(extensionAgencyName)
                .append(extensionAgencyURI)
                .append(extensionVersionID)
                .append(extensionURI)
                .append(extensionReasonCode)
                .append(extensionReason)
                .toHashCode();
    }
}
