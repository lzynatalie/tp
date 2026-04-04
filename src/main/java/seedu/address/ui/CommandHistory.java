package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the history of commands entered by the user. It allows adding new commands to the history and navigating
 * through the history using the up and down arrow keys.
 */
public class CommandHistory {

    private final List<String> commandHistory = new ArrayList<>();
    private int historyIndex = 0;
    private String currentInput = "";

    public CommandHistory() {

    }

    /**
     * Adds a command to the history. If the command is a duplicate of an existing command, the existing command
     * is removed and the new command is added to the end of the history.
     */
    public void addCommand(String command) {
        assert command != null;
        String trimmedCommand = command.trim();

        if (trimmedCommand.isEmpty()) {
            return;
        }

        removeAnyDuplicateCommands(trimmedCommand);

        commandHistory.add(trimmedCommand);
        historyIndex = commandHistory.size();
        currentInput = "";
    }

    private void removeAnyDuplicateCommands(String command) {
        commandHistory.removeIf(c -> c.equalsIgnoreCase(command));
    }

    /**
     * Returns the previous command in the history. If there is no previous command, returns the current input.
     */
    public String getPreviousCommand(String currentInput) {
        assert currentInput != null;
        if (commandHistory.isEmpty()) {
            return currentInput;
        }

        // set current input
        if (historyIndex == commandHistory.size()) {
            this.currentInput = currentInput;
        }

        if (historyIndex > 0) {
            historyIndex -= 1;
        }

        return commandHistory.get(historyIndex);
    }

    /**
     * Returns the next command in the history. If there is no next command, returns the current input.
     */
    public String getNextCommand() {
        if (commandHistory.isEmpty()) {
            return currentInput;
        }

        if (historyIndex < commandHistory.size()) {
            historyIndex += 1;
        }

        if (historyIndex == commandHistory.size()) {
            return currentInput;
        } else {
            return commandHistory.get(historyIndex);
        }
    }

}
