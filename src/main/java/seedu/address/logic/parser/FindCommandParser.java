package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DOCTOR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_IC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PATIENT_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PATIENT_PHONE;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.DoctorNameContainsKeywordsPredicate;
import seedu.address.model.person.EmailContainsKeywordsPredicate;
import seedu.address.model.person.Ic;
import seedu.address.model.person.Name;
import seedu.address.model.person.DoctorName;
import seedu.address.model.person.Email;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Prefix strings for find, longest first so {@code pn/} is checked before {@code p/}.
     */
    private static final String[] FIND_PREFIXES_LONGEST_FIRST = {"pn/", "ic/", "p/", "e/", "d/"};

    private static final Pattern PREFIX_LIKE = Pattern.compile("(^|\\s)([a-zA-Z]+)/");

    /** Unknown {@code word/} only after whitespace inside a field value (not at value start). */
    private static final Pattern PREFIX_LIKE_AFTER_SPACE = Pattern.compile("\\s([a-zA-Z]+)/");

    private static final String MESSAGE_REQUIRES_PREFIX = "Find requires at least one search prefix. "
            + "Only pn/, ic/, p/, e/, and d/ are allowed.\n"
            + FindCommand.MESSAGE_USAGE;

    private static final String MESSAGE_UNKNOWN_PREFIX = "Find only accepts these prefixes: "
            + "pn/, ic/, p/, e/, and d/.\n"
            + FindCommand.MESSAGE_USAGE;

    private static final String MESSAGE_PREAMBLE_NOT_ALLOWED = "Text before the first search prefix is not allowed. "
            + "Start with a prefix such as pn/ or ic/.\n"
            + FindCommand.MESSAGE_USAGE;

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * Each argument must use one or more of: {@code pn/}, {@code ic/}, {@code p/}, {@code e/}, {@code d/}.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException("At least one parameter to search with must be provided. You "
                    + "can use the command 'find' with the following parameters: pn/<PATIENT_NAME>, ic/<IC>,"
                    + "p/<PATIENT_PHONE>, e/<EMAIL>, d/<DOCTOR>");
        }

        assertNoUnknownPrefixTokens(args);

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

        boolean hasAnySearchField = hasName || hasIc || hasPhone || hasEmail || hasDoctor;
        String preamble = argMultimap.getPreamble().trim();

        // Handle legacy format: if no recognized prefixes, treat preamble as legacy patient name
        if (!hasAnySearchField) {
            if (preamble.isEmpty()) {
                throw new ParseException(MESSAGE_REQUIRES_PREFIX);
            }
            // Check if preamble contains valid patient names
            List<String> nameKeywords = Arrays.asList(preamble.split("\\s+"));
            boolean allValid = true;
            for (String keyword : nameKeywords) {
                if (!Name.isValidName(keyword)) {
                    allValid = false;
                    break;
                }
            }

            // If all keywords are valid, it's valid legacy format but requires prefix
            if (allValid) {
                throw new ParseException(MESSAGE_REQUIRES_PREFIX);
            }

            // If any keyword is invalid, throw NAME validation error
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }

        if (!preamble.isEmpty()) {
            throw new ParseException(MESSAGE_PREAMBLE_NOT_ALLOWED);
        }

        Predicate<Person> predicate = null;
        StringBuilder criteriaBuilder = new StringBuilder();

        if (argMultimap.getValue(PREFIX_PATIENT_NAME).isPresent()) {
            String nameArgs = argMultimap.getValue(PREFIX_PATIENT_NAME).get().trim();
            if (nameArgs.isEmpty()) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }
            assertNoUnknownPrefixTokensInValues(nameArgs);
            String normalizedNameArgs = nameArgs.replaceAll("\\s+", " ");
            ParserUtil.parseName(normalizedNameArgs);
            List<String> nameKeywords = Arrays.asList(nameArgs.split("\\s+"));
            // Validate each keyword
            for (String keyword : nameKeywords) {
                if (!Name.isValidName(keyword)) {
                    throw new ParseException(Name.MESSAGE_CONSTRAINTS);
                }
            }
            Predicate<Person> namePredicate = new NameContainsKeywordsPredicate(nameKeywords);
            predicate = namePredicate;
            criteriaBuilder.append("Patient Name: ").append(nameArgs);
        }

        if (hasIc) {
            String icArg = argMultimap.getValue(PREFIX_IC).get().trim();
            if (icArg.isEmpty()) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }
            assertNoUnknownPrefixTokensInValues(icArg);
            ParserUtil.parseIc(icArg);
            String icToMatch = icArg;
            Predicate<Person> icPredicate = person -> person.getIc().value.equalsIgnoreCase(icToMatch);
            predicate = predicate == null ? icPredicate : predicate.or(icPredicate);
            if (criteriaBuilder.length() > 0) {
                criteriaBuilder.append(", ");
            }
            criteriaBuilder.append("IC Number: ").append(icArg);
        }

        if (hasPhone) {
            String phoneArg = argMultimap.getValue(PREFIX_PATIENT_PHONE).get().trim();
            if (phoneArg.isEmpty()) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }
            assertNoUnknownPrefixTokensInValues(phoneArg);
            ParserUtil.parsePhone(phoneArg);
            String phoneToMatch = phoneArg;
            Predicate<Person> phonePredicate = person -> person.getPhone().value.equals(phoneToMatch);
            predicate = predicate == null ? phonePredicate : predicate.or(phonePredicate);
            if (criteriaBuilder.length() > 0) {
                criteriaBuilder.append(", ");
            }
            criteriaBuilder.append("Phone Number: ").append(phoneArg);
        }

        if (hasEmail) {
            String emailArg = argMultimap.getValue(PREFIX_EMAIL).get().trim();
            if (emailArg.isEmpty()) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }
            assertNoUnknownPrefixTokensInValues(emailArg);
            // Validate email format
            if (!Email.isValidEmail(emailArg)) {
                throw new ParseException(Email.MESSAGE_CONSTRAINTS);
            }
            List<String> emailKeywords = Arrays.asList(emailArg.split("\\s+"));
            for (String emailKeyword : emailKeywords) {
                ParserUtil.parseEmail(emailKeyword);
            }
            Predicate<Person> emailPredicate = new EmailContainsKeywordsPredicate(emailKeywords);
            predicate = predicate == null ? emailPredicate : predicate.or(emailPredicate);
            if (criteriaBuilder.length() > 0) {
                criteriaBuilder.append(", ");
            }
            criteriaBuilder.append("Email: ").append(emailArg);
        }

        if (hasDoctor) {
            String doctorArg = argMultimap.getValue(PREFIX_DOCTOR).get().trim();
            if (doctorArg.isEmpty()) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }
            assertNoUnknownPrefixTokensInValues(doctorArg);
            ParserUtil.parseDoctorName(doctorArg);
            List<String> doctorNameKeywords = Arrays.asList(doctorArg.split("\\s+"));
            // Validate each keyword
            for (String keyword : doctorNameKeywords) {
                if (!DoctorName.isValidDoctorName(keyword)) {
                    throw new ParseException(DoctorName.MESSAGE_CONSTRAINTS);
                }
            }
            Predicate<Person> doctorNamePredicate = new DoctorNameContainsKeywordsPredicate(doctorNameKeywords);
            predicate = predicate == null ? doctorNamePredicate : predicate.or(doctorNamePredicate);
            if (criteriaBuilder.length() > 0) {
                criteriaBuilder.append(", ");
            }
            criteriaBuilder.append("Doctor Name: ").append(doctorArg);
        }

        String criteriaDescription = criteriaBuilder.toString().trim();
        return new FindCommand(predicate, criteriaDescription);
    }

    /**
     * Rejects tokens that look like {@code word/} unless {@code word/} is a recognized find prefix.
     */
    private static void assertNoUnknownPrefixTokens(String args) throws ParseException {
        Matcher m = PREFIX_LIKE.matcher(args);
        while (m.find()) {
            int prefixStart = m.start(2);
            if (!isRecognizedFindPrefixAt(args, prefixStart)) {
                throw new ParseException(MESSAGE_UNKNOWN_PREFIX);
            }
        }
    }

    /**
     * Rejects a field value that contains {@code word/} after a space when {@code word/} is not a recognized
     * find prefix (e.g. {@code pn/Alice n/notes}).
     */
    private static void assertNoUnknownPrefixTokensInValues(String value) throws ParseException {
        Matcher m = PREFIX_LIKE_AFTER_SPACE.matcher(value);
        while (m.find()) {
            int prefixStart = m.start(1);
            if (!isRecognizedFindPrefixAt(value, prefixStart)) {
                throw new ParseException(MESSAGE_UNKNOWN_PREFIX);
            }
        }
    }

    private static boolean isRecognizedFindPrefixAt(String s, int start) {
        for (String prefix : FIND_PREFIXES_LONGEST_FIRST) {
            if (start + prefix.length() <= s.length()
                    && s.regionMatches(true, start, prefix, 0, prefix.length())) {
                return true;
            }
        }
        return false;
    }

}
