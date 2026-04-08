package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.SingleUpdateCommand.UpdatePersonDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.UpdatePersonDescriptorBuilder;

public class MultipleUpdateCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_invalidIndexFilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        List<Index> indices = Arrays.asList(INDEX_FIRST_PERSON, outOfBoundIndex);
        UpdatePersonDescriptor descriptor = new UpdatePersonDescriptor();

        MultipleUpdateCommand command = new MultipleUpdateCommand(indices, descriptor);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_multiplePersons_success() {
        List<Index> indices = Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        // Use Address instead of Phone, as Address is allowed to be shared/bulk-updated
        String newAddress = "123 New Clinic St";
        UpdatePersonDescriptor descriptor = new UpdatePersonDescriptorBuilder().withAddress(newAddress).build();
        MultipleUpdateCommand command = new MultipleUpdateCommand(indices, descriptor);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());

        Person updatedFirstPerson = new PersonBuilder(firstPerson).withAddress(newAddress).build();
        Person updatedSecondPerson = new PersonBuilder(secondPerson).withAddress(newAddress).build();

        String expectedNames = updatedFirstPerson.getName() + ", " + updatedSecondPerson.getName();
        String expectedMessage = String.format(MultipleUpdateCommand.MESSAGE_UPDATE_MULTIPLE_SUCCESS, expectedNames);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, updatedFirstPerson);
        expectedModel.setPerson(secondPerson, updatedSecondPerson);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        List<Index> indices = Arrays.asList(INDEX_FIRST_PERSON);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());

        UpdatePersonDescriptor descriptor = new UpdatePersonDescriptorBuilder(secondPerson).build();
        MultipleUpdateCommand command = new MultipleUpdateCommand(indices, descriptor);

        // Ensure this matches your MultipleUpdateCommand's internal duplicate error string
        String expectedMessage = SingleUpdateCommand.MESSAGE_DUPLICATE_PERSON
                + " (Conflict detected for: " + secondPerson.getName().fullName + ")";

        assertCommandFailure(command, model, expectedMessage);
    }


    @Test
    public void equals() {
        List<Index> indices1 = Arrays.asList(INDEX_FIRST_PERSON);
        List<Index> indices2 = Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);

        UpdatePersonDescriptor descriptor1 = new UpdatePersonDescriptor(DESC_AMY);
        UpdatePersonDescriptor descriptor2 = new UpdatePersonDescriptor(DESC_BOB);

        MultipleUpdateCommand command1 = new MultipleUpdateCommand(indices1, descriptor1);
        MultipleUpdateCommand command2 = new MultipleUpdateCommand(indices2, descriptor2);

        // same object -> returns true
        assertTrue(command1.equals(command1));

        // same values -> returns true
        MultipleUpdateCommand command1Copy = new MultipleUpdateCommand(indices1, descriptor1);
        assertTrue(command1.equals(command1Copy));

        // different types -> returns false
        assertFalse(command1.equals(1));

        // null -> returns false
        assertFalse(command1.equals(null));

        // different indices -> returns false
        assertFalse(command1.equals(command2));

        // different descriptor -> returns false
        MultipleUpdateCommand command3 = new MultipleUpdateCommand(indices1, descriptor2);
        assertFalse(command1.equals(command3));
    }

    @Test
    public void toStringMethod() {
        List<Index> indices = Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        UpdatePersonDescriptor updatePersonDescriptor = new UpdatePersonDescriptor();
        MultipleUpdateCommand command = new MultipleUpdateCommand(indices, updatePersonDescriptor);

        String expected = MultipleUpdateCommand.class.getCanonicalName() + "{targetIndices=" + indices
                + ", updatePersonDescriptor=" + updatePersonDescriptor + "}";
        assertEquals(expected, command.toString());
    }

    @Test
    public void execute_bulkIcUpdate_throwsCommandException() {
        List<Index> indices = Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        // Testing IC specifically for bulk update
        UpdatePersonDescriptor descriptor = new UpdatePersonDescriptorBuilder().withIc("S1234567A").build();
        MultipleUpdateCommand command = new MultipleUpdateCommand(indices, descriptor);

        assertCommandFailure(command, model, "IC is an unique identifier and cannot be updated in bulk. "
                + "Please update this field individually using single update.");
    }

    @Test
    public void execute_singlePersonWithIc_success() {
        // Case: Only 1 index + IC present.
        // Guard should be SKIPPED because targetIndices.size() is not > 1.
        List<Index> indices = Arrays.asList(INDEX_FIRST_PERSON);
        String newIc = "S9999999A";
        UpdatePersonDescriptor descriptor = new UpdatePersonDescriptorBuilder().withIc(newIc).build();
        MultipleUpdateCommand command = new MultipleUpdateCommand(indices, descriptor);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person updatedFirstPerson = new PersonBuilder(firstPerson).withIc(newIc).build();

        String expectedMessage = String.format(MultipleUpdateCommand.MESSAGE_UPDATE_MULTIPLE_SUCCESS,
                updatedFirstPerson.getName().fullName);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, updatedFirstPerson);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_multiplePersonsNoIc_success() {
        // Case: 2 indices + NO IC present (updating Address instead).
        // Guard should be SKIPPED because ic.isPresent() is false.
        List<Index> indices = Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        String newAddress = "456 Side Street";
        UpdatePersonDescriptor descriptor = new UpdatePersonDescriptorBuilder().withAddress(newAddress).build();
        MultipleUpdateCommand command = new MultipleUpdateCommand(indices, descriptor);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());

        Person updatedFirstPerson = new PersonBuilder(firstPerson).withAddress(newAddress).build();
        Person updatedSecondPerson = new PersonBuilder(secondPerson).withAddress(newAddress).build();

        String expectedNames = updatedFirstPerson.getName() + ", " + updatedSecondPerson.getName();
        String expectedMessage = String.format(MultipleUpdateCommand.MESSAGE_UPDATE_MULTIPLE_SUCCESS, expectedNames);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, updatedFirstPerson);
        expectedModel.setPerson(secondPerson, updatedSecondPerson);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateIndices_throwsCommandException() {
        // Path: same index used twice (e.g., update 1,1)
        List<Index> indices = Arrays.asList(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON);
        UpdatePersonDescriptor descriptor = new UpdatePersonDescriptorBuilder().withAddress("New St").build();
        MultipleUpdateCommand command = new MultipleUpdateCommand(indices, descriptor);

        assertCommandFailure(command, model, "Duplicate indices detected. "
                + "Each index should only be listed once (e.g., 'update 1,2' not 'update 1,1').");
    }

    @Test
    public void execute_conflictInModel_throwsCommandException() {
        // Path: Update Person 1 to match Person 3 (who is not in our current index list)
        List<Index> indices = Arrays.asList(INDEX_FIRST_PERSON);
        // Grab details of a third person who is NOT being updated
        Person thirdPerson = model.getFilteredPersonList().get(2);
        UpdatePersonDescriptor descriptor = new UpdatePersonDescriptorBuilder(thirdPerson).build();

        MultipleUpdateCommand command = new MultipleUpdateCommand(indices, descriptor);

        String expectedMessage = SingleUpdateCommand.MESSAGE_DUPLICATE_PERSON
                + " (Conflict detected for: " + thirdPerson.getName().fullName + ")";
        assertCommandFailure(command, model, expectedMessage);
    }


}
