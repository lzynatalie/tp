package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class DoctorNameContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("ben");
        List<String> secondPredicateKeywordList = Arrays.asList("ben", "leong");

        DoctorNameContainsKeywordsPredicate firstPredicate =
                new DoctorNameContainsKeywordsPredicate(firstPredicateKeywordList);
        DoctorNameContainsKeywordsPredicate secondPredicate =
                new DoctorNameContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        DoctorNameContainsKeywordsPredicate firstPredicateCopy =
                new DoctorNameContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different keywords -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_doctorNameContainsKeywords_returnsTrue() {
        PersonBuilder personBuilder = new PersonBuilder().withDoctorName("Ben Leong");

        // One matching keyword (single word) should match one of the name tokens
        DoctorNameContainsKeywordsPredicate predicate =
                new DoctorNameContainsKeywordsPredicate(Collections.singletonList("Ben"));
        assertTrue(predicate.test(personBuilder.build()));

        predicate = new DoctorNameContainsKeywordsPredicate(Arrays.asList("Ben", "Other"));
        assertTrue(predicate.test(personBuilder.build()));

        // Mixed-case keywords are accepted
        predicate = new DoctorNameContainsKeywordsPredicate(Collections.singletonList("lEoNg"));
        assertTrue(predicate.test(personBuilder.build()));
    }

    @Test
    public void test_doctorNameDoesNotContainKeywords_returnsFalse() {
        PersonBuilder personBuilder = new PersonBuilder().withDoctorName("Ben Leong");

        // Zero keywords
        DoctorNameContainsKeywordsPredicate predicate =
                new DoctorNameContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(personBuilder.build()));

        // Non-matching keyword
        predicate = new DoctorNameContainsKeywordsPredicate(Collections.singletonList("Doctor"));
        assertFalse(predicate.test(personBuilder.build()));

        // Keywords match other fields but not the doctor name
        predicate = new DoctorNameContainsKeywordsPredicate(Arrays.asList("alice@example.com", "85355255"));
        assertFalse(predicate.test(new PersonBuilder().withDoctorName("Ben Leong")
                .withEmail("alice@example.com")
                .withPhone("85355255")
                .build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        DoctorNameContainsKeywordsPredicate predicate = new DoctorNameContainsKeywordsPredicate(keywords);

        String expected = DoctorNameContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}

