package eu.europa.ec.dynamicdiscovery.util;

import java.time.OffsetDateTime;

public class DatatypeConverter {
    protected DatatypeConverter() {
    }

    public static OffsetDateTime parseDateTime(String value) {
        return OffsetDateTime.parse(value);
    }

    public static String printDateTime(OffsetDateTime value) {
        return value.toString();
    }
}
