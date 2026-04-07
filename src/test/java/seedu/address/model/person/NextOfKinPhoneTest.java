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
        assertFalse(Phone.isValidPhone("")); // empty string
        assertFalse(Phone.isValidPhone(" ")); // spaces only
        assertFalse(Phone.isValidPhone("1")); // 1 digit
        assertFalse(Phone.isValidPhone("1234567")); // less than 8 numbers
        assertFalse(Phone.isValidPhone("phone")); // non-numeric
        assertFalse(Phone.isValidPhone("9011p041")); // alphabets within digits
        assertFalse(Phone.isValidPhone("9312 1534")); // spaces within digits
        assertFalse(Phone.isValidPhone("123456789")); // more than 8 numbers;
        assertFalse(Phone.isValidPhone(" 12345678")); // leading white space
        assertFalse(Phone.isValidPhone("12345678 ")); // trailing white space


        // valid phone numbers
        assertTrue(Phone.isValidPhone("93121534")); // exactly 8 numbers
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
