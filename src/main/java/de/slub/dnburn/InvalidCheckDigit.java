package de.slub.dnburn;

import de.slub.urn.URNSyntaxException;

public class InvalidCheckDigit extends URNSyntaxException {
    public InvalidCheckDigit(char expected, char actual) {
        super(String.format("Check digit is invalid. Expected '%c' but got '%c'", expected, actual));
    }
}
