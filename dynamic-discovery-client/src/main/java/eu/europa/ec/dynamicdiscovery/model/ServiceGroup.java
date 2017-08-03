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
 *
 */
package eu.europa.ec.dynamicdiscovery.model;

import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import org.oasis_open.docs.bdxr.ns.smp._2016._05.ServiceGroupType;

import java.util.List;

public class ServiceGroup {

    private ServiceGroupType serviceGroupType;
    private List<DocumentIdentifier> documentIdentifiers;

    public ServiceGroup(ServiceGroupType serviceGroupType, List<DocumentIdentifier> documentIdentifiers) throws TechnicalException {
        if (serviceGroupType == null) {
            throw new IllegalStateException("ServiceInformationType must be not null");
        }
        this.documentIdentifiers = documentIdentifiers;
        this.serviceGroupType = serviceGroupType;
    }

    public ServiceGroupType getOriginalServiceGroup() throws TechnicalException {
        return serviceGroupType;
    }

    public List<DocumentIdentifier> getDocumentIdentifiers() {
        return documentIdentifiers;
    }
}