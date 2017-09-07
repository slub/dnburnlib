package de.slub.dnburn;

import de.slub.urn.URNSyntaxException;

public class InvalidNamespaceIdentifier extends URNSyntaxException {
    public InvalidNamespaceIdentifier(String subnamespacePrefix) {
        super(String.format("Invalid country code '%s' in URN; expected 'de'", subnamespacePrefix));
    }
}
