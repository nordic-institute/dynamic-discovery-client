
/**
 * Purpose of the class it to provide  OffsetDateTime to string and string to OffsetDateTime conversion
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */

package eu.europa.ec.dynamicdiscovery.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.OffsetDateTime;

public class OffsetDateTimeAdapter
    extends XmlAdapter<String, OffsetDateTime>
{
    public OffsetDateTime unmarshal(String value) {
        return (eu.europa.ec.dynamicdiscovery.util.DatatypeConverter.parseDateTime(value));
    }

    public String marshal(OffsetDateTime value) {
        return (eu.europa.ec.dynamicdiscovery.util.DatatypeConverter.printDateTime(value));
    }
}
