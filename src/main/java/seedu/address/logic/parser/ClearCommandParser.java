package seedu.address.logic.parser;

import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parse input arguments to ensure no parameter and creates a new ClearCommand object
 */
public class ClearCommandParser implements Parser<ClearCommand> {
    @Override
    public ClearCommand parse(String arguments) throws ParseException {
        if (!arguments.trim().isEmpty()) {
            throw new ParseException(ClearCommand.MESSAGE_EXTRA_PARAMETERS);
        }
        return new ClearCommand();
    }
}
