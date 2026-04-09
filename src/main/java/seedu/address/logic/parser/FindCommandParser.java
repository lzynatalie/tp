package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DOCTOR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_IC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PATIENT_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PATIENT_PHONE;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.DoctorNameContainsKeywordsPredicate;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmailContainsKeywordsPredicate;
import seedu.address.model.person.IcContainsKeywordsPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * Accepts either:
     * - prefixed form: pn/KEYWORD [MORE_KEYWORDS]..., ic/IC, p/PATIENT_PHONE
     * - legacy form: KEYWORD [MORE_KEYWORDS]... (no prefix used)
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        // No text at all or only whitespace after the command word
        if (trimmedArgs.isEmpty()) {
            throw new ParseException("At least one parameter to search with must be provided. You "
                    + "can use the command 'find' with the following parameters: pn/<PATIENT_NAME>, ic/<IC>,"
                    + "p/<PATIENT_PHONE>, e/<EMAIL>, d/<DOCTOR>");
        }

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_PATIENT_NAME, PREFIX_IC, PREFIX_PATIENT_PHONE,
                        PREFIX_EMAIL, PREFIX_DOCTOR);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_PATIENT_NAME, PREFIX_IC, PREFIX_PATIENT_PHONE,
                PREFIX_EMAIL, PREFIX_DOCTOR);

        boolean hasName = argMultimap.getValue(PREFIX_PATIENT_NAME).isPresent();
        boolean hasIc = argMultimap.getValue(PREFIX_IC).isPresent();
        boolean hasPhone = argMultimap.getValue(PREFIX_PATIENT_PHONE).isPresent();
        boolean hasEmail = argMultimap.getValue(PREFIX_EMAIL).isPresent();
        boolean hasDoctor = argMultimap.getValue(PREFIX_DOCTOR).isPresent();

        // Legacy behaviour: no prefixes, treat entire args as name keywords
        if (!hasName && !hasIc && !hasPhone && !hasEmail && !hasDoctor) {
            List<String> legacyKeywords = Arrays.asList(trimmedArgs.split("\\s+"));
            String criteriaDescription = "Patient Name: " + trimmedArgs;
            return new FindCommand(new NameContainsKeywordsPredicate(legacyKeywords), criteriaDescription);
        }

        Predicate<Person> predicate = null;
        StringBuilder criteriaBuilder = new StringBuilder();

        if (argMultimap.getValue(PREFIX_PATIENT_NAME).isPresent()) {
            String nameArgs = argMultimap.getValue(PREFIX_PATIENT_NAME).get().trim();
            predicate = makePredicate(predicate, criteriaBuilder, "Patient Name: ", nameArgs,
                    arg -> new NameContainsKeywordsPredicate(Arrays.asList(arg.split("\\s+"))));
        }

        if (hasIc) {
            String icArg = argMultimap.getValue(PREFIX_IC).get().trim();
            predicate = makePredicate(predicate, criteriaBuilder, "IC Number: ", icArg,
                    arg -> person -> person.getIc().value.equalsIgnoreCase(arg));
        }

        if (hasPhone) {
            String phoneArg = argMultimap.getValue(PREFIX_PATIENT_PHONE).get().trim();
            predicate = makePredicate(predicate, criteriaBuilder, "Phone Number: ", phoneArg,
                    arg -> person -> person.getPhone().value.equals(arg));
        }

        if (hasEmail) {
            String emailArg = argMultimap.getValue(PREFIX_EMAIL).get().trim();
            predicate = makePredicate(predicate, criteriaBuilder, "Email: ", emailArg,
                    arg -> new EmailContainsKeywordsPredicate(Arrays.asList(arg.split("\\s+"))));
        }

        if (hasDoctor) {
            String doctorArg = argMultimap.getValue(PREFIX_DOCTOR).get().trim();
            predicate = makePredicate(predicate, criteriaBuilder, "Doctor Name: ", doctorArg,
                    arg -> new DoctorNameContainsKeywordsPredicate(Arrays.asList(arg.split("\\s+"))));
        }

        String criteriaDescription = criteriaBuilder.toString().trim();
        return new FindCommand(predicate, criteriaDescription);
    }

    /**
     * Ensures {@code trimmedArg} is non-empty for a prefixed field, or throws.
     */
    private String parseNonEmptyPrefixedArg(String trimmedArg) throws ParseException {
        if (trimmedArg.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
        return trimmedArg;
    }

    private static Predicate<Person> mergePredicates(Predicate<Person> current, Predicate<Person> next) {
        return current == null ? next : current.or(next);
    }

    private static void appendCriteriaSegment(StringBuilder criteriaBuilder, String label, String value) {
        if (criteriaBuilder.length() > 0) {
            criteriaBuilder.append(", ");
        }
        criteriaBuilder.append(label).append(value);
    }

    /**
     * Validates the prefixed argument, merges {@code toPredicate.apply(arg)} into {@code current},
     * appends a segment to {@code criteriaBuilder}, and returns the combined predicate.
     */
    private Predicate<Person> makePredicate(
            Predicate<Person> current,
            StringBuilder criteriaBuilder,
            String criteriaLabel,
            String trimmedArg,
            Function<String, Predicate<Person>> toPredicate) throws ParseException {
        String arg = parseNonEmptyPrefixedArg(trimmedArg);
        Predicate<Person> merged = mergePredicates(current, toPredicate.apply(arg));
        appendCriteriaSegment(criteriaBuilder, criteriaLabel, arg);
        return merged;
    }

}
