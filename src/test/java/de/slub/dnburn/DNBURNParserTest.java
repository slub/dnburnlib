package de.slub.dnburn;

import de.slub.urn.URNSyntaxException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DNBURNParserTest {

    @Test
    public void Returns_given_check_digit_after_parsing_a_DNB_URN() {
        char expectedDigit = '5';
        DNBURN dnburn = DNBURN.create("urn:nbn:de:gbv:089-332175294" + expectedDigit);
        char actualDigit = dnburn.checkDigit();
        assertEquals("Wrong check digit", expectedDigit, actualDigit);
    }

    @Test(expected = InvalidCheckDigit.class)
    public void Throws_InvalidCheckDigit_when_parsing_DNB_URN_with_invalid_check_digit() throws URNSyntaxException {
        char invalidDigit = '0';
        DNBURN.parse("urn:nbn:de:gbv:089-332175294" + invalidDigit);
    }

    @Test(expected = InvalidNamespaceIdentifier.class)
    public void Throws_InvalidNamespaceIdentifier_when_parsing_DNB_URN_with_invalid_namespace_identifier() throws URNSyntaxException {
        DNBURN.parse("urn:nbn:ch:gbv:089-3321752945");
    }

    @Test(expected = URNSyntaxException.class)
    public void Throws_URNSyntaxException_when_parsing_non_NBN_URNs() throws URNSyntaxException {
        DNBURN.parse("urn:XXX:de:gbv:089-3321752945");
    }

    @Test(expected = NullPointerException.class)
    public void Throws_NullPointerException_when_null_is_passed() {
        DNBURN.create(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void Throws_IllegalArgumentException_when_parsing_non_NBN_URN() {
        DNBURN.create("urn:foo:bar:4711");
    }

    @Test(expected = IllegalArgumentException.class)
    public void Throws_IllegalArgumentException_when_parsing_non_NBN_DE_URN() {
        DNBURN.create("urn:nbn:ch:4711");
    }

    @Test
    public void Parsed_DNB_URN_literal_is_lexically_equivalent_to_String_representation() {
        String str = "urn:nbn:de:gbv:089-3321752945";
        DNBURN urn = DNBURN.create(str);
        assertEquals("URN literal should be equal to input string", str, urn.toString());
    }
}