package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Next-of-kin in the system.
 * Guarantees: immutable; name is validated as declared in {@link #isValidNextOfKin(String)}
 */
public class NextOfKin {

    public static final String MESSAGE_CONSTRAINTS =
            "Next-of-kin name can contain: (A-Z, a-z), whitespaces, comma (,), "
                    + "hyphens (-), apostrophe (‘), period (.).\n"
                    + "The next-of-kin name should not be empty and must start with a letter.\n";

    public static final String VALIDATION_REGEX = "[A-Za-z][A-Za-z ,.'-]*";

    private final String fullName;

    /**
     * Constructs a {@code NextOfKin}.
     *
     * @param nextOfKinName A valid next-of-kin name.
     */
    public NextOfKin(String nextOfKinName) {
        requireNonNull(nextOfKinName);
        String trimmedNextOfKinName = nextOfKinName.trim();
        checkArgument(isValidNextOfKin(trimmedNextOfKinName), MESSAGE_CONSTRAINTS);
        fullName = trimmedNextOfKinName;
    }

    /**
     * Returns true if a given string is a valid next-of-kin.
     */
    public static boolean isValidNextOfKin(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof NextOfKin)) {
            return false;
        }
        NextOfKin otherNok = (NextOfKin) other;
        return fullName.equalsIgnoreCase(otherNok.fullName); // case-insensitive
    }

    @Override
    public int hashCode() {
        return fullName.toLowerCase().hashCode(); // consistent with equals
    }
}
