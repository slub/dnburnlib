package de.slub.dnburn;

import de.slub.urn.URNSyntaxException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DNBURNEqualityTest {

    @Test
    public void Lexically_equivalent_URNs_are_equal() throws URNSyntaxException {
        String urn = "urn:nbn:de:gbv:089-3321752945";
        DNBURN a = DNBURN.parse(urn);
        DNBURN b = DNBURN.parse(urn);
        assertEquals("Lexically equivalen URNs should be equal", a, b);
    }

    @Test
    public void Lexically_equivalent_URNs_have_same_hash_value() throws URNSyntaxException {
        String urn = "urn:nbn:de:gbv:089-3321752945";
        DNBURN a = DNBURN.parse(urn);
        DNBURN b = DNBURN.parse(urn);
        assertEquals("Lexically equivalen URNs should have same hash valu", a.hashCode(), b.hashCode());
    }

    @Test
    public void Cloned_URN_should_be_equal_to_original_URN() throws URNSyntaxException, CloneNotSupportedException {
        String urn = "urn:nbn:de:gbv:089-3321752945";
        DNBURN a = DNBURN.parse(urn);
        DNBURN b = a.clone();
        assertEquals("Cloned URN should be equal to original URN", a, b);
    }

}
