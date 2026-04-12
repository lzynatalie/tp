package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PhoneTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Phone(null));
    }

    @Test
    public void constructor_emptyPhone_throwsIllegalArgumentException() {
        String invalidPhone = "";
        assertThrows(IllegalArgumentException.class, () -> new Phone(invalidPhone));
    }

    @Test
    public void constructor_whiteSpacesOnlyPhone_throwsIllegalArgumentException() {
        String invalidPhone = "   ";
        assertThrows(IllegalArgumentException.class, () -> new Phone(invalidPhone));
    }

    @Test
    public void constructor_validPhone_success() {
        String validPhone = "93121534";
        assertEquals(validPhone, new Phone(validPhone).value);
    }

    @Test
    public void constructor_validPhoneWithLeadingAndTrailingWhiteSpaces_success() {
        String validPhone = "  93121534  ";
        assertEquals(validPhone.trim(), new Phone(validPhone).value);
    }

    @Test
    public void isValidPhone() {
        // null phone number
        assertThrows(NullPointerException.class, () -> Phone.isValidPhone(null));

        // invalid phone numbers
        assertFalse(Phone.isValidPhone(" ")); // spaces only

        // testing boundary values
        assertFalse(Phone.isValidPhone("")); // 0 digits
        assertFalse(Phone.isValidPhone("+")); // just a plus

        assertFalse(Phone.isValidPhone("12")); // 2 digit
        assertFalse(Phone.isValidPhone("1234567890123456")); // 16 digit

        assertFalse(Phone.isValidPhone("+1234567890123456")); // has optional plus
        assertFalse(Phone.isValidPhone("+12345-67890-12345-6")); // has hyphen added
        assertFalse(Phone.isValidPhone("+12 34 5-6 78 90 -1 23 45 -6")); // has space added

        assertFalse(Phone.isValidPhone("phone 93121534")); // has alphabets
        assertFalse(Phone.isValidPhone("9312/1534")); // has slash
        assertFalse(Phone.isValidPhone("9312.1534")); // has period
        assertFalse(Phone.isValidPhone("9312_1534")); // has underscore
        assertFalse(Phone.isValidPhone("(9312)1534")); // has brackets

        assertFalse(Phone.isValidPhone("9312+1534")); // plus not at the start
        assertFalse(Phone.isValidPhone("++93121534")); // multiple plus
        assertFalse(Phone.isValidPhone("+-93121534")); // optional plus followed by a hyphen (not a digit)
        assertFalse(Phone.isValidPhone("+ 93121534")); // optional plus followed by a space (not a digit)
        assertFalse(Phone.isValidPhone("-93121534")); // starts with hyphen (not a digit)
        assertFalse(Phone.isValidPhone(" 93121534")); // starts with space (not a digit)

        // valid phone numbers
        assertTrue(Phone.isValidPhone("93121534")); // just digits
        assertTrue(Phone.isValidPhone("+93121534")); // with optional '+'

        // testing boundary values
        assertTrue(Phone.isValidPhone("123")); // 3 digits
        assertTrue(Phone.isValidPhone("123456789012345")); // 15 digits

        // shows that optional '+', hyphens and spaces are not counted as digits
        assertTrue(Phone.isValidPhone("+123456789012345")); // 15 digits with optional '+'
        assertTrue(Phone.isValidPhone("+12345-67890-12345")); // still is 15 digits with hyphens
        assertTrue(Phone.isValidPhone("+12 345-67 890-12 345")); // still is 15 digits with spaces
    }

    @Test
    public void equals() {
        Phone phone = new Phone("99999999");

        // same values -> returns true
        assertTrue(phone.equals(new Phone("99999999")));

        // same object -> returns true
        assertTrue(phone.equals(phone));

        // null -> returns false
        assertFalse(phone.equals(null));

        // different types -> returns false
        assertFalse(phone.equals(5.0f));

        // different values -> returns false
        assertFalse(phone.equals(new Phone("99555555")));
    }
}
