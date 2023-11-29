/*
 * Copyright 2017-2023 European Commission | eDelivery Dynamic Discovery Client
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence attached in file: LICENSE-EUPL-v1.2-EN.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 */
package eu.europa.ec.dynamicdiscovery.util;

import eu.europa.ec.dynamicdiscovery.core.fetcher.FetcherResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * This class provides static utility methods for input/output operations, to replace other similar libraries
 * as: org.apache.commons.io.IOUtils to reduce dependent libraries
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */
public class IOUtils {
    private static final int BUFFER_LENGTH = 8192;

    protected IOUtils() {
    }

    /**
     * Method  reads the response http body  and returns it as bytearray. The method is not intended for large
     * payloads because they are stored in the memory, also the limit is the size of byte.length!
     * If large payloads are expected consider the streaming approach
     *
     * @param fetcherResponse the response in the HTTP request
     * @return body bytearray response
     * @throws IOException
     */
    public static byte[] readResponseData(FetcherResponse fetcherResponse) throws IOException {

        InputStream inputStream = fetcherResponse.getInputStream();
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            copy(inputStream, baos);
            return baos.toByteArray();
        }
    }

    /**
     * Copies bytes from an {@code InputStream} to an {@code OutputStream}.
     * <p>
     * This method buffers the input internally, so there is no need to use a {@code BufferedInputStream}.
     * </p>
     *
     * @param inputStream  the {@code InputStream} to read.
     * @param outputStream the {@code OutputStream} to write.
     * @return the number of bytes copied, or -1 if greater than {@link Integer#MAX_VALUE}.
     * @throws NullPointerException if the InputStream is {@code null}.
     * @throws NullPointerException if the OutputStream is {@code null}.
     * @throws IOException          if an I/O error occurs.
     */
    public static long copy(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        Objects.requireNonNull(inputStream, "inputStream");
        Objects.requireNonNull(outputStream, "outputStream");

        byte[] buf = new byte[BUFFER_LENGTH];
        int length;
        long copiedLength = 0;
        while ((length = inputStream.read(buf)) != -1) {
            outputStream.write(buf, 0, length);
            copiedLength += length;
        }
        return copiedLength;
    }
}
