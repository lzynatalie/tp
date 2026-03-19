package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code SingleDeleteCommand}.
 */
public class SingleDeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new SingleDeleteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(
                DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new SingleDeleteCommand(outOfBoundIndex);

        Index lastIndex = Index.fromOneBased(model.getFilteredPersonList().size());
        assertCommandFailure(deleteCommand, model, Messages.getErrorMessageForInvalidIndex(lastIndex));
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new SingleDeleteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(
                DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        showNoPerson(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        DeleteCommand deleteCommand = new SingleDeleteCommand(outOfBoundIndex);

        Index lastIndex = Index.fromOneBased(model.getFilteredPersonList().size());
        assertCommandFailure(deleteCommand, model, Messages.getErrorMessageForInvalidIndex(lastIndex));
    }

    @Test
    public void execute_noPersons_throwsCommandException() {
        showNoPerson(model);

        DeleteCommand deleteCommand = new SingleDeleteCommand(INDEX_FIRST_PERSON);
        assertCommandFailure(deleteCommand, model, Messages.getErrorMessageForNoPersons(DeleteCommand.COMMAND_WORD));
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new SingleDeleteCommand(INDEX_FIRST_PERSON);
        DeleteCommand deleteSecondCommand = new SingleDeleteCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new SingleDeleteCommand(INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));

        // same target index but different command type -> returns true
        DeleteCommand deleteRangeCommand = new RangeDeleteCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteRangeCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteCommand deleteCommand = new SingleDeleteCommand(targetIndex);
        String expected = SingleDeleteCommand.class.getCanonicalName()
                + "{targetIndices=" + Set.of(targetIndex) + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}
