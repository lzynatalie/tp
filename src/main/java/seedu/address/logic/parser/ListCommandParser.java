package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SYMPTOM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_URGENCY;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;

/**
 * Parses input arguments and creates a new ListCommand object
 */
public class ListCommandParser implements Parser<ListCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ListCommand
     * and returns a ListCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ListCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        // `list` with no parameters => show everyone.
        if (trimmedArgs.isEmpty()) {
            return new ListCommand();
        }

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_URGENCY, PREFIX_SYMPTOM);

        // Disallow any unprefixed input: e.g. `list fever cough` should be rejected.
        String preamble = argMultimap.getPreamble().trim();
        if (!preamble.isEmpty()) {
            throw new ParseException("Please use prefixes \"u/\" for urgency level, \"s/\" for symptoms\n"
                + "Examples: `list u/high` `list s/fever` `list u/high s/fever`.");
        }

        List<String> rawUrgencies = argMultimap.getAllValues(PREFIX_URGENCY);
        List<String> rawSymptoms = argMultimap.getAllValues(PREFIX_SYMPTOM);

        Set<String> urgencyMatches = parseUrgencySet(rawUrgencies);
        Set<String> symptomMatches = parseSymptomSet(rawSymptoms);

        // Intersection semantics: if both urgency and symptoms are provided,
        // only include persons matching both filters.
        Predicate<Person> predicate = person -> true;
        if (!urgencyMatches.isEmpty()) {
            predicate = predicate.and(person ->
                    urgencyMatches.contains(person.getUrgencyLevel().toString().toLowerCase()));
        }
        if (!symptomMatches.isEmpty()) {
            predicate = predicate.and(person -> person.getSymptoms().stream()
                    .anyMatch(symptom -> symptomMatches.contains(symptom.symptomName.toLowerCase())));
        }

        String criteriaDescription = buildCriteriaDescription(urgencyMatches, symptomMatches);
        return new ListCommand(predicate, criteriaDescription);
    }

    private static Set<String> parseUrgencySet(List<String> rawUrgencies) throws ParseException {
        LinkedHashSet<String> normalized = new LinkedHashSet<>();
        for (String urgency : rawUrgencies) {
            String trimmed = urgency.trim();
            if (trimmed.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
            }
            // Validate using existing UrgencyLevel constraints.
            normalized.add(ParserUtil.parseUrgencyLevel(trimmed).toString().toLowerCase());
        }
        return normalized;
    }

    private static Set<String> parseSymptomSet(List<String> rawSymptoms) throws ParseException {
        LinkedHashSet<String> normalized = new LinkedHashSet<>();
        for (String symptom : rawSymptoms) {
            String trimmed = symptom.trim();
            if (trimmed.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
            }
            normalized.add(ParserUtil.parseSymptom(trimmed).symptomName.toLowerCase());
        }
        return normalized;
    }

    private static String buildCriteriaDescription(Set<String> urgencyMatches, Set<String> symptomMatches) {
        boolean hasUrgency = !urgencyMatches.isEmpty();
        boolean hasSymptoms = !symptomMatches.isEmpty();

        StringBuilder builder = new StringBuilder();
        if (hasUrgency) {
            builder.append("Urgency: ").append(String.join(", ", urgencyMatches));
        }
        if (hasSymptoms) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append("Symptoms: ").append(String.join(", ", symptomMatches));
        }
        return builder.toString();
    }
}
