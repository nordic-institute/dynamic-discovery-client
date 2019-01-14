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
package eu.europa.ec.dynamicdiscovery.util;

//import com.google.common.io.CharStreams;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public class CommonUtil {

    public static String getStringFromXmlFile(String fileName) throws Exception {
        try {
            return new String(Files.readAllBytes(Paths.get("src","test","resources","response",fileName+".xml")), "UTF-8");
        } catch (Exception exc) {
            throw new Exception(exc.getMessage(), exc);
        }
    }

    public static InputStream getStreamFromXmlFile(String fileName) throws Exception {
        try {
            return CommonUtil.class.getResourceAsStream("/response/" + fileName + ".xml");
        } catch (Exception exc) {
            throw new Exception(exc.getMessage(), exc);
        }
    }

    public static KeyStore loadTrustStore(String fileName) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(Thread.currentThread().getContextClassLoader().getResource(fileName).getFile()), null);
        return keyStore;
    }

    public static  Certificate loadCertificate(String certFilename) throws IOException, CertificateException {
        InputStream fis = Thread.currentThread().getContextClassLoader().getResource(certFilename).openStream();
        BufferedInputStream bis = new BufferedInputStream(fis);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        return cf.generateCertificate(bis);
    }
}
