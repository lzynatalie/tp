package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

public class MultipleDeleteCommand extends DeleteCommand {

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the people identified by the index numbers used in the displayed person list.\n"
            + "Parameters: INDEX,INDEX[,INDEX,...] (must be positive integers)\n"
            + "Example: " + COMMAND_WORD + " 1,2,4";

    private final Index[] targetIndices;

    public MultipleDeleteCommand(Index[] targetIndices) {
        this.targetIndices = targetIndices;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        Person[] personsToDelete = new Person[targetIndices.length];
        for (int i = 0; i < targetIndices.length; i++) {
            Index targetIndex = targetIndices[i];
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_INDICES);
            }

            Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
            personsToDelete[i] = personToDelete;
        }

        StringBuilder deletedPersonsString = new StringBuilder();
        for (Person person : personsToDelete) {
            model.deletePerson(person);
            deletedPersonsString.append("\n" + Messages.format(person));
        }
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, deletedPersonsString));
    }

    @Override
    public Set<Index> getTargetIndicesAsSet() {
        return Set.of(targetIndices);
    }
}
