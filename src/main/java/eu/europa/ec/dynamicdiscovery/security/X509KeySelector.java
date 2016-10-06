package eu.europa.ec.dynamicdiscovery.security;

import javax.xml.crypto.*;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import java.security.Key;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Iterator;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 * @author Erlend Klakegg Bergheim - erlend.klakegg.bergheim@difi.no
 */
public class X509KeySelector extends KeySelector {
    private X509Certificate certificate;

    X509KeySelector() {
    }

    public KeySelectorResult select(KeyInfo keyInfo, KeySelector.Purpose purpose, AlgorithmMethod method, XMLCryptoContext context) throws KeySelectorException {
        Iterator ki = keyInfo.getContent().iterator();

        while (true) {
            XMLStructure info;
            do {
                if (!ki.hasNext()) {
                    throw new KeySelectorException("No key found!");
                }

                info = (XMLStructure) ki.next();
            } while (!(info instanceof X509Data));

            X509Data x509Data = (X509Data) info;
            Iterator xi = x509Data.getContent().iterator();

            while (xi.hasNext()) {
                Object o = xi.next();
                if (o instanceof X509Certificate) {
                    this.certificate = (X509Certificate) o;
                    final PublicKey key = this.certificate.getPublicKey();
                    if (algEquals(method.getAlgorithm(), key.getAlgorithm())) {
                        return new KeySelectorResult() {
                            public Key getKey() {
                                return key;
                            }
                        };
                    }
                }
            }
        }
    }

    static boolean algEquals(String algURI, String algName) {
        return algName.equalsIgnoreCase("DSA") && algURI.equalsIgnoreCase("http://www.w3.org/2000/09/xmldsig#dsa-sha1") || algName.equalsIgnoreCase("RSA") && algURI.equalsIgnoreCase("http://www.w3.org/2000/09/xmldsig#rsa-sha1");
    }

    public X509Certificate getCertificate() {
        return this.certificate;
    }
}
