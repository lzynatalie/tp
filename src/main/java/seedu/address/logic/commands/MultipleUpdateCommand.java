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
        List<Person> lastShownList = model.getFilteredPersonList();

        // 1. Validate indices and capture the exact Person references FIRST
        // This prevents list-shifting bugs because we grab everyone before making changes.
        List<Person> personsToUpdate = new ArrayList<>();
        for (Index index : targetIndices) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            personsToUpdate.add(lastShownList.get(index.getZeroBased()));
        }

        // 2. Pre-compute updates and check for duplicates
        // This guarantees Atomic Execution: if one fails, nothing changes.
        List<Person> updatedPersons = new ArrayList<>();
        for (Person personToUpdate : personsToUpdate) {
            Person updatedPerson = SingleUpdateCommand.createUpdatedPerson(personToUpdate, updatePersonDescriptor);

            if (!personToUpdate.isSamePerson(updatedPerson) && model.hasPerson(updatedPerson)) {
                throw new CommandException(SingleUpdateCommand.MESSAGE_DUPLICATE_PERSON
                        + " (Conflict at " + personToUpdate.getName().fullName + ")");
            }
            updatedPersons.add(updatedPerson);
        }

        // 3. Safely apply all updates now that we know no errors will be thrown
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
