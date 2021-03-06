package team.serenity.logic.parser;

import static team.serenity.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static team.serenity.logic.parser.CliSyntax.PREFIX_GRP;

import java.util.stream.Stream;

import team.serenity.logic.commands.DelGrpCommand;
import team.serenity.logic.parser.exceptions.ParseException;
import team.serenity.model.group.GroupContainsKeywordPredicate;
import team.serenity.model.group.GroupName;

/**
 * Parses input arguments and creates a new DelGrpCommand object.
 */
public class DelGrpCommandParser implements Parser<DelGrpCommand> {

    private final ParseException delGrpCommandParserException = new ParseException(
        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DelGrpCommand.MESSAGE_USAGE));

    @Override
    public DelGrpCommand parse(String args) throws ParseException {

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_GRP);

        if (!arePrefixesPresent(argMultimap, PREFIX_GRP) || !argMultimap.getPreamble().isEmpty()) {
            throw this.delGrpCommandParserException;
        }

        GroupName groupName = SerenityParserUtil.parseGroupName(argMultimap.getValue(PREFIX_GRP).get());

        return new DelGrpCommand(new GroupContainsKeywordPredicate(groupName.toString().toUpperCase()));
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given {@code
     * ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
