package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the notes field for a patient.
 * Guarantees: immutable, valid as per constraints.
 */
public class Notes {

    public static final int MAX_LENGTH = 500;
    public static final String MESSAGE_CONSTRAINTS =
            "Invalid value:  The notes field exceeds the maximum limit of " + MAX_LENGTH + " characters.";

    private final String value;

    /**
     * Constructs a {@code Notes}.
     * @param notes A string representing the notes. Can be null or empty.
     * @throws IllegalArgumentException if the notes exceed the maximum length
     */
    public Notes(String notes) {
        requireNonNull(notes);
        checkArgument(isValidNotes(notes), MESSAGE_CONSTRAINTS);
        this.value = notes;
    }

    /**
     * Returns true if a given string is a valid notes value.
     */
    public static boolean isValidNotes(String test) {
        return test.length() <= MAX_LENGTH;
    }

    public String getValue() {
        return value;
    }

    /**
     * Appends the given Notes to this current Notes.
     * Returns a new Notes object containing the combined text.
     */
    public Notes append(Notes additionalNotes) {
        if (additionalNotes.value.trim().isEmpty()) {
            return this;
        }

        String existingText = this.value;

        if (existingText.trim().isEmpty()) {
            return additionalNotes;
        }

        return new Notes(existingText + "\n" + additionalNotes.value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Notes // instanceof handles nulls
                && value.equals(((Notes) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
