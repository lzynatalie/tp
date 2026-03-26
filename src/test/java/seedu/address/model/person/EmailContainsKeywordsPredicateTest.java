package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class EmailContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first@example.com");
        List<String> secondPredicateKeywordList = Arrays.asList("first@example.com", "second@example.com");

        EmailContainsKeywordsPredicate firstPredicate = new EmailContainsKeywordsPredicate(firstPredicateKeywordList);
        EmailContainsKeywordsPredicate secondPredicate = new EmailContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        EmailContainsKeywordsPredicate firstPredicateCopy =
                new EmailContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different keywords -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_emailContainsKeywords_returnsTrue() {
        PersonBuilder personBuilder = new PersonBuilder().withEmail("alice@example.com");

        EmailContainsKeywordsPredicate predicate =
                new EmailContainsKeywordsPredicate(Collections.singletonList("alice@example.com"));
        assertTrue(predicate.test(personBuilder.build()));

        predicate = new EmailContainsKeywordsPredicate(Arrays.asList("bob@example.com", "alice@example.com"));
        assertTrue(predicate.test(personBuilder.build()));

        // Mixed-case keywords are accepted
        predicate = new EmailContainsKeywordsPredicate(Collections.singletonList("ALICE@EXAMPLE.COM"));
        assertTrue(predicate.test(personBuilder.build()));
    }

    @Test
    public void test_emailDoesNotContainKeywords_returnsFalse() {
        PersonBuilder personBuilder = new PersonBuilder().withEmail("alice@example.com");

        // Zero keywords
        EmailContainsKeywordsPredicate predicate = new EmailContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(personBuilder.build()));

        // Non-matching keyword
        predicate = new EmailContainsKeywordsPredicate(Collections.singletonList("bob@example.com"));
        assertFalse(predicate.test(personBuilder.build()));

        // Keywords match name/phone/address but not email
        predicate = new EmailContainsKeywordsPredicate(Arrays.asList("Alice", "85355255", "gym"));
        assertFalse(predicate.test(new PersonBuilder().withEmail("alice@example.com").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        EmailContainsKeywordsPredicate predicate = new EmailContainsKeywordsPredicate(keywords);

        String expected = EmailContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}

