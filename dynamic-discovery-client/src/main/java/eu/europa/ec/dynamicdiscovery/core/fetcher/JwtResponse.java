/*
 * #%L
 * dynamic-discovery-cli
 * %%
 * Copyright (C) 2025 - 2025 European Commission | eDelivery | Dynamic Discovery Client
 * %%
 * Licensed under the LGPL, Version 2.1 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * [PROJECT_HOME]\license\lgpl2-1\license.txt or https://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package eu.europa.ec.dynamicdiscovery.core.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europa.ec.dynamicdiscovery.core.security.impl.JwtCredentials;
import org.slf4j.Logger;

import java.io.InputStream;

/**
 * Represents a response containing JWT credentials.
 * This class is responsible for reading the JWT credentials from an InputStream
 * and providing access to them.
 *
 * @author Joze Rihtarsic
 * @since 3.1
 */
public class JwtResponse implements IFetcherResponse {
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(JwtResponse.class);

    private JwtCredentials jwtCredentials;
    private final InputStream inputStream;

    public JwtResponse(InputStream stream) {
        this.inputStream = stream;
        this.initialize();
    }

    protected void initialize() {
        if (inputStream == null) {
            LOG.warn("InputStream is null, cannot initialize JwtResponse.");

        }
        try (InputStream is = inputStream) {
            ObjectMapper objectMapper = new ObjectMapper();

            // Read JSON file and deserialize into JwtCredentials object
            jwtCredentials = objectMapper.readValue(
                    is,
                    JwtCredentials.class
            );

        } catch (Exception e) {
            LOG.error("Error reading JWT credentials from InputStream", e);
            jwtCredentials = null;
        }
    }

    public JwtCredentials getJwtCredentials() {
        return jwtCredentials;
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public String toString() {
        return "JwtResponse{" +
                "jwtToken='" + jwtCredentials + '\'' +
                ", inputStream=" + inputStream +
                '}';
    }
}
