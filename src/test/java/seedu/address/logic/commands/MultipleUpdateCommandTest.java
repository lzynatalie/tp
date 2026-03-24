package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.SingleUpdateCommand.UpdatePersonDescriptor;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class MultipleUpdateCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_invalidIndexFilteredList_failure() {
        // Create an out-of-bounds index to test the atomic execution block
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        // Pass one valid index and one invalid index
        List<Index> indices = Arrays.asList(INDEX_FIRST_PERSON, outOfBoundIndex);
        UpdatePersonDescriptor descriptor = new UpdatePersonDescriptor();

        MultipleUpdateCommand command = new MultipleUpdateCommand(indices, descriptor);

        // The entire command should fail safely and throw the invalid index message
        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
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

        // Note: Check your MultipleUpdateCommand.java's toString() method.
        // If your variables are named differently there, adjust "targetIndices=" or "updatePersonDescriptor=" below.
        String expected = MultipleUpdateCommand.class.getCanonicalName() + "{targetIndices=" + indices
                + ", updatePersonDescriptor=" + updatePersonDescriptor + "}";
        assertEquals(expected, command.toString());
    }
}
