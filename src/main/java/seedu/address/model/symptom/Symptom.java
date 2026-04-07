package seedu.address.model.symptom;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Symptom in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidSymptomName(String)}
 */
public class Symptom {

    public static final String MESSAGE_CONSTRAINTS =
            "Symptom names can contain alphanumeric characters and whitespaces only.\n"
                    + "It cannot be blank if the prefix is used and must start with an alphanumeric character\n";

    public static final String VALIDATION_REGEX = "\\p{Alnum}[\\p{Alnum}\\s]*";

    public final String symptomName;

    /**
     * Constructs a {@code Symptom}.
     *
     * @param symptomName A valid symptom name.
     */
    public Symptom(String symptomName) {
        requireNonNull(symptomName);
        String trimmedSymptomName = symptomName.trim();
        checkArgument(isValidSymptomName(trimmedSymptomName), MESSAGE_CONSTRAINTS);
        this.symptomName = trimmedSymptomName;
    }

    /**
     * Returns true if a given string is a valid symptom name.
     */
    public static boolean isValidSymptomName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Symptom)) {
            return false;
        }

        Symptom otherSymptom = (Symptom) other;
        return symptomName.equalsIgnoreCase(otherSymptom.symptomName);
    }

    @Override
    public int hashCode() {
        return symptomName.toLowerCase().hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + symptomName + ']';
    }

}
