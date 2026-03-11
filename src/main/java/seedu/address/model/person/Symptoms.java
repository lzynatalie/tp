package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

public class Symptoms {

    public static final String MESSAGE_CONSTRAINTS =
            "Symptoms should not be blank";

    public final String symptoms;

    public Symptoms(String symptoms) {
        requireNonNull(symptoms);
        checkArgument(isValidSymptoms(symptoms), MESSAGE_CONSTRAINTS);
        this.symptoms = symptoms;
    }

    private Boolean isValidSymptoms(String symptoms) {
        return !symptoms.isEmpty();
    }


    @Override
    public String toString() {
        return symptoms;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Symptoms)) {
            return false;
        }

        Symptoms otherSymptoms = (Symptoms) other;
        return symptoms.equals(otherSymptoms.symptoms);
    }

    @Override
    public int hashCode() {
        return symptoms.hashCode();
    }

}
