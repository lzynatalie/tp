package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Doctor in the system.
 * Guarantees: immutable; name is validated and non-null.
 */
public class DoctorName {

    public static final String MESSAGE_CONSTRAINTS =
            "Doctor name can contain: (A-Z, a-z), whitespaces, comma (,), "
                    + "hyphens (-), apostrophe (‘), period (.).\n"
                    + "The doctor name should not be empty and must start with a letter.\n";


    public static final String VALIDATION_REGEX = "[A-Za-z][A-Za-z ,.'-]*";

    private final String fullName;

    /**
     * Constructs a {@code DoctorName}.
     *
     * @param doctorName A valid doctor name.
     */
    public DoctorName(String doctorName) {
        requireNonNull(doctorName);
        String trimmedDoctorName = doctorName.trim();
        checkArgument(isValidDoctorName(trimmedDoctorName), MESSAGE_CONSTRAINTS);
        this.fullName = trimmedDoctorName;
    }

    /**
     * Returns true if a given string is a valid doctor name.
     */
    public static boolean isValidDoctorName(String test) {
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
        if (!(other instanceof DoctorName)) {
            return false;
        }
        DoctorName otherDoctor = (DoctorName) other;
        return fullName.equalsIgnoreCase(otherDoctor.fullName); // case-insensitive
    }

    @Override
    public int hashCode() {
        return fullName.toLowerCase().hashCode(); // consistent with equals
    }
}
