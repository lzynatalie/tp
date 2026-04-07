package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class AddressTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Address(null));
    }

    // Empty address
    @Test
    public void constructor_emptyAddress_throwsIllegalArgumentException() {
        String invalidAddress = "";
        // Note it throws an exception because the regex enforces that the first character must not be a whitespace,
        // so an empty string is invalid
        assertThrows(IllegalArgumentException.class, () -> new Address(invalidAddress));
    }

    // Spaces only address
    @Test
    public void constructor_whiteSpacesOnlyAddress_throwsIllegalArgumentException() {
        String invalidAddress = " ";
        // Note it throws an exception because the regex enforces that the first character must not be a whitespace,

        assertThrows(IllegalArgumentException.class, () -> new Address(invalidAddress));
    }

    // A valid address
    @Test
    public void construtor_validAddress_success() {
        String validAddress = "Blk 456, Den Road, #01-355";
        assertEquals(validAddress, new Address(validAddress).value);
    }

    // A valid address with leading and trailing whitespaces
    @Test
    public void constructor_validAddressWithLeadingAndTrailingWhitespaces_success() {
        String validAddress = "   Blk 456, Den Road, #01-355  ";
        // Note the constructor will trim it and store the trimmed version
        assertEquals("Blk 456, Den Road, #01-355", new Address(validAddress).value);
    }


    @Test
    public void isValidAddress() {
        // null address
        assertThrows(NullPointerException.class, () -> Address.isValidAddress(null));

        // valid addresses
        assertTrue(Address.isValidAddress("Blk 456, Den Road, #01-355"));
        assertTrue(Address.isValidAddress("-")); // one character

        // invalid addresses
        assertFalse(Address.isValidAddress("")); // empty string
        assertFalse(Address.isValidAddress(" ")); // spaces only


        // Special cases

        // valid address with trailing whitespaces will be True because the regex only enforces that the first character
        // must not be a whitespace.
        assertTrue(Address.isValidAddress("Blk 456, Den Road, #01-355    "));

        // valid address with leading whitespaces will be False because the regex enforces that the first
        // character must not be a whitespace.
        assertFalse(Address.isValidAddress("   Blk 456, Den Road, #01-355"));

        // valid address with leading and trailing whitespaces will be False because the regex enforces that the first
        // character must not be a whitespace.
        assertFalse(Address.isValidAddress("   Blk 456, Den Road, #01-355    "));


    }

    @Test
    public void equals() {
        Address address = new Address("Valid Address");

        // same values -> returns true
        assertTrue(address.equals(new Address("Valid Address")));

        // same object -> returns true
        assertTrue(address.equals(address));

        // null -> returns false
        assertFalse(address.equals(null));

        // different types -> returns false
        assertFalse(address.equals(5.0f));

        // different values -> returns false
        assertFalse(address.equals(new Address("Other Valid Address")));
    }
}
