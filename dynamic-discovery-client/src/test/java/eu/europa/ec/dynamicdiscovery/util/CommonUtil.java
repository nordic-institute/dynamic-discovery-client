/*
 * #%L
 * dynamic-discovery-cli
 * %%
 * Copyright (C) 2016 - 2023 European Commission | eDelivery | Dynamic Discovery Client
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
package eu.europa.ec.dynamicdiscovery.util;

import eu.europa.ec.dynamicdiscovery.core.reader.impl.AbstractXMLResponseReader;
import org.junit.platform.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

/**
 * @author Flávio W. R. Santos
 */
public class CommonUtil {

    private static final ThreadLocal<DocumentBuilder> threadLocalDocumentBuilder = ThreadLocal.withInitial(AbstractXMLResponseReader::createDocumentBuilder);
    static final Logger LOG = LoggerFactory.getLogger(CommonUtil.class);
    static final String ROOT_RESPONSE = "/response";
    public static final String OASIS_SMP_10 = "oasis-smp-1.0";
    public static final String OASIS_SMP_20 = "oasis-smp-2.0";
    public static final String PEPPOL = "peppol";

    public static String getResourcePath(String name, String standard) {
        return ROOT_RESPONSE + "/" + standard + "/" + name + ".xml";
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

    public static byte[] getContentFromOasisSMP20XmlResource(String fileName) throws Exception {
        try (InputStream is = getInputStreamFromOasisSMP20XmlResource(fileName)) {
            return readAllBytes(is);
        }
    }

    public static byte[] getContentFromPeppolXmlResource(String fileName) throws Exception {
        try (InputStream is = getInputStreamFromPeppolResource(fileName)) {
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
        return new String(ba, StandardCharsets.UTF_8);
    }

    public static InputStream getInputStreamFromOasisSMP10XmlResource(String fileName) {
        return getISForName(fileName, OASIS_SMP_10);
    }

    public static InputStream getInputStreamFromOasisSMP20XmlResource(String fileName) {
        return getISForName(fileName, OASIS_SMP_20);
    }

    public static InputStream getInputStreamFromPeppolResource(String fileName) {
        return getISForName(fileName, PEPPOL);
    }

    public static KeyStore loadTrustStore(String fileName) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        final InputStream resourceAsStream = CommonUtil.class.getClassLoader().getResourceAsStream(fileName);
        keyStore.load(resourceAsStream, null);
        return keyStore;
    }

    public static KeyStore loadKeystore(String fileName, String type, String password) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(type);
        keyStore.load(CommonUtil.class.getResourceAsStream(fileName), StringUtils.isBlank(password) ? null : password.toCharArray());
        return keyStore;
    }

    public static KeyStore loadKeystoreFromFile(String fileName, String type, String password) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(type);
        keyStore.load(new FileInputStream(fileName), StringUtils.isBlank(password) ? null : password.toCharArray());
        return keyStore;
    }


    public static Certificate loadCertificate(String certFilename) throws CertificateException {
        InputStream fis = CommonUtil.class.getResourceAsStream(certFilename);
        BufferedInputStream bis = new BufferedInputStream(fis);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        return cf.generateCertificate(bis);
    }

    public static byte[] readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, bos);
        return bos.toByteArray();
    }

    public static byte[] readAllBytesForResource(String resource) throws IOException {
        LOG.info("Get resource bytes [{}]", resource);
        InputStream fis = CommonUtil.class.getResourceAsStream(resource);
        return readAllBytes(fis);
    }
}
