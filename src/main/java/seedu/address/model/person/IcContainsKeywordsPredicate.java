package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Ic} equals one of the keywords given, ignoring case.
 * Matching is exact on the full IC string (unlike name or email predicates, which use word
 * containment).
 */
public class IcContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public IcContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        return keywords.stream()
                .anyMatch(keyword -> person.getIc().value.equalsIgnoreCase(keyword));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof IcContainsKeywordsPredicate)) {
            return false;
        }

        IcContainsKeywordsPredicate otherIcContainsKeywordsPredicate = (IcContainsKeywordsPredicate) other;
        return keywords.equals(otherIcContainsKeywordsPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
