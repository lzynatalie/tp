package seedu.address.model.symptom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class SymptomTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Symptom(null));
    }

    @Test
    public void constructor_emptySymptomName_throwsIllegalArgumentException() {
        String invalidSymptomName = "";
        assertThrows(IllegalArgumentException.class, () -> new Symptom(invalidSymptomName));
    }

    @Test
    public void constructor_whiteSpacesOnlySymptomName_throwsIllegalArgumentException() {
        String invalidSymptomName = "   ";
        assertThrows(IllegalArgumentException.class, () -> new Symptom(invalidSymptomName));
    }

    @Test
    public void constructor_validSymptomName_throwsIllegalArgumentException() {
        String validSymptomName = "cough";
        assertEquals(validSymptomName, new Symptom(validSymptomName).symptomName);
    }

    @Test
    public void constructor_validSymptomNameWithLeadingAndTrailingWhiteSpaces_throwsIllegalArgumentException() {
        String validSymptomName = "  cough  ";
        assertEquals(validSymptomName.trim(), new Symptom(validSymptomName).symptomName);
    }


    @Test
    public void isValidSymptomName() {
        // null symptom name
        assertThrows(NullPointerException.class, () -> Symptom.isValidSymptomName(null));

        // blank symptom name
        assertFalse(Symptom.isValidSymptomName(""));
        assertFalse(Symptom.isValidSymptomName("   "));

        // invalid symptom name
        assertFalse(Symptom.isValidSymptomName(" cough")); // leading white space
        assertFalse(Symptom.isValidSymptomName("cough!")); // invalid character
        assertFalse(Symptom.isValidSymptomName("!cough")); // starts with invalid character

        // valid symptom name
        assertTrue(Symptom.isValidSymptomName("cough ")); // trailing white space
        assertTrue(Symptom.isValidSymptomName("cough123")); // with numbers at the end
        assertTrue(Symptom.isValidSymptomName("123cough")); // with numbers at the start
        assertTrue(Symptom.isValidSymptomName("123 cough")); // with internal space

    }

}
