package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class DoctorNameTest {

    // Constructor tests
    @Test
    public void constructor_validName_success() {
        String validName = "Dr. John O'Conner, M.D.";
        DoctorName doctorName = new DoctorName(validName);
        assertEquals("Dr. John O'Conner, M.D.", doctorName.getFullName());
    }

    // A valid name with leading and trailing whitespaces
    @Test
    public void constructor_withLeadingAndTrailingWhitespaces_success() {
        String validName = "   Dr. Jane Doe  ";
        DoctorName doctorName = new DoctorName(validName);
        assertEquals("Dr. Jane Doe", doctorName.getFullName()); // should trim whites
    }

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DoctorName(null));
    }

    @Test
    public void constructor_whiteSpacesOnlyDoctorName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new DoctorName("   "));
    }

    @Test
    public void constructor_invalidDoctorName_throwsIllegalArgumentException() {
        String invalidName = "Dr@John#Doe"; // contains invalid characters
        assertThrows(IllegalArgumentException.class, () -> new DoctorName(invalidName));
    }


    // isValidName tests
    @Test
    public void isValidName_validNames_returnsTrue() {
        assertTrue(DoctorName.isValidDoctorName("John Doe"));
        assertTrue(DoctorName.isValidDoctorName("O'Conner, Jane"));
        assertTrue(DoctorName.isValidDoctorName("Anne-Marie Smith"));
        assertTrue(DoctorName.isValidDoctorName("Dr. John A. Doe"));
        assertTrue(DoctorName.isValidDoctorName("Dr John, M.D."));
        assertTrue(DoctorName.isValidDoctorName("John Doe   ")); // trailing whitespaces should be valid
    }

    @Test
    public void isValidName_invalidNames_returnsFalse() {
        assertFalse(DoctorName.isValidDoctorName("")); // empty
        assertFalse(DoctorName.isValidDoctorName("Dr@John")); // invalid characters
        assertFalse(DoctorName.isValidDoctorName("John123")); // numbers not allowed
        assertFalse(DoctorName.isValidDoctorName("Dr#Jane!")); // special characters #!
        assertFalse(DoctorName.isValidDoctorName("   ")); // only whitespaces
        assertFalse(DoctorName.isValidDoctorName("-Dr. John")); // does not start with an alphabetic character
        assertFalse(DoctorName.isValidDoctorName("  Dr. John")); // leading whitespace should be invalid
    }

    // Equals and hashCode tests
    @Test
    public void equals_sameObject_returnsTrue() {
        DoctorName doctorName = new DoctorName("John Doe");
        assertEquals(doctorName, doctorName);
    }

    @Test
    public void equals_sameNameDifferentCase_returnsTrue() {
        DoctorName name1 = new DoctorName("John Doe");
        DoctorName name2 = new DoctorName("john doe");
        assertEquals(name1, name2);
    }

    @Test
    public void equals_differentName_returnsFalse() {
        DoctorName name1 = new DoctorName("John Doe");
        DoctorName name2 = new DoctorName("Jane Doe");
        assertNotEquals(name1, name2);
    }

    @Test
    public void equals_notDoctorName_returnsFalse() {
        DoctorName doctorName = new DoctorName("John Doe");
        assertNotEquals(doctorName, "John Doe"); // different type
    }

    @Test
    public void hashCode_sameNameDifferentCase_equalHashCode() {
        DoctorName name1 = new DoctorName("John Doe");
        DoctorName name2 = new DoctorName("john doe");
        assertEquals(name1.hashCode(), name2.hashCode());
    }

    // toString test
    @Test
    public void toString_returnsFullName() {
        DoctorName doctorName = new DoctorName("John Doe");
        assertEquals("John Doe", doctorName.toString());
    }
}
