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
