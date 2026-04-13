package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SYMPTOM;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

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
    public void execute_validPrefixesUnfilteredList_success() {
        Person targetPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        targetPerson = modifyPerson(model, targetPerson, true, true);
        assertTrue(hasSymptoms(targetPerson) && hasNotes(targetPerson),
                "Precondition failed: target person should have symptoms and notes.");

        // delete all symptoms and notes of the target person
        DeleteCommand deleteCommand = new SingleDeleteCommand(INDEX_FIRST_PERSON,
                Map.of(PREFIX_SYMPTOM, List.of(), PREFIX_NOTES, List.of()));

        Person expectedPerson = new PersonBuilder(targetPerson).withSymptoms().withNotes("").build();
        String expectedMessage = String.format(
                DeleteCommand.MESSAGE_DELETE_FIELD_SUCCESS, Messages.format(expectedPerson));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(targetPerson, expectedPerson);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_missingFieldValueUnfilteredList_throwsCommandException() {
        Person targetPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        targetPerson = modifyPerson(model, targetPerson, false, true);
        assertTrue(!hasSymptoms(targetPerson), "Precondition failed: target person should not have symptoms.");

        // delete cough symptom of the target person, but symptom field has no values to delete
        DeleteCommand deleteCommand =
                new SingleDeleteCommand(INDEX_FIRST_PERSON, Map.of(PREFIX_SYMPTOM, List.of("cough")));

        assertCommandFailure(deleteCommand, model, DeleteCommand.MESSAGE_SYMPTOM_NOT_FOUND);
    }

    @Test
    public void execute_missingFieldValuesUnfilteredList_throwsCommandException() {
        Person targetPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        targetPerson = modifyPerson(model, targetPerson, false, true);
        assertTrue(!hasSymptoms(targetPerson), "Precondition failed: target person should not have symptoms.");

        // delete all symptoms and notes of the target person, but symptom field has no values to delete
        DeleteCommand deleteCommand = new SingleDeleteCommand(INDEX_FIRST_PERSON,
                Map.of(PREFIX_SYMPTOM, List.of(), PREFIX_NOTES, List.of()));

        assertCommandFailure(deleteCommand, model, DeleteCommand.MESSAGE_SYMPTOM_NOT_FOUND);
    }

    @Test
    public void execute_validPrefixFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person targetPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        targetPerson = modifyPerson(model, targetPerson, true, true);
        assertTrue(hasSymptoms(targetPerson), "Precondition failed: target person should have symptoms.");

        // delete fever symptom of the target person
        DeleteCommand deleteCommand =
                new SingleDeleteCommand(INDEX_FIRST_PERSON, Map.of(PREFIX_SYMPTOM, List.of("fever")));

        Person expectedPerson = new PersonBuilder(targetPerson).withSymptoms("cough").build();
        String expectedMessage = String.format(
                DeleteCommand.MESSAGE_DELETE_FIELD_SUCCESS, Messages.format(expectedPerson));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(targetPerson, expectedPerson);
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_missingFieldValueFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person targetPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        targetPerson = modifyPerson(model, targetPerson, true, false);
        assertTrue(!hasNotes(targetPerson), "Precondition failed: target person should not have notes.");

        // delete notes of the target person but notes field has no value to delete
        DeleteCommand deleteCommand = new SingleDeleteCommand(INDEX_FIRST_PERSON, Map.of(PREFIX_NOTES, List.of()));

        assertCommandFailure(deleteCommand, model, DeleteCommand.MESSAGE_NOTES_NOT_FOUND);
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
        DeleteCommand deleteFirstCommandWithPrefixes = new SingleDeleteCommand(INDEX_FIRST_PERSON,
                Map.of(PREFIX_SYMPTOM, List.of("fever", "cough"), PREFIX_NOTES, List.of()));

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new SingleDeleteCommand(INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different target index -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));

        // same target index but different command type -> returns true
        DeleteCommand deleteRangeCommand = new RangeDeleteCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteRangeCommand));

        // different target indices and different command type -> returns false
        DeleteCommand deleteMultipleCommand = new MultipleDeleteCommand(INDEX_SECOND_PERSON);
        assertFalse(deleteFirstCommand.equals(deleteMultipleCommand));

        // same target index and same prefixes -> returns true
        DeleteCommand deleteFirstCommandWithSamePrefixes = new SingleDeleteCommand(INDEX_FIRST_PERSON,
                Map.of(PREFIX_NOTES, List.of(), PREFIX_SYMPTOM, List.of("cough", "fever")));
        assertTrue(deleteFirstCommandWithPrefixes.equals(deleteFirstCommandWithSamePrefixes));

        // same target index but different prefixes -> returns false
        assertFalse(deleteFirstCommand.equals(deleteFirstCommandWithPrefixes));

        // same target indices and same prefixes but different command type -> returns true
        DeleteCommand deleteMultipleCommandWithSamePrefixes =
                new MultipleDeleteCommand(new Index[]{ INDEX_FIRST_PERSON },
                        Map.of(PREFIX_SYMPTOM, List.of("fever", "cough"), PREFIX_NOTES, List.of()));
        assertTrue(deleteFirstCommandWithSamePrefixes.equals(deleteMultipleCommandWithSamePrefixes));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteCommand deleteCommand = new SingleDeleteCommand(targetIndex,
                Map.of(PREFIX_SYMPTOM, List.of("fever", "cough"), PREFIX_NOTES, List.of()));
        String expected = SingleDeleteCommand.class.getCanonicalName()
                + "{targetIndices=" + List.of(INDEX_FIRST_PERSON)
                + ", prefixes=" + List.of(
                        Map.entry(PREFIX_NOTES, List.of()),
                        Map.entry(PREFIX_SYMPTOM, List.of("fever", "cough")))
                + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    @Test
    public void isUndoable_returnsTrue() {
        DeleteCommand deleteCommand = new SingleDeleteCommand(INDEX_FIRST_PERSON);
        assertTrue(deleteCommand.isUndoable());
    }

    @Test
    public void undo_personWasDeleted_successful() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        int initialSize = model.getAddressBook().getPersonList().size();

        // Execute delete
        DeleteCommand deleteCommand = new SingleDeleteCommand(INDEX_FIRST_PERSON);
        deleteCommand.execute(model);
        assertEquals(initialSize - 1, model.getAddressBook().getPersonList().size());

        // Execute undo
        deleteCommand.undo(model);

        // Verify the person was restored
        assertEquals(initialSize, model.getAddressBook().getPersonList().size());
        assertTrue(model.getAddressBook().getPersonList().contains(personToDelete));
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }

    /**
     * Modifies the person in the model to meet the requirements and returns that person.
     */
    private Person modifyPerson(Model model, Person personToModify, boolean hasSymptoms, boolean hasNotes) {
        PersonBuilder modifiedPersonBuilder = new PersonBuilder(personToModify);

        if (hasSymptoms) {
            modifiedPersonBuilder.withSymptoms("fever", "cough");
        } else {
            modifiedPersonBuilder.withSymptoms();
        }

        if (hasNotes) {
            modifiedPersonBuilder.withNotes("Stays up late to do CS2103");
        } else {
            modifiedPersonBuilder.withNotes("");
        }

        Person modifiedPerson = modifiedPersonBuilder.build();
        model.setPerson(personToModify, modifiedPerson);

        return modifiedPerson;
    }

    private boolean hasSymptoms(Person person) {
        return !person.getSymptoms().isEmpty();
    }

    private boolean hasNotes(Person person) {
        return !person.getNotes().getValue().isEmpty();
    }
}
