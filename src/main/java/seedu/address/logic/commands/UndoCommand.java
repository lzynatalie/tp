package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Undoes the last executed command.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";

    public static final String MESSAGE_SUCCESS = "Undo successful: reversed the previous command.";
    public static final String MESSAGE_FAILURE = "Cannot undo: recent command was a bulk update, "
            + "no recent action to undo, or undo was already used.";
    public static final String MESSAGE_USAGE = "Usage: undo";
    public static final String MESSAGE_EXTRA_PARAMETERS = "There should not be extra parameters.\nUsage: undo";

    private Command lastCommand;

    /**
     * Sets the last command to be undone.
     * @param command the command to undo (may be null if no command to undo)
     */
    public void setLastCommand(Command command) {
        this.lastCommand = command;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (lastCommand == null) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        lastCommand.undo(model);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public String toString() {
        return COMMAND_WORD;
    }

    @Override
    public boolean equals(Object other) {
        return other == this || other instanceof UndoCommand;
    }
}
