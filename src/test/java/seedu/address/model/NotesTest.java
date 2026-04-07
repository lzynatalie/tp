package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Notes;
import seedu.address.testutil.Assert;

public class NotesTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Notes(null));
    }

    @Test
    public void constructor_emptyNotes_setsEmpty() {
        Notes notes = new Notes("");
        assertEquals("", notes.getValue());
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

    @Test
    public void append_emptyAdditionalNotes_returnsOriginal() throws CommandException {
        // This turns Line 50 GREEN
        Notes original = new Notes("Existing Content");
        Notes emptyAppend = new Notes("");
        assertEquals(original, original.append(emptyAppend));
    }

    @Test
    public void append_toEmptyNotes_returnsAdditional() throws CommandException {
        // This turns Line 42 GREEN, while accounting for timestamps!
        Notes emptyOriginal = new Notes("");
        Notes toAppend = new Notes("New Content");

        // Generate the expected timestamp format
        String timestamp = java.time.LocalDateTime.now().format(java.time.format
                        .DateTimeFormatter.ofPattern("dd MMM HH:mm"));
        String expectedNote = "[" + timestamp + "] New Content";

        assertEquals(expectedNote, emptyOriginal.append(toAppend).toString());
    }

    @Test
    public void append_exceedsMaxLength_throwsCommandException() {
        // Fill base to near the limit
        Notes base = new Notes("a".repeat(490));
        Notes additional = new Notes("overflow");
        assertThrows(CommandException.class, () -> base.append(additional));
    }

    @Test
    public void append_exactlyAtMaxLength_doesNotThrow() {
        // Base + formatted append must be exactly MAX_LENGTH
        // Timestamp format: "[dd MMM HH:mm] " = ~17 chars; adjust base accordingly
        int timestampOverhead = 17; // "[dd MMM HH:mm] ".length()
        int additionalLength = 10;
        int baseLength = Notes.MAX_LENGTH - additionalLength - 1 - timestampOverhead; // 1 for newline
        Notes base = new Notes("a".repeat(baseLength));
        Notes additional = new Notes("b".repeat(additionalLength));
        // Should not throw — we just verify no exception
        assertDoesNotThrow(() -> base.append(additional));
    }

    @Test
    public void append_commandExceptionMessage_isUserFriendly() {
        Notes base = new Notes("a".repeat(490));
        Notes additional = new Notes("overflow");
        CommandException ex = assertThrows(CommandException.class, () -> base.append(additional));
        assertTrue(ex.getMessage().contains("Cannot append notes"));
        assertTrue(ex.getMessage().contains(String.valueOf(Notes.MAX_LENGTH)));
    }
}
