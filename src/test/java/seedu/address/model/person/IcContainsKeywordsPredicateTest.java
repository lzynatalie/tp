package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class IcContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("S1234567A");
        List<String> secondPredicateKeywordList = Arrays.asList("S1234567A", "S7654321B");

        IcContainsKeywordsPredicate firstPredicate =
                new IcContainsKeywordsPredicate(firstPredicateKeywordList);
        IcContainsKeywordsPredicate secondPredicate =
                new IcContainsKeywordsPredicate(secondPredicateKeywordList);

        assertTrue(firstPredicate.equals(firstPredicate));

        IcContainsKeywordsPredicate firstPredicateCopy =
                new IcContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        assertFalse(firstPredicate.equals(1));
        assertFalse(firstPredicate.equals(null));
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_icMatchesExactly_returnsTrue() {
        Person person = new PersonBuilder().withIc("S1234567A").build();

        IcContainsKeywordsPredicate predicate =
                new IcContainsKeywordsPredicate(Collections.singletonList("S1234567A"));
        assertTrue(predicate.test(person));

        predicate = new IcContainsKeywordsPredicate(Arrays.asList("S7654321B", "S1234567A"));
        assertTrue(predicate.test(person));

        predicate = new IcContainsKeywordsPredicate(Collections.singletonList("s1234567a"));
        assertTrue(predicate.test(person));
    }

    @Test
    public void test_icDoesNotMatch_returnsFalse() {
        Person person = new PersonBuilder().withIc("S1234567A").build();

        IcContainsKeywordsPredicate predicate =
                new IcContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(person));

        predicate = new IcContainsKeywordsPredicate(Collections.singletonList("S7654321B"));
        assertFalse(predicate.test(person));

        // Partial / substring must not match (exact match only)
        predicate = new IcContainsKeywordsPredicate(Collections.singletonList("S1234567"));
        assertFalse(predicate.test(person));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("S1234567A", "S7654321B");
        IcContainsKeywordsPredicate predicate = new IcContainsKeywordsPredicate(keywords);

        String expected = IcContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
