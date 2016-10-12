package eu.europa.ec.dynamicdiscovery.util;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by rodrfla on 03/10/2016.
 */
public class CommonUtil {

    private static DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

    static {
        documentBuilderFactory.setNamespaceAware(true);
    }

    public CommonUtil() {
    }

    public static Document parse(InputStream inputStream) throws SAXException, IOException, ParserConfigurationException {
        return documentBuilderFactory.newDocumentBuilder().parse(inputStream);
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
