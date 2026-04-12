package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class ClearCommandParserTest {
    private ClearCommandParser parser = new ClearCommandParser();

    @Test
    public void parse_withExtraParameters_throwsParseException() {
        String argsToParse = "all";
        assertParseFailure(parser, argsToParse, ClearCommand.MESSAGE_EXTRA_PARAMETERS);
    }

    @Test
    public void parse_withWhiteSpaces_success() throws ParseException {
        String argsToParse = "   ";
        assertParseSuccess(parser, argsToParse, new ClearCommand());
    }

    @Test
    public void parse_empty_success() throws ParseException {
        String argsToParse = "";
        assertParseSuccess(parser, argsToParse, new ClearCommand());
    }
}
