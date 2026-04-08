package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPEND_NOTES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DOCTOR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_IC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEXT_OF_KIN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEXT_OF_KIN_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEXT_OF_KIN_RELATIONSHIP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PATIENT_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PATIENT_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SYMPTOM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_URGENCY;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.SingleUpdateCommand.UpdatePersonDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Updates the details of multiple existing persons in the address book.
 */
public class MultipleUpdateCommand extends Command {

    public static final String COMMAND_WORD = "update"; // Matches SingleUpdateCommand

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Updates the details of multiple persons identified "
            + "by the index numbers used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: <INDEX1>,<INDEX2>,... (must be positive integers separated by commas without spaces) "
            + "[" + PREFIX_PATIENT_NAME + "<PATIENT_NAME>] "
            + "[" + PREFIX_PATIENT_PHONE + "<PATIENT_PHONE>] "
            + "[" + PREFIX_EMAIL + "<EMAIL>] "
            + "[" + PREFIX_ADDRESS + "<ADDRESS>] "
            + "[" + PREFIX_IC + "<IC>] "
            + "[" + PREFIX_URGENCY + "<LEVEL>] "
            + "[" + PREFIX_NEXT_OF_KIN + "<NEXT_OF_KIN_NAME>] "
            + "[" + PREFIX_NEXT_OF_KIN_PHONE + "<NEXT_OF_KIN_PHONE>] "
            + "[" + PREFIX_NEXT_OF_KIN_RELATIONSHIP + "<NEXT_OF_KIN_RELATIONSHIP>] "
            + "[" + PREFIX_DOCTOR + "<DOCTOR>] "
            + "[" + PREFIX_SYMPTOM + "<SYMPTOM>] "
            + "[" + PREFIX_NOTES + "<NOTES>] [" + PREFIX_APPEND_NOTES + "<APPEND_NOTES>]...\n"
            + "Example: " + COMMAND_WORD + " 1,2 "
            + PREFIX_PATIENT_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_NOT_UPDATED = "At least one field to update must be provided.";

    public static final String MESSAGE_UPDATE_MULTIPLE_SUCCESS = "Successfully updated: %1$s";

    private final List<Index> targetIndices;
    private final UpdatePersonDescriptor updatePersonDescriptor;

    /**
     * @param targetIndices of the persons in the filtered person list to edit
     * @param updatePersonDescriptor details to edit the persons with
     */
    public MultipleUpdateCommand(List<Index> targetIndices, UpdatePersonDescriptor updatePersonDescriptor) {
        requireNonNull(targetIndices);
        requireNonNull(updatePersonDescriptor);

        this.targetIndices = targetIndices;
        this.updatePersonDescriptor = new UpdatePersonDescriptor(updatePersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // 1. Check for duplicate indices in the command
        // Using distinct() to see if the count of unique indices matches the total count
        long uniqueIndexCount = targetIndices.stream().distinct().count();
        if (uniqueIndexCount < targetIndices.size()) {
            throw new CommandException("Duplicate indices detected. "
                    + "Each index should only be listed once (e.g., 'update 1,2' not 'update 1,1').");
        }

        // 2. Block unique identity updates in bulk
        if (targetIndices.size() > 1 && updatePersonDescriptor.getIc().isPresent()) {
            throw new CommandException("IC is an unique identifier and cannot be updated in bulk. "
                    + "Please update this field individually using single update.");
        }

        List<Person> lastShownList = model.getFilteredPersonList();

        // 3. Validate indices and capture the exact Person references
        List<Person> personsToUpdate = new ArrayList<>();
        for (Index index : targetIndices) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            personsToUpdate.add(lastShownList.get(index.getZeroBased()));
        }

        // 4. Pre-compute updates and check for internal/external duplicates (Atomic Check)
        List<Person> updatedPersons = new ArrayList<>();
        for (Person personToUpdate : personsToUpdate) {
            Person updatedPerson = SingleUpdateCommand.createUpdatedPerson(personToUpdate, updatePersonDescriptor);

            // Check if it conflicts with the existing address book
            boolean existsInModel = !personToUpdate.isSamePerson(updatedPerson) && model.hasPerson(updatedPerson);

            // Check if it conflicts with someone else we are currently updating in this command
            boolean existsInCurrentBatch = updatedPersons.stream().anyMatch(p -> p.isSamePerson(updatedPerson));

            if (existsInModel || existsInCurrentBatch) {
                throw new CommandException(SingleUpdateCommand.MESSAGE_DUPLICATE_PERSON
                        + " (Conflict detected for: " + updatedPerson.getName().fullName + ")");
            }
            updatedPersons.add(updatedPerson);
        }

        // 5. Final Execution: Safely apply changes
        StringBuilder updatedNames = new StringBuilder();
        for (int i = 0; i < personsToUpdate.size(); i++) {
            model.setPerson(personsToUpdate.get(i), updatedPersons.get(i));
            updatedNames.append(updatedPersons.get(i).getName().fullName).append(", ");
        }

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        String finalNames = updatedNames.substring(0, updatedNames.length() - 2);
        return new CommandResult(String.format(MESSAGE_UPDATE_MULTIPLE_SUCCESS, finalNames));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof MultipleUpdateCommand otherCommand)) {
            return false;
        }
        return targetIndices.equals(otherCommand.targetIndices)
                && updatePersonDescriptor.equals(otherCommand.updatePersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndices", targetIndices)
                .add("updatePersonDescriptor", updatePersonDescriptor)
                .toString();
    }
}
