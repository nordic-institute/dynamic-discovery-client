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
package eu.europa.ec.dynamicdiscovery.util;

import eu.europa.ec.dynamicdiscovery.core.reader.impl.AbstractXMLResponseReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import java.io.*;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

/**
 * @author Flávio W. R. Santos
 */
public class CommonUtil {

    private static final ThreadLocal<DocumentBuilder> threadLocalDocumentBuilder = ThreadLocal.withInitial(() -> AbstractXMLResponseReader.createDocumentBuilder());
    static final Logger LOG = LoggerFactory.getLogger(CommonUtil.class);
    static final String ROOT_RESPONSE = "/response";
    static final String OASIS_SMP_10 = "/oasis-smp-1.0";
    static final String OASIS_SMP_20 = "/oasis-smp-2.0";

    public static String getResourcePath(String name, String standard) {
        return ROOT_RESPONSE + standard + "/" + name + ".xml";
    }

    public static InputStream getISForName(String name, String standard) {
        String path = getResourcePath(name, standard);
        LOG.debug("Get resource from path: [{}]", path);
        return CommonUtil.class.getResourceAsStream(path);

    }

    public static byte[] getContentFromOasisSMP10XmlResource(String fileName) throws Exception {
        try (InputStream is = getInputStreamFromOasisSMP10XmlResource(fileName)) {
            return readAllBytes(is);
        }
    }

    public static Document getOasisSMP10DocumentFromXmlFile(String fileName) throws Exception {
        try (InputStream is = getInputStreamFromOasisSMP10XmlResource(fileName)) {
            return threadLocalDocumentBuilder.get().parse(is);
        }
    }

    public static Document getOasisSMP20DocumentFromXmlFile(String fileName) throws Exception {
        try (InputStream is = getInputStreamFromOasisSMP20XmlResource(fileName)) {
            return threadLocalDocumentBuilder.get().parse(is);
        }
    }

    public static String getStringFromXmlFile(String fileName) throws Exception {
        return getStringFromXmlBytes(getContentFromOasisSMP10XmlResource(fileName));
    }

    public static String getStringFromXmlBytes(byte[] ba) throws Exception {
        return new String(ba, "UTF-8");
    }

    public static InputStream getInputStreamFromOasisSMP10XmlResource(String fileName) throws Exception {
        return getISForName(fileName, OASIS_SMP_10);
    }

    public static InputStream getInputStreamFromOasisSMP20XmlResource(String fileName) throws Exception {
        return getISForName(fileName, OASIS_SMP_20);
    }

    public static KeyStore loadTrustStore(String fileName) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(Thread.currentThread().getContextClassLoader().getResource(fileName).getFile()), null);
        return keyStore;
    }

    public static Certificate loadCertificate(String certFilename) throws IOException, CertificateException {
        InputStream fis = Thread.currentThread().getContextClassLoader().getResource(certFilename).openStream();
        BufferedInputStream bis = new BufferedInputStream(fis);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        return cf.generateCertificate(bis);
    }

    public static byte[] readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, bos);
        return bos.toByteArray();
    }
}
