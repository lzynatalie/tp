package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class IcTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Ic(null));
    }

    // Empty Ic
    @Test
    public void constructor_emptyIc_throwsIllegalArgumentException() {
        String invalidIc = "";
        assertThrows(IllegalArgumentException.class, () -> new Ic(invalidIc));
    }

    // Spaces only Ic
    @Test
    public void constructor_whiteSpacesOnlyIc_throwsIllegalArgumentException() {
        String invalidIc = "   ";
        assertThrows(IllegalArgumentException.class, () -> new Ic(invalidIc));
    }

    // An invalid Ic
    @Test
    public void constructor_invalidIc_throwsIllegalArgumentException() {
        String invalidIc = "1234567A"; // missing first character
        assertThrows(IllegalArgumentException.class, () -> new Ic(invalidIc));
    }

    // A valid Ic
    @Test
    public void constructor_validIc_success() {
        String validIc = "S1234567A";
        assertEquals(validIc, new Ic(validIc).value);
    }

    // A valid Ic with leading and trailing whitespaces
    @Test
    public void constructor_validIcWithLeadingAndTrailingWhitespaces_success() {
        String validIc = "   S1234567A   ";
        // Note the constructor will trim it and store the trimmed version
        assertEquals("S1234567A", new Ic(validIc).value);
    }



    @Test
    public void isValidIc() {

        // null Ic
        assertThrows(NullPointerException.class, () -> Ic.isValidIc(null));

        // blank Ic
        assertFalse(Ic.isValidIc("")); // empty string
        assertFalse(Ic.isValidIc(" ")); // spaces only

        // invalid Ic
        assertFalse(Ic.isValidIc("123456A")); // less than 7 digits, no first character
        assertFalse(Ic.isValidIc("S123456A")); // less than 7 digits, with first character
        assertFalse(Ic.isValidIc("A123456A")); // less than 7 digits, with invalid first character
        assertFalse(Ic.isValidIc("12345678A")); // more than 7 digits, no first character
        assertFalse(Ic.isValidIc("S12345678A")); // more than 7 digits, with first character
        assertFalse(Ic.isValidIc("A12345678A")); // more than 7 digits, with invalid first character
        assertFalse(Ic.isValidIc("S123456")); // less than 7 digits, no last character
        assertFalse(Ic.isValidIc("S123456A")); // less than 7 digits, with last character
        assertFalse(Ic.isValidIc("S123456_")); // less than 7 digits, with invalid last character
        assertFalse(Ic.isValidIc("S12345678")); // more than 7 digits, no last character
        assertFalse(Ic.isValidIc("S12345678A")); // more than 7 digits, with last character
        assertFalse(Ic.isValidIc("S12345678_")); // more than 7 digits, with invalid last character
        assertFalse(Ic.isValidIc("1234567A")); // first character is missing
        assertFalse(Ic.isValidIc("X1234567A")); // first character is not S, T, F or G
        assertFalse(Ic.isValidIc("_1234567A")); // first character is underscore, not S, T, F or G
        assertFalse(Ic.isValidIc("+1234567A")); // first character is '+' symbol, not S, T, F or G
        assertFalse(Ic.isValidIc(" 1234567A")); // first character is space, not S, T, F or G
        assertFalse(Ic.isValidIc(".1234567A")); // first character is period, not S, T, F or G
        assertFalse(Ic.isValidIc("-1234567A")); // first character is '-' symbol, not S, T, F or G
        assertFalse(Ic.isValidIc("@1234567A")); // first character is '@' symbol, not S, T, F or G
        assertFalse(Ic.isValidIc(",1234567A")); // first character is ',' symbol, not S, T, F or G
        assertFalse(Ic.isValidIc("+x1234567A")); // first character has 2 characters, both invalid
        assertFalse(Ic.isValidIc("+S1234567A")); // first character has 2 characters, one invalid then valid
        assertFalse(Ic.isValidIc("S+1234567A")); // first character has 2 characters, one valid then invalid
        assertFalse(Ic.isValidIc("FS1234567A")); // first character has 2 characters, both valid
        assertFalse(Ic.isValidIc("S1234567")); // missing last character
        assertFalse(Ic.isValidIc("S1234567_")); // last character is underscore, not a letter
        assertFalse(Ic.isValidIc("S1234567+")); // last character is '+' symbol, not a letter
        assertFalse(Ic.isValidIc("S1234567 ")); // last character is space, not a letter
        assertFalse(Ic.isValidIc("S1234567.")); // last character is period, not a letter
        assertFalse(Ic.isValidIc("S1234567-")); // last character is '-' symbol, not a letter
        assertFalse(Ic.isValidIc("S1234567@")); // last character is '@' symbol, not a letter
        assertFalse(Ic.isValidIc("S1234567,")); // last character is ',' symbol, not a letter
        assertFalse(Ic.isValidIc("S1234567@@")); // last character has 2 characters, both invalid
        assertFalse(Ic.isValidIc("S1234567@A")); // last character has 2 characters, one invalid then valid
        assertFalse(Ic.isValidIc("S1234567A@")); // last character has 2 characters, one valid then invalid
        assertFalse(Ic.isValidIc("S1234567AB")); // last character has 2 characters, both valid
        assertFalse(Ic.isValidIc("S1234A56A")); // letter in the middle of 6 digits
        assertFalse(Ic.isValidIc("SS123456A")); // letter in the start of 6 digits
        assertFalse(Ic.isValidIc("S1234A56AA")); // letter in the end of 6 digits

        assertFalse(Ic.isValidIc("S-1234567A")); // tries negative number


        // valid Ic
        assertTrue(Ic.isValidIc("S1234567A"));
        assertTrue(Ic.isValidIc("s1234567A")); // case-insensitive
        assertTrue(Ic.isValidIc("S1234567a")); // case-insensitive
        assertTrue(Ic.isValidIc("s1234567a")); // case-insensitive

        assertTrue(Ic.isValidIc("T1234567A"));
        assertTrue(Ic.isValidIc("t1234567A")); // case-insensitive
        assertTrue(Ic.isValidIc("T1234567a")); // case-insensitive
        assertTrue(Ic.isValidIc("t1234567a")); // case-insensitive

        assertTrue(Ic.isValidIc("F1234567A"));
        assertTrue(Ic.isValidIc("f1234567A")); // case-insensitive
        assertTrue(Ic.isValidIc("F1234567a")); // case-insensitive
        assertTrue(Ic.isValidIc("f1234567a")); // case-insensitive

        assertTrue(Ic.isValidIc("G1234567A"));
        assertTrue(Ic.isValidIc("g1234567A")); // case-insensitive
        assertTrue(Ic.isValidIc("G1234567a")); // case-insensitive
        assertTrue(Ic.isValidIc("g1234567a")); // case-insensitive


        assertTrue(Ic.isValidIc("g9999999a")); // highest number
        assertTrue(Ic.isValidIc("g0000000a")); // lowest number


    }

    @Test
    public void equals() {
        Ic ic = new Ic("G9999999Y");

        // same values -> returns true
        assertTrue(ic.equals(new Ic("G9999999Y"))); // exact same
        assertTrue(ic.equals(new Ic("g9999999Y"))); // lower case first character, same digits and last character
        assertTrue(ic.equals(new Ic("G9999999y"))); // same first character and digits, lower case last character
        assertTrue(ic.equals(new Ic("g9999999Y"))); // lower case first character and last character, same digits

        // same object -> returns true
        assertTrue(ic.equals(ic));

        // null -> returns false
        assertFalse(ic.equals(null));

        // different types -> returns false
        assertFalse(ic.equals(5.0f));

        // different values -> returns false
        assertFalse(ic.equals(new Ic("F0000000B"))); // completely different
        assertFalse(ic.equals(new Ic("G9999999Z"))); // same digits but different last character
        assertFalse(ic.equals(new Ic("S9999999Y"))); // same digits but different first character
        assertFalse(ic.equals(new Ic("S9999999Z"))); // same digits but different first and last character
        assertFalse(ic.equals(new Ic("G9999998Y"))); // different digits but same first and last character

    }
}

