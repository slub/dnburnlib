package de.slub.dnburn;

import de.slub.nbnurn.NBNURN;
import de.slub.urn.URNSyntaxException;

import java.util.HashMap;

/**
 * Represents a National Bibliographic Number (NBN) as Uniform Resource Name (URN) with additional constraints imposed
 * by the German National Library (DNB) for the use of NBN URNs in the nbn:de namespace.
 * <p>
 * To obtain an instance, you can parse a URN string literal such as "urn:nbn:de:gbv:089-3321752945" using the {@code
 * parse()} and {@code create()} methods.
 * <p>
 * Note: The vocabulary for describing the various parts of a URN is different in the DNB URNs specification. You will
 * find urn:{namespace identifier}:{sub namespaces}:{namespace specific string}, where the namespace identifier is fixed
 * to "nbn:de". The URN and NBN URN specifications use slightly different terms to denote the URN parts.
 *
 * @author Ralf Claussnitzer
 * @see <a href="https://tools.ietf.org/html/rfc2141">URN Syntax</a>
 * @see <a href="https://tools.ietf.org/html/rfc1737">Functional Requirements for Uniform Resource Names</a>
 * @see <a href="http://www.iana.org/assignments/urn-namespaces/urn-namespaces.xhtml">Official IANA Registry of URN
 * Namespaces</a>
 * @see <a href="http://d-nb.info/1045320641/34">Policy for issuing URNs in the urn:nbn:de namespace</a>
 * @see <a href="http://nbn-resolving.de/nbncheckdigit.php">Information about the check digit algorithm</a>
 */
final public class DNBURN {

    private static final int URN_NBN_DE_PART_CHECKSUM = 801;
    private static final HashMap<Character, Integer> CHAR_MAP = new HashMap<Character, Integer>() {{
        put('0', 1);
        put('1', 2);
        put('2', 3);
        put('3', 4);
        put('4', 5);
        put('5', 6);
        put('6', 7);
        put('7', 8);
        put('8', 9);
        put('9', 41);
        put('a', 18);
        put('b', 14);
        put('c', 19);
        put('d', 15);
        put('e', 16);
        put('f', 21);
        put('g', 22);
        put('h', 23);
        put('i', 24);
        put('j', 25);
        put('k', 42);
        put('l', 26);
        put('m', 27);
        put('n', 13);
        put('o', 28);
        put('p', 29);
        put('q', 31);
        put('r', 12);
        put('s', 32);
        put('t', 33);
        put('u', 11);
        put('v', 34);
        put('w', 35);
        put('x', 36);
        put('y', 37);
        put('z', 38);
        put('+', 49);
        put(':', 17);
        put('-', 39);
        put('/', 45);
        put('_', 43);
        put('.', 47);
    }};
    private static final String COUNTRY_CODE = "de";
    private final char checkDigit;
    private NBNURN nbnurn;

    private DNBURN(NBNURN nbnurn, char checkDigit) {
        this.nbnurn = nbnurn;
        this.checkDigit = checkDigit;
    }

    /**
     * Creates a DNB URN by parsing the given string.
     * <p>
     * This convenience factory method works as if by invoking the parse(String) method; any URNSyntaxException thrown
     * by the method is caught and wrapped in a new IllegalArgumentException object, which is then thrown.
     * <p>
     * This method is provided for use in situations where it is known that the given string is a legal URN, for example
     * for URN constants declared within in a program, and so it would be considered a programming error for the string
     * not to parse as such. The methods, which throw URNSyntaxException directly, should be used situations where a URN
     * is being constructed from user input or from some other source that may be prone to errors.
     *
     * @param str String to be parsed into a DNB URN
     * @return The new URN
     * @throws NullPointerException     If str is null
     * @throws IllegalArgumentException If the given string cannot be parsed into a DNB URN
     */
    public static DNBURN create(String str) {
        assertNotNull(str);
        try {
            return parse(str);
        } catch (URNSyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static DNBURN create(
            String libraryNetworkAbbreviation,
            String libraryIdentifier,
            String uniqueNumber,
            char checkDigit) {
        assertNotNull(libraryNetworkAbbreviation);
        assertNotNull(libraryIdentifier);
        assertNotNull(uniqueNumber);
        try {
            NBNURN nbnurn = NBNURN.newInstance(COUNTRY_CODE, libraryNetworkAbbreviation + ":" + libraryIdentifier, uniqueNumber);
            assertCheckDigitMatches(nbnurn, checkDigit);
            return new DNBURN(nbnurn, checkDigit);
        } catch (URNSyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Parses a given string into a DNB URN.
     * <p>
     * A valid DNB URN contains the fixed Namespace Identifier "nbn:de" as well as a valid check digit as the last
     * character.
     *
     * @param str String to be parsed into a DNB URN
     * @return The new DNB URN
     * @throws URNSyntaxException         If the string cannot be parsed into any NBN URN
     * @throws InvalidCheckDigit          If the appended check digit is not valid
     * @throws InvalidNamespaceIdentifier If the namespace identifier is not "nbn:de"
     */
    public static DNBURN parse(String str) throws URNSyntaxException {
        NBNURN nbnurn = NBNURN.fromString(str.substring(0, str.length() - 1));
        if (!nbnurn.getCountryCode().equals(COUNTRY_CODE)) {
            throw new InvalidNamespaceIdentifier(nbnurn.getCountryCode());
        }
        char parsedCheckDigit = str.charAt(str.length() - 1);
        assertCheckDigitMatches(nbnurn, parsedCheckDigit);
        return new DNBURN(nbnurn, parsedCheckDigit);
    }

    private static void assertNotNull(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
    }

    private static char calculateCheckDigit(NBNURN nbnurn) {
        int sum = URN_NBN_DE_PART_CHECKSUM;
        int index = 22;
        int charcode = 0;
        for (Character c : nbnurn.toString().toCharArray()) {
            charcode = CHAR_MAP.get(c);
            if (charcode < 10) {
                sum += charcode * ++index;
            } else {
                sum += (charcode / 10 * ++index) + (charcode % 10 * ++index);
            }
        }
        int lastDigit = ((charcode < 10) ? (charcode) : (charcode % 10));
        if (lastDigit == 0) {
            throw new IllegalStateException("Last digit of URN is 0");
        }
        int checkDigit = (sum / lastDigit) % 10;
        return Integer.toString(checkDigit).charAt(0);
    }

    private static void assertCheckDigitMatches(NBNURN nbnurn, char checkDigit) throws InvalidCheckDigit {
        char calculatedCheckDigit = calculateCheckDigit(nbnurn);
        if (calculatedCheckDigit != checkDigit) {
            throw new InvalidCheckDigit(calculatedCheckDigit, checkDigit);
        }
    }

    @Override
    public boolean equals(Object obj) {
        DNBURN that = (DNBURN) obj;
        return obj instanceof DNBURN &&
                (this.nbnurn.equals(that.nbnurn) && this.checkDigit == that.checkDigit);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    protected DNBURN clone() throws CloneNotSupportedException {
        return new DNBURN(nbnurn, checkDigit);
    }

    /**
     * Convenience method for obtaining this DNB URNs check digit.
     *
     * @return Check digit
     */
    public char checkDigit() {
        return checkDigit;
    }

    @Override
    public String toString() {
        return nbnurn.toString() + checkDigit;
    }
}
