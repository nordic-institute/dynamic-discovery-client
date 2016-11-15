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
package eu.europa.ec.dynamicdiscovery.util;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CommonUtil {

    private static DocumentBuilderFactory documentBuilderFactory;

    static {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
    }

    public CommonUtil() {
    }

    public static Document parse(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {
        return documentBuilderFactory.newDocumentBuilder().parse(inputStream);
    }

    public static Source convertToSource(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {
        return new DOMSource(parse(inputStream));
    }

    public static Source convertToSource(Document document) throws ParserConfigurationException, IOException, SAXException {
        return new DOMSource(document);
    }

    public static InputStream trim(InputStream inputStream) throws IOException {
        int ch;
        StringBuilder sb = new StringBuilder();
        while ((ch = inputStream.read()) != -1) {
            sb.append((char) ch);
        }
        return new ByteArrayInputStream(sb.toString().trim().getBytes());
    }
}
