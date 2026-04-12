package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";
    public static final String MESSAGE_EXTRA_PARAMETERS = "There should not be extra parameters.\nUsage: clear";

    private AddressBook clearedAddressBook;
    private boolean wasExecuted = false;

    @Override
    public boolean isUndoable() {
        return true;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        clearedAddressBook = new AddressBook(model.getAddressBook());
        model.setAddressBook(new AddressBook());
        wasExecuted = true;
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public void undo(Model model) throws CommandException {
        requireNonNull(model);
        if (wasExecuted && clearedAddressBook != null) {
            model.setAddressBook(new AddressBook(clearedAddressBook));
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this || other instanceof ClearCommand;
    }
}
