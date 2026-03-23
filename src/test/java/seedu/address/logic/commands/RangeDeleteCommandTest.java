package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.logic.commands.CommandTestUtil.showPersonsInIndexRange;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
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
 * {@code RangeDeleteCommand}.
 */
public class RangeDeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndicesUnfilteredList_success() {
        Person firstPersonToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondPersonToDelete = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Person thirdPersonToDelete = model.getFilteredPersonList().get(INDEX_THIRD_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new RangeDeleteCommand(INDEX_FIRST_PERSON, INDEX_THIRD_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                "\n" + Messages.format(firstPersonToDelete) + "\n" + Messages.format(secondPersonToDelete)
                        + "\n" + Messages.format(thirdPersonToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(firstPersonToDelete);
        expectedModel.deletePerson(secondPersonToDelete);
        expectedModel.deletePerson(thirdPersonToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new RangeDeleteCommand(INDEX_FIRST_PERSON, outOfBoundIndex);

        Index lastIndex = Index.fromOneBased(model.getFilteredPersonList().size());
        assertCommandFailure(deleteCommand, model, Messages.getErrorMessageForInvalidIndices(lastIndex));
    }

    @Test
    public void execute_validIndicesFilteredList_success() {
        showPersonsInIndexRange(model, INDEX_FIRST_PERSON, INDEX_THIRD_PERSON);

        Person firstPersonToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondPersonToDelete = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Person thirdPersonToDelete = model.getFilteredPersonList().get(INDEX_THIRD_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new RangeDeleteCommand(INDEX_FIRST_PERSON, INDEX_THIRD_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                "\n" + Messages.format(firstPersonToDelete) + "\n" + Messages.format(secondPersonToDelete)
                        + "\n" + Messages.format(thirdPersonToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(firstPersonToDelete);
        expectedModel.deletePerson(secondPersonToDelete);
        expectedModel.deletePerson(thirdPersonToDelete);
        showNoPerson(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonsInIndexRange(model, INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);

        Index outOfBoundIndex = INDEX_THIRD_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        DeleteCommand deleteCommand = new RangeDeleteCommand(INDEX_FIRST_PERSON, outOfBoundIndex);

        Index lastIndex = Index.fromOneBased(model.getFilteredPersonList().size());
        assertCommandFailure(deleteCommand, model, Messages.getErrorMessageForInvalidIndices(lastIndex));
    }

    @Test
    public void execute_startIndexEqualsEndIndexUnfilteredList_success() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new RangeDeleteCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                "\n" + Messages.format(personToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_startIndexEqualsEndIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new RangeDeleteCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                "\n" + Messages.format(personToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        showNoPerson(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_startIndexGreaterThanEndIndexUnfilteredList_throwsCommandException() {
        DeleteCommand deleteCommand = new RangeDeleteCommand(INDEX_SECOND_PERSON, INDEX_FIRST_PERSON);

        assertCommandFailure(deleteCommand, model, Messages.getErrorMessageForInvalidRangeIndices());
    }

    @Test
    public void execute_startIndexGreaterThanEndIndexFilteredList_throwsCommandException() {
        showPersonsInIndexRange(model, INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);

        DeleteCommand deleteCommand = new RangeDeleteCommand(INDEX_SECOND_PERSON, INDEX_FIRST_PERSON);

        assertCommandFailure(deleteCommand, model, Messages.getErrorMessageForInvalidRangeIndices());
    }

    @Test
    public void execute_noPersons_throwsCommandException() {
        showNoPerson(model);

        DeleteCommand deleteCommand = new RangeDeleteCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        assertCommandFailure(deleteCommand, model, Messages.getErrorMessageForNoPersons(DeleteCommand.COMMAND_WORD));
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand =
                new RangeDeleteCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        DeleteCommand deleteSecondCommand =
                new RangeDeleteCommand(INDEX_SECOND_PERSON, INDEX_THIRD_PERSON);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy =
                new RangeDeleteCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different people -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));

        // different target indices and different command type -> returns false
        DeleteCommand deleteMultipleCommand = new MultipleDeleteCommand(INDEX_FIRST_PERSON, INDEX_THIRD_PERSON);
        assertFalse(deleteFirstCommand.equals(deleteMultipleCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteCommand deleteCommand = new RangeDeleteCommand(targetIndex, targetIndex);
        String expected = RangeDeleteCommand.class.getCanonicalName()
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
