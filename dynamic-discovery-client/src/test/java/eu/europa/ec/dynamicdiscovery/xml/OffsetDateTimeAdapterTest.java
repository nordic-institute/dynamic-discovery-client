package eu.europa.ec.dynamicdiscovery.xml;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.OffsetDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OffsetDateTimeAdapterTest {
    private static Stream<Arguments> testDateTimeArguments() {
        return Stream.of(
                Arguments.of("Null value",
                        null,
                        null
                ),
                Arguments.of("Empty value",
                        "",
                        null
                ),
                Arguments.of("Blank value",
                        "  ",
                        null
                ),
                Arguments.of("DateTime with positive offset",
                        "2023-10-01T11:00:00+04:00",
                        OffsetDateTime.parse("2023-10-01T11:00:00+04:00")
                ),
                Arguments.of("DateTime with negative offset",
                        "2023-10-01T11:00:00-04:00",
                        OffsetDateTime.parse("2023-10-01T11:00:00-04:00")
                ),
                Arguments.of("DateTime no seconds with positive offset",
                        "2023-10-01T11:00+04:00",
                        OffsetDateTime.parse("2023-10-01T11:00:00+04:00")
                ),
                Arguments.of("DateTime no seconds with negative offset",
                        "2023-10-01T11:00-04:00",
                        OffsetDateTime.parse("2023-10-01T11:00:00-04:00")
                ),
                Arguments.of("Use default offset if not provided",
                        "2023-10-01T11:00:00",
                        OffsetDateTime.parse("2023-10-01T11:00:00" + OffsetDateTime.now().getOffset())
                ),
                Arguments.of("Use default offset if not provided+ no seconds",
                        "2023-10-01T11:00",
                        OffsetDateTime.parse("2023-10-01T11:00:00" + OffsetDateTime.now().getOffset())
                )
        );
    }

    OffsetDateTimeAdapter testInstance = new OffsetDateTimeAdapter();

    @ParameterizedTest
    @MethodSource("testDateTimeArguments")
    void unmarshal(String desc, String value, OffsetDateTime expected) {
        System.out.println("unmarshal: " + desc);
        assertEquals(expected, testInstance.unmarshal(value));


    }

    @Test
    void marshal() {
        // create test
        OffsetDateTime value = OffsetDateTime.parse("2023-10-01T11:00:00+04:00");
        String expected = "2023-10-01T11:00:00+04:00";
        String result = testInstance.marshal(value);
        assertEquals(expected, result);
    }
}