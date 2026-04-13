package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonsInIndexRange;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SYMPTOM;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
 * {@code MultipleDeleteCommand}.
 */
public class MultipleDeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndicesUnfilteredList_success() {
        Person firstPersonToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondPersonToDelete = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new MultipleDeleteCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                "\n" + Messages.format(firstPersonToDelete) + "\n" + Messages.format(secondPersonToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(firstPersonToDelete);
        expectedModel.deletePerson(secondPersonToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new MultipleDeleteCommand(INDEX_FIRST_PERSON, outOfBoundIndex);

        Index lastIndex = Index.fromOneBased(model.getFilteredPersonList().size());
        assertCommandFailure(deleteCommand, model, Messages.getErrorMessageForInvalidIndices(lastIndex));
    }

    @Test
    public void execute_validIndicesFilteredList_success() {
        showPersonsInIndexRange(model, INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);

        Person firstPersonToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondPersonToDelete = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new MultipleDeleteCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                "\n" + Messages.format(firstPersonToDelete) + "\n" + Messages.format(secondPersonToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(firstPersonToDelete);
        expectedModel.deletePerson(secondPersonToDelete);
        showNoPerson(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonsInIndexRange(model, INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);

        Index outOfBoundIndex = INDEX_THIRD_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        DeleteCommand deleteCommand = new MultipleDeleteCommand(INDEX_FIRST_PERSON, outOfBoundIndex);

        Index lastIndex = Index.fromOneBased(model.getFilteredPersonList().size());
        assertCommandFailure(deleteCommand, model, Messages.getErrorMessageForInvalidIndices(lastIndex));
    }

    @Test
    public void execute_duplicateIndicesUnfilteredList_throwsCommandException() {
        DeleteCommand deleteCommand = new MultipleDeleteCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON);

        assertCommandFailure(deleteCommand, model,
                Messages.getErrorMessageForDuplicateIndices(Set.of(INDEX_FIRST_PERSON)));
    }

    @Test
    public void execute_duplicateIndicesFilteredList_throwsCommandException() {
        showPersonsInIndexRange(model, INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);

        DeleteCommand deleteCommand = new MultipleDeleteCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON);

        assertCommandFailure(deleteCommand, model,
                Messages.getErrorMessageForDuplicateIndices(Set.of(INDEX_FIRST_PERSON)));
    }

    @Test
    public void execute_validPrefixUnfilteredList_success() {
        Person firstTargetPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondTargetPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        firstTargetPerson = modifyPerson(model, firstTargetPerson, true, true);
        secondTargetPerson = modifyPerson(model, secondTargetPerson, true, true);
        assertTrue(hasSymptoms(firstTargetPerson) && hasNotes(firstTargetPerson),
                "Precondition failed: target person should have symptoms and notes.");
        assertTrue(hasSymptoms(secondTargetPerson) && hasNotes(secondTargetPerson),
                "Precondition failed: target person should have symptoms and notes.");

        // delete cough symptom and notes of the target persons
        DeleteCommand deleteCommand = new MultipleDeleteCommand(new Index[]{ INDEX_FIRST_PERSON, INDEX_SECOND_PERSON },
                Map.of(PREFIX_SYMPTOM, List.of("cough"), PREFIX_NOTES, List.of()));

        Person firstExpectedPerson = new PersonBuilder(firstTargetPerson).withSymptoms().withNotes("").build();
        Person secondExpectedPerson = new PersonBuilder(secondTargetPerson).withSymptoms().withNotes("").build();
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_FIELD_SUCCESS,
                "\n" + Messages.format(firstExpectedPerson) + "\n" + Messages.format(secondExpectedPerson));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(firstTargetPerson, firstExpectedPerson);
        expectedModel.setPerson(secondTargetPerson, secondExpectedPerson);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_missingFieldValueUnfilteredList_throwsCommandException() {
        Person firstTargetPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondTargetPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        firstTargetPerson = modifyPerson(model, firstTargetPerson, true, true);
        secondTargetPerson = modifyPerson(model, secondTargetPerson, true, true);
        assertTrue(hasSymptoms(firstTargetPerson),
                "Precondition failed: target person should have symptoms.");
        assertTrue(hasSymptoms(secondTargetPerson),
                "Precondition failed: target person should have symptoms.");

        // delete fever symptom of the target persons, but symptom fields have no fever value
        DeleteCommand deleteCommand = new MultipleDeleteCommand(
                new Index[]{ INDEX_FIRST_PERSON, INDEX_SECOND_PERSON }, Map.of(PREFIX_SYMPTOM, List.of("fever")));

        assertCommandFailure(deleteCommand, model, DeleteCommand.MESSAGE_SYMPTOM_NOT_FOUND);
    }

    @Test
    public void execute_missingFieldValuesUnfilteredList_throwsCommandException() {
        Person firstTargetPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondTargetPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        firstTargetPerson = modifyPerson(model, firstTargetPerson, false, false);
        secondTargetPerson = modifyPerson(model, secondTargetPerson, false, false);
        assertTrue(!hasNotes(firstTargetPerson), "Precondition failed: target person should not have notes.");
        assertTrue(!hasNotes(secondTargetPerson), "Precondition failed: target person should not have notes.");

        // delete notes of the target persons, but notes fields have no values to delete
        DeleteCommand deleteCommand = new MultipleDeleteCommand(
                new Index[]{ INDEX_FIRST_PERSON, INDEX_SECOND_PERSON }, Map.of(PREFIX_NOTES, List.of()));

        assertCommandFailure(deleteCommand, model, DeleteCommand.MESSAGE_NOTES_NOT_FOUND);
    }

    @Test
    public void execute_validPrefixFilteredList_success() {
        showPersonsInIndexRange(model, INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);

        Person firstTargetPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondTargetPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        firstTargetPerson = modifyPerson(model, firstTargetPerson, true, true);
        secondTargetPerson = modifyPerson(model, secondTargetPerson, true, true);
        assertTrue(hasSymptoms(firstTargetPerson), "Precondition failed: target person should have symptoms.");
        assertTrue(hasSymptoms(secondTargetPerson), "Precondition failed: target person should have symptoms.");

        // delete all symptoms of target persons
        DeleteCommand deleteCommand = new MultipleDeleteCommand(
                new Index[]{ INDEX_FIRST_PERSON, INDEX_SECOND_PERSON }, Map.of(PREFIX_SYMPTOM, List.of()));

        Person firstExpectedPerson = new PersonBuilder(firstTargetPerson).withSymptoms().build();
        Person secondExpectedPerson = new PersonBuilder(secondTargetPerson).withSymptoms().build();
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_FIELD_SUCCESS,
                "\n" + Messages.format(firstExpectedPerson) + "\n" + Messages.format(secondExpectedPerson));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(firstTargetPerson, firstExpectedPerson);
        expectedModel.setPerson(secondTargetPerson, secondExpectedPerson);
        showPersonsInIndexRange(expectedModel, INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_missingFieldValueFilteredList_throwsCommandException() {
        showPersonsInIndexRange(model, INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);

        Person secondTargetPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        secondTargetPerson = modifyPerson(model, secondTargetPerson, false, true);
        assertTrue(!hasSymptoms(secondTargetPerson),
                "Precondition failed: target person should not have symptoms.");

        // delete all symptoms of target persons, but symptom field of one has no values to delete
        DeleteCommand deleteCommand = new MultipleDeleteCommand(
                new Index[]{ INDEX_FIRST_PERSON, INDEX_SECOND_PERSON }, Map.of(PREFIX_SYMPTOM, List.of()));

        assertCommandFailure(deleteCommand, model, DeleteCommand.MESSAGE_SYMPTOM_NOT_FOUND);
    }

    @Test
    public void execute_noPersons_throwsCommandException() {
        showNoPerson(model);

        DeleteCommand deleteCommand = new MultipleDeleteCommand(INDEX_FIRST_PERSON);
        assertCommandFailure(deleteCommand, model, Messages.getErrorMessageForNoPersons(DeleteCommand.COMMAND_WORD));
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new MultipleDeleteCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        DeleteCommand deleteSecondCommand = new MultipleDeleteCommand(INDEX_FIRST_PERSON, INDEX_THIRD_PERSON);
        DeleteCommand deleteFirstCommandWithPrefixes = new MultipleDeleteCommand(
                new Index[]{ INDEX_FIRST_PERSON, INDEX_SECOND_PERSON },
                Map.of(PREFIX_SYMPTOM, List.of(), PREFIX_NOTES, List.of()));

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new MultipleDeleteCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different target indices -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));

        // same target indices but different command type -> returns true
        DeleteCommand deleteRangeCommand = new RangeDeleteCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteRangeCommand));

        // different target indices and different command type -> returns false
        DeleteCommand deleteSingleCommand = new SingleDeleteCommand(INDEX_FIRST_PERSON);
        assertFalse(deleteFirstCommand.equals(deleteSingleCommand));

        // same target indices and same prefixes -> returns true
        DeleteCommand deleteFirstCommandWithSamePrefixes = new MultipleDeleteCommand(
                new Index[]{ INDEX_FIRST_PERSON, INDEX_SECOND_PERSON },
                Map.of(PREFIX_NOTES, List.of(), PREFIX_SYMPTOM, List.of()));
        assertTrue(deleteFirstCommandWithPrefixes.equals(deleteFirstCommandWithSamePrefixes));

        // same target indices but different prefixes -> returns false
        assertFalse(deleteFirstCommand.equals(deleteFirstCommandWithPrefixes));

        // same target indices and same prefixes but different command type -> returns true
        DeleteCommand deleteRangeCommandWithSamePrefixes = new RangeDeleteCommand(
                INDEX_FIRST_PERSON, INDEX_SECOND_PERSON, Map.of(PREFIX_SYMPTOM, List.of(), PREFIX_NOTES, List.of()));
        assertTrue(deleteFirstCommandWithSamePrefixes.equals(deleteRangeCommandWithSamePrefixes));
    }

    @Test
    public void toStringMethod() {
        Index firstIndex = Index.fromOneBased(1);
        Index secondIndex = Index.fromOneBased(2);
        DeleteCommand deleteCommand =
                new MultipleDeleteCommand(new Index[]{ firstIndex, secondIndex }, Map.of(PREFIX_SYMPTOM, List.of()));
        String expected = MultipleDeleteCommand.class.getCanonicalName()
                + "{targetIndices=" + List.of(firstIndex, secondIndex)
                + ", prefixes=" + List.of(Map.entry(PREFIX_SYMPTOM, List.of())) + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    @Test
    public void isUndoable_returnsTrue() {
        DeleteCommand deleteCommand = new MultipleDeleteCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        assertTrue(deleteCommand.isUndoable());
    }

    @Test
    public void undo_personsWereDeleted_successful() throws Exception {
        Model testModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Person firstPersonToDelete = testModel.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondPersonToDelete = testModel.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        int initialSize = testModel.getAddressBook().getPersonList().size();

        // Execute delete
        DeleteCommand deleteCommand = new MultipleDeleteCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        deleteCommand.execute(testModel);
        assertEquals(initialSize - 2, testModel.getAddressBook().getPersonList().size());

        // Execute undo
        deleteCommand.undo(testModel);

        // Verify the persons were restored
        assertEquals(initialSize, testModel.getAddressBook().getPersonList().size());
        assertTrue(testModel.getAddressBook().getPersonList().contains(firstPersonToDelete));
        assertTrue(testModel.getAddressBook().getPersonList().contains(secondPersonToDelete));
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
            modifiedPersonBuilder.withSymptoms("cough");
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
