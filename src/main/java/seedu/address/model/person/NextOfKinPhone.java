package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the next-of-kin's phone number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidNextOfKinPhone(String)}
 */
public class NextOfKinPhone {


    public static final String MESSAGE_CONSTRAINTS =
            "Next-of-kin phone numbers may contain digits, spaces, hyphens, and one optional '+' only at the start.\n"
                    + "If '+' is not used, the phone number must start with a digit.\n"
                    + "If '+' is used, it must be followed by a digit.\n"
                    + "The phone number must be contained in the range of 3 and 15 digits and cannot be blank.\n";

    // Use lookahead regex to ensure that only contains 3-15 digits.
    public static final String VALIDATION_REGEX = "^(\\+)?(?=(?:[ -]*[0-9]){3,15}$)[0-9][0-9 -]*$";
    public final String value;

    /**
     * Constructs a {@code Phone}.
     *
     * @param nextOfKinPhone A valid next-of-kin phone number.
     */
    public NextOfKinPhone(String nextOfKinPhone) {
        requireNonNull(nextOfKinPhone);
        String trimmedNextOfKinPhone = nextOfKinPhone.trim();
        checkArgument(isValidNextOfKinPhone(trimmedNextOfKinPhone), MESSAGE_CONSTRAINTS);
        value = trimmedNextOfKinPhone;
    }

    /**
     * Returns true if a given string is a valid phone number.
     */
    public static boolean isValidNextOfKinPhone(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NextOfKinPhone)) {
            return false;
        }

        NextOfKinPhone otherPhone = (NextOfKinPhone) other;
        return value.equals(otherPhone.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
