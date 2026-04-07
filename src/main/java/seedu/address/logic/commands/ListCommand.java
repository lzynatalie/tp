package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.function.Predicate;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Lists patients in the address book to the user.
 * <p>
 * Without parameters, it lists all patients.
 * When provided with parameters, it lists patients whose
 * urgency level or symptoms match any of the specified parameters.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists patients filtered by\n"
            + "  - urgency level: u/<LEVEL>...\n"
            + "  - symptoms: s/<SYMPTOM>...\n"
            + "If no parameters are provided, it lists all patients.\n"
            + "Examples:\n"
            + "  " + COMMAND_WORD + " u/high\n"
            + "  " + COMMAND_WORD + " s/fever s/cough\n"
            + "  " + COMMAND_WORD + " u/high s/fever";

    public static final String MESSAGE_SUCCESS = "There are %d patient record(s).";
    public static final String MESSAGE_SUCCESS_FILTERED = "Found %d patient(s) matching the following criteria: %s";

    public static final String MESSAGE_EMPTY_LIST = "Your patient record list is currently empty.";

    private final Predicate<Person> predicate;
    private final String criteriaDescription;

    /**
     * Creates a {@code ListCommand} that lists all patients.
     */
    public ListCommand() {
        this(PREDICATE_SHOW_ALL_PERSONS, null);
    }

    /**
     * Creates a {@code ListCommand} with the given filtering predicate.
     *
     * @param predicate filter predicate
     * @param criteriaDescription description of filtering criteria shown in the result message
     */
    public ListCommand(Predicate<Person> predicate, String criteriaDescription) {
        this.predicate = requireNonNull(predicate);
        this.criteriaDescription = criteriaDescription;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);

        int numberOfPatients = model.getFilteredPersonList().size();

        if (numberOfPatients == 0) {
            if (criteriaDescription != null) {
                throw new CommandException("No patient(s) matching the following criteria: \""
                        + criteriaDescription + "\"");
            }
            return new CommandResult(MESSAGE_EMPTY_LIST);
        }

        if (criteriaDescription == null) {
            return new CommandResult(String.format(MESSAGE_SUCCESS, numberOfPatients));
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS_FILTERED, numberOfPatients,
                criteriaDescription));
    }
}
