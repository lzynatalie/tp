package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Notes;

public class NotesTest {

    @Test
    public void constructor_nullNotes_setsEmpty() {
        Notes notes = new Notes(null);
        assertEquals("", notes.getValue());
    }

    @Test
    public void constructor_emptyNotes_setsEmpty() {
        Notes notes = new Notes("");
        assertEquals("", notes.getValue());
    }

    @Test
    public void constructor_validNotes_trimsSpaces() {
        Notes notes = new Notes("   Patient has mild symptoms.  ");
        assertEquals("Patient has mild symptoms.", notes.getValue());
    }

    @Test
    public void constructor_maxLengthNotes_accepts() {
        String maxNotes = "a".repeat(Notes.MAX_LENGTH);
        Notes notes = new Notes(maxNotes);
        assertEquals(maxNotes, notes.getValue());
    }

    @Test
    public void constructor_exceedingMaxLength_throwsException() {
        String tooLong = "a".repeat(Notes.MAX_LENGTH + 1);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Notes(tooLong));
        assertEquals(Notes.MESSAGE_CONSTRAINTS, exception.getMessage());
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        Notes notes1 = new Notes("Some notes");
        Notes notes2 = new Notes("Some notes");
        assertEquals(notes1, notes2);
    }

    @Test
    public void equals_differentValues_returnsFalse() {
        Notes notes1 = new Notes("Notes 1");
        Notes notes2 = new Notes("Notes 2");
        assertNotEquals(notes1, notes2);
    }

    @Test
    public void hashCode_sameValues_sameHashCode() {
        Notes notes1 = new Notes("Notes");
        Notes notes2 = new Notes("Notes");
        assertEquals(notes1.hashCode(), notes2.hashCode());
    }

    @Test
    public void hashCode_differentValues_differentHashCode() {
        Notes notes1 = new Notes("Notes 1");
        Notes notes2 = new Notes("Notes 2");
        assertNotEquals(notes1.hashCode(), notes2.hashCode());
    }
}
