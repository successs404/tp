package team.serenity.logic.parser;

import static team.serenity.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static team.serenity.logic.parser.CliSyntax.PREFIX_ID;
import static team.serenity.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.Optional;
import java.util.stream.Stream;

import team.serenity.commons.util.ParserUtil;
import team.serenity.logic.commands.MarkPresentCommand;
import team.serenity.logic.parser.exceptions.ParseException;
import team.serenity.model.group.Student;

/**
 * Parses input arguments and creates a new MarkPresentCommand object.
 * Current support:
 * markpresent name/NAME id/STUDENT_NUMBER
 * markpresent all
 */
public class MarkPresentCommandParser implements Parser<MarkPresentCommand> {

    public static final String MESSAGE_STUDENT_NOT_GIVEN = "Please ensure student name / id is given";

    /**
     * Parses the given {@code String} of arguments in the context of the MarkPresentCommand and
     * returns a MarkPresentCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public MarkPresentCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_ID);

        String studentName;
        String studentNumber;
        Optional<Student> student;
        Optional<String> keyToAll = Optional.ofNullable(argMultimap.getPreamble());

        if (argMultimap.getValue(PREFIX_NAME).isPresent() && argMultimap.getValue(PREFIX_ID).isPresent()) {

            // If single student specified, get student
            studentName = ParserUtil.parseStudentName(argMultimap.getValue(PREFIX_NAME).get());
            studentNumber = ParserUtil.parseStudentID(argMultimap.getValue(PREFIX_ID).get());
            student = Optional.ofNullable(new Student(studentName, studentNumber));

            return new MarkPresentCommand(student.get());

        } else if (keyToAll.get().equals("all")) {

            // mark attendance of all students
            return new MarkPresentCommand();

        } else {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkPresentCommand.MESSAGE_USAGE));
        }

    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given {@code
     * ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
