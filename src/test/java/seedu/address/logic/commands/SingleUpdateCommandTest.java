package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SYMPTOM_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.SingleUpdateCommand.UpdatePersonDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Notes;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.UpdatePersonDescriptorBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for SingleUpdateCommand.
 */
public class SingleUpdateCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Person editedPerson = new PersonBuilder().build();
        UpdatePersonDescriptor descriptor = new UpdatePersonDescriptorBuilder(editedPerson).build();
        SingleUpdateCommand updateCommand = new SingleUpdateCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(SingleUpdateCommand
                .MESSAGE_UPDATE_PERSON_SUCCESS, Messages.format(editedPerson), descriptor.getModifiedFields());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(updateCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withSymptoms(VALID_SYMPTOM_HUSBAND).build();

        UpdatePersonDescriptor descriptor = new UpdatePersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withSymptoms(VALID_SYMPTOM_HUSBAND).build();
        SingleUpdateCommand updateCommand = new SingleUpdateCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(SingleUpdateCommand
                .MESSAGE_UPDATE_PERSON_SUCCESS, Messages.format(editedPerson), descriptor.getModifiedFields());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(updateCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        UpdatePersonDescriptor descriptor = new UpdatePersonDescriptor();
        SingleUpdateCommand updateCommand = new SingleUpdateCommand(INDEX_FIRST_PERSON, descriptor);
        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(SingleUpdateCommand
                .MESSAGE_UPDATE_PERSON_SUCCESS, Messages.format(editedPerson), descriptor.getModifiedFields());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(updateCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personInFilteredList).withName(VALID_NAME_BOB).build();

        UpdatePersonDescriptor descriptor = new UpdatePersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        SingleUpdateCommand updateCommand = new SingleUpdateCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(SingleUpdateCommand
                .MESSAGE_UPDATE_PERSON_SUCCESS, Messages.format(editedPerson), descriptor.getModifiedFields());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(updateCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        UpdatePersonDescriptor descriptor = new UpdatePersonDescriptorBuilder(firstPerson).build();
        SingleUpdateCommand updateCommand = new SingleUpdateCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(updateCommand, model, SingleUpdateCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit person in filtered list into a duplicate in address book
        Person personInList = model.getAddressBook().getPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        SingleUpdateCommand updateCommand = new SingleUpdateCommand(INDEX_FIRST_PERSON,
                new UpdatePersonDescriptorBuilder(personInList).build());

        assertCommandFailure(updateCommand, model, SingleUpdateCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        UpdatePersonDescriptor descriptor = new UpdatePersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        SingleUpdateCommand updateCommand = new SingleUpdateCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(updateCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;

        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        SingleUpdateCommand updateCommand = new SingleUpdateCommand(outOfBoundIndex,
                new UpdatePersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(updateCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_appendNoteWithTimestamp_success() {
        Person personToUpdate = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String textToAppend = "Patient is recovering well.";
        UpdatePersonDescriptor descriptor = new UpdatePersonDescriptor();
        descriptor.setNotesToAppend(new Notes(textToAppend));

        SingleUpdateCommand updateCommand = new SingleUpdateCommand(INDEX_FIRST_PERSON, descriptor);

        // Generate the expected timestamp format
        String timestamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd MMM HH:mm"));
        String formattedAppend = "[" + timestamp + "] " + textToAppend;

        String existingNotes = personToUpdate.getNotes().toString();
        String expectedNoteText = existingNotes.isEmpty() ? formattedAppend : existingNotes + "\n" + formattedAppend;

        Person editedPerson = new PersonBuilder(personToUpdate)
                .withNotes(expectedNoteText).build();
        String expectedMessage = String.format(SingleUpdateCommand
                .MESSAGE_UPDATE_PERSON_SUCCESS, Messages.format(editedPerson), descriptor.getModifiedFields());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToUpdate, editedPerson);

        assertCommandSuccess(updateCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_appendNonEmptyNote_success() throws Exception {
        UpdatePersonDescriptor descriptor = new UpdatePersonDescriptor();
        descriptor.setNotesToAppend(new Notes("Patient is recovering well."));

        SingleUpdateCommand updateCommand = new SingleUpdateCommand(INDEX_FIRST_PERSON, descriptor);
        updateCommand.execute(model);

        String updatedNote = model.getFilteredPersonList().get(0).getNotes().toString();
        assertTrue(updatedNote.contains("Patient is recovering well."));
    }

    @Test
    public void equals() {
        final SingleUpdateCommand standardCommand = new SingleUpdateCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        UpdatePersonDescriptor copyDescriptor = new UpdatePersonDescriptor(DESC_AMY);
        SingleUpdateCommand commandWithSameValues = new SingleUpdateCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertEquals(standardCommand, commandWithSameValues);

        // same object -> returns true
        assertEquals(standardCommand, standardCommand);

        // null -> returns false
        assertNotEquals(null, standardCommand);

        // different types -> returns false
        assertNotEquals(standardCommand, new ClearCommand());

        // different index -> returns false
        assertNotEquals(standardCommand, new SingleUpdateCommand(INDEX_SECOND_PERSON, DESC_AMY));

        // different descriptor -> returns false
        assertNotEquals(standardCommand, new SingleUpdateCommand(INDEX_FIRST_PERSON, DESC_BOB));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        UpdatePersonDescriptor updatePersonDescriptor = new UpdatePersonDescriptor();
        SingleUpdateCommand updateCommand = new SingleUpdateCommand(index, updatePersonDescriptor);

        String expected = SingleUpdateCommand.class.getCanonicalName() + "{index=" + index + ", editPersonDescriptor="
                + updatePersonDescriptor + "}";
        assertEquals(expected, updateCommand.toString());
    }
}
