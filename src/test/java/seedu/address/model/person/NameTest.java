package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class NameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Name(null));
    }


    // Empty name
    @Test
    public void constructor_emptyName_throwsIllegalArgumentException() {
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, () -> new Name(invalidName));
    }

    // Spaces only name
    @Test
    public void constructor_whiteSpacesOnlyName_throwsIllegalArgumentException() {
        String invalidName = "   ";
        assertThrows(IllegalArgumentException.class, () -> new Name(invalidName));
    }

    // A valid name
    @Test
    public void constructor_validName_success() {
        String validName = "Valid Name";
        assertEquals(validName, new Name(validName).fullName);
    }

    // A valid name with leading and trailing whitespaces
    @Test
    public void constructor_validNameWithLeadingAndTrailingWhitespaces_success() {
        String validName = "   Valid Name   ";
        // Note the constructor will trim it and store the trimmed version
        assertEquals("Valid Name", new Name(validName).fullName);
    }

    // Invalid name
    @Test
    public void constructor_validNameWithNumbers_throwsIllegalArgumentException() {
        String invalidName = "John Doe 123";
        assertThrows(IllegalArgumentException.class, () -> new Name(invalidName));
    }

    @Test
    public void isValidName() {
        // null name
        assertThrows(NullPointerException.class, () -> Name.isValidName(null));

        // invalid name
        assertFalse(Name.isValidName("")); // empty string
        assertFalse(Name.isValidName(" ")); // spaces only
        assertFalse(Name.isValidName("^+_")); // contains only invalid characters
        assertFalse(Name.isValidName("peter jack*")); // contains invalid characters
        assertFalse(Name.isValidName("12345")); // numbers only
        assertFalse(Name.isValidName("peter jack 2nd")); // contains numbers
        assertFalse(Name.isValidName("_peter jack")); // starts with non-alphabetical character
        assertFalse(Name.isValidName("  peter jack")); // leading spaces

        // valid name
        assertTrue(Name.isValidName("p")); // minimal
        assertTrue(Name.isValidName("peterjack")); // alphabets only, without spaces
        assertTrue(Name.isValidName("peter jack")); // alphabets only
        assertTrue(Name.isValidName("Peter Jack")); // with capital letters
        assertTrue(Name.isValidName("Peter Jack Rayston the Second")); // long names
        assertTrue(Name.isValidName("peter' jack")); // with apostrophes
        assertTrue(Name.isValidName("peter-jack")); // with hyphens
        assertTrue(Name.isValidName("peter, jack")); // with commas
        assertTrue(Name.isValidName("peter. jack")); // with periods
        assertTrue(Name.isValidName("p'.-,")); // minimal with all valid special characters only
        assertTrue(Name.isValidName("Peter'.,- Jack Rayston the Second")); // with all valid special characters
        assertTrue(Name.isValidName("peter jack  ")); // with trailing spaces

    }

    @Test
    public void equals() {
        Name name = new Name("Valid Name");

        // same values -> returns true
        assertTrue(name.equals(new Name("Valid Name")));
        assertTrue(name.equals(new Name("valid name"))); // case-insensitive

        // same object -> returns true
        assertTrue(name.equals(name));

        // null -> returns false
        assertFalse(name.equals(null));

        // different types -> returns false
        assertFalse(name.equals(5.0f));

        // different values -> returns false
        assertFalse(name.equals(new Name("Other Valid Name")));
    }
}
