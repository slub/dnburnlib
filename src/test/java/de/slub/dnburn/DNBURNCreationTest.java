package de.slub.dnburn;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DNBURNCreationTest {

    final private String libraryNetworkAbbreviation = "gbv";
    final private String libraryIdentifier = "089";
    final private String uniqueNumber = "332175294";
    final private char checkDigit = '5';

    final private String urnLiteral = String.format("urn:nbn:de:%s:%s-%s%s",
            libraryNetworkAbbreviation,
            libraryIdentifier,
            uniqueNumber,
            checkDigit);

    @Test
    public void Create_DNBURN_from_individual_parts() {
        DNBURN urn = DNBURN.create(libraryNetworkAbbreviation, libraryIdentifier, uniqueNumber, checkDigit);
        assertNotNull(urn);
        assertEquals(urnLiteral, urn.toString());
    }

    @Test(expected = InvalidCheckDigit.class)
    public void Creation_fails_with_exception_if_check_digit_doesnt_match() throws Throwable {
        try {
            char invalidCheckDigit = checkDigit - 1;
            DNBURN.create(libraryNetworkAbbreviation, libraryIdentifier, uniqueNumber, invalidCheckDigit);
        } catch (IllegalArgumentException iae) {
            Throwable cause = iae.getCause();
            assertNotNull(cause);
            throw cause;
        }
    }

}
