package team.serenity.logic.parser;

import static team.serenity.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import team.serenity.commons.core.index.Index;
import team.serenity.commons.util.ParserUtil;
import team.serenity.logic.commands.DelQnCommand;
import team.serenity.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DelQnCommand object.
 */
public class DelQnCommandParser implements Parser<DelQnCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DelQnCommand and returns a DelQnCommand
     * object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public DelQnCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new DelQnCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DelQnCommand.MESSAGE_USAGE), pe);
        }
    }

}
