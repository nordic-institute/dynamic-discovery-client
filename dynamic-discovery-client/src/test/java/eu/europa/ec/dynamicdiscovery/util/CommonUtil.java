package eu.europa.ec.dynamicdiscovery.util;

import com.google.common.io.CharStreams;
import eu.europa.ec.dynamicdiscovery.exception.StreamException;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by rodrfla on 21/11/2016.
 */
public class CommonUtil {

    public static String getStringFromXmlFile(String fileName) throws TechnicalException {
        try {
            InputStream inputStream = getStreamFromXmlFile(fileName);
            return CharStreams.toString(new InputStreamReader(inputStream, "UTF-8"));
        } catch (Exception exc) {
            throw new StreamException(exc.getMessage(), exc);
        }
    }

    public static InputStream getStreamFromXmlFile(String fileName) throws TechnicalException {
        try {
            return CommonUtil.class.getResourceAsStream("/response/" + fileName + ".xml");
        } catch (Exception exc) {
            throw new StreamException(exc.getMessage(), exc);
        }
    }
}
