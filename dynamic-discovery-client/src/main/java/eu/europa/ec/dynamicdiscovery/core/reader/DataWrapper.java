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
package eu.europa.ec.dynamicdiscovery.core.reader;

/**
 * Interface for classes which provide the common discovered SMP data: ServiceGroup, ServiceMetadata using various standards
 * This interface describes a standard mechanism to access wrapped resources represented by their proxy, to permit direct access to the resource delegates.
 *
 * @author Joze Rihtarsic
 * @Since: 2.0
 */

public interface DataWrapper {
    /**
     * The method returns the source object for the proxy class. The access to the source object allows access to
     * non-standard methods and data not exposed by the proxy class.
     *
     * @param iface The  expected class of the source object.
     * @return an object that implements the class. If the source object can not be cast to give class, null is returned.
     */
    <T> T unwrap(java.lang.Class<T> iface);

    /**
     * Returns true if source data object implements the class.
     *
     * @param iface a Class defining the source object.
     * @return true if source object can be cast to give class.
     */
    boolean isWrapperFor(java.lang.Class<?> iface);
}
