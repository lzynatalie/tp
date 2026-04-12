package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_MISSING_PERSON_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PATIENT_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SYMPTOM;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.MultipleDeleteCommand;
import seedu.address.logic.commands.RangeDeleteCommand;
import seedu.address.logic.commands.SingleDeleteCommand;
import seedu.address.model.symptom.Symptom;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class DeleteCommandParserTest {

    private DeleteCommandParser parser = new DeleteCommandParser();

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "", String.format(
                MESSAGE_MISSING_PERSON_INDEX, DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgsSingleIndex_returnsDeleteCommand() {
        assertParseSuccess(parser, "1", new SingleDeleteCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_validArgsSingleIndexWithWhitespace_returnsDeleteCommand() {
        assertParseSuccess(parser, "  1  ", new SingleDeleteCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidArgsSingleIndex_throwsParseException() {
        assertParseFailure(parser, "a", String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, SingleDeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgsSingleIndexZero_throwsParseException() {
        assertParseFailure(parser, "0", String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, SingleDeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgsNegativeIndex_throwsParseException() {
        assertParseFailure(parser, "-1", String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, SingleDeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgsMultipleIndices_returnsDeleteCommand() {
        assertParseSuccess(parser, "1,2,3",
                new MultipleDeleteCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON, INDEX_THIRD_PERSON));
    }

    @Test
    public void parse_validArgsMultipleIndicesWithWhitespace_returnsDeleteCommand() {
        assertParseSuccess(parser, "  1,2,3  ",
                new MultipleDeleteCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON, INDEX_THIRD_PERSON));
    }

    @Test
    public void parse_invalidArgsMultipleIndices_throwsParseException() {
        assertParseFailure(parser, "1,a,3", String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, MultipleDeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgsMultipleIndicesZero_throwsParseException() {
        assertParseFailure(parser, "0,1,2", String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, MultipleDeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgsMultipleIndicesWithSpaceAroundDelimiter_throwsParseException() {
        assertParseFailure(parser, "1 , 2 , 3", String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, MultipleDeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgsRangeIndices_returnsDeleteCommand() {
        assertParseSuccess(parser, "1-3",
                new RangeDeleteCommand(INDEX_FIRST_PERSON, INDEX_THIRD_PERSON));
    }

    @Test
    public void parse_validArgsRangeIndicesWithWhitespace_returnsDeleteCommand() {
        assertParseSuccess(parser, "  1-3  ",
                new RangeDeleteCommand(INDEX_FIRST_PERSON, INDEX_THIRD_PERSON));
    }

    @Test
    public void parse_invalidArgsRangeIndices_throwsParseException() {
        assertParseFailure(parser, "1-a", String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, RangeDeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgsRangeIndicesZero_throwsParseException() {
        assertParseFailure(parser, "0-2", String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, RangeDeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgsRangeIndicesWithSpaceAroundDelimiter_throwsParseException() {
        assertParseFailure(parser, "1 - 3", String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, RangeDeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgsMultipleIndicesAndRangeIndices_throwsParseException() {
        assertParseFailure(parser, "1,2-3", String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, MultipleDeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgsSingleIndexWithPrefixes_returnsDeleteCommand() {
        assertParseSuccess(parser, "1 n/ s/", new SingleDeleteCommand(INDEX_FIRST_PERSON,
                Map.of(new Prefix("n/"), List.of(), new Prefix("s/"), List.of())));
    }

    @Test
    public void parse_validArgsMultipleIndicesWithPrefix_returnsDeleteCommand() {
        assertParseSuccess(parser, "1,2 s/", new MultipleDeleteCommand(
                new Index[]{ INDEX_FIRST_PERSON, INDEX_SECOND_PERSON }, Map.of(new Prefix("s/"), List.of())));
    }

    @Test
    public void parse_validArgsRangeIndicesWithPrefix_returnsDeleteCommand() {
        assertParseSuccess(parser, "1-2 n/", new RangeDeleteCommand(
                INDEX_FIRST_PERSON, INDEX_SECOND_PERSON, Map.of(new Prefix("n/"), List.of())));
    }

    @Test
    public void parse_invalidArgsNotesPrefixWithValue_throwsParseException() {
        assertParseFailure(parser, "1 n/notes",
                String.format(DeleteCommand.MESSAGE_VALUE_NOT_ALLOWED, PREFIX_NOTES));
    }

    @Test
    public void parse_validArgsSymptomsPrefixWithValue_returnsDeleteCommand() {
        assertParseSuccess(parser, "1 s/symptom",
                new SingleDeleteCommand(INDEX_FIRST_PERSON, Map.of(new Prefix("s/"), List.of("symptom"))));
    }

    @Test
    public void parse_invalidArgsSymptomsPrefixWithInvalidValue_throwsParseException() {
        assertParseFailure(parser, "1 s/#symptom", Symptom.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_validArgsSymptomsPrefixesWithValue_returnsDeleteCommand() {
        assertParseSuccess(parser, "1 s/fever s/cough",
                new SingleDeleteCommand(INDEX_FIRST_PERSON, Map.of(new Prefix("s/"), List.of("fever", "cough"))));
    }

    @Test
    public void parse_invalidArgsDuplicateNotesPrefix_throwsParseException() {
        assertParseFailure(parser, "1 n/ n/", String.format(
                DeleteCommand.MESSAGE_DUPLICATE_PREFIXES, PREFIX_NOTES));
    }

    @Test
    public void parse_invalidArgsDuplicateSymptomsPrefix_throwsParseException() {
        assertParseFailure(parser, "1 s/ s/", String.format(
                DeleteCommand.MESSAGE_DUPLICATE_PREFIXES, PREFIX_SYMPTOM));
    }

    @Test
    public void parse_invalidArgsMissingValueSymptomsPrefix_throwsParseException() {
        assertParseFailure(parser, "1 s/fever s/", String.format(
                DeleteCommand.MESSAGE_VALUE_MISSING, PREFIX_SYMPTOM));
    }

    @Test
    public void parse_invalidArgsNonOptionalFieldPrefix_throwsParseException() {
        assertParseFailure(parser, "1 p/", String.format(
                DeleteCommand.MESSAGE_NON_OPTIONAL_FIELD_PREFIXES, PREFIX_PATIENT_PHONE));
    }

    @Test
    public void parse_invalidArgsNonOptionalFieldPrefixes_throwsParseException() {
        assertParseFailure(parser, "1 p/ e/", String.format(
                DeleteCommand.MESSAGE_NON_OPTIONAL_FIELD_PREFIXES, PREFIX_PATIENT_PHONE + ", " + PREFIX_EMAIL));
    }

    @Test
    public void parse_invalidArgsNonExistentFieldPrefix_throwsParseException() {
        assertParseFailure(parser, "1 x/", String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, SingleDeleteCommand.MESSAGE_USAGE));
    }
}
