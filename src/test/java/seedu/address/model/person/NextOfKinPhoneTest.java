package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class NextOfKinPhoneTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new NextOfKinPhone(null));
    }

    @Test
    public void constructor_invalidNextOfKinPhone_throwsIllegalArgumentException() {
        String invalidPhone = "";
        assertThrows(IllegalArgumentException.class, () -> new NextOfKinPhone(invalidPhone));
    }

    @Test
    public void constructor_whiteSpacesOnlyNextOfKinPhone_throwsIllegalArgumentException() {
        String invalidPhone = "   ";
        assertThrows(IllegalArgumentException.class, () -> new NextOfKinPhone(invalidPhone));
    }

    @Test
    public void constructor_validNextOfKinPhone_success() {
        String validPhone = "93121534";
        assertEquals(validPhone, new NextOfKinPhone(validPhone).value);
    }

    @Test
    public void constructor_validPhoneWithLeadingAndTrailingWhiteSpaces_sucess() {
        String validPhone = "  93121534  ";
        assertEquals(validPhone.trim(), new NextOfKinPhone(validPhone).value);
    }

    @Test
    public void isValidNextOfKinPhone() {
        // null phone number
        assertThrows(NullPointerException.class, () -> NextOfKinPhone.isValidNextOfKinPhone(null));

        // invalid phone numbers
        assertFalse(NextOfKinPhone.isValidNextOfKinPhone(" ")); // spaces only

        // testing boundary values
        assertFalse(NextOfKinPhone.isValidNextOfKinPhone("")); // 0 digits
        assertFalse(NextOfKinPhone.isValidNextOfKinPhone("+")); // just a plus

        assertFalse(NextOfKinPhone.isValidNextOfKinPhone("12")); // 2 digit
        assertFalse(NextOfKinPhone.isValidNextOfKinPhone("1234567890123456")); // 16 digit

        assertFalse(NextOfKinPhone.isValidNextOfKinPhone("+1234567890123456")); // has optional plus
        assertFalse(NextOfKinPhone.isValidNextOfKinPhone("+12345-67890-12345-6")); // has hyphen added
        assertFalse(NextOfKinPhone.isValidNextOfKinPhone("+12 34 5-6 78 90 -1 23 45 -6")); // has space added

        assertFalse(NextOfKinPhone.isValidNextOfKinPhone("phone 93121534")); // has alphabets
        assertFalse(NextOfKinPhone.isValidNextOfKinPhone("9312/1534")); // has slash
        assertFalse(NextOfKinPhone.isValidNextOfKinPhone("9312.1534")); // has period
        assertFalse(NextOfKinPhone.isValidNextOfKinPhone("9312_1534")); // has underscore
        assertFalse(NextOfKinPhone.isValidNextOfKinPhone("(9312)1534")); // has brackets

        assertFalse(NextOfKinPhone.isValidNextOfKinPhone("9312+1534")); // plus not at the start
        assertFalse(NextOfKinPhone.isValidNextOfKinPhone("++93121534")); // multiple plus
        // optional plus followed by a hyphen(not a digit)
        assertFalse(NextOfKinPhone.isValidNextOfKinPhone("+-93121534"));
        // optional plus followed by a space (not a digit)
        assertFalse(NextOfKinPhone.isValidNextOfKinPhone("+ 93121534"));
        assertFalse(NextOfKinPhone.isValidNextOfKinPhone("-93121534")); // starts with hyphen (not a digit)
        assertFalse(NextOfKinPhone.isValidNextOfKinPhone(" 93121534")); // starts with space (not a digit)

        // valid phone numbers
        assertTrue(NextOfKinPhone.isValidNextOfKinPhone("93121534")); // just digits
        assertTrue(NextOfKinPhone.isValidNextOfKinPhone("+93121534")); // with optional '+'

        // testing boundary values
        assertTrue(NextOfKinPhone.isValidNextOfKinPhone("123")); // 3 digits
        assertTrue(NextOfKinPhone.isValidNextOfKinPhone("123456789012345")); // 15 digits

        // shows that optional '+', hyphens and spaces are not counted as digits
        assertTrue(NextOfKinPhone.isValidNextOfKinPhone("+123456789012345")); // 15 digits with optional '+'
        assertTrue(NextOfKinPhone.isValidNextOfKinPhone("+12345-67890-12345")); // still is 15 digits with hyphens
        assertTrue(NextOfKinPhone.isValidNextOfKinPhone("+12 345-67 890-12 345")); // still is 15 digits with spaces
    }

    @Test
    public void equals() {
        NextOfKinPhone nextOfKinPhone = new NextOfKinPhone("99999999");

        // same values -> returns true
        assertTrue(nextOfKinPhone.equals(new NextOfKinPhone("99999999")));

        // same object -> returns true
        assertTrue(nextOfKinPhone.equals(nextOfKinPhone));

        // null -> returns false
        assertFalse(nextOfKinPhone.equals(null));

        // different types -> returns false
        assertFalse(nextOfKinPhone.equals(5.0f));

        // different values -> returns false
        assertFalse(nextOfKinPhone.equals(new Phone("99555555")));
    }
}
