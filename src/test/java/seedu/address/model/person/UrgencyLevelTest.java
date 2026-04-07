package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class UrgencyLevelTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new UrgencyLevel(null));
    }

    @Test
    public void constructor_emptyUrgencyLevel_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new UrgencyLevel(""));
    }

    @Test
    public void constructor_whiteSpacesOnlyUrgencyLevel_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new UrgencyLevel("   "));
    }

    @Test
    public void constructor_validUrgencyLevel_throwsIllegalArgumentException() {
        assertEquals(UrgencyLevel.Level.LOW, new UrgencyLevel("low").level);
    }

    @Test
    public void constructor_validUrgencyLevelWithLeadingAndTrailingWhiteSpaces_throwsIllegalArgumentException() {
        assertEquals(UrgencyLevel.Level.LOW, new UrgencyLevel("  low  ").level);
    }

    @Test
    public void isValidUrgencyLevel() {
        // valid urgency levels
        assertTrue(UrgencyLevel.isValidUrgencyLevel("low"));
        assertTrue(UrgencyLevel.isValidUrgencyLevel("HIGH"));

        // invalid urgency levels
        assertFalse(UrgencyLevel.isValidUrgencyLevel("urgent"));
        assertFalse(UrgencyLevel.isValidUrgencyLevel("123"));
    }

    @Test
    public void getStyleClass() {
        // Ensures the style class matches the CSS requirements with the prefix
        assertEquals("urgency-low", new UrgencyLevel("low").getStyleClass());
        assertEquals("urgency-extreme", new UrgencyLevel("EXTREME").getStyleClass());
    }

    @Test
    public void compareTo() {
        UrgencyLevel low = new UrgencyLevel("low");
        UrgencyLevel extreme = new UrgencyLevel("extreme");

        // Extreme (4) should be "less than" Low (1) for descending sort order (triage)
        assertTrue(extreme.compareTo(low) < 0);
        assertTrue(low.compareTo(extreme) > 0);
        assertEquals(0, low.compareTo(new UrgencyLevel("low")));
    }

    @Test
    public void equals() {
        UrgencyLevel low = new UrgencyLevel("low");

        // same object
        assertTrue(low.equals(low));

        // same values
        assertTrue(low.equals(new UrgencyLevel("low")));

        // case-insensitive
        assertTrue(low.equals(new UrgencyLevel("LOW")));

        // different values
        assertFalse(low.equals(new UrgencyLevel("high")));

        // null -> returns false
        assertFalse(low.equals(null));

        // different types -> returns false
        assertFalse(low.equals(5.0f));

    }

    @Test
    public void toStringMethod() {
        assertEquals("LOW", new UrgencyLevel("low").toString());
    }
}
