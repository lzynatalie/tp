package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_SYMPTOM_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.SYMPTOM_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.SYMPTOM_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SYMPTOM_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SYMPTOM_HUSBAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPEND_NOTES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PATIENT_PHONE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.MultipleUpdateCommand;
import seedu.address.logic.commands.SingleUpdateCommand;
import seedu.address.logic.commands.SingleUpdateCommand.UpdatePersonDescriptor;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.symptom.Symptom;
import seedu.address.testutil.UpdatePersonDescriptorBuilder;

public class UpdateCommandParserTest {

    private static final String SYMPTOM_EMPTY = " " + CliSyntax.PREFIX_SYMPTOM;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, SingleUpdateCommand.MESSAGE_USAGE);

    private final UpdateCommandParser parser = new UpdateCommandParser();

    @Test
    public void parse_missingParts_failure() {
        String expectedMissingIndexMessage = "Missing index! Please provide an index.\n"
                + SingleUpdateCommand.MESSAGE_USAGE;

        // 1. Missing index, but a valid field is provided (e.g., " n/Amy Bee") -> Preamble is empty
        assertParseFailure(parser, NAME_DESC_AMY, expectedMissingIndexMessage);

        // 2. No fields specified
        assertParseFailure(parser, "1", SingleUpdateCommand.MESSAGE_NOT_UPDATED);

        // 3. Completely empty command
        assertParseFailure(parser, "", expectedMissingIndexMessage);

        // 4. Invalid index provided (e.g., typing raw text "Amy Bee" instead of a number)
        assertParseFailure(parser, VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        assertParseFailure(parser, "-5" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "0" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_EMAIL_DESC, Email.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_ADDRESS_DESC, Address.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_SYMPTOM_DESC, Symptom.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser, "1" + INVALID_PHONE_DESC + EMAIL_DESC_AMY, Phone.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + SYMPTOM_DESC_FRIEND + SYMPTOM_DESC_HUSBAND
                + SYMPTOM_EMPTY, Symptom.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + SYMPTOM_DESC_FRIEND + SYMPTOM_EMPTY
                + SYMPTOM_DESC_HUSBAND, Symptom.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + SYMPTOM_EMPTY + SYMPTOM_DESC_FRIEND
                + SYMPTOM_DESC_HUSBAND, Symptom.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser, "1" + INVALID_NAME_DESC + INVALID_EMAIL_DESC
                + VALID_ADDRESS_AMY + VALID_PHONE_AMY, Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + SYMPTOM_DESC_HUSBAND
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + NAME_DESC_AMY + SYMPTOM_DESC_FRIEND;

        UpdatePersonDescriptor descriptor = new UpdatePersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withSymptoms(VALID_SYMPTOM_HUSBAND, VALID_SYMPTOM_FRIEND).build();
        SingleUpdateCommand expectedCommand = new SingleUpdateCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + EMAIL_DESC_AMY;

        UpdatePersonDescriptor descriptor = new UpdatePersonDescriptorBuilder().withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_AMY).build();
        SingleUpdateCommand expectedCommand = new SingleUpdateCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        Index targetIndex = INDEX_THIRD_PERSON;
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY;
        UpdatePersonDescriptor descriptor = new UpdatePersonDescriptorBuilder().withName(VALID_NAME_AMY).build();
        SingleUpdateCommand expectedCommand = new SingleUpdateCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY;
        descriptor = new UpdatePersonDescriptorBuilder().withPhone(VALID_PHONE_AMY).build();
        expectedCommand = new SingleUpdateCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = targetIndex.getOneBased() + EMAIL_DESC_AMY;
        descriptor = new UpdatePersonDescriptorBuilder().withEmail(VALID_EMAIL_AMY).build();
        expectedCommand = new SingleUpdateCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = targetIndex.getOneBased() + ADDRESS_DESC_AMY;
        descriptor = new UpdatePersonDescriptorBuilder().withAddress(VALID_ADDRESS_AMY).build();
        expectedCommand = new SingleUpdateCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = targetIndex.getOneBased() + SYMPTOM_DESC_FRIEND;
        descriptor = new UpdatePersonDescriptorBuilder().withSymptoms(VALID_SYMPTOM_FRIEND).build();
        expectedCommand = new SingleUpdateCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + PHONE_DESC_BOB;
        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PATIENT_PHONE));

        userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + INVALID_PHONE_DESC;
        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PATIENT_PHONE));

        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY + ADDRESS_DESC_AMY + EMAIL_DESC_AMY
                + SYMPTOM_DESC_FRIEND + PHONE_DESC_AMY + ADDRESS_DESC_AMY + EMAIL_DESC_AMY + SYMPTOM_DESC_FRIEND
                + PHONE_DESC_BOB + ADDRESS_DESC_BOB + EMAIL_DESC_BOB + SYMPTOM_DESC_HUSBAND;
        assertParseFailure(parser, userInput, Messages
                .getErrorMessageForDuplicatePrefixes(PREFIX_PATIENT_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS));

        userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + INVALID_ADDRESS_DESC + INVALID_EMAIL_DESC
                + INVALID_PHONE_DESC + INVALID_ADDRESS_DESC + INVALID_EMAIL_DESC;
        assertParseFailure(parser, userInput, Messages
                .getErrorMessageForDuplicatePrefixes(PREFIX_PATIENT_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS));
    }

    @Test
    public void parse_resetSymptoms_success() {
        Index targetIndex = INDEX_THIRD_PERSON;
        String userInput = targetIndex.getOneBased() + SYMPTOM_EMPTY;

        UpdatePersonDescriptor descriptor = new UpdatePersonDescriptorBuilder().withSymptoms().build();
        SingleUpdateCommand expectedCommand = new SingleUpdateCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_notesAndAppendNotesSimultaneously_throwsParseException() {
        String expectedMessage = "You cannot overwrite a note (n/) and append to a note (an/) in the same command.";
        assertParseFailure(parser, "1 n/Fever an/Take panadol", expectedMessage);
    }

    @Test
    public void parse_emptyAppendNotes_throwsParseException() {
        String expectedMessage = "The text to append cannot be empty. If you want to clear the note, use n/ instead.";

        // FIX: Use the actual prefix constant to guarantee the tokenizer recognizes it,
        // even if your team changed the prefix string during the merge!
        assertParseFailure(parser, "1 " + PREFIX_APPEND_NOTES.getPrefix(), expectedMessage);
    }

    @Test
    public void parse_multipleIndices_returnsMultipleUpdateCommand() {
        List<Index> indices = Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        UpdatePersonDescriptor descriptor = new UpdatePersonDescriptorBuilder()
                .withPhone(VALID_PHONE_AMY).build();

        MultipleUpdateCommand expectedCommand = new MultipleUpdateCommand(indices, descriptor);

        // FIX: Remove the explicit space after 1,2.
        // PHONE_DESC_AMY already provides the required space (" p/91234567").
        // This prevents the preamble from becoming "1,2 " and triggering our strict space-blocker.
        assertParseSuccess(parser, "1,2" + PHONE_DESC_AMY, expectedCommand);
    }

    @Test
    public void parse_multipleIndicesWithSpaces_failure() {
        // CHANGE: Expect MultipleUpdateCommand.MESSAGE_USAGE because the preamble contains a comma
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, MultipleUpdateCommand.MESSAGE_USAGE);

        assertParseFailure(parser, "1, 2" + PHONE_DESC_AMY, expectedMessage);
        assertParseFailure(parser, "1 ,2" + PHONE_DESC_AMY, expectedMessage);
    }

    @Test
    public void parse_appendNotePresent_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " an/Append this";
        UpdatePersonDescriptor descriptor = new UpdatePersonDescriptorBuilder()
                .withNotesToAppend("Append this").build();

        SingleUpdateCommand expectedCommand = new SingleUpdateCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_trailingCommas_failure() {
        // CHANGE: Expect MultipleUpdateCommand.MESSAGE_USAGE because of the commas
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, MultipleUpdateCommand.MESSAGE_USAGE);

        assertParseFailure(parser, "1,2,3,,," + PHONE_DESC_AMY, expectedMessage);
    }
}
