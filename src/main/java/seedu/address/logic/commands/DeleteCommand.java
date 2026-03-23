package seedu.address.logic.commands;

import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a command that deletes one or more people from the address book.
 */
public abstract class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = "Usage:\n"
            + "        Single deletion: " + COMMAND_WORD + " INDEX\n"
            + "        Multiple deletion: " + COMMAND_WORD + " INDEX,INDEX[,INDEX,...]\n"
            + "        Range deletion: " + COMMAND_WORD + " START_INDEX-END_INDEX";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person(s): %1$s";

    public abstract Set<Index> getTargetIndicesAsSet();

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return this.getTargetIndicesAsSet().equals(otherDeleteCommand.getTargetIndicesAsSet());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndices", this.getTargetIndicesAsSet())
                .toString();
    }
}
