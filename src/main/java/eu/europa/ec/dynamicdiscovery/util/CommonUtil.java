package eu.europa.ec.dynamicdiscovery.util;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;

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

    public static Proxy proxyAuthentication(final String proxyServerAddress, final int port, final String user, final String password) throws Exception {

        if ((user == null || user.isEmpty()) || (password == null || password.isEmpty())) {
            throw new Exception("Credential for Proxy Authentication is missing.");
        }

        if ((proxyServerAddress == null || proxyServerAddress.isEmpty()) || port == 0) {
            throw new Exception("Configuration for Proxy Authentication is missing.");
        }

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyServerAddress, port));
        Authenticator authenticator = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return (new PasswordAuthentication(user,
                        password.toCharArray()));
            }
        };
        Authenticator.setDefault(authenticator);
        return proxy;
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
