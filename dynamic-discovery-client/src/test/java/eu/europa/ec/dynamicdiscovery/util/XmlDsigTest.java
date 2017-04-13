package eu.europa.ec.dynamicdiscovery.util;

import org.junit.Assert;
import org.junit.Test;
import org.w3._2000._09.xmldsig_.SignatureType;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by idragusa on 4/13/17.
 */
public class XmlDsigTest {

    /*
    * Regression test for EDELIVERY-2059 (https://ec.europa.eu/cefdigital/tracker/browse/EDELIVERY-2059)
    *
    * XmlDsig classes are quite often used by other libraries (xmlsec, cxf, santuario), therefore, the signature of the classes
    * generated from the xmldsig-core-schema.xsd has to match the classes in these libraries.
    * Any 'uncommon' bindings, like <xjc:simple />, may lead to differences in these classes that will collide
    * with XMLDsig classes from other libraries.
    */
    @Test
    public void checkXMLDSIGGeneratedClasses() {
        /* Expected annotations */
        Assert.assertNotNull(SignatureType.class.getAnnotation(XmlType.class));
        Assert.assertNotNull(SignatureType.class.getAnnotation(XmlAccessorType.class));

        /* Not expected annotations */
        Assert.assertNull(SignatureType.class.getAnnotation(XmlRootElement.class));
    }
}
