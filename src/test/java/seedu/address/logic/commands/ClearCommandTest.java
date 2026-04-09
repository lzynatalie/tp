package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void isUndoable_returnsTrue() {
        ClearCommand clearCommand = new ClearCommand();
        assertTrue(clearCommand.isUndoable());
    }

    @Test
    public void execute_emptyAddressBook_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyAddressBook_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setAddressBook(new AddressBook());

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void undo_afterExecute_restoresAddressBook() throws CommandException {
        AddressBook typicalAddressBook = getTypicalAddressBook();
        Model model = new ModelManager(typicalAddressBook, new UserPrefs());
        ClearCommand clearCommand = new ClearCommand();

        // Execute clear
        clearCommand.execute(model);
        assertTrue(model.getAddressBook().getPersonList().isEmpty(), "Address book should be empty after clear");

        // Undo clear
        clearCommand.undo(model);
        assertFalse(model.getAddressBook().getPersonList().isEmpty(), "Address book should be restored after undo");
        assertTrue(model.getAddressBook().getPersonList().size() == typicalAddressBook.getPersonList().size(),
                "Address book should have same size as before clear");
    }

    @Test
    public void undo_withoutExecute_doesNothing() throws CommandException {
        AddressBook typicalAddressBook = getTypicalAddressBook();
        Model model = new ModelManager(typicalAddressBook, new UserPrefs());
        ClearCommand clearCommand = new ClearCommand();

        // Call undo without execute
        clearCommand.undo(model);
        assertFalse(model.getAddressBook().getPersonList().isEmpty(), "Address book should remain unchanged");
        assertTrue(model.getAddressBook().getPersonList().size() == typicalAddressBook.getPersonList().size(),
                "Address book size should remain the same");
    }

}
