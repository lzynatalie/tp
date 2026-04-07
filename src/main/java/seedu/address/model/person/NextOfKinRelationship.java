package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Next-of-kin relationship in the system.
 * Guarantees: immutable; name is validated and non-null.
 */
public class NextOfKinRelationship {

    public static final String MESSAGE_CONSTRAINTS =
            "Next-of-kin relationship can contain: (A-Z, a-z), whitespaces, comma (,), "
                    + "hyphens (-), apostrophe (‘), period (.).\n"
                    + "The next-of-kin relationship should not be empty and must start with a letter.\n";

    public static final String VALIDATION_REGEX = "[A-Za-z][A-Za-z ,.'-]*";

    private final String label;

    /**
     * Constructs a {@code NextOfKinRelationship}.
     *
     * @param label A valid next of kin relationship label.
     */
    public NextOfKinRelationship(String label) {
        requireNonNull(label);
        String trimmedLabel = label.trim();
        checkArgument(isValidNextOfKinRelationship(trimmedLabel), MESSAGE_CONSTRAINTS);
        this.label = trimmedLabel;
    }

    /**
     * Returns true if a given string is a valid next-of-kin.
     */
    public static boolean isValidNextOfKinRelationship(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof NextOfKinRelationship)) {
            return false;
        }
        NextOfKinRelationship otherNok = (NextOfKinRelationship) other;
        return label.equalsIgnoreCase(otherNok.label); // case-insensitive
    }

    @Override
    public int hashCode() {
        return label.toLowerCase().hashCode(); // consistent with equals
    }
}
